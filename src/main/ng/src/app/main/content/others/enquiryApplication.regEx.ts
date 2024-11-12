export class EnquiryApplicationRegEx {

    // Does not accept decimal places
    public static tenorYear = /^\d{1,2}?$/;
    // Does not accept decimal places
    public static tenorMonth = /^\d{1,2}?$/;

    // Accepts upto 3 digits and 2 ** mandatory ** decimal places.
    // *** Change {2,2} to {1,2} to accept upto 2 decimal places (99.1 & 99.99 are valid)
    // *** Change {2,2} to {1,3} to accept upto 3 decimal places (99.999)
    public static projectCost = /^\d{1,4}(\.\d{1,2})?$/;
    // ** Check comments on projectCost for other variations in the regEx.
    public static equity = /^\d{1,4}(\.\d{1,4})?$/;
    public static projectDebtAmount = /^\d{1,4}(\.\d{1,2})?$/;
    public static pfsDebtAmount = /^\d{1,4}(\.\d{1,2})?$/;
    public static expectedSubDebt = /^\d{1,4}(\.\d{1,2})?$/;
    public static pfsSubDebtAmount = /^\d{1,4}(\.\d{1,2})?$/;
    public static leadFILoanAmount = /^\d{1,4}(\.\d{1,2})?$/;
    public static borrowerNetWorth = /^\d{1,4}(\.\d{1,2})?$/;
    public static borrowerPAT = /^\d{1,4}(\.\d{1,2})?$/;

    // Accepts upto 4 digits and upto 2 decimal places.
    public static projectCapacity = /^\d{1,4}(\.\d{1,2})?$/;

    // Accepts upto 8 digits and upto 2 decimal places.
    public static expectedInterestRate = /^\d{1,2}(\.\d{1,2})?$/;

    public static pan =   /[A-Za-z]{5}\d{4}[A-Za-z]{1}?$/; // /^[A-Z0-9]*$/;

    public  static email = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;

    public static telephoneNumber = /^[+\d]+$/;

    public static numbersOnly = /^[+\d]+$/;
}
