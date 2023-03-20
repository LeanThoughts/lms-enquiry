package pfs.lms.enquiry.monitoring.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.monitoring.lfa.LendersFinancialAdvisor;
import pfs.lms.enquiry.monitoring.lie.LendersIndependentEngineer;
import pfs.lms.enquiry.utils.DataConversionUtility;

import java.io.Serializable;
import java.text.ParseException;

@Component
@JsonInclude (JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties (ignoreUnknown = true)
public class SAPLFAResource implements Serializable   {


    public SAPLFAResource() {
        saplfaResourceDetails = new SAPLFAResourceDetails();
    }

    @JsonProperty(value = "d")
    private SAPLFAResourceDetails saplfaResourceDetails;


    public SAPLFAResourceDetails getSaplfaResourceDetails() {
        return saplfaResourceDetails;
    }

    public void setSaplfaResourceDetails(SAPLFAResourceDetails saplfaResourceDetails) {
        this.saplfaResourceDetails = saplfaResourceDetails;
    }

    public SAPLFAResourceDetails
                    mapToSAP(LendersFinancialAdvisor lendersFinancialAdvisor, User lastProcessedBy) throws ParseException {
        DataConversionUtility dataConversionUtility =  new DataConversionUtility();

       SAPLFAResourceDetails detailsResource= new SAPLFAResourceDetails();

        detailsResource.setId(lendersFinancialAdvisor.getId());
        detailsResource.setMonitorId(lendersFinancialAdvisor.getLoanMonitor().getId().toString());
        detailsResource.setSerialNumber(lendersFinancialAdvisor.getSerialNumber());
        detailsResource.setBpCode(lendersFinancialAdvisor.getBpCode());
        detailsResource.setName(lendersFinancialAdvisor.getName());
        detailsResource.setAppraisalId(lendersFinancialAdvisor.getLoanAppraisal().getId().toString());

        if (lendersFinancialAdvisor.getDateOfAppointment() != null)
            detailsResource.setDateOfAppointment(dataConversionUtility.convertDateToSAPFormat(lendersFinancialAdvisor.getDateOfAppointment()));
        else
            detailsResource.setDateOfAppointment(null);

        if (lendersFinancialAdvisor.getContractPeriodFrom() != null)
        detailsResource.setContractPeriodFrom(dataConversionUtility.convertDateToSAPFormat(lendersFinancialAdvisor.getContractPeriodFrom()));
        else
            detailsResource.setContractPeriodFrom(null);


        if (lendersFinancialAdvisor.getContractPeriodTo() != null)
        detailsResource.setContractPeriodTo(dataConversionUtility.convertDateToSAPFormat(lendersFinancialAdvisor.getContractPeriodTo()));
        else
            detailsResource.setContractPeriodTo(null);

        detailsResource.setContactPerson(lendersFinancialAdvisor.getContactPerson());
        detailsResource.setContactNumber(lendersFinancialAdvisor.getContactNumber());
        detailsResource.setEmail(lendersFinancialAdvisor.getEmail());
        return detailsResource;
    }
}
