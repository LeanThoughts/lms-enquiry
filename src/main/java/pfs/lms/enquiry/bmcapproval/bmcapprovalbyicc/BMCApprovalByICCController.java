package pfs.lms.enquiry.bmcapproval.bmcapprovalbyicc;

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
public class BMCApprovalByICCController {

    private final IBMCApprovalByICCService approvalByICCService;

    @PostMapping("/bmcApprovalByICCs/create")
    public ResponseEntity<BmcApprovalByICC> create(@RequestBody BMCApprovalByICCResource BMCApprovalByIccResource,
                                                   HttpServletRequest request) {

        return ResponseEntity.ok(approvalByICCService.create(BMCApprovalByIccResource,
                request.getUserPrincipal().getName()));
    }

    @PutMapping("/bmcApprovalByICCs/update")
    public ResponseEntity<BmcApprovalByICC> update(@RequestBody BMCApprovalByICCResource BMCApprovalByIccResource,
                                                   HttpServletRequest request) throws CloneNotSupportedException {

        return ResponseEntity.ok(approvalByICCService.update(BMCApprovalByIccResource,
                request.getUserPrincipal().getName()));
    }

    @DeleteMapping("/bmcApprovalByICCs/delete/{id}")
    public ResponseEntity<BmcApprovalByICC> delete(@PathVariable("id") UUID iccFurtherDetailId, HttpServletRequest request) {
        BmcApprovalByICC BMCApprovalByIcc = approvalByICCService.delete(iccFurtherDetailId,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(BMCApprovalByIcc);
    }
}
