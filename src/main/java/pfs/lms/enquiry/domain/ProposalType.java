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
public class ProposalType extends AggregateRoot<ProposalType> {

    private String code;

    private String value;
}
