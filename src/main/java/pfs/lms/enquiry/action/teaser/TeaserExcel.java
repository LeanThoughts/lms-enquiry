package pfs.lms.enquiry.action.teaser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.action.projectproposal.ProjectProposalRepository;
import pfs.lms.enquiry.action.projectproposal.dealguaranteetimeline.DealGuaranteeTimeline;
import pfs.lms.enquiry.action.projectproposal.financials.PromoterBorrowerFinancial;
import pfs.lms.enquiry.action.projectproposal.projectdetail.ProjectDetail;
import pfs.lms.enquiry.action.projectproposal.shareholder.ShareHolder;
import pfs.lms.enquiry.domain.AssistanceType;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.domain.ProjectType;

import pfs.lms.enquiry.utils.ConversionUtils;

import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
@Slf4j
@Getter
@Setter
public class TeaserExcel {


    private SXSSFSheet sxssfSheet;
    Integer currentContentRow = 0 ;
    Integer serialNumberSecondSection = 0;

    ProjectType projectType;
    AssistanceType assistanceType;

    ConversionUtils.PeriodUnitConversion periodUnitConversion = new ConversionUtils.PeriodUnitConversion();



    public SXSSFWorkbook writeHeaderLineSXSS(SXSSFWorkbook sxssfWorkbook, SXSSFSheet sheet) {

        this.sxssfSheet = sheet;
        sxssfSheet = sxssfWorkbook.createSheet("Teaser");

        sxssfSheet.setRandomAccessWindowSize(50);
        short fontHeight = 300;
        Row row = sxssfSheet.createRow(0);



        CellStyle style = sxssfWorkbook.createCellStyle();

        Font font = sxssfWorkbook.createFont();
        font.setBold(true);
        font.setFontHeight(fontHeight);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        //style.setFillBackgroundColor(IndexedColors.RED.getIndex());
        style.setFillForegroundColor(IndexedColors.BLACK.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        style.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());

        style.setAlignment(HorizontalAlignment.CENTER);


        createSXSSCell(row, 0, "Sr.No", style);
        createSXSSCell(row, 1, "Particulars", style);
        createSXSSCell(row, 2, "Details", style);
        createSXSSCell(row, 3, "", style);
        createSXSSCell(row, 4, "", style);
        createSXSSCell(row, 5, "", style);
        createSXSSCell(row, 6, "", style);
        createSXSSCell(row, 7, "", style);


        int firstRow = 0;
        int lastRow = 0;
        int firstCol = 1;
        int lastCol = 6;
        sxssfSheet.addMergedRegion(new CellRangeAddress(0,0,1,8));

        return sxssfWorkbook;

    }


    public SXSSFWorkbook writeContentLinesSXSS(SXSSFWorkbook sxssfWorkbook, SXSSFSheet sheet,TeaserContent teaserContent) throws ParseException {

        CellStyle headerStyle = getHeaderStyle(sxssfWorkbook);
        CellStyle contentStyle = getContentStyle(sxssfWorkbook);
        CellStyle subHeaderStyle = getSubHeaderStyle(sxssfWorkbook);
        CellStyle particularsHeaderStyle = getParticularsHeaderStyle(sxssfWorkbook);
        CellStyle titleStyle = getTitleStyle(sxssfWorkbook);


        this.sxssfSheet = sxssfWorkbook.getSheet("Teaser");
        int serialNo = 1;

        TeaserLineContent content;



        content = new TeaserLineContent(serialNo ,titleStyle, "Name of the Project & Borrower", titleStyle,  teaserContent.getProjectName(), contentStyle, null, null, null, null, null, null, null, null,null, null,null, null);
        writeDataLinesSXSSFirstPage(++currentContentRow,content, true, 2,8);

        content = new TeaserLineContent(++serialNo, contentStyle,"Name of the Sponsor/ Group", titleStyle,  teaserContent.getPromoterName(), null, null, null, null, null, null, null, null, null,null, null,null, null);
        writeDataLinesSXSSFirstPage(++currentContentRow,content, true, 2,8);

        content = new TeaserLineContent(++serialNo, contentStyle, "Type of Project (Very brief project/proposal synopsis)", titleStyle, teaserContent.getProjectName(), contentStyle, null, null, null, null, null, null, null, null, null, null,null, null);
        writeDataLinesSXSSFirstPage(++currentContentRow, content, true, 2,8);

        content = new TeaserLineContent(++serialNo, contentStyle,"Rating of the Group", titleStyle, teaserContent.getGroupRating(), contentStyle, "Rating of the Project", titleStyle, teaserContent.getProjectRating(), contentStyle, null, null, null, null, null, null,null, null);
        writeDataLinesSXSSFirstPage(++currentContentRow, content, true, 4,8);

        content = new TeaserLineContent(++serialNo, contentStyle,"Type of loans and the Purpose: Corporate loan, Term loan and any other type of loan", contentStyle, teaserContent.getTypeAndPurposeOfLoan(), contentStyle, null, null, null, null, null, null, null, null, null, null ,null, null );
        writeDataLinesSXSSFirstPage(++currentContentRow, content, true, 2,6);

        content = new TeaserLineContent(++serialNo, contentStyle,"Total funds required for the project", titleStyle, teaserContent.getTotalFundsRequired(), contentStyle, "Total debt required", titleStyle, teaserContent.getProjectCost(), contentStyle, "Proposed share of PFS", titleStyle, teaserContent.getProposedShareOfPFS(), contentStyle, null, null ,null, null );
        writeDataLinesSXSSFirstPage(++currentContentRow, content, true, 6,8);


        content = new TeaserLineContent(++serialNo, contentStyle,"Total Corporate/Structured Loan Requirement", titleStyle, teaserContent.getTotalCorporateStructuredLoanRequirement(), contentStyle, "Proposed share of PFS", titleStyle, teaserContent.getProposedShareOfPFS(), contentStyle, null, contentStyle, null, null, null, null ,null, null);
        writeDataLinesSXSSFirstPage(++currentContentRow, content, true, 4,8);


        content = new TeaserLineContent(++serialNo, contentStyle,"End use of the funds to be availed from PFS", titleStyle, teaserContent.getEndUseOFundsFromPFS(), contentStyle, null, null, null, null, null, null, null, null, null, null ,null, null );
        writeDataLinesSXSSFirstPage(++currentContentRow,content, true, 2,8);

        content = new TeaserLineContent(++serialNo, contentStyle,"Rate of Interest", titleStyle, teaserContent.getRateOfInterest(), null, "Fee", titleStyle, teaserContent.getFee(), contentStyle, "Tenure", contentStyle, teaserContent.getTenure(), contentStyle, "Moratorium", titleStyle ,teaserContent.getMoratoriumPeriod(), contentStyle );
        writeDataLinesSXSSFirstPage(++currentContentRow,content, false, 2,8);


        String sourceAndCashFlow = "";
        if ( teaserContent.getSourceAndCashFlow() != null) sourceAndCashFlow = teaserContent.getSourceAndCashFlow();

        content = new TeaserLineContent(++serialNo, contentStyle,"Source and cash flow of repayment of loan and Interest", titleStyle, "Pls also share the excel calculations of both DSCR and Cash DSCR separately.\n" + sourceAndCashFlow, contentStyle, null, null, null, null, null, null, null, null, null, null ,null, null );
        writeDataLinesSXSSFirstPage(++currentContentRow,content, true, 2,8);

        content = new TeaserLineContent(++serialNo, contentStyle,"Other Collateral", titleStyle, "", null, null, null, null, null, null, null, null, null, null, null,null, null);
        writeDataLinesSXSSFirstPage(++currentContentRow,content, true, 2,8);

        content = new TeaserLineContent(++serialNo, contentStyle,"Quantification of the Security", titleStyle, teaserContent.getQuantificationOfSecurity(), null, null, null, null, null, null, titleStyle, null, null, null, titleStyle ,null, null );
        writeDataLinesSXSSFirstPage(++currentContentRow,content, true, 2,8);

        content = new TeaserLineContent(++serialNo, contentStyle,"Consolidated Group Leverage", titleStyle, "", contentStyle, "Total Debt/TNW", contentStyle, teaserContent.getTotalDebitTNW(),  contentStyle, "TOL/TNW ", contentStyle, teaserContent.getTotalTOLTNW(), contentStyle, null,null,null,null   );
        writeDataLinesSXSSFirstPage(++currentContentRow, content, true, 6,8);

        content = new TeaserLineContent(++serialNo, contentStyle,"Delays in Debt Servicing/Status of SMA etc. with various lenders", titleStyle, null, null, null, null, null, null, null, null, null, null, null, null ,null, null );
        writeDataLinesSXSSFirstPage(++currentContentRow,content, true, 2,8);

        return sxssfWorkbook;

    }




