package pfs.lms.enquiry.applicationfee.invoice;

public interface IInvoicingDetailService {

    InvoicingDetail create(InvoicingDetailResource invoicingDetailResource, String username);

    InvoicingDetail update(InvoicingDetailResource invoicingDetailResource, String username) throws CloneNotSupportedException;
}
