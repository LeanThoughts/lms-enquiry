package pfs.lms.enquiry.bmcapproval.bmcloanenhancement;

import lombok.*;
import pfs.lms.enquiry.bmcapproval.BmcIccApproval;
import pfs.lms.enquiry.domain.AggregateRoot;

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
@EqualsAndHashCode(of = {"bmcICCApproval", "iccMeetingNumber", "serialNumber"}, callSuper = false)
public class BmcLoanEnhancement extends AggregateRoot<BmcLoanEnhancement> implements Cloneable {

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private BmcIccApproval bmcICCApproval;

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