    public SXSSFSheet writeContentShareHolding(List<ShareHolder> shareHolders, SXSSFSheet sheet, SXSSFWorkbook sxssfWorkbook) throws  ParseException {

        this.sxssfSheet = sheet;
        CellStyle headerStyle = getHeaderStyle(sxssfWorkbook);
        CellStyle contentStyle = getContentStyle(sxssfWorkbook);
        CellStyle subHeaderStyle = getSubHeaderStyle(sxssfWorkbook);
        CellStyle particularsHeaderStyle = getParticularsHeaderStyle(sxssfWorkbook);
        CellStyle titleStyle = getTitleStyle(sxssfWorkbook);



        int columnCount = 0;
        int firstRow = currentContentRow + 1;

        Row row = sxssfSheet.createRow(++currentContentRow);
        createSXSSCell(row,   columnCount, "Serial Number", subHeaderStyle);
        createSXSSCell(row, ++columnCount, "Particulars", subHeaderStyle);
        createSXSSCell(row, ++columnCount, "Details", subHeaderStyle);
        createSXSSCell(row, ++columnCount, "", subHeaderStyle);
        createSXSSCell(row, ++columnCount, "", subHeaderStyle);
        createSXSSCell(row, ++columnCount, "", subHeaderStyle);
        createSXSSCell(row, ++columnCount, "", subHeaderStyle);
        createSXSSCell(row, ++columnCount, "", subHeaderStyle);
        createSXSSCell(row, ++columnCount, "", contentStyle);
        sxssfSheet.addMergedRegion(new CellRangeAddress(currentContentRow, currentContentRow, 2, 8));

        columnCount = 0;
        row = sxssfSheet.createRow(++currentContentRow);
        createSXSSCell(row,   columnCount, ++serialNumberSecondSection+".", particularsHeaderStyle);
        createSXSSCell(row, ++columnCount, "Share Holding Structure of the Borrower", particularsHeaderStyle);
        createSXSSCell(row, ++columnCount, "Company Name", subHeaderStyle);
        createSXSSCell(row, ++columnCount, "Equity Capital", subHeaderStyle);
        createSXSSCell(row, ++columnCount, "Percentage Holding", subHeaderStyle);
        createSXSSCell(row, ++columnCount, "", contentStyle);
        createSXSSCell(row, ++columnCount, "", contentStyle);
        createSXSSCell(row, ++columnCount, "", contentStyle);
        createSXSSCell(row, ++columnCount, "", contentStyle);
        sxssfSheet.addMergedRegion(new CellRangeAddress(
                currentContentRow,
                currentContentRow ,
                5,
                8));

        if (shareHolders.size() == 0) {
            return sxssfSheet;
        }

        for (ShareHolder shareHolder: shareHolders){
            columnCount = 0;
            row = sxssfSheet.createRow(++currentContentRow);
            createSXSSCell(row,   columnCount, serialNumberSecondSection, contentStyle);
            createSXSSCell(row, ++columnCount, "", contentStyle);
            createSXSSCell(row, ++columnCount, shareHolder.getCompanyName(), contentStyle);
            createSXSSCell(row, ++columnCount, shareHolder.getEquityCapital().toString(), contentStyle);
            createSXSSCell(row, ++columnCount, shareHolder.getPercentageHolding().toString(), contentStyle);
            createSXSSCell(row, ++columnCount, "", contentStyle);
            createSXSSCell(row, ++columnCount, "", contentStyle);
            createSXSSCell(row, ++columnCount, "", contentStyle);
            createSXSSCell(row, ++columnCount, "", contentStyle);
            sxssfSheet.addMergedRegion(new CellRangeAddress(
                    currentContentRow,
                    currentContentRow ,
                    5,
                    8));

        }


        sxssfSheet.addMergedRegion(new CellRangeAddress(
                firstRow + 1,
                firstRow + 1 + shareHolders.size() ,
                0,
                0));
        sxssfSheet.addMergedRegion(new CellRangeAddress(
                firstRow + 1,
                firstRow + 1 + shareHolders.size() ,
                1,
                1));

        return sxssfSheet;
    }



