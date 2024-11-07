package pfs.lms.enquiry.controller;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
