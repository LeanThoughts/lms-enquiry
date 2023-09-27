package pfs.lms.enquiry.boardapproval.approvalbyboard;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Slf4j
@RepositoryRestController
@RequiredArgsConstructor
public class ApprovalByBoardController {

    private final ApprovalByBoardService service;

    @PostMapping("/approvalByBoards/create")
    public ResponseEntity<ApprovalByBoard> create(@RequestBody ApprovalByBoardResource resource,
                                                  HttpServletRequest request) {
        return ResponseEntity.ok(service.create(resource, request.getUserPrincipal().getName()));
    }

    @PutMapping("/approvalByBoards/update")
    public ResponseEntity<ApprovalByBoard> update(@RequestBody ApprovalByBoardResource resource,
                                                  HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(service.update(resource, request.getUserPrincipal().getName()));
    }

    @DeleteMapping("/approvalByBoards/delete/{id}")
    public ResponseEntity<ApprovalByBoard> update(@PathVariable("id") UUID id,
                                                  HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(service.delete(id, request.getUserPrincipal().getName()));
    }
}