    public SXSSFSheet writeProjectCost(TeaserContent teaserContent, SXSSFSheet sheet, SXSSFWorkbook sxssfWorkbook) throws  ParseException {

        this.sxssfSheet = sheet;
        CellStyle headerStyle = getHeaderStyle(sxssfWorkbook);
        CellStyle contentStyle = getContentStyle(sxssfWorkbook);
        CellStyle subHeaderStyle = getSubHeaderStyle(sxssfWorkbook);
        CellStyle particularsHeaderStyle = getParticularsHeaderStyle(sxssfWorkbook);
        CellStyle titleStyle = getTitleStyle(sxssfWorkbook);



        int columnCount = 0;
        int serialNumber = 0;
        int firstRow = currentContentRow + 1;


        columnCount = 0;
        Row row = sxssfSheet.createRow(++currentContentRow);
        createSXSSCell(row,   columnCount, ++serialNumberSecondSection+".", particularsHeaderStyle);
        createSXSSCell(row, ++columnCount, "Project Cost Details i.e. Equity, Debt, etc.", particularsHeaderStyle);
        createSXSSCell(row, ++columnCount, "Project Cost", subHeaderStyle);
        createSXSSCell(row, ++columnCount, teaserContent.getProjectCost(), contentStyle);
        createSXSSCell(row, ++columnCount, "", contentStyle);
        createSXSSCell(row, ++columnCount, "", contentStyle);
        createSXSSCell(row, ++columnCount, "", contentStyle);
        createSXSSCell(row, ++columnCount, "", contentStyle);
        createSXSSCell(row, ++columnCount, "", contentStyle);
        sxssfSheet.addMergedRegion(new CellRangeAddress(
                currentContentRow,
                currentContentRow ,
                4,
                8));


        columnCount = 0;
        row = sxssfSheet.createRow(++currentContentRow);
        createSXSSCell(row,   columnCount, ++serialNumber+".", contentStyle);
        createSXSSCell(row, ++columnCount, "", contentStyle);
        createSXSSCell(row, ++columnCount, "Debt", subHeaderStyle);
        createSXSSCell(row, ++columnCount, teaserContent.getDebt(), contentStyle);
        createSXSSCell(row, ++columnCount, "", contentStyle);
        createSXSSCell(row, ++columnCount, "", contentStyle);
        createSXSSCell(row, ++columnCount, "", contentStyle);
        createSXSSCell(row, ++columnCount, "", contentStyle);
        createSXSSCell(row, ++columnCount, "", contentStyle);

        sxssfSheet.addMergedRegion(new CellRangeAddress(
                currentContentRow,
                currentContentRow ,
                4,
                8));

        columnCount = 0;
        row = sxssfSheet.createRow(++currentContentRow);
        createSXSSCell(row,   columnCount, ++serialNumber+".", contentStyle);
        createSXSSCell(row, ++columnCount, "", contentStyle);
        createSXSSCell(row, ++columnCount, "Equity", subHeaderStyle);
        createSXSSCell(row, ++columnCount, teaserContent.getEquity(), contentStyle);
        createSXSSCell(row, ++columnCount, "", contentStyle);
        createSXSSCell(row, ++columnCount, "", contentStyle);
        createSXSSCell(row, ++columnCount, "", contentStyle);
        createSXSSCell(row, ++columnCount, "", contentStyle);
        createSXSSCell(row, ++columnCount, "", contentStyle);
        sxssfSheet.addMergedRegion(new CellRangeAddress(
                currentContentRow,
                currentContentRow ,
                4,
                8));

        columnCount = 0;
        row = sxssfSheet.createRow(++currentContentRow);
        createSXSSCell(row,   columnCount, ++serialNumber+".", contentStyle);
        createSXSSCell(row, ++columnCount, "", contentStyle);
        createSXSSCell(row, ++columnCount, "Debt Equity Ratio", subHeaderStyle);
        createSXSSCell(row, ++columnCount, teaserContent.getDebtEquityRatio(), contentStyle);
        createSXSSCell(row, ++columnCount, "", contentStyle);
        createSXSSCell(row, ++columnCount, "", contentStyle);
        createSXSSCell(row, ++columnCount, "", contentStyle);
        createSXSSCell(row, ++columnCount, "", contentStyle);
        createSXSSCell(row, ++columnCount, "", contentStyle);
        sxssfSheet.addMergedRegion(new CellRangeAddress(
                currentContentRow,
                currentContentRow ,
                4,
                8));
        sxssfSheet.addMergedRegion(new CellRangeAddress(
                firstRow ,
                firstRow +   3 ,
                0,
                0));
        sxssfSheet.addMergedRegion(new CellRangeAddress(
                firstRow ,
                firstRow +  3 ,
                1,
                1));

        return sxssfSheet;


    }

    public SXSSFSheet writePage2Items(DealGuaranteeTimeline dealGuaranteeTimeline, SXSSFSheet sheet, SXSSFWorkbook sxssfWorkbook) throws  ParseException {

        TeaserLineContent content;
        CellStyle titleStyle = getTitleStyle(sxssfWorkbook);
        CellStyle contentStyle = getContentStyle(sxssfWorkbook);

        if (dealGuaranteeTimeline==null){
            dealGuaranteeTimeline = new DealGuaranteeTimeline();
        }

        content = new TeaserLineContent(serialNumberSecondSection ,titleStyle, "Deal Transaction Structure", contentStyle,  dealGuaranteeTimeline.getDealTransactionStructure(), contentStyle, null, null, null, null, null, null, null, null,null, null,null, null);
        writeDataLinesSXSSFirstPage(++currentContentRow,content, true, 2,8);

        content = new TeaserLineContent(++serialNumberSecondSection ,titleStyle, "Status of PBG and MABG if applicable", contentStyle,  dealGuaranteeTimeline.getStatusOfPBGAndMABG(), contentStyle, null, null, null, null, null, null, null, null,null, null,null, null);
        writeDataLinesSXSSFirstPage(++currentContentRow,content, true, 2,8);

        content = new TeaserLineContent(++serialNumberSecondSection ,titleStyle, "Timeline of the project milestones from the day of sanction to the day of proposed completion. ", contentStyle,  dealGuaranteeTimeline.getTimelinesMilestones(), contentStyle, null, null, null, null, null, null, null, null,null, null,null, null);
        writeDataLinesSXSSFirstPage(++currentContentRow,content, true, 2,8);

        content = new TeaserLineContent(++serialNumberSecondSection ,titleStyle, "Strengths of the Project/Proposal", contentStyle,  dealGuaranteeTimeline.getStrengths(), contentStyle, null, null, null, null, null, null, null, null,null, null,null, null);
        writeDataLinesSXSSFirstPage(++currentContentRow,content, true, 2,8);

        content = new TeaserLineContent(++serialNumberSecondSection ,titleStyle, "Details of Funding arrangements from sources other than PFS", contentStyle,  dealGuaranteeTimeline.getFundingArrangement(), contentStyle, null, null, null, null, null, null, null, null,null, null,null, null);
        writeDataLinesSXSSFirstPage(++currentContentRow,content, true, 2,8);


        content = new TeaserLineContent(++serialNumberSecondSection ,titleStyle, "Schedule of stages of disbursement of the loan", contentStyle,  dealGuaranteeTimeline.getDisbursementStageSchedule(), contentStyle, null, null, null, null, null, null, null, null,null, null,null, null);
        writeDataLinesSXSSFirstPage(++currentContentRow,content, true, 2,8);

        content = new TeaserLineContent(++serialNumberSecondSection ,titleStyle, "Any CBI/ Economic Offense Enquiry", contentStyle,  dealGuaranteeTimeline.getOffensesEnquiry(), contentStyle, null, null, null, null, null, null, null, null,null, null,null, null);
        writeDataLinesSXSSFirstPage(++currentContentRow,content, true, 2,8);

        content = new TeaserLineContent(++serialNumberSecondSection ,titleStyle, "Existing Relations of the Borrower and Group with PFS/PTC", contentStyle,  dealGuaranteeTimeline.getExistingRelationsPFSPTC(), contentStyle, null, null, null, null, null, null, null, null,null, null,null, null);
        writeDataLinesSXSSFirstPage(++currentContentRow,content, true, 2,8);

        content = new TeaserLineContent(++serialNumberSecondSection ,titleStyle, "Deviation w.r.t operating guidelines/other policies of PFS", contentStyle,  dealGuaranteeTimeline.getDeviations(), contentStyle, null, null, null, null, null, null, null, null,null, null,null, null);
        writeDataLinesSXSSFirstPage(++currentContentRow,content, true, 2,8);

        content = new TeaserLineContent(++serialNumberSecondSection ,titleStyle, "ESMS Categorization and Associated Risks", contentStyle,  dealGuaranteeTimeline.getEsmsCategorization(), contentStyle, null, null, null, null, null, null, null, null,null, null,null, null);
        writeDataLinesSXSSFirstPage(++currentContentRow,content, true, 2,8);

        content = new TeaserLineContent(++serialNumberSecondSection ,titleStyle, "Project Details", contentStyle,  dealGuaranteeTimeline.getOtherProjectDetails(), contentStyle, null, null, null, null, null, null, null, null,null, null,null, null);
        writeDataLinesSXSSFirstPage(++currentContentRow,content, true, 2,8);

        return sxssfSheet;

    }

