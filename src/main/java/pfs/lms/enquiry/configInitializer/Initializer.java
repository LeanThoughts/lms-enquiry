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

    private final ProjectTypeCoreSectorRepository projectTypeCoreSectorRepository;

    private final FinancingTypeRepository financingTypeRepository;
    private final LoanTypeRepository loanTypeRepository;

    private final AssistanceTypeRepository assistanceTypeRepository;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    private final IndustrySectorRepository industrySectorRepository;

    private final UnitOfMeasureRepository unitOfMeasureRepository;

    private final UserRoleRepository userRoleRepository;

    private final RejectionCategoryRepository rejectionCategoryRepository;

    private final StateRepository stateRepository;

    private final EnquiryPortalCommonConfigRepository enquiryPortalCommonConfigRepository;

    private final LoanApplicationRepository loanApplicationRepository;

    private final WorkflowApproverRepository workflowApproverRepository;

    private final TRAAccountTypeRepository traAccountTypeRepository;

    private final DocumentationTypeRepository documentationTypeRepository;
    private final DocumentationStatusRepository documentationStatusRepository;

    private final CustomerRejectionReasonRepository customerRejectionReasonRepository;

    private final PurposeOfLoanRepository purposeOfLoanRepository;
    private final FeeTypeRepository feeTypeRepository;
    private final SanctionTypeRepository sanctionTypeRepository;

    @Override
    public void run(String... strings) throws Exception {


        loanClassRepository.deleteAll();

        if (loanClassRepository.count() == 0) {
            LoanClass lc1 = new LoanClass("1", "PowerGen-Convl");
            LoanClass lc2 = new LoanClass("2", "PowerGen-Renew");
            LoanClass lc3 = new LoanClass("3", "Railways");
            LoanClass lc4 = new LoanClass("4", "Urban Instrastructure");
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
            LoanClass lc32 = new LoanClass("30", "Others");
            LoanClass lc27 = new LoanClass("41", "NFB-LOCforCAP");
            LoanClass lc28 = new LoanClass("42", "NFB-LOCforBG");
            LoanClass lc29 = new LoanClass("43", "NFB-LOCOthers");
            LoanClass lc30 = new LoanClass("51", "CL-Corp.Privat");
            LoanClass lc31 = new LoanClass("52", "CL-Corp.Govt");
            loanClassRepository.saveAll(Arrays.asList(lc1, lc2, lc3, lc4, lc5, lc6, lc7, lc8, lc9, lc10,
                    lc11, lc12, lc13, lc14, lc15, lc16, lc17, lc18, lc19, lc20, lc21, lc22,
                    lc23, lc24, lc25, lc26, lc27, lc28, lc29, lc30, lc31,lc32
            ));
            log.info("Added loan class sample data");
        }

        projectTypeRepository.deleteAll();

        if (projectTypeRepository.count() == 0) {
            ProjectType pt1 = new ProjectType("1","Thermal-Coal.");
            ProjectType pt2 = new ProjectType("2","Thermal-Lignite");
            ProjectType pt3 = new ProjectType("3","Thermal-Gas");
            ProjectType pt4 = new ProjectType("4","Renewable-Hydro");
            ProjectType pt5 = new ProjectType("5","Renewable-Solar");
            ProjectType pt6 = new ProjectType("6","Renewable-Wind");
            ProjectType pt7 = new ProjectType("7","Renewable-Biomass");
            ProjectType pt8 = new ProjectType("8","Renewable-Co-Gen.");
            ProjectType pt9 = new ProjectType("9","Railway Siding");
            ProjectType pt10 = new ProjectType("10","Railway Wagons/Coach");
            ProjectType pt11 = new ProjectType("11","Railway Terminals");
            ProjectType pt12 = new ProjectType("12","Smart City");
            ProjectType pt13 = new ProjectType("13","Roads-Toll");
            ProjectType pt14 = new ProjectType("14","Roads-HAM");
            ProjectType pt15 = new ProjectType("15","Roads & Bridges");
            ProjectType pt16 = new ProjectType("16","Ports");
            ProjectType pt17 = new ProjectType("17","Oil & Gas");
            ProjectType pt18 = new ProjectType("18","Gas Exploration");
            ProjectType pt19 = new ProjectType("19","Oil Rigs");
            ProjectType pt20 = new ProjectType("20","Water Treatment Plant");
            ProjectType pt21 = new ProjectType("21","Sewerage Treatment Plant");
            ProjectType pt22 = new ProjectType("22","Water Distribution");
            ProjectType pt23 = new ProjectType("23","Irrigation-DAM/ Channels");
            ProjectType pt24 = new ProjectType("24","Storm Water Drainage System");
            ProjectType pt25 = new ProjectType("25","Airport");
            ProjectType pt26 = new ProjectType("26","EM - Charging Infra");
            ProjectType pt27 = new ProjectType("27","Waste Handling");
            ProjectType pt28 = new ProjectType("28","Waste to Energy");
            ProjectType pt29 = new ProjectType("29","Coal Mining");
            ProjectType pt30 = new ProjectType("30","Coal Washery");
            ProjectType pt31 = new ProjectType("31","Large Country Pipelines");
            ProjectType pt32 = new ProjectType("32","Power Transmission");
            ProjectType pt33 = new ProjectType("33","Solar Park/Pooling Substation");
            ProjectType pt34 = new ProjectType("34","Power Distribution");
            ProjectType pt35 = new ProjectType("35","Solar Power Pumps/Electric Pumps");
            ProjectType pt36 = new ProjectType("36","Logistics Park/ Terminal");
            ProjectType pt37 = new ProjectType("37","Warehouse / Cold Chain Facility");
            ProjectType pt38 = new ProjectType("38","Shipyard");
            ProjectType pt39 = new ProjectType("39","City Gas Distribution");
            ProjectType pt40 = new ProjectType("40","Hotels & Resorts");
            ProjectType pt41 = new ProjectType("41","Real Estate");
            ProjectType pt42 = new ProjectType("42","SEZ-Industrial Park");
            ProjectType pt43 = new ProjectType("43","Telecom Towers");
            ProjectType pt44 = new ProjectType("44","Telecom Networks");
            ProjectType pt45 = new ProjectType("45","Telecommunication Services");
            ProjectType pt46 = new ProjectType("46","Inland Waterways");
            ProjectType pt47 = new ProjectType("47","Public Transport");
            ProjectType pt48 = new ProjectType("48","Hospitals");
            ProjectType pt49 = new ProjectType("49","Educational Institutes");
            ProjectType pt50 = new ProjectType("50","Sports Infrastructure");
            ProjectType pt51 = new ProjectType("51","Energy Efficiency");
            ProjectType pt52 = new ProjectType("52","Smart Metering");
            ProjectType pt53 = new ProjectType("53","Others");


            projectTypeRepository.saveAll(Arrays.asList(
                    pt1, pt2, pt3, pt4, pt5, pt6, pt7, pt8, pt9, pt10, pt11, pt12,
                    pt13, pt14, pt15, pt16, pt17, pt18, pt19, pt20,
                    pt21, pt22, pt23, pt24, pt25, pt26, pt27, pt28, pt29, pt30, pt31, pt32,
                    pt33, pt34, pt35, pt36, pt37, pt38, pt39, pt40, pt41, pt42, pt42,
                    pt43,
                    pt44,
                    pt45,
                    pt46,
                    pt47,
                    pt48,
                    pt49,
                    pt50,
                    pt51,
                    pt52,
                    pt53

            ));
            log.info("Added project type sample data");
        }

        projectTypeCoreSectorRepository.deleteAll();
        ProjectTypeCoreSector ptcs0 = new ProjectTypeCoreSector("0","");
        ProjectTypeCoreSector ptcs1 = new ProjectTypeCoreSector("1","Infrastructure");
        ProjectTypeCoreSector ptcs2 = new ProjectTypeCoreSector("2","NonInfrastructure");
        projectTypeCoreSectorRepository.saveAll(Arrays.asList(ptcs0,ptcs1,ptcs2));


        purposeOfLoanRepository.deleteAll();

        PurposeOfLoan p1 = new PurposeOfLoan("1","Term Loan");
        PurposeOfLoan p2 = new PurposeOfLoan("10","Term Loan/Sub Debt");
        PurposeOfLoan p3 = new PurposeOfLoan("1a","TL-Term Loan");
        PurposeOfLoan p4 = new PurposeOfLoan("1b","TL - Term Loan & Non-fund bas");
        PurposeOfLoan p5 = new PurposeOfLoan("1c","TL Cost Overrun Funding");
        PurposeOfLoan p6 = new PurposeOfLoan("1d","TL-Refinancing");
        PurposeOfLoan p7 = new PurposeOfLoan("1e","TL-Acquisition Financing");
        PurposeOfLoan p8 = new PurposeOfLoan("1f","TL-Refinancing/Top up");
        PurposeOfLoan p9 = new PurposeOfLoan("1g","TL-Take out financing");
        PurposeOfLoan p10 = new PurposeOfLoan("1h","TL-Top Up Loan");
        PurposeOfLoan p11 = new PurposeOfLoan("1i","TL-Sub Debt");
        PurposeOfLoan p12 = new PurposeOfLoan("1j","TL-Cash Flow Securitization");
        PurposeOfLoan p13 = new PurposeOfLoan("1k","TL-Others");
        PurposeOfLoan p14 = new PurposeOfLoan("2","Term Loan & Non-fund based");
        PurposeOfLoan p15 = new PurposeOfLoan("20","NFBF-CAPEX");
        PurposeOfLoan p16 = new PurposeOfLoan("21","NFBF-BG/PBG");
        PurposeOfLoan p17 = new PurposeOfLoan("22","NFBF-OTHERS");
        PurposeOfLoan p18 = new PurposeOfLoan("2a","LOC-Buyers Credit");
        PurposeOfLoan p19 = new PurposeOfLoan("2b","LOC-Foreign Letter of Credit");
        PurposeOfLoan p20 = new PurposeOfLoan("2c","LOC-Inland Letter of Credit");
        PurposeOfLoan p21 = new PurposeOfLoan("2d","LOC-Performance Bank Guarante");
        PurposeOfLoan p22 = new PurposeOfLoan("2e","LOC-Usance Letter of Credit");
        PurposeOfLoan p23 = new PurposeOfLoan("2f","LOC-Mobilization Advance Bank");
        PurposeOfLoan p24 = new PurposeOfLoan("2g","LOC-Advance Bank Guarantee");
        PurposeOfLoan p25 = new PurposeOfLoan("2h","LOC-Others");
        PurposeOfLoan p26 = new PurposeOfLoan("3","Term Loan (Cost Overrun Fundi");
        PurposeOfLoan p27 = new PurposeOfLoan("3a","CL-Medium Term Capex");
        PurposeOfLoan p28 = new PurposeOfLoan("3b","CL-Short Term Capex");
        PurposeOfLoan p29 = new PurposeOfLoan("3c","CL-Acquisition Financing");
        PurposeOfLoan p30 = new PurposeOfLoan("3d","CL-Bridge Loan");
        PurposeOfLoan p31 = new PurposeOfLoan("3e","CL-Revolving Facility");
        PurposeOfLoan p32 = new PurposeOfLoan("3f","CL-Working Cap/Cash Flow Mism");
        PurposeOfLoan p33 = new PurposeOfLoan("3g","CL-Working Capital Refinancin");
        PurposeOfLoan p34 = new PurposeOfLoan("3h","CL-Debenture-NCD");
        PurposeOfLoan p35 = new PurposeOfLoan("3i","CL-Cash Flow Mismatch");
        PurposeOfLoan p36 = new PurposeOfLoan("3j","CL-Others");
        PurposeOfLoan p37 = new PurposeOfLoan("4","Term Loan / Refinancing");
        PurposeOfLoan p38 = new PurposeOfLoan("5","Term Loan (Cost Overrun)");
        PurposeOfLoan p39 = new PurposeOfLoan("6","Term Loan/Acquisition Financi");
        PurposeOfLoan p40 = new PurposeOfLoan("7","Term Loan/Refinancing/Top up");
        PurposeOfLoan p41 = new PurposeOfLoan("8","Term Loan/Take out financing");
        PurposeOfLoan p42 = new PurposeOfLoan("9","Term Loan/Top Up Loan");

        purposeOfLoanRepository.saveAll(Arrays.asList(p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11,p12,p13,p14,p15,p16,p17,p18,p19,p20,p21,p22,p23,p24,p25,p26,p27,p28,p29,p30,p31,p32,p33,p34,p35,p36,p37,p38,p39,p40,p41,p42));
        log.info("Loan Purposes  ");


        financingTypeRepository.deleteAll();

        if (financingTypeRepository.count() == 0) {
            FinancingType ft1 = new FinancingType("1", "Sole Lending");
            FinancingType ft2 = new FinancingType("2", "Multiple Banking");
            FinancingType ft3 = new FinancingType("3", "Consortium Lending");
            financingTypeRepository.saveAll(Arrays.asList(ft1, ft2, ft3));
            log.info("Added financing type  ");
        }
        loanTypeRepository.deleteAll();

        if (loanTypeRepository.count() == 0) {
            LoanType lt1 = new LoanType("001", "Term Loan");
            LoanType lt2 = new LoanType("002", "Non Fund Loan");
            LoanType lt3 = new LoanType("003", "Corporate Loan");
            loanTypeRepository.saveAll(Arrays.asList(lt1, lt2, lt3));
            log.info("Added Loan types  ");
        }





        DocumentationType documentationType = documentationTypeRepository.findByCode("1");
        if (documentationType == null){
            documentationType = new DocumentationType();
            documentationType.setCode("1");
            documentationType.setValue("Facility Agreement");
            documentationTypeRepository.save(documentationType);
        }
        documentationType = documentationTypeRepository.findByCode("2");
        if (documentationType == null){
            documentationType = new DocumentationType();
            documentationType.setCode("2");
            documentationType.setValue("TRA Agreement");
            documentationTypeRepository.save(documentationType);
        }
        documentationType = documentationTypeRepository.findByCode("3");
        if (documentationType == null){
            documentationType = new DocumentationType();
            documentationType.setCode("3");
            documentationType.setValue("Facility Agent Agreement");
            documentationTypeRepository.save(documentationType);
        }
        documentationType = documentationTypeRepository.findByCode("4");
        if (documentationType == null){
            documentationType = new DocumentationType();
            documentationType.setCode("4");
            documentationType.setValue("Security Trustee Agreement");
            documentationTypeRepository.save(documentationType);
        }

        DocumentationStatus documentationStatus  = documentationStatusRepository.findByCode("1");
        if (documentationStatus == null){
            documentationStatus = new DocumentationStatus();
            documentationStatus.setCode("1");
            documentationStatus.setValue("Not started");
            documentationStatusRepository.save(documentationStatus);
        }
        documentationStatus = new DocumentationStatus();
        documentationStatus = documentationStatusRepository.findByCode("2");
        if (documentationStatus == null){
            documentationStatus = new DocumentationStatus();
            documentationStatus.setCode("2");
            documentationStatus.setValue("Pending");
            documentationStatusRepository.save(documentationStatus);
        }
        documentationStatus = new DocumentationStatus();
        documentationStatus = documentationStatusRepository.findByCode("3");
        if (documentationStatus == null){
            documentationStatus = new DocumentationStatus();
            documentationStatus.setCode("3");
            documentationStatus.setValue("Executed");
            documentationStatusRepository.save(documentationStatus);
        }
        documentationStatus = new DocumentationStatus();
        documentationStatus = documentationStatusRepository.findByCode("4");
        if (documentationStatus == null){
            documentationStatus = new DocumentationStatus();
            documentationStatus.setCode("4");
            documentationStatus.setValue("Approved");
            documentationStatusRepository.save(documentationStatus);
        }

        AssistanceType at1 = assistanceTypeRepository.getAssistanceTypeByCode("D");
        if (at1 == null) {
            at1 = new AssistanceType("D", "Debt");
            assistanceTypeRepository.save(at1);
        }
        AssistanceType at2 = assistanceTypeRepository.getAssistanceTypeByCode("E");
        if (at2 == null) {
            at2 = new AssistanceType("E", "Equity");
            assistanceTypeRepository.save(at2);
        }
        AssistanceType at3 = assistanceTypeRepository.getAssistanceTypeByCode("N");
        if (at3 == null) {
            at3 = new AssistanceType("N", "NCD");
            assistanceTypeRepository.save(at3);
        }
        AssistanceType at4 = assistanceTypeRepository.getAssistanceTypeByCode("C");
        if (at4 == null) {
            at4 = new AssistanceType("C", "CCD");
            assistanceTypeRepository.save(at4);
        }



        log.info("Added assistance type sample data");


        if (userRepository.count() == 0) {
            User user1 = new User("Admin", "- PFS", "admin@gmail.com", "ZLM023", true, "admin@gmail.com", "50000284", "02", false, false);
            userRepository.save(user1);

//            User user2 = new User("Loan", "Officer - 1", "lo1@gmail.com", "ZLM013", true, "lo1@gmail.com", "50000284", "01");
//            userRepository.save(user2);
//
//            User user3 = new User("Gopinath", "B R", "gopinath.br@gmail.com", "TR0100", true, "gopinath.br@gmail.com", "", "03");
//            userRepository.save(user3);
        }

        productRepository.deleteAll();
        if (productRepository.count() == 0) {
            Product pr1 = new Product("301", "Bridge Loan");
            Product pr2 = new Product("302", "Short Term Loan");
            Product pr3 = new Product("303", "Term Loan");
            Product pr4 = new Product("304", "Debentures");
            Product pr5 = new Product("305", "Non Fund Based Loan");
            Product pr6 = new Product("30F", "Facilities");
            Product pr7 = new Product("310", "Facilities Drawdown-NFB Loan");
            Product pr8 = new Product("311", "Facilities Drawdown-Term Loan");
            Product pr9 = new Product("991", "Short Term Loan for Vehicle");
            productRepository.saveAll(Arrays.asList(pr1, pr2, pr3, pr4, pr5, pr6, pr7, pr8, pr9));
            log.info("Added products sample data");
        }

        sanctionTypeRepository.deleteAll();
        if(sanctionTypeRepository.count() == 0){
            SanctionType st1 = new SanctionType(" ","Original Sanction");
            SanctionType st2 = new SanctionType("1","Change in Saction Amount");
            SanctionType st3 = new SanctionType("2","Timeline Extension");
            SanctionType st4 = new SanctionType("3","Waiver of Condition");
            SanctionType st5 = new SanctionType("4","Language Modification");
            SanctionType st6 = new SanctionType("5","Change in Intt Rate");
            SanctionType st7 = new SanctionType("6","Reimbursement");
            SanctionType st8 = new SanctionType("7","Waiver of billed amt");
            sanctionTypeRepository.saveAll(Arrays.asList(st1, st2, st3, st4, st5, st6, st7));
            log.info("Added Sanction Types");
        }

        feeTypeRepository.deleteAll();
        if(feeTypeRepository.count() == 0){
            FeeType ft1 = new FeeType("9501","Application Fees");
            FeeType ft2 = new FeeType("9502","Processing Fees");
            FeeType ft3 = new FeeType("9503","Upfront Fees");
            FeeType ft4 = new FeeType("9504","Lead FI Fees");
            FeeType ft5 = new FeeType("9505","Lender Agent Fee");
            FeeType ft6 = new FeeType("9506","Management Fee");
            FeeType ft7 = new FeeType("9507","Monitoring Fee");
            FeeType ft8 = new FeeType("9508","Additional Monitoring Fee");
            FeeType ft9 = new FeeType("9509","Security Trustee Fee");
            FeeType ft10 = new FeeType("9510","Letter of Comfort Fee");
            FeeType ft11 = new FeeType("9511","Foreclosure Fees");
            FeeType ft12 = new FeeType("9512","RE-validation Fees");
            FeeType ft13 = new FeeType("9516","Underwriting Fees");
            FeeType ft14 = new FeeType("9517","Appraisal Note Sharing Fees");
            FeeType ft15 = new FeeType("9518","Commitment Fees");
            FeeType ft16 = new FeeType("9519","Syndication Fees");
            FeeType ft17 = new FeeType("9520","Other Fees");
            FeeType ft18 = new FeeType("9521","Facility Agent Fee");
            FeeType ft19 = new FeeType("9522","NoGo Fee");
            FeeType ft20 = new FeeType("9524","Project Advisory Fees");
            FeeType ft21 = new FeeType("9527","Loan Documentation Fees");
            FeeType ft22 = new FeeType("9528","Legal Counsel Fees");
            FeeType ft23 = new FeeType("9529","Lender's Engineers Fees");
            FeeType ft24 = new FeeType("9530","Lender's Financial Adv. Fees");
            FeeType ft25 = new FeeType("9531","Lenders Insurance Adv. Fees");
            FeeType ft26 = new FeeType("9532","Other Out of Pocket Exp Fees");
            FeeType ft27 = new FeeType("9533","Other Re-imbursements Fees");

            feeTypeRepository.saveAll(Arrays.asList(ft1,ft2,ft3,ft4,ft5,ft6,ft7,ft8,ft9,ft10,ft11,ft12,ft13,ft14,ft15,ft16,ft17,ft18,ft19,ft20,
                    ft21,ft22,ft23,ft24,ft25,ft26,ft27));
            log.info("Added Fee Types");
        }

        industrySectorRepository.deleteAll();

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
            State s1 = new State("01", "Andhra Pradesh");
            State s2 = new State("02", "Arunachal Pradesh");
            State s3 = new State("03", "Assam");
            State s4 = new State("04", "Bihar");
            State s5 = new State("05", "Goa");
            State s6 = new State("06", "Gujarat");
            State s7 = new State("07", "Haryana");
            State s8 = new State("08", "Himachal Pradesh");
            State s9 = new State("09", "Jammu and Kashmir");
            State s10 = new State("10", "Karnataka");
            State s11 = new State("11", "Kerala");
            State s12 = new State("12", "Madhya Pradesh");
            State s13 = new State("13", "Maharashtra");
            State s14 = new State("14", "Manipur");
            State s15 = new State("15", "Meghalaya");
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
            State s28 = new State("28", "Dadra and Nagar Hav.");
            State s29 = new State("29", "Daman and Diu");
            State s30 = new State("30", "Delhi");
            State s31 = new State("31", "Lakshadweep");
            State s32 = new State("32", "Pondicherry");
            State s33 = new State("33", "Chhattisgarh");
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

        if (customerRejectionReasonRepository.findAll().size() == 0) {
            CustomerRejectionReason c1 = new CustomerRejectionReason("01","Competitors' conditions better");
            CustomerRejectionReason c2 = new CustomerRejectionReason("02","Withdrawal without reason");
            CustomerRejectionReason c3 = new CustomerRejectionReason("03","Too little capital");
            CustomerRejectionReason c4 = new CustomerRejectionReason("04","Unacceptable rating");
            CustomerRejectionReason c5 = new CustomerRejectionReason("05","Insufficient financing");
            CustomerRejectionReason c6 = new CustomerRejectionReason("06","Too many uncovered parts");
            CustomerRejectionReason c7 = new CustomerRejectionReason("07","High collateral requirements");
            CustomerRejectionReason c8 = new CustomerRejectionReason("08","Too high construction costs");
            CustomerRejectionReason c9 = new CustomerRejectionReason("09","Unfavorable analysis of demand, location");
            CustomerRejectionReason c10 = new CustomerRejectionReason("10","Better conditions for competition");
            CustomerRejectionReason c11 = new CustomerRejectionReason("11","Incorrect entry");
            CustomerRejectionReason c12 = new CustomerRejectionReason("12","Application: Data incomplete");
            CustomerRejectionReason c13 = new CustomerRejectionReason("13","Other Reasons");
            CustomerRejectionReason c14 = new CustomerRejectionReason("14","Expiry of Validity of Sanction letter");
            CustomerRejectionReason c15 = new CustomerRejectionReason("15","Non-payment of processing fees");
            CustomerRejectionReason c16 = new CustomerRejectionReason("16","SCOD &/or Availability period Lapsed");

            customerRejectionReasonRepository.saveAll(Arrays.asList(c1,
                    c2,
                    c3,
                    c4,
                    c5,
                    c6,
                    c7,
                    c8,
                    c9,
                    c10,
                    c11,
                    c12,
                    c13,
                    c14,
                    c15,
                    c16
            ));
        }




        log.info("Added Customer Rejection Reasons");

//        WorkflowApprover workflowApprover = workflowApproverRepository.findByProcessName("Monitoring");
//        if (workflowApprover == null) {
//            workflowApprover.setId(1);
//            workflowApprover.setApproverName("Project Department Head");
//            workflowApprover.setApproverEmail("naveenkverma@ptcfinancial.com");
//            workflowApprover.setProcessName("Monitoring");
//            workflowApproverRepository.save(workflowApprover);
//        }
//        workflowApprover = workflowApproverRepository.findByProcessName("Appraisal");
//        if (workflowApprover == null) {
//            workflowApprover.setId(2);
//            workflowApprover.setApproverName("Appraisal Department Head");
//            workflowApprover.setApproverEmail("naveenkverma@ptcfinancial.com");
//            workflowApprover.setProcessName("Appraisal");
//            workflowApproverRepository.save(workflowApprover);
//        }

        TRAAccountType traAccountType = new TRAAccountType();
        traAccountType = traAccountTypeRepository.findByCode("01");
        if (traAccountType != null) {
            traAccountType.setValue("Revenue Account");
            traAccountTypeRepository.save(traAccountType);
        } else {
            traAccountType = new TRAAccountType("01", "Revenue Account");
            traAccountTypeRepository.save(traAccountType);
        }

        traAccountType = traAccountTypeRepository.findByCode("02");
        if (traAccountType != null) {
            traAccountType.setValue("Surplus Account");
            traAccountTypeRepository.save(traAccountType);
        } else {
            traAccountType = new TRAAccountType("02", "Surplus Account");
            traAccountTypeRepository.save(traAccountType);
        }

        traAccountType = traAccountTypeRepository.findByCode("03");
        if (traAccountType != null) {
            traAccountType.setValue("Distribution Account");
            traAccountTypeRepository.save(traAccountType);
        } else {
            traAccountType = new TRAAccountType("03", "Distribution Account");
            traAccountTypeRepository.save(traAccountType);
        }
        traAccountType = traAccountTypeRepository.findByCode("04");
        if (traAccountType != null) {
            traAccountType.setValue("Loss Proceeds Account");
            traAccountTypeRepository.save(traAccountType);
        } else {
            traAccountType = new TRAAccountType("04", "Loss Proceeds Account");
            traAccountTypeRepository.save(traAccountType);
        }
        traAccountType = traAccountTypeRepository.findByCode("05");
        if (traAccountType != null) {
            traAccountType.setValue("O&M Account");
            traAccountTypeRepository.save(traAccountType);
        } else {
            traAccountType = new TRAAccountType("05", "O&M Account");
            traAccountTypeRepository.save(traAccountType);
        }
        traAccountType = traAccountTypeRepository.findByCode("06");
        if (traAccountType != null) {
            traAccountType.setValue("Statutory Dues Account");
            traAccountTypeRepository.save(traAccountType);
        } else {
            traAccountType = new TRAAccountType("06", "Statutory Dues Account");
            traAccountTypeRepository.save(traAccountType);
        }
        traAccountType = traAccountTypeRepository.findByCode("07");
        if (traAccountType != null) {
            traAccountType.setValue("DSRA Account");
            traAccountTypeRepository.save(traAccountType);
        } else {
            traAccountType = new TRAAccountType("07", "DSRA Account");
            traAccountTypeRepository.save(traAccountType);
        }

        traAccountType = traAccountTypeRepository.findByCode("08");
        if (traAccountType != null) {
            traAccountType.setValue("Debt Service Payment Account");
            traAccountTypeRepository.save(traAccountType);
        } else {
            traAccountType = new TRAAccountType("08", "Debt Service Payment Account");
            traAccountTypeRepository.save(traAccountType);
        }

        traAccountType = traAccountTypeRepository.findByCode("09");
        if (traAccountType != null) {
            traAccountType.setValue("Construction Account");
            traAccountTypeRepository.save(traAccountType);
        } else {
            traAccountType = new TRAAccountType("09", "Construction Account");
            traAccountTypeRepository.save(traAccountType);
        }

        traAccountType = traAccountTypeRepository.findByCode("10");
        if (traAccountType != null) {
            traAccountType.setValue("Disbursement Account");
            traAccountTypeRepository.save(traAccountType);
        } else {
            traAccountType = new TRAAccountType("10", "Disbursement Account");
            traAccountTypeRepository.save(traAccountType);
        }

        traAccountType = traAccountTypeRepository.findByCode("11");
        if (traAccountType != null) {
            traAccountType.setValue("Disputes Reserve Account");
            traAccountTypeRepository.save(traAccountType);
        } else {
            traAccountType = new TRAAccountType("11", "Disputes Reserve Account");
            traAccountTypeRepository.save(traAccountType);
        }

        traAccountType = traAccountTypeRepository.findByCode("12");
        if (traAccountType != null) {
            traAccountType.setValue("Revenue Account");
            traAccountTypeRepository.save(traAccountType);
        } else {
            traAccountType = new TRAAccountType("12", "Revenue Account");
            traAccountTypeRepository.save(traAccountType);
        }

        traAccountType = traAccountTypeRepository.findByCode("13");
        if (traAccountType != null) {
            traAccountType.setValue("Compensation and Liquidated Damagest");
            traAccountTypeRepository.save(traAccountType);
        } else {
            traAccountType = new TRAAccountType("13", "Compensation and Liquidated Damages");
            traAccountTypeRepository.save(traAccountType);
        }
        traAccountType = traAccountTypeRepository.findByCode("14");
        if (traAccountType != null) {
            traAccountType.setValue("Enforcement Proceeds Account ");
            traAccountTypeRepository.save(traAccountType);
        } else {
            traAccountType = new TRAAccountType("14", "Enforcement Proceeds Account ");
            traAccountTypeRepository.save(traAccountType);
        }

        traAccountType = traAccountTypeRepository.findByCode("16");
        if (traAccountType != null) {
            traAccountType.setValue("O&M Reserve Account");
            traAccountTypeRepository.save(traAccountType);
        } else {
            traAccountType = new TRAAccountType("16", "O&M Reserve Account");
            traAccountTypeRepository.save(traAccountType);
        }


    }


}
