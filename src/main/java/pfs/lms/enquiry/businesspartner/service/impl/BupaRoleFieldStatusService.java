package pfs.lms.enquiry.businesspartner.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.businesspartner.domain.BupaRoleEntityFieldStatus;
import pfs.lms.enquiry.businesspartner.domain.BupaRoleEntitySetFieldStatus;
import pfs.lms.enquiry.businesspartner.repository.BupaRoleEntityFieldStatusRepository;
import pfs.lms.enquiry.businesspartner.repository.BupaRoleEntitySetFieldStatusRepository;
import pfs.lms.enquiry.businesspartner.resource.BupaRoleFieldStatusResource;
import pfs.lms.enquiry.businesspartner.service.IBupaRoleFieldStatusService;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BupaRoleFieldStatusService implements IBupaRoleFieldStatusService {

    private final BupaRoleEntityFieldStatusRepository bupaRoleEntityFieldStatusRepository;
    private final BupaRoleEntitySetFieldStatusRepository bupaRoleEntitySetFieldStatusRepository;

    @Override
    public BupaRoleFieldStatusResource getFieldStatusByBupaRole(String businessPartnerRole) {
        BupaRoleFieldStatusResource bupaRoleFieldStatusResource = new BupaRoleFieldStatusResource();

        List<BupaRoleEntityFieldStatus> bupaRoleFieldStatusList = bupaRoleEntityFieldStatusRepository.findByBupaRoleCode(businessPartnerRole);
        List<BupaRoleEntitySetFieldStatus> bupaRoleEntityFieldStatusList = bupaRoleEntitySetFieldStatusRepository.findByBupaRoleCode(businessPartnerRole);

        bupaRoleFieldStatusResource.setBupaRoleEntityFieldStatusList(bupaRoleFieldStatusList);
        bupaRoleFieldStatusResource.setBupaRoleEntitySetFieldStatusList(bupaRoleEntityFieldStatusList);

        return bupaRoleFieldStatusResource;
    }
}
