package pfs.lms.enquiry.appraisal.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;
import pfs.lms.enquiry.monitoring.resource.SAPLoanMonitorResourceDetails;
import pfs.lms.enquiry.monitoring.resource.SAPMonitorHeaderResourceDetails;

import java.io.Serializable;
import java.text.ParseException;

@Component
@JsonInclude (JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties (ignoreUnknown = true)
public class SAPLoanAppraisalHeaderResource implements Serializable   {



    public SAPLoanAppraisalHeaderResource() {
        sapMonitorHeaderResourceDetails = new SAPLoanAppraisalHeaderResourceDetails();
    }

    @JsonProperty(value = "d")
    private SAPLoanAppraisalHeaderResourceDetails sapMonitorHeaderResourceDetails;



    public void setsapMonitorHeaderResourceDetails (SAPLoanAppraisalHeaderResourceDetails sapLoanAppraisalHeaderResourceDetails) {
        this.sapMonitorHeaderResourceDetails = sapLoanAppraisalHeaderResourceDetails;
    }


    public SAPLoanAppraisalHeaderResourceDetails
                    mapLoanAppraisalHeaderToSAP(LoanAppraisal loanAppraisal ) throws ParseException {

        SAPLoanAppraisalHeaderResourceDetails detailsResource = new SAPLoanAppraisalHeaderResourceDetails();

        detailsResource.setLoanContract(loanAppraisal.getLoanApplication().getLoanContractId());
        detailsResource.setAppraisalId(loanAppraisal.getId().toString());
        detailsResource.setProcessInstanceId(loanAppraisal.getProcessInstanceId());
        if (loanAppraisal.getWorkFlowStatusCode() != null)
            detailsResource.setWorkflowStatusCode(loanAppraisal.getWorkFlowStatusCode().toString());
        else
            detailsResource.setWorkflowStatusCode(null);
        if (loanAppraisal.getWorkFlowStatusDescription() != null)
            detailsResource.setWorkflowStatusDesc(loanAppraisal.getWorkFlowStatusDescription());
        else
            detailsResource.setWorkflowStatusCode(null);

        return detailsResource;
    }
}
