package pfs.lms.enquiry.action.rejectbypfs;

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
public class RejectByPfsResource {

    private UUID id;
    private UUID loanApplicationId;

    private String rejectionCategory;
    private String rejectionReason;
    private LocalDate rejectionDate;
}
