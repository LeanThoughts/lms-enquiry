package pfs.lms.enquiry.documentation.nodalofficer;

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
public class NodalOfficerResource {

    private UUID id;
    private UUID loanApplicationId;

    private Integer serialNumber;

    private String bpCode;
    private String name;

    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean deleteFlag;
}
