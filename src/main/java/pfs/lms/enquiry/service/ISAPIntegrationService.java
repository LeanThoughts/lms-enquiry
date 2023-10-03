package pfs.lms.enquiry.service;

import pfs.lms.enquiry.resource.SAPLoanApplicationDetailsResource;
import pfs.lms.enquiry.resource.SAPLoanApplicationResource;
import pfs.lms.enquiry.resource.SAPSanctionLetterResource;

public interface ISAPIntegrationService {

    String fetchCSRFToken();

    SAPLoanApplicationResource postLoanApplication(SAPLoanApplicationResource sapLoanApplicationResource);
    SAPSanctionLetterResource  postSanctionLetter(SAPSanctionLetterResource sapSanctionLetterResource);


    void getLoanApplication(String loanApplicationId);
}
