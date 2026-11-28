package pfs.lms.enquiry.managementdashboard;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pfs.lms.enquiry.exception.LmsException;
import pfs.lms.enquiry.managementdashboard.ManagementDashboardDtos.Dashboard;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * The SAP JSON of ZPFS_LMS_DASHBOARD_SRV (DashboardSet with $expand) mapped to the portal's dashboard.
 */
class ManagementDashboardMapperTest {

    private static final String SAP_JSON = "{\"d\":{"
            + "\"__metadata\":{\"id\":\"x\",\"type\":\"ZPFS_LMS_DASHBOARD_SRV.Dashboard\"},"
            + "\"RunDate\":\"\\/Date(1795824000000)\\/\",\"AmtInCr\":true,\"DashboardId\":\"DB-20261128\","
            + "\"Fyear\":\"2026\",\"RepMonth\":\"11\",\"FyearFull\":\"2026-27\","
            + "\"UpdateDate\":\"\\/Date(1795824000000)\\/\",\"UpdateTime\":\"PT09H12M00S\","
            + "\"BudgetActual\":{\"results\":[{\"Particulars\":\"Sanctions\",\"Budget\":\"4500.000\",\"Actual\":\"3120.000\"}]},"
            + "\"InterestRates\":{\"results\":[{\"Particulars\":\"Repo Rate\",\"Percentage\":\"6.25\",\"EffectiveFrom\":\"\\/Date(1754697600000)\\/\"}]},"
            + "\"Ratings\":{\"results\":[{\"RatingTypeDesc\":\"NCD\",\"CrisilRating\":\"AA-\",\"IcraRating\":\"\"}]},"
            + "\"SmaOverdue\":{\"results\":[{\"SmaCategory\":\"0\",\"Particulars\":\"SMA-0\",\"PrincipalOvdAmt\":\"42.100\","
            + "\"InterestOvdAmt\":\"18.600\",\"OverdueAmt\":\"60.700\",\"OutstandingAmt\":\"980.000\"}]},"
            + "\"EclOverdue\":{\"results\":[{\"EclClassification\":\"3\",\"Particulars\":\"Stage 3\",\"PrincipalOvdAmt\":\"214.600\","
            + "\"InterestOvdAmt\":\"96.200\",\"OverdueAmt\":\"310.800\",\"EffectiveCapital\":\"612.000\"}]},"
            + "\"LoanBook\":{\"results\":[{\"Fyear\":\"2026\",\"Quarter\":\"Q2\",\"LoanBookGross\":\"18642.000\","
            + "\"LoanBookNetOfProv\":\"17905.000\",\"GrossNpa\":\"612.000\",\"GrossNpaPct\":\"3.28\",\"NetNpa\":\"214.000\","
            + "\"NetNpaPct\":\"1.19\",\"ProvAmtAllLoans\":\"737.000\",\"ProvAmtNpaLoans\":\"398.000\",\"ProvCovRatio\":\"3.950\","
            + "\"ProvCovRatioNpa\":\"65.000\",\"CostOfFunds\":\"7.820000\",\"YieldPct\":\"10.46\"}]},"
            + "\"CashFlow\":{\"results\":[{\"EvaluationPeriod\":\"M\",\"PeriodDesc\":\"Next month\",\"InflowPrincipal\":\"186.000\","
            + "\"InflowInterest\":\"142.000\",\"InflowPrepayment\":\"20.000\",\"OutflowPrincipal\":\"95.000\",\"OutflowInterest\":\"88.000\","
            + "\"OutflowDisb\":\"210.000\",\"TotalInflow\":\"348.000\",\"TotalOutflow\":\"393.000\",\"NetFlow\":\"-45.000\"}]},"
            + "\"CollEfficiency\":{\"results\":[{\"MonthNum\":\"04\",\"MonthName\":\"April\",\"CollEff\":\"97.2\"}]},"
            + "\"Liquidity\":{\"results\":[{\"Particulars\":\"Cash\",\"Amount\":\"412.000\"}]},"
            + "\"Proposals\":{\"results\":[{\"Particulars\":\"Enquiries\",\"EnqCount\":\"38\",\"Amount\":\"9450.000\"}]}"
            + "}}";

