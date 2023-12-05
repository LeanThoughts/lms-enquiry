package pfs.lms.enquiry.applicationfee.formalrequest;

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
public class FormalRequestResource {

    private UUID id;
    private UUID loanApplicationId;

    private Integer serialNumber;
    private String documentName;
    private LocalDate uploadDate;
    private LocalDate documentLetterDate;
    private LocalDate documentReceivedDate;
    private String fileReference;
}
