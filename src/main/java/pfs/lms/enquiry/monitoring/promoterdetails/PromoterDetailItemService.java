package pfs.lms.enquiry.monitoring.promoterdetails;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class PromoterDetailItemService implements IPromoterDetailItemService {

    private final PromoterDetailItemRepository promoterDetailItemRepository;
    private final IChangeDocumentService changeDocumentService;
    private final PromoterDetailRepository promoterDetailRepository;

    @Override
    public PromoterDetailItem savePromoterDetailItem(PromoterDetailItem resource, UUID promoterDetailId,
                                                     Integer itemsCount, String username ) {
        PromoterDetailItem promoterDetailItem = new PromoterDetailItem(
                resource.getSerialNumber(),
                resource.getShareHoldingCompany(),
                resource.getPaidupCapitalEquitySanction(),
                resource.getPaidupCapitalEquityCurrent(),
                resource.getEquityLinkInstrumentSanction(),
                resource.getEquityLinkInstrumentCurrent()
        );
        promoterDetailItem = promoterDetailItemRepository.save(promoterDetailItem);

        PromoterDetail promoterDetail = promoterDetailRepository.getOne(promoterDetailId);



        // Change Documents for Promoter Details
        changeDocumentService.createChangeDocument(
                promoterDetail.getLoanMonitor().getId(), promoterDetail.getId().toString(),null,
                promoterDetail.getLoanMonitor().getLoanApplication().getLoanContractId(),
                null,
                promoterDetail,
                "Created",
                username,
                "Monitoring", "Promoter Details Item");

        return promoterDetailItem;
    }

    @Override
    public PromoterDetailItem updatePromoterDetailItem(PromoterDetailItem resource,
                                                       UUID promoterDetailId,  String username) throws CloneNotSupportedException {

        PromoterDetailItem promoterDetailItem = promoterDetailItemRepository.findById(resource.getId())
                .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

        PromoterDetailItem oldPromoterDetailItem = (PromoterDetailItem) promoterDetailItem.clone();

        PromoterDetail promoterDetail = promoterDetailRepository.getOne(promoterDetailId);



        promoterDetailItem.setShareHoldingCompany(resource.getShareHoldingCompany());
        promoterDetailItem.setPaidupCapitalEquitySanction(resource.getPaidupCapitalEquitySanction());
        promoterDetailItem.setPaidupCapitalEquityCurrent(resource.getPaidupCapitalEquityCurrent());
        promoterDetailItem.setEquityLinkInstrumentSanction(resource.getEquityLinkInstrumentSanction());
        promoterDetailItem.setEquityLinkInstrumentCurrent(resource.getEquityLinkInstrumentCurrent());
        promoterDetailItem = promoterDetailItemRepository.save(promoterDetailItem);

        // Change Documents for Promoter Details
//        changeDocumentService.createChangeDocument(
//                promoterDetail.getLoanMonitor().getId(), promoterDetail.getId().toString(),null,
//                promoterDetail.getLoanMonitor().getLoanApplication().getLoanContractId(),
//                oldPromoterDetailItem,
//                promoterDetail,
//                "Updated",
//                username,
//                "Monitoring", "Promoter Details Item");

        return promoterDetailItem;
    }
}
