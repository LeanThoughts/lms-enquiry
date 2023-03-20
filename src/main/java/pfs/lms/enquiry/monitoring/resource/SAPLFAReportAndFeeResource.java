package pfs.lms.enquiry.monitoring.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.monitoring.lfa.LFAReportAndFee;
 import pfs.lms.enquiry.utils.DataConversionUtility;

import java.io.Serializable;
import java.text.ParseException;

@Component
@JsonInclude (JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties (ignoreUnknown = true)
public class SAPLFAReportAndFeeResource implements Serializable   {


    public SAPLFAReportAndFeeResource() {
        saplfaReportAndFeeResourceDetails = new SAPLFAReportAndFeeResourceDetails();
     }

    @JsonProperty(value = "d")
    private SAPLFAReportAndFeeResourceDetails saplfaReportAndFeeResourceDetails;


    public SAPLFAReportAndFeeResourceDetails getSaplfaReportAndFeeResourceDetails() {
        return saplfaReportAndFeeResourceDetails;
    }

    public void setSaplfaReportAndFeeResourceDetails(SAPLFAReportAndFeeResourceDetails saplfaReportAndFeeResourceDetails) {
        this.saplfaReportAndFeeResourceDetails = saplfaReportAndFeeResourceDetails;
    }

    public SAPLFAReportAndFeeResourceDetails
                    mapToSAP(LFAReportAndFee lfaReportAndFee,
                             User lastProcessedBy) throws ParseException {

        DataConversionUtility dataConversionUtility =  new DataConversionUtility();


       SAPLFAReportAndFeeResourceDetails detailsResource= new SAPLFAReportAndFeeResourceDetails();

        detailsResource.setId(lfaReportAndFee.getId());
        detailsResource.setSerialNo(lfaReportAndFee.getSerialNumber());
        detailsResource.setReporttype(lfaReportAndFee.getReportType());
        detailsResource.setLieId(lfaReportAndFee.getLendersFinancialAdvisor().getId());

        if (lfaReportAndFee.getDateOfReceipt() != null)
            detailsResource.setDateofreceipt(dataConversionUtility.convertDateToSAPFormat(lfaReportAndFee.getDateOfReceipt()));
        else
            detailsResource.setDateofreceipt(null);

        if (lfaReportAndFee.getInvoiceDate() != null)
            detailsResource.setInvoicedate(dataConversionUtility.convertDateToSAPFormat(lfaReportAndFee.getInvoiceDate()));
        else
            detailsResource.setInvoicedate(null);

        detailsResource.setInvoiceno(lfaReportAndFee.getInvoiceNo());
        detailsResource.setFeeamount(lfaReportAndFee.getFeeAmount().toString());
        detailsResource.setStatusoffeepaid(lfaReportAndFee.getStatusOfFeePaid());

        if  (lfaReportAndFee.getReportDate() != null)
            detailsResource.setReportdate(dataConversionUtility.convertDateToSAPFormat(lfaReportAndFee.getReportDate()));
        else
            detailsResource.setReportdate(null);
        detailsResource.setRemarks(lfaReportAndFee.getRemarks());
       if ( lfaReportAndFee.getPercentageCompletion() != null)
        detailsResource.setRemarks(lfaReportAndFee.getPercentageCompletion().toString());

 //                Sapfiinvoicedate
//                Sapfiinvoicenumber
//                Feeamountraisedoncustomer
//                Statusoffeereceipt


        if (lfaReportAndFee.getNextReportDate() != null)
            detailsResource.setNextreportdate(dataConversionUtility.convertDateToSAPFormat(lfaReportAndFee.getNextReportDate()));
        else
            detailsResource.setNextreportdate(null);


        detailsResource.setDocumenttitle(lfaReportAndFee.getDocumentTitle());



        return detailsResource;
    }
}
