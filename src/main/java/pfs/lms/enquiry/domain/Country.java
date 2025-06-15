package pfs.lms.enquiry.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class Country   {

    @Id
    @Column(name = "countryCode", nullable = false)
    private String countryCode;

    private String value;
}
