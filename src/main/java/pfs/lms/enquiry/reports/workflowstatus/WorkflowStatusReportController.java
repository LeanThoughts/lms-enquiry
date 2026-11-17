package pfs.lms.enquiry.reports.workflowstatus;

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
import pfs.lms.enquiry.reports.workflowstatus.WorkflowStatusDtos.ExportRequest;
import pfs.lms.enquiry.reports.workflowstatus.WorkflowStatusDtos.Report;
import pfs.lms.enquiry.reports.workflowstatus.WorkflowStatusDtos.Selection;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

/**
 * REST API of the Workflow Status Report (Reports › Miscellaneous), under /api from {@link ApiController}:
 * {@code GET /reports/workflow-status?companyCode=PFS&releaseObject=VDARL&contractFrom=…&contractTo=…
 * &partnerFrom=…&partnerTo=…&dateFrom=yyyy-MM-dd&dateTo=yyyy-MM-dd}. Runs the SAP report through
 * ZPFS_LMS_REPORTS_SRV and returns its rows. {@code POST /reports/workflow-status/export} returns the rows as an
 * Excel file.
 */
@ApiController
public class WorkflowStatusReportController {

    private static final String XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    private final WorkflowStatusReportService service;
    private final WorkflowStatusExcelWriter excelWriter;

    public WorkflowStatusReportController(WorkflowStatusReportService service, WorkflowStatusExcelWriter excelWriter) {
        this.service = service;
        this.excelWriter = excelWriter;
    }

    @GetMapping("/reports/workflow-status")
    public ResponseEntity<Report> run(@RequestParam(required = false) String companyCode,
                                      @RequestParam(required = false) String releaseObject,
                                      @RequestParam(required = false) String contract,
                                      @RequestParam(required = false) String partner,
                                      @RequestParam(required = false) String date,
                                      HttpServletRequest request) {
        signedIn(request);
        Selection selection = new Selection();
        selection.companyCode = companyCode;
        selection.releaseObject = releaseObject;
        selection.contract = SelectOptions.parse(contract);
        selection.partner = SelectOptions.parse(partner);
        selection.date = SelectOptions.parse(date);
        return ResponseEntity.ok(service.run(selection));
    }

    /**
     * Excel download of the report as shown on the screen: the browser posts the selection and the rows in their
     * displayed order (sorted / filtered); no second call to SAP is made.
     */
    @PostMapping(value = "/reports/workflow-status/export", produces = XLSX)
    public ResponseEntity<Resource> export(@RequestBody ExportRequest export, HttpServletRequest request) {
        signedIn(request);
        byte[] bytes = excelWriter.write(export.getSelection(), export.getRows(), export.getColumns());
        String fileName = "workflow-status-report-" + LocalDate.now() + ".xlsx";
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
