export class TRAModel
{
    id: string;
    bankKey: string;
    traBankName: string;
    branch: string;
    address: string;
    beneficiaryName: string;
    ifscCode: string;
    accountNumber: string;
    contactName: string;
    contactNumber: string;
    typeOfAccount: string; 
    email: string;
    pfsAuthorisedPersonBPCode: string;
    pfsAuthorisedPerson: string;

    /**
     * constructor();
     * @param _tra 
     */
    constructor(_tra: any)
    {
        this.id = _tra.id || '';
        this.bankKey = _tra.bankKey || '';
        this.traBankName = _tra.traBankName || '';
        this.branch = _tra.branch || '';
        this.address = _tra.address || '';
        this.beneficiaryName = _tra.beneficiaryName || '';
        this.ifscCode = _tra.iFSCCode || '';
        this.accountNumber = _tra.accountNumber || '';
        this.contactName = _tra.contactName || '';
        this.contactNumber = _tra.contactNumber || '';
        this.typeOfAccount = _tra.typeOfAccount || 'TRA Account';
        this.email = _tra.email || '';
        this.pfsAuthorisedPersonBPCode = _tra.pfsAuthorisedPersonBPCode || '';
        this.pfsAuthorisedPerson = _tra.pfsAuthorisedPerson || '';
    }
}
