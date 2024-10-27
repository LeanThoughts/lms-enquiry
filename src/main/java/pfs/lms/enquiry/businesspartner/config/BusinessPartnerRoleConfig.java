package pfs.lms.enquiry.businesspartner.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerRole;
import pfs.lms.enquiry.businesspartner.repository.BusinessPartnerRoleRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class BusinessPartnerRoleConfig implements CommandLineRunner {

    private final BusinessPartnerRoleRepository businessPartnerRoleRepository;

    @Override
    public void run(String... strings) throws Exception {

        BusinessPartnerRole role = new BusinessPartnerRole();


        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("TR0100");
        if (role == null) {
            role = new BusinessPartnerRole(null, "TR0100", "Main Loan Partner");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("TR0101");
        if (role == null) {
            role = new BusinessPartnerRole(null, "TR0101", "Co-Borrower");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("TR0110");
        if (role == null) {
            role = new BusinessPartnerRole(null, "TR0110", "Prospect");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("TR0113");
        if (role == null) {
            role = new BusinessPartnerRole(null, "TR0113", "Credit Standing Check");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("TR0115");
        if (role == null) {
            role = new BusinessPartnerRole(null, "TR0115", "Special Arrangement");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("TR0120");
        if (role == null) {
            role = new BusinessPartnerRole(null, "TR0120", "Cust. Authorized Drawer");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("TR0121");
        if (role == null) {
            role = new BusinessPartnerRole(null, "TR0121", "Other Loan Partner");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("ZLM001");
        if (role == null) {
            role = new BusinessPartnerRole(null, "ZLM001", "Promoter");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("ZLM002");
        if (role == null) {
            role = new BusinessPartnerRole(null, "ZLM002", "Lenders Financial Advisor");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("ZLM003");
        if (role == null) {
            role = new BusinessPartnerRole(null, "ZLM003", "Lenders Ind. Engineer");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("ZLM004");
        if (role == null) {
            role = new BusinessPartnerRole(null, "ZLM004", "Lenders Insurance Advisor");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("ZLM005");
        if (role == null) {
            role = new BusinessPartnerRole(null, "ZLM005", "Security Trustee");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("ZLM006");
        if (role == null) {
            role = new BusinessPartnerRole(null, "ZLM006", "Lenders Legal Counsel");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("ZLM007");
        if (role == null) {
            role = new BusinessPartnerRole(null, "ZLM007", "Loan underwriter");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("ZLM008");
        if (role == null) {
            role = new BusinessPartnerRole(null, "ZLM008", "Syndicate Partner");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("ZLM009");
        if (role == null) {
            role = new BusinessPartnerRole(null, "ZLM009", "Co-Security Trustee");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("ZLM010");
        if (role == null) {
            role = new BusinessPartnerRole(null, "ZLM010", "Co-Appraisal Officer");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("ZLM011");
        if (role == null) {
            role = new BusinessPartnerRole(null, "ZLM011", "TRA Banker");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("ZLM012");
        if (role == null) {
            role = new BusinessPartnerRole(null, "ZLM012", "Consultant");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("ZLM013");
        if (role == null) {
            role = new BusinessPartnerRole(null, "ZLM013", "Appraisal Officer");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("ZLM014");
        if (role == null) {
            role = new BusinessPartnerRole(null, "ZLM014", "PFS Relationship officer");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("ZLM015");
        if (role == null) {
            role = new BusinessPartnerRole(null, "ZLM015", "EPC contractor");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("ZLM016");
        if (role == null) {
            role = new BusinessPartnerRole(null, "ZLM016", "Co-Lender");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("ZLM017");
        if (role == null) {
            role = new BusinessPartnerRole(null, "ZLM017", "Customer-Empl(Supp&3rd Pa");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("ZLM018");
        if (role == null) {
            role = new BusinessPartnerRole(null, "ZLM018", "Nodal Officer-Legal");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("ZLM019");
        if (role == null) {
            role = new BusinessPartnerRole(null, "ZLM019", "Nodal Officer-Disb&Recov");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("ZLM020");
        if (role == null) {
            role = new BusinessPartnerRole(null, "ZLM020", "Lead Bank");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("ZLM021");
        if (role == null) {
            role = new BusinessPartnerRole(null, "ZLM021", "Employee(SuppServ&3rd pa)");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("ZLM022");
        if (role == null) {
            role = new BusinessPartnerRole(null, "ZLM022", "Loan DocumentationOfficer");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("ZLM023");
        if (role == null) {
            role = new BusinessPartnerRole(null, "ZLM023", "PFS IT Team");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("ZLM024");
        if (role == null) {
            role = new BusinessPartnerRole(null, "ZLM024", "Nodal Officer-Monitoring");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("ZLM025");
        if (role == null) {
            role = new BusinessPartnerRole(null, "ZLM025", "Key Promoter");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("ZLM026");
        if (role == null) {
            role = new BusinessPartnerRole(null, "ZLM026", "Group Company");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("ZLM027");
        if (role == null) {
            role = new BusinessPartnerRole(null, "ZLM027", "Technology Provider");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("ZLM028");
        if (role == null) {
            role = new BusinessPartnerRole(null, "ZLM028", "Monitoring Head");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("ZLM029");
        if (role == null) {
            role = new BusinessPartnerRole(null, "ZLM029", "TRA Authorized Person");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("ZLM030");
        if (role == null) {
            role = new BusinessPartnerRole(null, "ZLM030", "Nodal Officer Risk");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("ZLM031");
        if (role == null) {
            role = new BusinessPartnerRole(null, "ZLM031", "Risk Dept. Head");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("ZLM032");
        if (role == null) {
            role = new BusinessPartnerRole(null, "ZLM032", "Appraisal Head");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("ZLM033");
        if (role == null) {
            role = new BusinessPartnerRole(null, "ZLM033", "Bus. Development Head");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("ZLM034");
        if (role == null) {
            role = new BusinessPartnerRole(null, "ZLM034", "BD Officer");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("ZLM035");
        if (role == null) {
            role = new BusinessPartnerRole(null, "ZLM035", "Legal-Functional Head");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("ZLM036");
        if (role == null) {
            role = new BusinessPartnerRole(null, "ZLM036", "Disb&Recov-FunctionalHead");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("ZLM037");
        if (role == null) {
            role = new BusinessPartnerRole(null, "ZLM037", "Loan Doc-FunctionalHead");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("ZLM038");
        if (role == null) {
            role = new BusinessPartnerRole(null, "ZLM038", "Authorized Signatory");
            businessPartnerRoleRepository.save(role);
        }
        role = businessPartnerRoleRepository.findBusinessPartnerRoleByCode("ZLM039");
        if (role == null) {
            role = new BusinessPartnerRole(null, "ZLM039", "Valuer");
            businessPartnerRoleRepository.save(role);
        }

        return;
    }
}