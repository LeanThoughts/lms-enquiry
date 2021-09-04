package pfs.lms.enquiry.authorization.domain;

import lombok.*;
import org.springframework.lang.Nullable;
import pfs.lms.enquiry.domain.AuditModel;
import pfs.lms.enquiry.domain.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@ToString
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class AuthorizationObject extends AuditModel {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String businessProcessName; //i.e Monitoring, Appraisal, etc.

    private String authorizationObject;


}
