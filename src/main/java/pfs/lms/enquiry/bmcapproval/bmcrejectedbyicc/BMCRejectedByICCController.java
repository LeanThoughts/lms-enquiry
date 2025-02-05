package pfs.lms.enquiry.bmcapproval.bmcrejectedbyicc;

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
public class BMCRejectedByICCController {

    private final IBMCRejectedByICCService rejectedByICCService;

    @PostMapping("/bmcRejectedByICCs/create")
    public ResponseEntity<BmcRejectedByIcc> create(@RequestBody BMCRejectedByICCResource BMCRejectedByIccResource,
                                                   HttpServletRequest request) {

        return ResponseEntity.ok(rejectedByICCService.create(BMCRejectedByIccResource,
                request.getUserPrincipal().getName()));
    }

    @PutMapping("/bmcRejectedByICCs/update")
    public ResponseEntity<BmcRejectedByIcc> update(@RequestBody BMCRejectedByICCResource BMCRejectedByIccResource,
                                                   HttpServletRequest request) throws CloneNotSupportedException {

        return ResponseEntity.ok(rejectedByICCService.update(BMCRejectedByIccResource,
                request.getUserPrincipal().getName()));
    }

    @DeleteMapping("/bmcRejectedByICCs/delete/{id}")
    public ResponseEntity<BmcRejectedByIcc> delete(@PathVariable("id") UUID iccFurtherDetailId, HttpServletRequest request) {
        BmcRejectedByIcc BMCRejectedByIcc = rejectedByICCService.delete(iccFurtherDetailId,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(BMCRejectedByIcc);
    }
}
