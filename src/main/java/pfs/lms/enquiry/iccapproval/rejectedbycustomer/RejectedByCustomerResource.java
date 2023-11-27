package pfs.lms.enquiry.iccapproval.rejectedbycustomer;

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
public class RejectedByCustomerResource {

    private UUID id;
    private UUID loanApplicationId;

    private String meetingNumber;
    private String rejectionCategory;
    private LocalDate dateOfRejection;
    private String remarks;
}
