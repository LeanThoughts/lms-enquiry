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
public class DocumentType implements Cloneable{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private String code;

    private String description;


    private String businessObjectId;


    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
