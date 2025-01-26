package pfs.lms.enquiry.bmcapproval.bmciccfurtherdetail;

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
public class BMCICCFurtherDetailResource {

    private UUID id;
    private UUID loanApplicationId;

    private Integer serialNumber;
    private String iccMeetingNumber;
    private LocalDate iccMeetingDate;
    private String detailsRequired;
}
