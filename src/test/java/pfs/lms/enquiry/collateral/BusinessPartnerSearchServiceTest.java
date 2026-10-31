package pfs.lms.enquiry.collateral;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerRoleType;
import pfs.lms.enquiry.businesspartner.repository.BusinessPartnerRoleTypeRepository;
import pfs.lms.enquiry.businesspartner.search.BusinessPartnerSearchCriteria;
import pfs.lms.enquiry.businesspartner.search.BusinessPartnerSearchResult;
import pfs.lms.enquiry.businesspartner.search.BusinessPartnerSearchService;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.repository.PartnerRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/** Master Data > Business Partners: the paged search (pfs.lms.enquiry.businesspartner.search). */
@DisplayName("Business partner search")
class BusinessPartnerSearchServiceTest extends CollateralTestSupport {

    private BusinessPartnerSearchService service;

    @BeforeAll
    void setUp() {
        service = bean(BusinessPartnerSearchService.class);
        BusinessPartnerRoleType trustee = new BusinessPartnerRoleType();
        trustee.setCode("TR0151");
        trustee.setValue("Security Trustee");
        bean(BusinessPartnerRoleTypeRepository.class).save(trustee);
        partner(20000021, "Brickwork", "Ratings", "TR0151", "BRICKWORK", "RATING", "AADCB3136C", "info@brickwork.test");
        partner(20000001, "Canara Bank", null, "TR0151", "CANARA", "BANK", "PANNOTAVL1", "trustee@canara.test");
        partner(50000042, "BSES Rajdhani Power Ltd", null, "FLVN00", "BSES", "POWER", "PANNOTAVL208", null);
        for (int i = 0; i < 30; i++) {
            partner(60000000 + i, "Zeta Solar SPV " + i, null, "FLVN00", "ZETA", null, null, null);
        }
    }

    private void partner(int number, String name1, String name2, String role, String term1, String term2, String pan, String email) {
        Partner partner = new Partner();
        partner.setId(UUID.randomUUID());
        partner.setPartyNumber(number);
        partner.setPartyName1(name1);
        partner.setPartyName2(name2);
        partner.setDefaultPartnerRole(role);
        partner.setSearchTerm1(term1);
        partner.setSearchTerm2(term2);
        partner.setPan(pan);
        partner.setEmail(email);
        bean(PartnerRepository.class).save(partner);
    }

    private static BusinessPartnerSearchCriteria criteria() {
        return new BusinessPartnerSearchCriteria();
    }

    private static List<Integer> numbers(BusinessPartnerSearchResult result) {
        return result.getRows().stream().map(BusinessPartnerSearchResult.Row::getPartyNumber).collect(Collectors.toList());
    }

    @Test
    @DisplayName("search term (term 1 or 2) and partner role, combined; the role text comes with each row")
    void searchTermAndRole() {
        BusinessPartnerSearchCriteria byTerm = criteria();
        byTerm.setSearchTerm("rating");
        assertThat(numbers(service.search(byTerm, 0, 25, null))).containsExactly(20000021);

        BusinessPartnerSearchCriteria byRole = criteria();
        byRole.setPartnerRole("TR0151");
        BusinessPartnerSearchResult trustees = service.search(byRole, 0, 25, null);
        assertThat(numbers(trustees)).containsExactly(20000021, 20000001);
        assertThat(trustees.getRows().get(0).getDefaultPartnerRoleText()).isEqualTo("Security Trustee");
        assertThat(trustees.getRows().get(0).getPartyName()).isEqualTo("Brickwork Ratings");

        byRole.setSearchTerm("bank");
        assertThat(numbers(service.search(byRole, 0, 25, null))).containsExactly(20000001);
        byRole.setPan("pannotavl1");
        byRole.setName("canara");
        assertThat(numbers(service.search(byRole, 0, 25, null))).containsExactly(20000001);
    }

    @Test
    @DisplayName("paged in the database: 30 matches, 25 per page, the last page has 5; sort by number")
    void paging() {
        BusinessPartnerSearchCriteria zeta = criteria();
        zeta.setSearchTerm("ZETA");
        BusinessPartnerSearchResult first = service.search(zeta, 0, 25, "number");
        assertThat(first.getTotal()).isEqualTo(30);
        assertThat(first.getRows()).hasSize(25);
        assertThat(first.getRows().get(0).getPartyNumber()).isEqualTo(60000000);
        BusinessPartnerSearchResult last = service.search(zeta, 9, 25, "number");
        assertThat(last.getPage()).isEqualTo(1);
        assertThat(last.getRows()).hasSize(5);
        assertThat(service.search(zeta, 0, 1000, null).getSize()).isEqualTo(200);
    }

    @Test
    @DisplayName("number range: From alone is one number, both a range; wrong ranges and letters are refused")
    void numberRange() {
        BusinessPartnerSearchCriteria range = criteria();
        range.setPartnerNumberFrom("20000001");
        assertThat(numbers(service.search(range, 0, 25, null))).containsExactly(20000001);
        range.setPartnerNumberTo("50000042");
        assertThat(numbers(service.search(range, 0, 25, "number"))).containsExactly(20000001, 20000021, 50000042);
        range.setPartnerNumberTo("1");
        assertThat(expectError(412, () -> service.search(range, 0, 25, null))).contains("must not be lower");
        range.setPartnerNumberTo("abc");
        assertThat(expectError(412, () -> service.search(range, 0, 25, null))).contains("must be a number");
        BusinessPartnerSearchCriteria wildcard = criteria();
        wildcard.setName("%");
        assertThat(service.search(wildcard, 0, 25, null).getTotal()).isZero();
    }
}
