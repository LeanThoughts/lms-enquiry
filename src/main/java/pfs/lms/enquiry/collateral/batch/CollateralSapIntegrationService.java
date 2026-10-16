package pfs.lms.enquiry.collateral.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pfs.lms.enquiry.collateral.domain.CollateralChecklist;
import pfs.lms.enquiry.collateral.domain.CollateralChildRecord;
import pfs.lms.enquiry.collateral.domain.CollateralItem;
import pfs.lms.enquiry.collateral.repository.CollateralChecklistRepository;
import pfs.lms.enquiry.collateral.repository.CollateralCoverageRepository;
import pfs.lms.enquiry.collateral.repository.CollateralDocumentRepository;
import pfs.lms.enquiry.collateral.repository.CollateralItemRepository;
import pfs.lms.enquiry.collateral.repository.CollateralPerfectionCersaiRepository;
import pfs.lms.enquiry.collateral.repository.CollateralPerfectionNeslRepository;
import pfs.lms.enquiry.collateral.repository.CollateralPerfectionRocRepository;
import pfs.lms.enquiry.collateral.repository.CollateralSecuritiesPositionRepository;
import pfs.lms.enquiry.domain.SAPIntegrationPointer;
import pfs.lms.enquiry.repository.SAPIntegrationRepository;
import pfs.lms.enquiry.resource.FileResource;
import pfs.lms.enquiry.sapintegrationservice.SapHttpLogContext;
import pfs.lms.enquiry.service.ISAPIntegrationPointerService;
import pfs.lms.enquiry.vault.FileStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Posts the collateral components of approved checklists to the SAP system of record through the gateway OData service
 * ZPFS_LMS_COLLATERAL_SRV, mirroring {@code pfs.lms.enquiry.businesspartner.batch}.
 * <p>
 * It sweeps the SAP integration pointers ({@link SAPIntegrationRepository}) for business process "Collateral
 * Management" with status 0 (Not Posted) or 2 (Posting Failed), so a failed post is retried on the next sweep. A
 * pointer is posted only when its checklist (the pointer's
 * {@code mainEntityId}) is Approved ({@link CollateralChecklist#STATUS_APPROVED}). Each pointer is set to 1 (In
 * Posting Process); the component is sent by its integration mode — create (C) as POST to the collection, change (U) as
 * PUT and delete (D) as DELETE on the keyed entity — and the pointer is moved to 3 (Posted) on a 2xx response or 2
 * (Posting Failed) otherwise.
 * <p>
 * Status: 0 Not Posted, 1 In Posting Process, 2 Posting Failed, 3 Posted Successfully.
 * <p>
 * Calls go through {@link CollateralSapGatewayClient} (same {@code sap.*} connection settings as the other batches);
 * success is decided by the HTTP status, and SAP's response body — the created entity or its error text — is logged.
 * The batch is inert until {@code sap.collateralServiceUri} is configured.
 */
@Service
public class CollateralSapIntegrationService {

    public static final String BUSINESS_PROCESS = "Collateral Management";
    public static final int STATUS_NOT_POSTED = 0;
    public static final int STATUS_IN_PROCESS = 1;
    public static final int STATUS_FAILED = 2;
    public static final int STATUS_POSTED = 3;

    /** SAP integration modes written by CollateralChangeDocumentService. */
    private static final String MODE_CREATE = "C";
    private static final String MODE_UPDATE = "U";
    private static final String MODE_DELETE = "D";

    private static final Logger log = LoggerFactory.getLogger(CollateralSapIntegrationService.class);

    private final SAPIntegrationRepository sapIntegrationRepository;
    private final ISAPIntegrationPointerService sapIntegrationPointerService;
    private final CollateralSapGatewayClient gateway;
    private final FileStorage fileStorage;
    private final CollateralChecklistRepository checklistRepository;
    private final CollateralSapPayloadMapper payloadMapper;
    private final Map<CollateralSapEntitySet, JpaRepository<?, UUID>> repositories =
            new EnumMap<>(CollateralSapEntitySet.class);

    /** Path of the gateway service from the SAP host, e.g. /sap/opu/odata/sap/ZPFS_LMS_COLLATERAL_SRV */
    private final String servicePath;

    public CollateralSapIntegrationService(SAPIntegrationRepository sapIntegrationRepository,
                                           ISAPIntegrationPointerService sapIntegrationPointerService,
                                           CollateralSapGatewayClient gateway,
                                           FileStorage fileStorage,
                                           CollateralChecklistRepository checklistRepository,
                                           CollateralItemRepository itemRepository,
                                           CollateralCoverageRepository coverageRepository,
                                           CollateralDocumentRepository documentRepository,
                                           CollateralSecuritiesPositionRepository securitiesPositionRepository,
                                           CollateralPerfectionRocRepository rocRepository,
                                           CollateralPerfectionCersaiRepository cersaiRepository,
                                           CollateralPerfectionNeslRepository neslRepository,
                                           CollateralSapPayloadMapper payloadMapper,
                                           @Value("${sap.collateralServiceUri:}") String servicePath) {
        this.sapIntegrationRepository = sapIntegrationRepository;
        this.sapIntegrationPointerService = sapIntegrationPointerService;
        this.gateway = gateway;
        this.fileStorage = fileStorage;
        this.checklistRepository = checklistRepository;
        this.payloadMapper = payloadMapper;
        this.servicePath = trimTrailingSlash(servicePath == null ? "" : servicePath.trim());
        repositories.put(CollateralSapEntitySet.HEADER, itemRepository);
        repositories.put(CollateralSapEntitySet.COVERAGE, coverageRepository);
        repositories.put(CollateralSapEntitySet.DOCUMENT, documentRepository);
        repositories.put(CollateralSapEntitySet.SECURITIES_POSITION, securitiesPositionRepository);
        repositories.put(CollateralSapEntitySet.PERFECTION_ROC, rocRepository);
        repositories.put(CollateralSapEntitySet.PERFECTION_CERSAI, cersaiRepository);
        repositories.put(CollateralSapEntitySet.PERFECTION_NESL, neslRepository);
    }

    /**
     * Posts every outstanding collateral component of approved checklists. Picks up pointers that are Not Posted
     * (status 0) and ones whose previous attempt is Posting Failed (status 2), so a failed post is retried on the next
     * sweep. Safe to call repeatedly: a component is handled at most once per sweep (a successful post moves its pointer
     * to status 3 and it is no longer picked up).
     */
    @Transactional
    public void syncApprovedCollateralsToSAP() {
        if (servicePath.isEmpty()) {
            log.debug("Collateral SAP integration is disabled (sap.collateralServiceUri is not set).");
            return;
        }
        List<SAPIntegrationPointer> pointers = pendingPointers(BUSINESS_PROCESS);
        pointers.sort(Comparator.comparing(SAPIntegrationPointer::getCreationDate,
                Comparator.nullsLast(Comparator.naturalOrder())));
        log.info("Collateral SAP integration: {} unposted/failed pointer(s) to consider", pointers.size());

        Map<String, Boolean> approvedByChecklist = new HashMap<>();
        int posted = 0;
        int failed = 0;
        int skipped = 0;
        for (SAPIntegrationPointer pointer : pointers) {
            Optional<CollateralSapEntitySet> target = CollateralSapEntitySet.forSubProcess(pointer.getSubBusinessProcessName());
            if (!target.isPresent()) {
                skipped++; // e.g. the per-loan "Collateral Checklist" header: no SAP entity set
                continue;
            }
            if (!isApproved(pointer.getMainEntityId(), approvedByChecklist)) {
                skipped++; // checklist not approved yet: leave the pointer at status 0
                continue;
            }
            if (send(pointer, target.get())) {
                posted++;
            } else {
                failed++;
            }
        }
        log.info("Collateral SAP integration finished: {} posted, {} failed, {} skipped", posted, failed, skipped);
    }

    /**
     * Posts every unprocessed collateral component that belongs to one checklist — the pointer's {@code mainEntityId},
     * i.e. the main object id — provided that checklist is Approved. Picks up pointers that are Not Posted (status 0)
     * and ones whose previous attempt is Posting Failed (status 2), so a failed post is retried. This is the
     * on-approval entry point: when a checklist is approved, all of its outstanding create / update / delete pointers
     * are sent to SAP in one pass, rather than waiting for the broad sweep. Safe to call repeatedly and for a
     * not-yet-approved checklist (nothing is sent in that case).
     *
     * @param mainObjectId the checklist id the pointers belong to ({@link SAPIntegrationPointer#getMainEntityId()})
     * @return the number of components posted successfully
     */
    @Transactional
    public int syncCollateralsForMainObject(String mainObjectId) {
        if (servicePath.isEmpty()) {
            log.debug("Collateral SAP integration is disabled (sap.collateralServiceUri is not set).");
            return 0;
        }
        if (mainObjectId == null || mainObjectId.trim().isEmpty()) {
            return 0;
        }
        String checklistId = mainObjectId.trim();
        if (!isApproved(checklistId, new HashMap<>())) {
            log.info("Collateral SAP integration: checklist {} is not approved; nothing sent", checklistId);
            return 0;
        }

        // All pending pointers that belong to this main object (checklist): Not Posted (0) and Posting Failed (2),
        // queried by example so the existing repository is left untouched (mainEntityId + the collateral process).
        List<SAPIntegrationPointer> pointers = pendingPointersForMainObject(checklistId);
        pointers.sort(Comparator.comparing(SAPIntegrationPointer::getCreationDate,
                Comparator.nullsLast(Comparator.naturalOrder())));
        log.info("Collateral SAP integration: {} unposted/failed pointer(s) for checklist {}", pointers.size(),
                checklistId);

        int posted = 0;
        int failed = 0;
        int skipped = 0;
        for (SAPIntegrationPointer pointer : pointers) {
            Optional<CollateralSapEntitySet> target =
                    CollateralSapEntitySet.forSubProcess(pointer.getSubBusinessProcessName());
            if (!target.isPresent()) {
                skipped++; // e.g. the per-loan "Collateral Checklist" header: no SAP entity set
                continue;
            }
            if (send(pointer, target.get())) {
                posted++;
            } else {
                failed++;
            }
        }
        log.info("Collateral SAP integration for checklist {} finished: {} posted, {} failed, {} skipped",
                checklistId, posted, failed, skipped);
        return posted;
    }

    private boolean isApproved(String checklistId, Map<String, Boolean> cache) {
        if (checklistId == null) {
            return false;
        }
        return cache.computeIfAbsent(checklistId, id -> {
            try {
                return checklistRepository.findById(UUID.fromString(id))
                        .map(c -> c.getWorkFlowStatusCode() != null
                                && c.getWorkFlowStatusCode() == CollateralChecklist.STATUS_APPROVED)
                        .orElse(false);
            } catch (IllegalArgumentException e) {
                return false;
            }
        });
    }

    /** Pointers to (re)send for a business process: Not Posted (status 0) and previously failed (status 2). */
    private List<SAPIntegrationPointer> pendingPointers(String businessProcess) {
        List<SAPIntegrationPointer> pointers = new ArrayList<>(
                sapIntegrationRepository.getByBusinessProcessNameAndStatus(businessProcess, STATUS_NOT_POSTED));
        pointers.addAll(sapIntegrationRepository.getByBusinessProcessNameAndStatus(businessProcess, STATUS_FAILED));
        return pointers;
    }

    /** Pointers to (re)send for one main object (checklist): Not Posted (0) and previously failed (2), by example. */
    private List<SAPIntegrationPointer> pendingPointersForMainObject(String checklistId) {
        List<SAPIntegrationPointer> pointers = new ArrayList<>(pointersForMainObject(checklistId, STATUS_NOT_POSTED));
        pointers.addAll(pointersForMainObject(checklistId, STATUS_FAILED));
        return pointers;
    }

    private List<SAPIntegrationPointer> pointersForMainObject(String checklistId, int status) {
        SAPIntegrationPointer probe = new SAPIntegrationPointer();
        probe.setBusinessProcessName(BUSINESS_PROCESS);
        probe.setMainEntityId(checklistId);
        probe.setStatus(status);
        return sapIntegrationRepository.findAll(Example.of(probe));
    }

    /** Sends one component to SAP by its integration mode (C create, U update, D delete). */
    private boolean send(SAPIntegrationPointer pointer, CollateralSapEntitySet target) {
        String mode = pointer.getMode();
        boolean delete = MODE_DELETE.equals(mode);

        Object entity = load(target, pointer.getBusinessObjectId());
        if (entity == null && !delete) {
            log.warn("Collateral SAP integration: {} {} no longer exists; marking the pointer failed",
                    pointer.getSubBusinessProcessName(), pointer.getBusinessObjectId());
            sapIntegrationPointerService.updateStatus(pointer.getId(), STATUS_FAILED);
            return false;
        }

        // The body carries the keys (ZID_NO / ITEM_ID / SERIAL_NO); for a delete the row may be gone, so a snapshot
        // payload is rebuilt from whatever remains, and the key predicate is taken from it.
        Map<String, Object> payload = entity != null ? payload(entity, target) : null;
        String collectionUri = servicePath + "/" + target.getEntitySet();

        sapIntegrationPointerService.updateStatus(pointer.getId(), STATUS_IN_PROCESS);
        // every gateway call below is written to the SAP HTTP log with this business context
        try (SapHttpLogContext.Scope ignored = SapHttpLogContext.open(BUSINESS_PROCESS,
                pointer.getSubBusinessProcessName(), pointer.getBusinessObjectId(), pointer.getId())) {
            // the keyed entity path, e.g. …/CoverageSet(ZidNo='0000000912',ItemId=guid'…'); needed for update, delete
            // and for the media stream of a created entity
            String predicate = payload == null ? null
                    : payloadMapper.keyPredicate(payload, target.getEntitySet(), target.getKeyColumns());
            boolean create = !delete && MODE_CREATE.equals(mode);
            if (predicate == null && !create) {
                log.warn("Collateral SAP integration: cannot build the SAP key for {} {} ({}); marking failed",
                        target.getEntitySet(), pointer.getBusinessObjectId(), mode);
                sapIntegrationPointerService.updateStatus(pointer.getId(), STATUS_FAILED);
                return false;
            }

            CollateralSapGatewayClient.Result result;
            if (create) {
                result = gateway.post(collectionUri, payload);
            } else if (delete) {
                result = gateway.delete(collectionUri + predicate);
            } else {
                result = gateway.put(collectionUri + predicate, payload);
            }
            // Success is the HTTP status (201 Created / 204 No Content / 200), not whether a body could be parsed:
            // a successful POST returns the created entity (er_entity), which is logged and must not count as failure.
            log.info("HTTP Status : " + result.getStatus() );
            log.info("HTTP Result Body : " + result.getBody() );
            boolean ok = result.isSuccess();

            // An uploaded file travels with its entity through the media entity DocumentAttachment: its entity is
            // created (keyed by the vault file id, with the owner's fields), then the bytes are sent as its stream.
            // Done after the owner exists in SAP; if any step fails the pointer is marked failed so the next sweep
            // resends everything (the owner create is idempotent on the SAP side, the attachment is created-or-
            // updated here, and the stream PUT replaces the previous content).
            if (ok && !delete && entity != null) {
                Optional<CollateralSapAttachment> attachment = CollateralSapAttachment.of(entity);
                if (attachment.isPresent()) {
                    ok = sendAttachment(target, payload, ownerIdOf(entity), attachment.get(), pointer);
                }
            }
            // The collateral item's four long texts live in their own entity (CollateralItemLongText) and always
            // follow the header: one PUT keyed by ZidNo, whether the header was created or updated.
            if (ok && !delete && target.isHeader() && entity instanceof CollateralItem) {
                ok = sendLongText((CollateralItem) entity, payload, pointer);
            }
            sapIntegrationPointerService.updateStatus(pointer.getId(), ok ? STATUS_POSTED : STATUS_FAILED);
            if (ok) {
                log.info("Collateral SAP integration: {} {} {} -> Posted ({})", mode, target.getEntitySet(),
                        pointer.getBusinessObjectId(), result);
            } else {
                log.warn("Collateral SAP integration: {} {} {} -> Failed ({})", mode, target.getEntitySet(),
                        pointer.getBusinessObjectId(), result);
            }
            return ok;
        } catch (Exception ex) {
            sapIntegrationPointerService.updateStatus(pointer.getId(), STATUS_FAILED);
            log.error("Collateral SAP integration: {} {} {} failed: {}", mode, target.getEntitySet(),
                    pointer.getBusinessObjectId(), ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * Sends the collateral item's long texts to SAP: always
     * {@code PUT CollateralItemLongTextSet(ZidNo='0000000912')} with Securitytypetext, Stipulatedsecurity,
     * Complianceremarkslegal and Actiontaken in the body (blank texts as empty strings). The gateway routes a PUT to
     * UPDATE_ENTITY, whose implementation stores the texts for the checklist number whether or not they exist yet.
     * Called after every successful header create or update, so the texts in SAP always match the portal.
     */
    private boolean sendLongText(CollateralItem item, Map<String, Object> headerPayload, SAPIntegrationPointer pointer) {
        Map<String, Object> payload = payloadMapper.toLongTextPayload(item, headerPayload);
        if (payload == null) {
            log.warn("Collateral SAP integration: long text of collateral item {} has no checklist number; marking failed",
                    pointer.getBusinessObjectId());
            return false;
        }
        String keyed = servicePath + "/" + CollateralSapPayloadMapper.COLLATERAL_ITEM_LONG_TEXT_SET
                + payloadMapper.keyPredicate(payload, CollateralSapPayloadMapper.COLLATERAL_ITEM_LONG_TEXT_SET,
                        new String[]{CollateralSapPayloadMapper.ZID_NO});
        CollateralSapGatewayClient.Result sent = gateway.put(keyed, payload);
        log.info("HTTP Status : " + sent.getStatus());
        log.info("HTTP Result Body : " + sent.getBody());
        if (sent.isSuccess()) {
            log.info("Collateral SAP integration: long text of collateral item {} sent ({})",
                    pointer.getBusinessObjectId(), sent);
        } else {
            log.warn("Collateral SAP integration: long text of collateral item {} failed ({})",
                    pointer.getBusinessObjectId(), sent);
        }
        return sent.isSuccess();
    }

    /**
     * Sends the entity's uploaded file to SAP through the media entity {@code DocumentAttachmentSet}, always as the
     * gateway's UPDATE_STREAM: {@code PUT DocumentAttachmentSet(<all keys>)/$value} with the file bytes as body and
     * the file's content type. Every property of DocumentAttachment except BdsDocId is a key, so the URL carries
     * BusinessObjectName, ZidNo, ItemId, Bukrs, Ranl, SerialNo, DocType, DocTypeDesc, DocTitle, Remarks, PtlId and
     * PtlDocuId (the vault file id) — the gateway hands them to UPDATE_STREAM in {@code it_key_tab}, an empty value as
     * an empty string. The Slug header adds the file name. The same request creates or replaces the content, so it
     * is safe to repeat on a retry.
     * <p>
     * A missing vault file is a data problem (the document was deleted from the vault, or the reference is stale) and
     * is reported as a failure so that it shows up in the pointer status rather than silently leaving SAP without the
     * document.
     */
    private boolean sendAttachment(CollateralSapEntitySet target, Map<String, Object> ownerPayload, UUID ownerId,
                                   CollateralSapAttachment attachment, SAPIntegrationPointer pointer) {
        // 1. the file bytes (before touching SAP, so a vault problem sends nothing)
        FileResource file;
        try {
            file = fileStorage.getFile(attachment.getFileId());
        } catch (RuntimeException ex) {
            log.error("Collateral SAP integration: file {} of {} {} is not in the vault: {}", attachment,
                    target.getEntitySet(), pointer.getBusinessObjectId(), ex.getMessage());
            return false;
        }
        byte[] content = file != null ? file.getDocumentContent() : null;
        if (content == null || content.length == 0) {
            log.error("Collateral SAP integration: file {} of {} {} is empty in the vault", attachment,
                    target.getEntitySet(), pointer.getBusinessObjectId());
            return false;
        }

        // 2. UPDATE_STREAM: PUT DocumentAttachmentSet(<all keys>)/$value with the file bytes — the same request the
        // business-partner document upload makes; the gateway passes the keys in it_key_tab
        Map<String, Object> attachmentPayload = payloadMapper.toAttachmentPayload(target, ownerPayload, ownerId,
                attachment.getFileId());
        String keyed = servicePath + "/" + CollateralSapPayloadMapper.DOCUMENT_ATTACHMENT_SET
                + payloadMapper.attachmentKeyPredicate(attachmentPayload);
        String contentType = attachment.contentType(file.getMimeType());
        String slug = payloadMapper.attachmentSlug(attachmentPayload, attachment.getFileName());

        CollateralSapGatewayClient.Result sent = gateway.putStream(keyed, content, contentType, slug);
        if (sent.isSuccess()) {
            log.info("Collateral SAP integration: file {} ({} bytes, {}) sent for {} {} ({})", attachment,
                    content.length, contentType, target.getEntitySet(), pointer.getBusinessObjectId(), sent);
        } else {
            log.warn("Collateral SAP integration: file {} for {} {} failed ({})", attachment, target.getEntitySet(),
                    pointer.getBusinessObjectId(), sent);
        }
        return sent.isSuccess();
    }

    /** The portal id of the entity being sent: the item's id for the header, the record's id for a child. */
    private static UUID ownerIdOf(Object entity) {
        if (entity instanceof CollateralItem) {
            return ((CollateralItem) entity).getId();
        }
        if (entity instanceof CollateralChildRecord) {
            return ((CollateralChildRecord<?>) entity).getId();
        }
        return null;
    }

    private Map<String, Object> payload(Object entity, CollateralSapEntitySet target) {
        Map<String, Object> payload = payloadMapper.toPayload(entity, target.getEntitySet());
        if (target.isHeader() && entity instanceof CollateralItem) {
            CollateralItem item = (CollateralItem) entity;
            // ChecklistHeader.ZloanNumber is not nullable in SAP: the loan contract number of the item's checklist
            // (or of the loan it belongs to) has to go with every collateral item.
            payloadMapper.putLoanNumber(payload, loanContractIdOf(item));
            // PTL_ID (and PORTAL_ID): the portal id of the collateral item, so SAP can refer back to the portal record
            payloadMapper.putPortalId(payload, item.getId(), true);
        }
        if (!target.isHeader() && entity instanceof CollateralChildRecord) {
            CollateralChildRecord<?> child = (CollateralChildRecord<?>) entity;
            CollateralItem item = child.getItem();
            // the child's SAP key is ZidNo (the checklist number) + ItemId (the record's own GUID)
            payloadMapper.putChecklistNumber(payload, item != null ? item.getChecklistIdNo() : null);
            payloadMapper.putItemId(payload, child.getId() != null ? child.getId().toString() : null);
            // PTL_ID: the portal id of this record (not nullable in SAP)
            payloadMapper.putPortalId(payload, child.getId(), false);
        }
        return payload;
    }

    /** The loan contract number of an item: its checklist's, else the loan application's (as the workflow does). */
    private static String loanContractIdOf(CollateralItem item) {
        CollateralChecklist checklist = item.getChecklist();
        if (checklist == null) {
            return null;
        }
        if (checklist.getLoanContractId() != null && !checklist.getLoanContractId().trim().isEmpty()) {
            return checklist.getLoanContractId();
        }
        return checklist.getLoanApplication() != null ? checklist.getLoanApplication().getLoanContractId() : null;
    }

    private Object load(CollateralSapEntitySet target, String businessObjectId) {
        if (businessObjectId == null) {
            return null;
        }
        try {
            return repositories.get(target).findById(UUID.fromString(businessObjectId)).orElse(null);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static String trimTrailingSlash(String value) {
        return value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
    }
}
