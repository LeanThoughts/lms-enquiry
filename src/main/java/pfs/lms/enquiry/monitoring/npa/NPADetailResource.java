package pfs.lms.enquiry.monitoring.npa;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class NPADetailResource {

    private String npaId;
    private NPADetail npaDetail;
}
