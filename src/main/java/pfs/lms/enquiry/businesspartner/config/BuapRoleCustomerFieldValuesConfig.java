package pfs.lms.enquiry.businesspartner.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.businesspartner.domain.AmendmentReason;
import pfs.lms.enquiry.businesspartner.domain.BupaRoleCustomerFieldValues;
import pfs.lms.enquiry.businesspartner.repository.AmendmentReasonRepository;
import pfs.lms.enquiry.businesspartner.repository.BupaRoleCustomerFieldValuesRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class BuapRoleCustomerFieldValuesConfig implements CommandLineRunner {

    private final BupaRoleCustomerFieldValuesRepository bupaRoleCustomerFieldValuesRepository;
 
    @Override
    public void run(String... strings) throws Exception {

        BupaRoleCustomerFieldValues bupaRoleCustomerFieldValues = new BupaRoleCustomerFieldValues();

        bupaRoleCustomerFieldValues = bupaRoleCustomerFieldValuesRepository.findByBupaRoleCode("BUP001");if (bupaRoleCustomerFieldValues == null){ bupaRoleCustomerFieldValues = new BupaRoleCustomerFieldValues("BUP001","21000000","PFS","A1","T","SBI01",false,"001","001","001");}


    }
}