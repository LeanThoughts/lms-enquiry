package pfs.lms.enquiry.iccapproval.furtherdetail;

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
public class FurtherDetailResource {

    private UUID id;
    private UUID loanApplicationId;

    private String furtherDetails;
    private LocalDate date;
}
