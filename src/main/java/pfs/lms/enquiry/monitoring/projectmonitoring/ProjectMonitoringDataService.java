package pfs.lms.enquiry.monitoring.projectmonitoring;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.monitoring.repository.LoanMonitorRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class ProjectMonitoringDataService implements IProjectMonitoringDataService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanMonitorRepository loanMonitorRepository;
    private final ProjectMonitoringDataRepository projectMonitoringDataRepository;
    private final ProjectMonitoringDataItemService projectMonitoringDataItemService;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public ProjectMonitoringData saveProjectMonitoringData(UUID loanApplicationId, HttpServletRequest request) {

        LoanApplication loanApplication = loanApplicationRepository.getOne(loanApplicationId);

        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if(loanMonitor == null) {
            loanMonitor = new LoanMonitor();
            loanMonitor.setLoanApplication(loanApplication);
            loanMonitor = loanMonitorRepository.save(loanMonitor);

            changeDocumentService.createChangeDocument(
                    loanMonitor.getId(), loanMonitor.getId().toString(), null,
                    loanApplication.getLoanContractId(),
                    null,
                    loanMonitor,
                    "Created",
                    request.getUserPrincipal().getName(),
                    "Monitoring", "Header");
        }

        ProjectMonitoringData projectMonitoringData =
                projectMonitoringDataRepository.findByLoanMonitorLoanApplicationId(loanApplicationId);
        if (projectMonitoringData == null) {
            projectMonitoringData = new ProjectMonitoringData();
            projectMonitoringData.setLoanMonitor(loanMonitor);
            List<ProjectMonitoringDataItem> projectMonitoringDataItems = new ArrayList<>();
            projectMonitoringDataItems.add(projectMonitoringDataItemService
                    .saveProjectMonitoringDataItem(new ProjectMonitoringDataItemResource(null, null, null, 1, null, "1",
                            "Unit Size", "", "", "", ""),request));
            projectMonitoringDataItems.add(projectMonitoringDataItemService
                    .saveProjectMonitoringDataItem(new ProjectMonitoringDataItemResource(null, null, null, 2, null, "2",
                            "Overall Project Cost", "", "", "", ""),request));
            projectMonitoringDataItems.add(projectMonitoringDataItemService
                    .saveProjectMonitoringDataItem(new ProjectMonitoringDataItemResource(null, null, null, 3, null, "3",
                            "Debt Equity Ratio", "", "", "", ""),request));
            projectMonitoringDataItems.add(projectMonitoringDataItemService
                    .saveProjectMonitoringDataItem(new ProjectMonitoringDataItemResource(null, null, null, 4, null, "4",
                            "Total Debt", "", "", "", ""),request));
            projectMonitoringDataItems.add(projectMonitoringDataItemService
                    .saveProjectMonitoringDataItem(new ProjectMonitoringDataItemResource(null, null, null, 5, null, "5",
                            "LEV. Cost of Supply w/o ROE - Total", "", "", "", ""),request));
            projectMonitoringDataItems.add(projectMonitoringDataItemService
                    .saveProjectMonitoringDataItem(new ProjectMonitoringDataItemResource(null, null, null, 6, null, "6",
                            "DSCR (MIN)", "", "", "", ""),request));
            projectMonitoringDataItems.add(projectMonitoringDataItemService
                    .saveProjectMonitoringDataItem(new ProjectMonitoringDataItemResource(null, null, null, 7, null, "7",
                            "DSCR (MAX)", "", "", "", ""),request));
            projectMonitoringDataItems.add(projectMonitoringDataItemService
                    .saveProjectMonitoringDataItem(new ProjectMonitoringDataItemResource(null, null, null, 8, null, "8",
                            "Offtake Volume", "", "", "", ""),request));
            projectMonitoringDataItems.add(projectMonitoringDataItemService
                    .saveProjectMonitoringDataItem(new ProjectMonitoringDataItemResource(null, null, null, 9, null, "9",
                            "Offtake Mix", "", "", "", ""),request));
            projectMonitoringDataItems.add(projectMonitoringDataItemService
                    .saveProjectMonitoringDataItem(new ProjectMonitoringDataItemResource(null, null, null, 10, null, "10",
                            "Sale Rate", "", "", "", ""),request));
            projectMonitoringDataItems.add(projectMonitoringDataItemService
                    .saveProjectMonitoringDataItem(new ProjectMonitoringDataItemResource(null, null, null, 11, null, "11",
                            "Fuel Mix", "", "", "", ""),request));
            projectMonitoringDataItems.add(projectMonitoringDataItemService
                    .saveProjectMonitoringDataItem(new ProjectMonitoringDataItemResource(null, null, null, 12, null, "12",
                            "Fuel Cost", "", "", "", ""),request));
            projectMonitoringDataItems.add(projectMonitoringDataItemService
                    .saveProjectMonitoringDataItem(new ProjectMonitoringDataItemResource(null, null, null, 13, null, "13",
                            "Construction Period", "", "", "", ""),request));
            projectMonitoringDataItems.add(projectMonitoringDataItemService
                    .saveProjectMonitoringDataItem(new ProjectMonitoringDataItemResource(null, null, null, 14, null, "14",
                            "SCOD(3 Revisions)", "", "", "", ""),request));
            projectMonitoringData.setProjectMonitoringDataItems(projectMonitoringDataItems);
            projectMonitoringData = projectMonitoringDataRepository.save(projectMonitoringData);
        }
        projectMonitoringData.getProjectMonitoringDataItems().sort(Comparator.comparingInt(ProjectMonitoringDataItem
                ::getSerialNumber));

        return projectMonitoringData;
    }
}
