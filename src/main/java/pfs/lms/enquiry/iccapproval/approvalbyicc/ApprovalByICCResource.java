package pfs.lms.enquiry.iccapproval.approvalbyicc;

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
public class ApprovalByICCResource {

    private UUID id;
    private UUID loanApplicationId;

    private String meetingNumber;
    private String remarks;
    private LocalDate meetingDate;
    private LocalDate edApprovalDate;
    private LocalDate cfoApprovalDate;

}
