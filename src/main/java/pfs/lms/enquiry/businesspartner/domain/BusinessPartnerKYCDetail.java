package pfs.lms.enquiry.businesspartner.domain;

import lombok.*;
import pfs.lms.enquiry.domain.AggregateRoot;
import pfs.lms.enquiry.domain.Partner;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class BusinessPartnerKYCDetail extends AggregateRoot<BusinessPartnerKYCDetail> implements Cloneable{

    @OneToOne
    Partner partner;

    private LocalDate kycDate;
    private String kycRiskCategory;

    private LocalDate reKYCDate;
    private String reKYCRiskCategory;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
