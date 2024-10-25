package pfs.lms.enquiry.applicationfee.applicationfeeprojectdetails;

import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

@RepositoryRestController
@RequiredArgsConstructor
public class ApplicationFeeProjectDetailController {
    private final IApplicationFeeProjectDetailService applicationFeeProjectDetailService;

    @PostMapping("/applicationFeeProjectDetails/create")
    public ResponseEntity<ApplicationFeeProjectDetail> createApplicationFeeProjectDetail(
            @RequestBody ApplicationFeeProjectDetailResource applicationFeeProjectDetailResource,
            HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        return ResponseEntity.ok(applicationFeeProjectDetailService.create(applicationFeeProjectDetailResource, username));
    }

//    @GetMapping("/applicationFeeProjectDetails")
//    public ResponseEntity<ApplicationFeeProjectDetail> getApplicationFeeProjectDetails(
//            @RequestParam("applicationFeeId") UUID applicationFeeId) {
//        return ResponseEntity.ok(applicationFeeProjectDetailService.getApplicationFeeProjectDetail(applicationFeeId));
//    }

    @PutMapping("/applicationFeeProjectDetails/update")
    public ResponseEntity<ApplicationFeeProjectDetail> updateApplicationFeeProjectDetail(
            @RequestBody ApplicationFeeProjectDetailResource applicationFeeProjectDetailResource,
            HttpServletRequest request) throws CloneNotSupportedException {
        String username = request.getUserPrincipal().getName();
        return ResponseEntity.ok(applicationFeeProjectDetailService.update(applicationFeeProjectDetailResource, username));
    }
}
