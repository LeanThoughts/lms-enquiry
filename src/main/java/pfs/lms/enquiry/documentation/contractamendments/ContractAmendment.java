package pfs.lms.enquiry.documentation.contractamendments;

import lombok.*;
import pfs.lms.enquiry.businesspartner.domain.AmendmentReason;
import pfs.lms.enquiry.businesspartner.domain.SanctionAuthority;
import pfs.lms.enquiry.documentation.Documentation;
import pfs.lms.enquiry.domain.AggregateRoot;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"documentation", "serialNumber", "approvalDate"}, callSuper = false)
public class ContractAmendment extends AggregateRoot<ContractAmendment> implements Cloneable {

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Documentation documentation;

    @NotNull
    private Integer serialNumber;
    @NotNull
    private LocalDate approvalDate;

    @OneToOne
    @JoinColumn(name = "sanction_authority_code")
    private SanctionAuthority sanctionAuthority;
    @OneToOne
    @JoinColumn(name = "amendment_reason_code")
    private AmendmentReason amendmentReason ;

    private String referenceClausesOfContractAgreement;
    private String remarks;

    private String documentName;
    private String documentType;
    private String fileReference;

    private Boolean deleteFlag;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
