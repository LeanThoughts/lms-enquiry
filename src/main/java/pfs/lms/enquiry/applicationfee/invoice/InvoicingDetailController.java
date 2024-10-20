package pfs.lms.enquiry.applicationfee.invoice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Slf4j
@RepositoryRestController
@RequiredArgsConstructor
public class InvoicingDetailController {

    private final IInvoicingDetailService invoicingDetailService;

    @PostMapping("/invoicingDetails/create")
    public ResponseEntity<InvoicingDetail> create(@RequestBody InvoicingDetailResource invoicingDetailResource,
                                                  HttpServletRequest request) {

        return ResponseEntity.ok(invoicingDetailService.create(invoicingDetailResource,
                request.getUserPrincipal().getName()));
    }

    @PutMapping("/invoicingDetails/update")
    public ResponseEntity<InvoicingDetail> update(@RequestBody InvoicingDetailResource invoicingDetailResource,
                                                  HttpServletRequest request) throws CloneNotSupportedException {

        return ResponseEntity.ok(invoicingDetailService.update(invoicingDetailResource,
                request.getUserPrincipal().getName()));
    }

    @GetMapping("/invoicingDetails/meetingNumbers/{loanApplicationId}")
    public ResponseEntity<List<MeetingNumber>> getMeetingNumbers(@PathVariable UUID loanApplicationId,
                                                         HttpServletRequest request) throws CloneNotSupportedException {

        return ResponseEntity.ok(invoicingDetailService.getICCMeetingNumbers(loanApplicationId));
    }
}
