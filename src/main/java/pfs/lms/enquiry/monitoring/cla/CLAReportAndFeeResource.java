package pfs.lms.enquiry.monitoring.cla;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class CLAReportAndFeeResource {

    private String commonLoanAgreementId;
    private CLAReportAndFee claReportAndFee;
}
