package pfs.lms.enquiry.iccapproval.loanenhancement;

import lombok.*;
import pfs.lms.enquiry.domain.AggregateRoot;
import pfs.lms.enquiry.iccapproval.ICCApproval;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"iccApproval", "iccMeetingNumber", "serialNumber"}, callSuper = false)
public class LoanEnhancement extends AggregateRoot<LoanEnhancement> implements Cloneable {

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private ICCApproval iccApproval;

    private Integer serialNumber;
    private String iccMeetingNumber;
    private LocalDate iccClearanceDate;
    private Double revisedProjectCost;
    private Double revisedEquity;
    private Double revisedContractAmount;
    private LocalDate revisedCommercialOperationsDate;
    private LocalDate reviseRepaymentStartDate;
    private String remarks;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
