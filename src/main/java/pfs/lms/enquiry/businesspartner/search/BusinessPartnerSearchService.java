package pfs.lms.enquiry.businesspartner.search;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerRoleType;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.exception.LmsException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Business partner search of Master Data > Business Partners: all criteria combined (AND), paged and sorted in the
 * database, so large partner lists are not loaded at once.
 */
@Service
@Transactional(readOnly = true)
public class BusinessPartnerSearchService {

    public static final int DEFAULT_PAGE_SIZE = 25;
    public static final int MAX_PAGE_SIZE = 200;
    /** Sort keys accepted by {@link #search}: name (default), number, role */
    public static final List<String> SORTS = Arrays.asList("name", "number", "role");

    @PersistenceContext
    private EntityManager entityManager;

    /** For tests */
    void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public BusinessPartnerSearchResult search(BusinessPartnerSearchCriteria criteria, Integer page, Integer size, String sort) {
        BusinessPartnerSearchCriteria selection = criteria == null ? new BusinessPartnerSearchCriteria() : criteria;
        int pageSize = size == null || size < 1 ? DEFAULT_PAGE_SIZE : Math.min(size, MAX_PAGE_SIZE);
        String sortKey = sort == null || !SORTS.contains(sort.trim().toLowerCase(Locale.ROOT)) ? "name"
                : sort.trim().toLowerCase(Locale.ROOT);
        Integer[] range = numberRange(selection);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Partner> countRoot = countQuery.from(Partner.class);
        countQuery.select(cb.count(countRoot)).where(conditions(cb, countRoot, selection, range));
        long total = entityManager.createQuery(countQuery).getSingleResult();

        int lastPage = total == 0 ? 0 : (int) ((total - 1) / pageSize);
        int pageIndex = page == null || page < 0 ? 0 : Math.min(page, lastPage);

        CriteriaQuery<Partner> query = cb.createQuery(Partner.class);
        Root<Partner> root = query.from(Partner.class);
        query.select(root).where(conditions(cb, root, selection, range)).orderBy(order(cb, root, sortKey));
        List<Partner> partners = entityManager.createQuery(query)
                .setFirstResult(pageIndex * pageSize)
                .setMaxResults(pageSize)
                .getResultList();

        Map<String, String> roleTexts = roleTexts();
        BusinessPartnerSearchResult result = new BusinessPartnerSearchResult();
        result.setRows(partners.stream().map(partner -> row(partner, roleTexts)).collect(Collectors.toList()));
        result.setPage(pageIndex);
        result.setSize(pageSize);
        result.setTotal(total);
        result.setSort(sortKey);
        return result;
    }

    private static Predicate[] conditions(CriteriaBuilder cb, Root<Partner> root, BusinessPartnerSearchCriteria selection,
                                          Integer[] range) {
        List<Predicate> conditions = new ArrayList<>();
        String name = text(selection.getName());
        if (name != null) {
            Expression<String> fullName = cb.concat(cb.concat(cb.coalesce(root.<String>get("partyName1"), ""), " "),
                    cb.coalesce(root.<String>get("partyName2"), ""));
            conditions.add(like(cb, fullName, name));
        }
        String email = text(selection.getEmail());
        if (email != null) {
            conditions.add(like(cb, root.get("email"), email));
        }
        String pan = text(selection.getPan());
        if (pan != null) {
            conditions.add(like(cb, root.get("pan"), pan));
        }
        String searchTerm = text(selection.getSearchTerm());
        if (searchTerm != null) {
            conditions.add(cb.or(like(cb, root.get("searchTerm1"), searchTerm), like(cb, root.get("searchTerm2"), searchTerm)));
        }
        String role = text(selection.getPartnerRole());
        if (role != null) {
            conditions.add(cb.equal(root.get("defaultPartnerRole"), role));
        }
        if (range[0] != null) {
            conditions.add(cb.greaterThanOrEqualTo(root.<Integer>get("partyNumber"), range[0]));
        }
        if (range[1] != null) {
            conditions.add(cb.lessThanOrEqualTo(root.<Integer>get("partyNumber"), range[1]));
        }
        return conditions.toArray(new Predicate[0]);
    }

