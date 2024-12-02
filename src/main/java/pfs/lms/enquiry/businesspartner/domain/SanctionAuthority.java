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
public  class SanctionAuthority {
    @Id
    @Column(name = "code", nullable = false)
    private String code;

    private String value;
}