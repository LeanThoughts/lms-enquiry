package pfs.lms.enquiry.action.otherdetail;

import lombok.*;
import pfs.lms.enquiry.action.EnquiryAction;
import pfs.lms.enquiry.domain.AggregateRoot;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"enquiryAction"}, callSuper = false)
public class OtherDetail extends AggregateRoot<OtherDetail> implements Cloneable {

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    private EnquiryAction enquiryAction;

    private String nameOfSourcingCompany;
    private String contactPersonName;
    private String contactNumber;
    private String email;
    private LocalDate enquiryDate;
    private String rating;
    private String creditStanding;
    private String creditStandingInstruction;
    private String creditStandingText;
    private LocalDate ratingDate;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
