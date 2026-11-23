package pfs.lms.enquiry.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import pfs.lms.enquiry.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static pfs.lms.enquiry.usermanagement.UserRepositorySecurity.READ;
import static pfs.lms.enquiry.usermanagement.UserRepositorySecurity.WRITE;

public interface UserRepository extends JpaRepository<User, UUID> {

    @Override
    @PreAuthorize(READ)
    Optional<User> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<User> findAll();

    @Override
    @PreAuthorize(READ)
    Page<User> findAll(Pageable pageable);

    @Override
    @RestResource(exported = false)
    @PreAuthorize(WRITE)
    User save(User user);

    @Override
    @PreAuthorize(WRITE)
    void delete(User user);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    User findByEmail(String email);

    @PreAuthorize(READ)
    Set<User> findByRiskDepartmentContainingIgnoreCase(String riskDepartment);

    @PreAuthorize(READ)
    List<User> findByFirstNameStartingWith(String firstName);

    @PreAuthorize(READ)
    List<User> findByFirstNameContaining(String firstName);

    @PreAuthorize(READ)
    List<User> findByLastNameStartingWith(String lastName);

    @PreAuthorize(READ)
    List<User> findByLastNameContaining(String lastName);

    @PreAuthorize(READ)
    List<User> findByFirstNameStartingWithAndLastNameStartingWith(String firstName, String lastName);

    @PreAuthorize(READ)
    List<User> findByFirstNameStartingWithAndLastNameContaining(String firstName, String lastName);

    @PreAuthorize(READ)
    List<User> findByEmailContains(String email);

    @PreAuthorize(READ)
    List<User> findByRole(String role);

    @PreAuthorize(READ)
    List<User> findByEmailContainsAndRole(String email, String role);
}