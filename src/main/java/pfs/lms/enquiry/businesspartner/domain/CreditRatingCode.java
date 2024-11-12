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
public class CreditRatingCode implements Cloneable{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private String code;

    private String value;



    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
