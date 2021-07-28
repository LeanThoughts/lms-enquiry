package pfs.lms.enquiry.monitoring.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.monitoring.domain.RateOfInterest;
import pfs.lms.enquiry.utils.DataConversionUtility;

import java.text.ParseException;

/**
 * Created by sajeev on 28-Jun-21.
 */
@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SAPInterestRateResource {

    @JsonProperty(value = "d")
    private SAPInterestRateResourceDetails sapInterestRateResourceDetails;


    public SAPInterestRateResourceDetails getSapInterestRateResourceDetails() {
        return sapInterestRateResourceDetails;
    }

    public void setSapInterestRateResourceDetails(SAPInterestRateResourceDetails sapInterestRateResourceDetails) {
        this.sapInterestRateResourceDetails = sapInterestRateResourceDetails;
    }

    public SAPInterestRateResourceDetails mapToSAP(RateOfInterest rateOfInterest) throws ParseException {

        DataConversionUtility dataConversionUtility = new DataConversionUtility();

        SAPInterestRateResourceDetails detailedResource = new SAPInterestRateResourceDetails();

        detailedResource.setId(rateOfInterest.getId());
        detailedResource.setMonitorId(rateOfInterest.getLoanMonitor().getId().toString());
        detailedResource.setSerialNo(rateOfInterest.getSerialNumber());

        if (rateOfInterest.getValidFromDate() !=null)
            detailedResource.setValidFromDate(dataConversionUtility.convertDateToSAPFormat(rateOfInterest.getValidFromDate()));
        else
            detailedResource.setValidFromDate(null);

        detailedResource.setConditionType(rateOfInterest.getConditionType());
        detailedResource.setInterestTypeIndicator(rateOfInterest.getInterestTypeIndicator());
        detailedResource.setReferenceInterestRate(rateOfInterest.getReferenceInterestRate());

        detailedResource.setInterestRate(String.format("%.7f",rateOfInterest.getInterestRate()));

        if (rateOfInterest.getCalculationDate() !=null)
            detailedResource.setCalculationDate(dataConversionUtility.convertDateToSAPFormat(rateOfInterest.getCalculationDate()));
        else
            detailedResource.setCalculationDate(null);

        if (rateOfInterest.getIsCalculationDateOnMonthEnd() == true)
            detailedResource.setIsDueDateOnMonthEnd('X');
        else
            detailedResource.setIsDueDateOnMonthEnd(' ');

        if (rateOfInterest.getDueDate() !=null)
            detailedResource.setDueDate(dataConversionUtility.convertDateToSAPFormat(rateOfInterest.getDueDate()));
        else
            detailedResource.setValidFromDate(null);

        if (rateOfInterest.getIsDueDateOnMonthEnd() == true)
            detailedResource.setIsDueDateOnMonthEnd('X');
        else
            detailedResource.setIsDueDateOnMonthEnd(' ');

        if (rateOfInterest.getIsCalculationDateOnMonthEnd() == true)
             detailedResource.setIsCalculationDateOnMonthEnd('X');
        else
            detailedResource.setIsDueDateOnMonthEnd(' ');

        detailedResource.setInterestPaymentFrequency(rateOfInterest.getInterestPaymentFrequency().toString());
        detailedResource.setPaymentForm(rateOfInterest.getPaymentForm());
        detailedResource.setInterestCalculationMethod(rateOfInterest.getInterestCalculationMethod());

        return detailedResource;


    }

}
