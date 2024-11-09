package pfs.lms.enquiry.applicationfee.applicationfee;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pfs.lms.enquiry.applicationfee.ApplicationFee;
import pfs.lms.enquiry.applicationfee.ApplicationFeeRepository;
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
    private final ApplicationFeeRepository applicationFeeRepository;
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
        LoanApplication loanApplication = new LoanApplication();
//
        log.info("Loan Number : " + inceptionFeeResource.getLoanContractId());
        log.info("Invoice Number : " + inceptionFeeResource.getInvoiceNumber());
        log.info("Invoice Date : " + inceptionFeeResource.getInvoiceDate());
        log.info("Amount : " + inceptionFeeResource.getAmount());
        try {
             loanApplication = loanApplicationRepository.findByLoanContractId(inceptionFeeResource.getLoanContractId());
        }
        catch (Exception ex){
            log.error("Finding LoanApplication by ID: " + inceptionFeeResource.getLoanContractId());
            log.error("Error: " + ex.getMessage());
        }
        if (loanApplication != null) {
            inceptionFeeResource.setLoanApplicationId(loanApplication.getId());

            List<InceptionFee> inceptionFeeList = inceptionFeeRepository.findByInvoiceNumber(inceptionFeeResource.getInvoiceNumber());
            if (inceptionFeeList.size() > 0) {
                log.info("Updating Application Fee : " + inceptionFeeResource.getInvoiceNumber());
                InceptionFee inceptionFee = inceptionFeeList.get(0);
                inceptionFeeResource.setId(inceptionFee.getId());

                ResponseEntity responseEntity =  ResponseEntity.ok(inceptionFeeService.update(inceptionFeeResource,
                        request.getUserPrincipal().getName()));

                log.info("Updating Application Fee Completed : " + inceptionFeeResource.getInvoiceNumber());
                log.info(responseEntity.toString());
                return responseEntity;

            } else {
                log.info("Create Application Fee : " + inceptionFeeResource.getInvoiceNumber());

                ResponseEntity responseEntity = ResponseEntity.ok(inceptionFeeService.create(inceptionFeeResource,
                        request.getUserPrincipal().getName()));

                log.info("Updating Application Fee Completed : " + inceptionFeeResource.getInvoiceNumber());
                log.info(responseEntity.toString());
                return responseEntity;

            }

        }
        return null;
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

            List<InceptionFee> inceptionFeeList = inceptionFeeRepository.findBySapFIDocumentNumberFee(inceptionFeeResource.getSapFIDocumentNumberFee());
            for (InceptionFee inceptionFee : inceptionFeeList) {
                inceptionFeeResource.setId(inceptionFee.getId());
                inceptionFeeResource.setStatusCode("5");
                inceptionFeeResource.setStatusDescription("Reversed");
                return ResponseEntity.ok(inceptionFeeService.update(inceptionFeeResource,
                        request.getUserPrincipal().getName()));
            }


        return null;
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

    @GetMapping("/inceptionFees/{id}")
    public ResponseEntity<List<InceptionFee>> get(@PathVariable("id") UUID applicationFeeId){

        ApplicationFee applicationFee = applicationFeeRepository.getOne(applicationFeeId);
        List<InceptionFee> inceptionFeeList = inceptionFeeRepository.findByApplicationFeeId(applicationFee.getId());
        return ResponseEntity.ok(inceptionFeeList);
    }



}