    private final ManagementDashboardSapClient client = new ManagementDashboardSapClient(
            "http://sap.example:8000", "360", "user", "pw", "/sap/opu/odata/sap/ZPFS_LMS_DASHBOARD_SRV");

    @Test
    @DisplayName("The dashboard URL reads DashboardSet by RunDate and AmtInCr with all ten navigations expanded")
    void url() {
        String url = client.url(LocalDate.of(2026, 11, 28), true);
        assertThat(url).startsWith("http://sap.example:8000/sap/opu/odata/sap/ZPFS_LMS_DASHBOARD_SRV/DashboardSet"
                + "(RunDate=datetime'2026-11-28T00:00:00',AmtInCr=true)?$expand=");
        assertThat(url).contains("BudgetActual,InterestRates,Ratings,SmaOverdue,EclOverdue,LoanBook,CashFlow,"
                + "CollEfficiency,Liquidity,Proposals");
        assertThat(url).endsWith("&$format=json&sap-client=360");
    }

    @Test
    @DisplayName("Header keys, dates, times, decimals and every navigation are mapped")
    void map() {
        Dashboard dashboard = ManagementDashboardMapper.map(client.entity(SAP_JSON));

        assertThat(dashboard.runDate).isEqualTo(LocalDate.of(2026, 11, 28));
        assertThat(dashboard.amountInCrore).isTrue();
        assertThat(dashboard.dashboardId).isEqualTo("DB-20261128");
        assertThat(dashboard.fiscalYearFull).isEqualTo("2026-27");
        assertThat(dashboard.reportingMonth).isEqualTo("11");
        assertThat(dashboard.updatedOn).isEqualTo(LocalDateTime.of(2026, 11, 28, 9, 12));

        assertThat(dashboard.budgetActual).hasSize(1);
        assertThat(dashboard.budgetActual.get(0).budget).isEqualByComparingTo(new BigDecimal("4500"));
        assertThat(dashboard.interestRates.get(0).effectiveFrom).isEqualTo(LocalDate.of(2025, 8, 9));
        assertThat(dashboard.ratings.get(0).icraRating).as("blank text is null").isNull();
        assertThat(dashboard.smaOverdue.get(0).outstanding).isEqualByComparingTo(new BigDecimal("980"));
        assertThat(dashboard.eclOverdue.get(0).effectiveCapital).isEqualByComparingTo(new BigDecimal("612"));
        assertThat(dashboard.loanBook.get(0).quarter).isEqualTo("Q2");
        assertThat(dashboard.loanBook.get(0).loanBookGross).isEqualByComparingTo(new BigDecimal("18642"));
        assertThat(dashboard.loanBook.get(0).grossNpaPercent).isEqualTo("3.28");
        assertThat(dashboard.loanBook.get(0).costOfFunds).isEqualByComparingTo(new BigDecimal("7.82"));
        assertThat(dashboard.cashFlow.get(0).netFlow).isEqualByComparingTo(new BigDecimal("-45"));
        assertThat(dashboard.collectionEfficiency.get(0).monthName).isEqualTo("April");
        assertThat(dashboard.liquidity.get(0).amount).isEqualByComparingTo(new BigDecimal("412"));
        assertThat(dashboard.proposals.get(0).enquiryCount).isEqualTo("38");
    }

    @Test
    @DisplayName("Missing navigations give empty lists, not nulls")
    void missingNavigations() {
        Dashboard dashboard = ManagementDashboardMapper.map(client.entity("{\"d\":{\"AmtInCr\":false}}"));
        assertThat(dashboard.loanBook).isEmpty();
        assertThat(dashboard.proposals).isEmpty();
        assertThat(dashboard.runDate).isNull();
        assertThat(dashboard.updatedOn).isNull();
    }

    @Test
    @DisplayName("An answer without entity is rejected as a gateway error")
    void badAnswer() {
        assertThatThrownBy(() -> client.entity("not json")).isInstanceOf(LmsException.class)
                .hasMessageContaining("not JSON");
        assertThatThrownBy(() -> client.entity("{\"d\":{\"results\":[]}}")).isInstanceOf(LmsException.class)
                .hasMessageContaining("No dashboard data");
    }
}
