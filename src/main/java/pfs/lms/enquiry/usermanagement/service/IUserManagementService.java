package pfs.lms.enquiry.usermanagement.service;

import pfs.lms.enquiry.usermanagement.dto.DepartmentDto;
import pfs.lms.enquiry.usermanagement.dto.UserDto;
import pfs.lms.enquiry.usermanagement.dto.UserRequestDto;
import pfs.lms.enquiry.usermanagement.dto.UserRoleDto;

import java.util.List;
import java.util.UUID;

/**
 * User Management operations on the existing User / UserRole domain.
 * Users are never deleted; they are deactivated.
 */
public interface IUserManagementService {

    /**
     * Search users. All parameters are optional; null or blank means "no filter".
     *
     * @param query          contains-match (ignore case) on first name, last name, e-mail and user name
     * @param role           exact role code
     * @param riskDepartment exact department code
     * @param status         true = active only, false = inactive only
     */
    List<UserDto> search(String query, String role, String riskDepartment, Boolean status);

    UserDto getById(UUID id);

    UserDto create(UserRequestDto request, String username);

    UserDto update(UUID id, UserRequestDto request, String username);

    /** Activate (true) or deactivate (false) a user. */
    UserDto updateStatus(UUID id, boolean active, String username);

    /** Role master with user counts and data-quality checks. */
    List<UserRoleDto> getRoles();

    /** Risk departments for the dropdown. */
    List<DepartmentDto> getDepartments();
}
