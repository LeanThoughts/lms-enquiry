package pfs.lms.enquiry.monitoring.resource;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import pfs.lms.enquiry.monitoring.domain.SiteVisit;

import java.util.UUID;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class SiteVisitResource {

    private UUID loanApplicationId;
    private SiteVisit siteVisit;
    private String moduleName;
}
