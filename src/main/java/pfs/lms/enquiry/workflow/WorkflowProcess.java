package pfs.lms.enquiry.workflow;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.test.Deployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.domain.WorkflowApprover;
import pfs.lms.enquiry.repository.WorkflowApproverRepository;
import pfs.lms.enquiry.vault.FileSystemStorage;

import java.io.InputStream;
import java.util.List;

/**
 * Created by sajeev on 09-Mar-21.
 */

@Component
@RequiredArgsConstructor

public class WorkflowProcess implements CommandLineRunner{
    @Autowired
    RuntimeService runtimeService;

    @Autowired
    ProcessEngine processEngine;


    @Autowired
    WorkflowApproverRepository workflowApproverRepository;

    private static final Logger log = LoggerFactory.getLogger(FileSystemStorage.class);


    @Override
    public void run(String... strings) throws Exception {


        RepositoryService repositoryService = processEngine.getRepositoryService();


        //Get the list of Activiti Processes Deployed
        List<org.activiti.engine.repository.Deployment> deployments  =
                repositoryService
                        .createDeploymentQuery()
                        .processDefinitionKeyLike("LoansOneLevelApproval")
                        .list();

        //If the process is not deploy, create the deployment
        try {
            if (deployments.size() == 0) {
                repositoryService.createDeployment()
                        .addClasspathResource("processes/LoansOneLevelApproval.bpmn20.xml")
                        .deploy();
            }
        } catch ( Exception ex) {
            log.info("Exception Deployment Activiti Workflow Model -------------------");
            log.info(ex.toString());
        }



        // Monitoring
        WorkflowApprover workflowApproverMonitoring = workflowApproverRepository.findByProcessName("Monitoring");

        if (workflowApproverMonitoring == null) {
            // Add Approver Name for Processes
            WorkflowApprover workflowApprover = new WorkflowApprover();
            workflowApprover.setProcessName("Monitoring");
            workflowApprover.setApproverName("Sajeev Khan");
            workflowApprover.setApproverEmail("sajeev@leanthoughts.com");
            workflowApproverRepository.save(workflowApprover);
        }


        // Monitoring
        WorkflowApprover workflowApproveAppraisal = workflowApproverRepository.findByProcessName("Appraisal");

        if (workflowApproverMonitoring == null) {
            // Add Approver Name for Processes
            WorkflowApprover workflowApprover = new WorkflowApprover();
            workflowApprover.setProcessName("Appraisal");
            workflowApprover.setApproverName("Sajeev Khan");
            workflowApprover.setApproverEmail("sajeev@leanthoughts.com");
            workflowApproverRepository.save(workflowApprover);
        }



    }
}
