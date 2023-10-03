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
public class SAPLoanApplicationResource implements Serializable {


    public SAPLoanApplicationResource() {
        sapLoanApplicationDetailsResource = new SAPLoanApplicationDetailsResource();
    }

    @JsonProperty(value = "d")
    private SAPLoanApplicationDetailsResource sapLoanApplicationDetailsResource;

    public SAPLoanApplicationDetailsResource getSapLoanApplicationDetailsResource() {
        return sapLoanApplicationDetailsResource;
    }

    public void setSapLoanApplicationDetailsResource(SAPLoanApplicationDetailsResource sapLoanApplicationDetailsResource) {
        this.sapLoanApplicationDetailsResource = sapLoanApplicationDetailsResource;
    }


    public SAPLoanApplicationDetailsResource mapBoardApproval(SAPLoanApplicationDetailsResource detailsResource,
                                                              LoanApplication loanApplication,
                                                              BoardApproval boardApproval,
                                                              List<DeferredByBoard> deferredByBoardList,
                                                              List<BoardApprovalReasonForDelay> boardApprovalReasonForDelayList,
                                                              List<RejectedByBoard> rejectedByBoardRepositoryList,
                                                              List<ApprovalByBoard> approvalByBoardList,
                                                              List<BoardApprovalRejectedByCustomer> boardApprovalRejectedByCustomerList
    ) throws ParseException {


        ApprovalByBoard approvalByBoard = new ApprovalByBoard();
        DeferredByBoard deferredByBoard = new DeferredByBoard();
        BoardApprovalReasonForDelay boardApprovalReasonForDelay = new BoardApprovalReasonForDelay();
        RejectedByBoard rejectedByBoard = new RejectedByBoard();
        BoardApprovalRejectedByCustomer boardApprovalRejectedByCustomer = new BoardApprovalRejectedByCustomer();

        if (boardApproval.getWorkFlowStatusCode() <= 2) {
            return detailsResource;
        }
//        if (loanApplication.getFunctionalStatus() != 4)
//            return detailsResource;

        if (approvalByBoardList.size() > 0)
            approvalByBoard = approvalByBoardList.get(0);
//        if (deferredByBoardList.size() > 0)
//            deferredByBoard = deferredByBoardList.get(0);
        if (rejectedByBoardRepositoryList.size() > 0)
            rejectedByBoard = rejectedByBoardRepositoryList.get(0);
        if (approvalByBoardList.size() > 0)
            approvalByBoard = approvalByBoardList.get(0);
        if (boardApprovalRejectedByCustomerList.size() > 0)
            boardApprovalRejectedByCustomer = boardApprovalRejectedByCustomerList.get(0);

        if (approvalByBoard != null) {
            detailsResource.setBoardMeetingNumber(approvalByBoard.getMeetingNumber().toString());
            if (approvalByBoard.getMeetingDate() != null)
                detailsResource.setBoardApprovalDate(DataConversionUtility.convertDateToSAPFormat(approvalByBoard.getMeetingDate()));
            else
                detailsResource.setBoardApprovalDate(null);

            if (approvalByBoard.getDetails() != null)
                detailsResource.setBoardApprovalRemarks(approvalByBoard.getDetails());
            detailsResource.setbODStatus("4"); //Approved by Board
            return detailsResource;
        }
        if (rejectedByBoard != null) {
            detailsResource.setBoardMeetingNumber(rejectedByBoard.getMeetingNumber().toString());
            if (rejectedByBoard.getMeetingDate() != null)
                detailsResource.setBoardApprovalDate(DataConversionUtility.convertDateToSAPFormat(rejectedByBoard.getMeetingDate()));
            else
                detailsResource.setBoardApprovalDate(null);

            if (rejectedByBoard.getDetails() != null)
                detailsResource.setBoardApprovalRemarks(rejectedByBoard.getDetails());
            detailsResource.setbODStatus("3"); //Rejected by Board
            return detailsResource;
        }
        if (boardApprovalRejectedByCustomer != null) {
            detailsResource.setbODStatus("5"); //Rejected by Customer
            return detailsResource;
        }

        // Use the Latest DeferredByBoard Item
        if (deferredByBoardList != null) {

            // Sort by Board Meeting Date
            Comparator<DeferredByBoard> comparator = (d1, d2) -> {
                return d1.getMeetingDate().compareTo(d2.getMeetingDate());
            };
            Collections.sort(deferredByBoardList, comparator);

            deferredByBoard = deferredByBoardList.get(0);

            detailsResource.setBoardMeetingNumber(deferredByBoard.getMeetingNumber().toString());
            if (deferredByBoard.getMeetingDate() != null)
                detailsResource.setBoardApprovalDate(DataConversionUtility.convertDateToSAPFormat(deferredByBoard.getMeetingDate()));
            else
                detailsResource.setBoardApprovalDate(null);

            if (deferredByBoard.getDetails() != null)
                detailsResource.setBoardApprovalRemarks(deferredByBoard.getDetails());
            detailsResource.setbODStatus("1"); //Deferred by Board
            return detailsResource;
        }


        return detailsResource;

    }

