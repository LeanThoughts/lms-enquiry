package pfs.lms.enquiry.boardapproval.rejectedbycustomer;

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
public class BoardApprovalRejectedByCustomerResource {

    private UUID id;
    private UUID loanApplicationId;

    private String approvalByBoardMeetingNumber;
    private LocalDate meetingDate;
    private String rejectionCategory;
    private String details;
}
