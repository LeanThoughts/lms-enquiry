export class OperatingParameterPLFModel {

    id: string;
    serialNumber: number;
    year: number;
    remarks: string;
    actualYearlyAveragePlfCuf: string;
    miUnGenerated: number;
    designPlfCuf: string;


    /**
     * constructor()
     * @param _operatingParameter 
     */
    constructor(_operatingParameter: any)
    {
        this.id = _operatingParameter.id || '';
        this.serialNumber = _operatingParameter.serialNumber || 0;
        this.year = _operatingParameter.year || 0;
        this.remarks = _operatingParameter.remarks || '';
        this.actualYearlyAveragePlfCuf = _operatingParameter.actualYearlyAveragePlfCuf || '';
        this.miUnGenerated = _operatingParameter.miUnGenerated || 0;
        console.log(this.miUnGenerated);
        this.designPlfCuf = _operatingParameter.designPlfCuf || '';
    }
}
