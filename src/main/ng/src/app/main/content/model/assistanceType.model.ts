export class AssistanceTypeModel {

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
     * @param _assistanceType
     */
    constructor(_assistanceType: any) {
        // Initialize the object.
        this.code = _assistanceType.code || '';
        this.value = _assistanceType.value || '';
    }

    /**
     * getAssistanceTypeDescription()
     * @param assistanceType
     */
    public static getAssistanceTypeDescription(assistanceType: string): string {
        switch (assistanceType) {
            case 'E': return 'Equity';
            case 'D': return 'Debt Options';
            case 'N': return 'NCD';
            case 'C': return 'CCD';

        }
    }
}
