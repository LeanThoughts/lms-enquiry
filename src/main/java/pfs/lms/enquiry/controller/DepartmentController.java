package pfs.lms.enquiry.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pfs.lms.enquiry.config.ApiController;
import pfs.lms.enquiry.domain.Department;
import pfs.lms.enquiry.domain.UserRole;
import pfs.lms.enquiry.repository.DepartmentRepository;
import pfs.lms.enquiry.repository.UserRoleRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by sajeev on 16-Feb-19.
 */
@Slf4j
@ApiController
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentRepository departmentRepository;



    @RequestMapping(value = "/departments", method = {RequestMethod.GET})
    public ResponseEntity<List<Department>> getDepartments(HttpServletRequest request) {

        List<Department> departmentList = departmentRepository.findAll();

        return ResponseEntity.ok(departmentList);
    }

}
