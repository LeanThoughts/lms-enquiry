package pfs.lms.enquiry.domain;

import lombok.*;

import javax.persistence.Entity;

/**
 * Created by sajeev on 08-Apr-21.
 */
@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class PartnerContact extends AggregateRoot<PartnerContact> {

//    @DiffIgnore
//    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER,optional = false)
//    @JoinColumn(name = "partner__id", nullable = false)
//    private Partner partner;

    private Integer serialNumber;

    private String printInDemandLetter;

    private String loanContractId;

    private String contactName;

    private String branchAddress;

    private String designation;

    private String department;

    private String mobilePhoneNumber;

    private String landPhoneNumber;

    private String email;

    private String faxNumber;





}
