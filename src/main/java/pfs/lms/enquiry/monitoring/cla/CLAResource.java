package pfs.lms.enquiry.monitoring.cla;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.UUID;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class CLAResource {

    private UUID loanApplicationId;
    private CommonLoanAgreement commonLoanAgreement;
}
