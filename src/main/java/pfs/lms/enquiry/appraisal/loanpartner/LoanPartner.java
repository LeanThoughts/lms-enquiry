package pfs.lms.enquiry.appraisal.loanpartner;

import lombok.*;
import pfs.lms.enquiry.domain.AggregateRoot;
import pfs.lms.enquiry.domain.LoanApplication;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"loanApplication", "businessPartnerId", "roleType"}, callSuper = false)
@EntityListeners(LoanPartnerEntityListener.class)
public class LoanPartner extends AggregateRoot<LoanPartner> implements Cloneable {

    @ManyToOne(fetch = FetchType.EAGER)
    private LoanApplication loanApplication;

    private Integer serialNumber;

    private String businessPartnerId;
    private String businessPartnerName;
    private String roleType;

    private LocalDate startDate;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
