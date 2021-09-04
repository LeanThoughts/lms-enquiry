package pfs.lms.enquiry.authorization.domain;

import lombok.*;
import pfs.lms.enquiry.domain.AuditModel;
import pfs.lms.enquiry.domain.UserRole;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@ToString
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class AuthorizationAccess extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String authorizationObject;
    private String userRoleCode;
    private String userRoleName;


    private Boolean accessAllowed;

}