    public SXSSFSheet writePage3Items(List<PromoterBorrowerFinancial> promoterBorrowerFinancials, SXSSFSheet sheet, SXSSFWorkbook sxssfWorkbook) throws  ParseException {

        TeaserLineContent content;
        CellStyle titleStyle = getTitleStyle(sxssfWorkbook);
        CellStyle contentStyle = getContentStyle(sxssfWorkbook);
        CellStyle finanacialsStyle = getFinancialsStyle(sxssfWorkbook);
        finanacialsStyle.setAlignment(HorizontalAlignment.CENTER);



        CellStyle financialsHeaderStyle = getFinancialsHeaderStyle(sxssfWorkbook);

        Collections.sort(promoterBorrowerFinancials, new Comparator<PromoterBorrowerFinancial>() {
            @Override
            public int compare(PromoterBorrowerFinancial u1, PromoterBorrowerFinancial u2) {
                return u1.getFiscalPeriod().compareTo(u2.getFiscalPeriod());
            }
        });



        PromoterBorrowerFinancial promoterBorrowerFinancial_Year1 = new PromoterBorrowerFinancial();
        PromoterBorrowerFinancial promoterBorrowerFinancial_Year2 = new PromoterBorrowerFinancial();;
        PromoterBorrowerFinancial promoterBorrowerFinancial_Year3 = new PromoterBorrowerFinancial();;
        PromoterBorrowerFinancial promoterBorrowerFinancial_Year4 = new PromoterBorrowerFinancial();;
        int i = 0;
            for (PromoterBorrowerFinancial promoterBorrowerFinancial: promoterBorrowerFinancials)
            {
            switch (i) {
                case 0:
                    promoterBorrowerFinancial_Year1 = promoterBorrowerFinancial;
                    break;
                case 1:
                    promoterBorrowerFinancial_Year2 = promoterBorrowerFinancial;
                    break;
                case 3:
                    promoterBorrowerFinancial_Year2 = promoterBorrowerFinancial;
                    break;
                case 4:
                    promoterBorrowerFinancial_Year2 = promoterBorrowerFinancial;
                    break;
            }
            i++;
        }


        content = new TeaserLineContent(++serialNumberSecondSection, titleStyle, "Promoter/Borrower Group Profile and Financials", contentStyle, "Particulars(INR CRs)", financialsHeaderStyle, promoterBorrowerFinancial_Year1.getFiscalPeriod(), financialsHeaderStyle, promoterBorrowerFinancial_Year2.getFiscalPeriod(), financialsHeaderStyle, promoterBorrowerFinancial_Year3.getFiscalPeriod(), financialsHeaderStyle, promoterBorrowerFinancial_Year4.getFiscalPeriod(), financialsHeaderStyle, null, null, null, null);
        writeDataLinesSXSSThirdPage(++currentContentRow, content, true, 7, 8);

        content = new TeaserLineContent("", titleStyle, "", contentStyle, "Profit & Loss", finanacialsStyle, null, null, null, null, null, null, null, null, null, null, null, null);
        writeDataLinesSXSSFirstPage(++currentContentRow, content, true, 2, 8);

        content = new TeaserLineContent("", titleStyle, "", contentStyle, "Revenue", contentStyle, promoterBorrowerFinancial_Year1.getRevenue(), contentStyle, promoterBorrowerFinancial_Year2.getRevenue(), contentStyle, promoterBorrowerFinancial_Year3.getRevenue(), contentStyle, promoterBorrowerFinancial_Year4.getRevenue(), contentStyle, null, null, null,null);
        writeDataLinesSXSSFirstPage(++currentContentRow, content, true, 5, 8);

        content = new TeaserLineContent("", titleStyle, "", contentStyle, "EBITDA", contentStyle, promoterBorrowerFinancial_Year1.getEbitda(), contentStyle, promoterBorrowerFinancial_Year2.getEbitda(), contentStyle, promoterBorrowerFinancial_Year3.getEbitda(), contentStyle, promoterBorrowerFinancial_Year4.getEbitda(), contentStyle, null, null, null,null);
        writeDataLinesSXSSFirstPage(++currentContentRow, content, true, 5, 8);

        content = new TeaserLineContent("", titleStyle, "", contentStyle, "Depreciation", contentStyle, promoterBorrowerFinancial_Year1.getDepreciation(), contentStyle, promoterBorrowerFinancial_Year2.getDepreciation(), contentStyle, promoterBorrowerFinancial_Year3.getDepreciation(), contentStyle, promoterBorrowerFinancial_Year4.getDepreciation(), contentStyle, null, null, null,null);
        writeDataLinesSXSSFirstPage(++currentContentRow, content, true, 5, 8);

        content = new TeaserLineContent("", titleStyle, "", contentStyle, "PBT  (after exceptionals)", contentStyle, promoterBorrowerFinancial_Year1.getPbt(), contentStyle, promoterBorrowerFinancial_Year2.getPbt(), contentStyle, promoterBorrowerFinancial_Year3.getPbt(), contentStyle, promoterBorrowerFinancial_Year4.getPbt(), contentStyle, null, null, null,null);
        writeDataLinesSXSSFirstPage(++currentContentRow, content, true, 5, 8);

        content = new TeaserLineContent("", titleStyle, "", contentStyle, "PAT", contentStyle, promoterBorrowerFinancial_Year1.getPat(), contentStyle, promoterBorrowerFinancial_Year2.getPat(), contentStyle, promoterBorrowerFinancial_Year3.getPat(), contentStyle, promoterBorrowerFinancial_Year4.getPat(), contentStyle, null, null, null,null);
        writeDataLinesSXSSFirstPage(++currentContentRow, content, true, 5, 8);

        content = new TeaserLineContent("", titleStyle, "", contentStyle, "Net Cash Accruals", contentStyle, promoterBorrowerFinancial_Year1.getNetCashAccruals(), contentStyle, promoterBorrowerFinancial_Year2.getNetCashAccruals(), contentStyle, promoterBorrowerFinancial_Year3.getNetCashAccruals(), contentStyle, promoterBorrowerFinancial_Year4.getNetCashAccruals(), contentStyle, null, null, null,null);
        writeDataLinesSXSSFirstPage(++currentContentRow, content, true, 5, 8);

        content = new TeaserLineContent("", titleStyle, "", contentStyle, "Balance Sheet", finanacialsStyle, null, null, null, null, null, null, null, null, null, null, null, null);
        writeDataLinesSXSSFirstPage(++currentContentRow, content, true, 2, 8);

        content = new TeaserLineContent("", titleStyle, "", contentStyle, "WC/ST Debt", contentStyle, promoterBorrowerFinancial_Year1.getWcDebt(), contentStyle, promoterBorrowerFinancial_Year2.getWcDebt(), contentStyle, promoterBorrowerFinancial_Year3.getWcDebt(), contentStyle, promoterBorrowerFinancial_Year4.getWcDebt(), contentStyle, null, null, null,null);
        writeDataLinesSXSSFirstPage(++currentContentRow, content, true, 5, 8);

        content = new TeaserLineContent("", titleStyle, "", contentStyle, "CPLTD (Current Portion of Long Term Debt)", contentStyle, promoterBorrowerFinancial_Year1.getCpltd(), contentStyle, promoterBorrowerFinancial_Year2.getCpltd(), contentStyle, promoterBorrowerFinancial_Year3.getCpltd(), contentStyle, promoterBorrowerFinancial_Year4.getCpltd(), contentStyle, null, null, null,null);
        writeDataLinesSXSSFirstPage(++currentContentRow, content, true, 5, 8);

        content = new TeaserLineContent("", titleStyle, "", contentStyle, "LT Debt (excl CPLTD)", contentStyle, promoterBorrowerFinancial_Year1.getLtDebt(), contentStyle, promoterBorrowerFinancial_Year2.getLtDebt(), contentStyle, promoterBorrowerFinancial_Year3.getLtDebt(), contentStyle, promoterBorrowerFinancial_Year4.getLtDebt(), contentStyle, null, null, null,null);
        writeDataLinesSXSSFirstPage(++currentContentRow, content, true, 5, 8);

        content = new TeaserLineContent("", titleStyle, "", contentStyle, "Total Debt", contentStyle, promoterBorrowerFinancial_Year1.getTotalDebt(), contentStyle, promoterBorrowerFinancial_Year2.getTotalDebt(), contentStyle, promoterBorrowerFinancial_Year3.getTotalDebt(), contentStyle, promoterBorrowerFinancial_Year4.getTotalDebt(), contentStyle, null, null, null,null);
        writeDataLinesSXSSFirstPage(++currentContentRow, content, true, 5, 8);

        content = new TeaserLineContent("", titleStyle, "", contentStyle, "Total Outstanding Liabilities (TOL)", contentStyle, promoterBorrowerFinancial_Year1.getTotalOutstandingLiabilities(), contentStyle, promoterBorrowerFinancial_Year2.getTotalOutstandingLiabilities(), contentStyle, promoterBorrowerFinancial_Year3.getTotalOutstandingLiabilities(), contentStyle, promoterBorrowerFinancial_Year4.getTotalOutstandingLiabilities(), contentStyle, null, null, null,null);
        writeDataLinesSXSSFirstPage(++currentContentRow, content, true, 5, 8);

        content = new TeaserLineContent("", titleStyle, "", contentStyle, "Share Capital", contentStyle, promoterBorrowerFinancial_Year1.getShareCapital(), contentStyle, promoterBorrowerFinancial_Year2.getShareCapital(), contentStyle, promoterBorrowerFinancial_Year3.getShareCapital(), contentStyle, promoterBorrowerFinancial_Year4.getShareCapital(), contentStyle, null, null, null,null);
        writeDataLinesSXSSFirstPage(++currentContentRow, content, true, 5, 8);

        content = new TeaserLineContent("", titleStyle, "", contentStyle, "Reserves and Surplus", contentStyle, promoterBorrowerFinancial_Year1.getReservesAndSurplus(), contentStyle, promoterBorrowerFinancial_Year2.getReservesAndSurplus(), contentStyle, promoterBorrowerFinancial_Year3.getReservesAndSurplus(), contentStyle, promoterBorrowerFinancial_Year4.getReservesAndSurplus(), contentStyle, null, null, null,null);
        writeDataLinesSXSSFirstPage(++currentContentRow, content, true, 5, 8);

        content = new TeaserLineContent("", titleStyle, "", contentStyle, "Tangible Net Worth(TNW)", contentStyle, promoterBorrowerFinancial_Year1.getTangibleNetWorth(), contentStyle, promoterBorrowerFinancial_Year2.getTangibleNetWorth(), contentStyle, promoterBorrowerFinancial_Year3.getTangibleNetWorth(), contentStyle, promoterBorrowerFinancial_Year4.getTangibleNetWorth(), contentStyle, null, null, null,null);
        writeDataLinesSXSSFirstPage(++currentContentRow, content, true, 5, 8);

        //"Investments in Subsidiaries/Associates"
        //content = new TeaserLineContent("", titleStyle, "", contentStyle, "Investments in Subsidiaries/Associates", contentStyle, promoterBorrowerFinancial_Year1.get(), contentStyle, promoterBorrowerFinancial_Year2.getTangibleNetWorth(), contentStyle, promoterBorrowerFinancial_Year3.getTangibleNetWorth(), contentStyle, promoterBorrowerFinancial_Year4.getTangibleNetWorth(), contentStyle, null, null, null,null);
        //writeDataLinesSXSSFirstPage(++currentContentRow, content, true, 5, 8);

        content = new TeaserLineContent("", titleStyle, "", contentStyle, "Adjusted Tangible Net Worth (ATNW)", contentStyle, promoterBorrowerFinancial_Year1.getAdjustedTangibleNetWorth(), contentStyle, promoterBorrowerFinancial_Year2.getAdjustedTangibleNetWorth(), contentStyle, promoterBorrowerFinancial_Year3.getAdjustedTangibleNetWorth(), contentStyle, promoterBorrowerFinancial_Year4.getAdjustedTangibleNetWorth(), contentStyle, null, null, null,null);
        writeDataLinesSXSSFirstPage(++currentContentRow, content, true, 5, 8);

        content = new TeaserLineContent("", titleStyle, "", contentStyle, "Cash and Bank Balance", contentStyle, promoterBorrowerFinancial_Year1.getCashAndBankBalance(), contentStyle, promoterBorrowerFinancial_Year2.getCashAndBankBalance(), contentStyle, promoterBorrowerFinancial_Year3.getCashAndBankBalance(), contentStyle, promoterBorrowerFinancial_Year4.getCashAndBankBalance(), contentStyle, null, null, null,null);
        writeDataLinesSXSSFirstPage(++currentContentRow, content, true, 5, 8);

        content = new TeaserLineContent("", titleStyle, "", contentStyle, "Current Assets", contentStyle, promoterBorrowerFinancial_Year1.getCurrentAssets(), contentStyle, promoterBorrowerFinancial_Year2.getCurrentAssets(), contentStyle, promoterBorrowerFinancial_Year3.getCurrentAssets(), contentStyle, promoterBorrowerFinancial_Year4.getCurrentAssets(), contentStyle, null, null, null,null);
        writeDataLinesSXSSFirstPage(++currentContentRow, content, true, 5, 8);

        content = new TeaserLineContent("", titleStyle, "", contentStyle, "Current Liabilities (incl. CPLTD)", contentStyle, promoterBorrowerFinancial_Year1.getCurrentLiabilities(), contentStyle, promoterBorrowerFinancial_Year2.getCurrentLiabilities(), contentStyle, promoterBorrowerFinancial_Year3.getCurrentLiabilities(), contentStyle, promoterBorrowerFinancial_Year4.getCurrentLiabilities(), contentStyle, null, null, null,null);
        writeDataLinesSXSSFirstPage(++currentContentRow, content, true, 5, 8);

          //"Inv. in Sub/Asso.",
//        content = new TeaserLineContent("", titleStyle, "", contentStyle, "Inv. in Sub/Asso.", contentStyle, promoterBorrowerFinancial_Year1.getCurrentLiabilities(), contentStyle, promoterBorrowerFinancial_Year2.getCurrentLiabilities(), contentStyle, promoterBorrowerFinancial_Year3.getCurrentLiabilities(), contentStyle, promoterBorrowerFinancial_Year4.getCurrentLiabilities(), contentStyle, null, null, null,null);
//        writeDataLinesSXSSFirstPage(++currentContentRow, content, true, 5, 8);
//
        content = new TeaserLineContent("", titleStyle, "", contentStyle, "Net Fixed Assets(inc cwip)", contentStyle, promoterBorrowerFinancial_Year1.getNetFixedAssets(), contentStyle, promoterBorrowerFinancial_Year2.getNetFixedAssets(), contentStyle, promoterBorrowerFinancial_Year3.getNetFixedAssets(), contentStyle, promoterBorrowerFinancial_Year4.getNetFixedAssets(), contentStyle, null, null, null,null);
        writeDataLinesSXSSFirstPage(++currentContentRow, content, true, 5, 8);

        content = new TeaserLineContent("", titleStyle, "", contentStyle, "FCCB/Quasi Equity", contentStyle, promoterBorrowerFinancial_Year1.getQuasiEquity(), contentStyle, promoterBorrowerFinancial_Year2.getQuasiEquity(), contentStyle, promoterBorrowerFinancial_Year3.getQuasiEquity(), contentStyle, promoterBorrowerFinancial_Year4.getQuasiEquity(), contentStyle, null, null, null,null);
        writeDataLinesSXSSFirstPage(++currentContentRow, content, true, 5, 8);


        content = new TeaserLineContent("", titleStyle, "", contentStyle, "Ratios", finanacialsStyle, null, null, null, null, null, null, null, null, null, null, null, null);
        writeDataLinesSXSSFirstPage(++currentContentRow, content, true, 2, 8);

        content = new TeaserLineContent("", titleStyle, "", contentStyle, "EBITDA Margin %", contentStyle, promoterBorrowerFinancial_Year1.getEbitdaMarginPercentage(), contentStyle, promoterBorrowerFinancial_Year2.getEbitdaMarginPercentage(), contentStyle, promoterBorrowerFinancial_Year3.getEbitdaMarginPercentage(), contentStyle, promoterBorrowerFinancial_Year4.getEbitdaMarginPercentage(), contentStyle, null, null, null,null);
        writeDataLinesSXSSFirstPage(++currentContentRow, content, true, 5, 8);

        content = new TeaserLineContent("", titleStyle, "", contentStyle, "EBITDA/ Interest", contentStyle, promoterBorrowerFinancial_Year1.getEbitdaInterest(), contentStyle, promoterBorrowerFinancial_Year2.getEbitdaInterest(), contentStyle, promoterBorrowerFinancial_Year3.getEbitdaInterest(), contentStyle, promoterBorrowerFinancial_Year4.getEbitdaInterest(), contentStyle, null, null, null,null);
        writeDataLinesSXSSFirstPage(++currentContentRow, content, true, 5, 8);

        content = new TeaserLineContent("", titleStyle, "", contentStyle, "Total Debt/ EBITDA", contentStyle, promoterBorrowerFinancial_Year1.getTotalDebtEbitda(), contentStyle, promoterBorrowerFinancial_Year2.getTotalDebtEbitda(), contentStyle, promoterBorrowerFinancial_Year3.getTotalDebtEbitda(), contentStyle, promoterBorrowerFinancial_Year4.getTotalDebtEbitda(), contentStyle, null, null, null,null);
        writeDataLinesSXSSFirstPage(++currentContentRow, content, true, 5, 8);

        content = new TeaserLineContent("", titleStyle, "", contentStyle, "Term Debt/ EBITDA", contentStyle, promoterBorrowerFinancial_Year1.getTermDebtEbitda(), contentStyle, promoterBorrowerFinancial_Year2.getTermDebtEbitda(), contentStyle, promoterBorrowerFinancial_Year3.getTermDebtEbitda(), contentStyle, promoterBorrowerFinancial_Year4.getTermDebtEbitda(), contentStyle, null, null, null,null);
        writeDataLinesSXSSFirstPage(++currentContentRow, content, true, 5, 8);

        content = new TeaserLineContent("", titleStyle, "", contentStyle, "Total Debt/ TNW", contentStyle, promoterBorrowerFinancial_Year1.getTotalDebtTnw(), contentStyle, promoterBorrowerFinancial_Year2.getTotalDebtTnw(), contentStyle, promoterBorrowerFinancial_Year3.getTotalDebtTnw(), contentStyle, promoterBorrowerFinancial_Year4.getTotalDebtTnw(), contentStyle, null, null, null,null);
        writeDataLinesSXSSFirstPage(++currentContentRow, content, true, 5, 8);

        content = new TeaserLineContent("", titleStyle, "", contentStyle, "TOL/TNW", contentStyle, promoterBorrowerFinancial_Year1.getTnw(), contentStyle, promoterBorrowerFinancial_Year2.getTnw(), contentStyle, promoterBorrowerFinancial_Year3.getTnw(), contentStyle, promoterBorrowerFinancial_Year4.getTnw(), contentStyle, null, null, null,null);
        writeDataLinesSXSSFirstPage(++currentContentRow, content, true, 5, 8);

        content = new TeaserLineContent("", titleStyle, "", contentStyle, "Current Ratio", contentStyle, promoterBorrowerFinancial_Year1.getCurrentRatio(), contentStyle, promoterBorrowerFinancial_Year2.getCurrentRatio(), contentStyle, promoterBorrowerFinancial_Year3.getCurrentRatio(), contentStyle, promoterBorrowerFinancial_Year4.getCurrentRatio(), contentStyle, null, null, null,null);
        writeDataLinesSXSSFirstPage(++currentContentRow, content, true, 5, 8);

        content = new TeaserLineContent("", titleStyle, "", contentStyle, "Cash DSCR*", contentStyle, promoterBorrowerFinancial_Year1.getCashDscr(), contentStyle, promoterBorrowerFinancial_Year2.getCashDscr(), contentStyle, promoterBorrowerFinancial_Year3.getCashDscr(), contentStyle, promoterBorrowerFinancial_Year4.getCashDscr(), contentStyle, null, null, null,null);
        writeDataLinesSXSSFirstPage(++currentContentRow, content, true, 5, 8);

        content = new TeaserLineContent("", titleStyle, "", contentStyle, "DSCR*", contentStyle, promoterBorrowerFinancial_Year1.getDscr(), contentStyle, promoterBorrowerFinancial_Year2.getDscr(), contentStyle, promoterBorrowerFinancial_Year3.getDscr(), contentStyle, promoterBorrowerFinancial_Year4.getDscr(), contentStyle, null, null, null,null);
        writeDataLinesSXSSFirstPage(++currentContentRow, content, true, 5, 8);

        return sxssfSheet;
    }


