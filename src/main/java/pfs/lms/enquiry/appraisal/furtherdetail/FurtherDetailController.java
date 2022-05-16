package pfs.lms.enquiry.appraisal.furtherdetail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RepositoryRestController
@RequiredArgsConstructor
public class FurtherDetailController {

    private final FurtherDetailService furtherDetailService;

    @PutMapping("/furtherDetails/update")
    public ResponseEntity<FurtherDetail> updateFurtherDetails(@RequestBody FurtherDetailResource furtherDetailResource,
                                                                 HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(furtherDetailService.updateFurtherDetails(furtherDetailResource,request.getUserPrincipal().getName()));
    }
}
