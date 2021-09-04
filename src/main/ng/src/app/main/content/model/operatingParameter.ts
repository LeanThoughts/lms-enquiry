export class OperatingParameterModel {

    id: string;
    serialNumber: number;
    month: string;
    year: number;
    exportNetGeneration: number;
    plfCufActual: number;
    applicableTariff: number;
    revenue: number;
    dateOfInvoice: Date;
    dateOfPaymentReceipt: Date;
    carbonDiOxideEmission: number;
    waterSaved: number;
    remarks: string;
    actualYearlyAveragePlfCuf: string;
    documentType: string;
    documentTitle: string;
    fileReference: string;

    /**
     * constructor()
     * @param _operatingParameter 
     */
    constructor(_operatingParameter: any)
    {
        this.id = _operatingParameter.id || '';
        this.serialNumber = _operatingParameter.serialNumber || 0;
        this.month = _operatingParameter.month || '';
        this.year = _operatingParameter.year || 0;
        this.exportNetGeneration = _operatingParameter.exportNetGeneration || 0;
        this.plfCufActual = _operatingParameter.plfCufActual || 0;
        this.applicableTariff = _operatingParameter.applicableTariff || 0;
        this.revenue = _operatingParameter.revenue || 0;
        this.dateOfInvoice = _operatingParameter.dateOfInvoice || undefined;
        this.dateOfPaymentReceipt = _operatingParameter.dateOfPaymentReceipt || undefined;
        this.carbonDiOxideEmission = _operatingParameter.carbonDiOxideEmission || 0;
        this.waterSaved = _operatingParameter.waterSaved || 0;
        this.remarks = _operatingParameter.remarks || '';
        this.actualYearlyAveragePlfCuf = _operatingParameter.actualYearlyAveragePlfCuf || '';
        this.documentType = _operatingParameter.documentType || '';
        this.documentTitle = _operatingParameter.documentTitle || '';
        this.fileReference = _operatingParameter.fileReference || '';
    }
}
