package pfs.lms.enquiry.applicationfee.invoice;

import java.util.List;
import java.util.UUID;

public interface IInvoicingDetailService {

    InvoicingDetail create(InvoicingDetailResource invoicingDetailResource, String username);

    InvoicingDetail update(InvoicingDetailResource invoicingDetailResource, String username) throws CloneNotSupportedException;

    List<String> getICCMeetingNumbers(UUID loanApplicationId);
}
