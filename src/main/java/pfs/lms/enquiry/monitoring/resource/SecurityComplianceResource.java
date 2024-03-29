package pfs.lms.enquiry.monitoring.resource;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import pfs.lms.enquiry.monitoring.domain.SecurityCompliance;

import java.util.UUID;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class SecurityComplianceResource {

    private UUID loanApplicationId;
    private SecurityCompliance securityCompliance;
}
