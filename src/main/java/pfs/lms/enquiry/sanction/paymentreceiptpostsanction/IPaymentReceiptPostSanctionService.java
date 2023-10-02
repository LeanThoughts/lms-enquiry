package pfs.lms.enquiry.sanction.paymentreceiptpostsanction;

import java.util.UUID;

public interface IPaymentReceiptPostSanctionService {

    PaymentReceiptPostSanction createPaymentReceipt(PaymentReceiptPostSanctionResource paymentReceiptPostSanctionResource,
                                                    String username);

    PaymentReceiptPostSanction updatePaymentReceipt(PaymentReceiptPostSanctionResource paymentReceiptPostSanctionResource,
                                                    String username) throws CloneNotSupportedException;

    PaymentReceiptPostSanction deletePaymentReceipt(UUID paymentReceiptId, String username) throws CloneNotSupportedException;
}
