export class BorrowerFinancialsModel {

    id: string;
    fiscalYear: number;
    turnover: number;
    pat: number;
    netWorth: number;
    dateOfExternalRating: Date;
    nextDueDateOfExternalRating: Date;
    overAllRating: string;
    annualReturnFileReference: string;
    ratingFileReference: string;
    remarks: string;

    /**
     * constructor()
     * @param _borrowerFinancialsDetails 
     */
    constructor(_borrowerFinancialsDetails: any)
    {
        this.id = _borrowerFinancialsDetails.id || '';
        this.fiscalYear = _borrowerFinancialsDetails.fiscalYear || 0;
        this.turnover = _borrowerFinancialsDetails.turnover || 0;
        this.pat = _borrowerFinancialsDetails.pat || 0;
        this.netWorth = _borrowerFinancialsDetails.netWorth || 0;
        this.dateOfExternalRating = _borrowerFinancialsDetails.dateOfExternalRating || undefined;
        this.nextDueDateOfExternalRating = _borrowerFinancialsDetails.nextDueDateOfExternalRating || undefined;
        this.overAllRating = _borrowerFinancialsDetails.overAllRating || '';
        this.annualReturnFileReference = _borrowerFinancialsDetails.annualReturnFileReference || '';
        this.ratingFileReference = _borrowerFinancialsDetails.ratingFileReference || '';
        this.remarks = _borrowerFinancialsDetails.remarks || '';
    }
}
