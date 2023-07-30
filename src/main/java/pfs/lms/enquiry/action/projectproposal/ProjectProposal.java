package pfs.lms.enquiry.action.projectproposal;

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
public class ProjectProposal extends AggregateRoot<ProjectProposal> implements Cloneable {

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    private EnquiryAction enquiryAction;

    private String loanEnquiryNumber;
    private String additionalDetails;
    private LocalDate proposalFormSharingDate;
    private String documentName;
    private String documentType;
    private String documentVersion;
    private String proposalStatus;

    private Integer serialNumber;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
