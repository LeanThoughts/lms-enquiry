package pfs.lms.enquiry.monitoring.lia;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class LIAReportAndFeeResource {

    private String lendersInsuranceAdvisorId;
    private LIAReportAndFee liaReportAndFee;
}
