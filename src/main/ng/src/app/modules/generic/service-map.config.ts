import { BusinessPartnerSearchService } from "../business-partner-search/business-partner-search.service";
import { IccInprincipleApprovalService } from "../loan-contract-search/functional-stage/icc-inprinciple-approval/icc-inprinciple-approval.service";
import { ProcessEnquiryService } from "../loan-contract-search/functional-stage/process-enquiry/process-enquiry.service";
import { ProjectProposalService } from "../loan-contract-search/functional-stage/process-enquiry/project-proposal/project-proposal.service";
import { RiskAssessmentService } from "../loan-contract-search/functional-stage/risk-assessment/risk-assessment.service";
import { ApplicationFeeService } from "../loan-contract-search/functional-stage/application-fee/application-fee.service";
import { SanctionService } from "../loan-contract-search/functional-stage/sanction/sanction.service";
import { BoardApprovalService } from "../loan-contract-search/functional-stage/board-approval/board-approval.service";

export const entityServiceMap: { [key: string]: any } = {

    businessPartnerFICustomerVendor: BusinessPartnerSearchService,
    businessPartnerFinancials: BusinessPartnerSearchService,
    businessPartnerBasicInformation: BusinessPartnerSearchService,
    businessPartnerKYCDetails: BusinessPartnerSearchService,
    businessPartnerLoanContacts: BusinessPartnerSearchService,
    businessPartnerBankDetails: BusinessPartnerSearchService,
    businessPartnerIdentificationDetails: BusinessPartnerSearchService,
    businessPartnerIndustryDetails: BusinessPartnerSearchService,
    businessPartnerRoles: BusinessPartnerSearchService,
    
    processEnquiryRejectedByPFS: ProcessEnquiryService,
    processEnquiryOtherDetails: ProcessEnquiryService,
    processEnquiryReasonForDelay: ProcessEnquiryService,
    processEnquiryEnquiryCompletion: ProcessEnquiryService,
    processEnquiryRejectedByCustomer: ProcessEnquiryService,
    processEnquiryProjectProposal: ProcessEnquiryService,
    
    projectProposal: ProjectProposalService,
    projectProposalProjectDetails: ProjectProposalService,
    projectProposalCreditRating: ProjectProposalService,
    projectProposalProjectCostDetails: ProjectProposalService,
    projectProposalShareHolding: ProjectProposalService,
    projectProposalOtherLoanDetails: ProjectProposalService,
    projectProposalOtherLoanDetailsDocuments: ProjectProposalService,
    projectProposalPromoterFinancials: ProjectProposalService,
    projectProposalCollateralDetails: ProjectProposalService,
    projectProposalDealGuarantee: ProjectProposalService,

    iccInprincipleApprovalFurtherDetails: IccInprincipleApprovalService,
    iccInprincipleApprovalReasonsForDelay: IccInprincipleApprovalService,
    iccInprincipleApprovalRejectedByIcc: IccInprincipleApprovalService,
    iccInprincipleApprovalApprovalByIcc: IccInprincipleApprovalService,
    iccInprincipleApprovalRejectedByCustomer: IccInprincipleApprovalService,
    iccInprincipleApprovalLoanEnhancements: IccInprincipleApprovalService,
    iccInprincipleApprovalRiskNotifications: IccInprincipleApprovalService,

    riskAssessmentPreliminaryRiskAssessment: RiskAssessmentService,

    applicationFeeTermSheets: ApplicationFeeService,
    applicationFeeFormalRequests: ApplicationFeeService,
    applicationFeeInceptionFees: ApplicationFeeService,
    applicationFeeProjectDetails: ApplicationFeeService,

    sanctionReasonsForDelay: SanctionService,
    sanctionPaymentReceiptsPreSanction: SanctionService,
    sanctionLetters: SanctionService,
    sanctionPaymentReceiptsPostSanction: SanctionService,
    sanctionRejectedByCustomer: SanctionService,

    boardApprovalDeferredByBoard: BoardApprovalService,
    boardApprovalReasonsForDelay: BoardApprovalService,
    boardApprovalRejectedByBoard: BoardApprovalService,
    boardApprovalApprovalByBoard: BoardApprovalService,
    boardApprovalRejectedByCustomer: BoardApprovalService,
}