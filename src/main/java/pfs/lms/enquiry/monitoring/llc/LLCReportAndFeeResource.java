package pfs.lms.enquiry.monitoring.llc;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class LLCReportAndFeeResource {

    private String lendersLegalCouncilId;
    private LLCReportAndFee llcReportAndFee;
}
