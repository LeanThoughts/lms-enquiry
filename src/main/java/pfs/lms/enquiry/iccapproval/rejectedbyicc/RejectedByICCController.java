package pfs.lms.enquiry.iccapproval.rejectedbyicc;

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
public class RejectedByICCController {

    private final IRejectedByICCService rejectedByICCService;

    @PostMapping("/rejectedByICCs/create")
    public ResponseEntity<RejectedByICC> create(@RequestBody RejectedByICCResource rejectedByIccResource,
                                                HttpServletRequest request) {

        return ResponseEntity.ok(rejectedByICCService.create(rejectedByIccResource,
                request.getUserPrincipal().getName()));
    }

    @PutMapping("/rejectedByICCs/update")
    public ResponseEntity<RejectedByICC> update(@RequestBody RejectedByICCResource rejectedByIccResource,
                                                HttpServletRequest request) throws CloneNotSupportedException {

        return ResponseEntity.ok(rejectedByICCService.update(rejectedByIccResource,
                request.getUserPrincipal().getName()));
    }

    @DeleteMapping("/rejectedByICCs/delete/{id}")
    public ResponseEntity<RejectedByICC> delete(@PathVariable("id") UUID iccFurtherDetailId, HttpServletRequest request) {
        RejectedByICC rejectedByIcc = rejectedByICCService.delete(iccFurtherDetailId,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(rejectedByIcc);
    }
}
