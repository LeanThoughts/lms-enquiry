package pfs.lms.enquiry.businesspartner.domain;

import lombok.*;
import pfs.lms.enquiry.domain.AggregateRoot;
import pfs.lms.enquiry.domain.Partner;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class BusinessPartnerIndustry extends AggregateRoot<BusinessPartnerIndustry> implements Cloneable{

    @ManyToOne
    Partner partner;

    private Integer serialNumber;

    private Long industrySystemId;
    private Long industryTypeId;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