        private void writeDataLinesSXSSFirstPage(Integer currentContentRow, TeaserLineContent teaserLineContent, Boolean mergeColumns, Integer mergeStart,Integer mergeEnd) throws ParseException {

        Row row = sxssfSheet.createRow(currentContentRow);
        int columnCount = 0;

        CellStyle regularStyle = getRegularStyle(sxssfSheet.getWorkbook());

        //String mergeArea = mergeStart + currentContentRow + ":" +  mergeEnd + currentContentRow;
        if (teaserLineContent.val0 != null)
            createSXSSCell(row, columnCount, teaserLineContent.val0.toString()+".", teaserLineContent.style0);
        else
            createSXSSCell(row, columnCount, "", regularStyle);

        if (teaserLineContent.val1 != null)
            createSXSSCell(row, ++columnCount, teaserLineContent.val1, teaserLineContent.style1);
        else
            createSXSSCell(row, ++columnCount, "", regularStyle);

        if (teaserLineContent.val2 != null)
            createSXSSCell(row, ++columnCount, teaserLineContent.val2, teaserLineContent.style2);
        else
            createSXSSCell(row, ++columnCount, "", regularStyle);

        if (teaserLineContent.val3 != null)
            createSXSSCell(row, ++columnCount, teaserLineContent.val3, teaserLineContent.style3);
        else
            createSXSSCell(row, ++columnCount, "", regularStyle);

        if (teaserLineContent.val4 != null)
            createSXSSCell(row, ++columnCount, teaserLineContent.val4, teaserLineContent.style4);
        else
            createSXSSCell(row, ++columnCount, "", regularStyle);

        if (teaserLineContent.val5 != null)
            createSXSSCell(row, ++columnCount, teaserLineContent.val5, teaserLineContent.style5);
        else
            createSXSSCell(row, ++columnCount, "", regularStyle);

        if (teaserLineContent.val6 != null)
            createSXSSCell(row, ++columnCount, teaserLineContent.val6, teaserLineContent.style6);
        else
            createSXSSCell(row, ++columnCount, "", regularStyle);

        if (teaserLineContent.val7 != null)
            createSXSSCell(row, ++columnCount, teaserLineContent.val7, teaserLineContent.style7);
        else
            createSXSSCell(row, ++columnCount, "", regularStyle);
        if (teaserLineContent.val8 != null)
            createSXSSCell(row, ++columnCount, teaserLineContent.val8, teaserLineContent.style8);
        else
            createSXSSCell(row, ++columnCount, "", regularStyle);

        if (mergeColumns == true)
            sxssfSheet.addMergedRegion(new CellRangeAddress(currentContentRow, currentContentRow, mergeStart, mergeEnd));

    }

