package pfs.lms.enquiry.businesspartner.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.businesspartner.domain.BupaRoleEntityFieldStatus;
import pfs.lms.enquiry.businesspartner.domain.Title;
import pfs.lms.enquiry.businesspartner.repository.BupaRoleEntityFieldStatusRepository;
import pfs.lms.enquiry.businesspartner.repository.TitleRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class BupaRoleEntityFieldStatusConfig implements CommandLineRunner {

    private final BupaRoleEntityFieldStatusRepository bupaRoleEntityFieldStatusRepository;

    @Override
    public void run(String... strings) throws Exception {

        BupaRoleEntityFieldStatus entityFieldStatus = new BupaRoleEntityFieldStatus();

        entityFieldStatus = bupaRoleEntityFieldStatusRepository.findByBupaRoleCodeAndEntityAndAndFieldName("TR0100","Partner","partyNumber");if (entityFieldStatus == null) { entityFieldStatus = new BupaRoleEntityFieldStatus(null, "TR0100","Partner","partyNumber",0);bupaRoleEntityFieldStatusRepository.save(entityFieldStatus); }
        entityFieldStatus = bupaRoleEntityFieldStatusRepository.findByBupaRoleCodeAndEntityAndAndFieldName("TR0100","Partner","partyCategory ");if (entityFieldStatus == null) { entityFieldStatus = new BupaRoleEntityFieldStatus(null, "TR0100","Partner","partyCategory ",2);bupaRoleEntityFieldStatusRepository.save(entityFieldStatus); }
        entityFieldStatus = bupaRoleEntityFieldStatusRepository.findByBupaRoleCodeAndEntityAndAndFieldName("TR0100","Partner","partnerGroup ");if (entityFieldStatus == null) { entityFieldStatus = new BupaRoleEntityFieldStatus(null, "TR0100","Partner","partnerGroup ",2);bupaRoleEntityFieldStatusRepository.save(entityFieldStatus); }
        entityFieldStatus = bupaRoleEntityFieldStatusRepository.findByBupaRoleCodeAndEntityAndAndFieldName("TR0100","Partner","partnerType  ");if (entityFieldStatus == null) { entityFieldStatus = new BupaRoleEntityFieldStatus(null, "TR0100","Partner","partnerType  ",2);bupaRoleEntityFieldStatusRepository.save(entityFieldStatus); }
        entityFieldStatus = bupaRoleEntityFieldStatusRepository.findByBupaRoleCodeAndEntityAndAndFieldName("TR0100","Partner","partnerExternalNumber ");if (entityFieldStatus == null) { entityFieldStatus = new BupaRoleEntityFieldStatus(null, "TR0100","Partner","partnerExternalNumber ",0);bupaRoleEntityFieldStatusRepository.save(entityFieldStatus); }
        entityFieldStatus = bupaRoleEntityFieldStatusRepository.findByBupaRoleCodeAndEntityAndAndFieldName("TR0100","Partner","partyRole ");if (entityFieldStatus == null) { entityFieldStatus = new BupaRoleEntityFieldStatus(null, "TR0100","Partner","partyRole ",1);bupaRoleEntityFieldStatusRepository.save(entityFieldStatus); }
        entityFieldStatus = bupaRoleEntityFieldStatusRepository.findByBupaRoleCodeAndEntityAndAndFieldName("TR0100","Partner","partyName1 ");if (entityFieldStatus == null) { entityFieldStatus = new BupaRoleEntityFieldStatus(null, "TR0100","Partner","partyName1 ",2);bupaRoleEntityFieldStatusRepository.save(entityFieldStatus); }
        entityFieldStatus = bupaRoleEntityFieldStatusRepository.findByBupaRoleCodeAndEntityAndAndFieldName("TR0100","Partner","partyName2 ");if (entityFieldStatus == null) { entityFieldStatus = new BupaRoleEntityFieldStatus(null, "TR0100","Partner","partyName2 ",2);bupaRoleEntityFieldStatusRepository.save(entityFieldStatus); }
        entityFieldStatus = bupaRoleEntityFieldStatusRepository.findByBupaRoleCodeAndEntityAndAndFieldName("TR0100","Partner","contactPersonName ");if (entityFieldStatus == null) { entityFieldStatus = new BupaRoleEntityFieldStatus(null, "TR0100","Partner","contactPersonName ",1);bupaRoleEntityFieldStatusRepository.save(entityFieldStatus); }
        entityFieldStatus = bupaRoleEntityFieldStatusRepository.findByBupaRoleCodeAndEntityAndAndFieldName("TR0100","Partner","addressLine1 ");if (entityFieldStatus == null) { entityFieldStatus = new BupaRoleEntityFieldStatus(null, "TR0100","Partner","addressLine1 ",2);bupaRoleEntityFieldStatusRepository.save(entityFieldStatus); }
        entityFieldStatus = bupaRoleEntityFieldStatusRepository.findByBupaRoleCodeAndEntityAndAndFieldName("TR0100","Partner","addressLine2 ");if (entityFieldStatus == null) { entityFieldStatus = new BupaRoleEntityFieldStatus(null, "TR0100","Partner","addressLine2 ",2);bupaRoleEntityFieldStatusRepository.save(entityFieldStatus); }
        entityFieldStatus = bupaRoleEntityFieldStatusRepository.findByBupaRoleCodeAndEntityAndAndFieldName("TR0100","Partner","street ");if (entityFieldStatus == null) { entityFieldStatus = new BupaRoleEntityFieldStatus(null, "TR0100","Partner","street ",2);bupaRoleEntityFieldStatusRepository.save(entityFieldStatus); }
        entityFieldStatus = bupaRoleEntityFieldStatusRepository.findByBupaRoleCodeAndEntityAndAndFieldName("TR0100","Partner","city ");if (entityFieldStatus == null) { entityFieldStatus = new BupaRoleEntityFieldStatus(null, "TR0100","Partner","city ",2);bupaRoleEntityFieldStatusRepository.save(entityFieldStatus); }
        entityFieldStatus = bupaRoleEntityFieldStatusRepository.findByBupaRoleCodeAndEntityAndAndFieldName("TR0100","Partner","state ");if (entityFieldStatus == null) { entityFieldStatus = new BupaRoleEntityFieldStatus(null, "TR0100","Partner","state ",2);bupaRoleEntityFieldStatusRepository.save(entityFieldStatus); }
        entityFieldStatus = bupaRoleEntityFieldStatusRepository.findByBupaRoleCodeAndEntityAndAndFieldName("TR0100","Partner","postalCode ");if (entityFieldStatus == null) { entityFieldStatus = new BupaRoleEntityFieldStatus(null, "TR0100","Partner","postalCode ",2);bupaRoleEntityFieldStatusRepository.save(entityFieldStatus); }
        entityFieldStatus = bupaRoleEntityFieldStatusRepository.findByBupaRoleCodeAndEntityAndAndFieldName("TR0100","Partner","country ");if (entityFieldStatus == null) { entityFieldStatus = new BupaRoleEntityFieldStatus(null, "TR0100","Partner","country ",2);bupaRoleEntityFieldStatusRepository.save(entityFieldStatus); }
        entityFieldStatus = bupaRoleEntityFieldStatusRepository.findByBupaRoleCodeAndEntityAndAndFieldName("TR0100","Partner","email ");if (entityFieldStatus == null) { entityFieldStatus = new BupaRoleEntityFieldStatus(null, "TR0100","Partner","email ",2);bupaRoleEntityFieldStatusRepository.save(entityFieldStatus); }
        entityFieldStatus = bupaRoleEntityFieldStatusRepository.findByBupaRoleCodeAndEntityAndAndFieldName("TR0100","Partner","contactNumber ");if (entityFieldStatus == null) { entityFieldStatus = new BupaRoleEntityFieldStatus(null, "TR0100","Partner","contactNumber ",2);bupaRoleEntityFieldStatusRepository.save(entityFieldStatus); }
        entityFieldStatus = bupaRoleEntityFieldStatusRepository.findByBupaRoleCodeAndEntityAndAndFieldName("TR0100","Partner","groupCompany ");if (entityFieldStatus == null) { entityFieldStatus = new BupaRoleEntityFieldStatus(null, "TR0100","Partner","groupCompany ",1);bupaRoleEntityFieldStatusRepository.save(entityFieldStatus); }
        entityFieldStatus = bupaRoleEntityFieldStatusRepository.findByBupaRoleCodeAndEntityAndAndFieldName("TR0100","Partner","userName ");if (entityFieldStatus == null) { entityFieldStatus = new BupaRoleEntityFieldStatus(null, "TR0100","Partner","userName ",1);bupaRoleEntityFieldStatusRepository.save(entityFieldStatus); }
        entityFieldStatus = bupaRoleEntityFieldStatusRepository.findByBupaRoleCodeAndEntityAndAndFieldName("TR0100","Partner","password ");if (entityFieldStatus == null) { entityFieldStatus = new BupaRoleEntityFieldStatus(null, "TR0100","Partner","password ",1);bupaRoleEntityFieldStatusRepository.save(entityFieldStatus); }
        entityFieldStatus = bupaRoleEntityFieldStatusRepository.findByBupaRoleCodeAndEntityAndAndFieldName("TR0100","Partner","pan ");if (entityFieldStatus == null) { entityFieldStatus = new BupaRoleEntityFieldStatus(null, "TR0100","Partner","pan ",0);bupaRoleEntityFieldStatusRepository.save(entityFieldStatus); }
        entityFieldStatus = bupaRoleEntityFieldStatusRepository.findByBupaRoleCodeAndEntityAndAndFieldName("TR0100","Partner","industrySector ");if (entityFieldStatus == null) { entityFieldStatus = new BupaRoleEntityFieldStatus(null, "TR0100","Partner","industrySector ",0);bupaRoleEntityFieldStatusRepository.save(entityFieldStatus); }
        entityFieldStatus = bupaRoleEntityFieldStatusRepository.findByBupaRoleCodeAndEntityAndAndFieldName("TR0100","Partner","msmeRegisterNumber ");if (entityFieldStatus == null) { entityFieldStatus = new BupaRoleEntityFieldStatus(null, "TR0100","Partner","msmeRegisterNumber ",0);bupaRoleEntityFieldStatusRepository.save(entityFieldStatus); }
        entityFieldStatus = bupaRoleEntityFieldStatusRepository.findByBupaRoleCodeAndEntityAndAndFieldName("TR0100","Partner","gstNumber ");if (entityFieldStatus == null) { entityFieldStatus = new BupaRoleEntityFieldStatus(null, "TR0100","Partner","gstNumber ",0);bupaRoleEntityFieldStatusRepository.save(entityFieldStatus); }
        entityFieldStatus = bupaRoleEntityFieldStatusRepository.findByBupaRoleCodeAndEntityAndAndFieldName("TR0100","Partner","cinNumber ");if (entityFieldStatus == null) { entityFieldStatus = new BupaRoleEntityFieldStatus(null, "TR0100","Partner","cinNumber ",0);bupaRoleEntityFieldStatusRepository.save(entityFieldStatus); }
        entityFieldStatus = bupaRoleEntityFieldStatusRepository.findByBupaRoleCodeAndEntityAndAndFieldName("TR0100","Partner","mobileNumber ");if (entityFieldStatus == null) { entityFieldStatus = new BupaRoleEntityFieldStatus(null, "TR0100","Partner","mobileNumber ",0);bupaRoleEntityFieldStatusRepository.save(entityFieldStatus); }
        entityFieldStatus = bupaRoleEntityFieldStatusRepository.findByBupaRoleCodeAndEntityAndAndFieldName("TR0100","Partner","partnerCategory ");if (entityFieldStatus == null) { entityFieldStatus = new BupaRoleEntityFieldStatus(null, "TR0100","Partner","partnerCategory ",2);bupaRoleEntityFieldStatusRepository.save(entityFieldStatus); }
        entityFieldStatus = bupaRoleEntityFieldStatusRepository.findByBupaRoleCodeAndEntityAndAndFieldName("TR0100","Partner","defaultPartnerRole ");if (entityFieldStatus == null) { entityFieldStatus = new BupaRoleEntityFieldStatus(null, "TR0100","Partner","defaultPartnerRole ",2);bupaRoleEntityFieldStatusRepository.save(entityFieldStatus); }
        entityFieldStatus = bupaRoleEntityFieldStatusRepository.findByBupaRoleCodeAndEntityAndAndFieldName("TR0100","Partner","title ");if (entityFieldStatus == null) { entityFieldStatus = new BupaRoleEntityFieldStatus(null, "TR0100","Partner","title ",2);bupaRoleEntityFieldStatusRepository.save(entityFieldStatus); }
        entityFieldStatus = bupaRoleEntityFieldStatusRepository.findByBupaRoleCodeAndEntityAndAndFieldName("TR0100","Partner","legalEntity ");if (entityFieldStatus == null) { entityFieldStatus = new BupaRoleEntityFieldStatus(null, "TR0100","Partner","legalEntity ",1);bupaRoleEntityFieldStatusRepository.save(entityFieldStatus); }
        entityFieldStatus = bupaRoleEntityFieldStatusRepository.findByBupaRoleCodeAndEntityAndAndFieldName("TR0100","Partner","legalForm ");if (entityFieldStatus == null) { entityFieldStatus = new BupaRoleEntityFieldStatus(null, "TR0100","Partner","legalForm ",1);bupaRoleEntityFieldStatusRepository.save(entityFieldStatus); }
        entityFieldStatus = bupaRoleEntityFieldStatusRepository.findByBupaRoleCodeAndEntityAndAndFieldName("TR0100","Partner","houseBank ");if (entityFieldStatus == null) { entityFieldStatus = new BupaRoleEntityFieldStatus(null, "TR0100","Partner","houseBank ",1);bupaRoleEntityFieldStatusRepository.save(entityFieldStatus); }


    }
        }