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
public class SortKey implements Cloneable{

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    private String description;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
