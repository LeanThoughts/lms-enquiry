package pfs.lms.enquiry.documentation.contractamendments;

import java.util.UUID;


public interface IContractAmendmentService {

    ContractAmendment create(ContractAmendmentResource resource, String username);

    ContractAmendment update(ContractAmendmentResource resource, String username) throws CloneNotSupportedException;

    ContractAmendment delete(UUID contractAmendmentId, String username) throws CloneNotSupportedException;
}

