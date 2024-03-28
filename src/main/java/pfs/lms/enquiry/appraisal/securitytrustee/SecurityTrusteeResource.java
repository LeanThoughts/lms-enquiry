package pfs.lms.enquiry.appraisal.securitytrustee;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.UUID;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SecurityTrusteeResource {
    private String moduleName;
    private UUID loanApplicationId;
    private SecurityTrustee securityTrustee;
}
