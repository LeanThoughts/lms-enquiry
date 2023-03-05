package pfs.lms.enquiry.monitoring.llc;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.UUID;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class LLCResource {
    private String moduleName;
    private UUID loanApplicationId;
    private LendersLegalCouncil lendersLegalCouncil;
}
