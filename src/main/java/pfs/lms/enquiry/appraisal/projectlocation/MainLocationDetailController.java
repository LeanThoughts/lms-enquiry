package pfs.lms.enquiry.appraisal.projectlocation;

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
public class MainLocationDetailController {

    private final MainLocationDetailService mainLocationDetailService;

    @PutMapping("/mainLocationDetails/update")
    public ResponseEntity<MainLocationDetail> updateMainLocation(@RequestBody MainLocationDetailResource mainLocationDetailResource,
                                                                   HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(mainLocationDetailService.updateMainLocationDetails(mainLocationDetailResource,
                request.getUserPrincipal().getName()));
    }
}
