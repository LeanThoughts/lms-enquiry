package pfs.lms.enquiry.action.projectproposal.financials;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RepositoryRestController
@RequiredArgsConstructor
public class PromoterBorrowerFinancialController {

    private final IPromoterBorrowerFinancialService promoterBorrowerFinancialService;

    @PreAuthorize("hasAuthority('LoanContracts.ProcessEnquiry.Edit')")
    @PostMapping("/promoterBorrowerFinancials/create")
    public ResponseEntity<PromoterBorrowerFinancial> create(@RequestBody PromoterBorrowerFinancialResource resource,
                                                            HttpServletRequest request) throws Exception {
        return ResponseEntity.ok(promoterBorrowerFinancialService.create(resource, request.getUserPrincipal().getName()));
    }

    @PreAuthorize("hasAuthority('LoanContracts.ProcessEnquiry.Edit')")
    @PutMapping("/promoterBorrowerFinancials/update")
    public ResponseEntity<PromoterBorrowerFinancial> update(@RequestBody PromoterBorrowerFinancialResource resource,
                                                            HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(promoterBorrowerFinancialService.update(resource, request.getUserPrincipal().getName()));
    }
}
