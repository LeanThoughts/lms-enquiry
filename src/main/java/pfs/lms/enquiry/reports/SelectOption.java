package pfs.lms.enquiry.reports;

/**
 * One line of an ABAP SELECT-OPTIONS table (a range table): SIGN (I include / E exclude), OPTION (EQ, NE, GT, GE, LT,
 * LE, BT, NB, CP, NP), LOW and HIGH. The report selection screens work like SAP's: each selection parameter holds any
 * number of such lines, entered as from/to or through the multiple-selection dialog.
 */
public class SelectOption {

    private String sign;
    private String option;
    private String low;
    private String high;

    public SelectOption() {
    }

    public SelectOption(String sign, String option, String low, String high) {
        this.sign = sign;
        this.option = option;
        this.low = low;
        this.high = high;
    }

    public String getSign() { return sign; }
    public void setSign(String sign) { this.sign = sign; }
    public String getOption() { return option; }
    public void setOption(String option) { this.option = option; }
    public String getLow() { return low; }
    public void setLow(String low) { this.low = low; }
    public String getHigh() { return high; }
    public void setHigh(String high) { this.high = high; }
}
