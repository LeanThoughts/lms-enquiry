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
    }
}
