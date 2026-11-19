/** Entities that require a module Edit permission before create/update/delete or save. */
export const ENTITY_EDIT_PERMISSION: Record<string, string> = {
  processEnquiryProjectProposal: 'LoanContracts.ProcessEnquiry.Edit',
  processEnquiryRejectedByPFS: 'LoanContracts.ProcessEnquiry.Edit',
  processEnquiryOtherDetails: 'LoanContracts.ProcessEnquiry.Edit',
  processEnquiryReasonForDelay: 'LoanContracts.ProcessEnquiry.Edit',
  processEnquiryEnquiryCompletion: 'LoanContracts.ProcessEnquiry.Edit',
  processEnquiryRejectedByCustomer: 'LoanContracts.ProcessEnquiry.Edit',
  iccInprincipleApprovalFurtherDetails: 'LoanContracts.IccApproval.Edit',
  iccInprincipleApprovalReasonsForDelay: 'LoanContracts.IccApproval.Edit',
  iccInprincipleApprovalRejectedByIcc: 'LoanContracts.IccApproval.Edit',
  iccInprincipleApprovalApprovalByIcc: 'LoanContracts.IccApproval.Edit',
  iccInprincipleApprovalRejectedByCustomer: 'LoanContracts.IccApproval.Edit',
  iccInprincipleApprovalLoanEnhancements: 'LoanContracts.IccApproval.Edit',
  iccInprincipleApprovalRiskNotifications: 'LoanContracts.IccApproval.Edit',
  riskAssessmentPreliminaryRiskAssessment: 'LoanContracts.RiskAssessment.Edit',
  applicationFeeTermSheets: 'LoanContracts.ApplicationFee.Edit',
  applicationFeeFormalRequests: 'LoanContracts.ApplicationFee.Edit',
  applicationFeeInceptionFees: 'LoanContracts.ApplicationFee.Edit',
  applicationFeeProjectDetails: 'LoanContracts.ApplicationFee.Edit',
  bmcApprovalFurtherDetails: 'LoanContracts.BmcApproval.Edit',
  bmcApprovalReasonsForDelay: 'LoanContracts.BmcApproval.Edit',
  bmcApprovalRejectedByIcc: 'LoanContracts.BmcApproval.Edit',
  bmcApprovalApprovalByIcc: 'LoanContracts.BmcApproval.Edit',
  bmcApprovalRejectedByCustomer: 'LoanContracts.BmcApproval.Edit',
  bmcApprovalLoanEnhancements: 'LoanContracts.BmcApproval.Edit',
  boardApprovalDeferredByBoard: 'LoanContracts.BoardApproval.Edit',
  boardApprovalReasonsForDelay: 'LoanContracts.BoardApproval.Edit',
  boardApprovalRejectedByBoard: 'LoanContracts.BoardApproval.Edit',
  boardApprovalApprovalByBoard: 'LoanContracts.BoardApproval.Edit',
  boardApprovalRejectedByCustomer: 'LoanContracts.BoardApproval.Edit',
  sanctionReasonsForDelay: 'LoanContracts.Sanction.Edit',
  sanctionPaymentReceiptsPreSanction: 'LoanContracts.Sanction.Edit',
  sanctionLetters: 'LoanContracts.Sanction.Edit',
  sanctionPaymentReceiptsPostSanction: 'LoanContracts.Sanction.Edit',
  sanctionRejectedByCustomer: 'LoanContracts.Sanction.Edit',
};

export function hasEntityEditPermission(
  entity: string,
  hasModulePermission: (permission: string) => boolean
): boolean {
  const permission = ENTITY_EDIT_PERMISSION[entity];
  if (!permission) {
    return true;
  }
  return hasModulePermission(permission);
}