package pfs.lms.enquiry.appraisal.riskrating;

import lombok.*;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.domain.AggregateRoot;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ExternalRating extends AggregateRoot<ExternalRating> implements Cloneable {

    @OneToOne(fetch = FetchType.EAGER)
    private LoanAppraisal loanAppraisal;

    private Integer serialNumber;

    private LocalDate validityDate;
    private String rating;
    private String ratingAgency;
    private String creditStanding;
    private String creditStandingInstruction;
    private String creditStandingText;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
