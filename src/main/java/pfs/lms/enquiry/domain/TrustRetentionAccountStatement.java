package pfs.lms.enquiry.domain;

import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.sql.Blob;

@Entity
@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class    TrustRetentionAccountStatement extends AbstractEntity {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private TrustRetentionAccount trustRetentionAccount;

    private String viewRights;

    private String remarks;

    private String periodQuarter;

    private String periodYear;

    private String documentType;

    private Blob documentContent;
}
