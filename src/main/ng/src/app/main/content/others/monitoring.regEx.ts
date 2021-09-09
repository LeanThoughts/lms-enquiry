export class MonitoringRegEx {

    public static genericAmount = /^\d{1,20}(\.\d{1,2})?$/;
    public static digitsOnly = /^\d{1,4}$/;
    public static holdingPercentage = /^\d{1,3}(\.\d{1,2})?$/;
    public static applicableTariff = /^\d{1,5}(\.\d{1,2})?$/;
    public static environmentParameters = /^\d{1,10}(\.\d{1,2})?$/;
} 
