package pfs.lms.enquiry.reports;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pfs.lms.enquiry.exception.LmsException;
import pfs.lms.enquiry.reports.SelectOptions.Kind;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** ABAP SELECT-OPTIONS semantics of the report selection screens: lines to OData $filter. */
@DisplayName("Report select-options")
class SelectOptionsTest {

    private static SelectOption so(String sign, String option, String low, String high) {
        return new SelectOption(sign, option, low, high);
    }

    private static String text(SelectOption... lines) {
        return SelectOptions.filter("Ranl", Arrays.asList(lines), Kind.TEXT, "Contract");
    }

    @Test
    void emptySelectionIsSentAsEmptyValue() {
        assertThat(SelectOptions.filter("Ranl", Collections.<SelectOption>emptyList(), Kind.TEXT, "C")).isEqualTo("Ranl eq ''");
        assertThat(SelectOptions.filter("D", null, Kind.DATE, "D"))
                .isEqualTo("D ge datetime'0001-01-01T00:00:00' and D le datetime'9999-12-31T23:59:59'");
        assertThat(text(so("I", "EQ", "", ""))).isEqualTo("Ranl eq ''");
    }

    @Test
    void singleValueRangeAndUpTo() {
        assertThat(text(so("I", "EQ", "100", ""))).isEqualTo("Ranl eq '100'");
        assertThat(text(so("I", "BT", "100", "200"))).isEqualTo("Ranl ge '100' and Ranl le '200'");
        assertThat(text(so("I", "BT", "100", ""))).isEqualTo("Ranl eq '100'");
        assertThat(text(so("I", "BT", "", "200"))).isEqualTo("Ranl le '200'");
    }

    @Test
    void severalIncludesAreOrdered() {
        assertThat(text(so("I", "EQ", "1", ""), so("I", "EQ", "2", ""), so("I", "BT", "5", "9")))
                .isEqualTo("(Ranl eq '1' or Ranl eq '2' or (Ranl ge '5' and Ranl le '9'))");
    }

    @Test
    void excludesAreNegatedAndAnded() {
        assertThat(text(so("I", "BT", "100", "200"), so("E", "EQ", "150", "")))
                .isEqualTo("Ranl ge '100' and Ranl le '200' and Ranl ne '150'");
        assertThat(text(so("I", "EQ", "1", ""), so("I", "EQ", "2", ""), so("E", "BT", "10", "20")))
                .isEqualTo("(Ranl eq '1' or Ranl eq '2') and (Ranl lt '10' or Ranl gt '20')");
        assertThat(text(so("E", "GE", "500", ""))).isEqualTo("Ranl lt '500'");
        assertThat(text(so("E", "EQ", "7", ""))).isEqualTo("Ranl ne '7'");
    }

    @Test
    void patterns() {
        assertThat(text(so("I", "CP", "10*", ""))).isEqualTo("startswith(Ranl,'10')");
        assertThat(text(so("I", "CP", "*9", ""))).isEqualTo("endswith(Ranl,'9')");
        assertThat(text(so("I", "CP", "*55*", ""))).isEqualTo("substringof('55',Ranl)");
        assertThat(text(so("E", "CP", "10*", ""))).isEqualTo("not startswith(Ranl,'10')");
        assertThatThrownBy(() -> text(so("I", "CP", "1*0", ""))).isInstanceOf(LmsException.class);
    }

    @Test
    void datesAreValidatedAndUseSapSemantics() {
        List<SelectOption> one = Collections.singletonList(so("I", "EQ", "2026-10-01", ""));
        assertThat(SelectOptions.filter("Datum", one, Kind.DATE, "Date")).isEqualTo("Datum eq datetime'2026-10-01T00:00:00'");
        List<SelectOption> range = Collections.singletonList(so("I", "BT", "2026-10-01", "2026-10-12"));
        assertThat(SelectOptions.filter("Datum", range, Kind.DATE, "Date"))
                .isEqualTo("Datum ge datetime'2026-10-01T00:00:00' and Datum le datetime'2026-10-12T23:59:59'");
        assertThatThrownBy(() -> SelectOptions.filter("Datum", Collections.singletonList(so("I", "BT", "2026-10-12", "2026-10-01")),
                Kind.DATE, "Date")).isInstanceOf(LmsException.class);
        assertThatThrownBy(() -> SelectOptions.filter("Datum", Collections.singletonList(so("I", "EQ", "12.10.2026", "")),
                Kind.DATE, "Date")).isInstanceOf(LmsException.class);
    }

    @Test
    void parseAndDescribe() {
        List<SelectOption> lines = SelectOptions.parse(
                "[{\"sign\":\"I\",\"option\":\"BT\",\"low\":\"100\",\"high\":\"200\"},{\"sign\":\"E\",\"option\":\"EQ\",\"low\":\"150\",\"high\":\"\"}]");
        assertThat(lines).hasSize(2);
        assertThat(SelectOptions.describe(lines)).isEqualTo("100 – 200; excl. 150");
        assertThat(SelectOptions.parse(null)).isEmpty();
        assertThat(SelectOptions.describeOrNull(lines.subList(0, 0))).isNull();
        assertThatThrownBy(() -> SelectOptions.parse("{oops")).isInstanceOf(LmsException.class);
    }
}
