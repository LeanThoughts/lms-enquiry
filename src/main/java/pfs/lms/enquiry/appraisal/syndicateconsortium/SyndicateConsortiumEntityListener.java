package pfs.lms.enquiry.appraisal.syndicateconsortium;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

@Slf4j
public class SyndicateConsortiumEntityListener {

    @PostPersist
    public void handleSyndicateConsortiumAfterCreate(SyndicateConsortium syndicateConsortium) {
        log.info("Inside syndicate consortium after create");
    }

    @PostUpdate
    public void handleSyndicateConsortiumAfterUpdate(SyndicateConsortium syndicateConsortium) {
    }

    @PostRemove
    public void handleSyndicateConsortiumAfterDelete(SyndicateConsortium syndicateConsortium) {
        log.info("Inside syndicate consortium after delete");
    }
}
