package pfs.lms.enquiry.configInitializer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.domain.*;
import pfs.lms.enquiry.repository.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sajeev on 14-May-21.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserRoleConfigInitializer implements CommandLineRunner {


    @Autowired
    UserRoleRepository userRoleRepository;


    @Override
    public void run(String... strings) throws Exception {



        List<UserRole> userRoleList = new ArrayList<>();

        if (userRoleRepository.findByCode("TR0100") == null ) {
            UserRole r1 = new UserRole("TR0100", "Loan Applicant");
            userRoleList.add(r1);

        if (userRoleRepository.findByCode("ZLM001") == null ) {
            UserRole r4  = new UserRole("ZLM001", "Promoter");
            userRoleList.add(r4 );
        }
            if (userRoleRepository.findByCode("ZLM002") == null ) {
                UserRole r8 = new UserRole("ZLM002", "Lenders Financial Advisor");
                userRoleList.add(r8);
            }
        if (userRoleRepository.findByCode("ZLM023") == null ) {
            UserRole r5  = new UserRole("ZLM023", "Co-Appraisal Officer");
            userRoleList.add(r5);
        }
        if (userRoleRepository.findByCode("ZLM010") == null ) {
            UserRole r6  = new UserRole("ZLM010", "Appraisal Head");
            userRoleList.add(r6 );
        }
        if (userRoleRepository.findByCode("TR0110") == null ) {
            UserRole r7  = new UserRole("TR0110", "Prospect");
            userRoleList.add(r7 );
        }

        if (userRoleRepository.findByCode("ZLM003") == null ) {
            UserRole r9 = new UserRole("ZLM003", "Lenders Engineer");
            userRoleList.add(r9);
        }
        if (userRoleRepository.findByCode("ZLM004") == null ) {
            UserRole r10 = new UserRole("ZLM004", "Lenders Insurance Advisor");
            userRoleList.add(r10);
        }
        if (userRoleRepository.findByCode("ZLM005") == null ) {
            UserRole r11 = new UserRole("ZLM005", "Security Trustee");
            userRoleList.add(r11);
        }
        if (userRoleRepository.findByCode("ZLM006") == null ) {
            UserRole r12 = new UserRole("", "Legal Counsel");
            userRoleList.add(r12);
        }
        if (userRoleRepository.findByCode("ZLM007") == null ) {
            UserRole r13 = new UserRole("ZLM007", "Loan underwriter");
            userRoleList.add(r13);
        }
        if (userRoleRepository.findByCode("ZLM008") == null ) {
            UserRole r14 = new UserRole("ZLM008", "Syndicate Partner");
            userRoleList.add(r14);
        }
        if (userRoleRepository.findByCode("ZLM009") == null ) {
            UserRole r15 = new UserRole("ZLM009", "Co-Security Trustee");
            userRoleList.add(r15);
        }
            UserRole ur = userRoleRepository.findByCode("ZLM010");
            if (ur != null) {
                ur.setValue("Appraisal Head");
                userRoleRepository.save(ur);
            } else {
                ur = new UserRole("ZLM010", "Appraisal Head");
                userRoleRepository.save(ur);

            }
        if (userRoleRepository.findByCode("ZLM011") == null ) {
            UserRole r16 = new UserRole("ZLM011", "TRA Banker");
            userRoleList.add(r16);
        }
        if (userRoleRepository.findByCode("ZLM012") == null ) {
            UserRole r17 = new UserRole("ZLM012", "Consultant");
            userRoleList.add(r17);
        }
        }
        if (userRoleRepository.findByCode("ZLM013") == null ) {
            UserRole r2 = new UserRole("ZLM013", "Appraisal Officer");
            userRoleList.add(r2);
        }


        if (userRoleRepository.findByCode("ZLM014") == null ) {
            UserRole r19 = new UserRole("PFS Relationship officer", "ZLM014");
            userRoleList.add(r19);
        }
        if (userRoleRepository.findByCode("ZLM015") == null ) {
            UserRole r20 = new UserRole("ZLM015", "EPC contractor");
            userRoleList.add(r20);
        }
        if (userRoleRepository.findByCode("ZLM016") == null ) {
            UserRole r21 = new UserRole("ZLM016", "Co-Lender");
            userRoleList.add(r21);
        }
        if (userRoleRepository.findByCode("ZLM017") == null ) {
            UserRole r22 = new UserRole("ZLM017", "Customer-Empl(Supp. 3rd Party)");
            userRoleList.add(r22);
        }
        if (userRoleRepository.findByCode("ZLM018") == null ) {
            UserRole r23 = new UserRole("ZLM018", "Nodal Officer-Legal");
            userRoleList.add(r23);
        }
        if (userRoleRepository.findByCode("ZLM019") == null ) {
            UserRole r24 = new UserRole("ZLM019", "Nodal Officer-Disb&Recovery");
            userRoleList.add(r24);
        }

        if (userRoleRepository.findByCode("ZLM020") == null ) {
            UserRole r25 = new UserRole("ZLM020", "Lead Bank");
            userRoleList.add(r25);
        }
        if (userRoleRepository.findByCode("ZLM021") == null ) {
            UserRole r26 = new UserRole("ZLM021", "Employee(SuppServ&3rd party");
            userRoleList.add(r26);
        }
        if (userRoleRepository.findByCode("ZLM022") == null ) {
            UserRole r27 = new UserRole("", "Loan DocumentationOfficer");
            userRoleList.add(r27);
        }

        if (userRoleRepository.findByCode("ZLM023") == null ) {
            UserRole r3  = new UserRole("ZLM023", "Administrator");
            userRoleList.add(r3 );
        }
        if (userRoleRepository.findByCode("ZLM024") == null ) {
            UserRole r29 = new UserRole("ZLM024", "Nodal Officer-Monitoring");
            userRoleList.add(r29);
        }

        UserRole ur = userRoleRepository.findByCode("ZLM025");
        if (ur != null) {
            ur.setValue("Key Promoter");
            userRoleRepository.save(ur);
        } else {
            ur = new UserRole("ZLM025", "Key Promoter");
            userRoleRepository.save(ur);
        }

        ur = userRoleRepository.findByCode("ZLM026");
        if (ur != null) {
            ur.setValue("Group Company");
            userRoleRepository.save(ur);
        } else {
            ur = new UserRole("ZLM026", "Group Company");
            userRoleRepository.save(ur);
        }

        ur = userRoleRepository.findByCode("ZLM027");
        if (ur != null) {
            ur.setValue("Technology Provide");
            userRoleRepository.save(ur);
        } else {
            ur = new UserRole("ZLM027", "Technology Provider");
            userRoleRepository.save(ur);
        }

        if (userRoleRepository.findByCode("ZLM033") == null ) {
            UserRole r30  = new UserRole("ZLM033", "Business Development Head");
            userRoleList.add(r30);
        }
        if (userRoleRepository.findByCode("ZLM034") == null ) {
            UserRole r31  = new UserRole("ZLM034", "Business Development Officer");
            userRoleList.add(r31);
        }
        ur = userRoleRepository.findByCode("ZLM040");
        if (ur != null) {
            ur.setValue("Monitoring Head");
            userRoleRepository.save(ur);
        } else {
            ur = new UserRole("ZLM040", "Monitoring Head");
            userRoleRepository.save(ur);
        }


        userRoleRepository.saveAll(userRoleList);
        log.info("Added User Roles");







    }

}
