package pfs.lms.enquiry.businesspartner.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerRoleType;
import pfs.lms.enquiry.businesspartner.repository.BusinessPartnerRoleRepository;
import pfs.lms.enquiry.businesspartner.repository.BusinessPartnerRoleTypeRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class BusinessPartnerRoleConfig implements CommandLineRunner {

    private final BusinessPartnerRoleTypeRepository businessPartnerRoleTypeRepository;

    @Override
    public void run(String... strings) throws Exception {

        BusinessPartnerRoleType role = new BusinessPartnerRoleType();


        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("TR0100");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "TR0100", "Main Loan Partner", "0001");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("TR0101");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "TR0101", "Co-Borrower","0001");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("TR0110");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "TR0110", "Prospect","0001");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("TR0113");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "TR0113", "Credit Standing Check","0001");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("TR0115");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "TR0115", "Special Arrangement","0001");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("TR0120");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "TR0120", "Cust. Authorized Drawer","0001");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("TR0121");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "TR0121", "Other Loan Partner","0001");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM001");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM001", "Promoter","0013");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM002");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM002", "Lenders Financial Advisor","0013");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM003");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM003", "Lenders Ind. Engineer","0013");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM004");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM004", "Lenders Insurance Advisor","0013");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM005");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM005", "Security Trustee","0013");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM006");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM006", "Lenders Legal Counsel","0013");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM007");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM007", "Loan underwriter","0013");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM008");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM008", "Syndicate Partner","0013");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM009" );
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM009", "Co-Security Trustee","0013");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM010");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM010", "Co-Appraisal Officer","0009");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM011");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM011", "TRA Banker","0013");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM012");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM012", "Consultant","0003");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM013");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM013", "Appraisal Officer","0009");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM014");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM014", "PFS Relationship officer","0009");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM015");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM015", "EPC contractor","0013");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM016");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM016", "Co-Lender","0013");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM017");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM017", "Customer-Empl(Supp&3rd Party)", "0016");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM018");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM018", "Nodal Officer-Legal","0009");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM019");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM019", "Nodal Officer-Disb&Recov","0009");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM020");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM020", "Lead Bank","0013");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM021");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM021", "Casual Emp.(Support Services)", "0014");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM022");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM022", "Loan DocumentationOfficer","0009");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM023");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM023", "PFS IT Team", "0009");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM024");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM024", "Nodal Officer-Monitoring","0009");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM025");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM025", "Key Promoter","0013");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM026");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM026", "Group Company","0013");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM027");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM027", "Technology Provider","0003");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM028");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM028", "Monitoring Head","0009");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM029");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM029", "TRA Authorized Person","0013");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM030");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM030", "Nodal Officer Risk","0009");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM031");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM031", "Risk Dept. Head","0009");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM032");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM032", "Appraisal Head","0009");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM033");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM033", "Bus. Development Head","0009");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM034");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM034", "BD Officer","0009");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM035");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM035", "Legal-Functional Head","0009");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM036");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM036", "Disb&Recov-FunctionalHead","0009");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM037");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM037", "Loan Doc-FunctionalHead","0009");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM038");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM038", "Authorized Signatory","0013");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM039");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM039", "Valuer","0013");
            businessPartnerRoleTypeRepository.save(role);
        }

        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM040");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM040", "Contractual Third Party","0015");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM041");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM041", "Treasury-Functional Head","0009");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM042");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM042", "Operations-FunctionalHead","0009");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM043");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM043", "Nodal Officer-Treasury","0009");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM044");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM044", "Nodal Officer-Operations","0009");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("BUP001");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "BUP001", "Contact Person","0013");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("BUP003");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "BUP003", "Employee New","F006");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("FLCU00");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "FLCU00", "FI Customer","0010");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("FLVN00");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "FLVN00", "FI Vendor","0003");
            businessPartnerRoleTypeRepository.save(role);
        }




        return;
    }
}