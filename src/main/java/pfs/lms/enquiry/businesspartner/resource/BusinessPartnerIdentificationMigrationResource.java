package pfs.lms.enquiry.businesspartner.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessPartnerIdentificationMigrationResource {

    private String id;

    private String partnerId;

    private String identificationCategory;

    private Integer serialNumber;
    private String identificationNumber;
    private String idInstitute;
    private LocalDate idEntryDate;
    private LocalDate idValidFromDate;
    private LocalDate idValidToDate;
    private String country;
    private String countryIso;
    private String region;
    private String fileReference;
    private String documentName;
    private String documentType;
}
