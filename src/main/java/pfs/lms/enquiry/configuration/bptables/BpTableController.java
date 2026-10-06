package pfs.lms.enquiry.configuration.bptables;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import pfs.lms.enquiry.config.ApiController;
import pfs.lms.enquiry.configuration.bptables.BpTableDtos.Overview;
import pfs.lms.enquiry.configuration.bptables.BpTableDtos.Row;
import pfs.lms.enquiry.configuration.bptables.BpTableDtos.RowRequest;
import pfs.lms.enquiry.configuration.bptables.BpTableDtos.TablePage;
import pfs.lms.enquiry.exception.LmsException;

import javax.servlet.http.HttpServletRequest;

/**
 * REST API of the Configuration apps of the business partner configuration tables (under /api from
 * {@link ApiController}). Rows are addressed by their id as a request parameter (ids are codes such as "0001").
 */
@ApiController
public class BpTableController {

    private static final String BASE = "/configuration/bp-tables";

    private final BpTableService service;

    public BpTableController(BpTableService service) {
        this.service = service;
    }

    @GetMapping(BASE)
    public ResponseEntity<Overview> getOverview(HttpServletRequest request) {
        return ResponseEntity.ok(service.getOverview(signedInUser(request)));
    }

    @GetMapping(BASE + "/{table}")
    public ResponseEntity<TablePage> getPage(@PathVariable String table,
                                             @RequestParam(required = false) Integer page,
                                             @RequestParam(required = false) Integer size,
                                             @RequestParam(required = false) String search,
                                             HttpServletRequest request) {
        return ResponseEntity.ok(service.getPage(table, page, size, search, signedInUser(request)));
    }

    @PostMapping(BASE + "/{table}/rows")
    public ResponseEntity<Row> create(@PathVariable String table, @RequestBody RowRequest row,
                                      HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(table, row.getValues(), signedInUser(request)));
    }

    @PutMapping(BASE + "/{table}/rows")
    public ResponseEntity<Row> update(@PathVariable String table, @RequestParam String id,
                                      @RequestBody RowRequest row, HttpServletRequest request) {
        return ResponseEntity.ok(service.update(table, id, row.getValues(), signedInUser(request)));
    }

    @DeleteMapping(BASE + "/{table}/rows")
    public ResponseEntity<Void> delete(@PathVariable String table, @RequestParam String id,
                                       HttpServletRequest request) {
        service.delete(table, id, signedInUser(request));
        return ResponseEntity.noContent().build();
    }

    private static String signedInUser(HttpServletRequest request) {
        String user = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : null;
        if (user == null) {
            throw new LmsException("Please sign in.", HttpStatus.UNAUTHORIZED);
        }
        return user;
    }
}
