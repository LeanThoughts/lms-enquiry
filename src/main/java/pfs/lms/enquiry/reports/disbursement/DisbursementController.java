package pfs.lms.enquiry.reports.disbursement;

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
import pfs.lms.enquiry.exception.LmsException;
import pfs.lms.enquiry.reports.SelectOptions;
import pfs.lms.enquiry.reports.disbursement.DisbursementDtos.ExportRequest;
import pfs.lms.enquiry.reports.disbursement.DisbursementDtos.Report;
import pfs.lms.enquiry.reports.disbursement.DisbursementDtos.Selection;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

/**
 * REST API of the Loans Disbursement - Periodwise and Comparison Report (Reports › Loans Management), under /api:
 * {@code GET /reports/disbursement?companyCode=PFS&mode=R&fiscalYearFrom=2026&fiscalYearTo=&periodType=M&periodFilter=
 * &contract=[…]&partner=[…]&projectType=&allLoans=true&suppressZero=true} (the select-options as JSON arrays of sign /
 * option / low / high) runs the SAP report through ZPFS_LMS_REPORTS_SRV;
 * {@code POST /reports/disbursement/export} returns the rows as Excel.
 */
@ApiController
public class DisbursementController {

    private static final String XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    private final DisbursementService service;
    private final DisbursementExcelWriter excelWriter;

    public DisbursementController(DisbursementService service, DisbursementExcelWriter excelWriter) {
        this.service = service;
        this.excelWriter = excelWriter;
    }

    @GetMapping("/reports/disbursement")
    public ResponseEntity<Report> run(@RequestParam(defaultValue = "PFS") String companyCode,
                                      @RequestParam(defaultValue = "R") String mode,
                                      @RequestParam(required = false) String fiscalYearFrom,
                                      @RequestParam(required = false) String fiscalYearTo,
                                      @RequestParam(defaultValue = "M") String periodType,
                                      @RequestParam(required = false) String periodFilter,
                                      @RequestParam(required = false) String contract,
                                      @RequestParam(required = false) String partner,
                                      @RequestParam(required = false) String projectType,
                                      @RequestParam(defaultValue = "true") boolean allLoans,
                                      @RequestParam(defaultValue = "true") boolean suppressZero,
                                      HttpServletRequest request) {
        signedIn(request);
        Selection selection = new Selection();
        selection.companyCode = companyCode;
        selection.mode = mode;
        selection.fiscalYearFrom = fiscalYearFrom;
        selection.fiscalYearTo = fiscalYearTo;
        selection.periodType = periodType;
        selection.periodFilter = periodFilter;
        selection.contract = SelectOptions.parse(contract);
        selection.partner = SelectOptions.parse(partner);
        selection.projectType = projectType;
        selection.allLoans = allLoans;
        selection.suppressZero = suppressZero;
        return ResponseEntity.ok(service.run(selection));
    }

    @PostMapping(value = "/reports/disbursement/export", produces = XLSX)
    public ResponseEntity<Resource> export(@RequestBody ExportRequest export, HttpServletRequest request) {
        signedIn(request);
        byte[] bytes = excelWriter.write(export.getSelection(), export.getRows(), export.getColumns());
        String fileName = "loans-disbursement-report-" + LocalDate.now() + ".xlsx";
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
