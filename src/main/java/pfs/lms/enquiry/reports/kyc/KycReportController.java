package pfs.lms.enquiry.reports.kyc;

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
import pfs.lms.enquiry.reports.kyc.KycReportDtos.ExportRequest;
import pfs.lms.enquiry.reports.kyc.KycReportDtos.Report;
import pfs.lms.enquiry.reports.kyc.KycReportDtos.Selection;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

/**
 * REST API of the Loans KYC Report (Reports › Loans Management), under /api from {@link ApiController}:
 * {@code GET /reports/kyc?contract=[…]&partner=[…]&status=[…]&includeAuthSignatories=false
 * &runDate=yyyy-MM-dd&effCapOnly=true&kycDate=[…]} (the select-options as JSON arrays of sign / option / low / high) runs the SAP report through ZPFS_LMS_REPORTS_SRV;
 * {@code POST /reports/kyc/export} returns the rows as Excel.
 */
@ApiController
public class KycReportController {

    private static final String XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    private final KycReportService service;
    private final KycExcelWriter excelWriter;

    public KycReportController(KycReportService service, KycExcelWriter excelWriter) {
        this.service = service;
        this.excelWriter = excelWriter;
    }

    @GetMapping("/reports/kyc")
    public ResponseEntity<Report> run(@RequestParam(required = false) String contract,
                                      @RequestParam(required = false) String partner,
                                      @RequestParam(required = false) String status,
                                      @RequestParam(defaultValue = "false") boolean includeAuthSignatories,
                                      @RequestParam(required = false) String runDate,
                                      @RequestParam(defaultValue = "false") boolean effCapOnly,
                                      @RequestParam(required = false) String kycDate,
                                      HttpServletRequest request) {
        signedIn(request);
        Selection selection = new Selection();
        selection.companyCode = "PFS";
        selection.contract = SelectOptions.parse(contract);
        selection.partner = SelectOptions.parse(partner);
        selection.status = SelectOptions.parse(status);
        selection.includeAuthSignatories = includeAuthSignatories;
        selection.runDate = runDate;
        selection.effCapOnly = effCapOnly;
        selection.kycDate = SelectOptions.parse(kycDate);
        return ResponseEntity.ok(service.run(selection));
    }

    @PostMapping(value = "/reports/kyc/export", produces = XLSX)
    public ResponseEntity<Resource> export(@RequestBody ExportRequest export, HttpServletRequest request) {
        signedIn(request);
        byte[] bytes = excelWriter.write(export.getSelection(), export.getRows(), export.getColumns());
        String fileName = "loans-kyc-report-" + LocalDate.now() + ".xlsx";
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
