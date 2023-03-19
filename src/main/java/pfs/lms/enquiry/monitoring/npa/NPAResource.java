package pfs.lms.enquiry.monitoring.npa;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.UUID;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class NPAResource {

    private UUID loanApplicationId;
    private NPA npa;
}
