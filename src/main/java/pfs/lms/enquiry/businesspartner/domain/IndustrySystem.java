package pfs.lms.enquiry.businesspartner.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
//@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class IndustrySystem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private String code;

    private String value;

    public IndustrySystem() {
    }
}
