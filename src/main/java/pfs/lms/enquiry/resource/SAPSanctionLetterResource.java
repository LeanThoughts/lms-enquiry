package pfs.lms.enquiry.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.boardapproval.BoardApproval;
import pfs.lms.enquiry.boardapproval.approvalbyboard.ApprovalByBoard;
import pfs.lms.enquiry.boardapproval.deferredbyboard.DeferredByBoard;
import pfs.lms.enquiry.boardapproval.reasonfordelay.BoardApprovalReasonForDelay;
import pfs.lms.enquiry.boardapproval.rejectedbyboard.RejectedByBoard;
import pfs.lms.enquiry.boardapproval.rejectedbycustomer.BoardApprovalRejectedByCustomer;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.sanction.sanctionletter.SanctionLetter;
import pfs.lms.enquiry.utils.DataConversionUtility;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SAPSanctionLetterResource implements Serializable {


    public SAPSanctionLetterResource() {
        sapSanctionLetterDetailsResource = new SAPSanctionLetterDetailsResource();
    }

    @JsonProperty(value = "d")
    private SAPSanctionLetterDetailsResource sapSanctionLetterDetailsResource;

    public SAPSanctionLetterDetailsResource getSapSanctionLetterDetailsResource() {
        return sapSanctionLetterDetailsResource;
    }

    public void setSapSanctionLetterDetailsResource(SAPSanctionLetterDetailsResource sapLoanApplicationDetailsResource) {
        this.sapSanctionLetterDetailsResource = sapLoanApplicationDetailsResource;
    }

    public SAPSanctionLetterDetailsResource mapSanctionLetter(SanctionLetter sanctionLetter) throws ParseException {
        sapSanctionLetterDetailsResource.setLoanContract(sanctionLetter.getSanction().getLoanApplication().getLoanContractId());
        sapSanctionLetterDetailsResource.setCompanyCode("PFS");
        sapSanctionLetterDetailsResource.setSerialNo(sanctionLetter.getSerialNumber().toString());

        sapSanctionLetterDetailsResource.setSanctionType("");

         if ( sanctionLetter.getDateOfAmendment() != null) {
            sapSanctionLetterDetailsResource.setAmendSancDate(DataConversionUtility.convertDateToSAPFormat(sanctionLetter.getDateOfAmendment()));
        }else{
            sapSanctionLetterDetailsResource.setAmendSancDate(null);
        }
        if ( sanctionLetter.getSanctionLetterIssueDate() != null) {
            sapSanctionLetterDetailsResource.setSanctionIssueOfferDate(DataConversionUtility.convertDateToSAPFormat(sanctionLetter.getSanctionLetterIssueDate()));
        }else{
            sapSanctionLetterDetailsResource.setSanctionIssueOfferDate(null);
        }
         if ( sanctionLetter.getSanctionLetterValidToDate() != null) {
            sapSanctionLetterDetailsResource.setSanctionOfferDateUntil(DataConversionUtility.convertDateToSAPFormat(sanctionLetter.getSanctionLetterValidToDate()));
        }else{
            sapSanctionLetterDetailsResource.setSanctionOfferDateUntil(null);
        }

        sapSanctionLetterDetailsResource.setSanctionOfferAcceptedFlag("");

        if ( sanctionLetter.getOriginalSanctionAmount() != null) {
            sapSanctionLetterDetailsResource.setOriginalSanctionAmount(DataConversionUtility.convertAmountToString(sanctionLetter.getOriginalSanctionAmount()));
        }else{
            sapSanctionLetterDetailsResource.setOriginalSanctionAmount(null);
        }
        if ( sanctionLetter.getRevisedSanctionAmount() != null) {
            sapSanctionLetterDetailsResource.setRevisedSanctionAmount(DataConversionUtility.convertAmountToString(sanctionLetter.getRevisedSanctionAmount()));
        }else{
            sapSanctionLetterDetailsResource.setRevisedSanctionAmount(null);
        }

        if ( sanctionLetter.getOriginalInterestRate() != null) {
            sapSanctionLetterDetailsResource.setOriginalInterestRate(DataConversionUtility.convertAmountToString(sanctionLetter.getOriginalInterestRate()));
        }else{
            sapSanctionLetterDetailsResource.setOriginalInterestRate(null);
        }
        if ( sanctionLetter.getRevisedInterestRate() != null) {
            sapSanctionLetterDetailsResource.setRevisedSanctionAmount(DataConversionUtility.convertAmountToString(sanctionLetter.getRevisedInterestRate()));
        }else{
            sapSanctionLetterDetailsResource.setRevisedSanctionAmount(null);
        }

        sapSanctionLetterDetailsResource.setDocumentType(sanctionLetter.getDocumentType());
        sapSanctionLetterDetailsResource.setDocumentTypeDesc(sanctionLetter.getDocumentTitle());
        sapSanctionLetterDetailsResource.setRemarks(sanctionLetter.getRemarks());



        return sapSanctionLetterDetailsResource;

    }

}
