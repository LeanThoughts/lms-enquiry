package pfs.lms.enquiry.referenceinterest.resource;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class ReferenceInterestValueResource {


    private UUID id;
    private String referenceInterestRate;

    private LocalDate validFromDate;
    private Double interestRate;

}
