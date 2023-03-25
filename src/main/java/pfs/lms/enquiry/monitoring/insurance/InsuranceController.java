package pfs.lms.enquiry.monitoring.insurance;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Slf4j
@RepositoryRestController
@RequiredArgsConstructor
public class InsuranceController {

    private final IInsuranceService insuranceService;

    @PostMapping("/loanApplications/insurances/create")
    public ResponseEntity<Insurance> createInsurance(@RequestBody InsuranceResource resource, HttpServletRequest request)
            throws CloneNotSupportedException {
        Insurance insurance = insuranceService.saveInsurance(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(insurance);
    }

    @PutMapping("/loanApplications/insurances/update")
    public ResponseEntity<Insurance> updateInsurance(@RequestBody InsuranceResource resource, HttpServletRequest request)
            throws CloneNotSupportedException {
        Insurance insurance = insuranceService.updateInsurance(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(insurance);
    }

    @GetMapping("/loanApplications/{loanApplicationId}/insurances")
    public ResponseEntity<List<Insurance>> getInsurances(@PathVariable("loanApplicationId") UUID loanApplicationId,
                                                  HttpServletRequest request) {
        List<Insurance> insurances = insuranceService.getInsurances(loanApplicationId);
        return ResponseEntity.ok(insurances);
    }
}
