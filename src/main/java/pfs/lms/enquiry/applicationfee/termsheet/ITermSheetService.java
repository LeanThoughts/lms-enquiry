package pfs.lms.enquiry.applicationfee.termsheet;

import java.util.UUID;

public interface ITermSheetService {

    TermSheet create(TermSheetResource termSheetResource, String username);

    TermSheet update(TermSheetResource termSheetResource, String username) throws CloneNotSupportedException;

    TermSheet delete(UUID termSheetId, String username);
}
