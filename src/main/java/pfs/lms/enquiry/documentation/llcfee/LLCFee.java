package pfs.lms.enquiry.documentation.llcfee;

import lombok.*;
import pfs.lms.enquiry.documentation.Documentation;
import pfs.lms.enquiry.domain.AggregateRoot;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"documentation", "serialNumber", "bpCode"}, callSuper = false)
public class LLCFee extends AggregateRoot<LLCFee> implements Cloneable {

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Documentation documentation;

    private Integer serialNumber;

    private LocalDate invoiceDate;
    private String invoiceNumber;
    private String feeName;

    private Double feeAmount;
    private String statusOfFeeReceipt;
    private String remarks;
    private String documentName;
    private String documentType;
    private String fileReference;

    private Boolean deleteFlag;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
