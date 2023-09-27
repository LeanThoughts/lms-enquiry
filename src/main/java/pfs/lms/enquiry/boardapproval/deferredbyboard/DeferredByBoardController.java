package pfs.lms.enquiry.boardapproval.deferredbyboard;

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
public class DeferredByBoardController {

    private final DeferredByBoardService service;

    @PostMapping("/deferredByBoards/create")
    public ResponseEntity<DeferredByBoard> create(@RequestBody DeferredByBoardResource resource,
                                                  HttpServletRequest request) {
        return ResponseEntity.ok(service.create(resource, request.getUserPrincipal().getName()));
    }

    @PutMapping("/deferredByBoards/update")
    public ResponseEntity<DeferredByBoard> update(@RequestBody DeferredByBoardResource resource,
                                                  HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(service.update(resource, request.getUserPrincipal().getName()));
    }

    @DeleteMapping("/deferredByBoards/delete/{id}")
    public ResponseEntity<DeferredByBoard> update(@PathVariable("id") UUID id,
                                                  HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(service.delete(id,  request.getUserPrincipal().getName()));
    }
}
