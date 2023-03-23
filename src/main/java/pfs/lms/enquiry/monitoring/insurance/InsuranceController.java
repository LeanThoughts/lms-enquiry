package pfs.lms.enquiry.monitoring.insurance;

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
}
