package pfs.lms.enquiry.action.teaser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

import pfs.lms.enquiry.action.EnquiryActionRepository;
import pfs.lms.enquiry.action.otherdetail.OtherDetailRepository;
import pfs.lms.enquiry.action.projectproposal.ProjectProposal;
import pfs.lms.enquiry.action.projectproposal.ProjectProposalRepository;
import pfs.lms.enquiry.action.projectproposal.collateraldetail.CollateralDetail;
import pfs.lms.enquiry.action.projectproposal.collateraldetail.CollateralDetailRepository;
import pfs.lms.enquiry.action.projectproposal.creditrating.CreditRatingRepository;
import pfs.lms.enquiry.action.projectproposal.dealguaranteetimeline.DealGuaranteeTimeline;
import pfs.lms.enquiry.action.projectproposal.dealguaranteetimeline.DealGuaranteeTimelineRepository;
import pfs.lms.enquiry.action.projectproposal.financials.PromoterBorrowerFinancial;
import pfs.lms.enquiry.action.projectproposal.financials.PromoterBorrowerFinancialRepository;
import pfs.lms.enquiry.action.projectproposal.projectcost.ProjectCost;
import pfs.lms.enquiry.action.projectproposal.projectcost.ProjectCostRepository;
import pfs.lms.enquiry.action.projectproposal.projectdetail.ProjectDetail;
import pfs.lms.enquiry.action.projectproposal.projectdetail.ProjectDetailRepository;
import pfs.lms.enquiry.action.projectproposal.projectproposalotherdetail.ProjectProposalOtherDetail;
import pfs.lms.enquiry.action.projectproposal.projectproposalotherdetail.ProjectProposalOtherDetailRepository;
import pfs.lms.enquiry.action.projectproposal.shareholder.ShareHolder;
import pfs.lms.enquiry.action.projectproposal.shareholder.ShareHolderRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.AssistanceTypeRepository;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.repository.PartnerRepository;
import pfs.lms.enquiry.repository.ProjectTypeRepository;


@Slf4j
@Service
@RequiredArgsConstructor
public class TeaserService implements ITeaserService {
    private TeaserResource teaserResource;
    private SXSSFWorkbook sxssfWorkbook;
    private SXSSFSheet sxssfSheet;

    private final ProjectTypeRepository projectTypeRepository;
    private final AssistanceTypeRepository assistanceTypeRepository;
    private final EnquiryActionRepository enquiryActionRepository;
    private final ProjectProposalRepository projectProposalRepository;
    private final ShareHolderRepository shareHolderRepository;
    private final CollateralDetailRepository collateralDetailRepository;
    private final ProjectDetailRepository projectDetailRepository;
    private final ProjectCostRepository projectCostRepository;
    private final DealGuaranteeTimelineRepository dealGuaranteeTimelineRepository;
    private final PromoterBorrowerFinancialRepository promoterBorrowerFinancialRepository;
    private final ProjectProposalOtherDetailRepository projectProposalOtherDetailRepository;
    private final OtherDetailRepository otherDetailRepository;
    private final CreditRatingRepository creditRatingRepository;
    private final LoanApplicationRepository loanApplicationRepository;
    private final PartnerRepository partnerRepository;

    private TeaserContent teaserContent;

