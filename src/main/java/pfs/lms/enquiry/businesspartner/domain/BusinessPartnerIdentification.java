package pfs.lms.enquiry.businesspartner.domain;

import lombok.*;
import pfs.lms.enquiry.domain.AggregateRoot;
import pfs.lms.enquiry.domain.Partner;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class BusinessPartnerIdentification extends AggregateRoot<BusinessPartnerIdentification> implements Cloneable{

    @ManyToOne
    @JoinColumn(name = "partner_id")
    Partner partner;

    private Integer serialNumber;
    
    private String identificationCategoryCode;
    private String identificationNumber;
    private String idInstitute;
    private LocalDate idEntryDate;
    private LocalDate idValidFromDate;
    private LocalDate idValidToDate;
    private String country;
    private String countryIso;
    private String region;
    private String fileReference;
    private String documentName;
    private String documentType;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }

}
