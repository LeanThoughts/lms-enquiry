package pfs.lms.enquiry.businesspartner.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import pfs.lms.enquiry.businesspartner.domain.SortKey;
import pfs.lms.enquiry.businesspartner.repository.SortKeyRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;

@RepositoryRestController
@RequiredArgsConstructor
public class SortKeyController {

    private final SortKeyRepository sortKeyRepository;
    @GetMapping("/sortkey")
    public ResponseEntity<List<SortKey>> findAll(HttpServletRequest request) {

        List<SortKey> sortKey = sortKeyRepository.findAll();
        sortKey.sort(Comparator.comparing(o ->o.getId()));

        return ResponseEntity.ok(sortKey);
    }

}
