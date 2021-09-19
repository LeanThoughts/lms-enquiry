package pfs.lms.enquiry.appraisal.proposaldetails;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

@Slf4j
public class ProposalDetailEventListener {

    @PostPersist
    public void handleProposalDetailAfterCreate(ProposalDetail proposalDetail) {
        log.info("Inside loan partner after create");
    }

    @PostUpdate
    public void handleProposalDetailAfterUpdate(ProposalDetail proposalDetail) {
    }

    @PostRemove
    public void handleProposalDetailAfterDelete(ProposalDetail proposalDetail) {
    }
}
