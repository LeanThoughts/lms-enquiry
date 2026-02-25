package pfs.lms.enquiry.businesspartner.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BupaFICustomerVendorDetailResource {

    private UUID partnerId;

    private String reconAccount;
    private String dunningProcedure;
    private String planningGroup;
    private String paymentMethod;
    private String houseBank;
    private Boolean checkDoubleInvoice;
    private String paymentTerms;
    private String sortKey;
    private String roleGrouping;
}
