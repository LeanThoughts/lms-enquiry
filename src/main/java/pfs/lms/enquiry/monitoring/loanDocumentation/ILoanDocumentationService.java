package pfs.lms.enquiry.monitoring.loanDocumentation;

import pfs.lms.enquiry.monitoring.npa.NPA;
import pfs.lms.enquiry.monitoring.npa.NPAResource;

import java.util.List;
import java.util.UUID;

public interface ILoanDocumentationService {

    LoanDocumentation saveLoanDocumentation(LoanDocumentationResource resource, String username);

    LoanDocumentation updateLoanDocumentation(LoanDocumentationResource resource, String username) throws CloneNotSupportedException;

    List<LoanDocumentation> getLoanDocumentation(String loanApplicationId, String name);

    LoanDocumentation deleteLoanDocumentation(UUID loanDocumentationId, String moduleName, String username);
}
