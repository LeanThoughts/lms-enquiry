package pfs.lms.enquiry.boardapproval.reasonfordelay;

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
public class BoardApprovalReasonForDelayResource {

    private UUID id;
    private UUID loanApplicationId;

    private Integer serialNumber;
    private LocalDate date;
    private String reason;
}