    private void writeDataLinesSXSSThirdPage(Integer currentContentRow, TeaserLineContent teaserLineContent, Boolean mergeColumns, Integer mergeStart,Integer mergeEnd) throws ParseException {

        Row row = sxssfSheet.createRow(currentContentRow);
        int columnCount = 0;

        CellStyle regularStyle = getRegularStyle(sxssfSheet.getWorkbook());

        //String mergeArea = mergeStart + currentContentRow + ":" +  mergeEnd + currentContentRow;
        if (teaserLineContent.val0 != null)
            createSXSSCell(row, columnCount, teaserLineContent.val0.toString()+".", teaserLineContent.style0);
        else
            createSXSSCell(row, columnCount, "", regularStyle);

        if (teaserLineContent.val1 != null)
            createSXSSCell(row, ++columnCount, teaserLineContent.val1, teaserLineContent.style1);
        else
            createSXSSCell(row, ++columnCount, "", regularStyle);

        if (teaserLineContent.val2 != null)
            createSXSSCell(row, ++columnCount, teaserLineContent.val2, teaserLineContent.style2);
        else
            createSXSSCell(row, ++columnCount, "", regularStyle);

        if (teaserLineContent.val3 != null)
            createSXSSCell(row, ++columnCount, teaserLineContent.val3, teaserLineContent.style3);
        else
            createSXSSCell(row, ++columnCount, "", regularStyle);

        if (teaserLineContent.val4 != null)
            createSXSSCell(row, ++columnCount, teaserLineContent.val4, teaserLineContent.style4);
        else
            createSXSSCell(row, ++columnCount, "", regularStyle);

        if (teaserLineContent.val5 != null)
            createSXSSCell(row, ++columnCount, teaserLineContent.val5, teaserLineContent.style5);
        else
            createSXSSCell(row, ++columnCount, "", regularStyle);

        if (teaserLineContent.val6 != null)
            createSXSSCell(row, ++columnCount, teaserLineContent.val6, teaserLineContent.style6);
        else
            createSXSSCell(row, ++columnCount, "", regularStyle);

        if (teaserLineContent.val7 != null)
            createSXSSCell(row, ++columnCount, teaserLineContent.val7, teaserLineContent.style7);
        else
            createSXSSCell(row, ++columnCount, "", regularStyle);
        if (teaserLineContent.val8 != null)
            createSXSSCell(row, ++columnCount, teaserLineContent.val8, teaserLineContent.style8);
        else
            createSXSSCell(row, ++columnCount, "", regularStyle);

        if (mergeColumns == true)
            sxssfSheet.addMergedRegion(new CellRangeAddress(currentContentRow, currentContentRow, mergeStart, mergeEnd));

    }


