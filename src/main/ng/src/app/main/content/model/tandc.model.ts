export class TandCModel
{
    id: string;
    documentType: string;
    documentTitle: string;
    communication: string;
    borrowerRequestLetterDate: Date;
    dateOfIssueOfAmendedSanctionLetter: Date;
    remarks: string;
    fileReference: string;

    amendedDocumentType: string;
    dateOfIssueOfAmendedDocument: Date;
    amendedDocumentRemarks: string;
    amendedDocumentTitle: string;
    amendedDocumentFileReference: string;

    /**
     * constructor();
     * @param _tandc 
     */
    constructor(_tandc: any)
    {
        this.id = _tandc.id || '';
        this.documentType = _tandc.documentType || '';
        this.documentTitle = _tandc.documentTitle || '';
        this.communication = _tandc.communication || '';
        this.borrowerRequestLetterDate = _tandc.borrowerRequestLetterDate || undefined;
        this.dateOfIssueOfAmendedSanctionLetter = _tandc.dateOfIssueOfAmendedSanctionLetter || undefined;
        this.remarks = _tandc.remarks || '';
        this.fileReference = _tandc.fileReference || '';
        this.amendedDocumentType = _tandc.amendedDocumentType || '';
        this.dateOfIssueOfAmendedDocument = _tandc.dateOfIssueOfAmendedDocument || undefined;
        this.amendedDocumentRemarks = _tandc.amendedDocumentRemarks || '';
        this.amendedDocumentTitle = _tandc.amendedDocumentTitle || '';
        this.amendedDocumentFileReference = _tandc.amendedDocumentFileReference || '';
    }
}
