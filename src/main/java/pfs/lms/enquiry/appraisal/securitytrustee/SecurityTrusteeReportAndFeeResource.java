package pfs.lms.enquiry.appraisal.securitytrustee;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SecurityTrusteeReportAndFeeResource {
    private String moduleName;
    private String securityTrusteeId;
    private SecurityTrusteeReportAndFee securityTrusteeReportAndFee;
}
