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
public class BusinessPartnerIndustry extends AggregateRoot<BusinessPartnerIndustry> implements Cloneable{

    @ManyToOne
    @JoinColumn(name = "partner_id")
    Partner partner;

    @OneToOne
    @JoinColumn(name = "industry_system_id")
    IndustrySystem industrySystem;
    
    @OneToOne
    @JoinColumn(name = "industry_type_id")
    IndustryType industryType;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }

}
