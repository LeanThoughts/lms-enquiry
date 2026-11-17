package pfs.lms.enquiry.reports.balancelist;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pfs.lms.enquiry.reports.SelectOptions;
import pfs.lms.enquiry.reports.balancelist.BalanceListDtos.Row;
import pfs.lms.enquiry.reports.balancelist.BalanceListDtos.Selection;

import static org.assertj.core.api.Assertions.assertThat;

class BalanceListServiceTest {

    @Test
    void emptySelectionSendsEveryParameter() {
        Selection s = new Selection();
        s.keyDate = "2026-10-17";
        String f = BalanceListService.filter(s);
        assertThat(f.startsWith("Bukrs eq 'PFS' and Ranl eq '' and Gsart eq '' and Stitel eq '' and Santwhr eq ''")).as(f).isTrue();
        assertThat(f.contains("Rrefkont eq '' and Rbear eq '' and PaInteb eq true and PaStich eq datetime'2026-10-17T00:00:00'")).as(f).isTrue();
        assertThat(f.endsWith("PaAnzw eq '' and FlgSald eq false and PaAll eq false and PaSumme eq false and PPrNew eq true and PPrOld eq false")).as(f).isTrue();
    }

    @Test
    void rangesAndSwitchesAreSent() {
        Selection s = new Selection();
        s.keyDate = "2026-10-17";
        s.contract = SelectOptions.parse("[{\"sign\":\"I\",\"option\":\"BT\",\"low\":\"1000\",\"high\":\"2000\"}]");
        s.currency = SelectOptions.parse("[{\"sign\":\"I\",\"option\":\"EQ\",\"low\":\"INR\",\"high\":\"\"}]");
        s.displayCurrency = " usd ";
        s.evaluateCompleted = false;
        s.nonZeroOnly = true;
        String f = BalanceListService.filter(s);
        assertThat(f.contains("(Ranl ge '1000' and Ranl le '2000')") || f.contains("Ranl ge '1000' and Ranl le '2000'")).as(f).isTrue();
        assertThat(f.contains("Santwhr eq 'INR'")).as(f).isTrue();
        assertThat(f.contains("PaAnzw eq 'USD'") && f.contains("PaInteb eq false") && f.contains("FlgSald eq true")).as(f).isTrue();
    }

    @Test
    void loanTypePropertyIsConfigurable() {
        Selection s = new Selection();
        s.keyDate = "2026-10-17";
        s.loanType = SelectOptions.parse("[{\"sign\":\"I\",\"option\":\"EQ\",\"low\":\"A1\",\"high\":\"\"}]");
        assertThat(BalanceListService.filter(s, "Gsart", "Other")).contains("Other eq 'A1'");
    }

    @Test
    void rowKeepsAmountsAndShowsMoratoriumAsX() throws Exception {
        Row r = BalanceListService.row(new ObjectMapper().readTree(
                "{\"Ranl\":\"0000010003814\",\"Moratorium\":true,\"Nomkap\":\"1250000.000\",\"Status\":\"  \"}"));
        assertThat(r.get("ranl")).isEqualTo("0000010003814");
        assertThat(r.get("moratorium")).isEqualTo("X");
        assertThat(r.get("nomkap")).isEqualTo("1250000.000");
        assertThat(r.get("status")).isNull();
    }
}
