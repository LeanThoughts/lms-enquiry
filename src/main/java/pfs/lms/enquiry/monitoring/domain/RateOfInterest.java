package pfs.lms.enquiry.monitoring.domain;

import lombok.*;
import pfs.lms.enquiry.domain.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class RateOfInterest extends AbstractEntity implements Cloneable {

    @ManyToOne(fetch = FetchType.EAGER)
    private LoanMonitor loanMonitor;

    private Integer serialNumber;

    private String conditionType;
    private LocalDate validFromDate;

    // 0 - Fixed
    // 1 - Reference Int. Rate
    private Character interestTypeIndicator;

    private String referenceInterestRate;

    // +  - Plus
    // -  - Minus
    // *  - Multiply
    private String refInterestSign;

    private Double interestRate;

    private LocalDate calculationDate;

    private Boolean isCalculationDateOnMonthEnd;

    private LocalDate dueDate;

    private Boolean isDueDateOnMonthEnd;

    private Integer interestPaymentFrequency;

    private String paymentForm;

    private String interestCalculationMethod;

    private LocalDate interestPeriodStartDate;
    private LocalDate nextInterestResetDate;
    private Integer interestPeriodUnit;
    private Integer interestPeriodFrequency;

//    private String particulars;
//
//    private String scheduledIfAny;
//
//    private Double sanctionPreCod;
//
//    private Double sanctionPostCod;
//
//    private Double presentRoi;
//
//    private String freeText;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }

}
