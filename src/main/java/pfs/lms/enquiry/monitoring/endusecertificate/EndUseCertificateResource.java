package pfs.lms.enquiry.monitoring.endusecertificate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class EndUseCertificateResource {

    private UUID loanApplicationId;
    private EndUseCertificate endUseCertificate;
}
