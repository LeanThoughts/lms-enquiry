package pfs.lms.enquiry.iccapproval.iccfurtherdetail;

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
public class ICCFurtherDetailController {

    private final IICCFurtherDetailService iiccFurtherDetailService;

    @PreAuthorize("hasAuthority('LoanContracts.IccApproval.Edit')")
    @PostMapping("/iCCFurtherDetails/create")
    public ResponseEntity<ICCFurtherDetail> create(@RequestBody ICCFurtherDetailResource iccFurtherDetailResource,
                                                   HttpServletRequest request) {

        return ResponseEntity.ok(iiccFurtherDetailService.create(iccFurtherDetailResource,
                request.getUserPrincipal().getName()));
    }

    @PreAuthorize("hasAuthority('LoanContracts.IccApproval.Edit')")
    @PutMapping("/iCCFurtherDetails/update")
    public ResponseEntity<ICCFurtherDetail> update(@RequestBody ICCFurtherDetailResource iccFurtherDetailResource,
                                                   HttpServletRequest request) throws CloneNotSupportedException {

        return ResponseEntity.ok(iiccFurtherDetailService.update(iccFurtherDetailResource,
                request.getUserPrincipal().getName()));
    }

    @PreAuthorize("hasAuthority('LoanContracts.IccApproval.Edit')")
    @DeleteMapping("/iCCFurtherDetails/delete/{id}")
    public ResponseEntity<ICCFurtherDetail> delete(@PathVariable("id") UUID iccFurtherDetailId, HttpServletRequest request) {
        ICCFurtherDetail iccFurtherDetail = iiccFurtherDetailService.delete(iccFurtherDetailId,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(iccFurtherDetail);
    }
}
