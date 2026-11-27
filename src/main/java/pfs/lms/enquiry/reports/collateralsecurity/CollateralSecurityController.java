package pfs.lms.enquiry.reports.collateralsecurity;

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
import pfs.lms.enquiry.reports.collateralsecurity.CollateralSecurityDtos.ExportRequest;
import pfs.lms.enquiry.reports.collateralsecurity.CollateralSecurityDtos.Report;
import pfs.lms.enquiry.reports.collateralsecurity.CollateralSecurityDtos.Selection;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

/**
 * REST API of the Collateral Securities Report (Reports › Collaterals), under /api:
 * {@code GET /reports/collateral-securities?companyCode=PFS&contract=[…]&partner=[…]&complied=true&…} runs the SAP
 * report through ZPFS_LMS_REPORTS_SRV (CollateralSecurityReportSet); {@code POST /reports/collateral-securities/export}
 * returns the rows as shown as Excel.
 */
@ApiController
public class CollateralSecurityController {

    private static final String XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    private final CollateralSecurityService service;
    private final CollateralSecurityExcelWriter excelWriter;

    public CollateralSecurityController(CollateralSecurityService service, CollateralSecurityExcelWriter excelWriter) {
        this.service = service;
        this.excelWriter = excelWriter;
    }

    @GetMapping("/reports/collateral-securities")
    public ResponseEntity<Report> run(@RequestParam(defaultValue = "PFS") String companyCode,
                                      @RequestParam(required = false) String contract,
                                      @RequestParam(required = false) String partner,
                                      @RequestParam(defaultValue = "true") boolean complied,
                                      @RequestParam(defaultValue = "true") boolean notComplied,
                                      @RequestParam(defaultValue = "true") boolean inProcess,
                                      @RequestParam(defaultValue = "true") boolean partiallyComplied,
                                      @RequestParam(defaultValue = "true") boolean timelineAvailable,
                                      HttpServletRequest request) {
        signedIn(request);
        Selection selection = new Selection();
        selection.companyCode = companyCode;
        selection.contract = SelectOptions.parse(contract);
        selection.partner = SelectOptions.parse(partner);
        selection.complied = complied;
        selection.notComplied = notComplied;
        selection.inProcess = inProcess;
        selection.partiallyComplied = partiallyComplied;
        selection.timelineAvailable = timelineAvailable;
        return ResponseEntity.ok(service.run(selection));
    }

    @PostMapping(value = "/reports/collateral-securities/export", produces = XLSX)
    public ResponseEntity<Resource> export(@RequestBody ExportRequest export, HttpServletRequest request) {
        signedIn(request);
        byte[] bytes = excelWriter.write(export.getSelection(), export.getRows(), export.getColumns());
        String fileName = "collateral-securities-" + LocalDate.now() + ".xlsx";
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