    public SAPLoanApplicationDetailsResource
    mapLoanApplicationToSAP(LoanApplication loanApplication, Partner partner, User lastProcessedBy) throws ParseException {

        SAPLoanApplicationDetailsResource detailsResource = new SAPLoanApplicationDetailsResource();

        detailsResource.setLoanApplicationId(Long.toString(loanApplication.getEnquiryNo().getId()));
        detailsResource.setPartnerExternalNumber(" ");
        detailsResource.setPartnerRole("TR0100");
        detailsResource.setName1(partner.getPartyName1());
        detailsResource.setName2(partner.getPartyName2() == null ? "" : partner.getPartyName2());
        if (partner.getPartyCategory() != null)
            detailsResource.setPartnerCategory(partner.getPartyCategory().toString());

        if (partner.getEmail().contains("@") == false) {
            partner.setEmail(partner.getEmail() + "@dummy.co.in");
        }

        detailsResource.setEmail(partner.getEmail());
        detailsResource.setCity(partner.getCity());
        detailsResource.setState(partner.getState());
        detailsResource.setPostalCode(partner.getPostalCode());
        detailsResource.setHouseNo(partner.getAddressLine1());
        detailsResource.setStreet(partner.getStreet());
        detailsResource.setCountry("IN");
        detailsResource.setPanNumber(partner.getPan());
        detailsResource.setContactPerName(partner.getContactPersonName());
        detailsResource.setIndustrySector(partner.getIndustrySector());
        if (partner.getPartyNumber() != null)
            detailsResource.setBusPartnerNumber(partner.getPartyNumber().toString());

        detailsResource.setApplicationDate("\\/Date(" + System.currentTimeMillis() + ")\\/");


        detailsResource.setProjectCapaacity(loanApplication.getProjectCapacity() == null ? "0.00" :
                String.format("%.2f", loanApplication.getProjectCapacity()));
        if (loanApplication.getProjectCapacityUnit() != null) {
            detailsResource.setProjectCapacityUnit(loanApplication.getProjectCapacityUnit());
        } else {
            detailsResource.setProjectCapacityUnit("");
        }
        //String myDate = "2014/10/29 18:10:45";
        if (loanApplication.getScheduledCOD() != null) {
            String scheduledCOD = loanApplication.getScheduledCOD().toString();
            scheduledCOD = scheduledCOD + " 01:01:01";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(scheduledCOD);
            long millis = date.getTime();
            detailsResource.setScheduledCommDate("\\/Date(" + millis + ")\\/");
        } else {
            detailsResource.setScheduledCommDate(null);
        }

        detailsResource.setProjectCostInCrores(loanApplication.getProjectCost() == null ? "0.000" :
                String.format("%.3f", loanApplication.getProjectCost()));
        detailsResource.setDebtAmountInCrores(loanApplication.getProjectDebtAmount() == null ? "0.000" :
                String.format("%.3f", loanApplication.getProjectDebtAmount()));
        detailsResource.setEquityAmountInCrores(loanApplication.getEquity() == null ? "0.000" :
                String.format("%.3f", loanApplication.getEquity()));
        detailsResource.setCurrency("INR");
        detailsResource.setApplicationCapitalInCrores(loanApplication.getPfsDebtAmount() == null ? "0.000" :
                String.format("%.3f", loanApplication.getPfsDebtAmount()));

        detailsResource.setLoanPurpose(loanApplication.getPurposeOfLoan());
        detailsResource.setPurpose(loanApplication.getLoanPurpose()); //Demand Letter Text
        detailsResource.setLoanClass(loanApplication.getLoanClass());
        detailsResource.setFinancingType(loanApplication.getFinancingType());
        detailsResource.setDebtEquityIndicator(loanApplication.getAssistanceType());
        detailsResource.setProjectType(loanApplication.getProjectType());
        detailsResource.setProjectTypeCoreSector(loanApplication.getProjectTypeCoreSector());
        detailsResource.setLoanType(loanApplication.getLoanType());
        detailsResource.setTerm(loanApplication.getTerm());


        detailsResource.setGroupCompanyName(loanApplication.getGroupCompany());
        detailsResource.setPromoterName(loanApplication.getPromoterName());
        detailsResource.setPromoterPATInCrores(loanApplication.getPromoterPATAmount() == null ? "0.000" :
                String.format("%.3f", loanApplication.getPromoterPATAmount()));
        detailsResource.setPromoterAreaOfBusiness(loanApplication.getPromoterAreaOfBusinessNature());
        detailsResource.setPromoterRating(loanApplication.getRating());
        detailsResource.setPromoterNetWorthInCrores(loanApplication.getPromoterNetWorthAmount() == null ? "0.000" :
                String.format("%.3f", loanApplication.getPromoterNetWorthAmount()));
        detailsResource.setPromoterKeyDirector(loanApplication.getPromoterKeyDirector());


        detailsResource.setProjectName(loanApplication.getProjectName());

        detailsResource.setTenorYear(loanApplication.getTenorYear().toString());
        detailsResource.setTenorMonth(loanApplication.getTenorMonth().toString());

        detailsResource.setLoanProduct(loanApplication.getProductCode());
        detailsResource.setProjectState(loanApplication.getProjectLocationState());
        detailsResource.setProjectDistrict(loanApplication.getProjectDistrict());

        detailsResource.setContactBranchAddress(loanApplication.getContactBranchAddress());
        detailsResource.setContactDepartment(loanApplication.getContactDepartment());
        detailsResource.setContactDesignation(loanApplication.getContactDesignation());
        if (loanApplication.getContactEmail() != null) {
            if (loanApplication.getContactEmail().contains("@") == false) {
                loanApplication.setContactEmail(loanApplication.getContactEmail() + "@dummy.co.in");
            }
        }
        detailsResource.setContactEmail(loanApplication.getContactEmail());
        detailsResource.setContactFaxNumber(loanApplication.getContactFaxNumber());
        detailsResource.setContactLandLinePhone(loanApplication.getContactLandLinePhone());
        detailsResource.setContactTelePhone(loanApplication.getContactTelePhone());

        detailsResource.setConstructionPeriod(loanApplication.getConstructionPeriod().toString());
        detailsResource.setConstructionPeriodUnit(loanApplication.getConstructionPeriodUnit());
        detailsResource.setMoratoriumPeriod(loanApplication.getMoratoriumPeriod().toString());
        detailsResource.setMoratoriumPeriodUnit(loanApplication.getMoratoriumPeriodUnit());

        //Board Approval
        detailsResource.setbODStatus(loanApplication.getbODStatus());
        detailsResource.setBoardMeetingNumber(loanApplication.getBoardMeetingNumber());
        detailsResource.setBoardApprovalRemarks(loanApplication.getBoardApprovalRemarks());
        if (loanApplication.getBoardApprovalDate() != null)
            detailsResource.setBoardApprovalDate(DataConversionUtility.convertDateToSAPFormat(loanApplication.getBoardApprovalDate()));
        else
            detailsResource.setBoardApprovalDate(null);

        //ICC
        detailsResource.setiCCStatus(loanApplication.getiCCStatus());
        detailsResource.setiCCMeetNumber(loanApplication.getiCCMeetNumber());
        detailsResource.setiCCRemarks(loanApplication.getiCCRemarks());
        if (loanApplication.getiCCClearanceDate() != null)
            detailsResource.setiCCClearanceDate(DataConversionUtility.convertDateToSAPFormat(loanApplication.getiCCClearanceDate()));
        else
            detailsResource.setiCCClearanceDate(null);

        //Enquiry Completion
        if (loanApplication.getEnquiryCompletionDate() != null)
            detailsResource.setEnquiryCompletionDate(DataConversionUtility.convertDateToSAPFormat(loanApplication.getEnquiryCompletionDate()));
        else
            detailsResource.setEnquiryCompletionDate(null);
        detailsResource.setEnquiryRemarks(loanApplication.getEnquiryRemarks());

        //Fee
        if (loanApplication.getTermSheetAcceptance() != null)
            detailsResource.setTermSheetAcceptance(DataConversionUtility.convertDateToSAPFormat(loanApplication.getTermSheetAcceptance()));
        else
            detailsResource.setTermSheetAcceptance(null);
        detailsResource.setFeeRemarks(loanApplication.getFeeRemarks());



        detailsResource.setLoanContract(loanApplication.getLoanContractId());

        // Set Loan Officer
        if (lastProcessedBy != null)
            detailsResource.setLoanOfficer(lastProcessedBy.getSapBPNumber());

        detailsResource.setLoanStatus(this.getLoanStatus(loanApplication.getFunctionalStatus(), loanApplication.getTechnicalStatus()));

        detailsResource.setiCCClearanceDate(null);

        return detailsResource;
    }

    private String getLoanStatus(Integer functionalStatus, Integer technicalStatus) {

        String loanStatus = "";

        switch (functionalStatus) {
            case 1: //01-Enquiry Stage
                loanStatus = "1"; //Application
            case 2: // 02-ICC ApprovalStage
                loanStatus = "1"; //Application
            case 3: //03-Appraisal Stage
                loanStatus = "1"; //Application
            case 4: //04-Board Approval Stage
                loanStatus = "1"; //Application
            case 5: //05-Sanction Stage
                if (technicalStatus == 4)
                    loanStatus = "20"; //Offer
                else
                    loanStatus = "1"; //Application
            case 6: //06-Loan Documentation Stage
                loanStatus = "60"; //Contract
            case 7: //07-Disbursement Stage
                loanStatus = "60"; //Contract
            default:
                loanStatus = "1"; //Application
        }
        return loanStatus;

    }
}
