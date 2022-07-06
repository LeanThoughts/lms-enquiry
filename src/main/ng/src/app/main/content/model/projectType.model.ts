export class ProjectTypeModel {

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
     * @param _projectType
     */
    constructor(_projectType: any) {
        // Initialize the object.
        this.code = _projectType.code || '';
        this.value = _projectType.value || '';
    }

    /**
     * getProjectTypeDescription()
     * @param projectType
     */
    public static getProjectTypeDescription(projectType: string): string {
        switch (projectType) {
          case '0':  return '';
          case '1': return 'TL-PG-Thermal-Coal';
          case '2': return 'TL-PG-Thermal-Lignite';
          case '3': return 'TL-PG-Thermal-Gas';
          case '4': return 'TL-PG-Hydro';
          case '5': return 'TL-PG-Renewable-Solar';
          case '6': return 'TL-PG-Renewable-Wind';
          case '7': return 'TL-PG-Renewable-Biomass';
          case '8': return 'Renewable-Co-Gen.';
          case '9': return 'TL-RY-RailwaySiding';
          case '10': return 'TL-UI-SmartCity';
          case '11': return 'TL-RD-Roads-Toll';
          case '12': return 'TL-RD-Roads-HAM';
          case '13': return 'TL-PT-Ports';
          case '14': return 'TL-OG-Oil&Gas';
          case '15': return 'TL-OT-Others';
          case '16': return 'TL-WI-WaterTreamentPlant';
          case '17': return 'TL-WI-SewarageTreamentPlant';
          case '18': return 'TL-WI-WaterDistribution';
          case '19': return 'TL-AP-Airport';
          case '20': return 'TL-EM-ChargingInfra';
          case '21': return 'TL-EM-EM&ChargingInfra';
          case '22': return 'TL-WM-WasteHandling';
          case '23': return 'TL-WM-WastetoEnergy';
          case '24': return 'TL-M-CoalMining';
          case '25': return 'TL-PT-PowerTransmission';
          case '26': return 'TL-PD-EnergyEfficiency';
          case '27': return 'TL-PD-PowerDistribution';
          case '28': return 'TL-PD-SmartMetering';
          case '29': return 'TL-LG-Logistics';
          case '41': return 'NFB-LOCforCAPEX';
          case '42': return 'NFB-LOCforBG/PBG';
          case '43': return 'NFB-LOCOthers';
          case '51': return 'CL-CP-BridgeLoan-Capex';
          case '52': return 'CL-CP-MezzanineLoan-Capex';
          case '53': return 'CL-CP-MediumTermLoan-Capex';
          case '54': return 'CL-CP-Debentures_Capex';
          case '55': return 'CL-CP-RevolvingFacility-Capex';
          case '56': return 'CL-CP-Others';
          case '57': return 'CL-CG-MezzanineLoan-CashFlowMismatch';
          case '58': return 'CL-CG-MediumTermLoan-CashFlowMismatc';
          case '59': return 'CL-CG-Debentures-CashFlowMismatch';
          case '60': return 'CL-CG-Others';

        }
    }
}
