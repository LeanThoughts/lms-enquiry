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
public class BusinessPartnerRoleTypePartnerGroup {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    private String roleType;

    private String partnerGroup;

    public BusinessPartnerRoleTypePartnerGroup() {
    }
}
