package pfs.lms.enquiry.monitoring.tra;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class TRAStatementResource {
    private String moduleName;
    private String trustRetentionAccountId;
    private TrustRetentionAccountStatement trustRetentionAccountStatement;
}
