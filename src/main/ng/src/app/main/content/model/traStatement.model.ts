export class TRAStatementModel
{
    id: string;
    viewRights: string;
    remarks: string;
    periodQuarter: string;
    periodYear: string;
    documentType: string;

    /**
     * constructor();
     * @param _traStatement 
     */
    constructor(_traStatement: any)
    {
        this.id = _traStatement.id || '';
        this.viewRights = _traStatement.viewRights || '';
        this.remarks = _traStatement.remarks || '';
        this.periodQuarter = _traStatement.periodQuarter || '';
        this.periodYear = _traStatement.periodYear || '';
        this.documentType = _traStatement.documentType || '';
    }
}
