export class LoanApplicationModel {

    id: string;

    assistanceType: string;
    createdOn: Date;
    createdByUserName:string;
    enquiryNo: EnquiryNumber;
    equity: number;
    expectedInterestRate: number;
    expectedSubDebt: number;
    financingType: string;
    functionalStatus: number;
    loanApplicant: string;
    loanClass: string;
    loanPurpose: number;
    term:number;
    leadFIName: string;
    leadFILoanAmount: number;
    pfsDebtAmount: number;
    pfsSubDebtAmount: number;
    projectName: string;
    projectCapacity: number;
    projectCapacityUnit:string;
    projectCost: number;
    projectDebtAmount: number;
    projectDistrict: string;
    projectLocationState: string;
    projectType: string;
    promoterName: string;
    promoterAreaOfBusinessNature: string;
    promoterKeyDirector: string;
    promoterNetWorthAmount: number;
    promoterPATAmount: number;
    rating: string;
    tenorYear: number;
    tenorMonth: number;
    scheduledCOD: Date;
    userBPNumber: string;
    productCode: string;
    groupCompany: string;
    loanContractId: string;
    busPartnerNumber: string;

    rejectionCategory: string;
    rejectionReason : string;
    rejectionDate : string;

    technicalStatus: string;
    technicalStatusDescription: string;


    contactBranchAddress: string;
    contactDesignation: string;
    contactDepartment: string;
    contactTelePhone: string;
    contactLandLinePhone: string;
    contactEmail : string;
    contactFaxNumber: string;

    industrySector: string;


  _links: Object;

    /**
     * constructor()
     * Initialize the object.
     * @param _loanApplication
     */
    constructor(_loanApplication: any) {
        this.id = _loanApplication.id || '';

        this.assistanceType = _loanApplication.assistanceType || '';
        this.createdOn = _loanApplication.createdOn || '';
        this.createdByUserName = _loanApplication.createdByUserName || '';
        this.enquiryNo = _loanApplication.enquiryNo;
        this.equity = _loanApplication.equity;
        this.expectedInterestRate = _loanApplication.expectedInterestRate;
        this.expectedSubDebt = _loanApplication.expectedSubDebt;
        this.financingType = _loanApplication.financingType || '';
        this.functionalStatus = _loanApplication.functionalStatus || 0;
        this.loanApplicant = _loanApplication.loanApplicant;
        this.loanClass = _loanApplication.loanClass || 0;
        this.loanPurpose = _loanApplication.loanPurpose || '';
        this.leadFIName = _loanApplication.leadFIName;
        this.leadFILoanAmount = _loanApplication.leadFILoanAmount;
        this.pfsDebtAmount = _loanApplication.pfsDebtAmount || 0;
        this.pfsSubDebtAmount = _loanApplication.pfsSubDebtAmount || 0;
        this.projectName = _loanApplication.projectName || '';
        this.projectCapacity = _loanApplication.projectCapacity;
        this.projectCapacityUnit = _loanApplication.projectCapacityUnit || '';
        this.projectCost = _loanApplication.projectCost;
        this.projectDebtAmount = _loanApplication.projectDebtAmount;
        this.projectDistrict = _loanApplication.projectDistrict;
        this.projectLocationState = _loanApplication.projectLocationState || '';
        this.projectType = _loanApplication.projectType || 0;
        this.promoterName = _loanApplication.promoterName || '';
        this.promoterAreaOfBusinessNature = _loanApplication.promoterAreaOfBusinessNature || '';
        this.promoterKeyDirector = _loanApplication.promoterKeyDirector;
        this.promoterNetWorthAmount = _loanApplication.promoterNetWorthAmount;
        this.promoterPATAmount = _loanApplication.promoterPATAmount;
        this.rating = _loanApplication.rating;
        this.tenorYear = _loanApplication.tenorYear || 0;
        this.tenorMonth = _loanApplication.tenorMonth || 0;
        this.scheduledCOD = _loanApplication.scheduledCOD || '';
        this.userBPNumber = _loanApplication.userBPNumber || '';
        this.productCode = _loanApplication.productCode || '';
        this.groupCompany = _loanApplication.groupCompany || '';
        this.loanContractId = _loanApplication.loanContractId || '';
        this.busPartnerNumber = _loanApplication.busPartnerNumber || '';

        this.contactBranchAddress = _loanApplication.contactBranchAddress || '';
        this.contactDesignation = _loanApplication.contactDesignation || '';
        this.contactDepartment = _loanApplication.contactDepartment || '';
        this.contactTelePhone = _loanApplication.contactTelePhone || '';
        this.contactLandLinePhone = _loanApplication.contactLandLinePhone || '';
        this.contactEmail  = _loanApplication.contactEmail || '';
        this.contactFaxNumber = _loanApplication.contactFaxNumber || '';

       this.industrySector = _loanApplication.industrySector || '';

        this._links = _loanApplication._links;
    }

