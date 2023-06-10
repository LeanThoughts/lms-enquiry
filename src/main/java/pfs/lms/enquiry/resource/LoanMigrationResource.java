package pfs.lms.enquiry.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pfs.lms.enquiry.appraisal.loanpartner.LoanPartner;
import pfs.lms.enquiry.appraisal.projectlocation.MainLocationDetail;
import pfs.lms.enquiry.appraisal.projectlocation.SubLocationDetail;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.domain.LoanContractExtension;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.monitoring.npa.NPA;
import pfs.lms.enquiry.monitoring.npa.NPADetail;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class LoanMigrationResource {

    private LoanApplication loanApplication;
    private Partner partner; //Main Loan Partner (TR0100)
    private LoanContractExtension loanContractExtension;
    private List<LoanPartner> loanPartners;
    private MainLocationDetail mainLocationDetail;
    private List<SubLocationDetail> subLocationDetailList;
    private NPA npa;
    private List<NPADetail> npaDetailList;
}
