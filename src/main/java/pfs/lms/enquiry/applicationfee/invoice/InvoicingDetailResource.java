package pfs.lms.enquiry.applicationfee.invoice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class InvoicingDetailResource {

    private UUID id;
    private UUID loanApplicationId;

    private String iccMeetingNumber;
    private String companyName;
    private String cinNumber;
    private String gstNumber;
    private String pan;
    private String msmeRegistrationNumber;
    private String doorNumber;
    private String address;
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String landline;
    private String mobile;
    private String email;
    private String projectType;
    private String projectCapacityUnit;
    private String projectLocationState;

    private Double pfsDebtAmount;
    private Double projectCapacity;
}
