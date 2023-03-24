package pfs.lms.enquiry.monitoring.loanDocumentation;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import pfs.lms.enquiry.monitoring.npa.NPA;

import java.util.UUID;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class LoanDocumentationResource {

    private UUID loanApplicationId;
    private LoanDocumentation loanDocumentation;
}
