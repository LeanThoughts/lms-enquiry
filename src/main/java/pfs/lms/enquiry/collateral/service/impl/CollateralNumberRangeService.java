package pfs.lms.enquiry.collateral.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pfs.lms.enquiry.collateral.domain.CollateralItem;
import pfs.lms.enquiry.collateral.domain.CollateralNumberRange;
import pfs.lms.enquiry.collateral.dto.ChecklistIdConfigurationDto;
import pfs.lms.enquiry.collateral.repository.CollateralItemRepository;
import pfs.lms.enquiry.collateral.repository.CollateralNumberRangeRepository;
import pfs.lms.enquiry.collateral.service.ICollateralChildService;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.exception.LmsException;
import pfs.lms.enquiry.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Generates the Checklist ID No. (SAP ZID_NO) of collaterals created in the portal.
 * <p>
 * The next number is the highest of: the number range's next number, the highest SAP number + 1 (maintained in the
 * Configuration app "Checklist ID Number Range") and the property {@code collateral.checklist-id.start-number}
 * (default 1). Numbers that already exist (e.g. loaded by the SAP migration) are skipped. The number range row is
 * locked while a number is drawn, so two users never get the same number, and numbers never go down.
 */
@Service
@Transactional
public class CollateralNumberRangeService {

    public static final String CHECKLIST_ID = "CHECKLIST_ID";
    /** ZID_NO is CHAR 10 in SAP. */
    public static final long MAX_CHECKLIST_ID = 9_999_999_999L;
    private static final String SOURCE_SAP_MIGRATION = "SAP_MIGRATION";

    private static final Logger log = LoggerFactory.getLogger(CollateralNumberRangeService.class);

    private final CollateralNumberRangeRepository rangeRepository;
    private final CollateralItemRepository itemRepository;
    private final ICollateralChildService childService;
    private final UserRepository userRepository;
    private final long startNumber;
    private final boolean authorizationEnabled;
    private final List<String> configurationRoles;

    public CollateralNumberRangeService(CollateralNumberRangeRepository rangeRepository,
                                        CollateralItemRepository itemRepository,
                                        ICollateralChildService childService,
                                        UserRepository userRepository,
                                        @Value("${collateral.checklist-id.start-number:1}") long startNumber,
                                        @Value("${collateral.authorization.enabled:true}") boolean authorizationEnabled,
                                        @Value("${collateral.configuration-roles:ZLM023}") String configurationRoles) {
        this.rangeRepository = rangeRepository;
        this.itemRepository = itemRepository;
        this.childService = childService;
        this.userRepository = userRepository;
        if (startNumber < 1 || startNumber > MAX_CHECKLIST_ID) {
            log.error("collateral.checklist-id.start-number {} is outside 1 - {}; 1 is used.", startNumber, MAX_CHECKLIST_ID);
            startNumber = 1;
        }
        this.startNumber = startNumber;
        this.authorizationEnabled = authorizationEnabled;
        this.configurationRoles = Arrays.stream(configurationRoles.split(","))
                .map(String::trim)
                .filter(role -> !role.isEmpty())
                .collect(Collectors.toList());
    }

    // ------------------------------------------------------------------------------------------------ numbers

