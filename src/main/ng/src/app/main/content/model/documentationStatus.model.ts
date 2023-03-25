export class DocumentationStatusModel {

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
     * @param _documentationStatus
     */
    constructor(_documentationStatus: any) {
        // Initialize the object.
        this.code = _documentationStatus.code || '';
        this.value = _documentationStatus.value || '';
    }
}
