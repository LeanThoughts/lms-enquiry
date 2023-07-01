package pfs.lms.enquiry.action.projectproposal.shareholder;

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
public class ShareHolderController {

    private final IShareHolderService shareHolderService;

    @PostMapping("/shareHolders/create")
    public ResponseEntity<ShareHolder> create(@RequestBody ShareHolderResource resource,
                                              HttpServletRequest request) {
        return ResponseEntity.ok(shareHolderService.create(resource, request.getUserPrincipal().getName()));
    }

    @PutMapping("/shareHolders/update")
    public ResponseEntity<ShareHolder> update(@RequestBody ShareHolderResource resource,
                                              HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(shareHolderService.update(resource, request.getUserPrincipal().getName()));
    }

    @DeleteMapping("/shareHolders/delete/{id}")
    public ResponseEntity<ShareHolder> delete(@PathVariable("id") UUID shareHolderId, HttpServletRequest request) {
        ShareHolder shareHolder = shareHolderService.delete(shareHolderId,request.getUserPrincipal().getName());
        return ResponseEntity.ok(shareHolder);
    }
}
