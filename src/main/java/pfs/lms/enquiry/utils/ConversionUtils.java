package pfs.lms.enquiry.utils;


public  class ConversionUtils {

    public static class PeriodUnitConversion {
        public String getPeriodUnit(String unit) {
            String unitDescription = "";
            switch (unit) {
                case "1":
                    unitDescription = "Days";
                    break;
                case "2":
                    unitDescription = "Weeks";
                    break;
                case "3":
                    unitDescription = "Months";
                    break;
                case "4":
                    unitDescription = "Years";
                    break;
            }
            return unitDescription;
        }
    }
}
