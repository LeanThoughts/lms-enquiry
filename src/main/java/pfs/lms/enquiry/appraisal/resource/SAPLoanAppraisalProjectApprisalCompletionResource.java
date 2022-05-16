package pfs.lms.enquiry.appraisal.resource;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.appraisal.customerrejection.CustomerRejection;
import pfs.lms.enquiry.appraisal.projectappraisalcompletion.ProjectAppraisalCompletion;
import pfs.lms.enquiry.utils.DataConversionUtility;

import javax.activation.DataContentHandler;
import java.io.Serializable;
import java.text.ParseException;

@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties (ignoreUnknown = true)

public class SAPLoanAppraisalProjectApprisalCompletionResource implements Serializable {

    public SAPLoanAppraisalProjectApprisalCompletionResource() {
        sapLoanAppraisalProjectAppraisalCompletionResourceDetails = new SAPLoanAppraisalProjectAppraisalCompletionResourceDetails();
    }

    @JsonProperty(value = "d")
    private SAPLoanAppraisalProjectAppraisalCompletionResourceDetails sapLoanAppraisalProjectAppraisalCompletionResourceDetails;

    DataConversionUtility dataConversionUtility;


    public void setSAPLoanAppraisalProjectAppraisalCompletionResourceDetails(pfs.lms.enquiry.appraisal.resource.SAPLoanAppraisalProjectAppraisalCompletionResourceDetails SAPLoanAppraisalProjectAppraisalCompletionResourceDetails) {
        this.sapLoanAppraisalProjectAppraisalCompletionResourceDetails = SAPLoanAppraisalProjectAppraisalCompletionResourceDetails;
    }

    public SAPLoanAppraisalProjectAppraisalCompletionResourceDetails
                                mapProjectAppraisalCompletionToSAP(ProjectAppraisalCompletion projectAppraisalCompletion) throws ParseException {

        SAPLoanAppraisalProjectAppraisalCompletionResourceDetails detailsResource = new SAPLoanAppraisalProjectAppraisalCompletionResourceDetails();

        detailsResource.setAppraisalId(projectAppraisalCompletion.getLoanAppraisal().getId().toString());
        detailsResource.setId(projectAppraisalCompletion.getId().toString());


        if (detailsResource.getDateOfProjectAppraisalCompletion() != null)
            detailsResource.setDateOfProjectAppraisalCompletion(dataConversionUtility.convertDateToSAPFormat(projectAppraisalCompletion.getDateOfProjectAppraisalCompletion()));
        else
            detailsResource.setDateOfProjectAppraisalCompletion(null);

        if (detailsResource.getAgendaNoteApprovalByDirA() != null)
            detailsResource.setAgendaNoteApprovalByDirA(dataConversionUtility.convertDateToSAPFormat(projectAppraisalCompletion.getAgendaNoteApprovalByDirA()));
        else
            detailsResource.setAgendaNoteApprovalByDirA(null);

        if (detailsResource.getAgendaNoteApprovalByDirB() != null)
            detailsResource.setAgendaNoteApprovalByDirB(dataConversionUtility.convertDateToSAPFormat(projectAppraisalCompletion.getAgendaNoteApprovalByDirB()));
        else
            detailsResource.setAgendaNoteApprovalByDirB(null);

        if (detailsResource.getAgendaNoteSubmissionToCoSecy() != null)
            detailsResource.setAgendaNoteSubmissionToCoSecy(dataConversionUtility.convertDateToSAPFormat(projectAppraisalCompletion.getAgendaNoteSubmissionToCoSecy()));
        else
            detailsResource.setAgendaNoteSubmissionToCoSecy(null);

        if (detailsResource.getAgendaNoteApprovalByMDAndCEO() != null)
            detailsResource.setDateOfProjectAppraisalCompletion(dataConversionUtility.convertDateToSAPFormat(projectAppraisalCompletion.getAgendaNoteApprovalByMDAndCEO()));
        else
            detailsResource.setAgendaNoteApprovalByMDAndCEO(null);



        detailsResource.setRemarks(detailsResource.getRemarks());


        return detailsResource;
    }



}
