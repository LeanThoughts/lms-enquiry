package pfs.lms.enquiry.monitoring.npa;

public interface INPAService {

    NPA saveNPA(NPAResource resource, String username);

    NPA updateNPA(NPAResource resource, String username) throws CloneNotSupportedException;

    NPA getNPA(String loanApplicationId, String name);
}
