package pfs.lms.enquiry.referenceinterest.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReferenceInterestValueDataMigResource {

    String referenceIntRateType;
    String description;
    String validFromDate;
    Double interestRate;
}
