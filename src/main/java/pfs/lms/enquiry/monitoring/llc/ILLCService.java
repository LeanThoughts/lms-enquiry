package pfs.lms.enquiry.monitoring.llc;

import java.util.List;
import java.util.UUID;

public interface ILLCService {

    LendersLegalCouncil saveLLC(LLCResource resource, String username) throws CloneNotSupportedException;

    LendersLegalCouncil updateLLC(LLCResource resource, String username) throws CloneNotSupportedException;

    List<LLCResource> getLendersLegalCouncils(String loanApplicationId);

    LLCReportAndFee saveLLCReportAndFee(LLCReportAndFeeResource resource, String username) throws CloneNotSupportedException;

    LLCReportAndFee updateLLCReportAndFee(LLCReportAndFeeResource resource, String username) throws CloneNotSupportedException;

    List<LLCReportAndFeeResource> getLLCReportAndFee(String loanApplicationId);

    LLCReportAndFee deleteLLCReportAndFee(UUID llcReportAndFeeId, String moduleName, String username);

    LendersLegalCouncil deleteLLC(UUID llcId, String moduleName, String username);
}
