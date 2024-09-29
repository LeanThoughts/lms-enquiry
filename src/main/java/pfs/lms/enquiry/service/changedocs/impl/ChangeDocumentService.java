package pfs.lms.enquiry.service.changedocs.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.ValueChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.action.EnquiryAction;
import pfs.lms.enquiry.action.enquiryactionreasonfordelay.EnquiryActionReasonForDelay;
import pfs.lms.enquiry.action.enquirycompletion.EnquiryCompletion;
import pfs.lms.enquiry.action.otherdetail.OtherDetail;
import pfs.lms.enquiry.action.projectproposal.ProjectProposal;
import pfs.lms.enquiry.action.projectproposal.collateraldetail.CollateralDetail;
import pfs.lms.enquiry.action.projectproposal.creditrating.CreditRating;
import pfs.lms.enquiry.action.projectproposal.dealguaranteetimeline.DealGuaranteeTimeline;
import pfs.lms.enquiry.action.projectproposal.financials.PromoterBorrowerFinancial;
import pfs.lms.enquiry.action.projectproposal.otherdetailsdocument.OtherDetailsDocument;
import pfs.lms.enquiry.action.projectproposal.projectcost.ProjectCost;
import pfs.lms.enquiry.action.projectproposal.projectdetail.ProjectDetail;
import pfs.lms.enquiry.action.projectproposal.projectproposalotherdetail.ProjectProposalOtherDetail;
import pfs.lms.enquiry.action.projectproposal.shareholder.ShareHolder;
import pfs.lms.enquiry.action.rejectbycustomer.RejectByCustomer;
import pfs.lms.enquiry.action.rejectbypfs.RejectByPfs;
import pfs.lms.enquiry.applicationfee.ApplicationFee;
import pfs.lms.enquiry.applicationfee.applicationfee.InceptionFee;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.appraisal.LoanAppraisalRepository;
import pfs.lms.enquiry.appraisal.customerrejection.CustomerRejection;
import pfs.lms.enquiry.appraisal.riskrating.ExternalRating;
import pfs.lms.enquiry.appraisal.securitytrustee.SecurityTrustee;
import pfs.lms.enquiry.appraisal.securitytrustee.SecurityTrusteeReportAndFee;
import pfs.lms.enquiry.iccapproval.iccfurtherdetail.ICCFurtherDetail;
import pfs.lms.enquiry.appraisal.knowyourcustomer.KnowYourCustomer;
import pfs.lms.enquiry.appraisal.loanpartner.LoanPartner;
import pfs.lms.enquiry.appraisal.projectappraisalcompletion.ProjectAppraisalCompletion;
import pfs.lms.enquiry.appraisal.projectdata.ProjectData;
import pfs.lms.enquiry.appraisal.projectlocation.MainLocationDetail;
import pfs.lms.enquiry.appraisal.projectlocation.SubLocationDetail;
import pfs.lms.enquiry.appraisal.proposaldetails.ProposalDetail;
import pfs.lms.enquiry.appraisal.reasonfordelay.ReasonForDelay;
import pfs.lms.enquiry.appraisal.riskrating.CorporateLoanRiskRating;
import pfs.lms.enquiry.appraisal.riskrating.TermLoanRiskRating;
import pfs.lms.enquiry.appraisal.syndicateconsortium.SyndicateConsortium;
import pfs.lms.enquiry.boardapproval.BoardApproval;
import pfs.lms.enquiry.boardapproval.approvalbyboard.ApprovalByBoard;
import pfs.lms.enquiry.boardapproval.deferredbyboard.DeferredByBoard;
import pfs.lms.enquiry.boardapproval.reasonfordelay.BoardApprovalReasonForDelay;
import pfs.lms.enquiry.boardapproval.rejectedbyboard.RejectedByBoard;
import pfs.lms.enquiry.boardapproval.rejectedbycustomer.BoardApprovalRejectedByCustomer;
import pfs.lms.enquiry.domain.*;
import pfs.lms.enquiry.iccapproval.risknotification.RiskNotification;
import pfs.lms.enquiry.monitoring.borrowerfinancials.BorrowerFinancials;
import pfs.lms.enquiry.monitoring.domain.*;
import pfs.lms.enquiry.monitoring.endusecertificate.EndUseCertificate;
import pfs.lms.enquiry.monitoring.insurance.Insurance;
import pfs.lms.enquiry.monitoring.lfa.LFAReportAndFee;
import pfs.lms.enquiry.monitoring.lfa.LendersFinancialAdvisor;
import pfs.lms.enquiry.monitoring.lia.LIAReportAndFee;
import pfs.lms.enquiry.monitoring.lia.LendersInsuranceAdvisor;
import pfs.lms.enquiry.monitoring.lie.LIEReportAndFee;
import pfs.lms.enquiry.monitoring.lie.LendersIndependentEngineer;
import pfs.lms.enquiry.monitoring.llc.LLCReportAndFee;
import pfs.lms.enquiry.monitoring.llc.LendersLegalCouncil;
import pfs.lms.enquiry.monitoring.loanDocumentation.LoanDocumentation;
import pfs.lms.enquiry.monitoring.npa.NPA;
import pfs.lms.enquiry.monitoring.npa.NPADetail;
import pfs.lms.enquiry.monitoring.operatingparameters.OperatingParameter;
import pfs.lms.enquiry.monitoring.operatingparameters.OperatingParameterPLF;
import pfs.lms.enquiry.monitoring.projectmonitoring.ProjectMonitoringDataItemHistory;
import pfs.lms.enquiry.monitoring.projectmonitoring.ProjectMonitoringDataItemResource;
import pfs.lms.enquiry.monitoring.promoterfinancials.PromoterFinancials;
import pfs.lms.enquiry.monitoring.tra.TrustRetentionAccount;
import pfs.lms.enquiry.monitoring.tra.TrustRetentionAccountStatement;
import pfs.lms.enquiry.monitoring.valuer.Valuer;
import pfs.lms.enquiry.monitoring.valuer.ValuerReportAndFee;
import pfs.lms.enquiry.repository.ChangeDocumentRepository;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.riskassessment.RiskAssessment;
import pfs.lms.enquiry.riskassessment.preliminaryriskassessment.PreliminaryRiskAssessment;
import pfs.lms.enquiry.sanction.Sanction;
import pfs.lms.enquiry.sanction.paymentreceiptpostsanction.PaymentReceiptPostSanction;
import pfs.lms.enquiry.sanction.paymentreceiptpresanction.PaymentReceiptPreSanction;
import pfs.lms.enquiry.sanction.reasonfordelay.SanctionReasonForDelay;
import pfs.lms.enquiry.sanction.rejectedbycustomer.SanctionRejectedByCustomer;
import pfs.lms.enquiry.sanction.sanctionletter.SanctionLetter;
import pfs.lms.enquiry.service.ISAPIntegrationPointerService;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.transaction.Transactional;
import java.util.*;
import java.util.regex.Pattern;

