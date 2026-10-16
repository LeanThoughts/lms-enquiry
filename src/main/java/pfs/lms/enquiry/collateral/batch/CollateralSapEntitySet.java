package pfs.lms.enquiry.collateral.batch;

import pfs.lms.enquiry.collateral.domain.CollateralCoverage;
import pfs.lms.enquiry.collateral.domain.CollateralDocument;
import pfs.lms.enquiry.collateral.domain.CollateralItem;
import pfs.lms.enquiry.collateral.domain.CollateralPerfectionCersai;
import pfs.lms.enquiry.collateral.domain.CollateralPerfectionNesl;
import pfs.lms.enquiry.collateral.domain.CollateralPerfectionRoc;
import pfs.lms.enquiry.collateral.domain.CollateralSecuritiesPosition;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Maps a collateral component to its entity set in the gateway OData service ZPFS_LMS_COLLATERAL_SRV. The key is the
 * {@code subBusinessProcessName} that {@code CollateralChangeDocumentService} writes on the SAP integration pointer, so
 * a pointer is routed to the right collection without re-reading the object's type.
 * <p>
 * "ChecklistHeaderSet" is the SAP checklist header, which is merged with {@link CollateralItem} in SAP (it carries
 * ZID_NO). The per-loan {@code CollateralChecklist} has no entity set and is not posted. There is no set for
 * {@code CollateralValue}.
 * <p>
 * {@code keyColumns} are the SAP columns that form the OData key of the entity set, used to build the key predicate for
 * update (PUT) and delete (DELETE). <b>Verify these against the service $metadata</b> — the header is keyed by ZID_NO
 * and PTL_ID (the collateral item's portal id), e.g. {@code ChecklistHeaderSet(ZidNo='0000000912',PtlId='…')}; the
 * children are keyed by ZID_NO, ITEM_ID (Edm.Guid, the record id) and PTL_ID (the same record id as a string), e.g.
 * {@code DocumentSet(ZidNo='0000001037',ItemId=guid'1589f4c0-…',PtlId='1589f4c0-…')}.
 */
public enum CollateralSapEntitySet {

    HEADER("Collateral Item", "ChecklistHeaderSet", CollateralItem.class,
            new String[]{"ZID_NO", "PTL_ID"}),
    COVERAGE("Collateral Coverage", "CoverageSet", CollateralCoverage.class,
            new String[]{"ZID_NO", "ITEM_ID", "PTL_ID"}),
    DOCUMENT("Collateral Documents", "DocumentSet", CollateralDocument.class,
            new String[]{"ZID_NO", "ITEM_ID", "PTL_ID"}),
    SECURITIES_POSITION("Securities Positions", "SecPositionSet", CollateralSecuritiesPosition.class,
            new String[]{"ZID_NO", "ITEM_ID", "PTL_ID"}),
    PERFECTION_ROC("Security Perfection RoC", "PerfRocSet", CollateralPerfectionRoc.class,
            new String[]{"ZID_NO", "ITEM_ID", "PTL_ID"}),
    PERFECTION_CERSAI("Security Perfection CERSAI", "PerfCersaiSet", CollateralPerfectionCersai.class,
            new String[]{"ZID_NO", "ITEM_ID", "PTL_ID"}),
    PERFECTION_NESL("Security Perfection NeSL", "PerfNeslSet", CollateralPerfectionNesl.class,
            new String[]{"ZID_NO", "ITEM_ID", "PTL_ID"});

    private static final Map<String, CollateralSapEntitySet> BY_SUB_PROCESS =
            Arrays.stream(values()).collect(Collectors.toMap(e -> e.subProcessName, Function.identity()));

    private final String subProcessName;
    private final String entitySet;
    private final Class<?> entityClass;
    private final String[] keyColumns;

    CollateralSapEntitySet(String subProcessName, String entitySet, Class<?> entityClass, String[] keyColumns) {
        this.subProcessName = subProcessName;
        this.entitySet = entitySet;
        this.entityClass = entityClass;
        this.keyColumns = keyColumns;
    }

    /** The entity set for a pointer's sub-business-process, or empty when the component is not posted (e.g. the header). */
    public static Optional<CollateralSapEntitySet> forSubProcess(String subProcessName) {
        return Optional.ofNullable(subProcessName == null ? null : BY_SUB_PROCESS.get(subProcessName.trim()));
    }

    /** True for the ChecklistHeaderSet (merged with the collateral item); children carry the item's ZID_NO as key. */
    public boolean isHeader() {
        return this == HEADER;
    }

    public String getSubProcessName() { return subProcessName; }
    public String getEntitySet() { return entitySet; }

    /**
     * The OData entity type behind the set (the set name without its "Set" suffix): {@code ChecklistHeader} for
     * {@code ChecklistHeaderSet}, {@code Document} for {@code DocumentSet}. This is the business object name an
     * uploaded file is attached to (DocumentAttachment.BusinessObjectName).
     */
    public String getEntityType() {
        return entitySet.endsWith("Set") ? entitySet.substring(0, entitySet.length() - 3) : entitySet;
    }
    public Class<?> getEntityClass() { return entityClass; }
    public String[] getKeyColumns() { return keyColumns.clone(); }
}
