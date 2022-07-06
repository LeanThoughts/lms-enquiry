package pfs.lms.enquiry.configInitializer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.domain.*;
import pfs.lms.enquiry.repository.*;

import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class Initializer implements CommandLineRunner {

    private final LoanClassRepository loanClassRepository;

    private final ProjectTypeRepository projectTypeRepository;

    private final FinancingTypeRepository financingTypeRepository;

    private final AssistanceTypeRepository assistanceTypeRepository;

    private final PartnerRepository partnerRepository;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    private final IndustrySectorRepository industrySectorRepository;

    private final UnitOfMeasureRepository unitOfMeasureRepository;

    private final UserRoleRepository userRoleRepository;

    private final RejectionCategoryRepository rejectionCategoryRepository;

    private final StateRepository stateRepository;

    private final EnquiryPortalCommonConfigRepository enquiryPortalCommonConfigRepository;

    private final LoanApplicationRepository loanApplicationRepository;

    @Override
    public void run(String... strings) throws Exception {

//        List<LoanApplication> loanApplicationList = loanApplicationRepository.findAll();
//        for (LoanApplication loanApplication: loanApplicationList) {
//             String loanContractID = loanApplication.getLoanContractId();
//            char ch = '1';
//            // Get the index
//            int index = 5;
//
//            //if (loanContractID.substring(0, index).equals("000002")) {
//
//                loanContractID = loanContractID.substring(0, index) + ch + loanContractID.substring(index + 1);
//                loanApplication.setLoanContractId(loanContractID);
//                loanApplicationRepository.saveAndFlush(loanApplication);
//            //}
//        }


        if (loanClassRepository.count() < 10) {
            LoanClass lc1 = new LoanClass("1", "PowerGen-Convl");
            LoanClass lc2 = new LoanClass("2", "PowerGen-Renew");
            LoanClass lc3 = new LoanClass("3", "Railways");
            LoanClass lc4 = new LoanClass("4", "Roads");
            LoanClass lc5 = new LoanClass("5", "Roads");
            LoanClass lc6 = new LoanClass("6", "Ports");
            LoanClass lc7 = new LoanClass("7", "Oil&Gas");
            LoanClass lc8 = new LoanClass("8", "Infrastructure");
            LoanClass lc9 = new LoanClass("9", "Airport");
            LoanClass lc10 = new LoanClass("10", "ElectrcMobility");
            LoanClass lc11 = new LoanClass("11", "WasteManagement");
            LoanClass lc12 = new LoanClass("12", "Mining");
            LoanClass lc13 = new LoanClass("13", "PowerTransmn");
            LoanClass lc14 = new LoanClass("14", "PowerDistribn");
            LoanClass lc15 = new LoanClass("15", "TL-Logistics");
            LoanClass lc16 = new LoanClass("16", "Shipyard");
            LoanClass lc17 = new LoanClass("17", "CityGasDistribn");
            LoanClass lc18 = new LoanClass("18", "Hotels&Resorts");
            LoanClass lc19 = new LoanClass("19", "RealEstate");
            LoanClass lc20 = new LoanClass("20", "Telecom");
            LoanClass lc21 = new LoanClass("21", "InlandWaterways");
            LoanClass lc22 = new LoanClass("22", "PublicTransport");
            LoanClass lc23 = new LoanClass("23", "Hospitals");
            LoanClass lc24 = new LoanClass("24", "EducationalInst");
            LoanClass lc25 = new LoanClass("25", "SportsInfra");
            LoanClass lc26 = new LoanClass("26", "EnrgyEfficiency");
            LoanClass lc27 = new LoanClass("41", "NFB-LOCforCAP");
            LoanClass lc28 = new LoanClass("42", "NFB-LOCforBG");
            LoanClass lc29 = new LoanClass("43", "NFB-LOCOthers");
            LoanClass lc30 = new LoanClass("51", "CL-Corp.Privat");
            LoanClass lc31 = new LoanClass("52", "CL-Corp.Govt");

            loanClassRepository.saveAll(Arrays.asList(lc1, lc2, lc3, lc4, lc5, lc6, lc7, lc8, lc9, lc10,
                    lc11, lc12, lc13, lc14, lc15, lc16, lc17, lc18, lc19, lc20, lc21, lc22,
                    lc23, lc24, lc25, lc26, lc27, lc28, lc29, lc30, lc31
            ));
            log.info("Added loan class sample data");
        }

        if (projectTypeRepository.count() < 18) {
            ProjectType pt1 = new ProjectType("1", "TL-PG-Thermal-Coal");
            ProjectType pt2 = new ProjectType("2", "TL-PG-Thermal-Lignite");
            ProjectType pt3 = new ProjectType("3", "TL-PG-Thermal-Gas");
            ProjectType pt4 = new ProjectType("4", "TL-PG-Hydro");
            ProjectType pt5 = new ProjectType("5", "TL-PG-Renewable-Solar");
            ProjectType pt6 = new ProjectType("6", "TL-PG-Renewable-Wind");
            ProjectType pt7 = new ProjectType("7", "TL-PG-Renewable-Biomass");
            ProjectType pt8 = new ProjectType("8", "Renewable-Co-Gen.");
            ProjectType pt9 = new ProjectType("9", "TL-RY-RailwaySiding");
            ProjectType pt10 = new ProjectType("10", "TL-UI-SmartCity");
            ProjectType pt11 = new ProjectType("11", "TL-RD-Roads-Toll");
            ProjectType pt12 = new ProjectType("12", "TL-RD-Roads-HAM");
            ProjectType pt13 = new ProjectType("13", "TL-PT-Ports");
            ProjectType pt14 = new ProjectType("14", "TL-OG-Oil&Gas");
            ProjectType pt15 = new ProjectType("15", "TL-OT-Others");
            ProjectType pt16 = new ProjectType("16", "TL-WI-WaterTreamentPlant");
            ProjectType pt17 = new ProjectType("17", "TL-WI-SewarageTreamentPlant");
            ProjectType pt18 = new ProjectType("18", "TL-WI-WaterDistribution");
            ProjectType pt19 = new ProjectType("19", "TL-AP-Airport");
            ProjectType pt20 = new ProjectType("20", "TL-EM-ChargingInfra");
            ProjectType pt21 = new ProjectType("21", "TL-EM-EM&ChargingInfra");
            ProjectType pt22 = new ProjectType("22", "TL-WM-WasteHandling");
            ProjectType pt23 = new ProjectType("23", "TL-WM-WastetoEnergy");
            ProjectType pt24 = new ProjectType("24", "TL-M-CoalMining");
            ProjectType pt25 = new ProjectType("25", "TL-PT-PowerTransmission");
            ProjectType pt26 = new ProjectType("26", "TL-PD-EnergyEfficiency");
            ProjectType pt27 = new ProjectType("27", "TL-PD-PowerDistribution");
            ProjectType pt28 = new ProjectType("28", "TL-PD-SmartMetering");
            ProjectType pt29 = new ProjectType("29", "TL-LG-Logistics");
            ProjectType pt30 = new ProjectType("41", "NFB-LOCforCAPEX");
            ProjectType pt31 = new ProjectType("42", "NFB-LOCforBG/PBG");
            ProjectType pt32 = new ProjectType("43", "NFB-LOCOthers");
            ProjectType pt33 = new ProjectType("51", "CL-CP-BridgeLoan-Capex");
            ProjectType pt34 = new ProjectType("52", "CL-CP-MezzanineLoan-Capex");
            ProjectType pt35 = new ProjectType("53", "CL-CP-MediumTermLoan-Capex");
            ProjectType pt36 = new ProjectType("54", "CL-CP-Debentures_Capex");
            ProjectType pt37 = new ProjectType("55", "CL-CP-RevolvingFacility-Capex");
            ProjectType pt38 = new ProjectType("56", "CL-CP-Others");
            ProjectType pt39 = new ProjectType("57", "CL-CG-MezzanineLoan-CashFlowMismatch");
            ProjectType pt40 = new ProjectType("58", "CL-CG-MediumTermLoan-CashFlowMismatc");
            ProjectType pt41 = new ProjectType("59", "CL-CG-Debentures-CashFlowMismatch");
            ProjectType pt42 = new ProjectType("60", "CL-CG-Others");

            projectTypeRepository.saveAll(Arrays.asList(
                    pt1, pt2, pt3, pt4, pt5, pt6, pt7, pt8, pt9, pt10, pt11, pt12,
                    pt13, pt14, pt15, pt16, pt17, pt18, pt19, pt20,
                    pt21, pt22, pt23, pt24, pt25, pt26, pt27, pt28, pt29, pt30, pt31, pt32,
                    pt33, pt34, pt35, pt36, pt37, pt38, pt39, pt40, pt41, pt42
            ));
            log.info("Added project type sample data");
        }


        if (financingTypeRepository.count() == 0) {
            FinancingType ft1 = new FinancingType("1", "Sole Lending");
            FinancingType ft2 = new FinancingType("2", "Multiple Banking");
            FinancingType ft3 = new FinancingType("3", "Consortium Lending");
            financingTypeRepository.saveAll(Arrays.asList(ft1, ft2, ft3));
            log.info("Added financing type sample data");
        }

        if (assistanceTypeRepository.count() == 0) {
            AssistanceType at1 = new AssistanceType("D", "Debt");
            AssistanceType at2 = new AssistanceType("E", "Equity");
            assistanceTypeRepository.saveAll(Arrays.asList(at1, at2));
            log.info("Added assistance type sample data");
        }

        if (userRepository.count() == 0) {
            User user1 = new User("Admin", "- PFS", "admin@gmail.com", "ZLM023", true, "admin@gmail.com", "50000284", "02", false);
            userRepository.save(user1);

//            User user2 = new User("Loan", "Officer - 1", "lo1@gmail.com", "ZLM013", true, "lo1@gmail.com", "50000284", "01");
//            userRepository.save(user2);
//
//            User user3 = new User("Gopinath", "B R", "gopinath.br@gmail.com", "TR0100", true, "gopinath.br@gmail.com", "", "03");
//            userRepository.save(user3);
        }

        if (productRepository.count() == 0) {
            Product p1 = new Product("301", "Bridge Loan");
            Product p2 = new Product("301", "Short Term Loan");
            Product p3 = new Product("303", "Term Loan");
            Product p4 = new Product("304", "Debentures");
            Product p5 = new Product("305", "Non Fund Based Loan");
            Product p6 = new Product("30F", "Facilities");
            Product p7 = new Product("310", "Facilities Drawdown-NFB Loan");
            Product p8 = new Product("311", "Facilities Drawdown-Term Loan");
            Product p9 = new Product("991", "Short Term Loan for Vehicle");
            productRepository.saveAll(Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9));
            log.info("Added products sample data");
        }

        if (industrySectorRepository.count() == 0) {
            IndustrySector i2 = new IndustrySector("1", "Power");
            IndustrySector i3 = new IndustrySector("2", "Railways");
            IndustrySector i4 = new IndustrySector("3", "Urban Infra");
            IndustrySector i5 = new IndustrySector("4", "Roads");
            IndustrySector i6 = new IndustrySector("5", "Ports");
            IndustrySector i7 = new IndustrySector("6", "Oil & Gas");
            IndustrySector i8 = new IndustrySector("7", "Corporates");
            IndustrySector i9 = new IndustrySector("8", "Infrastructure");
            IndustrySector i10 = new IndustrySector("9", "Others");
            IndustrySector i11 = new IndustrySector("10", "Energy Supply / Distribution");
            IndustrySector i12 = new IndustrySector("11", "Div. Holding comp");
            IndustrySector i13 = new IndustrySector("12", "Raw Materials");
            IndustrySector i14 = new IndustrySector("13", "Precious Metals");
            IndustrySector i15 = new IndustrySector("14", "Financial Services");
            IndustrySector i16 = new IndustrySector("15", "Real Estate");
            IndustrySector i17 = new IndustrySector("21", "Chemical Industry");
            IndustrySector i18 = new IndustrySector("22", "Health");
            IndustrySector i19 = new IndustrySector("23", "Glass");
            IndustrySector i20 = new IndustrySector("24", "Construction Industry");
            IndustrySector i21 = new IndustrySector("25", "Building Supplier");
            IndustrySector i22 = new IndustrySector("26", "Paper and Pulp");
            IndustrySector i23 = new IndustrySector("27", "Timber and Infrastructure");
            IndustrySector i24 = new IndustrySector("31", "Spinning Mill, Weaving Mill and Textile Refinement");
            IndustrySector i25 = new IndustrySector("32", "Apparel");
            IndustrySector i26 = new IndustrySector("41", "Iron and Steel");
            IndustrySector i27 = new IndustrySector("42", "Vehicles");
            IndustrySector i28 = new IndustrySector("43", "Vehicle Supplier");
            IndustrySector i29 = new IndustrySector("44", "Mechanical Engineering");
            IndustrySector i30 = new IndustrySector("45", "Specialized Mechanical Engineering");
            IndustrySector i31 = new IndustrySector("46", "Machine Tool Engineering");
            IndustrySector i32 = new IndustrySector("47", "Aircraft Construction");
            IndustrySector i33 = new IndustrySector("51", "Breweries, Beverages, Tobacco");
            IndustrySector i34 = new IndustrySector("52", "Nutrition");
            IndustrySector i35 = new IndustrySector("61", "Electricals / Electrical Engineering");
            IndustrySector i36 = new IndustrySector("62", "Computers and Data Processing");
            IndustrySector i37 = new IndustrySector("63", "Software");
            IndustrySector i38 = new IndustrySector("64", "Telecommunications");
            IndustrySector i39 = new IndustrySector("71", "Consumer Products");
            IndustrySector i40 = new IndustrySector("72", "Traffic and Transport");
            IndustrySector i41 = new IndustrySector("73", "Leisure and Hotel");
            IndustrySector i42 = new IndustrySector("81", "Commercial Banks");
            IndustrySector i43 = new IndustrySector("82", "Mortgage Banks");
            IndustrySector i44 = new IndustrySector("83", "Life Insurances");
            IndustrySector i45 = new IndustrySector("84", "Non-Life Insurances");
            IndustrySector i46 = new IndustrySector("85", "Reinsurances");
            IndustrySector i47 = new IndustrySector("86", "Insurance Holdings");
            IndustrySector i48 = new IndustrySector("91", "Trading");
            IndustrySector i49 = new IndustrySector("92", "Pharmaceutical Trade");
            IndustrySector i50 = new IndustrySector("93", "Publishing and Media");
            IndustrySector i51 = new IndustrySector("94", "Environment");


            industrySectorRepository.saveAll(Arrays.asList(i2, i3, i4, i5, i6, i7, i8, i9, i10));
            industrySectorRepository.saveAll(Arrays.asList(i11, i12, i13, i14, i15, i16, i17, i18, i19, i20));
            industrySectorRepository.saveAll(Arrays.asList(i21, i22, i23, i24, i25, i26, i27, i28, i29, i30));
            industrySectorRepository.saveAll(Arrays.asList(i31, i32, i33, i34, i35, i36, i37, i38, i39, i40));
            industrySectorRepository.saveAll(Arrays.asList(i41, i42, i43, i44, i45, i46, i47, i48, i49, i50, i51));
            log.info("Added Industry Sectors data");


        }

        if (unitOfMeasureRepository.count() == 0) {

            UnitOfMeasure u1 = new UnitOfMeasure("MW", "Mega Watts");
            UnitOfMeasure u2 = new UnitOfMeasure("KW", "Kilo Watts");
            UnitOfMeasure u3 = new UnitOfMeasure("KM", "Kilometers");
            UnitOfMeasure u4 = new UnitOfMeasure("KM2", "Square Kilometers");


            unitOfMeasureRepository.saveAll(Arrays.asList(u1, u2, u3, u4));
            log.info("Added Unit of Measures");
        }

        if (rejectionCategoryRepository.count() == 0) {
            RejectionCategory r1 = new RejectionCategory("1", "Rejected by Borrower");
            RejectionCategory r2 = new RejectionCategory("2", "Rejected by BD");
            RejectionCategory r3 = new RejectionCategory("3", "Rejected by ICC");
            RejectionCategory r4 = new RejectionCategory("4", "Rejected by Appraisal");
            RejectionCategory r5 = new RejectionCategory("5", "Rejected by Board");


            rejectionCategoryRepository.saveAll(Arrays.asList(r1, r2, r3, r4, r5));
            log.info("Added Rejection Categories");
        }


        if (userRoleRepository.count() == 0) {
            UserRole r1 = new UserRole("TR0100", "Loan Applicant");
            UserRole r2 = new UserRole("ZLM013", "Appraisal Officer");
            UserRole r3 = new UserRole("ZLM023", "Administrator");
            UserRole r4 = new UserRole("ZLM001", "Promoter");
            UserRole r5 = new UserRole("ZLM023", "Co-Appraisal Officer");
            UserRole r6 = new UserRole("ZLM010", "Appraisal Head");
            UserRole r7 = new UserRole("TR0110", "Prospect");


            userRoleRepository.saveAll(Arrays.asList(r1, r2, r3));
            log.info("Added User Roles");
        }

        if (userRoleRepository.count() <= 7) {

            UserRole r1 = new UserRole("ZLM002", "Lenders Financial Advisor");
            UserRole r2 = new UserRole("ZLM003", "Lenders Engineer");
            UserRole r3 = new UserRole("ZLM004", "Lenders Insurance Advisor");
            UserRole r4 = new UserRole("ZLM005", "Security Trustee");
            UserRole r5 = new UserRole("ZLM006", "Legal Counsel");
            UserRole r6 = new UserRole("ZLM007", "Loan underwriter");
            UserRole r7 = new UserRole("ZLM008", "Syndicate Partner");
            UserRole r8 = new UserRole("ZLM009", "Co-Security Trustee");
            UserRole r9 = new UserRole("ZLM011", "TRA Banker");
            UserRole r10 = new UserRole("ZLM012", "Consultant");
            UserRole r11 = new UserRole("ZLM014", "PFS Relationship officer");
            UserRole r12 = new UserRole("ZLM015", "EPC contractor");
            UserRole r13 = new UserRole("ZLM016", "Co-Lender");
            UserRole r14 = new UserRole("ZLM017", "Customer-Empl(Supp. 3rd Party");
            UserRole r15 = new UserRole("ZLM018", "Nodal Officer-Legal");
            UserRole r16 = new UserRole("ZLM019", "Nodal Officer-Disb&Recov");
            UserRole r17 = new UserRole("ZLM020", "Lead Bank");
            UserRole r18 = new UserRole("ZLM021", "Employee(SuppServ&3rd pa)");
            UserRole r19 = new UserRole("ZLM022", "Loan DocumentationOfficer");
            UserRole r20 = new UserRole("ZLM024", "Nodal Officer-Monitoring");
            userRoleRepository.saveAll(Arrays.asList(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10,
                    r11, r12, r13, r14, r15, r16, r17, r18, r19, r20));
            log.info("Added Additonal User Roles");
        }

        UserRole ur = userRoleRepository.findByCode("ZLM010");
        if (ur != null) {
            ur.setValue("Appraisal Head");
            userRoleRepository.save(ur);
        } else {
            ur = new UserRole("ZLM010", "Appraisal Head");
            userRoleRepository.save(ur);

        }
        ur = userRoleRepository.findByCode("ZLM025");
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
        ur = userRoleRepository.findByCode("ZLM040");
        if (ur != null) {
            ur.setValue("Monitoring Head");
            userRoleRepository.save(ur);
        } else {
            ur = new UserRole("ZLM040", "Monitoring Head");
            userRoleRepository.save(ur);
        }

        if (enquiryPortalCommonConfigRepository.count() == 0) {
            EnquiryPortalCommonConfig e1 = new EnquiryPortalCommonConfig("DEV", "info@leanthoughts.com");
            EnquiryPortalCommonConfig e2 = new EnquiryPortalCommonConfig("QA", "naveenkverma@ptcfinancial.com");
            EnquiryPortalCommonConfig e3 = new EnquiryPortalCommonConfig("PRD", "debt@ptcfinancial.com");
            EnquiryPortalCommonConfig e4 = new EnquiryPortalCommonConfig("", "info@leanthoughts.com");

            enquiryPortalCommonConfigRepository.saveAll(Arrays.asList(e1, e2, e3));
            log.info("Added Common Config.............");

        }

        if (stateRepository.count() == 0) {
            State s1 = new State("01", "Andra Pradesh");
            State s2 = new State("02", "Arunachal Pradesh");
            State s3 = new State("03", "Assam");
            State s4 = new State("04", "Bihar");
            State s5 = new State("05", "Goa");
            State s6 = new State("06", "Gujarat");
            State s7 = new State("07", "Haryana");
            State s8 = new State("08", "Himachal Pradesh");
            State s9 = new State("09", "Jammu und Kashmir");
            State s10 = new State("10", "Karnataka");
            State s11 = new State("11", "Kerala");
            State s12 = new State("12", "Madhya Pradesh");
            State s13 = new State("13", "Maharashtra");
            State s14 = new State("14", "Manipur");
            State s15 = new State("15", "Megalaya");
            State s16 = new State("16", "Mizoram");
            State s17 = new State("17", "Nagaland");
            State s18 = new State("18", "Orissa");
            State s19 = new State("19", "Punjab");
            State s20 = new State("20", "Rajasthan");
            State s21 = new State("21", "Sikkim");
            State s22 = new State("22", "Tamil Nadu");
            State s23 = new State("23", "Tripura");
            State s24 = new State("24", "Uttar Pradesh");
            State s25 = new State("25", "West Bengal");
            State s26 = new State("26", "Andaman and Nico.In.");
            State s27 = new State("27", "Chandigarh");
            State s28 = new State("28", "Dadra und Nagar Hav.");
            State s29 = new State("29", "Daman und Diu");
            State s30 = new State("30", "Delhi");
            State s31 = new State("31", "Lakshadweep");
            State s32 = new State("32", "Pondicherry");
            State s33 = new State("33", "Chhaattisgarh");
            State s34 = new State("34", "Jharkhand");
            State s35 = new State("35", "Uttaranchal");
            State s36 = new State("36", "Telangana");

            stateRepository.saveAll(Arrays.asList(s1,
                    s2,
                    s3,
                    s4,
                    s5,
                    s6,
                    s7,
                    s8,
                    s9,
                    s10,
                    s11,
                    s12,
                    s13,
                    s14,
                    s15,
                    s16,
                    s17,
                    s18,
                    s19,
                    s20,
                    s21,
                    s22,
                    s23,
                    s24,
                    s25,
                    s26,
                    s27,
                    s28,
                    s29,
                    s30,
                    s31,
                    s32,
                    s33,
                    s34,
                    s35,
                    s36
            ));
            log.info("Added STATES");

        }

    }


}
