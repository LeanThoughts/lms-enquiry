export class LoanDocumentationModel
{
  id: string;
  serialNumber: string;
  documentationTypeCode: string;
  documentationTypeDescription: string;
  executionDate: Date;
  approvalDate: Date;
  loanDocumentationStatusCode: string;
  loanDocumentationStatusCodeDescription: string;
  documentType: string;
  documentTitle: string;
  fileReference: string;
  remarks: string;
    /**
     * constructor();
     * @param _loanDocumentationModel
     */
    constructor(_loanDocumentationModel: any)
    {
        this.id = _loanDocumentationModel.id || '';
        this.serialNumber = _loanDocumentationModel.serialNumber || '';
        this.documentationTypeCode = _loanDocumentationModel.documentationTypeCode || '';
        this.documentationTypeDescription = _loanDocumentationModel.documentationTypeDescription || undefined;
        this.executionDate = _loanDocumentationModel.executionDate || '';
        this.approvalDate = _loanDocumentationModel.approvalDate || '';
        this.loanDocumentationStatusCode = _loanDocumentationModel.loanDocumentationStatusCode || undefined;
        this.loanDocumentationStatusCodeDescription = _loanDocumentationModel.loanDocumentationStatusCodeDescription || undefined;
        this.documentType = _loanDocumentationModel.documentType;
        this.documentTitle = _loanDocumentationModel.documentTitle || '';
        this.fileReference = _loanDocumentationModel.fileReference || undefined;
        this.remarks = _loanDocumentationModel.remarks || undefined;


    }
}
