package pfs.lms.enquiry.monitoring.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.monitoring.llc.LendersLegalCouncil;
import pfs.lms.enquiry.utils.DataConversionUtility;

import java.io.Serializable;
import java.text.ParseException;

@Component
@JsonInclude (JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties (ignoreUnknown = true)
public class SAPLLCResource implements Serializable   {


    public SAPLLCResource() {
        sapllcResourceDetails = new SAPLLCResourceDetails();
    }

    @JsonProperty(value = "d")
    private SAPLLCResourceDetails sapllcResourceDetails;

    public SAPLLCResourceDetails getSapllcResourceDetails() {
        return sapllcResourceDetails;
    }



    public void setSapllcResourceDetails(SAPLLCResourceDetails sapllcResourceDetails) {
        this.sapllcResourceDetails = sapllcResourceDetails;
    }

    public SAPLLCResourceDetails
                    mapToSAP(LendersLegalCouncil lenderslegalCounsel, User lastProcessedBy) throws ParseException {
        DataConversionUtility dataConversionUtility =  new DataConversionUtility();

       SAPLLCResourceDetails detailsResource= new SAPLLCResourceDetails();

        detailsResource.setId(lenderslegalCounsel.getId());
        detailsResource.setMonitorId(lenderslegalCounsel.getLoanMonitor().getId().toString());
        detailsResource.setSerialNumber(lenderslegalCounsel.getSerialNumber());
        detailsResource.setBpCode(lenderslegalCounsel.getBpCode());
        detailsResource.setName(lenderslegalCounsel.getName());
        detailsResource.setAppraisalId(lenderslegalCounsel.getLoanAppraisal().getId().toString());

        if (lenderslegalCounsel.getDateOfAppointment() != null)
            detailsResource.setDateOfAppointment(dataConversionUtility.convertDateToSAPFormat(lenderslegalCounsel.getDateOfAppointment()));
        else
            detailsResource.setDateOfAppointment(null);

        if (lenderslegalCounsel.getContractPeriodFrom() != null)
        detailsResource.setContractPeriodFrom(dataConversionUtility.convertDateToSAPFormat(lenderslegalCounsel.getContractPeriodFrom()));
        else
            detailsResource.setContractPeriodFrom(null);


        if (lenderslegalCounsel.getContractPeriodTo() != null)
        detailsResource.setContractPeriodTo(dataConversionUtility.convertDateToSAPFormat(lenderslegalCounsel.getContractPeriodTo()));
        else
            detailsResource.setContractPeriodTo(null);

        detailsResource.setContactPerson(lenderslegalCounsel.getContactPerson());
        detailsResource.setContactNumber(lenderslegalCounsel.getContactNumber());
        detailsResource.setEmail(lenderslegalCounsel.getEmail());
        return detailsResource;
    }
}
