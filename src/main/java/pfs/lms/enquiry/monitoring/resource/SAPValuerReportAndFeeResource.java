package pfs.lms.enquiry.monitoring.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.monitoring.lie.LIEReportAndFee;
import pfs.lms.enquiry.monitoring.valuer.Valuer;
import pfs.lms.enquiry.monitoring.valuer.ValuerReportAndFee;
import pfs.lms.enquiry.utils.DataConversionUtility;

import java.io.Serializable;
import java.text.ParseException;

@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SAPValuerReportAndFeeResource implements Serializable {


    public SAPValuerReportAndFeeResource() {
        sapValuerReportAndFeeResourceDetails = new SAPValuerReportAndFeeResourceDetails();
    }

    @JsonProperty(value = "d")
    private SAPValuerReportAndFeeResourceDetails sapValuerReportAndFeeResourceDetails;


    public SAPValuerReportAndFeeResourceDetails getSapValuerReportAndFeeResourceDetails() {
        return sapValuerReportAndFeeResourceDetails;
    }

    public void setSapValuerReportAndFeeResourceDetails(SAPValuerReportAndFeeResourceDetails sapValuerReportAndFeeResourceDetails) {
        this.sapValuerReportAndFeeResourceDetails = sapValuerReportAndFeeResourceDetails;
    }

    public SAPValuerReportAndFeeResourceDetails mapToSAP(ValuerReportAndFee valuerReportAndFee,
                                                         User lastProcessedBy) throws ParseException {

        DataConversionUtility dataConversionUtility = new DataConversionUtility();


        SAPValuerReportAndFeeResourceDetails detailsResource = new SAPValuerReportAndFeeResourceDetails();

        detailsResource.setId(valuerReportAndFee.getId());
        detailsResource.setSerialNo(valuerReportAndFee.getSerialNumber());
        detailsResource.setReporttype(valuerReportAndFee.getReportType());
        detailsResource.setValuerId(valuerReportAndFee.getValuer().getId());

        if (valuerReportAndFee.getDateOfReceipt() != null)
            detailsResource.setDateofreceipt(dataConversionUtility.convertDateToSAPFormat(valuerReportAndFee.getDateOfReceipt()));
        else
            detailsResource.setDateofreceipt(null);

        if (valuerReportAndFee.getInvoiceDate() != null)
            detailsResource.setInvoicedate(dataConversionUtility.convertDateToSAPFormat(valuerReportAndFee.getInvoiceDate()));
        else
            detailsResource.setInvoicedate(null);

        detailsResource.setInvoiceno(valuerReportAndFee.getInvoiceNo());

        if (valuerReportAndFee.getFeeAmount() != null)
            if (valuerReportAndFee.getFeeAmount() != null)

                detailsResource.setFeeamount(valuerReportAndFee.getFeeAmount().toString());

        detailsResource.setStatusoffeepaid(valuerReportAndFee.getStatusOfFeePaid());


        if (valuerReportAndFee.getNextReportDate() != null)
            detailsResource.setNextreportdate(dataConversionUtility.convertDateToSAPFormat(valuerReportAndFee.getNextReportDate()));
        else
            detailsResource.setNextreportdate(null);

        if (valuerReportAndFee.getReportDate() != null)
            detailsResource.setReportdate(dataConversionUtility.convertDateToSAPFormat(valuerReportAndFee.getReportDate()));
        else
            detailsResource.setReportdate(null);

        detailsResource.setRemarks(valuerReportAndFee.getRemarks());

        if (valuerReportAndFee.getPercentageCompletion() != null)
            detailsResource.setPercentagecompletion(valuerReportAndFee.getPercentageCompletion().toString());

        detailsResource.setDocumentType(valuerReportAndFee.getDocumentType());
        detailsResource.setDocumenttitle(valuerReportAndFee.getDocumentTitle());
        detailsResource.setFileReference(valuerReportAndFee.getFileReference());


        return detailsResource;
    }
}
