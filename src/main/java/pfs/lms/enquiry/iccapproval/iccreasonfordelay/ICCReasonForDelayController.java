package pfs.lms.enquiry.iccapproval.iccreasonfordelay;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Slf4j
@RepositoryRestController
@RequiredArgsConstructor
public class ICCReasonForDelayController {

    private final IICCReasonForDelayService iiccReasonForDelayService;

    @PostMapping("/iCCReasonForDelays/create")
    public ResponseEntity<ICCReasonForDelay> create(@RequestBody ICCReasonForDelayResource iccReasonForDelayResource,
                                                    HttpServletRequest request) {

        return ResponseEntity.ok(iiccReasonForDelayService.create(iccReasonForDelayResource,
                request.getUserPrincipal().getName()));
    }

    @PutMapping("/iCCReasonForDelays/update")
    public ResponseEntity<ICCReasonForDelay> update(@RequestBody ICCReasonForDelayResource iccReasonForDelayResource,
                                                    HttpServletRequest request) throws CloneNotSupportedException {

        return ResponseEntity.ok(iiccReasonForDelayService.update(iccReasonForDelayResource,
                request.getUserPrincipal().getName()));
    }

    @DeleteMapping("/iCCReasonForDelays/delete/{id}")
    public ResponseEntity<ICCReasonForDelay> delete(@PathVariable("id") UUID iccFurtherDetailId, HttpServletRequest request) {
        ICCReasonForDelay iccReasonForDelay = iiccReasonForDelayService.delete(iccFurtherDetailId,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(iccReasonForDelay);
    }
}