    /** Draws the next Checklist ID No.; runs in the caller's transaction, which keeps the range locked until it ends. */
    public long nextChecklistIdNo() {
        CollateralNumberRange range = lockRange();
        long next = firstFreeNumber(range);
        if (next > MAX_CHECKLIST_ID) {
            throw new LmsException("No Checklist ID No. is left in the number range (maximum " + MAX_CHECKLIST_ID
                    + "). Please contact the administrator.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        range.setNextNumber(next + 1);
        rangeRepository.save(range);
        return next;
    }

    /**
     * Creates the number range row if it does not exist yet and gives a Checklist ID No. to collaterals that were
     * created before the numbers were generated (oldest first). Their child rows get the number too.
     *
     * @return the number of collaterals that got a number
     */
    public int initialize() {
        if (!rangeRepository.existsById(CHECKLIST_ID)) {
            rangeRepository.saveAndFlush(new CollateralNumberRange(CHECKLIST_ID, startNumber));
            log.info("Collateral checklist ID number range created; first number {}", startNumber);
        }
        List<CollateralItem> missing = itemRepository.findByChecklistIdNoIsNull();
        missing.sort(Comparator
                .comparing((CollateralItem item) -> item.getCreatedOn() == null ? LocalDate.MIN : item.getCreatedOn())
                .thenComparing(item -> item.getCreatedAt() == null ? LocalTime.MIN : item.getCreatedAt()));
        for (CollateralItem item : missing) {
            item.setChecklistIdNo(nextChecklistIdNo());
            itemRepository.save(item);
            childService.updateChecklistIdNo(item);
            log.info("Collateral {} got Checklist ID No. {}", item.getId(), item.getChecklistIdNo());
        }
        return missing.size();
    }

    // ------------------------------------------------------------------------------------------------ configuration

    /** The Checklist ID number range as shown in the Configuration app. */
    @Transactional(readOnly = true)
    public ChecklistIdConfigurationDto getConfiguration(String principalName) {
        CollateralNumberRange range = rangeRepository.findById(CHECKLIST_ID)
                .orElse(new CollateralNumberRange(CHECKLIST_ID, startNumber));
        ChecklistIdConfigurationDto dto = new ChecklistIdConfigurationDto();
        dto.setSapHighestNumber(range.getSapHighestNumber());
        dto.setNextNumber(firstFreeNumber(range));
        dto.setHighestNumberInPortal(itemRepository.findHighestChecklistIdNo());
        dto.setHighestMigratedNumber(itemRepository.findHighestChecklistIdNo(SOURCE_SAP_MIGRATION));
        dto.setStartNumber(startNumber);
        dto.setMaximumNumber(MAX_CHECKLIST_ID);
        dto.setChangedBy(range.getChangedBy());
        dto.setChangedAt(range.getChangedAt());
        User user = principalName == null ? null : userRepository.findByEmail(principalName);
        dto.setUserRole(user != null ? user.getRole() : null);
        dto.setCanChange(canChange(user));
        return dto;
    }

    /**
     * Sets the highest number used in SAP; portal numbers continue above it. It must be below the maximum and not
     * below the highest number already loaded from SAP. Numbers already given in the portal are never given again.
     */
    public ChecklistIdConfigurationDto setSapHighestNumber(Long sapHighestNumber, String principalName) {
        User user = principalName == null ? null : userRepository.findByEmail(principalName);
        if (!canChange(user)) {
            String role = user != null && user.getRole() != null ? user.getRole().trim() : "(none)";
            throw new LmsException("No edit access for the user with the role " + role
                    + ". The Checklist ID number range can only be displayed.", HttpStatus.FORBIDDEN);
        }
        if (sapHighestNumber == null || sapHighestNumber < 0 || sapHighestNumber >= MAX_CHECKLIST_ID) {
            throw new LmsException("Highest ZID_NO in SAP must be a whole number between 0 and "
                    + (MAX_CHECKLIST_ID - 1) + ".", HttpStatus.PRECONDITION_FAILED);
        }
        Long migrated = itemRepository.findHighestChecklistIdNo(SOURCE_SAP_MIGRATION);
        if (migrated != null && sapHighestNumber < migrated) {
            throw new LmsException("Highest ZID_NO in SAP cannot be below " + migrated
                    + ", the highest Checklist ID No. already loaded from SAP.", HttpStatus.PRECONDITION_FAILED);
        }
        CollateralNumberRange range = lockRange();
        Long before = range.getSapHighestNumber();
        range.setSapHighestNumber(sapHighestNumber);
        range.setChangedBy(principalName);
        range.setChangedAt(LocalDateTime.now());
        rangeRepository.save(range);
        log.info("Highest ZID_NO in SAP changed from {} to {} by {}", before, sapHighestNumber, principalName);
        return getConfiguration(principalName);
    }

    // ------------------------------------------------------------------------------------------------ helpers

    private CollateralNumberRange lockRange() {
        return rangeRepository.findForUpdate(CHECKLIST_ID).orElseGet(() -> {
            rangeRepository.saveAndFlush(new CollateralNumberRange(CHECKLIST_ID, startNumber));
            return rangeRepository.findForUpdate(CHECKLIST_ID).orElseThrow(IllegalStateException::new);
        });
    }

    /** The lowest number allowed by the range, SAP's highest number and the start number, skipping used numbers. */
    private long firstFreeNumber(CollateralNumberRange range) {
        long next = Math.max(range.getNextNumber(), startNumber);
        if (range.getSapHighestNumber() != null) {
            next = Math.max(next, range.getSapHighestNumber() + 1);
        }
        while (next <= MAX_CHECKLIST_ID && itemRepository.existsByChecklistIdNo(next)) {
            next++;
        }
        return next;
    }

    private boolean canChange(User user) {
        if (!authorizationEnabled) {
            return true;
        }
        return user != null && user.isStatus() && user.getRole() != null && configurationRoles.contains(user.getRole().trim());
    }
}
