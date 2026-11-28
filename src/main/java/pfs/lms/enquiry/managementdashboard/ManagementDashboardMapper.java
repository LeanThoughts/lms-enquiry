package pfs.lms.enquiry.managementdashboard;

import com.fasterxml.jackson.databind.JsonNode;
import pfs.lms.enquiry.managementdashboard.ManagementDashboardDtos.BudgetActual;
import pfs.lms.enquiry.managementdashboard.ManagementDashboardDtos.CashFlow;
import pfs.lms.enquiry.managementdashboard.ManagementDashboardDtos.CollectionEfficiency;
import pfs.lms.enquiry.managementdashboard.ManagementDashboardDtos.Dashboard;
import pfs.lms.enquiry.managementdashboard.ManagementDashboardDtos.EclOverdue;
import pfs.lms.enquiry.managementdashboard.ManagementDashboardDtos.InterestRate;
import pfs.lms.enquiry.managementdashboard.ManagementDashboardDtos.Liquidity;
import pfs.lms.enquiry.managementdashboard.ManagementDashboardDtos.LoanBook;
import pfs.lms.enquiry.managementdashboard.ManagementDashboardDtos.Proposal;
import pfs.lms.enquiry.managementdashboard.ManagementDashboardDtos.Rating;
import pfs.lms.enquiry.managementdashboard.ManagementDashboardDtos.SmaOverdue;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Turns the OData V2 JSON of the SAP entity Dashboard (with its navigations expanded) into the portal's
 * {@link Dashboard}. SAP sends decimals as strings ("18642.000"), dates as {@code /Date(ms)/} and times as
 * {@code PT09H12M00S}; blank strings become null so the screen can tell "no value" from zero.
 */
final class ManagementDashboardMapper {

    private static final Pattern ODATA_DATE = Pattern.compile("/Date\\((-?\\d+)(?:[+-]\\d+)?\\)/");
    private static final Pattern ODATA_TIME = Pattern.compile("PT(?:(\\d+)H)?(?:(\\d+)M)?(?:(\\d+)S)?");

    private ManagementDashboardMapper() {
    }

    static Dashboard map(JsonNode d) {
        Dashboard dashboard = new Dashboard();
        dashboard.runDate = date(d.path("RunDate"));
        dashboard.amountInCrore = d.path("AmtInCr").asBoolean(false);
        dashboard.dashboardId = text(d.path("DashboardId"));
        dashboard.fiscalYear = text(d.path("Fyear"));
        dashboard.reportingMonth = text(d.path("RepMonth"));
        dashboard.fiscalYearFull = text(d.path("FyearFull"));
        LocalDate updateDate = date(d.path("UpdateDate"));
        if (updateDate != null) {
            LocalTime updateTime = time(d.path("UpdateTime"));
            dashboard.updatedOn = LocalDateTime.of(updateDate, updateTime == null ? LocalTime.MIDNIGHT : updateTime);
        }
        dashboard.budgetActual = rows(d, "BudgetActual", ManagementDashboardMapper::budgetActual);
        dashboard.interestRates = rows(d, "InterestRates", ManagementDashboardMapper::interestRate);
        dashboard.ratings = rows(d, "Ratings", ManagementDashboardMapper::rating);
        dashboard.smaOverdue = rows(d, "SmaOverdue", ManagementDashboardMapper::smaOverdue);
        dashboard.eclOverdue = rows(d, "EclOverdue", ManagementDashboardMapper::eclOverdue);
        dashboard.loanBook = rows(d, "LoanBook", ManagementDashboardMapper::loanBook);
        dashboard.cashFlow = rows(d, "CashFlow", ManagementDashboardMapper::cashFlow);
        dashboard.collectionEfficiency = rows(d, "CollEfficiency", ManagementDashboardMapper::collectionEfficiency);
        dashboard.liquidity = rows(d, "Liquidity", ManagementDashboardMapper::liquidity);
        dashboard.proposals = rows(d, "Proposals", ManagementDashboardMapper::proposal);
        return dashboard;
    }

    private static BudgetActual budgetActual(JsonNode n) {
        BudgetActual row = new BudgetActual();
        row.particulars = text(n.path("Particulars"));
        row.budget = decimal(n.path("Budget"));
        row.actual = decimal(n.path("Actual"));
        return row;
    }

    private static InterestRate interestRate(JsonNode n) {
        InterestRate row = new InterestRate();
        row.particulars = text(n.path("Particulars"));
        row.percentage = decimal(n.path("Percentage"));
        row.effectiveFrom = date(n.path("EffectiveFrom"));
        return row;
    }

    private static Rating rating(JsonNode n) {
        Rating row = new Rating();
        row.ratingTypeDesc = text(n.path("RatingTypeDesc"));
        row.crisilRating = text(n.path("CrisilRating"));
        row.icraRating = text(n.path("IcraRating"));
        return row;
    }

    private static SmaOverdue smaOverdue(JsonNode n) {
        SmaOverdue row = new SmaOverdue();
        row.smaCategory = text(n.path("SmaCategory"));
        row.particulars = text(n.path("Particulars"));
        row.principalOverdue = decimal(n.path("PrincipalOvdAmt"));
        row.interestOverdue = decimal(n.path("InterestOvdAmt"));
        row.overdue = decimal(n.path("OverdueAmt"));
        row.outstanding = decimal(n.path("OutstandingAmt"));
        return row;
    }

