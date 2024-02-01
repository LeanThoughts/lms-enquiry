package pfs.lms.enquiry.documentation;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.springframework.lang.Nullable;
import pfs.lms.enquiry.domain.AggregateRoot;
import pfs.lms.enquiry.domain.LoanApplication;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
@Setter
@Getter
@ToString
@EqualsAndHashCode(of = {"loanApplication"}, callSuper = false)
public class Documentation extends AggregateRoot<Documentation> implements Cloneable {

    @DiffIgnore
    @NotNull
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private LoanApplication loanApplication;

    @Nullable
    private String loanContractId;

    private String processInstanceId;

    private Integer workFlowStatusCode;

    private String workFlowStatusDescription;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }

    public Documentation() {
        this.workFlowStatusCode = 1;
        this.workFlowStatusDescription = "Created";
    }
}
