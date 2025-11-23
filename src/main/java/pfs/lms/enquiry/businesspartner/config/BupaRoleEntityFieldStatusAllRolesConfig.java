package pfs.lms.enquiry.businesspartner.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.businesspartner.domain.BupaRoleEntityFieldStatus;
import pfs.lms.enquiry.businesspartner.domain.BupaRoleEntitySetFieldStatus;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerRoleType;
import pfs.lms.enquiry.businesspartner.repository.BupaRoleEntityFieldStatusRepository;
import pfs.lms.enquiry.businesspartner.repository.BupaRoleEntitySetFieldStatusRepository;
import pfs.lms.enquiry.businesspartner.repository.BusinessPartnerRoleTypeRepository;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BupaRoleEntityFieldStatusAllRolesConfig implements CommandLineRunner {

    private final BupaRoleEntityFieldStatusRepository bupaRoleEntityFieldStatusRepository;
    private final BusinessPartnerRoleTypeRepository businessPartnerRoleTypeRepository;
    private final BupaRoleEntitySetFieldStatusRepository bupaRoleEntitySetFieldStatusRepository;

    @Override
    public void run(String... strings) throws Exception {

        BupaRoleEntityFieldStatus entityFieldStatus = new BupaRoleEntityFieldStatus();

        List<BusinessPartnerRoleType> businessPartnerRoleTypes = businessPartnerRoleTypeRepository.findAll();

        List<BupaRoleEntityFieldStatus> bupaRoleEntityFieldStatusList = bupaRoleEntityFieldStatusRepository.findByBupaRoleCode("TR0100");
        List<BupaRoleEntitySetFieldStatus> bupaRoleSetEntityFieldStatusList = bupaRoleEntitySetFieldStatusRepository.findByBupaRoleCode("TR0100");

        for (BusinessPartnerRoleType roleType : businessPartnerRoleTypes) {
            if (roleType.getCode().equals("TR0100")) {
                continue;
            }

            List<BupaRoleEntityFieldStatus> entityFieldStatusList = bupaRoleEntityFieldStatusRepository.findByBupaRoleCode(roleType.getCode());
            if ( entityFieldStatusList.size() > 0 ) {
                continue;
            } else {
                for (BupaRoleEntityFieldStatus fieldStatus : bupaRoleEntityFieldStatusList) {
                    fieldStatus.setBupaRoleCode(roleType.getCode());
                    fieldStatus.setId(null);
                }
                bupaRoleEntityFieldStatusRepository.saveAll(bupaRoleEntityFieldStatusList);
            }



        }

        for (BusinessPartnerRoleType roleType : businessPartnerRoleTypes) {
            if (roleType.getCode().equals("TR0100")) {
                continue;
            }

            List<BupaRoleEntitySetFieldStatus> entitySetFieldStatusList = bupaRoleEntitySetFieldStatusRepository.findByBupaRoleCode(roleType.getCode());
            if (entitySetFieldStatusList.size() > 0){
                continue;
            } else {
                for (BupaRoleEntitySetFieldStatus entitySetFieldStatus: bupaRoleSetEntityFieldStatusList){
                    entitySetFieldStatus.setBupaRoleCode(roleType.getCode());
                    entitySetFieldStatus.setId(null);
                    entitySetFieldStatusList.add(entitySetFieldStatus);
                }
                bupaRoleEntitySetFieldStatusRepository.saveAll(entitySetFieldStatusList);
            }

        }





    }
}