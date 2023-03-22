package pfs.lms.enquiry.monitoring.valuer;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.UUID;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class ValuerResource {
    private String moduleName;
    private UUID loanApplicationId;
    private Valuer valuer;
}
