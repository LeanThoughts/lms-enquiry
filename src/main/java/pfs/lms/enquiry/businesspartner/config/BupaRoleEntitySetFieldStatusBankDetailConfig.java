package pfs.lms.enquiry.businesspartner.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.businesspartner.domain.BupaRoleEntitySetFieldStatus;
import pfs.lms.enquiry.businesspartner.repository.BupaRoleEntitySetFieldStatusRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class BupaRoleEntitySetFieldStatusBankDetailConfig implements CommandLineRunner {

    private final BupaRoleEntitySetFieldStatusRepository bupaRoleEntitySetFieldStatusRepository;

    @Override
    public void run(String... strings) throws Exception {

        BupaRoleEntitySetFieldStatus entitySetFieldStatus = new BupaRoleEntitySetFieldStatus();


        entitySetFieldStatus = bupaRoleEntitySetFieldStatusRepository.findByBupaRoleCodeAndEntitySetAndFieldNameAndKeyFieldValue("TR0100","BusinesPartnerBankDetail","bankKey","2");if (entitySetFieldStatus == null) { entitySetFieldStatus = new BupaRoleEntitySetFieldStatus(null, "TR0100","BusinesPartnerBankDetail","bankKey","2",true,1);}bupaRoleEntitySetFieldStatusRepository.save(entitySetFieldStatus);
        entitySetFieldStatus = bupaRoleEntitySetFieldStatusRepository.findByBupaRoleCodeAndEntitySetAndFieldNameAndKeyFieldValue("TR0100","BusinesPartnerBankDetail","bankName","2");if (entitySetFieldStatus == null) { entitySetFieldStatus = new BupaRoleEntitySetFieldStatus(null, "TR0100","BusinesPartnerBankDetail","bankName","2",false,2);}bupaRoleEntitySetFieldStatusRepository.save(entitySetFieldStatus);
        entitySetFieldStatus = bupaRoleEntitySetFieldStatusRepository.findByBupaRoleCodeAndEntitySetAndFieldNameAndKeyFieldValue("TR0100","BusinesPartnerBankDetail","ifscCode","2");if (entitySetFieldStatus == null) { entitySetFieldStatus = new BupaRoleEntitySetFieldStatus(null, "TR0100","BusinesPartnerBankDetail","ifscCode","2",false,2);}bupaRoleEntitySetFieldStatusRepository.save(entitySetFieldStatus);
        entitySetFieldStatus = bupaRoleEntitySetFieldStatusRepository.findByBupaRoleCodeAndEntitySetAndFieldNameAndKeyFieldValue("TR0100","BusinesPartnerBankDetail","accountNumber","2");if (entitySetFieldStatus == null) { entitySetFieldStatus = new BupaRoleEntitySetFieldStatus(null, "TR0100","BusinesPartnerBankDetail","accountNumber","2",false,2);}bupaRoleEntitySetFieldStatusRepository.save(entitySetFieldStatus);
        entitySetFieldStatus = bupaRoleEntitySetFieldStatusRepository.findByBupaRoleCodeAndEntitySetAndFieldNameAndKeyFieldValue("TR0100","BusinesPartnerBankDetail","validFromDate","1");if (entitySetFieldStatus == null) { entitySetFieldStatus = new BupaRoleEntitySetFieldStatus(null, "TR0100","BusinesPartnerBankDetail","validFromDate","1",false,1);}bupaRoleEntitySetFieldStatusRepository.save(entitySetFieldStatus);
        entitySetFieldStatus = bupaRoleEntitySetFieldStatusRepository.findByBupaRoleCodeAndEntitySetAndFieldNameAndKeyFieldValue("TR0100","BusinesPartnerBankDetail","validToDate","1");if (entitySetFieldStatus == null) { entitySetFieldStatus = new BupaRoleEntitySetFieldStatus(null, "TR0100","BusinesPartnerBankDetail","validToDate","1",false,1);}bupaRoleEntitySetFieldStatusRepository.save(entitySetFieldStatus);
        entitySetFieldStatus = bupaRoleEntitySetFieldStatusRepository.findByBupaRoleCodeAndEntitySetAndFieldNameAndKeyFieldValue("TR0100","BusinesPartnerBankDetail","entryDate","1");if (entitySetFieldStatus == null) { entitySetFieldStatus = new BupaRoleEntitySetFieldStatus(null, "TR0100","BusinesPartnerBankDetail","entryDate","1",false,1);}bupaRoleEntitySetFieldStatusRepository.save(entitySetFieldStatus);
        entitySetFieldStatus = bupaRoleEntitySetFieldStatusRepository.findByBupaRoleCodeAndEntitySetAndFieldNameAndKeyFieldValue("TR0100","BusinesPartnerBankDetail","bankDetailId","1");if (entitySetFieldStatus == null) { entitySetFieldStatus = new BupaRoleEntitySetFieldStatus(null, "TR0100","BusinesPartnerBankDetail","bankDetailId","1",false,1);}bupaRoleEntitySetFieldStatusRepository.save(entitySetFieldStatus);
        entitySetFieldStatus = bupaRoleEntitySetFieldStatusRepository.findByBupaRoleCodeAndEntitySetAndFieldNameAndKeyFieldValue("TR0100","BusinesPartnerBankDetail","externalBankDetailId","1");if (entitySetFieldStatus == null) { entitySetFieldStatus = new BupaRoleEntitySetFieldStatus(null, "TR0100","BusinesPartnerBankDetail","externalBankDetailId","1",false,1);}bupaRoleEntitySetFieldStatusRepository.save(entitySetFieldStatus);
        entitySetFieldStatus = bupaRoleEntitySetFieldStatusRepository.findByBupaRoleCodeAndEntitySetAndFieldNameAndKeyFieldValue("TR0100","BusinesPartnerBankDetail","bankCountry","1");if (entitySetFieldStatus == null) { entitySetFieldStatus = new BupaRoleEntitySetFieldStatus(null, "TR0100","BusinesPartnerBankDetail","bankCountry","1",false,2);}bupaRoleEntitySetFieldStatusRepository.save(entitySetFieldStatus);
        entitySetFieldStatus = bupaRoleEntitySetFieldStatusRepository.findByBupaRoleCodeAndEntitySetAndFieldNameAndKeyFieldValue("TR0100","BusinesPartnerBankDetail","bankCountryIso","1");if (entitySetFieldStatus == null) { entitySetFieldStatus = new BupaRoleEntitySetFieldStatus(null, "TR0100","BusinesPartnerBankDetail","bankCountryIso","1",false,2);}bupaRoleEntitySetFieldStatusRepository.save(entitySetFieldStatus);
        entitySetFieldStatus = bupaRoleEntitySetFieldStatusRepository.findByBupaRoleCodeAndEntitySetAndFieldNameAndKeyFieldValue("TR0100","BusinesPartnerBankDetail","controlKey","1");if (entitySetFieldStatus == null) { entitySetFieldStatus = new BupaRoleEntitySetFieldStatus(null, "TR0100","BusinesPartnerBankDetail","controlKey","1",false,2);}bupaRoleEntitySetFieldStatusRepository.save(entitySetFieldStatus);
        entitySetFieldStatus = bupaRoleEntitySetFieldStatusRepository.findByBupaRoleCodeAndEntitySetAndFieldNameAndKeyFieldValue("TR0100","BusinesPartnerBankDetail","referenceNumber","1");if (entitySetFieldStatus == null) { entitySetFieldStatus = new BupaRoleEntitySetFieldStatus(null, "TR0100","BusinesPartnerBankDetail","referenceNumber","1",true,2);}bupaRoleEntitySetFieldStatusRepository.save(entitySetFieldStatus);
        entitySetFieldStatus = bupaRoleEntitySetFieldStatusRepository.findByBupaRoleCodeAndEntitySetAndFieldNameAndKeyFieldValue("TR0100","BusinesPartnerBankDetail","accountHolderName","1");if (entitySetFieldStatus == null) { entitySetFieldStatus = new BupaRoleEntitySetFieldStatus(null, "TR0100","BusinesPartnerBankDetail","accountHolderName","1",false,2);}bupaRoleEntitySetFieldStatusRepository.save(entitySetFieldStatus);
        entitySetFieldStatus = bupaRoleEntitySetFieldStatusRepository.findByBupaRoleCodeAndEntitySetAndFieldNameAndKeyFieldValue("TR0100","BusinesPartnerBankDetail","collectionAuthorization","1");if (entitySetFieldStatus == null) { entitySetFieldStatus = new BupaRoleEntitySetFieldStatus(null, "TR0100","BusinesPartnerBankDetail","collectionAuthorization","1",false,2);}bupaRoleEntitySetFieldStatusRepository.save(entitySetFieldStatus);
        entitySetFieldStatus = bupaRoleEntitySetFieldStatusRepository.findByBupaRoleCodeAndEntitySetAndFieldNameAndKeyFieldValue("TR0100","BusinesPartnerBankDetail","externalBankId","1");if (entitySetFieldStatus == null) { entitySetFieldStatus = new BupaRoleEntitySetFieldStatus(null, "TR0100","BusinesPartnerBankDetail","externalBankId","1",false,2);}bupaRoleEntitySetFieldStatusRepository.save(entitySetFieldStatus);
        entitySetFieldStatus = bupaRoleEntitySetFieldStatusRepository.findByBupaRoleCodeAndEntitySetAndFieldNameAndKeyFieldValue("TR0100","BusinesPartnerBankDetail","bankAccountName","1");if (entitySetFieldStatus == null) { entitySetFieldStatus = new BupaRoleEntitySetFieldStatus(null, "TR0100","BusinesPartnerBankDetail","bankAccountName","1",false,1);}bupaRoleEntitySetFieldStatusRepository.save(entitySetFieldStatus);
        entitySetFieldStatus = bupaRoleEntitySetFieldStatusRepository.findByBupaRoleCodeAndEntitySetAndFieldNameAndKeyFieldValue("TR0100","BusinesPartnerBankDetail","iBan","1");if (entitySetFieldStatus == null) { entitySetFieldStatus = new BupaRoleEntitySetFieldStatus(null, "TR0100","BusinesPartnerBankDetail","iBan","1",false,1);}bupaRoleEntitySetFieldStatusRepository.save(entitySetFieldStatus);
        entitySetFieldStatus = bupaRoleEntitySetFieldStatusRepository.findByBupaRoleCodeAndEntitySetAndFieldNameAndKeyFieldValue("TR0100","BusinesPartnerBankDetail","iBanFromDate","1");if (entitySetFieldStatus == null) { entitySetFieldStatus = new BupaRoleEntitySetFieldStatus(null, "TR0100","BusinesPartnerBankDetail","iBanFromDate","1",false,1);}bupaRoleEntitySetFieldStatusRepository.save(entitySetFieldStatus);
        entitySetFieldStatus = bupaRoleEntitySetFieldStatusRepository.findByBupaRoleCodeAndEntitySetAndFieldNameAndKeyFieldValue("TR0100","BusinesPartnerBankDetail","moveDate","1");if (entitySetFieldStatus == null) { entitySetFieldStatus = new BupaRoleEntitySetFieldStatus(null, "TR0100","BusinesPartnerBankDetail","moveDate","1",false,1);}bupaRoleEntitySetFieldStatusRepository.save(entitySetFieldStatus);
        entitySetFieldStatus = bupaRoleEntitySetFieldStatusRepository.findByBupaRoleCodeAndEntitySetAndFieldNameAndKeyFieldValue("TR0100","BusinesPartnerBankDetail","moveId","1");if (entitySetFieldStatus == null) { entitySetFieldStatus = new BupaRoleEntitySetFieldStatus(null, "TR0100","BusinesPartnerBankDetail","moveId","1",false,1);}bupaRoleEntitySetFieldStatusRepository.save(entitySetFieldStatus);
        entitySetFieldStatus = bupaRoleEntitySetFieldStatusRepository.findByBupaRoleCodeAndEntitySetAndFieldNameAndKeyFieldValue("TR0100","BusinesPartnerBankDetail","accountType","1");if (entitySetFieldStatus == null) { entitySetFieldStatus = new BupaRoleEntitySetFieldStatus(null, "TR0100","BusinesPartnerBankDetail","accountType","1",false,2);}bupaRoleEntitySetFieldStatusRepository.save(entitySetFieldStatus);



    }
        }

