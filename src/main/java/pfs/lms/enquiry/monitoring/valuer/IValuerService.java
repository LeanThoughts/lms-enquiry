package pfs.lms.enquiry.monitoring.valuer;

import java.util.List;
import java.util.UUID;

public interface IValuerService {

    Valuer saveValuer(ValuerResource resource, String username) throws CloneNotSupportedException;

    Valuer updateValuer(ValuerResource resource, String username) throws CloneNotSupportedException;

    Valuer deleteValuer(UUID valuerId, String moduleName, String username);

    List<ValuerResource> getValuers(String loanApplicationId);

    ValuerReportAndFee saveValuerReportAndFee(ValuerReportAndFeeResource resource, String username) throws CloneNotSupportedException;

    ValuerReportAndFee updateValuerReportAndFee(ValuerReportAndFeeResource resource, String username) throws CloneNotSupportedException;

    List<ValuerReportAndFeeResource> getValuerReportAndFees(String loanApplicationId);

    ValuerReportAndFee deleteValuerReportAndFee(UUID valuerReportAndFeeId, String moduleName, String username);
}
