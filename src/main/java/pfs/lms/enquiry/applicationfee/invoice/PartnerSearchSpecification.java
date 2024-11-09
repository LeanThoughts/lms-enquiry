package pfs.lms.enquiry.applicationfee.invoice;

import org.springframework.data.jpa.domain.Specification;

import pfs.lms.enquiry.domain.Partner;

public class PartnerSearchSpecification {

    public static Specification<Partner> partyName1Contains(String partyName1) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("partyName1"), "%" + partyName1 + "%");
    }

    public static Specification<Partner> partyNumberContains(String partyNumber) {
        // Convert partyNumber to String to avoid type mismatch error
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("partyNumber").as(String.class), "%" + partyNumber + "%");
    }

    public static Specification<Partner> searchTerm1Contains(String searchTerm1) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("searchTerm1"), "%" + searchTerm1 + "%");
    }

    public static Specification<Partner> searchTerm2Contains(String searchTerm2) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("searchTerm2"), "%" + searchTerm2 + "%");
    }
}
