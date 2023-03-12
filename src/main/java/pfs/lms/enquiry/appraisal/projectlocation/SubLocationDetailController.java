package pfs.lms.enquiry.appraisal.projectlocation;

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
public class SubLocationDetailController {

    private final SubLocationDetailService subLocationDetailService;

    @PostMapping("/subLocationDetails/create")
    public ResponseEntity<SubLocationDetail> createSubLocation(@RequestBody SubLocationDetailResource subLocationDetailResource,
                                                                   HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(subLocationDetailService.createSubLocation(subLocationDetailResource,
                request.getUserPrincipal().getName()));
    }

    @PutMapping("/subLocationDetails/update")
    public ResponseEntity<SubLocationDetail> updateSubLocation(@RequestBody SubLocationDetailResource subLocationDetailResource,
                                                                   HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(subLocationDetailService.updateSubLocation(subLocationDetailResource,
                request.getUserPrincipal().getName()));
    }
}
