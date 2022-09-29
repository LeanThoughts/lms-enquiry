package pfs.lms.enquiry.action.enquiryactionreasonfordelay;

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
public class EnquiryActionReasonForDelayResource {

    private UUID id;
    private UUID loanApplicationId;

    private String reason;
    private LocalDate date;
}
