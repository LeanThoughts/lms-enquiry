package pfs.lms.enquiry.businesspartner.batch;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerRole;
import pfs.lms.enquiry.utils.DataConversionUtility;

import java.io.Serializable;
import java.text.ParseException;
import java.time.LocalDate;

@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class SAPBusinessPartnerRoleResource implements Serializable {

    public SAPBusinessPartnerRoleResource() {
        sapBusinessPartnerRoleResourceDetail = new SAPBusinessPartnerRoleResourceDetail();
    }

    @JsonProperty(value = "d")
    private SAPBusinessPartnerRoleResourceDetail sapBusinessPartnerRoleResourceDetail;
    DataConversionUtility dataConversionUtility = new DataConversionUtility();


    public void setSapBusinessPartnerRoleResourceDetail(SAPBusinessPartnerRoleResourceDetail sapBusinessPartnerRoleResourceDetail) {
        this.sapBusinessPartnerRoleResourceDetail = sapBusinessPartnerRoleResourceDetail;
    }

    public SAPBusinessPartnerRoleResourceDetail
    mapResource(BusinessPartnerRole businessPartnerRole) throws ParseException {

        SAPBusinessPartnerRoleResourceDetail detailsResource = new SAPBusinessPartnerRoleResourceDetail();

        if (businessPartnerRole.getPartner().getPartyNumber() != null)
            detailsResource.setBusPartnerNumber(businessPartnerRole.getPartner().getPartyNumber().toString());
        else
            detailsResource.setBusPartnerNumber("");

        if (businessPartnerRole.getRoleType() != null) {
            detailsResource.setRoleType(businessPartnerRole.getRoleType().getCode());
            detailsResource.setPartnerRoleCategory(businessPartnerRole.getRoleType().getCode());
        } else {
            detailsResource.setRoleType("");
            detailsResource.setPartnerRoleCategory("");
        }

        if (businessPartnerRole.getDifferentiationType() != null)
            detailsResource.setDifferentiationType(businessPartnerRole.getDifferentiationType());
        else detailsResource.setDifferentiationType("");

        if (businessPartnerRole.getAllPartnerRoles() != null)
            detailsResource.setAllPartnerRoles(businessPartnerRole.getAllPartnerRoles());
        else detailsResource.setAllPartnerRoles("");

        LocalDate localDate = LocalDate.now();

        if (businessPartnerRole.getValidFromDate() != null) {
            detailsResource.setValidFromDate(dataConversionUtility.convertDateToSAPFormat(businessPartnerRole.getValidFromDate()));
        } else
            detailsResource.setValidFromDate(dataConversionUtility.convertDateToSAPFormat(localDate));

        if (businessPartnerRole.getValidToDate() != null) {
            detailsResource.setValidToDate(dataConversionUtility.convertDateToSAPFormat(businessPartnerRole.getValidToDate()));
        } else
            detailsResource.setValidToDate(dataConversionUtility.convertDateToSAPFormat(localDate));

        detailsResource.setEntityId(businessPartnerRole.getId().toString());

        return detailsResource;
    }


}
