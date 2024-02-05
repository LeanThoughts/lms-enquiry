package pfs.lms.enquiry.documentation.llcfee;

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
public class LLCFeeController {

    private final LLCFeeService service;

    @PostMapping("/lLCFees/create")
    public ResponseEntity<LLCFee> create(@RequestBody LLCFeeResource resource, HttpServletRequest request) {
        return ResponseEntity.ok(service.create(resource, request.getUserPrincipal().getName()));
    }

    @PutMapping("/lLCFees/update")
    public ResponseEntity<LLCFee> update(@RequestBody LLCFeeResource resource, HttpServletRequest request)
            throws CloneNotSupportedException {
        return ResponseEntity.ok(service.update(resource, request.getUserPrincipal().getName()));
    }

    @DeleteMapping("/lLCFees/delete/{id}")
    public ResponseEntity<LLCFee> delete(@PathVariable("id") UUID id,
                                         HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(service.delete(id, request.getUserPrincipal().getName()));
    }
}
