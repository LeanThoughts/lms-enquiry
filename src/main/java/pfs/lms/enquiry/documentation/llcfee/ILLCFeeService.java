package pfs.lms.enquiry.documentation.llcfee;

import java.util.UUID;

public interface ILLCFeeService {

    LLCFee create(LLCFeeResource resource, String username);

    LLCFee update(LLCFeeResource resource, String username) throws CloneNotSupportedException;

    LLCFee delete(UUID llcFeeId, String username) throws CloneNotSupportedException;
}
