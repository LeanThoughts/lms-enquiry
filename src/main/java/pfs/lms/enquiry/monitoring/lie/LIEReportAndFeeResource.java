package pfs.lms.enquiry.monitoring.lie;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import pfs.lms.enquiry.monitoring.lie.LIEReportAndFee;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class LIEReportAndFeeResource {
    private String moduleName;
    private String lendersIndependentEngineerId;
    private LIEReportAndFee lieReportAndFee;
}
