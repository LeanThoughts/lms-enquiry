package pfs.lms.enquiry.service;

import org.springframework.data.domain.Pageable;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.resource.LoanApplicationResource;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ILoanApplicationService {

    LoanApplication save(LoanApplicationResource resource, String username) throws InterruptedException;


    LoanApplication migrate(LoanApplicationResource resource, String username);
    LoanApplication migrateUpdate(LoanApplication loanApplication, Partner partner, String username);
    List<LoanApplication> searchLoans(HttpServletRequest request, Pageable pageable);

}
