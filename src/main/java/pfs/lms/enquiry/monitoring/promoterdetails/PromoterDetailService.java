package pfs.lms.enquiry.monitoring.promoterdetails;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.monitoring.repository.LoanMonitorRepository;
import pfs.lms.enquiry.service.changedocs.impl.ChangeDocumentService;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class PromoterDetailService implements IPromoterDetailService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanMonitorRepository loanMonitorRepository;
    private final ChangeDocumentService changeDocumentService;
    private final PromoterDetailRepository promoterDetailRepository;
    private final IPromoterDetailItemService promoterDetailItemService;

    @Override
    public PromoterDetail savePromoterDetails(PromoterDetailResource resource, String username) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());

        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if(loanMonitor == null)
        {
            loanMonitor = new LoanMonitor();
            loanMonitor.setLoanApplication(loanApplication);
            loanMonitor = loanMonitorRepository.save(loanMonitor);

            // Change Documents for Monitoring Header
            changeDocumentService.createChangeDocument(
                    loanMonitor.getId(), loanMonitor.getId().toString(),null,
                    loanApplication.getLoanContractId(),
                    null,
                    loanMonitor,
                    "Created",
                    username,
                    "Monitoring", "Header");
        }
        PromoterDetail promoterDetail = resource.getPromoterDetail();
        promoterDetail.setLoanMonitor(loanMonitor);
        promoterDetail.setDateOfChange(resource.getPromoterDetail().getDateOfChange());
        promoterDetail.setGroupExposure(resource.getPromoterDetail().getGroupExposure());
        promoterDetail = promoterDetailRepository.save(promoterDetail);

        for (PromoterDetailItem resourceItem : resource.getPromoterDetail().getPromoterDetailItemSet()) {
            if (resourceItem.getId() == null) {
                PromoterDetailItem promoterDetailItem = promoterDetailItemService.savePromoterDetailItem(resourceItem,
                        resource.getPromoterDetail().getId(),
                        resource.getPromoterDetail().getPromoterDetailItemSet().size(),username);
                promoterDetail.getPromoterDetailItemSet().add(promoterDetailItem);
                promoterDetail = promoterDetailRepository.save(promoterDetail);
            }
        }

        return promoterDetail;
    }

    @Override
    @Transactional
    public PromoterDetail updatePromoterDetails(PromoterDetailResource resource, String username) throws CloneNotSupportedException {
        final PromoterDetail existingPromoterDetail = promoterDetailRepository
                .findById(resource.getPromoterDetail().getId())
                .orElseThrow(() -> new EntityNotFoundException(resource.getPromoterDetail().getId().toString()));

        Object oldPromoterDetails = existingPromoterDetail.clone();

        existingPromoterDetail.setDateOfChange(resource.getPromoterDetail().getDateOfChange());
        existingPromoterDetail.setGroupExposure(resource.getPromoterDetail().getGroupExposure());
        PromoterDetail promoterDetail = promoterDetailRepository.save(existingPromoterDetail);

        for (PromoterDetailItem resourceItem : resource.getPromoterDetail().getPromoterDetailItemSet()) {
            if (resourceItem.getId() == null) {
                PromoterDetailItem promoterDetailItem = promoterDetailItemService.savePromoterDetailItem(resourceItem,
                        resource.getPromoterDetail().getId(),
                        resource.getPromoterDetail().getPromoterDetailItemSet().size(),username);
                promoterDetail.getPromoterDetailItemSet().add(promoterDetailItem);
                promoterDetail = promoterDetailRepository.save(existingPromoterDetail);
            } else {
                promoterDetailItemService.updatePromoterDetailItem(resourceItem, resource.getPromoterDetail().getId(),username);
                promoterDetail = promoterDetailRepository.getOne(promoterDetail.getId());
            }
        }

        // Change Documents for Promoter Details
        changeDocumentService.createChangeDocument(
                promoterDetail.getLoanMonitor().getId(), promoterDetail.getId().toString(),null,
                promoterDetail.getLoanMonitor().getLoanApplication().getLoanContractId(),
                null,
                oldPromoterDetails,
                "Updated",
                username,
                "Monitoring", "Promoter Details");


        return promoterDetail;
    }

    @Override
    public List<PromoterDetailResource> getPromoterDetails(String loanApplicationId, String name) {
        List<PromoterDetailResource> promoterDetailResources = new ArrayList<>();
        LoanApplication loanApplication = loanApplicationRepository.getOne(UUID.fromString(loanApplicationId));
        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if(loanMonitor != null) {
            List<PromoterDetail> promoterDetails
                    = promoterDetailRepository.findByLoanMonitor(loanMonitor);
            promoterDetails.forEach(
                    promoterDetail -> {
                        PromoterDetailResource promoterDetailResourceResource = new PromoterDetailResource();
                        promoterDetailResourceResource.setLoanApplicationId(loanApplication.getId());
                        promoterDetailResourceResource.setPromoterDetail(promoterDetail);
                        promoterDetailResources.add(promoterDetailResourceResource);
                    }
            );
        }

        return promoterDetailResources;
    }
}
