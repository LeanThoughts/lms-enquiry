package pfs.lms.enquiry.monitoring.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.monitoring.operatingparameters.OperatingParameterPLF;
import pfs.lms.enquiry.utils.DataConversionUtility;

import java.text.ParseException;

/**
 * Created by sajeev on 28-Jun-21.
 */
@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SAPOperatingParameterPLFCUFResource {

    @JsonProperty(value = "d")
    private SAPOperatingParameterPLFCUFResourceDetails sapOperatingParameterPLFCUFResourceDetails;


    public SAPOperatingParameterPLFCUFResourceDetails getSapOperatingParameterPLFCUFResourceDetails() {
        return sapOperatingParameterPLFCUFResourceDetails;
    }

    public void setSapOperatingParameterPLFCUFResourceDetails(SAPOperatingParameterPLFCUFResourceDetails sapOperatingParameterPLFCUFResourceDetails) {
        this.sapOperatingParameterPLFCUFResourceDetails = sapOperatingParameterPLFCUFResourceDetails;
    }

    public SAPOperatingParameterPLFCUFResourceDetails mapToSAP(OperatingParameterPLF operatingParameterPLF) throws ParseException {

        DataConversionUtility dataConversionUtility = new DataConversionUtility();

        SAPOperatingParameterPLFCUFResourceDetails detailedResource = new SAPOperatingParameterPLFCUFResourceDetails();

        detailedResource.setId(operatingParameterPLF.getId());
        detailedResource.setMonitorId(operatingParameterPLF.getLoanMonitor().getId().toString());
        detailedResource.setSerialNo(operatingParameterPLF.getSerialNumber().toString() );

        detailedResource.setMusGenDuringYear(operatingParameterPLF.getMiUnGenerated().toString());

        detailedResource.setPeriod(operatingParameterPLF.getYear().toString());
        //detailedResource.setActualPlfCuf(operatingParameterPLF.getActualYearlyAveragePlfCuf().toString());


        detailedResource.setDesignPlfCuf(operatingParameterPLF.getDesignPlfCuf().toString());

       // detailedResource.setDesignPlfCuf(String.format("%.2f", Double.parseDouble(operatingParameterPLF.getDesignPlfCuf().toString())));
       // detailedResource.setActualPlfCuf(String.format("%.2f", Double.parseDouble(operatingParameterPLF.getActualYearlyAveragePlfCuf().toString())));

        detailedResource.setDesignPlfCuf(operatingParameterPLF.getDesignPlfCuf());
        detailedResource.setActualPlfCuf(operatingParameterPLF.getActualYearlyAveragePlfCuf().toString());
        return detailedResource;


    }

}
