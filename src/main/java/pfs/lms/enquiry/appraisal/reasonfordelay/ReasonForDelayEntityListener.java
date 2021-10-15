package pfs.lms.enquiry.appraisal.reasonfordelay;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

@Slf4j
public class ReasonForDelayEntityListener {

    @PostPersist
    public void handleReasonForDelayAfterCreate(ReasonForDelay reasonForDelay) {
        log.info("Inside loan partner after create");
    }

    @PostUpdate
    public void handleReasonForDelayAfterUpdate(ReasonForDelay reasonForDelay) {
    }
}
