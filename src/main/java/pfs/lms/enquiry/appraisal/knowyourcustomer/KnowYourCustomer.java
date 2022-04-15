package pfs.lms.enquiry.appraisal.knowyourcustomer;

import lombok.*;
import pfs.lms.enquiry.domain.AggregateRoot;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"loanPartnerId", "documentType"}, callSuper = false)
@EntityListeners(KnowYourCustomerEntityListener.class)
public class KnowYourCustomer extends AggregateRoot<KnowYourCustomer> implements Cloneable {

    private String loanPartnerId;

    private String documentType;
    private String documentName;
    private String remarks;
    private String fileReference;

    private LocalDate dateOfCompletion;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
