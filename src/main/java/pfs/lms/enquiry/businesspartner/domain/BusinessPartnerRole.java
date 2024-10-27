package pfs.lms.enquiry.businesspartner.domain;

import lombok.*;
import pfs.lms.enquiry.domain.AggregateRoot;
import pfs.lms.enquiry.domain.Partner;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class BusinessPartnerRole extends AggregateRoot<BusinessPartnerRole> implements Cloneable{

    @ManyToOne
    @JoinColumn(name = "partner_id")
    Partner partner;

    @OneToOne
    @JoinColumn(name = "role_type_id")
    private BusinessPartnerRoleType roleType ;

    private String differentiationType;
    private String allPartnerRoles;
    private LocalDate validFromDate;
    private LocalDate validToDate;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
