package pfs.lms.enquiry.appraisal.proposaldetails;

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
public class ProposalDetailController {

    private final ProposalDetailService proposalDetailService;

    @PostMapping("/proposalDetails/create")
    public ResponseEntity<ProposalDetail> createProposalDetail(@RequestBody ProposalDetailResource proposalDetailResource,
                                                      HttpServletRequest request) {
        ProposalDetail proposalDetail = proposalDetailService.createProposalDetail(proposalDetailResource);
        return ResponseEntity.ok(proposalDetail);
    }

    @PutMapping("/proposalDetails/update")
    public ResponseEntity<ProposalDetail> updateProposalDetail(@RequestBody ProposalDetailResource proposalDetailResource,
                                                                   HttpServletRequest request) {
        ProposalDetail proposalDetail = proposalDetailService.updateProposalDetail(proposalDetailResource);
        return ResponseEntity.ok(proposalDetail);
    }
}
