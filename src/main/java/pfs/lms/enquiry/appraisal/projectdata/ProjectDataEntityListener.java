package pfs.lms.enquiry.appraisal.projectdata;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

@Slf4j
public class ProjectDataEntityListener {

    @PostPersist
    public void handleProjectDataAfterCreate(ProjectData projectData) {
        log.info("Inside loan partner after create");
    }

    @PostUpdate
    public void handleProjectDataAfterUpdate(ProjectData projectData) {
    }
}
