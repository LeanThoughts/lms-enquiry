package pfs.lms.enquiry.boardapproval.rejectedbyboard;

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
public class RejectedByBoardController {

    private final RejectedByBoardService service;

    @PostMapping("/rejectedByBoards/create")
    public ResponseEntity<RejectedByBoard> create(@RequestBody RejectedByBoardResource resource,
                                                  HttpServletRequest request) {
        return ResponseEntity.ok(service.create(resource, request.getUserPrincipal().getName()));
    }

    @PutMapping("/rejectedByBoards/update")
    public ResponseEntity<RejectedByBoard> update(@RequestBody RejectedByBoardResource resource,
                                                  HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(service.update(resource, request.getUserPrincipal().getName()));
    }

    @DeleteMapping("/rejectedByBoards/delete/{id}")
    public ResponseEntity<RejectedByBoard> update(@PathVariable("id") UUID id,
                                                  HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(service.delete(id));
    }
}
