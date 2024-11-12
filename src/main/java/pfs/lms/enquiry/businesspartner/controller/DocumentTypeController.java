package pfs.lms.enquiry.businesspartner.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import pfs.lms.enquiry.businesspartner.domain.DocumentType;
import pfs.lms.enquiry.businesspartner.repository.DocumentTypeRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;

@RepositoryRestController
@RequiredArgsConstructor
public class DocumentTypeController {

    private final DocumentTypeRepository documentTypeRepository;
    @GetMapping("/documentTypes")
    public ResponseEntity<List<DocumentType>> findAll(HttpServletRequest request) {

        List<DocumentType> documentTypes = documentTypeRepository.findAll();
        documentTypes.sort(Comparator.comparing(o ->o.getDescription()));

        return ResponseEntity.ok(documentTypes);
    }

}
