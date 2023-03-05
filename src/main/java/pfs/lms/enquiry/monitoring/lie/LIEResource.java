package pfs.lms.enquiry.monitoring.lie;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import pfs.lms.enquiry.monitoring.lie.LendersIndependentEngineer;


import java.util.UUID;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class LIEResource {
    private String moduleName;
    private UUID loanApplicationId;
    private LendersIndependentEngineer lendersIndependentEngineer;
}
