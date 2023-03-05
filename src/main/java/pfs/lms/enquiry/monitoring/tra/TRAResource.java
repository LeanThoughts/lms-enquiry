package pfs.lms.enquiry.monitoring.tra;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.UUID;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class TRAResource {
    private String moduleName;
    private UUID loanApplicationId;
    private TrustRetentionAccount trustRetentionAccount;

}
