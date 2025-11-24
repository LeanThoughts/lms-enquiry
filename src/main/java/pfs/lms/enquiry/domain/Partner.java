package pfs.lms.enquiry.domain;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class Partner extends AggregateRoot<Partner> implements Cloneable{
    //Workflow Status
    // 01 - Created //11 Updated
    // 02 - Sent for Approval
    // 03 - Approved
    // 04 - Rejected
    private Integer workFlowStatusCode;

    private String workFlowStatusDescription;
    private String processInstanceId;
    private Integer partyNumber;

    /**
     * 1 - Person
     * 2 - Organization
     * 3 - Group
     */
    private Integer partyCategory;

    private String partnerType;
    private String partnerGroup;
    private String partnerExternalNumber;

    /**
     * TR0110 - Prospect
     * ZLM013 - Appraisal Officer
     * ZLM010 - Co-Appraisal Officer
     * ZLM023 - PFS IT Team
     * ZLM001 - Promoter
     * TR0100 - Main Loan Partner
     * TR0110 - Co-Borrower
     */
    private String partyRole;

    @Size(max = 100)
    private String partyName1;

    @Size(max = 100)
    private String partyName2;

    @Size(max = 100)
    private String contactPersonName;

    private String addressLine1;

    private String addressLine2;

    private String addressLine3;
    private LocalDate addressValidFromDate;
    private String externalBPNumber;


    private String street;

    private String city;

    private String state;

    @Size(max = 8)
    private String postalCode;

    @Size(max = 2)
    private String country = "IN";

    @Column(unique = false)
    private String email;

    @Size(max = 15)
    private String contactNumber;

    @Size(max = 100)
    private String groupCompany;

    @Column(unique = false)
    private String userName;

    @Size(max = 100)
    private String password;

    private String pan;

    private String industrySector;

    @Size(max = 30)
    private String cinNumber;
    @Size(max = 30)
    private String gstNumber;
    @Size(max = 30)
    private String msmeRegisterNumber;
    @Size(max = 30)
    private String mobileNumber;
    @Size(max = 30)
    private String faxNumber;

    @Size(max = 10)
    private String searchTerm1;

    @Size(max = 10)
    private String searchTerm2;

    private String title;

    private String legalForm;
    private String legalEntity;
    private String houseBank;
    private String planningGroup;
    private String reconAccount;
    private String sortKey;
    private String dunningProcedure;
    private String paymentTerms;
    private String paymentMethod;
    private boolean checkDoubleInvoice;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PartnerRoleType> partnerRoleTypes;


    @Nullable
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL )
    @JoinColumn(name="partner__id",referencedColumnName = "id")
    private List<PartnerContact> partnerContacts;

    private String partnerCategory;

    private String defaultPartnerRole;

    public Partner(Integer partyNumber, String partnerGroup,String partnerType, String partnerExternalNumber,Integer partyCategory, String partyRole, @Size(max = 100) String partyName1, @Size(max = 100) String partyName2, @Size(max = 100) String contactPersonName, String addressLine1, String addressLine2, String street, String city, String state, @Size(max = 8) String postalCode, @Size(max = 2) String country, String email, @Size(max = 15) String contactNumber, @Size(max = 100) String groupCompany, String userName, @Size(max = 100) String password, String pan, String industrySector,String msmeRegisterNumber,String gstNumber,String cinNumber,String mobileNumber, String partnerCategory, String defaultPartnerRole, String title, String legalEntity, String legalForm, String houseBank) {
        this.partyNumber = partyNumber;
        this.partyCategory = partyCategory;
        this.partnerGroup = partnerGroup;
        this.partnerType  = partnerType;
        this.partnerExternalNumber = partnerExternalNumber;
        this.partyRole = partyRole;
        this.partyName1 = partyName1;
        this.partyName2 = partyName2;
        this.contactPersonName = contactPersonName;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.street = street;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
        this.email = email;
        this.contactNumber = contactNumber;
        this.groupCompany = groupCompany;
        this.userName = userName;
        this.password = password;
        this.pan = pan;
        this.industrySector = industrySector;
        this.msmeRegisterNumber = msmeRegisterNumber;
        this.gstNumber = gstNumber;
        this.cinNumber = cinNumber;
        this.mobileNumber = mobileNumber;
        this.partnerCategory = partnerCategory;
        this.defaultPartnerRole = defaultPartnerRole;
        this.title = title;
        this.legalEntity = legalEntity;
        this.legalForm = legalForm;
        this.houseBank = houseBank;
        registerEvent(PartnerCreated.of(this));
    }

    public Partner(@Size(max = 20) String userName, @Size(max = 100) String password) {
        this.userName = userName;
        this.password = password;
    }

    public Partner(String partyRole, @Size(max = 100) String partyName1, @Size(max = 100) String partyName2, String email, @Size(max = 15) String contactNumber, @Size(max = 100) String password) {
        this.partyRole = partyRole;
        this.partyName1 = partyName1;
        this.partyName2 = partyName2;
        this.email = email;
        this.contactNumber = contactNumber;
        this.password = password;
    }

    @Value
    @RequiredArgsConstructor(staticName = "of")
    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
    public static class PartnerCreated {
        final Partner partner;
    }

    public void addPartnerRole (PartnerRoleType partnerRoleType) {
        this.getPartnerRoleTypes().add(partnerRoleType);
    }

    public void addPartnerContact(PartnerContact partnerContact){
        this.getPartnerContacts().add(partnerContact);
    }

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }

    public String getPartyName() {
        return this.partyName1 + (this.partyName2 != null ? " " + this.partyName2 : "");
    }
}
