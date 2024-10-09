package pfs.lms.enquiry.riskassessment.preliminaryriskassessment;

import lombok.*;
import pfs.lms.enquiry.domain.AggregateRoot;
import pfs.lms.enquiry.riskassessment.RiskAssessment;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"riskAssessment", "dateOfAssessment", "mdApprovalDate"}, callSuper = false)
public class PreliminaryRiskAssessment extends AggregateRoot<PreliminaryRiskAssessment> implements Cloneable {

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    private RiskAssessment riskAssessment;

    private LocalDate dateOfAssessment;
    private String remarksByRiskDepartment;
    private LocalDate mdApprovalDate;
    private String remarks;

    private String documentTitle;
    private String documentType;
    private String fileReference;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
