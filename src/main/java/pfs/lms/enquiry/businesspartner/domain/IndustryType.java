package pfs.lms.enquiry.businesspartner.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
//@NoArgsConstructor(access = AccessLevel.PRIVATE)

public class IndustryType {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private String code;

    private String value;

    @OneToOne
    @JoinColumn(name = "industry_system_id")
    private IndustrySystem industrySystem;

    public IndustryType(Long id, String code, String value, IndustrySystem industrySystem) {
        this.id = id;
        this.code = code;
        this.value = value;
        this.industrySystem = industrySystem;
    }

    public IndustryType() {
    }
}
