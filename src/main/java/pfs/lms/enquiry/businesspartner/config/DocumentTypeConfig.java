package pfs.lms.enquiry.businesspartner.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.businesspartner.domain.DocumentType;

import pfs.lms.enquiry.businesspartner.repository.DocumentTypeRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class DocumentTypeConfig implements CommandLineRunner {

    private final DocumentTypeRepository documentTypeRepository;
 
    @Override
    public void run(String... strings) throws Exception {
        DocumentType documentType  = new DocumentType(null, "", "","");
        // documentType = documentTypeRepository.findByCode("");if (documentType == null){ documentType = new DocumentType(null,"ZPFSBP0001","Identity Proof","BUS1006"); documentTypeRepository.save(documentType); }

        documentType = documentTypeRepository.findByCode("ZPFSBP0001");if (documentType == null){ documentType = new DocumentType(null,"ZPFSBP0001","Identity Proof","BUS1006"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSBP0002");if (documentType == null){ documentType = new DocumentType(null,"ZPFSBP0002","PAN Card","BUS1006"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSBP0003");if (documentType == null){ documentType = new DocumentType(null,"ZPFSBP0003","Passport","BUS1006"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSBP0004");if (documentType == null){ documentType = new DocumentType(null,"ZPFSBP0004","Address Proof","BUS1006"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSBP0005");if (documentType == null){ documentType = new DocumentType(null,"ZPFSBP0005","MoA and Articles of Association (AoA)","BUS1006"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSBP0006");if (documentType == null){ documentType = new DocumentType(null,"ZPFSBP0006","Certification of Incorporation","BUS1006"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSBP0007");if (documentType == null){ documentType = new DocumentType(null,"ZPFSBP0007","Certificate of Commencement of Business","BUS1006"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSBP0008");if (documentType == null){ documentType = new DocumentType(null,"ZPFSBP0008","PAN Card of Company","BUS1006"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSBP0009");if (documentType == null){ documentType = new DocumentType(null,"ZPFSBP0009","Shareholding Pattern of the Company","BUS1006"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSBP0010");if (documentType == null){ documentType = new DocumentType(null,"ZPFSBP0010","Credit Rating Letter","BUS1006"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSBP0011");if (documentType == null){ documentType = new DocumentType(null,"ZPFSBP0011","Financial Statements","BUS1006"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSBP0012");if (documentType == null){ documentType = new DocumentType(null,"ZPFSBP0012","Authority Letter by CMD/MD/CEO/WTD/Direc","BUS1006"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSBP0013");if (documentType == null){ documentType = new DocumentType(null,"ZPFSBP0013","Copy of Telephone Bill","BUS1006"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSBP0014");if (documentType == null){ documentType = new DocumentType(null,"ZPFSBP0014","Copy of Water Bill","BUS1006"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSBP0015");if (documentType == null){ documentType = new DocumentType(null,"ZPFSBP0015","Electricity Bill in the name of Company","BUS1006"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSBP0016");if (documentType == null){ documentType = new DocumentType(null,"ZPFSBP0016","Institution on Letter head of Company","BUS1006"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSBP0017");if (documentType == null){ documentType = new DocumentType(null,"ZPFSBP0017","Statement Declaring List of Defaults","BUS1006"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSBP0018");if (documentType == null){ documentType = new DocumentType(null,"ZPFSBP0018","Detail of Dispute/Demand","BUS1006"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSBP0019");if (documentType == null){ documentType = new DocumentType(null,"ZPFSBP0019","Resolution of BoD","BUS1006"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSBP0020");if (documentType == null){ documentType = new DocumentType(null,"ZPFSBP0020","KYC detail","BUS1006"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM01");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM01","PFS Project Notes","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM10");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM10","Loan Agreement","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM11");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM11","Lenders Agent Agreement","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM12");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM12","Security Trustee Agreement","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM13");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM13","Trust and Retention Agreement","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM14");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM14","Inter Creditor Agreement","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM15");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM15","Confirmation to ICA","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM16");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM16","Deed of Hypothecation","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM17");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM17","Deed of Pledge","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM18");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM18","Power of Attorney","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM19");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM19","Indenture of Mortgage","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM2");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM2","Project Contract Documents","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM20");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM20","Deed of Personal Guarantee","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM21");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM21","Deed of Corporate Guarantee","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM22");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM22","Affidavit","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM23");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM23","Borrowers Undertaking","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM24");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM24","Promoters Undertaking","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM25");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM25","Other Agreements","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM26");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM26","CA / CS/ Auditors Certificates","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM27");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM27","Amendment agreements","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM28");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM28","Novation / Accession Agreements","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM29");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM29","Demand Letters","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM3");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM3","Project Clearances/Approvals","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM30");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM30","Fee Invoices","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM31");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM31","Debit Notes","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM32");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM32","No Objection Certificate","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM33");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM33","Term Sheet","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM34");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM34","Sanction Letter","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM35");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM35","Amendment to Sanction Letters","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM36");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM36","Disbursement Intimations","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM37");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM37","Other Communication (if any)","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM38");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM38","Loan Request Application","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM39");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM39","Request for Disbursement","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM4");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM4","Promoter and Borrower Financials","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM40");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM40","Request for Amendment","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM41");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM41","Request for Prepayment","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM42");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM42","Any Other requests","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM43");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM43","Gallery","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM44");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM44","Operating_Parameter","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM45");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM45","NOC_Prepayment","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM46");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM46","NOC_Intt_Reduction","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM47");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM47","NOC_Other_Request","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM48");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM48","Demand Letters (Old)","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM49");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM49","Fee Invoices (Old)","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM5");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM5","DPR","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM50");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM50","Monitoring:LIE Report","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM51");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM51","Monitoring:LFA Report","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM52");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM52","Monitoring:TRA Statement","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM53");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM53","MonitoriTerms and Condition Modification","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM54");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM54","Monitoring:Operating Parameters","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM55");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM55","Monitoring:BorrowerFinancials-Annual Rpt","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM56");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM56","Monitoring:BorrowerFinancialsc-Rating","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM57");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM57","Monitoring:PromoterFinancials-Annual Rpt","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM58");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM58","Monitoring:PromoterFinancialsc-Rating","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM59");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM59","Site Visit","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM6");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM6","Credit Opinion/CIBIL/Analyst Reports","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM60");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM60","Lead Banker Document","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM61");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM61","Approved note sheet","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM62");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM62","Board approved minutes","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM63");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM63","Appraisal Note-Internal","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM64");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM64","Approval for placing agenda to Board","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM65");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM65","Due Diligence Report","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM66");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM66","Signed Board Agenda note","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM67");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM67","Board Agenda","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM68");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM68","Board Minutes","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM69");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM69","Amendment letters","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM7");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM7","Financial Model","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM70");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM70","Security Compliance Certificate","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM71");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM71","1st Disbursement approval","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM72");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM72","LIE Work Order","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM73");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM73","LFA Work Order","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM74");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM74","Authority Letters","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM75");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM75","Facility Agreement","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM76");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM76","Subsequent Amendments","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM77");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM77","Subsequent Amendments Addendum","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM78");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM78","Pledge Deeds","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM79");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM79","Guarantee Deeds","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM8");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM8","Queries/Emails/Imp Communication","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM80");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM80","Deed of Hypothecation-DOH","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM81");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM81","Authority Letters","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM82");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM82","Other Security Documents","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM83");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM83","Valuation Report","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM84");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM84","CA Certificate-Fin Covenants","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM85");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM85","CA Certificate","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM86");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM86","End Use Certificate","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM87");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM87","Insurance Document","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM88");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM88","Site Images","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM89");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM89","Minutes of Meeting (MOM)","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM9");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM9","Miscellaneous Documents","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM90");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM90","Mail from Site Visit","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM91");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM91","ICC Approved Minutes","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM92");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM92","RMC Approved Minutes","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM93");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM93","Mail - General","BUS2049"); documentTypeRepository.save(documentType); }
        documentType = documentTypeRepository.findByCode("ZPFSLM94");if (documentType == null){ documentType = new DocumentType(null,"ZPFSLM94","Prelim Risk Assessment Report","BUS2049"); documentTypeRepository.save(documentType); }



        return;
    }
}