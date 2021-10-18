package pfs.lms.enquiry.appraisal.projectappraisalcompletion;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

@Slf4j
public class ProjectAppraisalCompletionEntityListener {

    @PostPersist
    public void handleProjectAppraisalCompletionAfterCreate(ProjectAppraisalCompletion projectAppraisalCompletion) {
        log.info("Inside loan partner after create");
    }

    @PostUpdate
    public void handleProjectAppraisalCompletionAfterUpdate(ProjectAppraisalCompletion projectAppraisalCompletion) {
    }
}
