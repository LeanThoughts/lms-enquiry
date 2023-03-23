package pfs.lms.enquiry.monitoring.insurance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class InsuranceResource {

    private UUID id;
    private UUID loanApplicationId;

    private LocalDate validFrom;
    private LocalDate validTo;
    private String documentType;
    private String documentTitle;
    private String fileReference;
}
