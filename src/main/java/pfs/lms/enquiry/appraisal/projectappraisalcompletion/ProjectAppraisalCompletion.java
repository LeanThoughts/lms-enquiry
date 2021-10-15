package pfs.lms.enquiry.appraisal.projectappraisalcompletion;

import lombok.*;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.domain.AggregateRoot;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
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
@EqualsAndHashCode(of = {"loanAppraisal"}, callSuper = false)
@EntityListeners(ProjectAppraisalCompletionEntityListener.class)
public class ProjectAppraisalCompletion extends AggregateRoot<ProjectAppraisalCompletion> implements Cloneable {

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    private LoanAppraisal loanAppraisal;

    private LocalDate dateOfProjectAppraisalCompletion;
    private LocalDate agendaNoteApprovalByDirA;
    private LocalDate agendaNoteApprovalByDirB;
    private LocalDate agendaNoteApprovalByMDAndCEO;
    private LocalDate agendaNoteSubmissionToCoSecy;
    private String remarks;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
