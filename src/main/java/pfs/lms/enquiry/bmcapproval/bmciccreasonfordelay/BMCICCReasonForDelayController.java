package pfs.lms.enquiry.bmcapproval.bmciccreasonfordelay;

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
public class BMCICCReasonForDelayController {

    private final IBMCICCReasonForDelayService IBMCICCReasonForDelayService;

    @PostMapping("/bmcICCReasonForDelays/create")
    public ResponseEntity<BMCICCReasonForDelay> create(@RequestBody BMCICCReasonForDelayResource BMCICCReasonForDelayResource,
                                                       HttpServletRequest request) {

        return ResponseEntity.ok(IBMCICCReasonForDelayService.create(BMCICCReasonForDelayResource,
                request.getUserPrincipal().getName()));
    }

    @PutMapping("/bmcICCReasonForDelays/update")
    public ResponseEntity<BMCICCReasonForDelay> update(@RequestBody BMCICCReasonForDelayResource BMCICCReasonForDelayResource,
                                                       HttpServletRequest request) throws CloneNotSupportedException {

        return ResponseEntity.ok(IBMCICCReasonForDelayService.update(BMCICCReasonForDelayResource,
                request.getUserPrincipal().getName()));
    }

    @DeleteMapping("/bmcICCReasonForDelays/delete/{id}")
    public ResponseEntity<BMCICCReasonForDelay> delete(@PathVariable("id") UUID iccFurtherDetailId, HttpServletRequest request) {
        BMCICCReasonForDelay BMCICCReasonForDelay = IBMCICCReasonForDelayService.delete(iccFurtherDetailId,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(BMCICCReasonForDelay);
    }
}
