package pfs.lms.enquiry.action.teaser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import pfs.lms.enquiry.repository.AssistanceTypeRepository;
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

    private TeaserContent teaserContent;

    @Override
    public SXSSFWorkbook generateTeaserExcel(HttpServletResponse response,TeaserResource teaserResource) throws IOException, ParseException {
        sxssfWorkbook = new SXSSFWorkbook();
        TeaserExcel teaserExcel = new TeaserExcel();


        this.teaserContent = getTeaserContent(teaserResource);

        //Purpose - Assistance Type



        try {
            sxssfWorkbook   = teaserExcel.writeHeaderLineSXSS(sxssfWorkbook,sxssfSheet);
            sxssfWorkbook   = teaserExcel.writeContentLinesSXSS( sxssfWorkbook,sxssfWorkbook.getSheet("Teaser"),teaserContent);
            sxssfSheet      = teaserExcel.writeContentShareHolding(teaserResource.getShareHolders(),sxssfWorkbook.getSheet("Teaser"),sxssfWorkbook);
            sxssfSheet      = teaserExcel.writeProjectCost(teaserContent,sxssfWorkbook.getSheet("Teaser"),sxssfWorkbook);
            sxssfSheet      = teaserExcel.writePage2Items(teaserResource.getDealGuaranteeTimeline(),sxssfWorkbook.getSheet("Teaser"),sxssfWorkbook);
            sxssfSheet      = teaserExcel.writePage3Items(teaserResource.getPromoterBorrowerFinancials(),sxssfWorkbook.getSheet("Teaser"),sxssfWorkbook);


        } catch ( Exception exception) {
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
        if ( teaserResource.getProjectDetail().getTenorYear() != null )
            teaserContent.setTenure(teaserResource.getProjectDetail().getTenorYear() + " Years");

        if ( teaserResource.getProjectDetail().getTenorMonths() != null )
            teaserContent.setTenure(teaserContent.getTenure() + " " + teaserResource.getProjectDetail().getTenorMonths() + " Months");

        if ( teaserResource.getProjectDetail().getMoratoriumPeriod() != null )
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

        teaserContent.setProjectCost(formatAmount( teaserResource.getProjectCost().getProjectCost()));
        teaserContent.setDebt(formatAmount( teaserResource.getProjectCost().getDebt()));
        teaserContent.setEquity(formatAmount( teaserResource.getProjectCost().getEquity()));
        teaserContent.setDebtEquityRatio(teaserResource.getProjectCost().getDebtEquityRatio().toString());




        return  teaserContent;
    }

    private String formatAmount(Double amount){

//        return NumberFormat.getCurrencyInstance(new Locale("en", "IN"))
//                .format(amount);
        NumberFormat formatter = new DecimalFormat("#0.00");
        return  formatter.format(amount);

    }

    private String getPeriodUnit(String periodUnit){
        String unitName;

        switch (periodUnit){
            case "1":
                unitName = "Days"; break;
            case "2":
                unitName = "Weeks";break;
            case "3":
                unitName = "Months";break;
            case "4":
                unitName = "Years";break;
            default:
                unitName = "";
        }

        return  unitName;

    }
}
