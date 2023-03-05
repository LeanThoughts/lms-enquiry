package pfs.lms.enquiry.service.changedocs.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.model.Site;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.ValueChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.appraisal.LoanAppraisalRepository;
import pfs.lms.enquiry.appraisal.customerrejection.CustomerRejection;
import pfs.lms.enquiry.appraisal.furtherdetail.FurtherDetail;
import pfs.lms.enquiry.appraisal.knowyourcustomer.KnowYourCustomer;
import pfs.lms.enquiry.appraisal.loanpartner.LoanPartner;
import pfs.lms.enquiry.appraisal.projectappraisalcompletion.ProjectAppraisalCompletion;
import pfs.lms.enquiry.appraisal.projectdata.ProjectData;
import pfs.lms.enquiry.appraisal.proposaldetails.ProposalDetail;
import pfs.lms.enquiry.appraisal.reasonfordelay.ReasonForDelay;
import pfs.lms.enquiry.appraisal.riskrating.CorporateLoanRiskRating;
import pfs.lms.enquiry.appraisal.riskrating.TermLoanRiskRating;
import pfs.lms.enquiry.appraisal.syndicateconsortium.SyndicateConsortium;
import pfs.lms.enquiry.domain.*;
import pfs.lms.enquiry.monitoring.borrowerfinancials.BorrowerFinancials;
import pfs.lms.enquiry.monitoring.domain.*;
import pfs.lms.enquiry.monitoring.lfa.LFAReportAndFee;
import pfs.lms.enquiry.monitoring.lfa.LendersFinancialAdvisor;
import pfs.lms.enquiry.monitoring.lie.LIEReportAndFee;
import pfs.lms.enquiry.monitoring.lie.LendersIndependentEngineer;
import pfs.lms.enquiry.monitoring.operatingparameters.OperatingParameter;
import pfs.lms.enquiry.monitoring.operatingparameters.OperatingParameterPLF;
import pfs.lms.enquiry.monitoring.projectmonitoring.ProjectMonitoringDataItemHistory;
import pfs.lms.enquiry.monitoring.projectmonitoring.ProjectMonitoringDataItemResource;
import pfs.lms.enquiry.monitoring.promoterdetails.PromoterDetail;
import pfs.lms.enquiry.monitoring.promoterfinancials.PromoterFinancials;
import pfs.lms.enquiry.monitoring.tra.TrustRetentionAccount;
import pfs.lms.enquiry.monitoring.tra.TrustRetentionAccountStatement;
import pfs.lms.enquiry.repository.ChangeDocumentRepository;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.ISAPIntegrationPointerService;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.*;

import static org.javers.core.diff.ListCompareAlgorithm.LEVENSHTEIN_DISTANCE;

