package pfs.lms.enquiry.businesspartner.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CountryCode implements Cloneable{

    @Id
    @Column(name = "code", nullable = false)
    private String code;

    private String value;


    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
