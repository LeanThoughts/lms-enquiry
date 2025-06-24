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

public class SAPBusinessPartnerBasicDetailsResourceDetail {

    @JsonProperty(value = "BusPartnerNumber")
    private String busPartnerNumber;

    @JsonProperty(value = "PartnerCategory")
    private String partnerCategory;

    @JsonProperty(value = "PartnerType")
    private String partnerType;

    @JsonProperty(value = "PartnerGroup")
    private String partnerGroup;

    @JsonProperty(value = "Title")
    private String title;

    @JsonProperty(value = "PartnerExternalNumber")
    private String partnerExternalNumber;

    @JsonProperty(value = "PartnerRole")
    private String partnerRole;

    @JsonProperty(value = "Name1")
    private String name1;

    @JsonProperty(value = "Name2")
    private String name2;

    @JsonProperty(value = "Firstname")
    private String firstname;

    @JsonProperty(value = "Lastname")
    private String lastname;

    @JsonProperty(value = "Email")
    private String email;

    @JsonProperty(value = "City")
    private String city;

    @JsonProperty(value = "District")
    private String district;

    @JsonProperty(value = "State")
    private String state;


    @JsonProperty(value = "PostalCode")
    private String postalCode;

    @JsonProperty(value = "HouseNo")
    private String houseNo;

//    @JsonProperty(value = "Street")
//    private String street;

    @JsonProperty(value = "AddressLine1")
    private String addressLine1;
    @JsonProperty(value = "AddressLine2")
    private String addressLine2;
    @JsonProperty(value = "AddressLine3")
    private String addressLine3;


    @JsonProperty(value = "AddressValidFromDate")
    private String addressValidFromDate;

    @JsonProperty(value = "Country")
    private String country;

    @JsonProperty(value = "ContactPerName")
    private String contactPerName;

    @JsonProperty(value = "ContactNumber" )
    private String contactNumber;

    @JsonProperty(value = "SearchTerm1")
    private String searchTerm1;

    @JsonProperty(value = "SearchTerm2")
    private String searchTerm2;

    @JsonProperty(value ="Role")
    private String role;

//    @JsonProperty(value ="LegalForm")
//    private String legalForm;

//    @JsonProperty(value ="LegalOrg")
//    private String legalEntity;

    @JsonProperty(value = "HouseBank")
    private String houseBank;


    @JsonProperty(value = "EntityId")
    private String entityId;

}
