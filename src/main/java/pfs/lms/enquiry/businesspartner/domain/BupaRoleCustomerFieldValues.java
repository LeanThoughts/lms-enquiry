package pfs.lms.enquiry.businesspartner.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public  class BupaRoleCustomerFieldValues {
    @Id
    private Integer id;

    private String bupaRoleCode;
    private String partnerGroup;

    private String reconAccount;
    private String dunningProcedure;
    private String planningGroup;
    private String paymentMethods;
    private String houseBank;
    private Boolean checkDoubleInvoice;
    private String paymentTerms;
    private String sortKey;
    private String roleGrouping;
 }