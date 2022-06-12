package pfs.lms.enquiry.domain;

import lombok.*;
import org.hibernate.annotations.IndexColumn;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by sajeev on 15-Dec-18.
 */
@Entity
@ToString
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class ChangeDocument extends AuditModel  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nullable
    private UUID loanBusinessProcessObjectId; //Id of the Business Project Object i.e. Monitoring, Appraisal etc.

    @Nullable
    private String enitityId; // Id of Entity

    @Nullable
    private String mainEntityId; //Id of Main Entity


    @Nullable
    private String businessProcessName; //i.e Monitoring, Appraisal, etc.

    @Nullable
    private String subProcessName; // i.e. LIE, LFA etc.

    @Nullable
    private String userName;

    @Nullable
    private Date date;

    @Nullable
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private LoanApplication loanApplication;

    @Nullable
    private String loanContractId;

    @NotNull
    private String action;

    @Nullable
    private String tableKey;



    @Nullable
    @IndexColumn(name = "INDEX_COL12")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL )
    @JoinColumn(name="changeDocumentItem__Id",referencedColumnName = "id")
    private List<ChangeDocumentItem> changeDocumentItems;


    public ChangeDocumentItem addChangeDocumentItem (ChangeDocumentItem changeDocumentItem) {

        this.getChangeDocumentItems().add(changeDocumentItem);
        return changeDocumentItem;
    }




}
