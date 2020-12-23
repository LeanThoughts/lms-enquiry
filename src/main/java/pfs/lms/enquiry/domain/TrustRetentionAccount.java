package pfs.lms.enquiry.domain;

import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class TrustRetentionAccount extends AbstractEntity {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private LoanMonitor loanMonitor;

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
}
