export class LFAReportAndFeeModel
{
    id: string;
    reportType: string;
    dateOfReceipt: Date;
    invoiceDate: Date;
    invoiceNo: string;
    feeAmount: number;
    statusOfFeeReceipt: string;
    statusOfFeePaid: string;
    documentTitle: string;
    nextReportDate: Date;
    fileReference: string;
    documentType: string;
    sapFIInvoiceDate: Date;
    sapFIInvoiceNumber: string;
    feeAmountRaisedOnCustomer: number;
    reportDate: Date;
    percentageCompletion: number;
    remarks: string;

    /**
     * constructor();
     * @param _lfaReportAndFee 
     */
    constructor(_lfaReportAndFee: any)
    {
        this.id = _lfaReportAndFee.id || '';
        this.reportType = _lfaReportAndFee.reportType || '';
        this.dateOfReceipt = _lfaReportAndFee.dateOfReceipt || undefined;
        this.invoiceDate = _lfaReportAndFee.invoiceDate || undefined;
        this.invoiceNo = _lfaReportAndFee.invoiceNo || '';
        this.feeAmount = _lfaReportAndFee.feeAmount || 0;
        this.statusOfFeeReceipt = _lfaReportAndFee.statusOfFeeReceipt || '';
        this.statusOfFeePaid = _lfaReportAndFee.statusOfFeePaid || '';
        this.documentTitle = _lfaReportAndFee.documentTitle || '';
        this.nextReportDate = _lfaReportAndFee.nextReportDate || undefined;
        this.documentType = _lfaReportAndFee.documentType || '';
        this.fileReference = _lfaReportAndFee.fileReference || '';
        this.sapFIInvoiceDate = _lfaReportAndFee.sapFIInvoiceDate || undefined;
        this.sapFIInvoiceNumber = _lfaReportAndFee.sapFIInvoiceNumber || '';
        this.feeAmountRaisedOnCustomer = _lfaReportAndFee.feeAmountRaisedOnCustomer;
        this.reportDate = _lfaReportAndFee.reportDate || undefined;
        this.percentageCompletion = _lfaReportAndFee.percentageCompletion || '';
        this.remarks = _lfaReportAndFee.remarks || '';
    }
}