    @Override
    public SXSSFWorkbook generateTeaserExcelForProposal(HttpServletResponse response, UUID projectProposalId) throws IOException, ParseException {

        LoanApplication loanApplication = new LoanApplication();

        ProjectProposal projectProposal = projectProposalRepository.getOne(projectProposalId);
        loanApplication = projectProposal.getEnquiryAction().getLoanApplication();


        TeaserResource teaserResource = new TeaserResource();
        teaserResource.setLoanApplication(loanApplication);
        teaserResource.setPartner(partnerRepository.findByPartyNumber(Integer.parseInt(loanApplication.getbusPartnerNumber())));

        List<CollateralDetail> collateralDetails = collateralDetailRepository.findByProjectProposalId(projectProposal.getId());
        List<ShareHolder> shareHolders = shareHolderRepository.findByProjectProposalId(projectProposal.getId());
        ProjectCost projectCost = projectCostRepository.findByProjectProposalId(projectProposal.getId());
        ProjectDetail projectDetail = projectDetailRepository.findByProjectProposalId(projectProposal.getId());
        ProjectProposalOtherDetail projectProposalOtherDetail = projectProposalOtherDetailRepository.findByProjectProposalId(projectProposal.getId());
        List<PromoterBorrowerFinancial> promoterBorrowerFinancials = promoterBorrowerFinancialRepository.findByProjectProposalId(projectProposal.getId());
        DealGuaranteeTimeline dealGuaranteeTimeline = dealGuaranteeTimelineRepository.findByProjectProposalId(projectProposal.getId());

        teaserResource.setProjectProposal(projectProposal);
        teaserResource.setProjectDetail(projectDetail);
        teaserResource.setCollateralDetails(collateralDetails);
        teaserResource.setShareHolders(shareHolders);
        teaserResource.setProjectProposalOtherDetail(projectProposalOtherDetail);
        teaserResource.setProjectCost(projectCost);
        teaserResource.setPromoterBorrowerFinancials(promoterBorrowerFinancials);
        teaserResource.setDealGuaranteeTimeline(dealGuaranteeTimeline);


        SXSSFWorkbook sxssfWorkbook = this.generateTeaserExcel(response, teaserResource);

        return sxssfWorkbook;

    }

    @Override
    public SXSSFWorkbook generateTeaserExcel(HttpServletResponse response, TeaserResource teaserResource) throws IOException, ParseException {
        sxssfWorkbook = new SXSSFWorkbook();
        TeaserExcel teaserExcel = new TeaserExcel();
        sxssfWorkbook = teaserExcel.writeHeaderLineSXSS(sxssfWorkbook, sxssfSheet);

        if (teaserResource.getProjectDetail() != null) {
            this.teaserContent = getTeaserContent(teaserResource);
        } else {
            return sxssfWorkbook;
        }

        try {
            sxssfWorkbook = teaserExcel.writeContentLinesSXSS(sxssfWorkbook, sxssfWorkbook.getSheet("Teaser"), teaserContent);
            if (teaserResource.getShareHolders() != null)
                sxssfSheet = teaserExcel.writeContentShareHolding(teaserResource.getShareHolders(), sxssfWorkbook.getSheet("Teaser"), sxssfWorkbook);
            if (teaserContent != null)
                sxssfSheet = teaserExcel.writeProjectCost(teaserContent, sxssfWorkbook.getSheet("Teaser"), sxssfWorkbook);
            if (teaserResource.getDealGuaranteeTimeline() != null)
                sxssfSheet = teaserExcel.writePage2Items(teaserResource.getDealGuaranteeTimeline(), sxssfWorkbook.getSheet("Teaser"), sxssfWorkbook);
            if (teaserResource.getPromoterBorrowerFinancials() != null)
                sxssfSheet = teaserExcel.writePage3Items(teaserResource.getPromoterBorrowerFinancials(), sxssfWorkbook.getSheet("Teaser"), sxssfWorkbook);


        } catch (Exception exception) {
            log.error("EXCEPTION :  " + exception.toString());
        }


        ServletOutputStream outputStream = response.getOutputStream();
        sxssfWorkbook.write(outputStream);
        sxssfWorkbook.close();

        outputStream.close();
        return sxssfWorkbook;
    }

