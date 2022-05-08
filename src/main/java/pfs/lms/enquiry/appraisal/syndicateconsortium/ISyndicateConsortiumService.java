package pfs.lms.enquiry.appraisal.syndicateconsortium;

import java.util.UUID;

public interface ISyndicateConsortiumService {
    SyndicateConsortium createSyndicateConsortium(SyndicateConsortiumResource syndicateConsortiumResource);

    SyndicateConsortium updateSyndicateConsortium(SyndicateConsortiumResource syndicateConsortiumResource);

    // bankId is the same as syndicateConsortiumId
    SyndicateConsortium deleteSyndicateConsortium(UUID bankId);
}
