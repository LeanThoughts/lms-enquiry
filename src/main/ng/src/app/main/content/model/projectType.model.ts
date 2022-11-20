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
          case '1':  return 'Thermal-Coal.';
          case '2':  return 'Thermal-Lignite';
          case '3':  return 'Thermal-Gas';
          case '4':  return 'Renewable-Hydro';
          case '5':  return 'Renewable-Solar';
          case '6':  return 'Renewable-Wind';
          case '7':  return 'Renewable-Biomass';
          case '8':  return 'Renewable-Co-Gen.';
          case '9':  return 'Railway Siding';
          case '10':  return 'Railway Wagons/Coach';
          case '11':  return 'Railway Terminals';
          case '12':  return 'Smart City';
          case '13':  return 'Roads-Toll';
          case '14':  return 'Roads-HAM';
          case '15':  return 'Roads & Bridges';
          case '16':  return 'Ports';
          case '17':  return 'Oil & Gas';
          case '18':  return 'Gas Exploration';
          case '19':  return 'Oil Rigs';
          case '20':  return 'Water Treatment Plant';
          case '21':  return 'Sewerage Treatment Plant';
          case '22':  return 'Water Distribution';
          case '23':  return 'Irrigation-DAM/ Channels';
          case '24':  return 'Storm Water Drainage System';
          case '25':  return 'Airport';
          case '26':  return 'EM - Charging Infra';
          case '27':  return 'Waste Handling';
          case '28':  return 'Waste to Energy';
          case '29':  return 'Coal Mining';
          case '30':  return 'Coal Washery';
          case '31':  return 'Large Country Pipelines';
          case '32':  return 'Power Transmission';
          case '33':  return 'Solar Park/Pooling Substation';
          case '34':  return 'Power Distribution';
          case '35':  return 'Solar Power Pumps/Electric Pumps';
          case '36':  return 'Logistics Park/ Terminal';
          case '37':  return 'Warehouse / Cold Chain Facility';
          case '38':  return 'Shipyard';
          case '39':  return 'City Gas Distribution';
          case '40':  return 'Hotels & Resorts';
          case '41':  return 'Real Estate';
          case '42':  return 'SEZ-Industrial Park';
          case '43':  return 'Telecom Towers';
          case '44':  return 'Telecom Networks';
          case '45':  return 'Telecommunication Services';
          case '46':  return 'Inland Waterways';
          case '47':  return 'Public Transport';
          case '48':  return 'Hospitals';
          case '49':  return 'Educational Institutes';
          case '50':  return 'Sports Infrastructure';
          case '51':  return 'Energy Efficiency';
          case '52':  return 'Smart Metering';
          case '53':  return 'Others';

        }
    }
}
