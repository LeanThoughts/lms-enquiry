package pfs.lms.enquiry.appraisal.riskreport;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import pfs.lms.enquiry.config.ApiController;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RepositoryRestController
@ApiController

public class RiskClientController {

    private final IRiskClient riskClient;

    @GetMapping("/test")
    public ResponseEntity test( HttpServletRequest request) {
        return ResponseEntity.ok(null);
    }

    @GetMapping("/loanApplications/riskModelSummary/{loanContractId}")
    public ResponseEntity<List<RiskEvaluationSummary>> findRiskModelSummaryForLoanContractId(
            @PathVariable String loanContractId, HttpServletRequest request) {
        return ResponseEntity.ok(riskClient.findRiskModelSummaryForLoanContractId(loanContractId));
    }
    @GetMapping(value = "/riskModelPDF",produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getPdf(
            @RequestParam(value = "id", required = true) Long id) {

        String fileName  = "Risk_Model" + "_" + id.toString() + "_" + ".pdf";;

        HttpHeaders responseHeader = new HttpHeaders();
        responseHeader.setContentType(MediaType.APPLICATION_PDF);
        responseHeader.setCacheControl("must-revalidate, post-check=0, pre-check=0");
         responseHeader.setContentDispositionFormData(fileName,fileName);
        //responseHeader.add("Expires", "0");
        //responseHeader.add("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        //responseHeader.add("Pragma", "no-cache");
        //String fileName  = "Risk_Model" + "_" + id.toString();
        //responseHeader.add("Content-disposition", "inline; filename=" + fileName + ".pdf");


         byte[] responseByteArray = riskClient.printRiskReport(id);

         return new ResponseEntity(responseByteArray,responseHeader, HttpStatus.OK);
    }

    public ResponseEntity streamToResponseEntity(ByteArrayOutputStream stream , Long id){
        HttpHeaders responseHeader = new HttpHeaders();
        responseHeader.setContentType(MediaType.APPLICATION_PDF);
        responseHeader.add("Expires", "0");
        responseHeader.add("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        responseHeader.add("Pragma", "no-cache");

        String fileName  ="Risk_Grading" + "_" + id.toString();

        responseHeader.add("Content-disposition", "inline; filename=" + fileName + ".pdf");


        return new ResponseEntity(stream.toByteArray(), responseHeader, HttpStatus.OK);
    }
}
