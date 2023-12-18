package pfs.lms.enquiry.applicationfee.applicationfee;

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
public class InceptionFeeController {

    private final IInceptionFeeService inceptionFeeService;

    @PostMapping("/inceptionFees/create")
    public ResponseEntity<InceptionFee> create(@RequestBody InceptionFeeResource inceptionFeeResource,
                                               HttpServletRequest request) {

        return ResponseEntity.ok(inceptionFeeService.create(inceptionFeeResource,
                request.getUserPrincipal().getName()));
    }

    @PutMapping("/inceptionFees/update")
    public ResponseEntity<InceptionFee> update(@RequestBody InceptionFeeResource inceptionFeeResource,
                                               HttpServletRequest request) throws CloneNotSupportedException {

        return ResponseEntity.ok(inceptionFeeService.update(inceptionFeeResource,
                request.getUserPrincipal().getName()));
    }

    @DeleteMapping("/inceptionFees/delete/{id}")
    public ResponseEntity<InceptionFee> delete(@PathVariable("id") UUID inceptionFeeId, HttpServletRequest request) {
        InceptionFee inceptionFee = inceptionFeeService.delete(inceptionFeeId,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(inceptionFee);
    }
}