/**
 * Created by sajeev on 17-Dec-18.
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class ChangeDocumentService implements IChangeDocumentService {


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


        sapIntegrationPointerService.saveForObject(businessProcessName, subProcessName, entityId, mainEntityId,mode);

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
            Map<String, String> result = getObjectDetails(object.getClass().getSimpleName(), object);

            changeDocumentItem.setEntityName(object.getClass().getSimpleName().toString());
            changeDocumentItem.setEntityDescription(result.get("description"));
            changeDocumentItem.setAttributeName(change.getPropertyName());

            if (change.getRight() != null)
                changeDocumentItem.setNewValue(change.getRight().toString());
            else
                changeDocumentItem.setNewValue(null);
            if (change.getLeft() != null)
                changeDocumentItem.setOldValue(change.getLeft().toString());
            else
                changeDocumentItem.setOldValue(null);

            changeDocumentItem.setTableKey(result.get("id"));

            changeDocumentItem.setCreatedAt(new Date());
            changeDocumentItem.setUpdatedAt(new Date());


            changeDocumentItems.add(changeDocumentItem);

            i++;

        }


        return changeDocumentItems;

    }

    private Map<String, String> getObjectDetails(String className, Object object) {

        Object objectParsed = new Object();

        Map<String, String> result = new HashMap<>();

        try {

            switch (className) {
                case "LoanMonitor":
                    LoanMonitor loanMonitor = (LoanMonitor) object;
                    result.put("id", loanMonitor.getLoanApplication().getLoanContractId().toString());
                    result.put("description", loanMonitor.getLoanApplication().getProjectName());
                    return result;
                case "LendersIndependentEngineer":
                    LendersIndependentEngineer lendersIndependentEngineer = (LendersIndependentEngineer) object;
                    result.put("id", lendersIndependentEngineer.getSerialNumber().toString());
                    result.put("description", lendersIndependentEngineer.getName());
                    return result;

                case "LIEReportAndFee":
                    LIEReportAndFee lieReportAndFee = (LIEReportAndFee) object;
                    result.put("id", lieReportAndFee.getLendersIndependentEngineer().getSerialNumber().toString());
                    result.put("description", lieReportAndFee.getReportType() + lieReportAndFee.getDateOfReceipt());
                    return result;

                case "LendersFinancialAdvisor":
                    LendersFinancialAdvisor lendersFinancialAdvisor = (LendersFinancialAdvisor) object;
                    result.put("id", lendersFinancialAdvisor.getSerialNumber().toString());
                    result.put("description", lendersFinancialAdvisor.getName());
                    return result;

                case "LFAReportAndFee":
                    LFAReportAndFee lfaReportAndFee = (LFAReportAndFee) object;
                    result.put("id", lfaReportAndFee.getLendersFinancialAdvisor().getSerialNumber().toString());
                    result.put("description", lfaReportAndFee.getReportType() + lfaReportAndFee.getDateOfReceipt());
                    return result;
                case "TrustRetentionAccount$HibernateProxy$Lup33nzA":
                case "TrustRetentionAccount":
                    TrustRetentionAccount trustRetentionAccount = (TrustRetentionAccount) object;
                    result.put("id", trustRetentionAccount.getSerialNumber().toString());
                    result.put("description", trustRetentionAccount.getBankKey());
                    return result;
                case "TrustRetentionAccountStatement":
                    TrustRetentionAccountStatement trustRetentionAccountStatement = (TrustRetentionAccountStatement) object;
                    result.put("id", trustRetentionAccountStatement.getSerialNumber().toString());
                    result.put("description", trustRetentionAccountStatement.getDocumentType());
                    return result;
                case "TermsAndConditionsModification":
                    TermsAndConditionsModification termsAndConditionsModification = (TermsAndConditionsModification) object;
                    result.put("id", termsAndConditionsModification.getSerialNumber().toString());
                    result.put("description", termsAndConditionsModification.getCommunication());
                    return result;
                case "SecurityCompliance":
                    SecurityCompliance securityCompliance = (SecurityCompliance) object;
                    result.put("id", securityCompliance.getSerialNumber().toString());
                    result.put("description", securityCompliance.getCollateralObjectType());
                    return result;
                case "SiteVisit":
                    SiteVisit siteVisit = (SiteVisit) object;
                    result.put("id", siteVisit.getSerialNumber().toString());
                    result.put("description", siteVisit.getSerialNumber().toString());
                    return result;
                case "OperatingParameter":
                    OperatingParameter operatingParameter = (OperatingParameter) object;
                    result.put("id", operatingParameter.getSerialNumber().toString());
                    result.put("description", operatingParameter.getSerialNumber().toString());
                    return result;
                case "OperatingParameterPLF":
                    OperatingParameterPLF operatingParameterPLF = (OperatingParameterPLF) object;
                    result.put("id", operatingParameterPLF.getSerialNumber().toString());
                    result.put("description", operatingParameterPLF.getSerialNumber().toString());
                    return result;
                case "RateOfInterest":
                    RateOfInterest rateOfInterest = (RateOfInterest) object;
                    result.put("id", rateOfInterest.getSerialNumber().toString());
                    result.put("description", rateOfInterest.getConditionType().toString());
                    return result;
                case "BorrowerFinancials":
                    BorrowerFinancials borrowerFinancials = (BorrowerFinancials) object;
                    result.put("id", borrowerFinancials.getSerialNumber().toString());
                    result.put("description", borrowerFinancials.getFiscalYear().toString());
                    return result;
                case "PromoterFinancials":
                    PromoterFinancials promoterFinancials = (PromoterFinancials) object;
                    result.put("id", promoterFinancials.getSerialNumber().toString());
                    result.put("description", promoterFinancials.getFiscalYear().toString());
                    return result;
                case "FinancialCovenants":
                    FinancialCovenants financialCovenants = (FinancialCovenants) object;
                    result.put("id", financialCovenants.getSerialNumber().toString());
                    result.put("description", financialCovenants.getFinancialCovenantType().toString());
                    return result;

                case "PromoterDetail":
                    PromoterDetail promoterDetails = (PromoterDetail) object;
                    result.put("id", promoterDetails.getId().toString());
                    result.put("description", promoterDetails.getDateOfChange().toString());
                    return result;
                case "ProjectMonitoringDataItemResource":
                    ProjectMonitoringDataItemResource projectMonitoringDataItemResource = (ProjectMonitoringDataItemResource) object;
                    result.put("id", projectMonitoringDataItemResource.getId().toString());
                    result.put("description", projectMonitoringDataItemResource.getParticulars().toString());
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
                        }
                        else{
                            result.put("id", loanAppraisal.getLoanApplication().getEnquiryNo().toString());
                            result.put("description", loanAppraisal.getLoanApplication().getEnquiryNo().toString());
                            changeDocument.setLoanApplication(loanAppraisal.getLoanApplication());
                        }
                    }
                    else {
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

                        }
                        else{
                            result.put("id", loanAppraisal1.getLoanApplication().getEnquiryNo().toString());
                            result.put("description", loanAppraisal1.getLoanApplication().getEnquiryNo().toString());
                            changeDocument.setLoanApplication(loanAppraisal1.getLoanApplication());
                        }
                    }
                    else {
                        result.put("id", loanAppraisal1.getId().toString());
                        result.put("description", loanAppraisal1.getId().toString());

                    }
                    return result;

                case "CustomerRejection":
                    CustomerRejection customerRejection = (CustomerRejection) object;
                    result.put("id", customerRejection.getDate().toString());
                    result.put("description", customerRejection.getDate().toString());
                    return result;
                case "FurtherDetail":
                    FurtherDetail furtherDetail = (FurtherDetail) object;
                    result.put("id", furtherDetail.getDate().toString());
                    result.put("description", "Further details");//furtherDetail.getDate().toString());
                    return result;
                case "LoanPartner":
                    LoanPartner loanPartner = (LoanPartner) object;
                    LoanAppraisal loanAppraisal2 =  loanAppraisalRepository.getOne(UUID.fromString(loanPartner.getLoanAppraisalId()));
                    if (loanAppraisal2.getLoanApplication() != null) {
                        if (loanAppraisal2.getLoanApplication().getLoanContractId() != null) {
                            result.put("id",loanPartner.getBusinessPartnerId().toString());
                            result.put("description",loanPartner.getBusinessPartnerId().toString());
                            changeDocument.setLoanApplication(loanAppraisal2.getLoanApplication());

                        }
                        else{
                            result.put("id",loanPartner.getBusinessPartnerId().toString());
                            result.put("description",loanPartner.getBusinessPartnerId().toString());
                            changeDocument.setLoanApplication(loanAppraisal2.getLoanApplication());
                        }
                    }
                    else {
                        result.put("id",loanPartner.getBusinessPartnerId().toString());
                        result.put("description",loanPartner.getBusinessPartnerId().toString());
                    }

                    return result;
                case "KnowYourCustomer":
                    KnowYourCustomer knowYourCustomer = (KnowYourCustomer) object;
                    result.put("id", knowYourCustomer.getDocumentType().toString());
                    result.put("description", knowYourCustomer.getDocumentName().toString());
                    return result;
                case "ProjectAppraisalCompletion":
                    ProjectAppraisalCompletion projectAppraisalCompletion = (ProjectAppraisalCompletion) object;
                    result.put("id", projectAppraisalCompletion.getId().toString());
                    result.put("description", projectAppraisalCompletion.getDateOfProjectAppraisalCompletion().toString());
                    return result;
                case "ProjectData":
                    ProjectData projectData = (ProjectData) object;
                    result.put("id", projectData.getId().toString());
                    result.put("description", projectData.getProjectName().toString());
                    return result;
                case "ProposalDetail":
                    ProposalDetail proposalDetail = (ProposalDetail) object;
                    result.put("id", proposalDetail.getId().toString());
                    result.put("description", "Proposal Details");// proposalDetail.get().toString());
                    return result;
                case "ReasonForDelay":
                    ReasonForDelay reasonForDelay = (ReasonForDelay) object;
                    result.put("id", reasonForDelay.getDate().toString());
                    result.put("description", "Reason for delay");//reasonForDelay.getId().toString());
                    return result;
                case "SyndicateConsortium":
                    SyndicateConsortium syndicateConsortium = (SyndicateConsortium) object;
                    result.put("id", syndicateConsortium.getId().toString());
                    result.put("description", syndicateConsortium.getBankName().toString());
                    return result;
                case "CorporateLoanRiskRating":
                    CorporateLoanRiskRating corporateLoanRiskRating = (CorporateLoanRiskRating) object;
                    result.put("id", corporateLoanRiskRating.getId().toString());
                    result.put("description", corporateLoanRiskRating.getYear());
                    return result;
                case "TermLoanRiskRating":
                    TermLoanRiskRating termLoanRiskRating = (TermLoanRiskRating) object;
                    result.put("id", termLoanRiskRating.getId().toString() );
                    result.put("description", termLoanRiskRating.getYear());
                    return result;



            }

        } catch (Exception ex) {

            //System.out.println(className);
        }
        return null;
    }


    private ChangeDocument prepareHeader(UUID loanBusinessProcessObjectId, String entityId, String mainEntityId,
                                         String loanContractId,
                                         Object changedObject,
                                         String action,
                                         String userName,
                                         String businessProcessName, String subProcessName) {

        //ChangeDocument changeDocument = new ChangeDocument();
        changeDocument.setLoanBusinessProcessObjectId(loanBusinessProcessObjectId);
        changeDocument.setDate(new Date());
        LoanApplication loanApplication = new LoanApplication();
        if (loanContractId != null) {
            loanApplication = loanApplicationRepository.findByLoanContractId(loanContractId);
            changeDocument.setLoanApplication(loanApplication);
            changeDocument.setLoanContractId(loanContractId);
        }

        changeDocument.setAction(action);
        changeDocument.setBusinessProcessName(businessProcessName);
        changeDocument.setSubProcessName(subProcessName);
        changeDocument.setEnitityId(entityId);
        changeDocument.setMainEntityId(mainEntityId);
        changeDocument.setUserName(userName);

        Map<String, String> result = getObjectDetails(changedObject.getClass().getSimpleName(), changedObject);
        changeDocument.setTableKey(result.get("description"));

        return changeDocument;
    }


}
