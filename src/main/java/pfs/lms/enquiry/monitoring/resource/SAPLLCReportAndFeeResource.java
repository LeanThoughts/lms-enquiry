package pfs.lms.enquiry.monitoring.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.monitoring.llc.LLCReportAndFee;
import pfs.lms.enquiry.utils.DataConversionUtility;

import java.io.Serializable;
import java.text.ParseException;

@Component
@JsonInclude (JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties (ignoreUnknown = true)
public class SAPLLCReportAndFeeResource implements Serializable   {


    public SAPLLCReportAndFeeResource() {
        sapllcReportAndFeeResourceDetails = new SAPLLCReportAndFeeResourceDetails();
     }

    @JsonProperty(value = "d")
    private SAPLLCReportAndFeeResourceDetails sapllcReportAndFeeResourceDetails;


    public SAPLLCReportAndFeeResourceDetails getSapllcReportAndFeeResourceDetails() {
        return sapllcReportAndFeeResourceDetails;
    }

    public void setSapllcReportAndFeeResourceDetails(SAPLLCReportAndFeeResourceDetails sapllcReportAndFeeResourceDetails) {
        this.sapllcReportAndFeeResourceDetails = sapllcReportAndFeeResourceDetails;
    }

    public SAPLLCReportAndFeeResourceDetails
                    mapToSAP(LLCReportAndFee llcReportAndFee,
                             User lastProcessedBy) throws ParseException {

        DataConversionUtility dataConversionUtility =  new DataConversionUtility();


       SAPLLCReportAndFeeResourceDetails detailsResource= new SAPLLCReportAndFeeResourceDetails();

        detailsResource.setId(llcReportAndFee.getId());
        detailsResource.setSerialNo(llcReportAndFee.getSerialNumber());
        detailsResource.setReporttype(llcReportAndFee.getReportType());
        detailsResource.setLlcId(llcReportAndFee.getLendersLegalCouncil().getId());

        if (llcReportAndFee.getDateOfReceipt() != null)
            detailsResource.setDateofreceipt(dataConversionUtility.convertDateToSAPFormat(llcReportAndFee.getDateOfReceipt()));
        else
            detailsResource.setDateofreceipt(null);

        if (llcReportAndFee.getInvoiceDate() != null)
            detailsResource.setInvoicedate(dataConversionUtility.convertDateToSAPFormat(llcReportAndFee.getInvoiceDate()));
        else
            detailsResource.setInvoicedate(null);

        detailsResource.setInvoiceno(llcReportAndFee.getInvoiceNo());
        detailsResource.setFeeamount(llcReportAndFee.getFeeAmount().toString());
        detailsResource.setStatusoffeepaid(llcReportAndFee.getStatusOfFeePaid());

        if  (llcReportAndFee.getReportDate() != null)
            detailsResource.setReportdate(dataConversionUtility.convertDateToSAPFormat(llcReportAndFee.getReportDate()));
        else
            detailsResource.setReportdate(null);
        detailsResource.setRemarks(llcReportAndFee.getRemarks());
        detailsResource.setRemarks(llcReportAndFee.getPercentageCompletion().toString());

 //                Sapfiinvoicedate
//                Sapfiinvoicenumber
//                Feeamountraisedoncustomer
//                Statusoffeereceipt


        if (llcReportAndFee.getNextReportDate() != null)
            detailsResource.setNextreportdate(dataConversionUtility.convertDateToSAPFormat(llcReportAndFee.getNextReportDate()));
        else
            detailsResource.setNextreportdate(null);


        detailsResource.setDocumenttitle(llcReportAndFee.getDocumentTitle());



        return detailsResource;
    }
}
