package pfs.lms.enquiry.domain;

import lombok.*;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class Rating extends AggregateRoot<FeeType>
{
    private String code;
    private String value;
}
