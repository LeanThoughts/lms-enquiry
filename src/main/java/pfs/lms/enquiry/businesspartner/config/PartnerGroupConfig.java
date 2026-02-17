package pfs.lms.enquiry.businesspartner.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.businesspartner.domain.LegalForm;
import pfs.lms.enquiry.businesspartner.domain.PartnerGroup;
import pfs.lms.enquiry.businesspartner.repository.LegalFormRepository;
import pfs.lms.enquiry.businesspartner.repository.PartnerGroupRepository;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(1)
public class PartnerGroupConfig implements CommandLineRunner {

    private final PartnerGroupRepository partnerGroupRepository;
 
    @Override
    public void run(String... strings) throws Exception {
        
        PartnerGroup partnerGroup = new PartnerGroup();

        partnerGroup = partnerGroupRepository.findByCode("0001");if (partnerGroup == null){ partnerGroup = new PartnerGroup("0001","PFS-Main Loan Partners",false,"0","0"); partnerGroupRepository.save(partnerGroup); }
        partnerGroup = partnerGroupRepository.findByCode("0002");if (partnerGroup == null){ partnerGroup = new PartnerGroup("0002","PFS-Treasury C&V",false,"0","0"); partnerGroupRepository.save(partnerGroup); }
        partnerGroup = partnerGroupRepository.findByCode("0003");if (partnerGroup == null){ partnerGroup = new PartnerGroup("0003","PFS-Domestic Vendors-Others",false,"0","0"); partnerGroupRepository.save(partnerGroup); }
        partnerGroup = partnerGroupRepository.findByCode("0004");if (partnerGroup == null){ partnerGroup = new PartnerGroup("0004","PFS-Domestic Vendors-MSME",false,"0","0"); partnerGroupRepository.save(partnerGroup); }
        partnerGroup = partnerGroupRepository.findByCode("0005");if (partnerGroup == null){ partnerGroup = new PartnerGroup("0005","PFS-Foreign Vendors",false,"0","0"); partnerGroupRepository.save(partnerGroup); }
        partnerGroup = partnerGroupRepository.findByCode("0006");if (partnerGroup == null){ partnerGroup = new PartnerGroup("0006","PFS-Inter Comp Vendors",false,"0","0"); partnerGroupRepository.save(partnerGroup); }
        partnerGroup = partnerGroupRepository.findByCode("0007");if (partnerGroup == null){ partnerGroup = new PartnerGroup("0007","PFS-OneTime Vendors",false,"0","0"); partnerGroupRepository.save(partnerGroup); }
        partnerGroup = partnerGroupRepository.findByCode("0008");if (partnerGroup == null){ partnerGroup = new PartnerGroup("0008","PFS-Contractual Emp(On Roll)",true,"10001","10999"); partnerGroupRepository.save(partnerGroup); }
        partnerGroup = partnerGroupRepository.findByCode("0009");if (partnerGroup == null){ partnerGroup = new PartnerGroup("0009","PFS-Employees(On Roll)",true,"501","9999"); partnerGroupRepository.save(partnerGroup); }
        partnerGroup = partnerGroupRepository.findByCode("0010");if (partnerGroup == null){ partnerGroup = new PartnerGroup("0010","PFS-Power Sale Customers",false,"0","0"); partnerGroupRepository.save(partnerGroup); }
        partnerGroup = partnerGroupRepository.findByCode("0011");if (partnerGroup == null){ partnerGroup = new PartnerGroup("0011","PFS- Other Customers",false,"0","0"); partnerGroupRepository.save(partnerGroup); }
        partnerGroup = partnerGroupRepository.findByCode("0012");if (partnerGroup == null){ partnerGroup = new PartnerGroup("0012","PFS-Casuals",false,"0","0"); partnerGroupRepository.save(partnerGroup); }
        partnerGroup = partnerGroupRepository.findByCode("0013");if (partnerGroup == null){ partnerGroup = new PartnerGroup("0013","PFS-Other Loan Partners (OBLP)",false,"0","0"); partnerGroupRepository.save(partnerGroup); }
        partnerGroup = partnerGroupRepository.findByCode("0014");if (partnerGroup == null){ partnerGroup = new PartnerGroup("0014","PFS-Casual Empl (Support Service)",true,"11001","11999"); partnerGroupRepository.save(partnerGroup); }
        partnerGroup = partnerGroupRepository.findByCode("0015");if (partnerGroup == null){ partnerGroup = new PartnerGroup("0015","PFS-Contractual (3rd Party)",true,"12001","12999"); partnerGroupRepository.save(partnerGroup); }
        partnerGroup = partnerGroupRepository.findByCode("0016");if (partnerGroup == null){ partnerGroup = new PartnerGroup("0016","PFS-Customer Empl (Support Service)",true,"E11001","E11999"); partnerGroupRepository.save(partnerGroup); }
        partnerGroup = partnerGroupRepository.findByCode("0017");if (partnerGroup == null){ partnerGroup = new PartnerGroup("0017","PFS-Customer Empl (3rd Party)",true,"E12001","10999"); partnerGroupRepository.save(partnerGroup); }
        partnerGroup = partnerGroupRepository.findByCode("0018");if (partnerGroup == null){ partnerGroup = new PartnerGroup("0018","PFS-Trainees",false,"14001","14999"); partnerGroupRepository.save(partnerGroup); }


        return;
    }
}