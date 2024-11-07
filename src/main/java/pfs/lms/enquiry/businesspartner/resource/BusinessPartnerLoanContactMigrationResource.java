package pfs.lms.enquiry.businesspartner.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessPartnerLoanContactMigrationResource {

    private String id;

    private String partnerId;

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
