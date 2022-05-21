package pfs.lms.enquiry.appraisal.loanpartner;

import lombok.*;
import pfs.lms.enquiry.domain.AggregateRoot;
import pfs.lms.enquiry.domain.LoanApplication;

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
@EqualsAndHashCode(of = {"loanApplication", "businessPartnerId", "roleType"}, callSuper = false)
public class LoanPartner extends AggregateRoot<LoanPartner> implements Cloneable {

    @ManyToOne(fetch = FetchType.EAGER)
    private LoanApplication loanApplication;

    private String loanAppraisalId;

    private Integer serialNumber;

    private String businessPartnerId;
    private String businessPartnerName;
    private String roleType;
    private String roleDescription;
    private String kycStatus;
    private boolean kycRequired;
    private LocalDate startDate;
    private LocalDate endDate;



}
