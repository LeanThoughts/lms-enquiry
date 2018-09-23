package pfs.lms.enquiry.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.domain.Partner;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanApplicationResource {

    private LoanApplication loanApplication;
    private Partner partner;
}
