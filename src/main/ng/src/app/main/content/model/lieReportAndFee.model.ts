export class LIEReportAndFeeModel
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
     * @param _lieReportAndFee 
     */
    constructor(_lieReportAndFee: any)
    {
        this.id = _lieReportAndFee.id || '';
        this.reportType = _lieReportAndFee.reportType || '';
        this.dateOfReceipt = _lieReportAndFee.dateOfReceipt || undefined;
        this.invoiceDate = _lieReportAndFee.invoiceDate || undefined;
        this.invoiceNo = _lieReportAndFee.invoiceNo || '';
        this.feeAmount = _lieReportAndFee.feeAmount || 0;
        this.statusOfFeeReceipt = _lieReportAndFee.statusOfFeeReceipt || '';
        this.statusOfFeePaid = _lieReportAndFee.statusOfFeePaid || '';
        this.documentTitle = _lieReportAndFee.documentTitle || '';
        this.nextReportDate = _lieReportAndFee.nextReportDate || undefined;
        this.documentType = _lieReportAndFee.documentType || '';
        this.fileReference = _lieReportAndFee.fileReference || '';
        this.sapFIInvoiceDate = _lieReportAndFee.sapFIInvoiceDate || undefined;
        this.sapFIInvoiceNumber = _lieReportAndFee.sapFIInvoiceNumber || '';
        this.feeAmountRaisedOnCustomer = _lieReportAndFee.feeAmountRaisedOnCustomer;
        this.reportDate = _lieReportAndFee.reportDate || undefined;
        this.percentageCompletion = _lieReportAndFee.percentageCompletion || '';
        this.remarks = _lieReportAndFee.remarks || '';
    }
}
