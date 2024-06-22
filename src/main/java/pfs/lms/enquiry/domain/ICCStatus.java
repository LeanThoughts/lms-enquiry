package pfs.lms.enquiry.domain;

import lombok.*;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode()
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class ICCStatus extends AggregateRoot<ICCStatus> {

    private String code;

    private String value;
}