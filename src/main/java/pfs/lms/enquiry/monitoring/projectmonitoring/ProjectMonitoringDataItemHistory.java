package pfs.lms.enquiry.monitoring.projectmonitoring;

import lombok.*;
import pfs.lms.enquiry.domain.AggregateRoot;

import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@ToString
@EqualsAndHashCode(of = {"particulars", "originalData"}, callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMonitoringDataItemHistory extends AggregateRoot<ProjectMonitoringDataItemHistory> {

    private String projectMonitoringDataId;

    private LocalDate dateOfEntry;

    private String particulars;
    private String description;
    private String originalData;
    private String revisedData1;
    private String revisedData2;
    private String remarks;
}
