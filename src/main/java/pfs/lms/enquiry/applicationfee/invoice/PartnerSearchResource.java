package pfs.lms.enquiry.applicationfee.invoice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartnerSearchResource {
    private String partyName1;
    private String partyNumber;
    private String searchTerm1;
    private String searchTerm2;
}
