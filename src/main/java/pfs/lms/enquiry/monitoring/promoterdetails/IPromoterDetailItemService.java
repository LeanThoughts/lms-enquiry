package pfs.lms.enquiry.monitoring.promoterdetails;

import java.util.List;
import java.util.UUID;

public interface IPromoterDetailItemService {

    PromoterDetailItem savePromoterDetailItem(PromoterDetailItemResource resource, String username)
            throws CloneNotSupportedException;

    PromoterDetailItem updatePromoterDetailItem(PromoterDetailItemResource resource, String username)
            throws CloneNotSupportedException;

    List<PromoterDetailItem> getPromoterDetailItems(UUID loanApplicationId);
}
