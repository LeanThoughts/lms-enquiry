package pfs.lms.enquiry.action.projectproposal.otherdetailsdocuent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RepositoryRestController
@RequiredArgsConstructor
public class OtherDetailsDocumentController {

    private final IOtherDetailsDocumentService otherDetailsDocumentService;

    @PostMapping("/otherDetailsDocuments/create")
    public ResponseEntity<OtherDetailsDocument> create(@RequestBody OtherDetailsDocumentResource resource,
                                                       HttpServletRequest request) {
        return ResponseEntity.ok(otherDetailsDocumentService.create(resource, request.getUserPrincipal().getName()));
    }

    @PutMapping("/otherDetailsDocuments/update")
    public ResponseEntity<OtherDetailsDocument> update(@RequestBody OtherDetailsDocumentResource resource,
                                                       HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(otherDetailsDocumentService.update(resource, request.getUserPrincipal().getName()));
    }
}
