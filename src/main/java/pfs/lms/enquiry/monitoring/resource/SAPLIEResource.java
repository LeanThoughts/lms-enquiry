package pfs.lms.enquiry.monitoring.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.monitoring.lie.LendersIndependentEngineer;
import pfs.lms.enquiry.utils.DataConversionUtility;

import java.io.Serializable;
import java.text.ParseException;

@Component
@JsonInclude (JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties (ignoreUnknown = true)
public class SAPLIEResource implements Serializable   {


    public SAPLIEResource() {
        saplieResourceDetails = new SAPLIEResourceDetails();
    }

    @JsonProperty(value = "d")
    private SAPLIEResourceDetails saplieResourceDetails;


    public SAPLIEResourceDetails getSaplieResourceDetails() {
        return saplieResourceDetails;
    }

    public void setSaplieResourceDetails(SAPLIEResourceDetails saplieResourceDetails) {
        this.saplieResourceDetails = saplieResourceDetails;
    }

    public SAPLIEResourceDetails
                    mapToSAP(LendersIndependentEngineer lendersIndependentEngineer, User lastProcessedBy) throws ParseException {
        DataConversionUtility dataConversionUtility =  new DataConversionUtility();

       SAPLIEResourceDetails detailsResource= new SAPLIEResourceDetails();

        detailsResource.setId(lendersIndependentEngineer.getId());
        detailsResource.setMonitorId(lendersIndependentEngineer.getLoanMonitor().getId().toString());
        detailsResource.setSerialNumber(lendersIndependentEngineer.getSerialNumber());
        detailsResource.setBpCode(lendersIndependentEngineer.getBpCode());
        detailsResource.setName(lendersIndependentEngineer.getName());
        detailsResource.setAppraisalId(lendersIndependentEngineer.getLoanAppraisal().getId().toString());

        if (lendersIndependentEngineer.getDateOfAppointment() != null)
            detailsResource.setDateOfAppointment(dataConversionUtility.convertDateToSAPFormat(lendersIndependentEngineer.getDateOfAppointment()));
        else
            detailsResource.setDateOfAppointment(null);

        if (lendersIndependentEngineer.getContractPeriodFrom() != null)
        detailsResource.setContractPeriodFrom(dataConversionUtility.convertDateToSAPFormat(lendersIndependentEngineer.getContractPeriodFrom()));
        else
            detailsResource.setContractPeriodFrom(null);


        if (lendersIndependentEngineer.getContractPeriodFrom() != null)
        detailsResource.setContractPeriodTo(dataConversionUtility.convertDateToSAPFormat(lendersIndependentEngineer.getContractPeriodTo()));
        else
            detailsResource.setContractPeriodTo(null);

        detailsResource.setContactPerson(lendersIndependentEngineer.getContactPerson());
        detailsResource.setContactNumber(lendersIndependentEngineer.getContactNumber());
        detailsResource.setEmail(lendersIndependentEngineer.getEmail());
        return detailsResource;
    }
}
