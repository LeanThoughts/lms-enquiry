package pfs.lms.enquiry.monitoring.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.monitoring.borrowerfinancials.BorrowerFinancials;
import pfs.lms.enquiry.utils.DataConversionUtility;

import java.text.ParseException;

/**
 * Created by sajeev on 28-Jun-21.
 */
@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SAPBorrowerFinancialsResource {

    @JsonProperty(value = "d")
    private SAPBorrowerFinancialsResourceDetails sapBorrowerFinancialsResourceDetails;

    public SAPBorrowerFinancialsResourceDetails getSapBorrowerFinancialsResourceDetails() {
        return sapBorrowerFinancialsResourceDetails;
    }

    public void setSapBorrowerFinancialsResourceDetails(SAPBorrowerFinancialsResourceDetails sapBorrowerFinancialsResourceDetails) {
        this.sapBorrowerFinancialsResourceDetails = sapBorrowerFinancialsResourceDetails;
    }


    public SAPBorrowerFinancialsResourceDetails mapToSAP(BorrowerFinancials borrowerFinancials) throws ParseException {

        DataConversionUtility dataConversionUtility = new DataConversionUtility();

        SAPBorrowerFinancialsResourceDetails detailedResource = new SAPBorrowerFinancialsResourceDetails();

        detailedResource.setId(borrowerFinancials.getId());
        detailedResource.setMonitorId(borrowerFinancials.getLoanMonitor().getId().toString());
        detailedResource.setSerialNo(borrowerFinancials.getSerialNumber() );

        if (borrowerFinancials.getDateOfExternalRating() != null)
             detailedResource.setDateofexternalrating(dataConversionUtility.convertDateToSAPFormat(borrowerFinancials.getDateOfExternalRating()));
        else
            detailedResource.setDateofexternalrating(null);

        if(borrowerFinancials.getNextDueDateOfExternalRating() != null) {
            detailedResource.setNextduedateofexternalrating(dataConversionUtility.convertDateToSAPFormat(borrowerFinancials.getNextDueDateOfExternalRating()));
        } else
            detailedResource.setNextduedateofexternalrating(null);

        detailedResource.setFiscalyear(  borrowerFinancials.getFiscalYear().toString());
        detailedResource.setNetworth(String.format("%.2f",borrowerFinancials.getNetWorth()));
        detailedResource.setTurnover(String.format("%.2f",borrowerFinancials.getTurnover()));
        detailedResource.setPat(String.format("%.2f",borrowerFinancials.getPat()));
        detailedResource.setOverallrating(borrowerFinancials.getOverAllRating());
        detailedResource.setRemarks(borrowerFinancials.getRemarks());


        return detailedResource;


    }

}
