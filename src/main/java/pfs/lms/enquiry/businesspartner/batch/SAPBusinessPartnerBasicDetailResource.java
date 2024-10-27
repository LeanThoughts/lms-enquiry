package pfs.lms.enquiry.businesspartner.batch;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.utils.DataConversionUtility;

import java.io.Serializable;
import java.text.ParseException;

@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties (ignoreUnknown = true)

public class SAPBusinessPartnerBasicDetailResource implements Serializable {

    public SAPBusinessPartnerBasicDetailResource() {
        sapBusinessPartnerBasicDetailsResourceDetail = new SAPBusinessPartnerBasicDetailsResourceDetail();
    }

    @JsonProperty(value = "d")
    private SAPBusinessPartnerBasicDetailsResourceDetail sapBusinessPartnerBasicDetailsResourceDetail;
    DataConversionUtility dataConversionUtility =  new DataConversionUtility();


    public void setSAPBusinessPartnerBasicDetailsResourceDetails(SAPBusinessPartnerBasicDetailsResourceDetail sapBusinessPartnerBasicDetailsResourceDetail) {
        this.sapBusinessPartnerBasicDetailsResourceDetail = sapBusinessPartnerBasicDetailsResourceDetail;
    }

    public SAPBusinessPartnerBasicDetailsResourceDetail
                                mapBupaBasicDetails(Partner partner) throws ParseException {

        SAPBusinessPartnerBasicDetailsResourceDetail detailsResource = new SAPBusinessPartnerBasicDetailsResourceDetail();
        if (partner.getPartyNumber() != null)
            detailsResource.setBusPartnerNumber(partner.getPartyNumber().toString());
        else
            detailsResource.setBusPartnerNumber("");

        if (partner.getPartyCategory() !=null)
            detailsResource.setPartnerCategory(partner.getPartyCategory().toString());
        if (partner.getPartnerType() != null)
            detailsResource.setPartnerType(partner.getPartnerType());
        if (partner.getPartnerExternalNumber() != null)
            detailsResource.setPartnerExternalNumber(partner.getPartnerExternalNumber());
        if (partner.getPartyRole() != null)
            detailsResource.setPartnerRole(partner.getPartyRole());
        if (partner.getPartyName1() != null)
            detailsResource.setName1(partner.getPartyName1());
        if (partner.getPartyName2() != null)
            detailsResource.setName2(partner.getPartyName2());
        if (partner.getPartyName1() != null)
            detailsResource.setFirstname(partner.getPartyName1());
        if (partner.getPartyName2() != null)
            detailsResource.setLastname(partner.getPartyName2());
        if (partner.getEmail() != null){
            detailsResource.setEmail(partner.getEmail());
        }
        if (partner.getCity() != null){
            detailsResource.setCity(partner.getCity());
        }
        if (partner.getState() != null){
            detailsResource.setState(partner.getState());
        }
        if (partner.getPostalCode() != null){
            detailsResource.setPostalCode(partner.getPostalCode());
        }
        if (partner.getAddressLine1() != null){
            detailsResource.setHouseNo(partner.getAddressLine1());
        }
        if (partner.getAddressLine2() != null){
            detailsResource.setStreet(partner.getAddressLine2());
        }
        if (partner.getCountry() != null){
            detailsResource.setCountry(partner.getCountry());
        }

        if (partner.getContactPersonName() != null){
            detailsResource.setContactPerName(partner.getContactPersonName());
        }
        if (partner.getContactNumber() != null){
            detailsResource.setContactNumber(partner.getContactNumber());
        }

        if (partner.getSearchTerm1() != null){
            detailsResource.setSearchTerm1(partner.getSearchTerm1());
        }

        if (partner.getSearchTerm2() != null){
            detailsResource.setSearchTerm2(partner.getSearchTerm2());
        }
        if (partner.getPartyRole() != null){
            detailsResource.setRole(partner.getPartyRole());
        }

//        if (customerRejection.getDate() != null){
//            detailsResource.setDate(dataConversionUtility.convertDateToSAPFormat(customerRejection.getDate()));
//        } else
//            detailsResource.setDate(null);



        return detailsResource;
    }



}
