package pfs.lms.enquiry.bmcapproval.bmciccreasonfordelay;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Slf4j
@RepositoryRestController
@RequiredArgsConstructor
public class BMCICCReasonForDelayController {

    private final IBMCICCReasonForDelayService IBMCICCReasonForDelayService;

    @PreAuthorize("hasAuthority('LoanContracts.BmcApproval.Edit')")
    @PostMapping("/bmcIccReasonForDelays/create")
    public ResponseEntity<BmcIccReasonForDelay> create(@RequestBody BMCICCReasonForDelayResource BMCICCReasonForDelayResource,
                                                       HttpServletRequest request) {

        return ResponseEntity.ok(IBMCICCReasonForDelayService.create(BMCICCReasonForDelayResource,
                request.getUserPrincipal().getName()));
    }

    @PreAuthorize("hasAuthority('LoanContracts.BmcApproval.Edit')")
    @PutMapping("/bmcIccReasonForDelays/update")
    public ResponseEntity<BmcIccReasonForDelay> update(@RequestBody BMCICCReasonForDelayResource BMCICCReasonForDelayResource,
                                                       HttpServletRequest request) throws CloneNotSupportedException {

        return ResponseEntity.ok(IBMCICCReasonForDelayService.update(BMCICCReasonForDelayResource,
                request.getUserPrincipal().getName()));
    }

    @PreAuthorize("hasAuthority('LoanContracts.BmcApproval.Edit')")
    @DeleteMapping("/bmcIccReasonForDelays/delete/{id}")
    public ResponseEntity<BmcIccReasonForDelay> delete(@PathVariable("id") UUID iccFurtherDetailId, HttpServletRequest request) {
        BmcIccReasonForDelay BMCICCReasonForDelay = IBMCICCReasonForDelayService.delete(iccFurtherDetailId,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(BMCICCReasonForDelay);
    }
}
