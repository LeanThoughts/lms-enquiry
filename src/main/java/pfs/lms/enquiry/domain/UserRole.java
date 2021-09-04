package pfs.lms.enquiry.domain;

import lombok.*;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
//@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UserRole extends AggregateRoot<UserRole> {

    private String code;

    private String value;

    public UserRole() {

    }
}
