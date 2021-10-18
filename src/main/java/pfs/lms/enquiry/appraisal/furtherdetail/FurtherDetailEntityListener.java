package pfs.lms.enquiry.appraisal.furtherdetail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

@RepositoryEventHandler
@Slf4j
public class FurtherDetailEntityListener {

    @PostPersist
    public void handleFurtherDetailAfterCreate(FurtherDetail furtherDetail) {
        log.info("New further detail entry created");
    }

    @PostUpdate
    public void handleFurtherDetailAfterUpdate(FurtherDetail furtherDetail) {
        log.info("Updated existing further detail entry");
    }
}
