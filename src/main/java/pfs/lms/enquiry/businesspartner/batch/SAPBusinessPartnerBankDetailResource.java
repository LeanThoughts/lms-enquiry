package pfs.lms.enquiry.businesspartner.batch;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerBankDetail;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.utils.DataConversionUtility;

import java.io.Serializable;
import java.text.ParseException;

@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class SAPBusinessPartnerBankDetailResource implements Serializable {

    public SAPBusinessPartnerBankDetailResource() {
        sapBusinessPartnerBankDetailResourceDetail = new SAPBusinessPartnerBankDetailResourceDetail();
    }

    @JsonProperty(value = "d")
    private SAPBusinessPartnerBankDetailResourceDetail sapBusinessPartnerBankDetailResourceDetail;
    DataConversionUtility dataConversionUtility = new DataConversionUtility();


    public void setSapBusinessPartnerBankDetailResourceDetail(SAPBusinessPartnerBankDetailResourceDetail sapBusinessPartnerBankDetailResourceDetail) {
        this.sapBusinessPartnerBankDetailResourceDetail = sapBusinessPartnerBankDetailResourceDetail;
    }

    public SAPBusinessPartnerBankDetailResourceDetail
    mapResourceDetails(BusinessPartnerBankDetail businessPartnerBankDetail) throws ParseException {

        SAPBusinessPartnerBankDetailResourceDetail detailsResource = new SAPBusinessPartnerBankDetailResourceDetail();
        if (businessPartnerBankDetail.getPartner().getPartyNumber() != null)
            detailsResource.setBusPartnerNumber(businessPartnerBankDetail.getPartner().getPartyNumber().toString());
        else
            detailsResource.setBusPartnerNumber("");

        if (businessPartnerBankDetail.getBankDetailId() != null)
            detailsResource.setBankDetailId(businessPartnerBankDetail.getBankDetailId());
        else
            detailsResource.setBankDetailId("");

        if (businessPartnerBankDetail.getBankCountry() != null)
            detailsResource.setBankCountry(businessPartnerBankDetail.getBankCountry().substring(0,2));
        else detailsResource.setBankCountry("");

        if (businessPartnerBankDetail.getBankCountryIso() != null)
            detailsResource.setBankCountryIso(businessPartnerBankDetail.getBankCountryIso());
        else
            detailsResource.setBankCountryIso("");

        if (businessPartnerBankDetail.getBankKey() != null)
            detailsResource.setBankKey(businessPartnerBankDetail.getBankKey());
        else
            detailsResource.setBankKey(businessPartnerBankDetail.getBankKey());

        if (businessPartnerBankDetail.getControlKey() != null)
            detailsResource.setControlKey(businessPartnerBankDetail.getControlKey());
        else
            detailsResource.setControlKey("");
        if (businessPartnerBankDetail.getAccountNumber() != null){
            detailsResource.setBankAccountNumber(businessPartnerBankDetail.getAccountNumber());
        }else detailsResource.setBankAccountNumber("");

        if (businessPartnerBankDetail.getReferenceNumber() != null)
            detailsResource.setReferenceNumber(businessPartnerBankDetail.getReferenceNumber());
        else
            detailsResource.setReferenceNumber("");

        if (businessPartnerBankDetail.getAccountHolderName() != null)
            detailsResource.setAccountHolderName(businessPartnerBankDetail.getAccountHolderName());
        else detailsResource.setAccountHolderName("");

        if (businessPartnerBankDetail.getCollectionAuthorization() != null)
            detailsResource.setCollectionAuthorization(businessPartnerBankDetail.getCollectionAuthorization());
        else detailsResource.setCollectionAuthorization("");

        if(businessPartnerBankDetail.getExternalBankDetailId() != null)
            detailsResource.setExternalBankDetailId(businessPartnerBankDetail.getExternalBankDetailId());
        else detailsResource.setExternalBankDetailId("");

        if(businessPartnerBankDetail.getBankAccountName() != null)
            detailsResource.setBankAccountName(businessPartnerBankDetail.getBankAccountName());
        else detailsResource.setBankAccountName("");

        if(businessPartnerBankDetail.getIBan() != null)
            detailsResource.setIban(businessPartnerBankDetail.getIBan());
        else detailsResource.setIban("");

        if (businessPartnerBankDetail.getValidFromDate() != null){
            detailsResource.setValidFromDate(dataConversionUtility.convertDateToSAPFormat(businessPartnerBankDetail.getValidFromDate()));
        } else
            detailsResource.setValidFromDate(null);

        if (businessPartnerBankDetail.getValidToDate() != null){
            detailsResource.setValidToDate(dataConversionUtility.convertDateToSAPFormat(businessPartnerBankDetail.getValidToDate()));
        } else
            detailsResource.setValidToDate(null);

        if (businessPartnerBankDetail.getIBanFromDate() != null){
            detailsResource.setIbanFromDate(dataConversionUtility.convertDateToSAPFormat(businessPartnerBankDetail.getIBanFromDate()));
        } else
            detailsResource.setIbanFromDate(null);

        if (businessPartnerBankDetail.getMoveDate() != null){
            detailsResource.setMoveDate(dataConversionUtility.convertDateToSAPFormat(businessPartnerBankDetail.getMoveDate()));
        } else
            detailsResource.setMoveDate(null);

        if (businessPartnerBankDetail.getMoveId() != null){
            detailsResource.setMoveId( businessPartnerBankDetail.getMoveId());
        } else
            detailsResource.setMoveId(null);

        detailsResource.setEntityId(businessPartnerBankDetail.getId().toString());

        return detailsResource;
    }


}
