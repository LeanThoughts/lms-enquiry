package pfs.lms.enquiry.sanction.paymentreceiptpresanction;

import java.util.UUID;

public interface IPaymentReceiptPreSanctionService {

    PaymentReceiptPreSanction createPaymentReceipt(PaymentReceiptPreSanctionResource paymentReceiptPreSanctionResource,
                                                   String username);

    PaymentReceiptPreSanction updatePaymentReceipt(PaymentReceiptPreSanctionResource paymentReceiptPreSanctionResource,
                                                   String username) throws CloneNotSupportedException;

    PaymentReceiptPreSanction deletePaymentReceipt(UUID paymentReceiptId) throws CloneNotSupportedException;
}
