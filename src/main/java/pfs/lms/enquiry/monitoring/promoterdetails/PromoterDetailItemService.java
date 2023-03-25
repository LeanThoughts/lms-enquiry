package pfs.lms.enquiry.monitoring.promoterdetails;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;
import pfs.lms.enquiry.monitoring.repository.LoanMonitorRepository;
import pfs.lms.enquiry.monitoring.service.impl.LoanMonitoringService;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class PromoterDetailItemService implements IPromoterDetailItemService {

    private final PromoterDetailItemRepository promoterDetailItemRepository;
    private final IChangeDocumentService changeDocumentService;
    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanMonitoringService loanMonitoringService;
    private final LoanMonitorRepository loanMonitorRepository;

//    @Override
//    public PromoterDetailItem savePromoterDetailItem(PromoterDetailItem resource, UUID promoterDetailId,
//                                                     Integer itemsCount, String username ) {
//        PromoterDetailItem promoterDetailItem = new PromoterDetailItem(
//                resource.getSerialNumber(),
//                resource.getShareHoldingCompany(),
//                resource.getPaidupCapitalEquitySanction(),
//                resource.getPaidupCapitalEquityCurrent(),
//                resource.getEquityLinkInstrumentSanction(),
//                resource.getEquityLinkInstrumentCurrent(),
//                resource.getDateOfChange(),
//                resource.getGroupExposure()
//        );
//        promoterDetailItem = promoterDetailItemRepository.save(promoterDetailItem);
//
//        PromoterDetail promoterDetail = promoterDetailRepository.getOne(promoterDetailId);
//
//
//
//        // Change Documents for Promoter Details
//        changeDocumentService.createChangeDocument(
//                promoterDetail.getLoanMonitor().getId(), promoterDetail.getId().toString(),null,
//                promoterDetail.getLoanMonitor().getLoanApplication().getLoanContractId(),
//                null,
//                promoterDetail,
//                "Created",
//                username,
//                "Monitoring", "Promoter Details Item");
//
//        return promoterDetailItem;
//    }

//    @Override
//    public PromoterDetailItem updatePromoterDetailItem(PromoterDetailItem resource,
//                                                       UUID promoterDetailId,  String username) throws CloneNotSupportedException {
//
//        PromoterDetailItem promoterDetailItem = promoterDetailItemRepository.findById(resource.getId())
//                .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));
//
//        PromoterDetailItem oldPromoterDetailItem = (PromoterDetailItem) promoterDetailItem.clone();
//
//        PromoterDetail promoterDetail = promoterDetailRepository.getOne(promoterDetailId);
//
//
//
//        promoterDetailItem.setShareHoldingCompany(resource.getShareHoldingCompany());
//        promoterDetailItem.setPaidupCapitalEquitySanction(resource.getPaidupCapitalEquitySanction());
//        promoterDetailItem.setPaidupCapitalEquityCurrent(resource.getPaidupCapitalEquityCurrent());
//        promoterDetailItem.setEquityLinkInstrumentSanction(resource.getEquityLinkInstrumentSanction());
//        promoterDetailItem.setEquityLinkInstrumentCurrent(resource.getEquityLinkInstrumentCurrent());
//        promoterDetailItem = promoterDetailItemRepository.save(promoterDetailItem);
//
//        // Change Documents for Promoter Details
////        changeDocumentService.createChangeDocument(
////                promoterDetail.getLoanMonitor().getId(), promoterDetail.getId().toString(),null,
////                promoterDetail.getLoanMonitor().getLoanApplication().getLoanContractId(),
////                oldPromoterDetailItem,
////                promoterDetail,
////                "Updated",
////                username,
////                "Monitoring", "Promoter Details Item");
//
//        return promoterDetailItem;
//    }

    @Override
    public PromoterDetailItem savePromoterDetailItem(PromoterDetailItemResource resource, String username)
            throws CloneNotSupportedException {

        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());
        LoanMonitor loanMonitor = loanMonitoringService.createLoanMonitor(loanApplication, username);
        PromoterDetailItem promoterDetailItem = new PromoterDetailItem();
        promoterDetailItem.setLoanMonitor(loanMonitor);
        promoterDetailItem.setSerialNumber(promoterDetailItemRepository.
                findByLoanMonitorOrderBySerialNumber(loanMonitor).size() + 1);
        promoterDetailItem.setEquityLinkInstrumentCurrent(resource.getPromoterDetailItem().getEquityLinkInstrumentCurrent());
        promoterDetailItem.setEquityLinkInstrumentSanction(resource.getPromoterDetailItem().getEquityLinkInstrumentSanction());
        promoterDetailItem.setPaidupCapitalEquityCurrent(resource.getPromoterDetailItem().getPaidupCapitalEquityCurrent());
        promoterDetailItem.setPaidupCapitalEquitySanction(resource.getPromoterDetailItem().getPaidupCapitalEquitySanction());
        promoterDetailItem.setShareHoldingCompany(resource.getPromoterDetailItem().getShareHoldingCompany());
        promoterDetailItem.setDateOfChange(resource.getPromoterDetailItem().getDateOfChange());
        promoterDetailItem.setGroupExposure(resource.getPromoterDetailItem().getGroupExposure());
        promoterDetailItem = promoterDetailItemRepository.save(promoterDetailItem);
        return promoterDetailItem;
    }

    @Override
    public PromoterDetailItem updatePromoterDetailItem(PromoterDetailItemResource resource, String username)
            throws CloneNotSupportedException {

        PromoterDetailItem promoterDetailItem = promoterDetailItemRepository.getOne(resource.getPromoterDetailItem().getId());
        promoterDetailItem.setEquityLinkInstrumentCurrent(resource.getPromoterDetailItem().getEquityLinkInstrumentCurrent());
        promoterDetailItem.setEquityLinkInstrumentSanction(resource.getPromoterDetailItem().getEquityLinkInstrumentSanction());
        promoterDetailItem.setPaidupCapitalEquityCurrent(resource.getPromoterDetailItem().getPaidupCapitalEquityCurrent());
        promoterDetailItem.setPaidupCapitalEquitySanction(resource.getPromoterDetailItem().getPaidupCapitalEquitySanction());
        promoterDetailItem.setShareHoldingCompany(resource.getPromoterDetailItem().getShareHoldingCompany());
        promoterDetailItem.setDateOfChange(resource.getPromoterDetailItem().getDateOfChange());
        promoterDetailItem.setGroupExposure(resource.getPromoterDetailItem().getGroupExposure());
        promoterDetailItem = promoterDetailItemRepository.save(promoterDetailItem);
        return promoterDetailItem;
    }

    @Override
    public List<PromoterDetailItem> getPromoterDetailItems(UUID loanApplicationId) {

        LoanApplication loanApplication = loanApplicationRepository.getOne(loanApplicationId);
        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        List<PromoterDetailItem> promoterDetailItems = promoterDetailItemRepository.
                findByLoanMonitorOrderBySerialNumber(loanMonitor);
        return promoterDetailItems;
    }
}
