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
            role = new BusinessPartnerRoleType(null, "TR0100", "Main Loan Partner");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("TR0101");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "TR0101", "Co-Borrower");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("TR0110");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "TR0110", "Prospect");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("TR0113");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "TR0113", "Credit Standing Check");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("TR0115");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "TR0115", "Special Arrangement");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("TR0120");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "TR0120", "Cust. Authorized Drawer");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("TR0121");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "TR0121", "Other Loan Partner");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM001");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM001", "Promoter");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM002");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM002", "Lenders Financial Advisor");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM003");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM003", "Lenders Ind. Engineer");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM004");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM004", "Lenders Insurance Advisor");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM005");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM005", "Security Trustee");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM006");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM006", "Lenders Legal Counsel");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM007");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM007", "Loan underwriter");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM008");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM008", "Syndicate Partner");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM009");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM009", "Co-Security Trustee");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM010");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM010", "Co-Appraisal Officer");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM011");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM011", "TRA Banker");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM012");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM012", "Consultant");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM013");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM013", "Appraisal Officer");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM014");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM014", "PFS Relationship officer");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM015");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM015", "EPC contractor");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM016");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM016", "Co-Lender");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM017");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM017", "Customer-Empl(Supp&3rd Pa");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM018");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM018", "Nodal Officer-Legal");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM019");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM019", "Nodal Officer-Disb&Recov");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM020");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM020", "Lead Bank");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM021");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM021", "Employee(SuppServ&3rd pa)");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM022");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM022", "Loan DocumentationOfficer");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM023");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM023", "PFS IT Team");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM024");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM024", "Nodal Officer-Monitoring");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM025");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM025", "Key Promoter");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM026");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM026", "Group Company");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM027");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM027", "Technology Provider");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM028");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM028", "Monitoring Head");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM029");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM029", "TRA Authorized Person");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM030");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM030", "Nodal Officer Risk");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM031");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM031", "Risk Dept. Head");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM032");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM032", "Appraisal Head");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM033");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM033", "Bus. Development Head");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM034");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM034", "BD Officer");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM035");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM035", "Legal-Functional Head");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM036");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM036", "Disb&Recov-FunctionalHead");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM037");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM037", "Loan Doc-FunctionalHead");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM038");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM038", "Authorized Signatory");
            businessPartnerRoleTypeRepository.save(role);
        }
        role = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode("ZLM039");
        if (role == null) {
            role = new BusinessPartnerRoleType(null, "ZLM039", "Valuer");
            businessPartnerRoleTypeRepository.save(role);
        }

        return;
    }
}