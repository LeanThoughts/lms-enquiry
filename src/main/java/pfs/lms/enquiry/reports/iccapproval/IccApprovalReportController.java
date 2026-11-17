package pfs.lms.enquiry.reports.iccapproval;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import pfs.lms.enquiry.config.ApiController;
import pfs.lms.enquiry.reports.SelectOptions;
import pfs.lms.enquiry.exception.LmsException;
import pfs.lms.enquiry.reports.iccapproval.IccApprovalDtos.ExportRequest;
import pfs.lms.enquiry.reports.iccapproval.IccApprovalDtos.Report;
import pfs.lms.enquiry.reports.iccapproval.IccApprovalDtos.Selection;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

/**
 * REST API of the ICC Approval Status Report (Reports › Loans Management), under /api from {@link ApiController}:
 * {@code GET /reports/icc-approval?companyCode=PFS&fiscalYear=2026&quarter=1&contractFrom=…&contractTo=…} runs the
 * SAP report through ZPFS_LMS_REPORTS_SRV; {@code POST /reports/icc-approval/export} returns the rows as Excel.
 */
@ApiController
public class IccApprovalReportController {

    private static final String XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    private final IccApprovalReportService service;
    private final IccApprovalExcelWriter excelWriter;

    public IccApprovalReportController(IccApprovalReportService service, IccApprovalExcelWriter excelWriter) {
        this.service = service;
        this.excelWriter = excelWriter;
    }

    @GetMapping("/reports/icc-approval")
    public ResponseEntity<Report> run(@RequestParam(required = false) String companyCode,
                                      @RequestParam(required = false) String fiscalYear,
                                      @RequestParam(required = false) String quarter,
                                      @RequestParam(required = false) String contract,
                                      HttpServletRequest request) {
        signedIn(request);
        Selection selection = new Selection();
        selection.companyCode = companyCode;
        selection.fiscalYear = fiscalYear;
        selection.quarter = quarter;
        selection.contract = SelectOptions.parse(contract);
        return ResponseEntity.ok(service.run(selection));
    }

    @PostMapping(value = "/reports/icc-approval/export", produces = XLSX)
    public ResponseEntity<Resource> export(@RequestBody ExportRequest export, HttpServletRequest request) {
        signedIn(request);
        byte[] bytes = excelWriter.write(export.getSelection(), export.getRows(), export.getColumns());
        String fileName = "icc-approval-status-report-" + LocalDate.now() + ".xlsx";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.parseMediaType(XLSX))
                .body(new ByteArrayResource(bytes));
    }

    private static void signedIn(HttpServletRequest request) {
        if (request.getUserPrincipal() == null) {
            throw new LmsException("Please sign in.", HttpStatus.UNAUTHORIZED);
        }
    }
}
