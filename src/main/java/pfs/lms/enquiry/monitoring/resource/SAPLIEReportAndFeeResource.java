package pfs.lms.enquiry.monitoring.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.monitoring.lie.LIEReportAndFee;
import pfs.lms.enquiry.utils.DataConversionUtility;

import java.io.Serializable;
import java.text.ParseException;

@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SAPLIEReportAndFeeResource implements Serializable {


    public SAPLIEReportAndFeeResource() {
        saplieReportAndFeeResourceDetails = new SAPLIEReportAndFeeResourceDetails();
    }

    @JsonProperty(value = "d")
    private SAPLIEReportAndFeeResourceDetails saplieReportAndFeeResourceDetails;


    public SAPLIEReportAndFeeResourceDetails getSaplieReportAndFeeResourceDetails() {
        return saplieReportAndFeeResourceDetails;
    }

    public void setSaplieReportAndFeeResourceDetails(SAPLIEReportAndFeeResourceDetails saplieReportAndFeeResourceDetails) {
        this.saplieReportAndFeeResourceDetails = saplieReportAndFeeResourceDetails;
    }

    public SAPLIEReportAndFeeResourceDetails
    mapToSAP(LIEReportAndFee lieReportAndFee,
             User lastProcessedBy) throws ParseException {

        DataConversionUtility dataConversionUtility = new DataConversionUtility();


        SAPLIEReportAndFeeResourceDetails detailsResource = new SAPLIEReportAndFeeResourceDetails();

        detailsResource.setId(lieReportAndFee.getId());
        detailsResource.setSerialNo(lieReportAndFee.getSerialNumber());
        detailsResource.setReporttype(lieReportAndFee.getReportType());
        detailsResource.setLieId(lieReportAndFee.getLendersIndependentEngineer().getId());

        if (lieReportAndFee.getDateOfReceipt() != null)
            detailsResource.setDateofreceipt(dataConversionUtility.convertDateToSAPFormat(lieReportAndFee.getDateOfReceipt()));
        else
            detailsResource.setDateofreceipt(null);

        if (lieReportAndFee.getInvoiceDate() != null)
            detailsResource.setInvoicedate(dataConversionUtility.convertDateToSAPFormat(lieReportAndFee.getInvoiceDate()));
        else
            detailsResource.setInvoicedate(null);

        detailsResource.setInvoiceno(lieReportAndFee.getInvoiceNo());

        if (lieReportAndFee.getFeeAmount() != null)
            if (lieReportAndFee.getFeeAmount() != null)

                detailsResource.setFeeamount(lieReportAndFee.getFeeAmount().toString());

        detailsResource.setStatusoffeepaid(lieReportAndFee.getStatusOfFeePaid());
        lieReportAndFee.getFileReference();


        if (lieReportAndFee.getNextReportDate() != null)
            detailsResource.setNextreportdate(dataConversionUtility.convertDateToSAPFormat(lieReportAndFee.getNextReportDate()));
        else
            detailsResource.setNextreportdate(null);

        if (lieReportAndFee.getReportDate() != null)
            detailsResource.setReportdate(dataConversionUtility.convertDateToSAPFormat(lieReportAndFee.getReportDate()));
        else
            detailsResource.setReportdate(null);

        detailsResource.setRemarks(lieReportAndFee.getRemarks());

        if (lieReportAndFee.getPercentageCompletion() != null)
            detailsResource.setPercentagecompletion(lieReportAndFee.getPercentageCompletion().toString());

        detailsResource.setDocumentType(lieReportAndFee.getDocumentType());
        detailsResource.setDocumenttitle(lieReportAndFee.getDocumentTitle());
        detailsResource.setFileReference(lieReportAndFee.getFileReference());


        return detailsResource;
    }
}
