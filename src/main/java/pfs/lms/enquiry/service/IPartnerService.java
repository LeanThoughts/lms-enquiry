package pfs.lms.enquiry.service;

import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.resource.PartnerResourceByEmail;
import pfs.lms.enquiry.resource.PartnerResourcesOrderByAlphabet;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface IPartnerService {
    Partner getOne(String username);
    Partner save(Partner partner);
    Partner migrate(Partner partner);
    Partner update(Partner partner);
    List<Partner> searchPartners(String[] searchParameters);
    PartnerResourcesOrderByAlphabet getPartnersOrderedByAlphabets();

    List<PartnerResourceByEmail> getPartnerByEmail();

    List<Partner> getAllPartners();

    List<Partner>   getPartnerByEmail(String email);

    List<Partner> getLendersIndependentEngineers();
    List<Partner> getLendersFinancialAdvisors();
    List<Partner> getTRAAuthorizedPersons();

    List<Partner> getPartnersByRoleType(String roleType);

    Partner migratePartner(Partner partner, HttpServletRequest httpServletRequest);
}