    private static EclOverdue eclOverdue(JsonNode n) {
        EclOverdue row = new EclOverdue();
        row.eclClassification = text(n.path("EclClassification"));
        row.particulars = text(n.path("Particulars"));
        row.principalOverdue = decimal(n.path("PrincipalOvdAmt"));
        row.interestOverdue = decimal(n.path("InterestOvdAmt"));
        row.overdue = decimal(n.path("OverdueAmt"));
        row.effectiveCapital = decimal(n.path("EffectiveCapital"));
        return row;
    }

    private static LoanBook loanBook(JsonNode n) {
        LoanBook row = new LoanBook();
        row.fiscalYear = text(n.path("Fyear"));
        row.quarter = text(n.path("Quarter"));
        row.loanBookGross = decimal(n.path("LoanBookGross"));
        row.loanBookNetOfProvisions = decimal(n.path("LoanBookNetOfProv"));
        row.grossNpa = decimal(n.path("GrossNpa"));
        row.grossNpaPercent = text(n.path("GrossNpaPct"));
        row.netNpa = decimal(n.path("NetNpa"));
        row.netNpaPercent = text(n.path("NetNpaPct"));
        row.provisionAllLoans = decimal(n.path("ProvAmtAllLoans"));
        row.provisionNpaLoans = decimal(n.path("ProvAmtNpaLoans"));
        row.provisionCoverageRatio = decimal(n.path("ProvCovRatio"));
        row.provisionCoverageRatioNpa = decimal(n.path("ProvCovRatioNpa"));
        row.costOfFunds = decimal(n.path("CostOfFunds"));
        row.yieldPercent = text(n.path("YieldPct"));
        return row;
    }

    private static CashFlow cashFlow(JsonNode n) {
        CashFlow row = new CashFlow();
        row.evaluationPeriod = text(n.path("EvaluationPeriod"));
        row.periodDesc = text(n.path("PeriodDesc"));
        row.inflowPrincipal = decimal(n.path("InflowPrincipal"));
        row.inflowInterest = decimal(n.path("InflowInterest"));
        row.inflowPrepayment = decimal(n.path("InflowPrepayment"));
        row.outflowPrincipal = decimal(n.path("OutflowPrincipal"));
        row.outflowInterest = decimal(n.path("OutflowInterest"));
        row.outflowDisbursement = decimal(n.path("OutflowDisb"));
        row.totalInflow = decimal(n.path("TotalInflow"));
        row.totalOutflow = decimal(n.path("TotalOutflow"));
        row.netFlow = decimal(n.path("NetFlow"));
        return row;
    }

    private static CollectionEfficiency collectionEfficiency(JsonNode n) {
        CollectionEfficiency row = new CollectionEfficiency();
        row.monthNumber = text(n.path("MonthNum"));
        row.monthName = text(n.path("MonthName"));
        row.collectionEfficiency = text(n.path("CollEff"));
        return row;
    }

    private static Liquidity liquidity(JsonNode n) {
        Liquidity row = new Liquidity();
        row.particulars = text(n.path("Particulars"));
        row.amount = decimal(n.path("Amount"));
        return row;
    }

    private static Proposal proposal(JsonNode n) {
        Proposal row = new Proposal();
        row.particulars = text(n.path("Particulars"));
        row.enquiryCount = text(n.path("EnqCount"));
        row.amount = decimal(n.path("Amount"));
        return row;
    }

    /** The rows of an expanded navigation: {@code nav.results} (V2) or a plain array. */
    private static <T> List<T> rows(JsonNode d, String navigation, Function<JsonNode, T> mapper) {
        List<T> rows = new ArrayList<>();
        JsonNode nav = d.path(navigation);
        JsonNode results = nav.path("results").isArray() ? nav.path("results") : nav;
        if (results.isArray()) {
            results.forEach(n -> rows.add(mapper.apply(n)));
        }
        return rows;
    }

    /** Trimmed text, or null when missing or blank. */
    static String text(JsonNode n) {
        if (n == null || n.isMissingNode() || n.isNull()) {
            return null;
        }
        String s = n.asText("").trim();
        return s.isEmpty() ? null : s;
    }

    /** OData decimal ("18642.000") or number; null when missing, blank or not a number. */
    static BigDecimal decimal(JsonNode n) {
        if (n == null || n.isMissingNode() || n.isNull()) {
            return null;
        }
        if (n.isNumber()) {
            return n.decimalValue();
        }
        String s = n.asText("").trim();
        if (s.isEmpty()) {
            return null;
        }
        try {
            return new BigDecimal(s);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    /** OData V2 date {@code /Date(1764288000000)/} (UTC midnight) or ISO {@code 2026-11-28T00:00:00}; null when missing. */
    static LocalDate date(JsonNode n) {
        String s = text(n);
        if (s == null) {
            return null;
        }
        Matcher m = ODATA_DATE.matcher(s);
        if (m.matches()) {
            return Instant.ofEpochMilli(Long.parseLong(m.group(1))).atOffset(ZoneOffset.UTC).toLocalDate();
        }
        try {
            return LocalDate.parse(s.length() > 10 ? s.substring(0, 10) : s);
        } catch (RuntimeException ex) {
            return null;
        }
    }

    /** OData V2 time {@code PT09H12M00S}; null when missing. */
    static LocalTime time(JsonNode n) {
        String s = text(n);
        if (s == null) {
            return null;
        }
        Matcher m = ODATA_TIME.matcher(s);
        if (!m.matches()) {
            return null;
        }
        return LocalTime.of(part(m.group(1)), part(m.group(2)), part(m.group(3)));
    }

    private static int part(String group) {
        return group == null ? 0 : Integer.parseInt(group);
    }
}
