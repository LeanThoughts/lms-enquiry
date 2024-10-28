package pfs.lms.enquiry.businesspartner.resource;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessPartnerLoanContactResource {

    private UUID id;

    private UUID partnerId;

    private Integer serialNumber;
    private String selection;
    private String loanNumber;
    private String name;
    private String branchAddress;
    private String designation;
    private String department;
    private String telephoneNumber;
    private String landLineNumber;
    private String email;
    private String faxNumber;
}
