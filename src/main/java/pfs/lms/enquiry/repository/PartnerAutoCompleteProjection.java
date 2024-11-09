package pfs.lms.enquiry.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartnerAutoCompleteProjection {
    private String partyName1;
    private String partyName2;
    private String partyNumber;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String street;
    private UUID id;
}
