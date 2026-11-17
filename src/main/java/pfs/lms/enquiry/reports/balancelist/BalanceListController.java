package pfs.lms.enquiry.reports.balancelist;

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
import pfs.lms.enquiry.reports.balancelist.BalanceListDtos.ExportRequest;
import pfs.lms.enquiry.reports.balancelist.BalanceListDtos.Report;
import pfs.lms.enquiry.reports.balancelist.BalanceListDtos.Selection;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

/**
 * REST API of the Loans Balance List (Reports › Loans Management), under /api from {@link ApiController}:
 * {@code GET /reports/balance-list?contract=[…]&productType=[…]&loanType=[…]&currency=[…]&acctRef=[…]&changedBy=[…]
 * &evaluateCompleted=true&keyDate=yyyy-MM-dd&displayCurrency=&nonZeroOnly=false&totalsForZero=false&totalsOnly=false}
 * (the select-options as JSON arrays of sign / option / low / high) runs the SAP report through ZPFS_LMS_REPORTS_SRV;
 * {@code POST /reports/balance-list/export} returns the rows as Excel.
 */
@ApiController
public class BalanceListController {

    private static final String XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    private final BalanceListService service;
    private final BalanceListExcelWriter excelWriter;

    public BalanceListController(BalanceListService service, BalanceListExcelWriter excelWriter) {
        this.service = service;
        this.excelWriter = excelWriter;
    }

    @GetMapping("/reports/balance-list")
    public ResponseEntity<Report> run(@RequestParam(required = false) String contract,
                                      @RequestParam(required = false) String productType,
                                      @RequestParam(required = false) String loanType,
                                      @RequestParam(required = false) String currency,
                                      @RequestParam(required = false) String acctRef,
                                      @RequestParam(required = false) String changedBy,
                                      @RequestParam(defaultValue = "true") boolean evaluateCompleted,
                                      @RequestParam(required = false) String keyDate,
                                      @RequestParam(required = false) String displayCurrency,
                                      @RequestParam(defaultValue = "false") boolean nonZeroOnly,
                                      @RequestParam(defaultValue = "false") boolean totalsForZero,
                                      @RequestParam(defaultValue = "false") boolean totalsOnly,
                                      HttpServletRequest request) {
        signedIn(request);
        Selection selection = new Selection();
        selection.companyCode = "PFS";
        selection.contract = SelectOptions.parse(contract);
        selection.productType = SelectOptions.parse(productType);
        selection.loanType = SelectOptions.parse(loanType);
        selection.currency = SelectOptions.parse(currency);
        selection.acctRef = SelectOptions.parse(acctRef);
        selection.changedBy = SelectOptions.parse(changedBy);
        selection.evaluateCompleted = evaluateCompleted;
        selection.keyDate = keyDate;
        selection.displayCurrency = displayCurrency;
        selection.nonZeroOnly = nonZeroOnly;
        selection.totalsForZero = totalsForZero;
        selection.totalsOnly = totalsOnly;
        return ResponseEntity.ok(service.run(selection));
    }

    @PostMapping(value = "/reports/balance-list/export", produces = XLSX)
    public ResponseEntity<Resource> export(@RequestBody ExportRequest export, HttpServletRequest request) {
        signedIn(request);
        byte[] bytes = excelWriter.write(export.getSelection(), export.getRows(), export.getColumns());
        String fileName = "loans-balance-list-" + LocalDate.now() + ".xlsx";
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
