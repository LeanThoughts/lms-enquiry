package pfs.lms.enquiry.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pfs.lms.enquiry.config.ApiController;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.domain.LoanContractExtension;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.repository.LoanContractExtensionRepository;
import pfs.lms.enquiry.resource.LoanContractExtensionResource;
import pfs.lms.enquiry.service.ILoanContractExtensionService;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Slf4j
@ApiController
@RequiredArgsConstructor

public class LoanContractExtensionController {

    private final ILoanContractExtensionService iLoanContractExtensionService;
    private final LoanContractExtensionRepository loanContractExtensionRepository;
    private final LoanApplicationRepository loanApplicationRepository;

    @GetMapping("/loancontractextension/{loanapplicationid}")
    public ResponseEntity get(@PathVariable("loanapplicationid") String loanapplicationid, HttpServletRequest request) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(UUID.fromString(loanapplicationid));
        LoanContractExtension resource = loanContractExtensionRepository.getLoanContractExtensionByLoanApplication(loanApplication);
        return ResponseEntity.ok(resource);

    }

    @PostMapping("/loancontractextension/{loanapplicationid}")
    public ResponseEntity create(@PathVariable("loanapplicationid") String loanapplicationid, @RequestBody LoanContractExtensionResource resource, HttpServletRequest request) throws InterruptedException, CloneNotSupportedException {
        resource.setLoanApplicationId(UUID.fromString(loanapplicationid));
        LoanContractExtension loanContractExtension =
                iLoanContractExtensionService.save(resource, request.getUserPrincipal().getName());

        return ResponseEntity.ok(loanContractExtension);

    }

    @PutMapping("/loancontractextension/{id}")
    public ResponseEntity update(@PathVariable("id") String loanContractExtensionId, @RequestBody LoanContractExtensionResource resource, HttpServletRequest request) throws InterruptedException, CloneNotSupportedException {
        LoanContractExtension loanContractExtension =
                iLoanContractExtensionService.update(resource, request.getUserPrincipal().getName());

        return ResponseEntity.ok(loanContractExtension);

    }

}