    /**
     * loanClassDescription()
     * Returns the string value of the loan class.
     */
    get loanClassDescription(): string {
        switch (this.loanClass) {
            case '0' : return '';
            case '001': return 'Power';
            case '002': return 'Railways';
            case '003': return 'Urban Infrastructure';
            case '004': return 'Roads';
            case '005': return 'Ports';
            case '006': return 'Oil & Gas';
            case '007': return 'Corporates';
            case '008': return 'Infrastructure';
            case '009': return 'Others';
        }
    }

    /**
     * projectTypeDescription()
     * Returns the string value of the project type.
     */
    get projectTypeDescription(): string {
        switch (this.projectType) {
            case  '0': return '';
            case '01': return 'Thermal - Coal';
            case '02': return 'Thermal - Ignite';
            case '03': return 'Thermal - Gas';
            case '04': return 'Hydro';
            case '05': return 'Renewable - Solar';
            case '06': return 'Renewable - Wind';
            case '07': return 'Renewable - Biomass';
            case '08': return 'Renewable - Small Hydro';
            case '09': return 'EPC Contractors';
            case '10': return 'Coal Mining';
            case '11': return 'Power Transmission';
            case '12': return 'Railway Siding';
            case '13': return 'Ports';
            case '14': return 'Corporate';
            case '15': return 'Renovation & Modernisation';
            case '16': return 'Others';
        }
    }

    /**
     * assistanceTypeDescription()
     * Returns the string value of assistance type.
     */
    get assistanceTypeDescription(): string {
        switch (this.assistanceType) {
            case 'E': return 'Equity';
            case 'D': return 'Debt';
        }
    }

    /**
     * functionalStatusDescription()
     * Returns the string value of the functional status.
     */
    get functionalStatusDescription(): string {
        switch (this.functionalStatus) {
            case 0: return '';
            case 1: return 'Enquiry Stage';
            case 2: return 'ICC ApprovalStage';
            case 3: return 'Apprisal Stage';
            case 4: return 'Board Approval Stage';
            case 5: return 'Loan Documenation Stage';
            case 6: return 'Loan Disbursement Stage';
            case 7: return 'Approved';
            case 8: return 'Rejected';
            case 9: return 'Cancelled';
        }
    }

    /*
        Industry Sector Codes
     */
    // get industrySectorDescription(): string{
    //   switch (this.industrySector) {
    //     case '0' : return ' ';
    //     case '1' : return 'Power';
    //     case '2' : return 'Railways';
    //     case '3' : return 'Urban Infra';
    //     case '4' : return 'Roads';
    //     case '5' : return 'Ports';
    //     case '6' : return 'Oil & Gas';
    //     case '7' : return 'Corporates';
    //     case '8' : return 'Infrastructure';
    //     case '9' : return 'Others';
    //     case '10' : return 'Energy Supply / Distribution';
    //     case '11' : return 'Div. Holding comp';
    //     case '12' : return 'Raw Materials';
    //     case '13' : return 'Precious Metals';
    //     case '14' : return 'Financial Services';
    //     case '15' : return 'Real Estate';
    //     case '21' : return 'Chemical Industry';
    //     case '22' : return 'Health';
    //     case '23' : return 'Glass';
    //     case '24' : return 'Construction Industry';
    //     case '25' : return 'Building Supplier';
    //     case '26' : return 'Paper and Pulp';
    //     case '27' : return 'Timber and Infrastructure';
    //     case '31' : return 'Spinning Mill, Weaving Mill and Textile Refinement';
    //     case '32' : return 'Apparel';
    //     case '41' : return 'Iron and Steel';
    //     case '42' : return 'Vehicles';
    //     case '43' : return 'Vehicle Supplier';
    //     case '44' : return 'Mechanical Engineering';
    //     case '45' : return 'Specialized Mechanical Engineering';
    //     case '46' : return 'Machine Tool Engineering';
    //     case '47' : return 'Aircraft Construction';
    //     case '51' : return 'Breweries, Beverages, Tobacco';
    //     case '52' : return 'Nutrition';
    //     case '61' : return 'Electricals / Electrical Engineering';
    //     case '62' : return 'Computers and Data Processing';
    //     case '63' : return 'Software';
    //     case '64' : return 'Telecommunications';
    //     case '71' : return 'Consumer Products';
    //     case '72' : return 'Traffic and Transport';
    //     case '73' : return 'Leisure and Hotel';
    //     case '81' : return 'Commercial Banks';
    //     case '82' : return 'Mortgage Banks';
    //     case '83' : return 'Life Insurances';
    //     case '84' : return 'Non-Life Insurances';
    //     case '85' : return 'Reinsurances';
    //     case '86' : return 'Insurance Holdings';
    //     case '91' : return 'Trading';
    //     case '92' : return 'Pharmaceutical Trade';
    //     case '93' : return 'Publishing and Media';
    //     case '94' : return 'Environment ';
    //
    //   }
    // }

}

class EnquiryNumber {
    id: number;
}
