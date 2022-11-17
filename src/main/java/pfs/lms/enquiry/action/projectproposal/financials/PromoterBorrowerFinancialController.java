package pfs.lms.enquiry.action.projectproposal.financials;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RepositoryRestController
@RequiredArgsConstructor
public class PromoterBorrowerFinancialController {

    private final IPromoterBorrowerFinancialService promoterBorrowerFinancialService;

    @PostMapping("/promoterBorrowerFinancials/create")
    public ResponseEntity<PromoterBorrowerFinancial> create(@RequestBody PromoterBorrowerFinancialResource resource,
                                                            HttpServletRequest request) {
        return ResponseEntity.ok(promoterBorrowerFinancialService.create(resource, request.getUserPrincipal().getName()));
    }

    @PutMapping("/promoterBorrowerFinancials/update")
    public ResponseEntity<PromoterBorrowerFinancial> update(@RequestBody PromoterBorrowerFinancialResource resource,
                                                            HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(promoterBorrowerFinancialService.update(resource, request.getUserPrincipal().getName()));
    }
}
