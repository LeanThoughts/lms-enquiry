package pfs.lms.enquiry.monitoring.resource;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProjectMonitorItemResource {
    Integer serialNumber;
    private LocalDate dateOfEntry;
    private String particulars;
    private String description;
    private String originalData;
    private String remarks;

}
