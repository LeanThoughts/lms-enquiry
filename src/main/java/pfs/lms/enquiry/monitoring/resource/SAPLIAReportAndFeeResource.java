package pfs.lms.enquiry.monitoring.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.monitoring.lia.LIAReportAndFee;
import pfs.lms.enquiry.utils.DataConversionUtility;

import java.io.Serializable;
import java.text.ParseException;

@Component
@JsonInclude (JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties (ignoreUnknown = true)
public class SAPLIAReportAndFeeResource implements Serializable   {


    public SAPLIAReportAndFeeResource() {
        sapliaReportAndFeeResourceDetails = new SAPLIAReportAndFeeResourceDetails();
     }

    @JsonProperty(value = "d")
    private SAPLIAReportAndFeeResourceDetails sapliaReportAndFeeResourceDetails;


    public SAPLIAReportAndFeeResourceDetails getSapliaReportAndFeeResourceDetails() {
        return sapliaReportAndFeeResourceDetails;
    }

    public void setSapliaReportAndFeeResourceDetails(SAPLIAReportAndFeeResourceDetails sapliaReportAndFeeResourceDetails) {
        this.sapliaReportAndFeeResourceDetails = sapliaReportAndFeeResourceDetails;
    }

    public SAPLIAReportAndFeeResourceDetails
                    mapToSAP(LIAReportAndFee liaReportAndFee,
                             User lastProcessedBy) throws ParseException {

        DataConversionUtility dataConversionUtility =  new DataConversionUtility();


       SAPLIAReportAndFeeResourceDetails detailsResource= new SAPLIAReportAndFeeResourceDetails();

        detailsResource.setLiaRptFeeId(liaReportAndFee.getId());
        detailsResource.setSerialNo(liaReportAndFee.getSerialNumber());
        detailsResource.setReporttype(liaReportAndFee.getReportType());
        detailsResource.setLiaId(liaReportAndFee.getLendersInsuranceAdvisor().getId());

        if (liaReportAndFee.getDateOfReceipt() != null)
            detailsResource.setDateofreceipt(dataConversionUtility.convertDateToSAPFormat(liaReportAndFee.getDateOfReceipt()));
        else
            detailsResource.setDateofreceipt(null);

        if (liaReportAndFee.getInvoiceDate() != null)
            detailsResource.setInvoicedate(dataConversionUtility.convertDateToSAPFormat(liaReportAndFee.getInvoiceDate()));
        else
            detailsResource.setInvoicedate(null);

        detailsResource.setInvoiceno(liaReportAndFee.getInvoiceNo());
        detailsResource.setFeeamount(liaReportAndFee.getFeeAmount().toString());
        detailsResource.setStatusoffeepaid(liaReportAndFee.getStatusOfFeePaid());

        if  (liaReportAndFee.getReportDate() != null)
            detailsResource.setReportdate(dataConversionUtility.convertDateToSAPFormat(liaReportAndFee.getReportDate()));
        else
            detailsResource.setReportdate(null);
        detailsResource.setRemarks(liaReportAndFee.getRemarks());
        if (liaReportAndFee.getPercentageCompletion() != null)
        detailsResource.setRemarks(liaReportAndFee.getPercentageCompletion().toString());

 //                Sapfiinvoicedate
//                Sapfiinvoicenumber
//                Feeamountraisedoncustomer
//                Statusoffeereceipt


        if (liaReportAndFee.getNextReportDate() != null)
            detailsResource.setNextreportdate(dataConversionUtility.convertDateToSAPFormat(liaReportAndFee.getNextReportDate()));
        else
            detailsResource.setNextreportdate(null);


        detailsResource.setDocumenttitle(liaReportAndFee.getDocumentTitle());



        return detailsResource;
    }
}
