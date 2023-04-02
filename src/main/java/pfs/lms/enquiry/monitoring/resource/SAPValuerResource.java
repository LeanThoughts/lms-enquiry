package pfs.lms.enquiry.monitoring.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.monitoring.lie.LendersIndependentEngineer;
import pfs.lms.enquiry.monitoring.valuer.Valuer;
import pfs.lms.enquiry.utils.DataConversionUtility;

import java.io.Serializable;
import java.text.ParseException;

@Component
@JsonInclude (JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties (ignoreUnknown = true)
public class SAPValuerResource implements Serializable   {


    public SAPValuerResource() {
        sapValuerResourceDetails = new SAPValuerResourceDetails();
    }

    @JsonProperty(value = "d")
    private SAPValuerResourceDetails sapValuerResourceDetails;


    public SAPValuerResourceDetails getSapValuerResourceDetails() {
        return sapValuerResourceDetails;
    }

    public void setSapValuerResourceDetails(SAPValuerResourceDetails sapValuerResourceDetails) {
        this.sapValuerResourceDetails = sapValuerResourceDetails;
    }

    public SAPValuerResourceDetails
                    mapToSAP(Valuer valuer, User lastProcessedBy) throws ParseException {
        DataConversionUtility dataConversionUtility =  new DataConversionUtility();

       SAPValuerResourceDetails detailsResource= new SAPValuerResourceDetails();

        detailsResource.setId(valuer.getId());
        detailsResource.setMonitorId(valuer.getLoanMonitor().getId().toString());
        detailsResource.setSerialNumber(valuer.getSerialNumber());
        detailsResource.setBpCode(valuer.getBpCode());
        detailsResource.setName(valuer.getName());
        detailsResource.setAppraisalId(valuer.getLoanAppraisal().getId().toString());

        if (valuer.getDateOfAppointment() != null)
            detailsResource.setDateOfAppointment(dataConversionUtility.convertDateToSAPFormat(valuer.getDateOfAppointment()));
        else
            detailsResource.setDateOfAppointment(null);

        if (valuer.getContractPeriodFrom() != null)
        detailsResource.setContractPeriodFrom(dataConversionUtility.convertDateToSAPFormat(valuer.getContractPeriodFrom()));
        else
            detailsResource.setContractPeriodFrom(null);


        if (valuer.getContractPeriodTo() != null)
        detailsResource.setContractPeriodTo(dataConversionUtility.convertDateToSAPFormat(valuer.getContractPeriodTo()));
        else
            detailsResource.setContractPeriodTo(null);

        detailsResource.setContactPerson(valuer.getContactPerson());
        detailsResource.setContactNumber(valuer.getContactNumber());
        detailsResource.setEmail(valuer.getEmail());
        return detailsResource;
    }
}
