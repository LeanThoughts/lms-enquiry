package pfs.lms.enquiry.businesspartner.batch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SAPBusinessPartnerBankDetailResourceDetail {

    @JsonProperty(value = "Businesspartner")
    private String busPartnerNumber;



    @JsonProperty(value = "Bankdetailid")
    private String bankDetailId;

    @JsonProperty(value = "Externalbankdetailid")
    private String externalBankDetailId;

    @JsonProperty(value = "BankCtry")
    private String bankCountry;

    @JsonProperty(value = "BankCtryiso")
    private String bankCountryIso;

    @JsonProperty(value = "BankKey")
    private String bankKey;

    @JsonProperty(value = "BankAcct")
    private String bankAccountNumber;

    @JsonProperty(value = "CtrlKey")
    private String controlKey;

    @JsonProperty(value = "BankRef")
    private String referenceNumber;

    @JsonProperty(value = "Accountholder")
    private String accountHolderName;

    @JsonProperty(value = "CollAuth")
    private String collectionAuthorization;

    @JsonProperty(value = "Externalbankid")
    private String externalBankId;

    @JsonProperty(value = "Bankaccountname")
    private String bankAccountName;

    @JsonProperty(value = "Iban")
    private String iban ;

    @JsonProperty(value = "IbanFromDate")
    private String ibanFromDate;

    @JsonProperty(value = "Bankdetailvalidfrom")
    private String validFromDate;

    @JsonProperty(value = "Bankdetailvalidto")
    private String validToDate;
    @JsonProperty(value = "Bankdetailmovedate")
    private String moveDate;

    @JsonProperty(value = "Bankdetailmoveid")
    private String moveId;

    @JsonProperty(value = "BankAccountType")
    private String accountType;


    @JsonProperty(value = "EntityId")
    private String entityId;
}
