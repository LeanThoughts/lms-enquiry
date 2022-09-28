package pfs.lms.enquiry.action.otherdetail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RepositoryRestController
@RequiredArgsConstructor
public class OtherDetailController {

    private final OtherDetailService otherDetailService;

    @PostMapping("/otherDetails/create")
    public ResponseEntity<OtherDetail> createOtherDetail(@RequestBody OtherDetailResource resource,
                                                             HttpServletRequest request) {
        return ResponseEntity.ok(otherDetailService.create(resource, request.getUserPrincipal().getName()));
    }

    @PutMapping("/otherDetails/update")
    public ResponseEntity<OtherDetail> updateOtherDetail(@RequestBody OtherDetailResource resource,
                                                             HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(otherDetailService.update(resource, request.getUserPrincipal().getName()));
    }
}
