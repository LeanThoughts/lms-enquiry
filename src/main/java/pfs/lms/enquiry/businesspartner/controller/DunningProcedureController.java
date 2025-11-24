package pfs.lms.enquiry.businesspartner.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import pfs.lms.enquiry.businesspartner.domain.DunningProcedure;
import pfs.lms.enquiry.businesspartner.domain.SortKey;
import pfs.lms.enquiry.businesspartner.repository.DunningProcedureRepository;
import pfs.lms.enquiry.businesspartner.repository.SortKeyRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;

@RepositoryRestController
@RequiredArgsConstructor
public class DunningProcedureController {

    private final DunningProcedureRepository dunningProcedureRepository;
    @GetMapping("/dunningprocedure")
    public ResponseEntity<List<DunningProcedure>> findAll(HttpServletRequest request) {

        List<DunningProcedure> dunningProcedures = dunningProcedureRepository.findAll();
        dunningProcedures.sort(Comparator.comparing(o ->o.getId()));

        return ResponseEntity.ok(dunningProcedures);
    }

}
