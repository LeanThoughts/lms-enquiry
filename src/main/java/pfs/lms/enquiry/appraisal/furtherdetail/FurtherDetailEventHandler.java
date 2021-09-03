package pfs.lms.enquiry.appraisal.furtherdetail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;

@RepositoryEventHandler
@Slf4j
public class FurtherDetailEventHandler {
    @HandleAfterCreate
    public void handleFurtherDetailAfterCreate(FurtherDetail furtherDetail) {
        log.info("Inside Further Detail After Create ....");
    }
}
