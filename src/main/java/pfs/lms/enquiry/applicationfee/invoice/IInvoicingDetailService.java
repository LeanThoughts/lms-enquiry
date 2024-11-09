package pfs.lms.enquiry.applicationfee.invoice;

import java.util.List;
import java.util.UUID;

import pfs.lms.enquiry.domain.Partner;

public interface IInvoicingDetailService {

    InvoicingDetail create(InvoicingDetailResource invoicingDetailResource, String username) throws CloneNotSupportedException;

    InvoicingDetail update(InvoicingDetailResource invoicingDetailResource, String username) throws CloneNotSupportedException;

    List<MeetingNumber> getICCMeetingNumbers(UUID loanApplicationId);

    List<Partner> searchPartners(PartnerSearchResource partnerSearchResource);
}
