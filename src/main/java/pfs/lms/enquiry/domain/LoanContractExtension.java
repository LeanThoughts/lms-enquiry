package pfs.lms.enquiry.domain;

import lombok.*;
import org.assertj.core.data.Percentage;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class LoanContractExtension extends AggregateRoot<LoanContractExtension> {

    @NotNull
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private LoanApplication loanApplication;

    private String boardMeetingNumber;

    private LocalDate boardApprovalDate;

    private String loanNumber;

    private LocalDate sanctionLetterDate;

    private LocalDate loanDocumentationDate;

    private LocalDate firstDisbursementDate;

    private Double sanctionAmount;

    private String disbursementStatus;

    private LocalDate scheduledCOD;
    private LocalDate actualCOD;

//  Percentage rate for condition items
    private Double baseRate;
//  Reference Interest Rate
    private String referenceRateType;
//  Frequency of change in Base rate
    private String frequenceOfChangeInBaseRate;
//  Applicable Spread (actual charged)
    private Double applicableSpread;
//  Spread - Fixed/ Floating
    private Double spreadFixedFloating;
//  Spread as per PFS BR (Should be-as per policy)
    private Double spreadPFSBR;
//  Applicable ROI as on Date
    private Double applicableROI;

//    Interest Period Start Date
    private LocalDate interestPeriodStartDate;
//    Interest Period Unit
    private Integer interestPeriodUnit;
//    Interest Period Frequency
    private Integer interestPeriodFrequency;
//    Next Interest Reset Date
    private LocalDate nextInterestResetDate;
//    Interest Rate Pre Sanc
    private Double interestRatePreSanction;
//    Interest Rate Post Sanc
    private Double interestRatePostSanction;


}
