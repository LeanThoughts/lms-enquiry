package pfs.lms.enquiry.monitoring.projectmonitoring;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;
import pfs.lms.enquiry.monitoring.repository.LoanMonitorRepository;
import pfs.lms.enquiry.monitoring.resource.ProjectMonitorResource;
import pfs.lms.enquiry.reports.EnquiryReportExcelV1;
import pfs.lms.enquiry.resource.LoanApplicationResource;
import pfs.lms.enquiry.resource.SearchResource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@RepositoryRestController
@RequiredArgsConstructor
public class ProjectMonitoringDataController {

    private final ProjectMonitoringDataRepository projectMonitoringDataRepository;

    private final IProjectMonitoringDataService projectMonitoringDataService;

    private final IProjectMonitoringDataItemService projectMonitoringDataItemService;

    private final LoanMonitorRepository loanMonitorRepository;
    @PostMapping("/projectMonitoringDatas/loanApplication/{loanApplicationId}")
    public ResponseEntity<ProjectMonitoringData> saveProjectMonitoringData(@PathVariable UUID loanApplicationId, HttpServletRequest request) {

        ProjectMonitoringData projectMonitoringData = projectMonitoringDataService
                .saveProjectMonitoringData(loanApplicationId, request);
        return ResponseEntity.ok(projectMonitoringData);
    }

    @GetMapping("/projectMonitoringDatas/loanApplication/{loanApplicationId}")
    public ResponseEntity<ProjectMonitoringData> getProjectMonitoringData(@PathVariable UUID loanApplicationId) {
        ProjectMonitoringData projectMonitoringData = projectMonitoringDataRepository.
                findByLoanMonitorLoanApplicationId(loanApplicationId);
        if (projectMonitoringData != null && projectMonitoringData.getProjectMonitoringDataItems() != null)
            projectMonitoringData.getProjectMonitoringDataItems().sort(Comparator.comparingInt(ProjectMonitoringDataItem
                    ::getSerialNumber));
        return ResponseEntity.ok(projectMonitoringData);
    }

    @GetMapping(value = "/projectMonitoringDatas/excel")
    public void searchAndGenerateExcel(
            HttpServletResponse response,
            @RequestParam(required = false) String projectMonitoringDataId,
            HttpServletRequest request) throws IOException, ParseException {

        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        ProjectMonitoringData projectMonitoringData = projectMonitoringDataRepository.getOne(projectMonitoringDataId);

        LoanMonitor loanMonitor = projectMonitoringData.getLoanMonitor();

        LoanApplication loanApplication = loanMonitor.getLoanApplication();


        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Project_Monitoring_History_" + currentDateTime + "_" +loanApplication.getProjectName() + ".xlsx";
        response.setHeader(headerKey, headerValue);


        List<ProjectMonitoringDataItem> projectMonitoringDataItems = new ArrayList<>(0);

        UUID projectMonitoringDataIdUUID = UUID.fromString(projectMonitoringDataId);

        ProjectMonitorResource projectMonitorResource = projectMonitoringDataItemService.getProjectMonitoringDataItemHistory(loanMonitor);

        ProjectMonitoringHistoryInExcel projectMonitoringHistoryInExcel = new ProjectMonitoringHistoryInExcel(projectMonitorResource);

        SXSSFWorkbook sxssfWorkbook = projectMonitoringHistoryInExcel.exportSXSSWorkBook(response);

    }



}
