package pfs.lms.enquiry.appraisal.loanpartner;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class LoanPartnerResource {

    private UUID id;
    private UUID loanApplicationId;

    private Integer serialNumber;

    private String businessPartnerId;
    private String businessPartnerName;
    private String roleType;
    private String roleDescription;
    private String kycStatus;

    private boolean kycRequired;

    private LocalDate startDate;
}
