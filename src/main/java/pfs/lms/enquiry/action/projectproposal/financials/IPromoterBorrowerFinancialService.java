package pfs.lms.enquiry.action.projectproposal.financials;

public interface IPromoterBorrowerFinancialService {

    PromoterBorrowerFinancial create(PromoterBorrowerFinancialResource resource, String username);

    PromoterBorrowerFinancial update(PromoterBorrowerFinancialResource resource, String username) throws CloneNotSupportedException;
}
