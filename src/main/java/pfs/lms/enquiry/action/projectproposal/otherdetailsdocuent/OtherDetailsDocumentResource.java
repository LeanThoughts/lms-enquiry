package pfs.lms.enquiry.action.projectproposal.otherdetailsdocuent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class OtherDetailsDocumentResource {

    private UUID id;
    private UUID projectProposalId;

    private String documentType;
    private String documentName;
    private String fileReference;
}
