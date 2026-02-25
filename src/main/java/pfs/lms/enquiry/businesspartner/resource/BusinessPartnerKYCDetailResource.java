package pfs.lms.enquiry.businesspartner.resource;

import lombok.*;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerIndustry;
import pfs.lms.enquiry.domain.AggregateRoot;
import pfs.lms.enquiry.domain.Partner;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessPartnerKYCDetailResource extends AggregateRoot<BusinessPartnerIndustry> implements Cloneable{

    private UUID id;

    private UUID partnerId;

    private LocalDate kycDate;
    private String kycRiskCategory;

    private LocalDate reKYCDate;
    private String reKYCRiskCategory;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
