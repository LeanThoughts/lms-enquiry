package pfs.lms.enquiry.reports.enquiry.service;

import org.springframework.data.domain.Pageable;
import pfs.lms.enquiry.reports.enquiry.dto.LoanEnquiryDashboardDTO;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

public interface IEnquiryDashboardService {

        LoanEnquiryDashboardDTO getLoanEnquiryDashboardData(LocalDate reportDate, HttpServletRequest request, Pageable pageable);

}