import static org.javers.core.diff.ListCompareAlgorithm.LEVENSHTEIN_DISTANCE;

/**
 * Created by sajeev on 17-Dec-18.
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class ChangeDocumentService implements IChangeDocumentService {

    String pattern = "[$^A-Za-z0-9]+";
    @Autowired
    ChangeDocumentRepository changeDocumentRepository;

    ChangeDocument changeDocument;

    @Autowired
    LoanApplicationRepository loanApplicationRepository;
    @Autowired
    LoanAppraisalRepository loanAppraisalRepository;


    @Autowired
    ISAPIntegrationPointerService sapIntegrationPointerService;

    // C - Create
    // U - Update
    // D - Delete
    private char mode;


    @Override
    public ChangeDocument createChangeDocument(UUID loanBusinessProcessObjectId, String entityId, String mainEntityId,
                                               String loanContractId,
                                               Object oldObject,
                                               Object changedObject,
                                               String action,
                                               String userName,
                                               String businessProcessName,
                                               String subProcessName) {


        changeDocument = new ChangeDocument();

        switch (action) {
            case "Created":
//                if (objectId == null) {
//                    changeDocument.setAction("Created");
                changeDocument = prepareCreateChangeDocument(loanBusinessProcessObjectId, entityId, mainEntityId,
                        loanContractId,
                        changedObject,
                        action,
                        userName,
                        businessProcessName, subProcessName);
                this.mode = 'C';

                break;
            case "Updated":
//                changeDocument.setAction("Sent for Approval");
                changeDocument = prepareUpdateChangeDocument(loanBusinessProcessObjectId, entityId, mainEntityId,
                        loanContractId,
                        oldObject,
                        changedObject,
                        action,
                        userName,
                        businessProcessName, subProcessName);

                this.mode = 'U';
                break;
            case "Deleted":
                changeDocument.setAction("Deleted");
                changeDocument = prepareCreateChangeDocument(loanBusinessProcessObjectId, entityId, mainEntityId,
                        loanContractId,
                        changedObject,
                        action,
                        userName,
                        businessProcessName, subProcessName);
                this.mode = 'D';
                break;
            case "Sent for Approval":
                changeDocument.setAction("Rejected");
                changeDocument = changeDocument = prepareUpdateChangeDocument(loanBusinessProcessObjectId, entityId, mainEntityId,
                        loanContractId,
                        oldObject,
                        changedObject,
                        action,
                        userName,
                        businessProcessName, subProcessName);
                this.mode = 'U';
                break;
            case "Approved":
                changeDocument.setAction("Approved");
                changeDocument = changeDocument = prepareUpdateChangeDocument(loanBusinessProcessObjectId, entityId, mainEntityId,
                        loanContractId,
                        oldObject,
                        changedObject,
                        action,
                        userName,
                        businessProcessName, subProcessName);
                this.mode = 'U';
                break;

            case "Rejected":
                changeDocument.setAction("Rejected");
                changeDocument = changeDocument = prepareUpdateChangeDocument(loanBusinessProcessObjectId, entityId, mainEntityId,
                        loanContractId,
                        oldObject,
                        changedObject,
                        action,
                        userName,
                        businessProcessName, subProcessName);
                this.mode = 'U';
                break;
        }

        changeDocument = this.saveChangeDocument(changeDocument);


        sapIntegrationPointerService.saveForObject(businessProcessName, subProcessName, entityId, mainEntityId, mode);

        return changeDocument;
    }


    @Override
    public ChangeDocument saveChangeDocument(ChangeDocument changeDocument) {

        changeDocument.setCreatedAt(new Date());
        changeDocument.setUpdatedAt(new Date());
        changeDocument = changeDocumentRepository.save(changeDocument);
        return changeDocument;
    }


    @Override
    public ChangeDocument updateChangeDocument(ChangeDocument changeDocument, Object object) {
        return null;
    }

    @Override
    public Page<ChangeDocument> findByBusinessProcessNameAndLoanContractIdAndDateBetween(String processName,
                                                                                         String loanContractId,
                                                                                         Date dateFrom,
                                                                                         Date dateTo,
                                                                                         Pageable pageable) {
        return null;
    }

    @Override
    public Page<ChangeDocument> findByBusinessProcessNameAndLoanContractIdAndDate(String processName,
                                                                                  String loanContractId,
                                                                                  Date date,
                                                                                  Pageable pageable) {
        return changeDocumentRepository.findByBusinessProcessNameAndLoanContractIdAndDate(processName, loanContractId, date, pageable);
    }

    @Override
    public Page<ChangeDocument> findByBusinessProcessNameAndLoanContractId(String processName,
                                                                           String loanContractId,
                                                                           Pageable pageable) {
        Page<ChangeDocument> changeDocumentPage = changeDocumentRepository.findByBusinessProcessNameAndLoanContractId(processName, loanContractId, pageable);
        return changeDocumentPage;

    }

    @Override
    public Page<ChangeDocument> findByBusinessProcessName(String businessProcessName, Pageable pageable) {
        return changeDocumentRepository.findByBusinessProcessName(businessProcessName, pageable);
    }

    @Override
    public Page<ChangeDocument> findByBusinessProcessNameAndDateBetween(String businessProcessName, Date dateFrom, Date dateTo, Pageable pageable) {
        return changeDocumentRepository.findByBusinessProcessNameAndDateBetween(businessProcessName, dateFrom, dateTo, pageable);
    }

    @Override
    public Page<ChangeDocument> findByBusinessProcessNameAndDate(String businessProcessName, Date date, Pageable pageable) {
        return changeDocumentRepository.findByBusinessProcessNameAndDate(businessProcessName, date, pageable);
    }


    private ChangeDocument prepareCreateChangeDocument(UUID loanBusinessProcessObjectId, String entityId, String mainEntityId,
                                                       String loanContractId,
                                                       Object changedObject,
                                                       String action,
                                                       String userName,
                                                       String businessProcessName,
                                                       String subProcessName) {

        changeDocument = prepareHeader(loanBusinessProcessObjectId, entityId, mainEntityId,
                loanContractId,
                changedObject,
                action,
                userName,
                businessProcessName, subProcessName);

        return changeDocument;


    }

    private ChangeDocument prepareDeletedChangeDocument(UUID loanBusinessProcessObjectId, String entityId, String mainEntityId,
                                                        String loanContractId,
                                                        Object changedObject,
                                                        String action,
                                                        String userName,
                                                        String businessProcessName,
                                                        String subProcessName) {

        changeDocument = prepareHeader(loanBusinessProcessObjectId, entityId, mainEntityId,
                loanContractId,
                changedObject,
                action,
                userName,
                businessProcessName, subProcessName);

        return changeDocument;


    }

    private ChangeDocument prepareUpdateChangeDocument(UUID loanBusinessProcessObjectId, String entityId, String mainEntityId,
                                                       String loanContractId,
                                                       Object oldObject,
                                                       Object changedObject,
                                                       String action,
                                                       String userName,
                                                       String businessProcessName,
                                                       String subProcessName) {

        changeDocument = prepareHeader(loanBusinessProcessObjectId, entityId, mainEntityId,
                loanContractId,
                changedObject,
                action,
                userName,
                businessProcessName, subProcessName);


        // Compare RiskModel Header //given
        Javers javers = JaversBuilder.javers()
                .withListCompareAlgorithm(LEVENSHTEIN_DISTANCE)
                .build();

        Diff diff = javers.compare(oldObject, changedObject);

        List<ChangeDocumentItem> changeDocumentItems = new ArrayList<>();
        changeDocumentItems = prepareChangeDocumentItems(diff, changeDocumentItems);


        changeDocument.setChangeDocumentItems(changeDocumentItems);


        return changeDocument;
    }


    private List<ChangeDocumentItem> prepareChangeDocumentItems(Diff diff, List<ChangeDocumentItem> changeDocumentItems) {


        int i = changeDocumentItems.size() + 1;
        for (ValueChange change : diff.getChangesByType(ValueChange.class)) {

            ChangeDocumentItem changeDocumentItem = new ChangeDocumentItem();
            changeDocumentItem.setItemNo(i);
            if (change.getPropertyName().equals("createdAt") || change.getPropertyName().equals("updatedAt"))
                continue;

            Object object = change.getAffectedObject().get();
            Map<String, Object> result = getObjectDetails(object.getClass().getSimpleName(), object);

            changeDocumentItem.setEntityName(object.getClass().getSimpleName().toString());
            changeDocumentItem.setEntityDescription((String)result.get("description"));
            changeDocumentItem.setAttributeName(change.getPropertyName());

            if (change.getRight() != null)
                changeDocumentItem.setNewValue(change.getRight().toString());
            else
                changeDocumentItem.setNewValue(null);
            if (change.getLeft() != null)
                changeDocumentItem.setOldValue(change.getLeft().toString());
            else
                changeDocumentItem.setOldValue(null);

            changeDocumentItem.setTableKey((String)result.get("id"));

            changeDocumentItem.setCreatedAt(new Date());
            changeDocumentItem.setUpdatedAt(new Date());


            changeDocumentItems.add(changeDocumentItem);

            i++;

        }


        return changeDocumentItems;

    }

    private Map<String, Object> getObjectDetails(String className, Object object) {

        Object objectParsed = new Object();

        Map<String, Object> result = new HashMap<>();

        if (Pattern.compile(pattern).matcher(className).find()) {
            String[] classNameComponents = className.split("\\$");
            className = classNameComponents[0];
        }

//        if (className.contains("\\$")) {
//            String[] classNameComponents = className.split("$");
//            className = classNameComponents[0];
//        }

        try {

            switch (className) {
                case "LoanApplication":
                    LoanApplication loanApplication = (LoanApplication) object;
                    if (loanApplication.getLoanContractId() == null)
                        result.put("id", loanApplication.getEnquiryNo().getId().toString());
                    else
                        result.put("id", loanApplication.getLoanContractId().toString());
                    result.put("description", loanApplication.getProjectName());
                    result.put("loanApplication",loanApplication );
                    return result;
                    case "LoanMonitor":
                        LoanMonitor loanMonitor = (LoanMonitor) object;
                    result.put("id", loanMonitor.getLoanApplication().getLoanContractId().toString());
                    result.put("description", loanMonitor.getLoanApplication().getProjectName());
                    result.put("loanApplication",loanMonitor.getLoanApplication());
                    return result;
                case "LendersIndependentEngineer":
                    LendersIndependentEngineer lendersIndependentEngineer = (LendersIndependentEngineer) object;
                    result.put("id", lendersIndependentEngineer.getSerialNumber().toString());
                    result.put("description", lendersIndependentEngineer.getName());
                    result.put("loanApplication",lendersIndependentEngineer.getLoanAppraisal().getLoanApplication());
                    return result;
                case "LIEReportAndFee":
                    LIEReportAndFee lieReportAndFee = (LIEReportAndFee) object;
                    result.put("id", lieReportAndFee.getLendersIndependentEngineer().getSerialNumber().toString());
                    result.put("description", lieReportAndFee.getReportType() + lieReportAndFee.getReportType());
                    result.put("loanApplication",lieReportAndFee.getLendersIndependentEngineer().getLoanAppraisal().getLoanApplication());
                    return result;
                case "LendersInsuranceAdvisor":
                    LendersInsuranceAdvisor lendersInsuranceAdvisor = (LendersInsuranceAdvisor) object;
                    result.put("id", lendersInsuranceAdvisor.getSerialNumber().toString());
                    result.put("description", lendersInsuranceAdvisor.getName());
                    result.put("loanApplication",lendersInsuranceAdvisor.getLoanAppraisal().getLoanApplication());
                    return result;
                case "LIAReportAndFee":
                    LIAReportAndFee liaReportAndFee = (LIAReportAndFee) object;
                    result.put("id", liaReportAndFee.getLendersInsuranceAdvisor().getSerialNumber().toString());
                    result.put("description", liaReportAndFee.getReportType() + liaReportAndFee.getReportType());
                    result.put("loanApplication",liaReportAndFee.getLendersInsuranceAdvisor().getLoanAppraisal().getLoanApplication());
                    return result;
                case "LendersLegalCouncil":
                    LendersLegalCouncil lendersLegalCouncil = (LendersLegalCouncil) object;
                    result.put("id", lendersLegalCouncil.getSerialNumber().toString());
                    result.put("description", lendersLegalCouncil.getName());
                    result.put("loanApplication",lendersLegalCouncil.getLoanAppraisal().getLoanApplication());
                    return result;
                case "LLCReportAndFee":
                    LLCReportAndFee llcReportAndFee = (LLCReportAndFee) object;
                    result.put("id", llcReportAndFee.getLendersLegalCouncil().getSerialNumber().toString());
                    result.put("description", llcReportAndFee.getReportType() + llcReportAndFee.getReportType());
                    result.put("loanApplication",llcReportAndFee.getLendersLegalCouncil().getLoanAppraisal().getLoanApplication());
                    return result;
                case "Valuer":
                    Valuer valuer = (Valuer) object;
                    result.put("id", valuer.getSerialNumber().toString());
                    result.put("description", valuer.getName());
                    result.put("loanApplication",valuer.getLoanAppraisal().getLoanApplication());
                    return result;
                case "ValuerReportAndFee":
                    ValuerReportAndFee valuerReportAndFee = (ValuerReportAndFee) object;
                    result.put("id", valuerReportAndFee.getSerialNumber().toString());
                    result.put("description", valuerReportAndFee.getReportType() + valuerReportAndFee.getReportType());
                    result.put("loanApplication",valuerReportAndFee.getValuer().getLoanAppraisal().getLoanApplication());
                    return result;
                case "LendersFinancialAdvisor":
                    LendersFinancialAdvisor lendersFinancialAdvisor = (LendersFinancialAdvisor) object;
                    result.put("id", lendersFinancialAdvisor.getSerialNumber().toString());
                    result.put("description", lendersFinancialAdvisor.getName());
                    result.put("loanApplication",lendersFinancialAdvisor.getLoanAppraisal().getLoanApplication());
                    return result;
                case "LFAReportAndFee":
                    LFAReportAndFee lfaReportAndFee = (LFAReportAndFee) object;
                    result.put("id", lfaReportAndFee.getLendersFinancialAdvisor().getSerialNumber().toString());
                    result.put("description", lfaReportAndFee.getReportType() + lfaReportAndFee.getReportType());
                    result.put("loanApplication",lfaReportAndFee.getLendersFinancialAdvisor().getLoanAppraisal().getLoanApplication());
                    return result;
                case "SecurityTrustee":
                    SecurityTrustee securityTrustee = (SecurityTrustee) object;
                    result.put("id", securityTrustee.getSerialNumber().toString());
                    result.put("description", securityTrustee.getName());
                    result.put("loanApplication",securityTrustee.getLoanAppraisal().getLoanApplication());
                    return result;
                case "SecurityTrusteeReportAndFee":
                    SecurityTrusteeReportAndFee securityTrusteeReportAndFee = (SecurityTrusteeReportAndFee) object;
                    result.put("id", securityTrusteeReportAndFee.getSecurityTrustee().getSerialNumber().toString());
                    result.put("description", securityTrusteeReportAndFee.getReportType() + securityTrusteeReportAndFee.getReportType());
                    result.put("loanApplication",securityTrusteeReportAndFee.getSecurityTrustee().getLoanAppraisal().getLoanApplication());
                    return result;


                case "TrustRetentionAccount":
                    TrustRetentionAccount trustRetentionAccount = (TrustRetentionAccount) object;
                    result.put("id", trustRetentionAccount.getSerialNumber().toString());
                    result.put("description", trustRetentionAccount.getBankKey());
                    result.put("loanApplication",trustRetentionAccount.getLoanAppraisal().getLoanApplication());
                    return result;
                case "TrustRetentionAccountStatement":
                    TrustRetentionAccountStatement trustRetentionAccountStatement = (TrustRetentionAccountStatement) object;
                    result.put("id", trustRetentionAccountStatement.getSerialNumber().toString());
                    result.put("description", trustRetentionAccountStatement.getDocumentType());
                    result.put("loanApplication",trustRetentionAccountStatement.getTrustRetentionAccount().getLoanAppraisal().getLoanApplication());

                    return result;
                case "TermsAndConditionsModification":
                    TermsAndConditionsModification termsAndConditionsModification = (TermsAndConditionsModification) object;
                    result.put("id", termsAndConditionsModification.getSerialNumber().toString());
                    result.put("description", termsAndConditionsModification.getCommunication());
                    result.put("loanApplication",termsAndConditionsModification.getLoanMonitor().getLoanApplication());
                    return result;
                case "SecurityCompliance":
                    SecurityCompliance securityCompliance = (SecurityCompliance) object;
                    result.put("id", securityCompliance.getSerialNumber().toString());
                    result.put("description", securityCompliance.getCollateralObjectType());
                    result.put("loanApplication",securityCompliance.getLoanMonitor().getLoanApplication());

                    return result;
                case "SiteVisit":
                    SiteVisit siteVisit = (SiteVisit) object;
                    result.put("id", siteVisit.getSerialNumber().toString());
                    result.put("description", siteVisit.getSerialNumber().toString());
                    result.put("loanApplication",siteVisit.getLoanApplication());

                    return result;
                case "OperatingParameter":
                    OperatingParameter operatingParameter = (OperatingParameter) object;
                    result.put("id", operatingParameter.getSerialNumber().toString());
                    result.put("description", operatingParameter.getSerialNumber().toString());
                    result.put("loanApplication",operatingParameter.getLoanMonitor().getLoanApplication());

                    return result;
                case "OperatingParameterPLF":
                    OperatingParameterPLF operatingParameterPLF = (OperatingParameterPLF) object;
                    result.put("id", operatingParameterPLF.getSerialNumber().toString());
                    result.put("description", operatingParameterPLF.getSerialNumber().toString());
                    result.put("loanApplication",operatingParameterPLF.getLoanMonitor().getLoanApplication());

                    return result;
                case "RateOfInterest":
                    RateOfInterest rateOfInterest = (RateOfInterest) object;
                    result.put("id", rateOfInterest.getSerialNumber().toString());
                    result.put("description", rateOfInterest.getConditionType().toString());
                    result.put("loanApplication",rateOfInterest.getLoanMonitor().getLoanApplication());

                    return result;
                case "BorrowerFinancials":
                    BorrowerFinancials borrowerFinancials = (BorrowerFinancials) object;
                    result.put("id", borrowerFinancials.getSerialNumber().toString());
                    result.put("description", borrowerFinancials.getFiscalYear().toString());
                    result.put("loanApplication",borrowerFinancials.getLoanMonitor().getLoanApplication());

                    return result;
                case "PromoterFinancials":
                    PromoterFinancials promoterFinancials = (PromoterFinancials) object;
                    result.put("id", promoterFinancials.getSerialNumber().toString());
                    result.put("description", promoterFinancials.getFiscalYear().toString());
                    result.put("loanApplication",promoterFinancials.getLoanMonitor().getLoanApplication());

                    return result;
                case "FinancialCovenants":
                    FinancialCovenants financialCovenants = (FinancialCovenants) object;
                    result.put("id", financialCovenants.getSerialNumber().toString());
                    result.put("description", financialCovenants.getFinancialCovenantType().toString());
                    result.put("loanApplication",financialCovenants.getLoanMonitor().getLoanApplication());

                    return result;

                case "PromoterDetail":
//                    PromoterDetail promoterDetails = (PromoterDetail) object;
//                    result.put("id", promoterDetails.getId().toString());
//                    result.put("description", promoterDetails.getDateOfChange().toString());
//                    return result;
                case "ProjectMonitoringDataItemResource":
                    ProjectMonitoringDataItemResource projectMonitoringDataItemResource = (ProjectMonitoringDataItemResource) object;
                    result.put("id", projectMonitoringDataItemResource.getId().toString());
                    result.put("description", projectMonitoringDataItemResource.getParticulars().toString());
                    result.put("loanApplication",loanApplicationRepository.getOne(projectMonitoringDataItemResource.getLoanApplicationId()));

                    return result;
                case "ProjectMonitoringDataItemHistory":
                    ProjectMonitoringDataItemHistory projectMonitoringDataItemHistory = (ProjectMonitoringDataItemHistory) object;
                    result.put("id", projectMonitoringDataItemHistory.getId().toString());
                    result.put("description", projectMonitoringDataItemHistory.getParticulars().toString());
                    return result;

                case "Appraisal":
                    LoanAppraisal loanAppraisal = (LoanAppraisal) object;
                    if (loanAppraisal.getLoanApplication() != null) {
                        if (loanAppraisal.getLoanApplication().getLoanContractId() != null) {
                            result.put("id", loanAppraisal.getLoanApplication().getLoanContractId().toString());
                            result.put("description", loanAppraisal.getLoanApplication().getLoanContractId().toString());
                            changeDocument.setLoanApplication(loanAppraisal.getLoanApplication());
                        } else {
                            result.put("id", loanAppraisal.getLoanApplication().getEnquiryNo().toString());
                            result.put("description", loanAppraisal.getLoanApplication().getEnquiryNo().toString());
                            changeDocument.setLoanApplication(loanAppraisal.getLoanApplication());
                        }
                    } else {
                        result.put("id", loanAppraisal.getId().toString());
                        result.put("description", loanAppraisal.getId().toString());

                    }

                    return result;
                case "LoanAppraisal":
                    LoanAppraisal loanAppraisal1 = (LoanAppraisal) object;
                    if (loanAppraisal1.getLoanApplication() != null) {
                        if (loanAppraisal1.getLoanApplication().getLoanContractId() != null) {
                            result.put("id", loanAppraisal1.getLoanApplication().getLoanContractId().toString());
                            result.put("description", loanAppraisal1.getLoanApplication().getLoanContractId().toString());
                            changeDocument.setLoanApplication(loanAppraisal1.getLoanApplication());

                        } else {
                            result.put("id", loanAppraisal1.getLoanApplication().getEnquiryNo().toString());
                            result.put("description", loanAppraisal1.getLoanApplication().getEnquiryNo().toString());
                            changeDocument.setLoanApplication(loanAppraisal1.getLoanApplication());
                        }
                    } else {
                        result.put("id", loanAppraisal1.getId().toString());
                        result.put("description", loanAppraisal1.getId().toString());

                    }
                    result.put("loanApplication",loanAppraisal1.getLoanApplication());

                    return result;

                case "CustomerRejection":
                    CustomerRejection customerRejection = (CustomerRejection) object;
                    result.put("id", customerRejection.getDate().toString());
                    result.put("description", customerRejection.getDate().toString());
                    result.put("loanApplication",customerRejection.getLoanAppraisal().getLoanApplication());

                    return result;
                case "FurtherDetail":
                    ICCFurtherDetail ICCFurtherDetail = (ICCFurtherDetail) object;
//                    result.put("id", ICCFurtherDetail.getDate().toString());
                    result.put("description", "Further details");//furtherDetail.getDate().toString());
//                    result.put("loanApplication", ICCFurtherDetail.getLoanAppraisal().getLoanApplication());

                    return result;
                case "LoanPartner":
                    LoanPartner loanPartner = (LoanPartner) object;
                    LoanAppraisal loanAppraisal2 = loanAppraisalRepository.getOne(UUID.fromString(loanPartner.getLoanAppraisalId()));
                    if (loanAppraisal2.getLoanApplication() != null) {
                        if (loanAppraisal2.getLoanApplication().getLoanContractId() != null) {
                            result.put("id", loanPartner.getBusinessPartnerId().toString());
                            result.put("description", loanPartner.getBusinessPartnerId().toString());
                            changeDocument.setLoanApplication(loanAppraisal2.getLoanApplication());

                        } else {
                            result.put("id", loanPartner.getBusinessPartnerId().toString());
                            result.put("description", loanPartner.getBusinessPartnerId().toString());
                            changeDocument.setLoanApplication(loanAppraisal2.getLoanApplication());
                        }
                    } else {
                        result.put("id", loanPartner.getBusinessPartnerId().toString());
                        result.put("description", loanPartner.getBusinessPartnerId().toString());
                    }
                    result.put("loanApplication",loanAppraisal2.getLoanApplication());

                    return result;
                case "KnowYourCustomer":
                    KnowYourCustomer knowYourCustomer = (KnowYourCustomer) object;
                    result.put("id", knowYourCustomer.getDocumentType().toString());
                    result.put("description", knowYourCustomer.getDocumentName().toString());
                    result.put("loanApplication",new LoanApplication());
                    return result;
                case "ProjectAppraisalCompletion":
                    ProjectAppraisalCompletion projectAppraisalCompletion = (ProjectAppraisalCompletion) object;
                    result.put("id", projectAppraisalCompletion.getId().toString());
                    result.put("description", projectAppraisalCompletion.getDateOfProjectAppraisalCompletion().toString());
                    result.put("loanApplication",projectAppraisalCompletion.getLoanAppraisal().getLoanApplication());
                    return result;
                case "ProjectData":
                    ProjectData projectData = (ProjectData) object;
                    result.put("id", projectData.getId().toString());
                    result.put("description", projectData.getProjectName().toString());
                    result.put("loanApplication",projectData.getLoanAppraisal().getLoanApplication());
                    return result;
                case "ProposalDetail":
                    ProposalDetail proposalDetail = (ProposalDetail) object;
                    result.put("id", proposalDetail.getId().toString());
                    result.put("description", "Proposal Details");// proposalDetail.get().toString());
                    result.put("loanApplication",proposalDetail.getLoanAppraisal().getLoanApplication());
                    return result;
                case "ReasonForDelay":
                    ReasonForDelay reasonForDelay = (ReasonForDelay) object;
                    result.put("id", reasonForDelay.getDate().toString());
                    result.put("description", "Reason for delay");//reasonForDelay.getId().toString());
                    result.put("loanApplication",reasonForDelay.getLoanAppraisal().getLoanApplication());
                    return result;
                case "SyndicateConsortium":
                    SyndicateConsortium syndicateConsortium = (SyndicateConsortium) object;
                    result.put("id", syndicateConsortium.getId().toString());
                    result.put("description", syndicateConsortium.getBankName().toString());
                    result.put("loanApplication",syndicateConsortium.getLoanAppraisal().getLoanApplication());
                    return result;
                case "CorporateLoanRiskRating":
                    CorporateLoanRiskRating corporateLoanRiskRating = (CorporateLoanRiskRating) object;
                    result.put("id", corporateLoanRiskRating.getId().toString());
                    result.put("description", corporateLoanRiskRating.getYear());
                    result.put("loanApplication",corporateLoanRiskRating.getLoanAppraisal().getLoanApplication());
                    return result;
                case "TermLoanRiskRating":
                    TermLoanRiskRating termLoanRiskRating = (TermLoanRiskRating) object;
                    result.put("id", termLoanRiskRating.getId().toString());
                    result.put("description", termLoanRiskRating.getYear());
                    result.put("loanApplication",termLoanRiskRating.getLoanAppraisal().getLoanApplication());
                    return result;
                case "ExternalRating":
                    ExternalRating externalRating = (ExternalRating) object;
                    result.put("id", externalRating.getId().toString());
                    result.put("description", externalRating.getSerialNumber());
                    result.put("loanApplication",externalRating.getLoanAppraisal().getLoanApplication());
                    return result;
                case "MainLocationDetail":
                    MainLocationDetail mainLocationDetail = (MainLocationDetail) object;
                    result.put("id", mainLocationDetail.getId().toString());
                    result.put("description", mainLocationDetail.getLocation());
                    result.put("loanApplication",mainLocationDetail.getLoanAppraisal().getLoanApplication());
                    return result;
                case "SubLocationDetail":
                    SubLocationDetail subLocationDetail = (SubLocationDetail) object;
                    result.put("id", subLocationDetail.getSerialNumber().toString());
                    result.put("description", subLocationDetail.getLocation());
                    result.put("loanApplication",subLocationDetail.getLoanAppraisal().getLoanApplication());
                    return result;
                case "NPA":
                    NPA npa = (NPA) object;
                    result.put("id", npa.getId().toString());
                    result.put("description", npa.getAssetClass());
                    result.put("loanApplication",npa.getLoanMonitor().getLoanApplication());
                    return result;
                case "NPADetail":
                    NPADetail npaDetail = (NPADetail) object;
                    result.put("id", npaDetail.getLineItemNumber().toString());
                    result.put("description", npaDetail.getLoanNumber());
                    result.put("loanApplication",npaDetail.getNpa().getLoanMonitor().getLoanApplication());

                    return result;
                case "LoanDocumentation":
                    LoanDocumentation loanDocumentation = (LoanDocumentation) object;
                    result.put("id", loanDocumentation.getSerialNumber().toString());
                    result.put("description", loanDocumentation.getDocumentationTypeDescription());
                    result.put("loanApplication",loanDocumentation.getLoanMonitor().getLoanApplication());
                    return result;
                case "Insurance":
                    Insurance insurance = (Insurance) object;
                    result.put("id", insurance.getSerialNumber().toString());
                    result.put("description", insurance.getValidFrom().toString());
                    result.put("loanApplication",insurance.getLoanMonitor().getLoanApplication());
                    return result;
                case "EndUseCertificate":
                    EndUseCertificate endUseCertificate = (EndUseCertificate) object;
                    result.put("id", endUseCertificate.getSerialNumber().toString());
                    result.put("description", endUseCertificate.getEventDate().toString());
                    result.put("loanApplication",endUseCertificate.getLoanMonitor().getLoanApplication());
                    return result;
                case "EnquiryAction":
                    EnquiryAction enquiryAction = (EnquiryAction) object;
                    result.put("id", enquiryAction.getId().toString());
                    result.put("description", enquiryAction.getId().toString());
                    result.put("loanApplication",enquiryAction.getLoanApplication());
                    return result;
                case "ProjectProposal":
                    ProjectProposal projectProposal = (ProjectProposal) object;
                    result.put("id", projectProposal.getId().toString());
                    result.put("description", projectProposal.getProposalStatus().toString());
                    result.put("loanApplication",projectProposal.getEnquiryAction().getLoanApplication());
                    return result;
                case "ProjectProposalOtherDetail":
                    ProjectProposalOtherDetail projectProposalOtherDetail = (ProjectProposalOtherDetail) object;
                    result.put("id", projectProposalOtherDetail.getId().toString());
                    result.put("description", projectProposalOtherDetail.getSourceAndCashFlow());
                    result.put("loanApplication",projectProposalOtherDetail.getProjectProposal().getEnquiryAction().getLoanApplication());
                    return result;
                case "ProjectDetail":
                    ProjectDetail projectDetail = (ProjectDetail) object;
                    result.put("id", projectDetail.getProjectName().toString());
                    result.put("description", projectDetail.getStatus());
                    result.put("loanApplication",projectDetail.getProjectProposal().getEnquiryAction().getLoanApplication());
                    return result;
                case "CollateralDetail":
                    CollateralDetail collateralDetail = (CollateralDetail) object;
                    result.put("id", collateralDetail.getCollateralType().toString());
                    result.put("description", collateralDetail.getDetails());
                    result.put("loanApplication",collateralDetail.getProjectProposal().getEnquiryAction().getLoanApplication());
                    return result;
                case "CreditRating":
                    CreditRating creditRating = (CreditRating) object;
                    result.put("id", creditRating.getCreditRating().toString());
                    result.put("description", creditRating.getCreditRating());
                    result.put("loanApplication",creditRating.getProjectProposal().getEnquiryAction().getLoanApplication());
                    return result;
                case "DealGuaranteeTimeline":
                    DealGuaranteeTimeline dealGuaranteeTimeline = (DealGuaranteeTimeline) object;
                    result.put("id", dealGuaranteeTimeline.getDealTransactionStructure().toString());
                    result.put("description", dealGuaranteeTimeline.getDealTransactionStructure());
                    result.put("loanApplication",dealGuaranteeTimeline.getProjectProposal().getEnquiryAction().getLoanApplication());
                    return result;
                case "PromoterBorrowerFinancial":
                    PromoterBorrowerFinancial promoterBorrowerFinancial = (PromoterBorrowerFinancial) object;
                    result.put("id", promoterBorrowerFinancial.getFiscalPeriod().toString());
                    result.put("description", promoterBorrowerFinancial.getFiscalPeriod());
                    result.put("loanApplication",promoterBorrowerFinancial.getProjectProposal().getEnquiryAction().getLoanApplication());
                    return result;
                case "OtherDetailsDocument":
                    OtherDetailsDocument otherDetailsDocument = (OtherDetailsDocument) object;
                    result.put("id", otherDetailsDocument.getDocumentType().toString());
                    result.put("description", otherDetailsDocument.getDocumentName());
                    result.put("loanApplication",otherDetailsDocument.getProjectProposal().getEnquiryAction().getLoanApplication());
                    return result;
                case "ProjectCost":
                    ProjectCost projectCost = (ProjectCost) object;
                    result.put("id", projectCost.getProjectCost().toString());
                    result.put("description", projectCost.getProjectCost().toString());
                    result.put("loanApplication",projectCost.getProjectProposal().getEnquiryAction().getLoanApplication());
                    return result;
                case "ShareHolder":
                    ShareHolder shareHolder = (ShareHolder) object;
                    result.put("id", shareHolder.getCompanyName().toString());
                    result.put("description", shareHolder.getCompanyName().toString());
                    result.put("loanApplication",shareHolder.getProjectProposal().getEnquiryAction().getLoanApplication());
                    return result;
             case "EnquiryActionReasonForDelay":
                    EnquiryActionReasonForDelay enquiryActionReasonForDelay = (EnquiryActionReasonForDelay) object;
                    result.put("id", enquiryActionReasonForDelay.getId().toString());
                    result.put("description", enquiryActionReasonForDelay.getReason().toString());
                    result.put("loanApplication",enquiryActionReasonForDelay.getEnquiryAction().getLoanApplication());
                 return result;
                case "EnquiryCompletion":
                    EnquiryCompletion enquiryCompletion = (EnquiryCompletion) object;
                    result.put("id", enquiryCompletion.getId().toString());
                    result.put("description", enquiryCompletion.getDate().toString());
                    result.put("loanApplication",enquiryCompletion.getEnquiryAction().getLoanApplication());
                    return result;
                case "OtherDetail":
                    OtherDetail otherDetail = (OtherDetail) object;
                    result.put("id", otherDetail.getId().toString());
                    result.put("description", otherDetail.getEnquiryDate().toString());
                    result.put("loanApplication",otherDetail.getEnquiryAction().getLoanApplication());
                    return result;
                case "RejectByCustomer":
                    RejectByCustomer rejectByCustomer = (RejectByCustomer) object;
                    result.put("id", rejectByCustomer.getId().toString());
                    result.put("description", rejectByCustomer.getRejectionDate().toString());
                    result.put("loanApplication",rejectByCustomer.getEnquiryAction().getLoanApplication());
                    return result;
                case "RejectByPfs":
                    RejectByPfs rejectByPfs = (RejectByPfs) object;
                    result.put("id", rejectByPfs.getId().toString());
                    result.put("description", rejectByPfs.getRejectionDate().toString());
                    result.put("loanApplication",rejectByPfs.getEnquiryAction().getLoanApplication());
                    return result;
                case "BoardApproval":
                    BoardApproval boardApproval = (BoardApproval) object;
                    result.put("id", boardApproval.getId().toString());
                    result.put("description", boardApproval.getLoanApplication().getEnquiryNo().getId().toString());
                    result.put("loanApplication",boardApproval.getLoanApplication());
                    return result;
                case "ApprovalByBoard":
                    ApprovalByBoard approvalByBoard = (ApprovalByBoard) object;
                    result.put("id", approvalByBoard.getId().toString());
                    result.put("description", approvalByBoard.getBoardApproval().getLoanApplication().getEnquiryNo().getId().toString());
                    result.put("loanApplication",approvalByBoard.getBoardApproval().getLoanApplication());
                    return result;
                case "DeferredByBoard":
                    DeferredByBoard deferredByBoard = (DeferredByBoard) object;
                    result.put("id", deferredByBoard.getId().toString());
                    result.put("description", deferredByBoard.getBoardApproval().getLoanApplication().getEnquiryNo().getId().toString());
                    result.put("loanApplication",deferredByBoard.getBoardApproval().getLoanApplication());
                    return result;
                case "BoardApprovalReasonForDelay":
                    BoardApprovalReasonForDelay boardApprovalReasonForDelay = (BoardApprovalReasonForDelay) object;
                    result.put("id", boardApprovalReasonForDelay.getId().toString());
                    result.put("description", boardApprovalReasonForDelay.getBoardApproval().getLoanApplication().getEnquiryNo().getId().toString());
                    result.put("loanApplication",boardApprovalReasonForDelay.getBoardApproval().getLoanApplication());
                    return result;
                case "RejectedByBoard":
                    RejectedByBoard rejectedByBoard = (RejectedByBoard) object;
                    result.put("id", rejectedByBoard.getId().toString());
                    result.put("description", rejectedByBoard.getBoardApproval().getLoanApplication().getEnquiryNo().getId().toString());
                    result.put("loanApplication",rejectedByBoard.getBoardApproval().getLoanApplication());
                    return result;
                case "BoardApprovalRejectedByCustomer":
                    BoardApprovalRejectedByCustomer boardApprovalRejectedByCustomer = (BoardApprovalRejectedByCustomer) object;
                    result.put("id", boardApprovalRejectedByCustomer.getId().toString());
                    result.put("description", boardApprovalRejectedByCustomer.getBoardApproval().getLoanApplication().getEnquiryNo().getId().toString());
                    result.put("loanApplication",boardApprovalRejectedByCustomer.getBoardApproval().getLoanApplication());
                    return result;
                case "Sanction":
                    Sanction sanction = (Sanction) object;
                    result.put("id", sanction.getId().toString());
                    result.put("description", sanction.getLoanApplication().getEnquiryNo().getId().toString());
                    result.put("loanApplication",sanction.getLoanApplication());
                    return result;
                case "PaymentReceiptPostSanction":
                    PaymentReceiptPostSanction paymentReceiptPostSanction = (PaymentReceiptPostSanction) object;
                    result.put("id", paymentReceiptPostSanction.getId().toString());
                    result.put("description", paymentReceiptPostSanction.getProformaInvoiceNumber().toString());
                    result.put("loanApplication",paymentReceiptPostSanction.getSanction().getLoanApplication());
                    return result;
                case "PaymentReceiptPreSanction":
                    PaymentReceiptPreSanction paymentReceiptPreSanction = (PaymentReceiptPreSanction) object;
                    result.put("id", paymentReceiptPreSanction.getId().toString());
                    result.put("description", paymentReceiptPreSanction.getProformaInvoiceNumber());
                    result.put("loanApplication",paymentReceiptPreSanction.getSanction().getLoanApplication());
                    return result;
                case "SanctionReasonForDelay":
                    SanctionReasonForDelay sanctionReasonForDelay = (SanctionReasonForDelay) object;
                    result.put("id", sanctionReasonForDelay.getId().toString());
                    result.put("description", sanctionReasonForDelay.getSerialNumber().toString());
                    result.put("loanApplication",sanctionReasonForDelay.getSanction().getLoanApplication());
                    return result;
                case "SanctionRejectedByCustomer":
                    SanctionRejectedByCustomer sanctionRejectedByCustomer = (SanctionRejectedByCustomer) object;
                    result.put("id", sanctionRejectedByCustomer.getId().toString());
                    result.put("description", sanctionRejectedByCustomer.getMeetingDate().toString());
                    result.put("loanApplication",sanctionRejectedByCustomer.getSanction().getLoanApplication());
                    return result;
                case "SanctionLetter":
                    SanctionLetter sanctionLetter = (SanctionLetter) object;
                    result.put("id", sanctionLetter.getId().toString());
                    result.put("description", sanctionLetter.getSerialNumber().toString());
                    result.put("loanApplication",sanctionLetter.getSanction().getLoanApplication());
                    return result;
                 case "ApplicationFee":
                     ApplicationFee applicationFee = (ApplicationFee) object;
                    result.put("id", applicationFee.getId().toString());
                    if (applicationFee.getLoanApplication().getLoanContractId() != null) {
                        result.put("description", applicationFee.getLoanApplication().getLoanContractId().toString());
                    } else{
                        result.put("description", applicationFee.getLoanApplication().getEnquiryNo().getId().toString());
                    }
                    result.put("loanApplication",applicationFee.getLoanApplication());
                    return result;
                case "InceptionFee":
                    InceptionFee inceptionFee = (InceptionFee) object;
                    result.put("id", inceptionFee.getId().toString());
                    if (inceptionFee.getApplicationFee().getLoanApplication().getLoanContractId() != null) {
                        result.put("description", inceptionFee.getApplicationFee().getLoanApplication().getLoanContractId().toString());
                    } else{
                        result.put("description", inceptionFee.getApplicationFee().getLoanApplication().getEnquiryNo().getId().toString());
                    }
                    result.put("loanApplication",inceptionFee.getApplicationFee().getLoanApplication());
                    return result;
                case "RiskNotification":
                    RiskNotification riskNotification = (RiskNotification) object;
                    result.put("id", riskNotification.getId().toString());
                    if (riskNotification.getIccApproval().getLoanApplication().getLoanContractId() != null) {
                        result.put("description", riskNotification.getIccApproval().getLoanApplication().getLoanContractId().toString());
                    } else{
                        result.put("description", riskNotification.getIccApproval().getLoanApplication().getEnquiryNo().getId().toString());
                    }
                    result.put("loanApplication",riskNotification.getIccApproval().getLoanApplication());
                    return result;
                case "RiskAssessment":
                    RiskAssessment riskAssessment = (RiskAssessment) object;
                    result.put("id", riskAssessment.getId().toString());
                    if (riskAssessment.getLoanApplication().getLoanContractId() != null) {
                        result.put("description", riskAssessment.getLoanApplication().getLoanContractId().toString());
                    } else{
                        result.put("description", riskAssessment.getLoanApplication().getEnquiryNo().getId().toString());
                    }
                    result.put("loanApplication",riskAssessment.getLoanApplication());
                    return result;

                case "PreliminaryRiskAssessment":
                    PreliminaryRiskAssessment preliminaryRiskAssessment = (PreliminaryRiskAssessment) object;
                    result.put("id", preliminaryRiskAssessment.getId().toString());
                    if (preliminaryRiskAssessment.getRiskAssessment().getLoanApplication().getLoanContractId() != null) {
                        result.put("description", preliminaryRiskAssessment.getRiskAssessment().getLoanApplication().getLoanContractId().toString());
                    } else{
                        result.put("description", preliminaryRiskAssessment.getRiskAssessment().getLoanApplication().getEnquiryNo().getId().toString());
                    }
                    result.put("loanApplication",preliminaryRiskAssessment.getRiskAssessment().getLoanApplication());
                    return result;
            }

        } catch (Exception ex) {

            //System.out.println(className);
        }
        return null;
    }


    private ChangeDocument prepareHeader(UUID loanBusinessProcessObjectId,
                                         String entityId,
                                         String mainEntityId,
                                         String loanContractId,
                                         Object changedObject,
                                         String action,
                                         String userName,
                                         String businessProcessName, String subProcessName) {

        Map<String, Object> result = getObjectDetails(changedObject.getClass().getSimpleName(), changedObject);


        //ChangeDocument changeDocument = new ChangeDocument();
        changeDocument.setLoanBusinessProcessObjectId(loanBusinessProcessObjectId);
        changeDocument.setDate(new Date());
        LoanApplication loanApplication = new LoanApplication();
        if (loanContractId != null) {
            loanApplication = loanApplicationRepository.findByLoanContractId(loanContractId);
            if (loanApplication == null) {
                EnquiryNo enquiryNo = new EnquiryNo();
                enquiryNo.setId( Long.parseLong( loanContractId.toString()));
                loanApplication = loanApplicationRepository.findByEnquiryNo(enquiryNo);
            }
        }
            else{
                loanApplication = (LoanApplication) result.get("loanApplication");
            }

        changeDocument.setLoanApplication(loanApplication);
        changeDocument.setLoanContractId(loanContractId);

        changeDocument.setAction(action);
        changeDocument.setBusinessProcessName(businessProcessName);
        changeDocument.setSubProcessName(subProcessName);
        changeDocument.setEnitityId(entityId);
        changeDocument.setMainEntityId(mainEntityId);
        changeDocument.setUserName(userName);

        changeDocument.setTableKey((String)result.get("description"));

        return changeDocument;
    }


}
