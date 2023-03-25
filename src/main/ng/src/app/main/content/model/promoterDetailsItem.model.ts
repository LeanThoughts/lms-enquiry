export class PromoterDetailsItemModel {

    id: string;
    serialNumber: number;
    shareHoldingCompany: string;
    paidupCapitalEquitySanction: number;
    equityLinkInstrumentSanction: number;
    paidupCapitalEquityCurrent: number;
    equityLinkInstrumentCurrent: number;
    dateOfChange: Date;
    groupExposure: number;

    /**
     * constructor()
     */
    constructor(_promoterDetailItem: any) {
        this.id = _promoterDetailItem.id;
        this.serialNumber = _promoterDetailItem.serialNumber || 0;
        this.shareHoldingCompany = _promoterDetailItem.shareHoldingCompany || '';
        this.paidupCapitalEquitySanction = _promoterDetailItem.paidupCapitalEquitySanction || 0;
        this.paidupCapitalEquityCurrent = _promoterDetailItem.paidupCapitalEquityCurrent || 0;
        this.equityLinkInstrumentSanction = _promoterDetailItem.equityLinkInstrumentSanction || 0;
        this.equityLinkInstrumentCurrent = _promoterDetailItem.equityLinkInstrumentCurrent || 0;
        this.dateOfChange = _promoterDetailItem.dateOfChange || '';
        this.groupExposure = _promoterDetailItem.groupExposure || 0;
    }
}
