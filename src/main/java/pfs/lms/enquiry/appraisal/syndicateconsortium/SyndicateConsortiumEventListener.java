package pfs.lms.enquiry.appraisal.syndicateconsortium;

import lombok.extern.slf4j.Slf4j;
import pfs.lms.enquiry.appraisal.loanpartner.LoanPartner;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

@Slf4j
public class SyndicateConsortiumEventListener {

    @PostPersist
    public void handleSyndicateConsortiumAfterCreate(LoanPartner loanPartner) {
        log.info("Inside loan partner after create");
    }

    @PostUpdate
    public void handleSyndicateConsortiumAfterUpdate(LoanPartner loanPartner) {
    }

    @PostRemove
    public void handleSyndicateConsortiumAfterDelete(LoanPartner loanPartner) {
    }
}
