package pfs.lms.enquiry.iccapproval.approvalbyicc;

import lombok.*;
import pfs.lms.enquiry.domain.AggregateRoot;
import pfs.lms.enquiry.iccapproval.ICCApproval;

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
@EqualsAndHashCode(of = {"iccApproval", "meetingNumber", "meetingDate"}, callSuper = false)
public class ApprovalByICC extends AggregateRoot<ApprovalByICC> implements Cloneable {

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    private ICCApproval iccApproval;

    private String meetingNumber;
    private String remarks;
    private LocalDate meetingDate;
    private LocalDate edApprovalDate;
    private LocalDate cfoApprovalDate;

    private String documentTypeMinutes;
    private String fileReference1;

    private String documentTypeMailFromCS;
    private String fileReference2;

    private Double iccApprovedRoi;
    private Double amountApproved;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
