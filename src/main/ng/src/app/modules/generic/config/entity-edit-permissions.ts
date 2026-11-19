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