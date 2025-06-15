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
public class BusinessPartnerType implements Cloneable{

    @Id
    private String code;

    private String value;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
