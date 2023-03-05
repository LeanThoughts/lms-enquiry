package pfs.lms.enquiry.monitoring.lfa;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import pfs.lms.enquiry.monitoring.lfa.LendersFinancialAdvisor;

import java.util.UUID;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class LFAResource {
    private String moduleName;
    private UUID loanApplicationId;
    private LendersFinancialAdvisor lendersFinancialAdvisor;
}
