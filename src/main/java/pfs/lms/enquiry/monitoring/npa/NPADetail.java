package pfs.lms.enquiry.monitoring.npa;

import lombok.*;
import pfs.lms.enquiry.domain.AggregateRoot;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class NPADetail extends AggregateRoot<NPADetail> implements Cloneable {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private NPA npa;

    private String loanNumber;
    private Integer lineItemNumber;
    private String npaAssetClass;
    private LocalDate assetClassificationChangeDate;
    private LocalDate provisionDate;
    private Double percentageSecured;
    private Double percentageUnsecured;
    private Double absoluteValue;
    private Double loanAssetValue;
    private Double securedLoanAsset;
    private Double unsecuredLoanAsset;
    private Double npaProvisionValue;
    private Double netAssetValue;
    private String remarks;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
