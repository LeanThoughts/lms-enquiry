package pfs.lms.enquiry.businesspartner.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.businesspartner.domain.*;
import pfs.lms.enquiry.businesspartner.repository.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(5)
public class BupaRoleEntityFieldStatusAllRolesConfig implements CommandLineRunner {

    private final BupaRoleEntityFieldStatusRepository bupaRoleEntityFieldStatusRepository;
    private final BusinessPartnerRoleTypeRepository businessPartnerRoleTypeRepository;
    private final BupaRoleEntitySetFieldStatusRepository bupaRoleEntitySetFieldStatusRepository;
    private final BusinessPartnerRoleTypePartnerGroupRepository businessPartnerRoleTypePartnerGroupRepository;
    private final PartnerGroupRepository partnerGroupRepository;

    @Override
    public void run(String... strings) throws Exception {

        BupaRoleEntityFieldStatus entityFieldStatus = new BupaRoleEntityFieldStatus();

        List<BusinessPartnerRoleType> businessPartnerRoleTypes = businessPartnerRoleTypeRepository.findAll();

        List<BupaRoleEntityFieldStatus> bupaRoleEntityFieldStatusList = bupaRoleEntityFieldStatusRepository.findByBupaRoleCode("TR0100");
        List<BupaRoleEntitySetFieldStatus> bupaRoleSetEntityFieldStatusList = bupaRoleEntitySetFieldStatusRepository.findByBupaRoleCode("TR0100");
        List<PartnerGroup> partnerGroupList = new ArrayList<>();

        List<BupaRoleEntityFieldStatus> bupaRoleEntityFieldStatusListForCreate = new ArrayList<>();

        for (BusinessPartnerRoleType roleType : businessPartnerRoleTypes) {
            if (roleType.getCode().equals("TR0100")) {continue;}

            partnerGroupList.clear();

            List<BupaRoleEntityFieldStatus> entityFieldStatusList = bupaRoleEntityFieldStatusRepository.findByBupaRoleCode(roleType.getCode());
            if ( entityFieldStatusList.size() > 0 ) {
                continue;
            } else {
                for (BupaRoleEntityFieldStatus fieldStatus : bupaRoleEntityFieldStatusList) {
                    BupaRoleEntityFieldStatus fieldStatusForCreate = new BupaRoleEntityFieldStatus();
                    fieldStatusForCreate.setBupaRoleCode(roleType.getCode());
                    fieldStatusForCreate.setEntity(fieldStatus.getEntity());
                    fieldStatusForCreate.setFieldName(fieldStatus.getFieldName());
                    fieldStatusForCreate.setFieldStatus(fieldStatus.getFieldStatus());

                    if (fieldStatus.getFieldName().equals("partyNumber") ){
                        List<BusinessPartnerRoleTypePartnerGroup> businessPartnerRoleTypePartnerGroups = businessPartnerRoleTypePartnerGroupRepository.findByRoleType(roleType.getCode());
                        for (BusinessPartnerRoleTypePartnerGroup businessPartnerRoleTypePartnerGroup : businessPartnerRoleTypePartnerGroups) {
                            PartnerGroup partnerGroup = partnerGroupRepository.findByCode(businessPartnerRoleTypePartnerGroup.getPartnerGroup());
                            if (partnerGroup.getExternalNumberRange() != null) {
                                if (partnerGroup.getExternalNumberRange() == true)
                                    partnerGroupList.add(partnerGroup);
                            }
                        }

                        if (partnerGroupList.size() > 0) {
                            fieldStatusForCreate.setFieldStatus(2);
                        }
                    }
                    fieldStatusForCreate.setId(null);
                    bupaRoleEntityFieldStatusListForCreate.add(fieldStatusForCreate);
                }

                bupaRoleEntityFieldStatusRepository.saveAll(bupaRoleEntityFieldStatusListForCreate);
                bupaRoleEntityFieldStatusListForCreate.clear();

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