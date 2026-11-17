package pfs.lms.enquiry.businesspartner.search;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pfs.lms.enquiry.config.ApiController;

/**
 * Business partner search with paging (under /api from {@link ApiController}):
 * {@code GET /business-partners/search?name=&email=&pan=&searchTerm=&partnerRole=&partnerNumberFrom=&partnerNumberTo=&page=0&size=25&sort=name}.
 * All parameters are optional and combined; page is 0 based, size at most 200, sort is name, number or role.
 */
@ApiController
public class BusinessPartnerSearchController {

    private final BusinessPartnerSearchService service;

    public BusinessPartnerSearchController(BusinessPartnerSearchService service) {
        this.service = service;
    }

    @GetMapping("/business-partners/search")
    public ResponseEntity<BusinessPartnerSearchResult> search(@RequestParam(required = false) String name,
                                                              @RequestParam(required = false) String email,
                                                              @RequestParam(required = false) String pan,
                                                              @RequestParam(required = false) String searchTerm,
                                                              @RequestParam(required = false) String partnerRole,
                                                              @RequestParam(required = false) String partnerNumberFrom,
                                                              @RequestParam(required = false) String partnerNumberTo,
                                                              @RequestParam(required = false) Integer page,
                                                              @RequestParam(required = false) Integer size,
                                                              @RequestParam(required = false) String sort) {
        BusinessPartnerSearchCriteria criteria = new BusinessPartnerSearchCriteria();
        criteria.setName(name);
        criteria.setEmail(email);
        criteria.setPan(pan);
        criteria.setSearchTerm(searchTerm);
        criteria.setPartnerRole(partnerRole);
        criteria.setPartnerNumberFrom(partnerNumberFrom);
        criteria.setPartnerNumberTo(partnerNumberTo);
        return ResponseEntity.ok(service.search(criteria, page, size, sort));
    }
}
