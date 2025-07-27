package pfs.lms.enquiry.businesspartner.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerType;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerType;
import pfs.lms.enquiry.businesspartner.repository.BusinessPartnerTypeRepository;
import pfs.lms.enquiry.businesspartner.repository.BusinessPartnerTypeRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class BusinessPartnerTypeConfig implements CommandLineRunner {

    private final BusinessPartnerTypeRepository businessPartnerTypeRepository;
 
    @Override
    public void run(String... strings) throws Exception {

        
        
        BusinessPartnerType businessPartnerType = new BusinessPartnerType();


        businessPartnerType = businessPartnerTypeRepository.findByCode("");if (businessPartnerType == null){ businessPartnerType = new BusinessPartnerType( "",""); businessPartnerTypeRepository.save(businessPartnerType); }

        businessPartnerType = businessPartnerTypeRepository.findByCode("0001");if (businessPartnerType == null){ businessPartnerType = new BusinessPartnerType( "0001","State and Central Bank"); businessPartnerTypeRepository.save(businessPartnerType); }
        businessPartnerType = businessPartnerTypeRepository.findByCode("0002");if (businessPartnerType == null){ businessPartnerType = new BusinessPartnerType( "0002","Public Sector Entity"    ); businessPartnerTypeRepository.save(businessPartnerType); }
        businessPartnerType = businessPartnerTypeRepository.findByCode("0003");if (businessPartnerType == null){ businessPartnerType = new BusinessPartnerType( "0003","Multi lateral development bank"     ); businessPartnerTypeRepository.save(businessPartnerType); }
        businessPartnerType = businessPartnerTypeRepository.findByCode("0004");if (businessPartnerType == null){ businessPartnerType = new BusinessPartnerType( "0004","Bank"); businessPartnerTypeRepository.save(businessPartnerType); }
        businessPartnerType = businessPartnerTypeRepository.findByCode("0005");if (businessPartnerType == null){ businessPartnerType = new BusinessPartnerType( "0005","Financial Services provider"    ); businessPartnerTypeRepository.save(businessPartnerType); }
        businessPartnerType = businessPartnerTypeRepository.findByCode("0006");if (businessPartnerType == null){ businessPartnerType = new BusinessPartnerType( "0006","Security trading house"     ); businessPartnerTypeRepository.save(businessPartnerType); }
        businessPartnerType = businessPartnerTypeRepository.findByCode("0007");if (businessPartnerType == null){ businessPartnerType = new BusinessPartnerType( "0007","Company"     ); businessPartnerTypeRepository.save(businessPartnerType); }
        businessPartnerType = businessPartnerTypeRepository.findByCode("0008");if (businessPartnerType == null){ businessPartnerType = new BusinessPartnerType( "0008","Private customer"     ); businessPartnerTypeRepository.save(businessPartnerType); }
        businessPartnerType = businessPartnerTypeRepository.findByCode("0009");if (businessPartnerType == null){ businessPartnerType = new BusinessPartnerType( "0009","City council"     ); businessPartnerTypeRepository.save(businessPartnerType); }
        businessPartnerType = businessPartnerTypeRepository.findByCode("0010");if (businessPartnerType == null){ businessPartnerType = new BusinessPartnerType( "0010","Public-Central Govt"     ); businessPartnerTypeRepository.save(businessPartnerType); }
        businessPartnerType = businessPartnerTypeRepository.findByCode("0011");if (businessPartnerType == null){ businessPartnerType = new BusinessPartnerType( "0011","Public-State Govt"     ); businessPartnerTypeRepository.save(businessPartnerType); }
        businessPartnerType = businessPartnerTypeRepository.findByCode("0012");if (businessPartnerType == null){ businessPartnerType = new BusinessPartnerType( "0012","Public-Others"     ); businessPartnerTypeRepository.save(businessPartnerType); }
        businessPartnerType = businessPartnerTypeRepository.findByCode("20");if (businessPartnerType == null){ businessPartnerType = new BusinessPartnerType( "20","Co-Operative"     ); businessPartnerTypeRepository.save(businessPartnerType); }
        businessPartnerType = businessPartnerTypeRepository.findByCode("21");if (businessPartnerType == null){ businessPartnerType = new BusinessPartnerType( "21","Non Government Organizations"     ); businessPartnerTypeRepository.save(businessPartnerType); }
        businessPartnerType = businessPartnerTypeRepository.findByCode("22");if (businessPartnerType == null){ businessPartnerType = new BusinessPartnerType( "22","Non commercial company"     ); businessPartnerTypeRepository.save(businessPartnerType); }
        businessPartnerType = businessPartnerTypeRepository.findByCode("23");if (businessPartnerType == null){ businessPartnerType = new BusinessPartnerType( "23","International organization"     ); businessPartnerTypeRepository.save(businessPartnerType); }
        businessPartnerType = businessPartnerTypeRepository.findByCode("30");if (businessPartnerType == null){ businessPartnerType = new BusinessPartnerType( "30","Private"     ); businessPartnerTypeRepository.save(businessPartnerType); }
        businessPartnerType = businessPartnerTypeRepository.findByCode("40");if (businessPartnerType == null){ businessPartnerType = new BusinessPartnerType( "40","Joint"     ); businessPartnerTypeRepository.save(businessPartnerType); }
        businessPartnerType = businessPartnerTypeRepository.findByCode("9001");if (businessPartnerType == null){ businessPartnerType = new BusinessPartnerType( "9001","Manager"     ); businessPartnerTypeRepository.save(businessPartnerType); }

        return;
    }
}