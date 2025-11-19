package pfs.lms.enquiry.resource;

import lombok.Data;
import pfs.lms.enquiry.domain.EnquiryNo;

@Data
public class ProcessorResource {

    private EnquiryNo enquiryNo;
    private String projectDepartmentInitiator;
    private String monitoringDepartmentInitiator;
    private String riskDepartmentInitiator;
    private String riskDepartmentHead;
    private String sarcDepartmentInitiator;


}
