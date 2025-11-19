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
public  class BupaRoleEntitySetFieldStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String bupaRoleCode;

    private String entitySet;

    private String fieldName;

    private String keyFieldValue;

    private boolean isKeyField;

    //0-Display Only
    //1 - Optional
    //2 - Mandatory
    //3 - Hide
    private Integer fieldStatus;
}