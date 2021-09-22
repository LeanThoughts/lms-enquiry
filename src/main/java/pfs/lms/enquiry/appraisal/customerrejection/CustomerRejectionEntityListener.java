package pfs.lms.enquiry.appraisal.customerrejection;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

@Slf4j
public class CustomerRejectionEntityListener {

    @PostPersist
    public void handleCustomerRejectionAfterCreate(CustomerRejection customerRejection) {
        log.info("Inside loan partner after create");
    }

    @PostUpdate
    public void handleCustomerRejectionAfterUpdate(CustomerRejection customerRejection) {
    }
}
