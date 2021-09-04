package pfs.lms.enquiry.appraisal.loanpartner;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

@Slf4j
public class LoanPartnerEventListener {

    @PostPersist
    public void handleLoanPartnerAfterCreate(LoanPartner loanPartner) {
        log.info("Inside loan partner after create");
    }

    @PostUpdate
    public void handleLoanPartnerAfterUpdate(LoanPartner loanPartner) {
    }

    @PostRemove
    public void handleLoanPartnerAfterDelete(LoanPartner loanPartner) {
    }
}
