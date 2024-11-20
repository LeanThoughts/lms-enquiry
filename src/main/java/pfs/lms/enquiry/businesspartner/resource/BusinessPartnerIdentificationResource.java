package pfs.lms.enquiry.businesspartner.resource;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessPartnerIdentificationResource {

    private UUID id;

    private UUID partnerId;

    private String identificationCategoryCode;

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
