package pfs.lms.enquiry.businesspartner.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.businesspartner.domain.PlanningGroup;
import pfs.lms.enquiry.businesspartner.repository.PlanningGroupRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlanningGroupConfig implements CommandLineRunner {

    private final PlanningGroupRepository planningGroupRepository;
 
    @Override
    public void run(String... strings) throws Exception {

        PlanningGroup planningGroup = new PlanningGroup();

        planningGroup = planningGroupRepository.findById(" ");if (planningGroup == null){ planningGroup = new PlanningGroup(" "," "); planningGroupRepository.save(planningGroup); }
        planningGroup = planningGroupRepository.findById("A1");if (planningGroup == null){ planningGroup = new PlanningGroup("A1","PFS-LoanCustomer"); planningGroupRepository.save(planningGroup); }
        planningGroup = planningGroupRepository.findById("A2");if (planningGroup == null){ planningGroup = new PlanningGroup("A2","PFS-TreasuryCustomers"); planningGroupRepository.save(planningGroup); }
        planningGroup = planningGroupRepository.findById("A3");if (planningGroup == null){ planningGroup = new PlanningGroup("A3","PFS-PowersaleCustomers"); planningGroupRepository.save(planningGroup); }
        planningGroup = planningGroupRepository.findById("A4");if (planningGroup == null){ planningGroup = new PlanningGroup("A4","PFS-OtherCustomers"); planningGroupRepository.save(planningGroup); }
        planningGroup = planningGroupRepository.findById("E1");if (planningGroup == null){ planningGroup = new PlanningGroup("E1","PFS-TreasuryVendors"); planningGroupRepository.save(planningGroup); }
        planningGroup = planningGroupRepository.findById("E2");if (planningGroup == null){ planningGroup = new PlanningGroup("E2","PFS-DomesticVendors-Others"); planningGroupRepository.save(planningGroup); }
        planningGroup = planningGroupRepository.findById("E3");if (planningGroup == null){ planningGroup = new PlanningGroup("E3","PFS-DomesticVendors-MSME"); planningGroupRepository.save(planningGroup); }
        planningGroup = planningGroupRepository.findById("E4");if (planningGroup == null){ planningGroup = new PlanningGroup("E4","PFS-ForeignVendors"); planningGroupRepository.save(planningGroup); }
        planningGroup = planningGroupRepository.findById("E5");if (planningGroup == null){ planningGroup = new PlanningGroup("E5","PFS-InterCompany"); planningGroupRepository.save(planningGroup); }
        planningGroup = planningGroupRepository.findById("E6");if (planningGroup == null){ planningGroup = new PlanningGroup("E6","PFS-OneTimeVendors"); planningGroupRepository.save(planningGroup); }
        planningGroup = planningGroupRepository.findById("E7");if (planningGroup == null){ planningGroup = new PlanningGroup("E7","PFS-Casuals"); planningGroupRepository.save(planningGroup); }
        planningGroup = planningGroupRepository.findById("E8");if (planningGroup == null){ planningGroup = new PlanningGroup("E8","PFS-Employees"); planningGroupRepository.save(planningGroup); }

    }
}