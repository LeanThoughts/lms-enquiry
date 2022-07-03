package pfs.lms.enquiry.reports;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pfs.lms.enquiry.config.ApiController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ApiController
@RequiredArgsConstructor
public class ReportController {


    @GetMapping("report/enquiry")
    public ResponseEntity getPartnerByEmailId(
            @RequestParam("enquiryDateFrom") String enquiryDateFrom,
            @RequestParam("enquiryDateTo") String enquiryDateTo,
            @RequestParam("enquiryNumberFrom") String enquiryNumberFrom,
            @RequestParam("enquiryNumberTo") String enquiryNumberTo,
            @RequestParam("projectName") String projectName,
            @RequestParam("projectLocation") String projectLocation,
            @RequestParam("loanClass") String loanClass,
            @RequestParam("projectType") String projectType,
            @RequestParam("financingType") String financingType,
            @RequestParam("productCode") Integer productCode,
            @RequestParam("status") String status,
            @RequestParam("createdBy") String createdBy,
            @RequestParam("approvedBy") String approvedBy,
            HttpServletRequest httpServletRequest) {


        return null;
    }
}
