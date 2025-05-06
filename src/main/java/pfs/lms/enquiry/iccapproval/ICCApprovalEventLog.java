package pfs.lms.enquiry.iccapproval;

import lombok.*;
import pfs.lms.enquiry.domain.AggregateRoot;
import pfs.lms.enquiry.iccapproval.loanenhancement.LoanEnhancement;

import java.time.LocalDate;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ICCApprovalEventLog extends AggregateRoot<LoanEnhancement> {

    private ICCApproval iccApproval;

    private LocalDate eventDate;

    private String eventType;
    private String meetingNumber;
    private String remarks;
}
