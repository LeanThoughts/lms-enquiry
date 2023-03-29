package pfs.lms.enquiry.monitoring.tra;

import lombok.*;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.domain.AbstractEntity;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class TrustRetentionAccount extends AbstractEntity implements  Cloneable{

    @ManyToOne(fetch = FetchType.EAGER)
    private LoanMonitor loanMonitor;

    @ManyToOne(fetch = FetchType.EAGER)
    private LoanAppraisal loanAppraisal;

    private Integer serialNumber;

    private String bankKey;

    private String traBankName;

    private String branch;

    private String address;

    private String beneficiaryName;

    private String ifscCode;

    private String accountNumber;

    private String contactName;

    private String typeOfAccount;

    private String contactNumber;

    private String email;

    private String pfsAuthorisedPersonBPCode;

    private String pfsAuthorisedPerson;


    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }

}
