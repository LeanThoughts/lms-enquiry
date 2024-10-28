package pfs.lms.enquiry.businesspartner.resource;

import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessPartnerBankDetailResource {

    private UUID id;

    private UUID partnerId;

    private Integer bankDetailId;
    private String externalBankDetailId;
    private String bankCountry;
    private String bankKey;
    private String bankCountryIso;
    private String bankAccountNumber;
    private String controlKey;
    private String referenceNumber;
    private String accountHolderName;
    private String collectionAuthorization;
    private String externalBankId;
    private String bankAccountName;
    private String iBan;
    private LocalDate iBanFromDate;
    private LocalDate validFromDate;
    private LocalDate validToDate;
    private LocalDate moveDate;
    private String moveId;
    private String accountType;
}
