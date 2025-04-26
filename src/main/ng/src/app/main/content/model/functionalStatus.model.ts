export class FunctionalStatusModel {

    /**
     * getFunctionalStatus()
     */
    public static getFunctionalStatuses(): any {
        return [
            {
                id: 0,
                name: 'Enquiry Stage',
                code: 1
            },
            {
                id: 1,
                name: 'ICC In-Principle Approved',
                code: 2
            }            
        ];
    }
}
