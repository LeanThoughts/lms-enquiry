package pfs.lms.enquiry.appraisal.projectdata;

import lombok.*;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.domain.AggregateRoot;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(ProjectDataEntityListener.class)
public class ProjectData extends AggregateRoot<ProjectData> implements Cloneable {

    @OneToOne(fetch = FetchType.EAGER)
    private LoanAppraisal loanAppraisal;

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

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
