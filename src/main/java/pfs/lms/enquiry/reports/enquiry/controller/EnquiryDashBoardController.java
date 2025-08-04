package pfs.lms.enquiry.reports.enquiry.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import pfs.lms.enquiry.reports.enquiry.dto.LoanEnquiryDashboardDTO;
import pfs.lms.enquiry.reports.enquiry.service.IEnquiryDashboardService;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;


@Slf4j
@RestController
@RequiredArgsConstructor
public class EnquiryDashBoardController {

     private final IEnquiryDashboardService enquiryDashboardService;


    @GetMapping("/enquiryDashBoard/{reportDate}")
    public ResponseEntity<LoanEnquiryDashboardDTO> getDashBoardData(
            @PathVariable("reportDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date reportDate, HttpServletRequest request,
            @PageableDefault(sort = "UNSORTED", size = 9999, direction = Sort.Direction.DESC) Pageable pageable) {

        LoanEnquiryDashboardDTO loanEnquiryDashboardDTO  = new LoanEnquiryDashboardDTO();

        LocalDate reportingDate = LocalDate.parse( new SimpleDateFormat("yyyy-MM-dd").format(reportDate) );
        log.info("Reporting Date : " + reportingDate.toString());

        try {
            loanEnquiryDashboardDTO = enquiryDashboardService.getLoanEnquiryDashboardData(reportingDate, request, pageable);
        } catch (Exception ex){
            log.error("Exception in enquiryDashboardService " + ex.getMessage());
        }
        log.info("Output : " +loanEnquiryDashboardDTO.toString());

        return ResponseEntity.ok(loanEnquiryDashboardDTO);
    }

}

