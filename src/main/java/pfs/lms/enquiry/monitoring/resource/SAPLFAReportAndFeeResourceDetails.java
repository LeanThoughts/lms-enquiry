package pfs.lms.enquiry.monitoring.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by gguptha on 09/11/18.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class SAPLFAReportAndFeeResourceDetails {



    @JsonProperty (value = "Id")
    private String id;

    @JsonProperty (value = "LfaId")
    private String lieId;

    @JsonProperty (value = "SerialNo")
    private Integer serialNo;

    @JsonProperty (value = "Reporttype")
    private String reporttype;

    @JsonProperty (value = "DateOfReceipt")
    private String dateofreceipt;

    @JsonProperty (value = "Invoicedate")
    private String invoicedate;

    @JsonProperty (value = "Invoiceno")
    private String invoiceno;

    @JsonProperty (value = "Feeamount")
    private String feeamount;

    @JsonProperty (value = "Statusoffeepaid")
    private String statusoffeepaid;

    @JsonProperty (value = "Sapfiinvoicedate")
    private String sapfiinvoicedate;

    @JsonProperty (value = "Sapfiinvoicenumber")
    private String sapfiinvoicenumber;

    @JsonProperty (value = "Feeamountraisedoncustomer")
    private String feeamountraisedoncustomer;

    @JsonProperty (value = "Statusoffeereceipt")
    private String statusoffeereceipt;

    @JsonProperty (value = "Documenttitle")
    private String documenttitle;
//    @JsonProperty (value = "Documentcontent")
//    private String documentcontent;
    @JsonProperty (value = "Nextreportdate")
    private String nextreportdate;

    @Override
    public String toString() {
        return "SAPLFAReportAndFeeResourceDetails{" +
                "id='" + id + '\'' +
                ", lieId='" + lieId + '\'' +
                ", serialNo=" + serialNo +
                ", reporttype='" + reporttype + '\'' +
                ", dateofreceipt='" + dateofreceipt + '\'' +
                ", invoicedate='" + invoicedate + '\'' +
                ", invoiceno='" + invoiceno + '\'' +
                ", feeamount='" + feeamount + '\'' +
                ", statusoffeepaid='" + statusoffeepaid + '\'' +
                ", sapfiinvoicedate='" + sapfiinvoicedate + '\'' +
                ", sapfiinvoicenumber='" + sapfiinvoicenumber + '\'' +
                ", feeamountraisedoncustomer='" + feeamountraisedoncustomer + '\'' +
                ", statusoffeereceipt='" + statusoffeereceipt + '\'' +
                ", documenttitle='" + documenttitle + '\'' +
                ", nextreportdate='" + nextreportdate + '\'' +
                '}';
    }
}
