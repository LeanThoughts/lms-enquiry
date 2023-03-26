package pfs.lms.enquiry.monitoring.npa;

import java.util.List;
import java.util.UUID;

public interface INPADetailService {

    NPADetail saveNPADetail(NPADetailResource resource, String username);

    NPADetail updateNPADetail(NPADetailResource resource, String username) throws CloneNotSupportedException;

    List<NPADetail> getNPADetail(String npaId);

    NPADetail deleteNPADetail(UUID npaDetailId, String username);
}
