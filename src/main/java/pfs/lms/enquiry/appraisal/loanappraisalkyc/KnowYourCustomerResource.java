package pfs.lms.enquiry.appraisal.loanappraisalkyc;

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
public class KnowYourCustomerResource {

    private UUID id;
    private UUID loanApplicationId;

    private Integer serialNumber;

    private String partnerType;
    private String kycType;
    private String status;

    private LocalDate dateOfCompletion;

    private String remarks;
    private String documentName;
    private String fileReference;

    private LocalDate uploadDate;
}
