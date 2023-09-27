package pfs.lms.enquiry.mail.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import pfs.lms.enquiry.config.ApiController;
import pfs.lms.enquiry.mail.domain.MailObject;
import pfs.lms.enquiry.mail.repository.MailObjectRepository;
import pfs.lms.enquiry.mail.resource.MailSearchResource;
import pfs.lms.enquiry.mail.service.EmailQueryService;
import pfs.lms.enquiry.mail.service.RiskNotificationService;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ApiController
@RequiredArgsConstructor
public class MailController {

    private final LoanApplicationRepository loanApplicationRepository;


    @Autowired
    UserRepository userRepository;

    @Autowired
    MailObjectRepository mailObjectRepository;

    @Autowired
    EmailQueryService emailQueryService;

    @Autowired
    RiskNotificationService riskNotificationService;

    @GetMapping("/mailObjects/queryParams")
    public ResponseEntity getMailsByQueryParamas(@RequestParam("query") String[] queryParams, HttpServletRequest httpServletRequest) {

        List<MailObject> mailObjects = emailQueryService.searchMailObject(queryParams);

        if (mailObjects != null)
            return ResponseEntity.ok(mailObjects);
        else
            return ResponseEntity.noContent().build();

    }



    @PutMapping("/mailObjects/search")
    public ResponseEntity search(@RequestBody MailSearchResource resource, HttpServletRequest request,
                                 @PageableDefault(sort = "dateTime",
                                         size = 9999,
                                         direction = Sort.Direction.DESC) Pageable pageable) {



        List<MailObject> mailObjects = emailQueryService.searchMailObjectByResource(resource);

        if (mailObjects != null)
            return ResponseEntity.ok(mailObjects);
        else
            return ResponseEntity.noContent().build();


    }

}
