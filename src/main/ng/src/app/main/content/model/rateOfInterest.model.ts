export class RateOfInterestModel {

    id: string;
    conditionType: string;
    validFromDate: Date;
    interestTypeIndicator: string;
    referenceInterestRate: number;
    refInterestSign: number;
    interestRate: number;
    calculationDate: Date;
    isCalculationDateOnMonthEnd: boolean;
    dueDate: Date;
    isDueDateOnMonthEnd: boolean;
    interestPaymentFrequency: number;
    paymentForm: string;
    interestCalculationMethod: string;

    interestPeriodStartDate: Date;
    interestPeriodUnit: number;
    interestPeriodFrequency: number;
    nextInterestResetDate: Date;

    /**
     * constructor()
     * @param _rateOfInterest 
     */
    constructor(_rateOfInterest: any)
    {
        this.id = _rateOfInterest.id || '';
        // this.conditionType = _rateOfInterest.conditionType || '201';
        this.conditionType = _rateOfInterest.conditionType || '';
        this.validFromDate = _rateOfInterest.validFromDate || undefined;
        this.interestTypeIndicator = _rateOfInterest.interestTypeIndicator || '0';
        this.referenceInterestRate = _rateOfInterest.referenceInterestRate || '';
        this.refInterestSign = _rateOfInterest.refInterestSign || '';
        this.interestRate = _rateOfInterest.interestRate;
        this.calculationDate = _rateOfInterest.calculationDate || undefined;
        this.isCalculationDateOnMonthEnd = _rateOfInterest.isCalculationDateOnMonthEnd || false;
        this.dueDate = _rateOfInterest.dueDate || undefined;
        this.isDueDateOnMonthEnd = _rateOfInterest.isDueDateOnMonthEnd || false;
        this.interestPaymentFrequency = _rateOfInterest.interestPaymentFrequency;
        this.paymentForm = _rateOfInterest.paymentForm || 'MN';
        this.interestCalculationMethod = _rateOfInterest.interestCalculationMethod || '3';

        this.interestPeriodStartDate = _rateOfInterest.interestPeriodStartDate || undefined;
        this.interestPeriodUnit = _rateOfInterest.interestPeriodUnit || 0;
        this.interestPeriodFrequency = _rateOfInterest.interestPeriodFrequency || 0;
        this.nextInterestResetDate = _rateOfInterest.nextInterestResetDate || undefined;
    }
}
