export class LoanClassModel {

    /**
     * code
     */
    code: string;

    /**
     * value
     */
    value: string;

    /**
     * constructor()
     * @param _loanClass
     */
    constructor(_loanClass: any) {
        // Initialize the object.
        this.code = _loanClass.code || '';
        this.value = _loanClass.value || '';
    }

    /**
     * getLoanClassDescription()
     * @param loanClass
     */
    public static getLoanClassDescription(loanClass: string): string {
        switch (loanClass) {
          case '0' : return '';
          case '1': return 'PowerGen-Convl';
          case '2': return 'PowerGen-Renew';
          case '3': return 'Railways';
          case '4': return 'Roads';
          case '5': return 'Roads';
          case '6': return 'Ports';
          case '7': return 'Oil&Gas';
          case '8': return 'Infrastructure';
          case '9': return 'Airport';
          case '10': return 'ElectrcMobility';
          case '11': return 'WasteManagement';
          case '12': return 'Mining';
          case '13': return 'PowerTransmn';
          case '14': return 'PowerDistribn';
          case '15': return 'TL-Logistics';
          case '16': return 'Shipyard';
          case '17': return 'CityGasDistribn';
          case '18': return 'Hotels&Resorts';
          case '19': return 'RealEstate';
          case '20': return 'Telecom';
          case '21': return 'InlandWaterways';
          case '22': return 'PublicTransport';
          case '23': return 'Hospitals';
          case '24': return 'EducationalInst';
          case '25': return 'SportsInfra';
          case '26': return 'EnrgyEfficiency';
          case '41': return 'NFB-LOCforCAP';
          case '42': return 'NFB-LOCforBG';
          case '43': return 'NFB-LOCOthers';
          case '51': return 'CL-Corp.Privat';
          case '52': return 'CL-Corp.Govt';

        }
    }
}
