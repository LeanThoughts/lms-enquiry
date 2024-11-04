package pfs.lms.enquiry.businesspartner.batch;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerIdentification;
import pfs.lms.enquiry.businesspartner.domain.IdentificationCategory;
import pfs.lms.enquiry.businesspartner.repository.IdentificationCategoryRepository;
import pfs.lms.enquiry.utils.DataConversionUtility;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.text.ParseException;


//@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonIgnoreProperties (ignoreUnknown = true)

@Slf4j
@Service
@Transactional
 public class SAPBusinessPartnerIdentificationResource implements Serializable {



    @JsonProperty(value = "d")
    private SAPBusinessPartnerIdentificationResourceDetail sapBusinessPartnerIdentificationResourceDetail;
    DataConversionUtility dataConversionUtility =  new DataConversionUtility();

    public SAPBusinessPartnerIdentificationResource( ) {
         sapBusinessPartnerIdentificationResourceDetail = new SAPBusinessPartnerIdentificationResourceDetail();
     }



    public void setSapBusinessPartnerIdentificationResourceDetail(SAPBusinessPartnerIdentificationResourceDetail sapBusinessPartnerIdentificationResourceDetail) {
        this.sapBusinessPartnerIdentificationResourceDetail = sapBusinessPartnerIdentificationResourceDetail;
    }

    public SAPBusinessPartnerIdentificationResourceDetail
                                mapResource(BusinessPartnerIdentification businessPartnerIdentification) throws ParseException {

        SAPBusinessPartnerIdentificationResourceDetail detailsResource = new SAPBusinessPartnerIdentificationResourceDetail();
        if (businessPartnerIdentification.getPartner().getPartyNumber() != null)
            detailsResource.setBusPartnerNumber(businessPartnerIdentification.getPartner().getPartyNumber().toString());
        else
            detailsResource.setBusPartnerNumber("");



        if(businessPartnerIdentification.getIdentificationNumber() != null)
            detailsResource.setIdentificationNumber(businessPartnerIdentification.getIdentificationNumber());
        else detailsResource.setIdentificationNumber("");

        if(businessPartnerIdentification.getIdInstitute() != null)
            detailsResource.setIdInstitute(businessPartnerIdentification.getIdInstitute());
        else detailsResource.setIdInstitute("");

        if(businessPartnerIdentification.getIdEntryDate() != null)
            detailsResource.setIdEntryDate(dataConversionUtility.convertDateToSAPFormat(businessPartnerIdentification.getIdEntryDate()));
        else detailsResource.setIdEntryDate(null);

        if(businessPartnerIdentification.getIdValidFromDate() != null)
            detailsResource.setIdValidFromDate(dataConversionUtility.convertDateToSAPFormat(businessPartnerIdentification.getIdValidFromDate()));
        else detailsResource.setIdValidFromDate(null);

        if(businessPartnerIdentification.getIdValidToDate() != null)
            detailsResource.setIdValidToDate(dataConversionUtility.convertDateToSAPFormat(businessPartnerIdentification.getIdValidToDate()));
        else detailsResource.setIdValidToDate(null);

        if(businessPartnerIdentification.getCountry() != null)
            detailsResource.setCountry( businessPartnerIdentification.getCountry() );
        else detailsResource.setCountry("");

        if(businessPartnerIdentification.getCountryIso() != null)
            detailsResource.setCountryIso( businessPartnerIdentification.getCountryIso() );
        else detailsResource.setCountryIso("");

        if(businessPartnerIdentification.getRegion() != null)
            detailsResource.setRegion( businessPartnerIdentification.getRegion() );
        else detailsResource.setRegion("");

        detailsResource.setEntityId(businessPartnerIdentification.getId().toString());

        return detailsResource;
    }



}
