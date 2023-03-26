package pfs.lms.enquiry.monitoring.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.monitoring.domain.FinancialCovenants;
import pfs.lms.enquiry.utils.DataConversionUtility;

import java.text.ParseException;

/**
 * Created by sajeev on 28-Jun-21.
 */
@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SAPFinancialCovenantsResource {

    public SAPFinancialCovenantsResource() {
        sapFinancialCovenantsResourceDetails = new SAPFinancialCovenantsResourceDetails();
    }

    @JsonProperty(value = "d")
    private SAPFinancialCovenantsResourceDetails sapFinancialCovenantsResourceDetails;


    public SAPFinancialCovenantsResourceDetails getSapFinancialCovenantsResourceDetails() {
        return sapFinancialCovenantsResourceDetails;
    }

    public void setSapFinancialCovenantsResourceDetails(SAPFinancialCovenantsResourceDetails sapFinancialCovenantsResourceDetails) {
        this.sapFinancialCovenantsResourceDetails = sapFinancialCovenantsResourceDetails;
    }


    public SAPFinancialCovenantsResourceDetails mapToSAP(FinancialCovenants financialCovenants) throws ParseException {

        DataConversionUtility dataConversionUtility = new DataConversionUtility();

        SAPFinancialCovenantsResourceDetails detailedResource = new SAPFinancialCovenantsResourceDetails();

        detailedResource.setFinCovId(financialCovenants.getId());
        detailedResource.setMonitorId(financialCovenants.getLoanMonitor().getId().toString());
        detailedResource.setSerialNo(financialCovenants.getSerialNumber());

        detailedResource.setFinancialcovenanttype(financialCovenants.getFinancialCovenantType());
        detailedResource.setFinancialyear(financialCovenants.getFinancialYear().toString());
        detailedResource.setDebtequityratio(String.format("%.2f",financialCovenants.getDebtEquityRatio()));
        detailedResource.setDscr(String.format("%.2f",financialCovenants.getDscr()));
        detailedResource.setToltnw(String.format("%.2f",financialCovenants.getTolTnw()));
        detailedResource.setRemarksfordeviation(financialCovenants.getRemarksForDeviation());

        detailedResource.setFileReference(financialCovenants.getFileReference());

        return detailedResource;


    }

}
