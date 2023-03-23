export class TandCModel
{
    id: string;
    documentType: string;
    leadBankerDocumentType: string;
    documentTitle: string;
    leadBankerDocumentTitle: string;
    communication: string;
    borrowerRequestLetterDate: Date;
    dateOfIssueOfAmendedSanctionLetter: Date;
    remarks: string;
    reasonsForAmendment: string;
    fileReference: string;

    amendedDocumentType: string;
    dateOfIssueOfAmendedDocument: Date;
    amendedDocumentRemarks: string;
    amendedDocumentTitle: string;
    amendedDocumentFileReference: string;

    leadBankerDocumentFileReference: string;

    internalDocumentType: string;
    dateOfInternalDocument: string;
    internalDocumentTitle: string;
    internalDocumentRemarks: string;
    internalDocumentFileReference: string;

    brlReasonsForAmendment: string;

    /**
     * constructor();
     * @param _tandc 
     */
    constructor(_tandc: any)
    {
        this.id = _tandc.id || '';
        this.documentType = _tandc.documentType || '';
        this.leadBankerDocumentType = _tandc.leadBankerDocumentType || '';
        this.documentTitle = _tandc.documentTitle || '';
        this.leadBankerDocumentTitle = _tandc.leadBankerDocumentTitle || '';
        this.communication = _tandc.communication || '';
        this.borrowerRequestLetterDate = _tandc.borrowerRequestLetterDate || undefined;
        this.dateOfIssueOfAmendedSanctionLetter = _tandc.dateOfIssueOfAmendedSanctionLetter || undefined;
        this.remarks = _tandc.remarks || '';
        this.reasonsForAmendment = _tandc.reasonsForAmendment || '';
        this.fileReference = _tandc.fileReference || '';
        this.amendedDocumentType = _tandc.amendedDocumentType || '';
        this.dateOfIssueOfAmendedDocument = _tandc.dateOfIssueOfAmendedDocument || undefined;
        this.amendedDocumentRemarks = _tandc.amendedDocumentRemarks || '';
        this.amendedDocumentTitle = _tandc.amendedDocumentTitle || '';
        this.amendedDocumentFileReference = _tandc.amendedDocumentFileReference || '';
        this.leadBankerDocumentFileReference = _tandc.leadBankerDocumentFileReference || '';

        this.internalDocumentType = _tandc.internalDocumentType || '';
        this.dateOfInternalDocument = _tandc.dateOfInternalDocument || '';
        this.internalDocumentTitle = _tandc.internalDocumentTitle || '';
        this.internalDocumentRemarks = _tandc.internalDocumentRemarks || '';
        this.internalDocumentFileReference = _tandc.internalDocumentFileReference || '';
        this.brlReasonsForAmendment = _tandc.brlReasonsForAmendment || '';
    }
}
