package pfs.lms.enquiry.action.projectproposal.collateraldetail;

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
public class CollateralDetailController {

    private final ICollateralDetailService collateralDetailService;

    @PostMapping("/collateralDetails/create")
    public ResponseEntity<CollateralDetail> create(@RequestBody CollateralDetailResource resource,
                                                                             HttpServletRequest request) {
        return ResponseEntity.ok(collateralDetailService.create(resource, request.getUserPrincipal().getName()));
    }

    @PutMapping("/collateralDetails/update")
    public ResponseEntity<CollateralDetail> update(@RequestBody CollateralDetailResource resource,
                                                                HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(collateralDetailService.update(resource, request.getUserPrincipal().getName()));
    }
}
