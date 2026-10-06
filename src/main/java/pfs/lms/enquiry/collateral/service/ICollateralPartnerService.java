package pfs.lms.enquiry.collateral.service;

import pfs.lms.enquiry.collateral.dto.PartnerSearchResultDto;
import pfs.lms.enquiry.collateral.dto.ValueEntryDto;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/** Business partners for the Security Trustee, Security Agent and Custodian fields (Partner.partyNumber). */
public interface ICollateralPartnerService {

    /** Maximum number of partners a search returns. */
    int MAX_RESULTS = 200;

    /**
     * Partners whose fields contain the given texts (case-insensitive; all given criteria must match).
     * Only partners with a party number are returned.
     */
    List<PartnerSearchResultDto> search(String name1, String name2, String defaultPartnerRole, String searchTerm1,
                                        String searchTerm2);

    /** Business partner roles for the "Default Partner Role" criterion. */
    List<ValueEntryDto> getPartnerRoles();

    /** Name ("name 1 name 2") of each party number that belongs to a partner. */
    Map<String, String> getNames(Collection<String> partyNumbers);

    /** True when a partner with this party number exists. */
    boolean exists(String partyNumber);
}
