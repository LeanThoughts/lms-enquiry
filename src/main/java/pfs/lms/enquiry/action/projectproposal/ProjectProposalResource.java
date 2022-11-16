package pfs.lms.enquiry.action.projectproposal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ProjectProposalResource {

    private UUID id;
    private UUID loanApplicationId;
    private UUID enquiryActionId;

    private String loanEnquiryNumber;
    private String additionalDetails;
    private LocalDate proposalFormSharingDate;
    private String documentName;
    private String documentType;
    private String documentVersion;
    private String proposalStatus;

    private DealGuaranteeTimeline dealGuaranteeTimeline;
}
