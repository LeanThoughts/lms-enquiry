export class DocumentationTypeModel {

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
     * @param _documentationType
     */
    constructor(_documentationType: any) {
        // Initialize the object.
        this.code = _documentationType.code || '';
        this.value = _documentationType.value || '';
    }
}
