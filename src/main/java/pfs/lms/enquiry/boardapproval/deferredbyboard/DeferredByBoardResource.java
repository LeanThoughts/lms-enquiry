package pfs.lms.enquiry.boardapproval.deferredbyboard;

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
public class DeferredByBoardResource {

    private UUID id;
    private UUID loanApplicationId;

    private Integer meetingNumber;
    private LocalDate meetingDate;
    private String details;
}
