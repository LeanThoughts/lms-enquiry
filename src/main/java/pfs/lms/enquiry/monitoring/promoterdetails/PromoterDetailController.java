package pfs.lms.enquiry.monitoring.promoterdetails;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Slf4j
@RepositoryRestController
@AllArgsConstructor
public class PromoterDetailController {

    private final IPromoterDetailItemService promoterDetailItemService;

    @PostMapping("/promoterDetailItems/create")
    public ResponseEntity<PromoterDetailItem> createPromoterDetailItem(@RequestBody PromoterDetailItemResource resource,
                                                                        HttpServletRequest request)
            throws CloneNotSupportedException {

        PromoterDetailItem promoterDetailItem =
                promoterDetailItemService.savePromoterDetailItem(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(promoterDetailItem);
    }

    @PutMapping("/promoterDetailItems/update")
    public ResponseEntity<PromoterDetailItem> updatePromoterDetailItem(@RequestBody PromoterDetailItemResource resource,
                                                                       HttpServletRequest request)
            throws CloneNotSupportedException {

        PromoterDetailItem promoterDetailItem =
                promoterDetailItemService.updatePromoterDetailItem(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(promoterDetailItem);
    }

    @GetMapping("/loanApplications/{loanapplicationid}/promoterDetailItems")
    public ResponseEntity<List<PromoterDetailItem>> getPromoterDetailItems(
            @PathVariable("loanapplicationid") UUID loanApplicationId, HttpServletRequest request) {

        List<PromoterDetailItem> promoterDetailItems = promoterDetailItemService.getPromoterDetailItems(loanApplicationId);
        return ResponseEntity.ok(promoterDetailItems);
    }
}
