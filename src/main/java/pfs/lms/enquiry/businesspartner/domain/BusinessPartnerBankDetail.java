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
public class BusinessPartnerBankDetail extends AggregateRoot<BusinessPartnerIndustry> implements Cloneable{

    @ManyToOne
    @JoinColumn(name = "partner_id")
    Partner partner;

    private Integer bankDetailId;
    private String externalBankDetailId;
    private String bankCountry;
    private String bankKey;
    private String bankCountryIso;
    private String bankAccountNumber;
    private String controlKey;
    private String referenceNumber;
    private String accountHolderName;
    private String collectionAuthorization;
    private String bankAccountName;
    private String iBan;
    private LocalDate iBanFromDate;
    private LocalDate validFromDate;
    private LocalDate validToDate;
    private LocalDate moveDate;
    private String moveId;
    private String accountType;


    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
