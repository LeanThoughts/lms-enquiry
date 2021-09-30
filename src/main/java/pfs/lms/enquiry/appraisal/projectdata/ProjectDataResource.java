package pfs.lms.enquiry.appraisal.projectdata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ProjectDataResource {

    private UUID id;
    private UUID loanApplicationId;

    private String projectName;
    private String typeOfFunding;
    private String policyApplicable;
    private Double projectCapacity;
    private String projectCapacityUnit;
    private Integer numberOfUnits;
    private String projectType;
    private String mainContractor;
    private String epcContractor;
    private String resourceAssemblyAgency;
}
