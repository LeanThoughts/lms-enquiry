package pfs.lms.enquiry.businesspartner.domain;

import lombok.*;
import pfs.lms.enquiry.domain.AggregateRoot;
import pfs.lms.enquiry.domain.Partner;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class BusinessPartnerLoanContact extends AggregateRoot<BusinessPartnerIndustry> implements Cloneable{

    @ManyToOne
    @JoinColumn(name = "partner_id")
    Partner partner;

    private Integer serialNumber;
    private String selection;
    private String loanNumber;
    private String name;
    private String branchAddress;
    private String designation;
    private String department;
    private String telephoneNumber;
    private String landLineNumber;
    private String email;
    private String faxNumber;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
