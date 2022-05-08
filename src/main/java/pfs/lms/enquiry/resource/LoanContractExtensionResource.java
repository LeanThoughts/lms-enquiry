package pfs.lms.enquiry.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import pfs.lms.enquiry.appraisal.loanpartner.LoanPartner;
import pfs.lms.enquiry.domain.LoanContractExtension;
import pfs.lms.enquiry.domain.Partner;

import java.util.List;
import java.util.UUID;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class LoanContractExtensionResource {

    private UUID loanApplicationId;
    private LoanContractExtension loanContractExtension;
    private List<LoanPartner> loanPartners;

}
