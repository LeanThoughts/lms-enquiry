package pfs.lms.enquiry.appraisal.knowyourcustomer;

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
    private String loanPartnerId;

    private String documentType;
    private String documentName;
    private String remarks;
    private String fileReference;

    private LocalDate dateOfCompletion;
}
