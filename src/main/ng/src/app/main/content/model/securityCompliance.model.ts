export class SecurityComplianceModel {

    id: string;
    collateralObjectType: string;
    collateralAgreementType: string; //(typeofSecurity)
    collateralAgreementTypeDescription: string;
    timelines: string;
    applicability: string;
    dateOfCreation: Date;
    value: number;
    validityDate: Date;
    securityPerfectionDate: Date;
    actionPeriod: string;
    actionPeriodSuffix: string;
    numberOfDays: number;
    eventType: string;
    eventDate: Date;
    timelineDate: Date;
    validityPeriod: string;
    remarks: string;
    location: string;
    additionalText: string;

    realEstateLandArea: number;
    areaUnitOfMeasure: string

    securityNoOfUnits: number;
    securityFaceValueAmount: number;
    holdingPercentage: number;

    /**
     * constructor()
     * @param _securityAndCompliance 
     */
    constructor(_securityAndCompliance: any)
    {
        this.id = _securityAndCompliance.id || '';
        this.collateralObjectType = _securityAndCompliance.collateralObjectType || '';
        this.collateralAgreementType = _securityAndCompliance.collateralAgreementType || '';
        this.collateralAgreementTypeDescription = _securityAndCompliance.collateralAgreementTypeDescription || '';
        this.timelines = _securityAndCompliance.timelines || '';
        this.applicability = _securityAndCompliance.applicability || '';
        this.dateOfCreation = _securityAndCompliance.dateOfCreation || undefined;
        this.value = _securityAndCompliance.value || 0;
        this.validityDate = _securityAndCompliance.validityDate || undefined;
        this.securityPerfectionDate = _securityAndCompliance.securityPerfectionDate || undefined;
        this.actionPeriod = _securityAndCompliance.actionPeriod || '';
        this.actionPeriodSuffix = _securityAndCompliance.actionPeriodSuffix || '';
        this.numberOfDays = _securityAndCompliance.numberOfDays || 0;
        this.eventType = _securityAndCompliance.eventType || '';
        this.eventDate = _securityAndCompliance.eventDate || undefined;
        this.timelineDate = _securityAndCompliance.timelineDate || undefined;
        this.validityPeriod = _securityAndCompliance.validityPeriod || '';
        this.remarks = _securityAndCompliance.remarks || '';
        this.location = _securityAndCompliance.location || '';
        this.additionalText = _securityAndCompliance.additionalText || '';

        this.realEstateLandArea = _securityAndCompliance.realEstateLandArea || 0;
        this.areaUnitOfMeasure = _securityAndCompliance.areaUnitOfMeasure || '';

        this.securityNoOfUnits = _securityAndCompliance.securityNoOfUnits || 0;
        this.securityFaceValueAmount = _securityAndCompliance.securityFaceValueAmount || 0;
        this.holdingPercentage = _securityAndCompliance.holdingPercentage || 0;
    }
}
