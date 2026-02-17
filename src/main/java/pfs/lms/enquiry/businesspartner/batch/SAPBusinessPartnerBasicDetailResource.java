package pfs.lms.enquiry.businesspartner.batch;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.businesspartner.repository.BupaRoleCustomerFieldValuesRepository;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.utils.DataConversionUtility;

import java.io.Serializable;
import java.text.ParseException;

@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)




public class SAPBusinessPartnerBasicDetailResource implements Serializable {

    public SAPBusinessPartnerBasicDetailResource() {
        sapBusinessPartnerBasicDetailsResourceDetail = new SAPBusinessPartnerBasicDetailsResourceDetail();
     }

    @JsonProperty(value = "d")
    private SAPBusinessPartnerBasicDetailsResourceDetail sapBusinessPartnerBasicDetailsResourceDetail;
    DataConversionUtility dataConversionUtility = new DataConversionUtility();


    public void setSAPBusinessPartnerBasicDetailsResourceDetails(SAPBusinessPartnerBasicDetailsResourceDetail sapBusinessPartnerBasicDetailsResourceDetail) {
        this.sapBusinessPartnerBasicDetailsResourceDetail = sapBusinessPartnerBasicDetailsResourceDetail;
    }

    public SAPBusinessPartnerBasicDetailsResourceDetail getSapBusinessPartnerBasicDetailsResourceDetail() {
        return sapBusinessPartnerBasicDetailsResourceDetail;
    }

    public SAPBusinessPartnerBasicDetailsResourceDetail
    mapBupaBasicDetails(Partner partner) throws ParseException {

        SAPBusinessPartnerBasicDetailsResourceDetail detailsResource = new SAPBusinessPartnerBasicDetailsResourceDetail();


        if ( partner.getPartnerGroup() != null)
            detailsResource.setPartnerGroup(partner.getPartnerGroup());
        else
            detailsResource.setPartnerGroup("0001");


        if (partner.getPartyNumber() != null)
            detailsResource.setBusPartnerNumber(partner.getPartyNumber().toString());
        else
            detailsResource.setBusPartnerNumber("");

        if (partner.getPartnerCategory() != null)
            detailsResource.setPartnerCategory(partner.getPartnerCategory());
        else
            detailsResource.setPartnerCategory("1");

        if (partner.getPartnerType() != null)
            detailsResource.setPartnerType(partner.getPartnerType());



        switch (partner.getPartnerCategory()) {
            case "1":
                if (partner.getPartyName1() != null)
                    detailsResource.setFirstname(partner.getPartyName1());
                if (partner.getPartyName2() != null)
                    detailsResource.setLastname(partner.getPartyName2());
                break;
            case "2" :
                if (partner.getPartyName1() != null)
                    detailsResource.setName1(partner.getPartyName1());
                if (partner.getPartyName2() != null)
                    detailsResource.setName2(partner.getPartyName2());
                break;
            case "3":
                if (partner.getPartyName1() != null)
                    detailsResource.setName1(partner.getPartyName1());
                if (partner.getPartyName2() != null)
                    detailsResource.setName2(partner.getPartyName2());

                break;
            default:


        }

        if (partner.getExternalBPNumber() != null)
            detailsResource.setPartnerExternalNumber(partner.getExternalBPNumber());


        if (partner.getPartyRole() != null)
            detailsResource.setPartnerRole(partner.getPartyRole());

        if (partner.getEmail() != null) {
            detailsResource.setEmail(partner.getEmail());
        }
        if (partner.getCity() != null) {
            detailsResource.setCity(partner.getCity());
        }
        if (partner.getState() != null) {
            detailsResource.setState(partner.getState());
        }
        if (partner.getPostalCode() != null) {
            detailsResource.setPostalCode(partner.getPostalCode());
        }
        //STREET
        if (partner.getAddressLine1() != null) {
            detailsResource.setAddressLine1(partner.getAddressLine1());
        }
        //STR_SUPPL1
        if (partner.getAddressLine2() != null) {
            detailsResource.setAddressLine2(partner.getAddressLine2());
        }
        //STR_SUPPL2
        if (partner.getAddressLine3() != null) {
            detailsResource.setAddressLine3(partner.getAddressLine3());
        }
//        if (partner.getStreet() != null) {
//            detailsResource.setStreet(partner.getStreet());
//        }

        if(partner.getAddressValidFromDate() != null)
            detailsResource.setAddressValidFromDate(partner.getAddressValidFromDate().toString());


        if (partner.getCountry() == null || partner.getCountry().length() == 0) {
            detailsResource.setCountry("IN");
        } else {
            detailsResource.setCountry(partner.getCountry());

        }

        if (partner.getContactPersonName() != null) {
            detailsResource.setContactPerName(partner.getContactPersonName());
        }
        if (partner.getContactNumber() != null) {
            detailsResource.setContactNumber(partner.getContactNumber());
        }

        if (partner.getSearchTerm1() != null) {
            detailsResource.setSearchTerm1(partner.getSearchTerm1());
        }

        if (partner.getSearchTerm2() != null) {
            detailsResource.setSearchTerm2(partner.getSearchTerm2());
        }
        if (partner.getPartyRole() != null) {
            detailsResource.setRole(partner.getPartyRole());
        }

        if (partner.getTitle() != null){
            detailsResource.setTitle(partner.getTitle());
        }
        if(partner.getPartnerType() != null){
            detailsResource.setPartnerType(partner.getPartnerType());
        }

        if(partner.getDefaultPartnerRole() == null) {
            detailsResource.setPartnerRole("TR0100");
        }else{
            detailsResource.setPartnerRole(partner.getDefaultPartnerRole());
        }

        if (partner.getLegalEntity() != null){
            detailsResource.setLegalEntity(partner.getLegalEntity());
        }
        if (partner.getLegalForm() != null){
            detailsResource.setLegalForm(partner.getLegalForm());
        }
        if (partner.getHouseBank() != null){
            detailsResource.setHouseBank(partner.getHouseBank());
        }

        if (partner.getDunningProcedure() != null){
            detailsResource.setDunningprocedure(partner.getDunningProcedure());
        }
        if (partner.getCheckDoubleInvoice() != null){
            detailsResource.setCheckdoubleinvoice("X");
        }
        if (partner.getPaymentMethod() != null){
            detailsResource.setPaymentmethod(partner.getPaymentMethod());
        }
        if (partner.getPaymentTerms() != null){
            detailsResource.setPaymentterms(partner.getPaymentTerms());
        }
        if (partner.getSortKey() != null){
            detailsResource.setSortkey(partner.getSortKey());
        }
        if (partner.getPlanningGroup() != null){
            detailsResource.setPlanninggroup(partner.getPlanningGroup());
        }
        if (partner.getReconAccount() != null){
            detailsResource.setReconaccount(partner.getReconAccount());
        } else{

        }

        detailsResource.setRole(partner.getPartyRole());
        detailsResource.setEntityId(partner.getId().toString());

        return detailsResource;
    }


}
