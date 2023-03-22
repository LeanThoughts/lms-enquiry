package pfs.lms.enquiry.monitoring.valuer;

import java.util.List;

public interface IValuerService {

    Valuer saveValuer(ValuerResource resource, String username) throws CloneNotSupportedException;

    Valuer updateValuer(ValuerResource resource, String username) throws CloneNotSupportedException;

    List<ValuerResource> getValuers(String loanApplicationId);

    ValuerReportAndFee saveValuerReportAndFee(ValuerReportAndFeeResource resource, String username) throws CloneNotSupportedException;

    ValuerReportAndFee updateValuerReportAndFee(ValuerReportAndFeeResource resource, String username) throws CloneNotSupportedException;

    List<ValuerReportAndFeeResource> getValuerReportAndFees(String loanApplicationId);
}
