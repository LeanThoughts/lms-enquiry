package pfs.lms.enquiry.monitoring.insurance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class InsuranceResource {

    private UUID loanApplicationId;
    private Insurance insurance;
}
