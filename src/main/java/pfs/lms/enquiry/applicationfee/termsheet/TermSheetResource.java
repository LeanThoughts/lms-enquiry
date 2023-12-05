package pfs.lms.enquiry.applicationfee.termsheet;

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
public class TermSheetResource {

    private UUID id;
    private UUID loanApplicationId;

    private Integer serialNumber;
    private String status;
    private LocalDate issuanceDate;
    private LocalDate acceptanceDate;
    private String fileReference;
}
