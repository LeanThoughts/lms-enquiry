package pfs.lms.enquiry.reports.covenants;

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
import pfs.lms.enquiry.reports.covenants.CovenantsDtos.ExportRequest;
import pfs.lms.enquiry.reports.covenants.CovenantsDtos.Report;
import pfs.lms.enquiry.reports.covenants.CovenantsDtos.Selection;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

/**
 * REST API of the Covenants Report (Reports › Loans Management), under /api:
 * {@code GET /reports/covenants?companyCode=PFS&contract=[…]} runs the SAP report through ZPFS_LMS_REPORTS_SRV
 * (CovenantsReportSet); {@code POST /reports/covenants/export} returns the rows as shown as Excel.
 */
@ApiController
public class CovenantsController {

    private static final String XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    private final CovenantsService service;
    private final CovenantsExcelWriter excelWriter;

    public CovenantsController(CovenantsService service, CovenantsExcelWriter excelWriter) {
        this.service = service;
        this.excelWriter = excelWriter;
    }

    @GetMapping("/reports/covenants")
    public ResponseEntity<Report> run(@RequestParam(defaultValue = "PFS") String companyCode,
                                      @RequestParam(required = false) String contract,
                                      HttpServletRequest request) {
        signedIn(request);
        Selection selection = new Selection();
        selection.companyCode = companyCode;
        selection.contract = SelectOptions.parse(contract);
        return ResponseEntity.ok(service.run(selection));
    }

    @PostMapping(value = "/reports/covenants/export", produces = XLSX)
    public ResponseEntity<Resource> export(@RequestBody ExportRequest export, HttpServletRequest request) {
        signedIn(request);
        byte[] bytes = excelWriter.write(export.getSelection(), export.getRows(), export.getColumns());
        String fileName = "covenants-" + LocalDate.now() + ".xlsx";
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
