package pfs.lms.enquiry.bmcapproval.bmcapprovalbyicc;

import lombok.*;
import pfs.lms.enquiry.bmcapproval.BMCICCApproval;
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
@EqualsAndHashCode(of = {"bmcICCApproval", "meetingNumber", "meetingDate"}, callSuper = false)
public class BMCApprovalByICC extends AggregateRoot<BMCApprovalByICC> implements Cloneable {

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    private BMCICCApproval bmcICCApproval;

    private String meetingNumber;
    private String remarks;
    private LocalDate meetingDate;
    private LocalDate edApprovalDate;
    private LocalDate cfoApprovalDate;

    private String documentTypeMinutes;
    private String fileReference1;

    private String documentTypeMailFromCS;
    private String fileReference2;
    
    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
