package pfs.lms.enquiry.configInitializer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.domain.Department;
import pfs.lms.enquiry.repository.DepartmentRepository;

/**
 * Created by sajeev on 14-May-21.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DepartmentConfigInitializer implements CommandLineRunner {
    private final DepartmentRepository departmentRepository;




    @Override
    public void run(String... strings) throws Exception {



        Department department = departmentRepository.findByCode("01");
        if (department != null) {
            department.setValue("Appraisal");
            departmentRepository.save(department);
        } else {
            department = new Department("01", "Appraisal Department");
            departmentRepository.save(department);
        }
        department = departmentRepository.findByCode("02");
        if (department != null) {
            department.setValue("Risk Department");
            departmentRepository.save(department);
        } else {
            department = new Department("02", "Risk Department");
            departmentRepository.save(department);
        }

        department = departmentRepository.findByCode("03");
        if (department != null) {
            department.setValue("Monitoring Department");
            departmentRepository.save(department);
        } else {
            department = new Department("03", "Monitoring Department");
            departmentRepository.save(department);
        }


        department = departmentRepository.findByCode("04");
        if (department != null) {
            department.setValue("Business Development");
            departmentRepository.save(department);
        } else {
            department = new Department("04", "Business Development");
            departmentRepository.save(department);
        }

        department = departmentRepository.findByCode("05");
        if (department != null) {
            department.setValue("Legal Department");
            departmentRepository.save(department);
        } else {
            department = new Department("05", "Legal Department");
            departmentRepository.save(department);
        }

        department = departmentRepository.findByCode("06");
        if (department != null) {
            department.setValue("Disbursement Dept.");
            departmentRepository.save(department);
        } else {
            department = new Department("06", "Disbursement Dept.");
            departmentRepository.save(department);
        }

        department = departmentRepository.findByCode("07");
        if (department != null) {
            department.setValue("Billing & Recovery");
            departmentRepository.save(department);
        } else {
            department = new Department("07", "Billing & Recovery");
            departmentRepository.save(department);
        }

        department = departmentRepository.findByCode("08");
        if (department != null) {
            department.setValue("Treasury");
            departmentRepository.save(department);
        } else {
            department = new Department("08", "Treasury");
            departmentRepository.save(department);
        }

        department = departmentRepository.findByCode("09");
        if (department != null) {
            department.setValue("Senior Management");
            departmentRepository.save(department);
        } else {
            department = new Department("09", "Senior Management");
            departmentRepository.save(department);
        }

        department = departmentRepository.findByCode("10");
        if (department != null) {
            department.setValue("IT Dept");
            departmentRepository.save(department);
        } else {
            department = new Department("10", "IT Dept");
            departmentRepository.save(department);
        }

        department = departmentRepository.findByCode("11");
        if (department != null) {
            department.setValue("Audit Dept");
            departmentRepository.save(department);
        } else {
            department = new Department("11", "Audit Dept");
            departmentRepository.save(department);
        }
        department = departmentRepository.findByCode("12");
        if (department != null) {
            department.setValue("SARC Dept");
            departmentRepository.save(department);
        } else {
            department = new Department("12", "SARC Dept");
            departmentRepository.save(department);
        }
        log.info("Added Departments");







    }

}
