package pfs.lms.enquiry.appraisal.loanappraisalkyc;

import lombok.*;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.domain.AggregateRoot;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"loanAppraisal", "serialNumber", "partnerType", "kycType"}, callSuper = false)
public class LoanAppraisalKYC extends AggregateRoot<LoanAppraisalKYC> implements Cloneable {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private LoanAppraisal loanAppraisal;

    private Integer serialNumber;

    private String partnerType;
    private String kycType;
    private String status;

    private LocalDate dateOfCompletion;

    private String remarks;
    private String documentName;
    private String fileReference;

    private LocalDate uploadDate;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
