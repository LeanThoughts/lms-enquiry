export class EnquiryApplicationModel {

    id: string;

    assistanceType: string;
    busPartnerNumber: string;
    createdOn: Date;
    createdByUserName:string;
    enquiryNumber: string;
    functionalStatus: number;
    loanAmount: number;
    loanClass: string;
    loanContractId: string;
    projectCapacity: number;
    projectCapacityUnit: string;
    projectCost: number;
    projectLocationState: string;
    projectName: string;
    projectType: string;
    technicalStatusDescription:string;
    rating: string;
    borrowerName: string;

    /**
     * constructor()
     * Initialize the object.
     * @param _enquiryApplication
     */
    constructor(_enquiryApplication: any) {
        this.id = _enquiryApplication.loanApplication.id || '';

        this.assistanceType = _enquiryApplication.loanApplication.assistanceType;
        this.busPartnerNumber = _enquiryApplication.loanApplication.busPartnerNumber;
        this.createdOn = _enquiryApplication.loanApplication.createdOn;
        this.createdByUserName = _enquiryApplication.loanApplication.createdByUserName;
        this.enquiryNumber = _enquiryApplication.loanApplication.enquiryNo.id;
        this.functionalStatus = _enquiryApplication.loanApplication.functionalStatus;
        this.loanAmount = _enquiryApplication.loanApplication.pfsDebtAmount;
        this.loanClass = _enquiryApplication.loanApplication.loanClass;
        this.loanContractId = _enquiryApplication.loanApplication.loanContractId;
        this.projectCapacity = _enquiryApplication.loanApplication.projectCapacity;
        this.projectCapacityUnit = _enquiryApplication.loanApplication.projectCapacityUnit;
        this.projectCost = _enquiryApplication.loanApplication.projectCost;
        this.projectLocationState = _enquiryApplication.loanApplication.projectLocationState;
        this.projectName = _enquiryApplication.loanApplication.projectName;
        this.projectType = _enquiryApplication.loanApplication.projectType;
        this.technicalStatusDescription = _enquiryApplication.loanApplication.technicalStatusDescription;
        this.rating = _enquiryApplication.loanApplication.rating;
        this.borrowerName = _enquiryApplication.partner.partyName1;
    }

    /**
     * loanClassDescription()
     * Returns the string value of the loan class.
     */
    get loanClassDescription(): string {
        switch (this.loanClass) {
            case '0' : return '';
          case '1' :  return 'Power Generation-Conventional';
          case '2' :  return 'Power Generation-Renewable';
          case '3' :  return 'Railways';
          case '4' :  return 'TL-Roads';
          case '5' :  return 'Roads';
          case '6' :  return 'Ports';
          case '7' :  return 'Oil & Gas';
          case '8' :  return 'TL-Water Infra';
          case '9' :  return 'Airport';
          case '10' :  return 'Electric Mobility';
          case '11' :  return 'Waste Management';
          case '12' :  return 'Mining';
          case '13' :  return 'Power Transmission';
          case '14' :  return 'Power Distribution';
          case '15' :  return 'TL-Logistics';
          case '16' :  return 'Shipyard';
          case '17' :  return 'City Gas Distribution';
          case '18' :  return 'Hotels & Resorts';
          case '19' :  return 'Real Estate';
          case '20' :  return 'Telecom';
          case '21' :  return 'Inland Waterways';
          case '22' :  return 'Public Transport';
          case '23' :  return 'Hospitals';
          case '24' :  return 'Educational Institutes';
          case '25' :  return 'Sports Infra';
          case '26' :  return 'Energy Efficiency';
          case '41' :  return 'NFB-LOC for CAPEX';
          case '42' :  return 'NFB-LOC for BG / PBG';
          case '43' :  return 'NFB-LOC Others';
          case '51' :  return 'CL-Corp. Private';
          case '52' :  return 'CL-Corp. Govt';

        }
    }

    /**
     * projectTypeDescription()
     * Returns the string value of the project type.
     */
    get projectTypeDescription(): string {
        switch (this.projectType) {
            case  '0': return '';
          case '1' :  return 'TL-PG-Thermal-Coal';
          case '2' :  return 'TL-PG-Thermal-Lignite';
          case '3' :  return 'TL-PG-Thermal-Gas';
          case '4' :  return 'TL-PG-Hydro';
          case '5' :  return 'TL-PG-Renewable-Solar';
          case '6' :  return 'TL-PG-Renewable-Wind';
          case '7' :  return 'TL-PG-Renewable-Biomass';
          case '8' :  return 'Renewable-Co-Gen.';
          case '9' :  return 'TL-RY-Railway Siding';
          case '10' :  return 'TL-UI-Smart City';
          case '11' :  return 'TL-RD-Roads-Toll';
          case '12' :  return 'TL-RD-Roads-HAM';
          case '13' :  return 'TL-PT-Ports';
          case '14' :  return 'TL-OG-Oil & Gas';
          case '15' :  return 'TL-OT-Others';
          case '16' :  return 'TL-WI - Water Treament Plant';
          case '17' :  return 'TL-WI - Sewarage Treament Plant';
          case '18' :  return 'TL-WI - Water Distribution';
          case '19' :  return 'TL-AP-Airport';
          case '20' :  return 'TL-EM-Charging Infra';
          case '21' :  return 'TL-EM-EM & Charging Infra';
          case '22' :  return 'TL-WM-Waste Handling';
          case '23' :  return 'TL-WM-Waste to Energy';
          case '24' :  return 'TL-M-Coal Mining';
          case '25' :  return 'TL-PT-Power Transmission';
          case '26' :  return 'TL-PD-Energy Efficiency';
          case '27' :  return 'TL-PD-Power Distribution';
          case '28' :  return 'TL-PD-Smart Metering';
          case '29' :  return 'TL-LG-Logistics';
          case '41' :  return 'NFB-LOC for CAPEX';
          case '42' :  return 'NFB-LOC for BG / PBG';
          case '43' :  return 'NFB-LOC Others';
          case '51' :  return 'CL-CP-Bridge Loan-Capex';
          case '52' :  return 'CL-CP-Mezzanine Loan-Capex';
          case '53' :  return 'CL-CP-Medium Term Loan-Capex';
          case '54' :  return 'CL-CP-Debentures_Capex';
          case '55' :  return 'CL-CP-Revolving Facility-Capex';
          case '56' :  return 'CL-CP-Others';
          case '57' :  return 'CL-CG-Mezzanine Loan-Cash Flow Mismatch';
          case '58' :  return 'CL-CG-Medium Term Loan-Cash Flow Mismatc';
          case '59' :  return 'CL-CG-Debentures-Cash Flow Mismatch';
          case '60' :  return 'CL-CG-Others';

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
}
