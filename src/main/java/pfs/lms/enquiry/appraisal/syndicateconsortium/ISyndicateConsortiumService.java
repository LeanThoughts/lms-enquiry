package pfs.lms.enquiry.appraisal.syndicateconsortium;

import java.util.UUID;

public interface ISyndicateConsortiumService {
    SyndicateConsortium createSyndicateConsortium(SyndicateConsortiumResource syndicateConsortiumResource, String username);

    SyndicateConsortium updateSyndicateConsortium(SyndicateConsortiumResource syndicateConsortiumResource, String username) throws CloneNotSupportedException;

    // bankId is the same as syndicateConsortiumId
    SyndicateConsortium deleteSyndicateConsortium(UUID bankId, String username);
}
