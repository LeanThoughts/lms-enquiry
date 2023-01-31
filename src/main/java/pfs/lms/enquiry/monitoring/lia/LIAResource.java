package pfs.lms.enquiry.monitoring.lia;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.UUID;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class LIAResource {

    private UUID loanApplicationId;
    private LendersInsuranceAdvisor lendersInsuranceAdvisor;
}
