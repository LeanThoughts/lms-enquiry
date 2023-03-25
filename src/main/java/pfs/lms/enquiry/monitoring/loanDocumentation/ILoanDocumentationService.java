package pfs.lms.enquiry.monitoring.loanDocumentation;

import pfs.lms.enquiry.monitoring.npa.NPA;
import pfs.lms.enquiry.monitoring.npa.NPAResource;

import java.util.List;

public interface ILoanDocumentationService {

    LoanDocumentation saveLoanDocumentation(LoanDocumentationResource resource, String username);

    LoanDocumentation updateLoanDocumentation(LoanDocumentationResource resource, String username) throws CloneNotSupportedException;

    List<LoanDocumentation> getLoanDocumentation(String loanApplicationId, String name);
}