    private void createSXSSCell(Row row, int columnCount, Object value, CellStyle style) {

        sxssfSheet.trackAllColumnsForAutoSizing();
        sxssfSheet.autoSizeColumn(columnCount);




        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);


    }



    private CellStyle getHeaderStyle(SXSSFWorkbook sxssfWorkbook ){
        short fontHeight = 200;
        CellStyle headerStyle = sxssfWorkbook.createCellStyle();
        Font headerFont = sxssfWorkbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeight(fontHeight);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_80_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);

        headerStyle.setTopBorderColor(IndexedColors.GREY_80_PERCENT.getIndex());
        headerStyle.setLeftBorderColor(IndexedColors.GREY_80_PERCENT.getIndex());
        headerStyle.setRightBorderColor(IndexedColors.GREY_80_PERCENT.getIndex());
        headerStyle.setBottomBorderColor(IndexedColors.GREY_80_PERCENT.getIndex());

        return headerStyle;
    }

    private CellStyle getContentStyle(SXSSFWorkbook sxssfWorkbook ){

        short fontHeight = 200;
        CellStyle contentStyle = sxssfWorkbook.createCellStyle();
        Font headerFont = sxssfWorkbook.createFont();
        headerFont.setBold(false);
        headerFont.setFontHeight(fontHeight);
        headerFont.setColor(IndexedColors.BLACK.getIndex());
        contentStyle.setFont(headerFont);
        contentStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        contentStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        contentStyle.setWrapText(true);

        contentStyle.setBorderBottom(BorderStyle.THIN);
        contentStyle.setBorderTop(BorderStyle.THIN);
        contentStyle.setBorderRight(BorderStyle.THIN);
        contentStyle.setBorderLeft(BorderStyle.THIN);

        contentStyle.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        contentStyle.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        contentStyle.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        contentStyle.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());

        return contentStyle;
    }

    private CellStyle getRegularStyle(SXSSFWorkbook sxssfWorkbook ){

        short fontHeight = 200;
        CellStyle regularStyle = sxssfWorkbook.createCellStyle();
        Font headerFont = sxssfWorkbook.createFont();
        headerFont.setBold(false);
        headerFont.setFontHeight(fontHeight);
        headerFont.setColor(IndexedColors.BLACK.getIndex());
        regularStyle.setFont(headerFont);
        regularStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        regularStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        regularStyle.setBorderBottom(BorderStyle.THIN);
        regularStyle.setBorderTop(BorderStyle.THIN);
        regularStyle.setBorderRight(BorderStyle.THIN);
        regularStyle.setBorderLeft(BorderStyle.THIN);

        regularStyle.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        regularStyle.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        regularStyle.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        regularStyle.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());

        return regularStyle;
    }
    private CellStyle getSubHeaderStyle(SXSSFWorkbook sxssfWorkbook ){
        short fontHeight = 200;
        CellStyle subHeaderStyle = sxssfWorkbook.createCellStyle();
        Font headerFont = sxssfWorkbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeight(fontHeight);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        subHeaderStyle.setFont(headerFont);
        subHeaderStyle.setFillForegroundColor(IndexedColors.GREY_80_PERCENT.getIndex());
        subHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        subHeaderStyle.setBorderBottom(BorderStyle.THIN);
        subHeaderStyle.setBorderTop(BorderStyle.THIN);
        subHeaderStyle.setBorderRight(BorderStyle.THIN);
        subHeaderStyle.setBorderLeft(BorderStyle.THIN);

        subHeaderStyle.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        subHeaderStyle.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        subHeaderStyle.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        subHeaderStyle.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());

        return subHeaderStyle;
    }

    private CellStyle getParticularsHeaderStyle(SXSSFWorkbook sxssfWorkbook ){
        short fontHeight = 200;
        CellStyle particularsStyle = sxssfWorkbook.createCellStyle();
        Font particularFont = sxssfWorkbook.createFont();
        particularFont.setBold(true);
        particularFont.setFontHeight(fontHeight);
        particularFont.setColor(IndexedColors.BLACK.getIndex());
        particularsStyle.setFont(particularFont);
        particularsStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        particularsStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        particularsStyle.setAlignment(HorizontalAlignment.LEFT);
        particularsStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        particularsStyle.setWrapText(true);

        particularsStyle.setBorderBottom(BorderStyle.THIN);
        particularsStyle.setBorderTop(BorderStyle.THIN);
        particularsStyle.setBorderRight(BorderStyle.THIN);
        particularsStyle.setBorderLeft(BorderStyle.THIN);

        particularsStyle.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        particularsStyle.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        particularsStyle.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        particularsStyle.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());

        return particularsStyle;
    }

    private CellStyle getFinancialsStyle(SXSSFWorkbook sxssfWorkbook ){
        short fontHeight = 200;
        CellStyle financialHeaderStyle = sxssfWorkbook.createCellStyle();
        Font financialFont = sxssfWorkbook.createFont();
        financialFont.setBold(true);
        financialFont.setFontHeight(fontHeight);
        financialFont.setColor(IndexedColors.WHITE.getIndex());
        financialHeaderStyle.setFont(financialFont);
        financialHeaderStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        financialHeaderStyle.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        financialHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        financialHeaderStyle.setAlignment(HorizontalAlignment.LEFT);
        financialHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        financialHeaderStyle.setWrapText(true);

        financialHeaderStyle.setBorderBottom(BorderStyle.THIN);
        financialHeaderStyle.setBorderTop(BorderStyle.THIN);
        financialHeaderStyle.setBorderRight(BorderStyle.THIN);
        financialHeaderStyle.setBorderLeft(BorderStyle.THIN);

        financialHeaderStyle.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        financialHeaderStyle.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        financialHeaderStyle.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        financialHeaderStyle.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());

        return financialHeaderStyle;
    }
    private CellStyle getFinancialsHeaderStyle(SXSSFWorkbook sxssfWorkbook ){
        short fontHeight = 200;
        CellStyle financialHeaderStyle = sxssfWorkbook.createCellStyle();
        Font financialFont = sxssfWorkbook.createFont();
        financialFont.setBold(true);
        financialFont.setFontHeight(fontHeight);
        financialFont.setColor(IndexedColors.WHITE.getIndex());
        financialHeaderStyle.setFont(financialFont);
        financialHeaderStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        financialHeaderStyle.setFillBackgroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        financialHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        financialHeaderStyle.setAlignment(HorizontalAlignment.LEFT);
        financialHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        financialHeaderStyle.setWrapText(true);

        financialHeaderStyle.setBorderBottom(BorderStyle.THIN);
        financialHeaderStyle.setBorderTop(BorderStyle.THIN);
        financialHeaderStyle.setBorderRight(BorderStyle.THIN);
        financialHeaderStyle.setBorderLeft(BorderStyle.THIN);

        financialHeaderStyle.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        financialHeaderStyle.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        financialHeaderStyle.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        financialHeaderStyle.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());

        return financialHeaderStyle;
    }

    private CellStyle getTitleStyle(SXSSFWorkbook sxssfWorkbook ){
        short fontHeight = 200;
        CellStyle titleStyle = sxssfWorkbook.createCellStyle();
        Font particularFont = sxssfWorkbook.createFont();
        particularFont.setBold(true);
        particularFont.setFontHeight(fontHeight);
        particularFont.setColor(IndexedColors.BLACK.getIndex());
        titleStyle.setFont(particularFont);
        titleStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleStyle.setAlignment(HorizontalAlignment.LEFT);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        titleStyle.setWrapText(true);

        titleStyle.setBorderBottom(BorderStyle.THIN);
        titleStyle.setBorderTop(BorderStyle.THIN);
        titleStyle.setBorderRight(BorderStyle.THIN);
        titleStyle.setBorderLeft(BorderStyle.THIN);

        titleStyle.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        titleStyle.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        titleStyle.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        titleStyle.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());

        return titleStyle;
    }


}
