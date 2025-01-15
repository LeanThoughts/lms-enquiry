package pfs.lms.enquiry.referenceinterest.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pfs.lms.enquiry.domain.AggregateRoot;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@ToString
public class ReferenceInterestRateValue extends AggregateRoot<ReferenceInterestRateValue> implements Cloneable{


    @ManyToOne
    @JoinColumn(name = "reference_interest_rate_id")
    private ReferenceInterestRate referenceInterestRate;

    private LocalDate validFromDate;
    private Double interestRate;

    private String createdBy;
    private LocalDate createdOn;

    private String changedBy;
    private LocalDate changedOn;
    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }

}
