package pfs.lms.enquiry.businesspartner.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public  class BupaRoleEntityFieldStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String bupaRoleCode;

    private String entity;

    private String fieldName;

    //0-Display Only
    //1 - Optional
    //2 - Mandatory
    //3 - Hide
    private Integer fieldStatus;
}