    private static Predicate like(CriteriaBuilder cb, Expression<String> column, String value) {
        String pattern = "%" + value.toLowerCase(Locale.ROOT).replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_") + "%";
        return cb.like(cb.lower(column), pattern, '\\');
    }

    private static List<Order> order(CriteriaBuilder cb, Root<Partner> root, String sort) {
        List<Order> order = new ArrayList<>();
        if ("number".equals(sort)) {
            order.add(cb.asc(root.get("partyNumber")));
        } else if ("role".equals(sort)) {
            order.add(cb.asc(root.get("defaultPartnerRole")));
        }
        order.add(cb.asc(cb.lower(root.<String>get("partyName1"))));
        order.add(cb.asc(cb.lower(root.<String>get("partyName2"))));
        order.add(cb.asc(root.get("id")));
        return order;
    }

    /**
     * From and To of the business partner numbers: only From is that one number (as before); only To is every number
     * up to it.
     */
    private static Integer[] numberRange(BusinessPartnerSearchCriteria selection) {
        Integer from = number(selection.getPartnerNumberFrom(), "Business Partner Number From");
        Integer to = number(selection.getPartnerNumberTo(), "Business Partner Number To");
        if (from != null && to == null) {
            to = from;
        }
        if (from != null && to < from) {
            throw new LmsException("Business Partner Number To must not be lower than Business Partner Number From.",
                    HttpStatus.PRECONDITION_FAILED);
        }
        return new Integer[]{from, to};
    }

    private static Integer number(String value, String label) {
        String text = text(value);
        if (text == null) {
            return null;
        }
        try {
            return Integer.valueOf(text);
        } catch (NumberFormatException e) {
            throw new LmsException(label + " must be a number.", HttpStatus.PRECONDITION_FAILED);
        }
    }

    private static String text(String value) {
        if (value == null) {
            return null;
        }
        String text = value.trim();
        return text.isEmpty() ? null : text;
    }

    private Map<String, String> roleTexts() {
        Map<String, String> texts = new HashMap<>();
        for (BusinessPartnerRoleType role : entityManager.createQuery("select r from BusinessPartnerRoleType r",
                BusinessPartnerRoleType.class).getResultList()) {
            if (role.getCode() != null) {
                texts.putIfAbsent(role.getCode().trim(), role.getValue());
            }
        }
        return texts;
    }

    private static BusinessPartnerSearchResult.Row row(Partner partner, Map<String, String> roleTexts) {
        BusinessPartnerSearchResult.Row row = new BusinessPartnerSearchResult.Row();
        row.setId(partner.getId() != null ? partner.getId().toString() : null);
        row.setPartyNumber(partner.getPartyNumber());
        row.setPartyName1(partner.getPartyName1());
        row.setPartyName2(partner.getPartyName2());
        String name = ((partner.getPartyName1() == null ? "" : partner.getPartyName1().trim()) + " "
                + (partner.getPartyName2() == null ? "" : partner.getPartyName2().trim())).trim();
        row.setPartyName(name);
        row.setDefaultPartnerRole(partner.getDefaultPartnerRole());
        if (partner.getDefaultPartnerRole() != null) {
            row.setDefaultPartnerRoleText(roleTexts.get(partner.getDefaultPartnerRole().trim()));
        }
        row.setPartnerGroup(partner.getPartnerGroup());
        row.setPartnerCategory(partner.getPartnerCategory());
        row.setPan(partner.getPan());
        row.setSearchTerm1(partner.getSearchTerm1());
        row.setSearchTerm2(partner.getSearchTerm2());
        row.setContactPersonName(partner.getContactPersonName());
        row.setEmail(partner.getEmail());
        row.setAddressLine1(partner.getAddressLine1());
        row.setAddressLine2(partner.getAddressLine2());
        row.setCity(partner.getCity());
        row.setWorkFlowStatusDescription(partner.getWorkFlowStatusDescription());
        return row;
    }
}
