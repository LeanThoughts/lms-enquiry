package pfs.lms.enquiry.monitoring.valuer;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class ValuerReportAndFeeResource {

    private String moduleName;
    private String valuerId;
    private ValuerReportAndFee valuerReportAndFee;
}
