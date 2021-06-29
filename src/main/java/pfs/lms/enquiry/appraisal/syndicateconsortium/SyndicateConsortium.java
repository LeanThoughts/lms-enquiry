package pfs.lms.enquiry.appraisal.syndicateconsortium;

import lombok.*;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.domain.AggregateRoot;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"loanAppraisal", "serialNumber", "bank"}, callSuper = false)
public class SyndicateConsortium extends AggregateRoot<SyndicateConsortium> implements Cloneable {

    @ManyToOne(fetch = FetchType.EAGER)
    private LoanAppraisal loanAppraisal;

    @ManyToOne(fetch = FetchType.EAGER)
    private Bank bank;

    private String bankName;

    private Integer serialNumber;

    private Double sanctionedAmount;
    private Double disbursedAmount;

    private String currency;
    private String approvalStatus;
    private String documentStage;
    private String disbursementStatus;

    private boolean lead;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
