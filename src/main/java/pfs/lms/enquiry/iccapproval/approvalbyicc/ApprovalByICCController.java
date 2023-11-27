package pfs.lms.enquiry.iccapproval.approvalbyicc;

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
public class ApprovalByICCController {

    private final IApprovalByICCService approvalByICCService;

    @PostMapping("/approvalByICCs/create")
    public ResponseEntity<ApprovalByICC> create(@RequestBody ApprovalByICCResource approvalByIccResource,
                                                HttpServletRequest request) {

        return ResponseEntity.ok(approvalByICCService.create(approvalByIccResource,
                request.getUserPrincipal().getName()));
    }

    @PutMapping("/approvalByICCs/update")
    public ResponseEntity<ApprovalByICC> update(@RequestBody ApprovalByICCResource approvalByIccResource,
                                                HttpServletRequest request) throws CloneNotSupportedException {

        return ResponseEntity.ok(approvalByICCService.update(approvalByIccResource,
                request.getUserPrincipal().getName()));
    }

    @DeleteMapping("/approvalByICCs/delete/{id}")
    public ResponseEntity<ApprovalByICC> delete(@PathVariable("id") UUID iccFurtherDetailId, HttpServletRequest request) {
        ApprovalByICC approvalByIcc = approvalByICCService.delete(iccFurtherDetailId,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(approvalByIcc);
    }
}
