package pfs.lms.enquiry.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pfs.lms.enquiry.config.ApiController;
import pfs.lms.enquiry.domain.WorkflowApprover;
import pfs.lms.enquiry.repository.WorkflowApproverRepository;

import java.util.List;
import java.util.Objects;

@ApiController
@RequiredArgsConstructor
@Slf4j
public class WorkflowApproverController {

    private final WorkflowApproverRepository workflowApproverRepository;

    @GetMapping("/workflowApprovers/list")
    public ResponseEntity<List<WorkflowApprover>> list(@RequestParam(required = false) String departmentCode,
                                                       @RequestParam(required = false) String processName) {

        WorkflowApprover probe = new WorkflowApprover();
        probe.setDepartmentCode(StringUtils.hasText(departmentCode) ? departmentCode : null);
        probe.setProcessName(StringUtils.hasText(processName) ? processName : null);
        return ResponseEntity.ok(workflowApproverRepository.findAll(Example.of(probe), Sort.by("departmentCode", "processName")));
    }

    @PostMapping("/workflowApprovers/create")
    public ResponseEntity<WorkflowApprover> create(@RequestBody WorkflowApprover resource) {

        validate(resource, null);
        WorkflowApprover workflowApprover = new WorkflowApprover();
        copy(resource, workflowApprover);
        return ResponseEntity.ok(workflowApproverRepository.save(workflowApprover));
    }

    @PutMapping("/workflowApprovers/update")
    public ResponseEntity<WorkflowApprover> update(@RequestBody WorkflowApprover resource) {

        WorkflowApprover workflowApprover = workflowApproverRepository.findById(resource.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Workflow approver not found."));
        validate(resource, workflowApprover.getId());
        copy(resource, workflowApprover);
        return ResponseEntity.ok(workflowApproverRepository.save(workflowApprover));
    }

    @DeleteMapping("/workflowApprovers/delete/{id}")
    public void delete(@PathVariable Integer id) {

        WorkflowApprover workflowApprover = workflowApproverRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Workflow approver not found."));
        workflowApproverRepository.delete(workflowApprover);
    }

    private void copy(WorkflowApprover source, WorkflowApprover target) {
        target.setDepartmentCode(StringUtils.hasText(source.getDepartmentCode()) ? source.getDepartmentCode().trim() : null);
        target.setProcessName(source.getProcessName().trim());
        target.setApproverName(source.getApproverName().trim());
        target.setApproverEmail(source.getApproverEmail().trim());
    }

    /**
     * The workflow looks up a single approver by department and process, so the combination must be unique
     */
    private void validate(WorkflowApprover resource, Integer currentId) {

        if (!StringUtils.hasText(resource.getProcessName()) || !StringUtils.hasText(resource.getApproverName())
                || !StringUtils.hasText(resource.getApproverEmail()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Process name, approver name and approver email are required.");

        String departmentCode = StringUtils.hasText(resource.getDepartmentCode()) ? resource.getDepartmentCode().trim() : null;
        String processName = resource.getProcessName().trim();
        WorkflowApprover probe = new WorkflowApprover();
        probe.setProcessName(processName);
        boolean duplicate = workflowApproverRepository.findAll(Example.of(probe)).stream()
                .anyMatch(approver -> Objects.equals(approver.getDepartmentCode(), departmentCode)
                        && !approver.getId().equals(currentId));
        if (duplicate)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A workflow approver already exists for department " + departmentCode + " and process " + processName + ".");
    }
}
