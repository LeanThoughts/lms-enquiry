package pfs.lms.enquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.domain.Department;
import pfs.lms.enquiry.domain.UserRole;

import java.util.UUID;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Department findByCode(String code);
}
