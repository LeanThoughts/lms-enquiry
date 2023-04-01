export class SiteVisitModel {

    id: string;
    serialNumber: number;
    siteVisitType: string;
    actualCOD: Date;
    dateOfSiteVisit: Date;
    dateOfLendersMeet: Date;
    documentType: string;
    documentTitle: string;
    fileReference: string;
    fiscalYear: string;
    initialSCOD: Date;
    revisedSCOD1: Date;
    revisedSCOD2: Date;
    businessPartnerId: string;
    businessPartnerName: string;

    /**
     * constructor()
     * @param _siteVisit 
     */
    constructor(_siteVisit: any)
    {
        this.id = _siteVisit.id || '';
        this.serialNumber = _siteVisit.serialNumber || 0;
        this.siteVisitType = _siteVisit.siteVisitType || '',
        this.actualCOD = _siteVisit.actualCOD || undefined;
        this.dateOfSiteVisit = _siteVisit.dateOfSiteVisit || undefined;
        this.dateOfLendersMeet = _siteVisit.dateOfLendersMeet || undefined;
        this.documentType = _siteVisit.documentType || '';
        this.documentTitle = _siteVisit.documentTitle || '';
        this.fileReference  = _siteVisit.fileReference || '';
        this.fiscalYear = _siteVisit.fiscalYear || '';
        this.initialSCOD = _siteVisit.initialSCOD || undefined;
        this.revisedSCOD1 = _siteVisit.revisedSCOD1 || undefined;
        this.revisedSCOD2 = _siteVisit.revisedSCOD2 || undefined;
        this.businessPartnerId = _siteVisit.businessPartnerId || '';
        this.businessPartnerName = _siteVisit.businessPartnerName || '';
    }
}
