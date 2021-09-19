package pfs.lms.enquiry.appraisal.loanappraisalkyc;

import lombok.extern.slf4j.Slf4j;
import pfs.lms.enquiry.appraisal.loanpartner.LoanPartner;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

@Slf4j
public class KnowYourCustomerEventListener {

    @PostPersist
    public void handleKnowYourCustomerAfterCreate(LoanPartner loanPartner) {
        log.info("Inside loan partner after create");
    }

    @PostUpdate
    public void handleKnowYourCustomerAfterUpdate(LoanPartner loanPartner) {
    }

    @PostRemove
    public void handleKnowYourCustomerAfterDelete(LoanPartner loanPartner) {
    }
}
