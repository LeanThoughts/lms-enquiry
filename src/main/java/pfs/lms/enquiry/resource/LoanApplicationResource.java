package pfs.lms.enquiry.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pfs.lms.enquiry.appraisal.projectlocation.MainLocationDetail;
import pfs.lms.enquiry.appraisal.projectlocation.SubLocationDetail;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.domain.Partner;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class LoanApplicationResource {

    private LoanApplication loanApplication;
    private Partner partner;
    private String  projectTypeDesc;
    private String  financingTypeDesc;
    private String  loanClassDesc;
    private MainLocationDetail mainLocationDetail;
    private List<SubLocationDetail> subLocationDetailList;
 }
