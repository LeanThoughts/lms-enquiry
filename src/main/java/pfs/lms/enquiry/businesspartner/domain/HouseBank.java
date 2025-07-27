package pfs.lms.enquiry.businesspartner.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public  class HouseBank {
    @Id
    @Column(name = "houseBankId", nullable = false)
    private String houseBankId;

    private String bankCountryKey;
    private String bankKey;
    private String firstTelephoneNumber;
    private String taxNumber1;
    private String nameOfContactPerson;
    private String languageKey;
    private String description;


}