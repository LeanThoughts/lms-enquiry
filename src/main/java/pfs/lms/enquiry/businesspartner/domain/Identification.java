package pfs.lms.enquiry.businesspartner.domain;

import lombok.*;
import pfs.lms.enquiry.domain.AggregateRoot;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class Identification extends AggregateRoot<Identification> implements Cloneable{

    @OneToOne
    @JoinColumn(name = "identification_category_id")
    private  IdentificationCategory identificationCategory;

    private String identificationNumber;
    private String idInstitute;
    private LocalDate idEntryDate;
    private LocalDate idValidFromDate;
    private LocalDate idValidToDate;
    private String country;
    private String countryIso;
    private String region;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }

}
