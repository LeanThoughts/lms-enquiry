package pfs.lms.enquiry.businesspartner.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.businesspartner.domain.IdentificationCategory;
import pfs.lms.enquiry.businesspartner.repository.IdentificationCategoryRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class IdentificationCategoryConfig implements CommandLineRunner {

    private final IdentificationCategoryRepository identificationCategoryRepository;
 
    @Override
    public void run(String... strings) throws Exception {

        IdentificationCategory identificationCategory = new IdentificationCategory();

        identificationCategory = identificationCategoryRepository.findIdentificationCategoryByCode("INEX01");if (identificationCategory == null){ identificationCategory = new IdentificationCategory(null,"INEX01","Excise Code"); identificationCategoryRepository.save(identificationCategory); }
        identificationCategory = identificationCategoryRepository.findIdentificationCategoryByCode("INEX02");if (identificationCategory == null){ identificationCategory = new IdentificationCategory(null,"INEX02","Excise registration number"); identificationCategoryRepository.save(identificationCategory); }
        identificationCategory = identificationCategoryRepository.findIdentificationCategoryByCode("INEX03");if (identificationCategory == null){ identificationCategory = new IdentificationCategory(null,"INEX03","excise range"); identificationCategoryRepository.save(identificationCategory); }
        identificationCategory = identificationCategoryRepository.findIdentificationCategoryByCode("INEX04");if (identificationCategory == null){ identificationCategory = new IdentificationCategory(null,"INEX04","excise division"); identificationCategoryRepository.save(identificationCategory); }
        identificationCategory = identificationCategoryRepository.findIdentificationCategoryByCode("INEX05");if (identificationCategory == null){ identificationCategory = new IdentificationCategory(null,"INEX05","excise coll"); identificationCategoryRepository.save(identificationCategory); }
        identificationCategory = identificationCategoryRepository.findIdentificationCategoryByCode("INST01");if (identificationCategory == null){ identificationCategory = new IdentificationCategory(null,"INST01","CST No."); identificationCategoryRepository.save(identificationCategory); }
        identificationCategory = identificationCategoryRepository.findIdentificationCategoryByCode("INST02");if (identificationCategory == null){ identificationCategory = new IdentificationCategory(null,"INST02","LST No"); identificationCategoryRepository.save(identificationCategory); }
        identificationCategory = identificationCategoryRepository.findIdentificationCategoryByCode("Z00001");if (identificationCategory == null){ identificationCategory = new IdentificationCategory(null,"Z00001","Tax Account Number(TAN)"); identificationCategoryRepository.save(identificationCategory); }
        identificationCategory = identificationCategoryRepository.findIdentificationCategoryByCode("Z00002");if (identificationCategory == null){ identificationCategory = new IdentificationCategory(null,"Z00002","Permanent Account Number (PAN)"); identificationCategoryRepository.save(identificationCategory); }
        identificationCategory = identificationCategoryRepository.findIdentificationCategoryByCode("Z00004");if (identificationCategory == null){ identificationCategory = new IdentificationCategory(null,"Z00004","Company Registration No (CIN)"); identificationCategoryRepository.save(identificationCategory); }
        identificationCategory = identificationCategoryRepository.findIdentificationCategoryByCode("Z00005");if (identificationCategory == null){ identificationCategory = new IdentificationCategory(null,"Z00005","Tax Identification No (TIN)"); identificationCategoryRepository.save(identificationCategory); }
        identificationCategory = identificationCategoryRepository.findIdentificationCategoryByCode("Z00006");if (identificationCategory == null){ identificationCategory = new IdentificationCategory(null,"Z00006","Service Tax No"); identificationCategoryRepository.save(identificationCategory); }
        identificationCategory = identificationCategoryRepository.findIdentificationCategoryByCode("Z00009");if (identificationCategory == null){ identificationCategory = new IdentificationCategory(null,"Z00009","MSME Number"); identificationCategoryRepository.save(identificationCategory); }
        identificationCategory = identificationCategoryRepository.findIdentificationCategoryByCode("Z00010");if (identificationCategory == null){ identificationCategory = new IdentificationCategory(null,"Z00010","Others"); identificationCategoryRepository.save(identificationCategory); }
        identificationCategory = identificationCategoryRepository.findIdentificationCategoryByCode("Z00011");if (identificationCategory == null){ identificationCategory = new IdentificationCategory(null,"Z00011","GSTIN Number"); identificationCategoryRepository.save(identificationCategory); }
        identificationCategory = identificationCategoryRepository.findIdentificationCategoryByCode("Z00012");if (identificationCategory == null){ identificationCategory = new IdentificationCategory(null,"Z00012","Legal Entity Identifier (LEI) No"); identificationCategoryRepository.save(identificationCategory); }
        identificationCategory = identificationCategoryRepository.findIdentificationCategoryByCode("Z00013");if (identificationCategory == null){ identificationCategory = new IdentificationCategory(null,"Z00013","C-KYC Ref No"); identificationCategoryRepository.save(identificationCategory); }

        return;
    }
}