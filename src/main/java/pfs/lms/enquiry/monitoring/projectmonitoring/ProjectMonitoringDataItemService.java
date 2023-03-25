package pfs.lms.enquiry.monitoring.projectmonitoring;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;
import pfs.lms.enquiry.monitoring.repository.LoanMonitorRepository;
import pfs.lms.enquiry.monitoring.resource.ProjectMonitorItemResource;
import pfs.lms.enquiry.monitoring.resource.ProjectMonitorResource;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectMonitoringDataItemService implements IProjectMonitoringDataItemService {
    private final LoanMonitorRepository loanMonitorRepository;

    private final ProjectMonitoringDataRepository projectMonitoringDataRepository;
    private final ProjectMonitoringDataItemRepository projectMonitoringDataItemRepository;
    private final ProjectMonitoringDataItemHistoryService projectMonitoringDataItemHistoryService;

    private final ProjectMonitoringDataItemHistoryRepository projectMonitoringDataItemHistoryRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public ProjectMonitoringDataItem saveProjectMonitoringDataItem(ProjectMonitoringDataItemResource projectMonitoringDataItemResource,
                                                                   HttpServletRequest request) {
        ProjectMonitoringDataItem projectMonitoringDataItem = new ProjectMonitoringDataItem(
                projectMonitoringDataItemResource.getSerialNumber(),
                projectMonitoringDataItemResource.getDateOfEntry(),
                projectMonitoringDataItemResource.getParticulars(),
                projectMonitoringDataItemResource.getDescription(),
                projectMonitoringDataItemResource.getOriginalData(),
                projectMonitoringDataItemResource.getRevisedData1(),
                projectMonitoringDataItemResource.getRevisedData2(),
                projectMonitoringDataItemResource.getRemarks()
        );
        projectMonitoringDataItem = projectMonitoringDataItemRepository.save(projectMonitoringDataItem);

        if (projectMonitoringDataItemResource.getProjectMonitoringDataId() != null) {
        ProjectMonitoringData projectMonitoringData =
                projectMonitoringDataRepository.getOne(projectMonitoringDataItemResource.getProjectMonitoringDataId().toString());


            // Change Documents for Project Monitoring Item
            changeDocumentService.createChangeDocument(
                    projectMonitoringData.getLoanMonitor().getId(),
                    projectMonitoringDataItemResource.getId().toString(),
                    null,
                    projectMonitoringData.getLoanMonitor().getLoanApplication().getLoanContractId(),
                    null,
                    projectMonitoringDataItemResource,
                    "Created",
                    request.getUserPrincipal().getName(),
                    "Monitoring", "Project Monitoring Item");
        }
        return projectMonitoringDataItem;
    }

    @Override
    @Transactional
    public ProjectMonitoringDataItem updateProjectMonitoringDataItem(UUID projectMonitoringDataItemId,
                                                                     ProjectMonitoringDataItemResource projectMonitoringDataItemResource,
                                                                     HttpServletRequest request) throws CloneNotSupportedException {
        ProjectMonitoringDataItem projectMonitoringDataItem = projectMonitoringDataItemRepository
                .findById( projectMonitoringDataItemId.toString())
                .orElseThrow(() -> new EntityNotFoundException(projectMonitoringDataItemId.toString()));

        Object existingProjectMonitoringItemObject = projectMonitoringDataItem.clone();
        ProjectMonitoringDataItem existingProjectMonitoringItem = (ProjectMonitoringDataItem) existingProjectMonitoringItemObject;

        if (projectMonitoringDataItem.getDateOfEntry() != null) {
            projectMonitoringDataItemHistoryService.saveProjectMonitoringDataItemHistory(
                    projectMonitoringDataItemResource.getLoanApplicationId(), projectMonitoringDataItemId,request);
        }

        projectMonitoringDataItem.setDateOfEntry(projectMonitoringDataItemResource.getDateOfEntry());
        projectMonitoringDataItem.setOriginalData(projectMonitoringDataItemResource.getOriginalData());
        projectMonitoringDataItem.setRevisedData1(projectMonitoringDataItemResource.getRevisedData1());
        projectMonitoringDataItem.setRevisedData2(projectMonitoringDataItemResource.getRevisedData2());
        projectMonitoringDataItem.setRemarks(projectMonitoringDataItemResource.getRemarks());
        projectMonitoringDataItem = projectMonitoringDataItemRepository.save(projectMonitoringDataItem);

        ProjectMonitoringData projectMonitoringData =
                projectMonitoringDataRepository.findByLoanMonitorLoanApplicationId(projectMonitoringDataItemResource.getLoanApplicationId());

    //   Optional pdi   = projectMonitoringDataRepository.findById(projectMonitoringDataItemResource.getProjectMonitoringDataId().toString());
   //    projectMonitoringData = (ProjectMonitoringData) pdi.get();

        // Change Documents for Project Monitoring Item
        changeDocumentService.createChangeDocument(
                projectMonitoringData.getLoanMonitor().getId(), projectMonitoringDataItemResource.getId().toString(),
                projectMonitoringData.getId().toString(),
                projectMonitoringData.getLoanMonitor().getLoanApplication().getLoanContractId(),
                existingProjectMonitoringItem,
                projectMonitoringDataItemResource,
                "Updated",
                request.getUserPrincipal().getName(),
                "Monitoring", "Project Monitoring Item");

        return projectMonitoringDataItem;
    }

    @Override
    public  ProjectMonitorResource  getProjectMonitoringDataItemHistory(LoanMonitor loanMonitor) {

         ProjectMonitorResource  projectMonitorResource  = new ProjectMonitorResource();
        List<ProjectMonitorItemResource> projectMonitorItemResourceList = new ArrayList<>();
        Integer serialNumber = 1;

        ProjectMonitoringData projectMonitoringData =
                projectMonitoringDataRepository.findByLoanMonitorLoanApplicationId(
                        loanMonitor.getLoanApplication().getId());

        for (ProjectMonitoringDataItem projectMonitoringDataItem: projectMonitoringData.getProjectMonitoringDataItems()) {
            ProjectMonitorItemResource projectMonitorItemResource = new ProjectMonitorItemResource();
            if (projectMonitoringDataItem.getOriginalData() !=null && projectMonitoringDataItem.getOriginalData().length() != 0) {
                projectMonitorItemResource.setSerialNumber(serialNumber);
                projectMonitorItemResource.setParticulars(projectMonitoringDataItem.getParticulars());
                projectMonitorItemResource.setDescription(projectMonitoringDataItem.getDescription());
                projectMonitorItemResource.setDateOfEntry(projectMonitoringDataItem.getDateOfEntry());
                projectMonitorItemResource.setOriginalData(projectMonitoringDataItem.getOriginalData());
                projectMonitorItemResource.setRemarks(projectMonitorItemResource.getRemarks());
                projectMonitorResource.addItem(projectMonitorItemResource);
                serialNumber++;
            }
        }

        List<ProjectMonitoringDataItemHistory> projectMonitoringDataItemHistoryList = projectMonitoringDataItemHistoryRepository.findByProjectMonitoringDataIdOrderByDateOfEntryDesc(
                projectMonitoringData.getId());

        for(ProjectMonitoringDataItemHistory historyItem : projectMonitoringDataItemHistoryList){
            ProjectMonitorItemResource projectMonitorItemResource = new ProjectMonitorItemResource();
            if (historyItem.getOriginalData() !=null && historyItem.getOriginalData().length() != 0) {
                projectMonitorItemResource.setSerialNumber(serialNumber);
                projectMonitorItemResource.setParticulars(historyItem.getParticulars());
                projectMonitorItemResource.setDescription(historyItem.getDescription());
                projectMonitorItemResource.setDateOfEntry(historyItem.getDateOfEntry());
                projectMonitorItemResource.setOriginalData(historyItem.getOriginalData());
                projectMonitorItemResource.setRemarks(historyItem.getRemarks());
                projectMonitorResource.addItem(projectMonitorItemResource);
                serialNumber++;
            }
        }

        projectMonitorResource.setProjectName( loanMonitor.getLoanApplication().getProjectName());
        projectMonitorResource.setLoanNumber( loanMonitor.getLoanApplication().getLoanContractId());
        projectMonitorResource.setDownloadDate( new Date().toString());


        return projectMonitorResource;
    }
}
