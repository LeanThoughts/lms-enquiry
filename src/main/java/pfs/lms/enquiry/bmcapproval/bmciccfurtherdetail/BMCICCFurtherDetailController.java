package pfs.lms.enquiry.bmcapproval.bmciccfurtherdetail;

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
public class BMCICCFurtherDetailController {

    private final IBMCICCFurtherDetailService iBMCICCFurtherDetailService;

    @PostMapping("/bmcICCFurtherDetails/create")
    public ResponseEntity<BmcICCFurtherDetail> create(@RequestBody BMCICCFurtherDetailResource bmciccFurtherDetailResource,
                                                      HttpServletRequest request) {

        return ResponseEntity.ok(iBMCICCFurtherDetailService.create(bmciccFurtherDetailResource,
                request.getUserPrincipal().getName()));
    }

    @PutMapping("/bmcICCFurtherDetails/update")
    public ResponseEntity<BmcICCFurtherDetail> update(@RequestBody BMCICCFurtherDetailResource bmciccFurtherDetailResource,
                                                      HttpServletRequest request) throws CloneNotSupportedException {

        return ResponseEntity.ok(iBMCICCFurtherDetailService.update(bmciccFurtherDetailResource,
                request.getUserPrincipal().getName()));
    }

    @DeleteMapping("/bmcICCFurtherDetails/delete/{id}")
    public ResponseEntity<BmcICCFurtherDetail> delete(@PathVariable("id") UUID bmciccFurtherDetailId, HttpServletRequest request) {
        BmcICCFurtherDetail BMCICCFurtherDetail = iBMCICCFurtherDetailService.delete(bmciccFurtherDetailId,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(BMCICCFurtherDetail);
    }
}
