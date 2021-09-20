package pfs.lms.enquiry.appraisal.knowyourcustomer;

import lombok.extern.slf4j.Slf4j;
import pfs.lms.enquiry.appraisal.syndicateconsortium.SyndicateConsortium;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

@Slf4j
public class KnowYourCustomerEntityListener {

    @PostPersist
    public void handleKnowYourCustomerAfterCreate(SyndicateConsortium syndicateConsortium) {
        log.info("Inside loan partner after create");
    }

    @PostUpdate
    public void handleKnowYourCustomerAfterUpdate(SyndicateConsortium syndicateConsortium) {
    }

    @PostRemove
    public void handleKnowYourCustomerAfterDelete(SyndicateConsortium syndicateConsortium) {
    }
}
