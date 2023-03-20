package pfs.lms.enquiry.monitoring.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.monitoring.lfa.LendersFinancialAdvisor;
import pfs.lms.enquiry.monitoring.lia.LendersInsuranceAdvisor;
import pfs.lms.enquiry.utils.DataConversionUtility;

import java.io.Serializable;
import java.text.ParseException;

@Component
@JsonInclude (JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties (ignoreUnknown = true)
public class SAPLIAResource implements Serializable   {


    public SAPLIAResource() {
        sapliaResourceDetails = new SAPLIAResourceDetails();
    }

    @JsonProperty(value = "d")
    private SAPLIAResourceDetails sapliaResourceDetails;


    public SAPLIAResourceDetails getSapliaResourceDetails() {
        return sapliaResourceDetails;
    }

    public void setSapliaResourceDetails(SAPLIAResourceDetails sapliaResourceDetails) {
        this.sapliaResourceDetails = sapliaResourceDetails;
    }

    public SAPLIAResourceDetails
                    mapToSAP(LendersInsuranceAdvisor lendersInsuranceAdvisor, User lastProcessedBy) throws ParseException {
        DataConversionUtility dataConversionUtility =  new DataConversionUtility();

       SAPLIAResourceDetails detailsResource= new SAPLIAResourceDetails();

        detailsResource.setId(lendersInsuranceAdvisor.getId());
        detailsResource.setMonitorId(lendersInsuranceAdvisor.getLoanMonitor().getId().toString());
        detailsResource.setSerialNumber(lendersInsuranceAdvisor.getSerialNumber());
        detailsResource.setBpCode(lendersInsuranceAdvisor.getBpCode());
        detailsResource.setName(lendersInsuranceAdvisor.getName());
        detailsResource.setAppraisalId(lendersInsuranceAdvisor.getLoanAppraisal().getId().toString());

        if (lendersInsuranceAdvisor.getDateOfAppointment() != null)
            detailsResource.setDateOfAppointment(dataConversionUtility.convertDateToSAPFormat(lendersInsuranceAdvisor.getDateOfAppointment()));
        else
            detailsResource.setDateOfAppointment(null);

        if (lendersInsuranceAdvisor.getContractPeriodFrom() != null)
        detailsResource.setContractPeriodFrom(dataConversionUtility.convertDateToSAPFormat(lendersInsuranceAdvisor.getContractPeriodFrom()));
        else
            detailsResource.setContractPeriodFrom(null);


        if (lendersInsuranceAdvisor.getContractPeriodTo() != null)
        detailsResource.setContractPeriodTo(dataConversionUtility.convertDateToSAPFormat(lendersInsuranceAdvisor.getContractPeriodTo()));
        else
            detailsResource.setContractPeriodTo(null);

        detailsResource.setContactPerson(lendersInsuranceAdvisor.getContactPerson());
        detailsResource.setContactNumber(lendersInsuranceAdvisor.getContactNumber());
        detailsResource.setEmail(lendersInsuranceAdvisor.getEmail());
        return detailsResource;
    }
}
