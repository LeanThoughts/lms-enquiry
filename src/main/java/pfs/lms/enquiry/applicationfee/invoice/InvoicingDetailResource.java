package pfs.lms.enquiry.applicationfee.invoice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class InvoicingDetailResource {

    private UUID id;
    private UUID loanApplicationId;

    private UUID partnerId;
}
