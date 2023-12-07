package pfs.lms.enquiry.applicationfee.invoice;

import lombok.*;
import pfs.lms.enquiry.applicationfee.ApplicationFee;
import pfs.lms.enquiry.domain.AggregateRoot;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"applicationFee", "companyName", "cinNumber", "gstNumber", "pan"}, callSuper = false)
public class InvoicingDetail extends AggregateRoot<InvoicingDetail> implements Cloneable {

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    private ApplicationFee applicationFee;

    private String iccMeetingNumber;
    private String companyName;
    private String cinNumber;
    private String gstNumber;
    private String pan;
    private String msmeRegistrationNumber;
    private String doorNumber;
    private String address;
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String landline;
    private String mobile;
    private String email;
    private String projectType;
    private String projectCapacityUnit;
    private String projectLocationState;

    private Double pfsDebtAmount;
    private Double projectCapacity;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
