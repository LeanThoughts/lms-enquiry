package pfs.lms.enquiry.collateral.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.collateral.domain.CollateralValue;
import pfs.lms.enquiry.collateral.domain.CollateralValueList;
import pfs.lms.enquiry.collateral.repository.CollateralValueRepository;
import pfs.lms.enquiry.domain.UserRole;
import pfs.lms.enquiry.repository.UserRoleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Seeds the Collateral Management value lists and the role ZLM035 (Legal-Functional Head) at start-up.
 * Only missing entries are added: values that were changed in the database are kept. Safe to run on every start.
 */
@Component
public class CollateralValueInitializer implements CommandLineRunner {

    static final String LEGAL_FUNCTIONAL_HEAD_ROLE = "ZLM035";
    static final String LEGAL_FUNCTIONAL_HEAD_TEXT = "Legal-Functional Head";

    private static final Logger log = LoggerFactory.getLogger(CollateralValueInitializer.class);

    private final CollateralValueRepository valueRepository;
    private final UserRoleRepository userRoleRepository;

    public CollateralValueInitializer(CollateralValueRepository valueRepository, UserRoleRepository userRoleRepository) {
        this.valueRepository = valueRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public void run(String... args) {
        try {
            seedValueLists();
            seedLegalFunctionalHeadRole();
        } catch (Exception ex) {
            log.error("Collateral Management value lists could not be seeded: {}", ex.getMessage(), ex);
        }
    }

    private void seedValueLists() {
        Set<String> existing = valueRepository.findAll().stream()
                .map(value -> value.getListName() + "|" + value.getCode())
                .collect(Collectors.toSet());
        List<CollateralValue> missing = new ArrayList<>();
        for (CollateralValueList list : CollateralValueList.values()) {
            int sortOrder = 1;
            for (String[] value : list.getValues()) {
                if (!existing.contains(list.name() + "|" + value[0])) {
                    missing.add(new CollateralValue(list.name(), value[0], value[1], sortOrder));
                }
                sortOrder++;
            }
        }
        if (!missing.isEmpty()) {
            valueRepository.saveAll(missing);
            log.info("Collateral Management: {} value list entries added", missing.size());
        }
    }

    private void seedLegalFunctionalHeadRole() {
        boolean exists = userRoleRepository.findAll().stream()
                .anyMatch(role -> LEGAL_FUNCTIONAL_HEAD_ROLE.equals(role.getCode()));
        if (!exists) {
            UserRole role = new UserRole();
            role.setCode(LEGAL_FUNCTIONAL_HEAD_ROLE);
            role.setValue(LEGAL_FUNCTIONAL_HEAD_TEXT);
            userRoleRepository.save(role);
            log.info("Collateral Management: role {} ({}) added", LEGAL_FUNCTIONAL_HEAD_ROLE, LEGAL_FUNCTIONAL_HEAD_TEXT);
        }
    }
}
