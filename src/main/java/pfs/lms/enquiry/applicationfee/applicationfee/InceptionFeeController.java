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
import java.util.UUID;

@Slf4j
@RepositoryRestController
//@RestController
@RequiredArgsConstructor
public class InceptionFeeController {
    private final LoanApplicationRepository loanApplicationRepository;

    private final IInceptionFeeService inceptionFeeService;

    @PostMapping("/inceptionFees/create")
    public ResponseEntity<InceptionFee> create(@RequestBody InceptionFeeResource inceptionFeeResource,
                                               HttpServletRequest request) {

        return ResponseEntity.ok(inceptionFeeService.create(inceptionFeeResource,
                request.getUserPrincipal().getName()));
    }

    //@RequestMapping(value = "/sapInceptionFees/create", method = RequestMethod.POST, produces = "application/json")
    @PostMapping("/inceptionFees/sapInceptionFees/create")
    public ResponseEntity<InceptionFee> createFromSAP(@RequestBody InceptionFeeResource inceptionFeeResource,
                                               HttpServletRequest request) {
//        InceptionFeeResource inceptionFeeResource = new InceptionFeeResource();
//
//        log.info("Loan Number : " + inceptionFeeSAPResource.getLoanContractId());
//        log.info("Invoice Number : " + inceptionFeeSAPResource.getInvoiceNumber());
//        log.info("Invoice Date : " + inceptionFeeSAPResource.getInvoiceDate());
//        log.info("Amount : " + inceptionFeeSAPResource.getAmount());
//        inceptionFeeResource.setAmount(inceptionFeeSAPResource.getAmount());
//        inceptionFeeResource.setInvoiceNumber(inceptionFeeSAPResource.getInvoiceNumber());
//        inceptionFeeResource.setInvoiceDate(inceptionFeeSAPResource.getInvoiceDate());
//        inceptionFeeResource.setAmountReceived(inceptionFeeSAPResource.getAmountReceived());
//        inceptionFeeResource.setTaxAmount(inceptionFeeSAPResource.getTaxAmount());
//        inceptionFeeResource.setTotalAmount(inceptionFeeSAPResource.getTotalAmount());


        LoanApplication loanApplication = loanApplicationRepository.findByLoanContractId(inceptionFeeResource.getLoanContractId());
        if (loanApplication != null){
            inceptionFeeResource.setLoanApplicationId(loanApplication.getId());

            return ResponseEntity.ok(inceptionFeeService.create(inceptionFeeResource,
                request.getUserPrincipal().getName()));
        }
        return  null;
    }

    @RequestMapping(value = "/inceptionFees/sapInceptionFees/update", method = RequestMethod.PUT, produces = "application/json")
    //@PostMapping("/sapInceptionFees/create")
    public ResponseEntity<InceptionFee> updateFromSAP(@RequestBody InceptionFeeResource inceptionFeeResource,
                                                      HttpServletRequest request) {


        log.info("Invoice Number : ", inceptionFeeResource.getInvoiceNumber());
        log.info("Invoice Date : ", inceptionFeeResource.getInvoiceDate());
        log.info("Amount : ", inceptionFeeResource.getAmount());


        LoanApplication loanApplication = loanApplicationRepository.findByLoanContractId(inceptionFeeResource.getLoanContractId());
        inceptionFeeResource.setLoanApplicationId(loanApplication.getId());



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
