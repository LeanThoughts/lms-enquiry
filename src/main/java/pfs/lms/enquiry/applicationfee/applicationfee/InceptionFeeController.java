package pfs.lms.enquiry.applicationfee.applicationfee;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Slf4j
@RepositoryRestController
//@RestController
@RequiredArgsConstructor
public class InceptionFeeController {
    private final LoanApplicationRepository loanApplicationRepository;

    private final IInceptionFeeService inceptionFeeService;
    private final InceptionFeeRepository inceptionFeeRepository;

    @PostMapping("/inceptionFees/create")
    public ResponseEntity<InceptionFee> create(@RequestBody InceptionFeeResource inceptionFeeResource,
                                               HttpServletRequest request) {

        return ResponseEntity.ok(inceptionFeeService.create(inceptionFeeResource,
                request.getUserPrincipal().getName()));
    }

    //@RequestMapping(value = "/sapInceptionFees/create", method = RequestMethod.POST, produces = "application/json")
    @PostMapping("/inceptionFees/sapInceptionFees/create")
    public ResponseEntity<InceptionFee> createFromSAP(@RequestBody InceptionFeeResource inceptionFeeResource,
                                               HttpServletRequest request) throws CloneNotSupportedException {

//
        log.info("Loan Number : " + inceptionFeeResource.getLoanContractId());
        log.info("Invoice Number : " + inceptionFeeResource.getInvoiceNumber());
        log.info("Invoice Date : " + inceptionFeeResource.getInvoiceDate());
        log.info("Amount : " + inceptionFeeResource.getAmount());

        LoanApplication loanApplication = loanApplicationRepository.findByLoanContractId(inceptionFeeResource.getLoanContractId());
        if (loanApplication != null){
            inceptionFeeResource.setLoanApplicationId(loanApplication.getId());

        List<InceptionFee> inceptionFeeList = inceptionFeeRepository.findByInvoiceNumber(inceptionFeeResource.getInvoiceNumber());
        if (inceptionFeeList.size() > 0 ) {
            InceptionFee inceptionFee = inceptionFeeList.get(0);
            inceptionFeeResource.setId(inceptionFee.getId());
            return ResponseEntity.ok(inceptionFeeService.update(inceptionFeeResource,
                    request.getUserPrincipal().getName()));
        }else {
            return ResponseEntity.ok(inceptionFeeService.create(inceptionFeeResource,
                    request.getUserPrincipal().getName()));

        }

        }
        return  null;
    }

    //@RequestMapping(value = "/sapInceptionFees/create", method = RequestMethod.POST, produces = "application/json")
    @PostMapping("/inceptionFees/sapInceptionFees/reverse")
    public ResponseEntity<InceptionFee> reverseFromSAP(@RequestBody InceptionFeeResource inceptionFeeResource,
                                                      HttpServletRequest request) throws CloneNotSupportedException {

//
        log.info("Loan Number : " + inceptionFeeResource.getLoanContractId());
        log.info("Invoice Number : " + inceptionFeeResource.getInvoiceNumber());
        log.info("Invoice Date : " + inceptionFeeResource.getInvoiceDate());
        log.info("Amount : " + inceptionFeeResource.getAmount());

        LoanApplication loanApplication = loanApplicationRepository.findByLoanContractId(inceptionFeeResource.getLoanContractId());
        if (loanApplication != null){
            inceptionFeeResource.setLoanApplicationId(loanApplication.getId());

            List<InceptionFee> inceptionFeeList = inceptionFeeRepository.findByInvoiceNumber(inceptionFeeResource.getInvoiceNumber());
            if (inceptionFeeList.size() > 0 ) {
                InceptionFee inceptionFee = inceptionFeeList.get(0);
                inceptionFeeResource.setId(inceptionFee.getId());
                inceptionFeeResource.setStatusCode("5");
                inceptionFeeResource.setStatusDescription("Reversed");
                return ResponseEntity.ok(inceptionFeeService.update(inceptionFeeResource,
                        request.getUserPrincipal().getName()));
            }
        }
        return  null;
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
