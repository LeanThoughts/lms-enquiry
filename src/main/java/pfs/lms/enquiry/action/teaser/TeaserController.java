package pfs.lms.enquiry.action.teaser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pfs.lms.enquiry.config.ApiController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@ApiController
@RequiredArgsConstructor
public class TeaserController {

    @GetMapping(value = "/teaser/excelv1")
    public void generateTeaserExcel(){

        String headerKey = "Content-Disposition";

    }

    @GetMapping("/teaser/excel/generate")
    public void generateTeaserExcel(
            HttpServletResponse response,
            @RequestParam(required = false) String loanEnquiryId,
            HttpServletRequest request) throws IOException, ParseException {

        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=LoanEnquiryTeaser_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

//        LoanApplicationResource loanApplicationResource = new LoanApplicationResource();
//
//
//
//        TeaserService teaserService = new TeaserService(loanApplicationResource);
//        teaserService.generateTeaserExcel(response);


    }
}
