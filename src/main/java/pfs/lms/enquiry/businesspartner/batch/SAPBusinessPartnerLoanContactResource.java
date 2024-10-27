package pfs.lms.enquiry.businesspartner.batch;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerLoanContact;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.utils.DataConversionUtility;

import java.io.Serializable;
import java.text.ParseException;

@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties (ignoreUnknown = true)

public class SAPBusinessPartnerLoanContactResource implements Serializable {

    public SAPBusinessPartnerLoanContactResource() {
        sapBusinessPartnerLoanContactResourceDetail = new SAPBusinessPartnerLoanContactResourceDetail();
    }

    @JsonProperty(value = "d")
    private SAPBusinessPartnerLoanContactResourceDetail sapBusinessPartnerLoanContactResourceDetail;
    DataConversionUtility dataConversionUtility =  new DataConversionUtility();


    public void setSapBusinessPartnerLoanContactResourceDetail(SAPBusinessPartnerLoanContactResourceDetail sapBusinessPartnerLoanContactResourceDetail) {
        this.sapBusinessPartnerLoanContactResourceDetail = sapBusinessPartnerLoanContactResourceDetail;
    }

    public SAPBusinessPartnerLoanContactResourceDetail
                                mapResource(BusinessPartnerLoanContact businessPartnerLoanContact ) throws ParseException {

        SAPBusinessPartnerLoanContactResourceDetail detailsResource = new SAPBusinessPartnerLoanContactResourceDetail();
        if (businessPartnerLoanContact.getPartner().getPartyNumber() != null)
            detailsResource.setBusPartnerNumber(businessPartnerLoanContact.getPartner().getPartyNumber().toString());
        else
            detailsResource.setBusPartnerNumber("");

        if(businessPartnerLoanContact.getSelection() != null)
            detailsResource.setSelection(businessPartnerLoanContact.getSelection());
        else detailsResource.setSelection("");

        if(businessPartnerLoanContact.getName() != null)
            detailsResource.setName(businessPartnerLoanContact.getName());
        else detailsResource.setName("");

        if(businessPartnerLoanContact.getLoanNumber() != null)
        detailsResource.setLoanNumber(businessPartnerLoanContact.getLoanNumber());
        else detailsResource.setLoanNumber("");

        if(businessPartnerLoanContact.getSerialNumber() != null)
        detailsResource.setSerialNumber(businessPartnerLoanContact.getSerialNumber().toString());
        else detailsResource.setSerialNumber("");

        if(businessPartnerLoanContact.getBranchAddress() != null)
        detailsResource.setBranchAddress(businessPartnerLoanContact.getBranchAddress());
        else detailsResource.setBranchAddress("");

        if(businessPartnerLoanContact.getDesignation() != null)
        detailsResource.setDesignation( businessPartnerLoanContact.getDesignation());
        else detailsResource.setDesignation("");

        if(businessPartnerLoanContact.getDepartment() != null)
        detailsResource.setDepartment(businessPartnerLoanContact.getDepartment());
        else detailsResource.setDepartment("");

        if(businessPartnerLoanContact.getTelephoneNumber() != null)
        detailsResource.setTelephoneNumber (businessPartnerLoanContact.getTelephoneNumber());
        else detailsResource.setTelephoneNumber("");

        if(businessPartnerLoanContact.getLandLineNumber() != null)
            detailsResource.setLandLineNumber(businessPartnerLoanContact.getLandLineNumber());
        else detailsResource.setLandLineNumber("");

        if(businessPartnerLoanContact.getEmail() != null)
            detailsResource.setEmail(businessPartnerLoanContact.getEmail());
        else detailsResource.setEmail("");

        if(businessPartnerLoanContact.getFaxNumber() != null)
            detailsResource.setFaxNumber(businessPartnerLoanContact.getFaxNumber());
        else detailsResource.setFaxNumber("");



//        if (customerRejection.getDate() != null){
//            detailsResource.setDate(dataConversionUtility.convertDateToSAPFormat(customerRejection.getDate()));
//        } else
//            detailsResource.setDate(null);



        return detailsResource;
    }



}
