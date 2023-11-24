package pfs.lms.enquiry.iccapproval.iccreasonfordelay;

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
public class ICCReasonForDelayResource {

    private UUID id;
    private UUID loanApplicationId;

    private String reasonForDelay;
    private LocalDate date;
}