    private TeaserContent getTeaserContent(TeaserResource teaserResource) {
        teaserContent = new TeaserContent();
        teaserContent.setProjectName(teaserResource.getProjectDetail().getProjectName());
        teaserContent.setPromoterName(teaserResource.getProjectDetail().getPromoterName());
        teaserContent.setProjectType(projectTypeRepository.findByCode(teaserResource.getLoanApplication().getProjectType()).getValue());
        //teaserExcel.setAssistanceType(assistanceTypeRepository.getAssistanceTypeByCode(teaserResource.getProjectDetail().getAssistanceType()).getValue());
        teaserContent.setGroupRating("");
        teaserContent.setProjectRating("");
        teaserContent.setTypeAndPurposeOfLoan(assistanceTypeRepository.getAssistanceTypeByCode(teaserResource.getProjectDetail().getAssistanceType()).getValue());
        teaserContent.setTotalFundsRequired(formatAmount(teaserResource.getProjectCost().getProjectCost()));
        teaserContent.setTotalDebtRequired(formatAmount(teaserResource.getProjectCost().getDebt()));
        teaserContent.setProposedShareOfPFS(formatAmount(teaserResource.getProjectCost().getPfsDebtAmount()));
        teaserContent.setTotalCorporateStructuredLoanRequirement("");
        teaserContent.setEndUseOFundsFromPFS(teaserResource.projectDetail.getEndUseOfFunds());
        //teaserContent.setRateOfInterest(teaserResource.getProjectDetail());
        if (teaserResource.getProjectDetail().getTenorYear() != null)
            teaserContent.setTenure(teaserResource.getProjectDetail().getTenorYear() + " Years");

        if (teaserResource.getProjectDetail().getTenorMonths() != null)
            teaserContent.setTenure(teaserContent.getTenure() + " " + teaserResource.getProjectDetail().getTenorMonths() + " Months");

        if (teaserResource.getProjectDetail().getMoratoriumPeriod() != null)
            teaserContent.setMoratoriumPeriod(teaserResource.getProjectDetail().getMoratoriumPeriod().toString());
        if (teaserResource.getProjectDetail().getMoratoriumPeriodUnit() != null)
            teaserContent.setMoratoriumPeriod(teaserContent.getMoratoriumPeriod() + getPeriodUnit(teaserResource.getProjectDetail().getMoratoriumPeriodUnit()));

        teaserContent.setFee(formatAmount(teaserResource.getProjectDetail().getFees()));
        teaserContent.setSourceAndCashFlow(teaserResource.getProjectProposalOtherDetail().getSourceAndCashFlow());
        teaserContent.setQuantificationOfSecurity("");
        teaserContent.setConsolidatedGroupLeverage(teaserResource.getProjectProposalOtherDetail().getConsolidatedGroupLeverage());
        teaserContent.setTotalDebitTNW(formatAmount(teaserResource.getProjectProposalOtherDetail().getTotalDebtTNW()));
        teaserContent.setTotalTOLTNW(formatAmount(teaserResource.getProjectProposalOtherDetail().getTolTNW()));
        teaserContent.setDelaysInDebtServicing(teaserResource.getProjectProposalOtherDetail().getDelayInDebtServicing());

        teaserContent.setProjectCost(formatAmount(teaserResource.getProjectCost().getProjectCost()));
        teaserContent.setDebt(formatAmount(teaserResource.getProjectCost().getDebt()));
        teaserContent.setEquity(formatAmount(teaserResource.getProjectCost().getEquity()));
        teaserContent.setDebtEquityRatio(teaserResource.getProjectCost().getDebtEquityRatio().toString());


        return teaserContent;
    }

    private String formatAmount(Double amount) {

//        return NumberFormat.getCurrencyInstance(new Locale("en", "IN"))
//                .format(amount);
        NumberFormat formatter = new DecimalFormat("#0.00");
        return formatter.format(amount);

    }

    private String getPeriodUnit(String periodUnit) {
        String unitName;

        switch (periodUnit) {
            case "1":
                unitName = "Days";
                break;
            case "2":
                unitName = "Weeks";
                break;
            case "3":
                unitName = "Months";
                break;
            case "4":
                unitName = "Years";
                break;
            default:
                unitName = "";
        }

        return unitName;

    }
}
