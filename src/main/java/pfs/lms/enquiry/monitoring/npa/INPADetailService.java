package pfs.lms.enquiry.monitoring.npa;

import java.util.List;

public interface INPADetailService {

    NPADetail saveNPADetail(NPADetailResource resource, String username);

    NPADetail updateNPADetail(NPADetailResource resource, String username) throws CloneNotSupportedException;

    List<NPADetail> getNPADetail(String npaId, String username);
}
