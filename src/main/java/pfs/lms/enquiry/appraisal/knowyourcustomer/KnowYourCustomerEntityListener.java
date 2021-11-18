package pfs.lms.enquiry.appraisal.knowyourcustomer;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

@Slf4j
public class KnowYourCustomerEntityListener {

    @PostPersist
    public void handleKnowYourCustomerAfterCreate(KnowYourCustomer knowYourCustomer) {
        log.info("Inside loan partner after create");
    }

    @PostUpdate
    public void handleKnowYourCustomerAfterUpdate(KnowYourCustomer knowYourCustomer) {
    }

    @PostRemove
    public void handleKnowYourCustomerAfterDelete(KnowYourCustomer knowYourCustomer) {
    }
}