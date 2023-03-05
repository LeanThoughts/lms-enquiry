package pfs.lms.enquiry.monitoring.lfa;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import pfs.lms.enquiry.monitoring.lfa.LFAReportAndFee;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class LFAReportAndFeeResource {
    private String moduleName;
    private String lendersFinancialAdvisorId;
    private LFAReportAndFee lfaReportAndFee;
}
