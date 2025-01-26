package pfs.lms.enquiry.bmcapproval.bmcloanenhancement;

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
public class BMCLoanEnhancementController {

    private final IBMCLoanEnhancementService ibmcLoanEnhancementService;

    @PostMapping("/bmcLoanEnhancements/create")
    public ResponseEntity<BMCLoanEnhancement> create(@RequestBody BMCLoanEnhancementResource bmcLoanEnhancementResource,
                                                     HttpServletRequest request) {

        return ResponseEntity.ok(ibmcLoanEnhancementService.create(bmcLoanEnhancementResource,
                request.getUserPrincipal().getName()));
    }

    @PutMapping("/bmcLoanEnhancements/update")
    public ResponseEntity<BMCLoanEnhancement> update(@RequestBody BMCLoanEnhancementResource bmcLoanEnhancementResource,
                                                     HttpServletRequest request) throws CloneNotSupportedException {

        return ResponseEntity.ok(ibmcLoanEnhancementService.update(bmcLoanEnhancementResource,
                request.getUserPrincipal().getName()));
    }

    @DeleteMapping("/bmcLoanEnhancements/delete/{id}")
    public ResponseEntity<BMCLoanEnhancement> delete(@PathVariable("id") UUID bmcLoanEnhancementId, HttpServletRequest request) {
        BMCLoanEnhancement BMCLoanEnhancement = ibmcLoanEnhancementService.delete(bmcLoanEnhancementId,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(BMCLoanEnhancement);
    }
}
