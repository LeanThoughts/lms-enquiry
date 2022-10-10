package pfs.lms.enquiry.action.otherdetail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class OtherDetailResource {

    private UUID id;
    private UUID loanApplicationId;

    private String nameOfSourcingCompany;
    private String contactPersonName;
    private String contactNumber;
    private String email;
    private LocalDate enquiryDate;
    private String rating;
    private String creditStanding;
    private String creditStandingInstruction;
    private String creditStandingText;
    private LocalDate ratingDate;
}
