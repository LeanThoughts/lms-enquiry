export class MonitoringRegEx {

    public static genericAmount = /^\d{1,20}(\.\d{1,2})?$/;
    public static digitsOnly = /^\d{1,4}$/;
    public static threeDigitsOnly = /^\d{1,3}$/;
    public static holdingPercentage = /^\d{1,3}(\.\d{1,2})?$/;
    public static applicableTariff = /^\d{1,5}(\.\d{1,2})?$/;
    public static environmentParameters = /^\d{1,10}(\.\d{1,2})?$/;
    public static twoCommaTwo = /^\d{1,2}(\.\d{1,2})?$/;
    public static threeCommaTwo = /^\d{1,3}(\.\d{1,2})?$/;
    public static tenCommaTwo = /^\d{1,10}(\.\d{1,2})?$/;
    public static twelveCommaTwo = /^\d{1,15}(\.\d{1,2})?$/;
    public static fifteenCommaTwo = /^\d{1,18}(\.\d{1,2})?$/;
    public static sixteenCommaTwo = /^\d{1,16}(\.\d{1,2})?$/;
    public static thirteenCommaTwo = /^\d{1,13}(\.\d{1,2})?$/;
    public static sevenCommaTwo = /^\d{1,7}(\.\d{1,2})?$/;
} 
