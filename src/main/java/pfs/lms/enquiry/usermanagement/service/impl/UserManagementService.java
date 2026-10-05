package pfs.lms.enquiry.usermanagement.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pfs.lms.enquiry.domain.Department;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.domain.UserRole;
import pfs.lms.enquiry.exception.LmsException;
import pfs.lms.enquiry.repository.DepartmentRepository;
import pfs.lms.enquiry.repository.UserRepository;
import pfs.lms.enquiry.repository.UserRoleRepository;
import pfs.lms.enquiry.usermanagement.dto.DepartmentDto;
import pfs.lms.enquiry.usermanagement.dto.UserDto;
import pfs.lms.enquiry.usermanagement.dto.UserRequestDto;
import pfs.lms.enquiry.usermanagement.dto.UserRoleDto;
import pfs.lms.enquiry.usermanagement.service.IUserManagementService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * User Management on the existing {@link User} and {@link UserRole} entities.
 * <p>
 * Works on the portal user table only: no OAuth account, Business Partner or e-mail side effects.
 * Users are never deleted, only deactivated.
 * <p>
 * Role and department lookups load the (small) master tables once per call instead of using
 * {@code findByCode}, so duplicate codes in the master data do not cause exceptions.
 */
@Service
@Transactional
public class UserManagementService implements IUserManagementService {

    private static final Logger log = LoggerFactory.getLogger(UserManagementService.class);

    /** Shape of an SAP role code such as ZLM023 or TR0100. */
    private static final Pattern ROLE_CODE = Pattern.compile("^[A-Z]{2,3}\\d{3,4}$");

    /** End date of a user that is valid without limit. */
    public static final LocalDate OPEN_END_DATE = LocalDate.of(9999, 12, 31);

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final DepartmentRepository departmentRepository;

    public UserManagementService(UserRepository userRepository,
                                 UserRoleRepository userRoleRepository,
                                 DepartmentRepository departmentRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.departmentRepository = departmentRepository;
    }

    // ------------------------------------------------------------------ queries

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> search(String query, String role, String riskDepartment, Boolean status) {
        String q = isBlank(query) ? null : query.trim().toLowerCase(Locale.ROOT);
        Map<String, List<UserRole>> roles = rolesByCode();
        Map<String, Department> departments = departmentsByCode();

        return userRepository.findAll().stream()
                .filter(u -> q == null
                        || containsIgnoreCase(u.getFirstName(), q)
                        || containsIgnoreCase(u.getLastName(), q)
                        || containsIgnoreCase(u.getEmail(), q)
                        || containsIgnoreCase(u.getUserName(), q))
                .filter(u -> isBlank(role) || role.equals(u.getRole()))
                .filter(u -> isBlank(riskDepartment) || riskDepartment.equals(u.getRiskDepartment()))
                .filter(u -> status == null || status == u.isStatus())
                .sorted(Comparator.comparing((User u) -> nullToEmpty(u.getFirstName()), String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(u -> nullToEmpty(u.getLastName()), String.CASE_INSENSITIVE_ORDER))
                .map(u -> toDto(u, roles, departments))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getById(UUID id) {
        return toDto(findOrThrow(id), rolesByCode(), departmentsByCode());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserRoleDto> getRoles() {
        List<UserRole> all = userRoleRepository.findAll();
        Map<String, Long> codeCounts = all.stream()
                .collect(Collectors.groupingBy(r -> nullToEmpty(r.getCode()).trim(), Collectors.counting()));
        Map<String, Long> usersPerRole = userRepository.findAll().stream()
                .filter(u -> u.getRole() != null)
                .collect(Collectors.groupingBy(User::getRole, Collectors.counting()));

        return all.stream()
                .sorted(Comparator.comparing((UserRole r) -> nullToEmpty(r.getCode()))
                        .thenComparing(r -> nullToEmpty(r.getValue())))
                .map(r -> new UserRoleDto(
                        r.getId(),
                        r.getCode(),
                        r.getValue(),
                        isBlank(r.getCode()) ? 0 : usersPerRole.getOrDefault(r.getCode(), 0L),
                        roleIssue(r, codeCounts)))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentDto> getDepartments() {
        return departmentsByCode().values().stream()
                .map(d -> new DepartmentDto(d.getCode(), d.getValue()))
                .sorted(Comparator.comparing(d -> nullToEmpty(d.getValue()), String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    // ------------------------------------------------------------------ commands

    @Override
    public UserDto create(UserRequestDto request, String username) {
        String email = request.getEmail().trim();
        if (emailTaken(email, null)) {
            throw new LmsException("A user with e-mail " + email + " already exists.", HttpStatus.CONFLICT);
        }
        Map<String, List<UserRole>> roles = rolesByCode();
        Map<String, Department> departments = departmentsByCode();
        UserRole role = resolveRole(request, roles);
        validateRiskDepartment(request.getRiskDepartment(), departments);

        // New users are always created active: valid from today until 31.12.9999.
        User user = new User();
        apply(user, request, email, role);
        user.setStatus(true);
        user.setStartDate(LocalDate.now());
        user.setEndDate(OPEN_END_DATE);
        user.setCreatedOn(LocalDate.now());
        user.setCreatedAt(LocalTime.now());
        user.setCreatedByUserName(username);

        user = userRepository.save(user);
        log.info("User Management: user {} created by {}", user.getEmail(), username);
        return toDto(user, roles, departments);
    }

    @Override
    public UserDto update(UUID id, UserRequestDto request, String username) {
        User user = findOrThrow(id);
        String email = request.getEmail().trim();
        if (emailTaken(email, user.getId())) {
            throw new LmsException("A user with e-mail " + email + " already exists.", HttpStatus.CONFLICT);
        }
        Map<String, List<UserRole>> roles = rolesByCode();
        Map<String, Department> departments = departmentsByCode();
        UserRole role = resolveRole(request, roles);
        validateRiskDepartment(request.getRiskDepartment(), departments);

        if (Boolean.FALSE.equals(request.getStatus()) && user.isStatus()) {
            checkNotSelf(user, username, "deactivate");
        }

        apply(user, request, email, role);
        if (request.getStatus() != null) {
            changeStatus(user, request.getStatus());
        }
        stampChange(user, username);

        user = userRepository.save(user);
        log.info("User Management: user {} updated by {}", user.getEmail(), username);
        return toDto(user, roles, departments);
    }

    @Override
    public UserDto updateStatus(UUID id, boolean active, String username) {
        User user = findOrThrow(id);
        if (!active) {
            checkNotSelf(user, username, "deactivate");
        }
        changeStatus(user, active);
        stampChange(user, username);
        user = userRepository.save(user);
        log.info("User Management: user {} {} by {}", user.getEmail(), active ? "activated" : "deactivated", username);
        return toDto(user, rolesByCode(), departmentsByCode());
    }

    // ------------------------------------------------------------------ helpers

    /**
     * Applies a status change and keeps the validity period in step:
     * deactivating ends the validity today, re-activating opens it again until 31.12.9999.
     * The start date is never changed here. Nothing happens when the status is unchanged.
     */
    private static void changeStatus(User user, boolean active) {
        if (user.isStatus() == active) {
            return;
        }
        user.setStatus(active);
        user.setEndDate(active ? OPEN_END_DATE : LocalDate.now());
    }

    private void apply(User user, UserRequestDto r, String email, UserRole role) {
        user.setFirstName(r.getFirstName().trim());
        user.setLastName(r.getLastName().trim());
        user.setEmail(email);
        user.setUserName(isBlank(r.getUserName()) ? null : r.getUserName().trim());
        user.setRole(role.getCode());
        user.setRoleDescription(role.getValue());
        user.setSapBPNumber(isBlank(r.getSapBPNumber()) ? null : r.getSapBPNumber().trim());

        boolean hasRiskDepartment = !isBlank(r.getRiskDepartment());
        user.setRiskDepartment(hasRiskDepartment ? r.getRiskDepartment().trim() : null);
        // Department head and display-only access only apply to risk department users.
        user.setDepartmentHead(hasRiskDepartment && Boolean.TRUE.equals(r.getDepartmentHead()));
        user.setRiskPortalDisplayOnlyAccess(hasRiskDepartment && Boolean.TRUE.equals(r.getRiskPortalDisplayOnlyAccess()));
    }

    private UserDto toDto(User u, Map<String, List<UserRole>> roles, Map<String, Department> departments) {
        UserDto dto = new UserDto();
        dto.setId(u.getId());
        dto.setFirstName(u.getFirstName());
        dto.setLastName(u.getLastName());
        dto.setEmail(u.getEmail());
        dto.setUserName(u.getUserName());
        dto.setRole(u.getRole());
        dto.setRoleDescription(!isBlank(u.getRoleDescription()) ? u.getRoleDescription() : firstRoleValue(u.getRole(), roles));
        dto.setSapBPNumber(u.getSapBPNumber());
        dto.setRiskDepartment(u.getRiskDepartment());
        Department department = u.getRiskDepartment() == null ? null : departments.get(u.getRiskDepartment());
        dto.setRiskDepartmentName(department != null ? department.getValue() : null);
        dto.setDepartmentHead(u.isDepartmentHead());
        dto.setRiskPortalDisplayOnlyAccess(u.getRiskPortalDisplayOnlyAccess());
        dto.setStatus(u.isStatus());
        dto.setStartDate(u.getStartDate());
        dto.setEndDate(u.getEndDate());
        dto.setCreatedOn(u.getCreatedOn());
        dto.setCreatedAt(u.getCreatedAt());
        dto.setCreatedByUserName(u.getCreatedByUserName());
        dto.setChangedOn(u.getChangedOn());
        dto.setChangedAt(u.getChangedAt());
        dto.setChangedByUserName(u.getChangedByUserName());
        return dto;
    }

    private User findOrThrow(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new LmsException("User " + id + " not found.", HttpStatus.NOT_FOUND));
    }

    private boolean emailTaken(String email, UUID ownId) {
        return userRepository.findAll().stream()
                .anyMatch(u -> u.getEmail() != null
                        && u.getEmail().trim().equalsIgnoreCase(email)
                        && !Objects.equals(u.getId(), ownId));
    }

    /**
     * Finds the UserRole for the requested code. When several rows share the code, the requested
     * description decides; otherwise the first row is used.
     */
    private UserRole resolveRole(UserRequestDto request, Map<String, List<UserRole>> roles) {
        String code = request.getRole() == null ? null : request.getRole().trim();
        List<UserRole> candidates = isBlank(code) ? null : roles.get(code);
        if (candidates == null || candidates.isEmpty()) {
            throw new LmsException("Role " + code + " does not exist.", HttpStatus.PRECONDITION_FAILED);
        }
        if (!isBlank(request.getRoleDescription())) {
            for (UserRole candidate : candidates) {
                if (request.getRoleDescription().trim().equalsIgnoreCase(nullToEmpty(candidate.getValue()).trim())) {
                    return candidate;
                }
            }
        }
        return candidates.get(0);
    }

    private void validateRiskDepartment(String code, Map<String, Department> departments) {
        if (!isBlank(code) && !departments.containsKey(code.trim())) {
            throw new LmsException("Risk department " + code + " does not exist.", HttpStatus.PRECONDITION_FAILED);
        }
    }

    private void checkNotSelf(User user, String username, String action) {
        if (user.getEmail() != null && user.getEmail().equalsIgnoreCase(username)) {
            throw new LmsException("You cannot " + action + " your own user.", HttpStatus.PRECONDITION_FAILED);
        }
    }

    private Map<String, List<UserRole>> rolesByCode() {
        return userRoleRepository.findAll().stream()
                .filter(r -> !isBlank(r.getCode()))
                .collect(Collectors.groupingBy(r -> r.getCode().trim(), LinkedHashMap::new, Collectors.toList()));
    }

    private Map<String, Department> departmentsByCode() {
        return departmentRepository.findAll().stream()
                .filter(d -> !isBlank(d.getCode()))
                .collect(Collectors.toMap(d -> d.getCode().trim(), Function.identity(), (a, b) -> a, LinkedHashMap::new));
    }

    private static String firstRoleValue(String code, Map<String, List<UserRole>> roles) {
        List<UserRole> list = code == null ? null : roles.get(code);
        return list == null || list.isEmpty() ? null : list.get(0).getValue();
    }

    private static String roleIssue(UserRole r, Map<String, Long> codeCounts) {
        String code = nullToEmpty(r.getCode()).trim();
        if (code.isEmpty()) {
            return "Code missing";
        }
        if (!ROLE_CODE.matcher(code).matches() && ROLE_CODE.matcher(nullToEmpty(r.getValue()).trim()).matches()) {
            return "Code and description swapped";
        }
        if (codeCounts.getOrDefault(code, 0L) > 1) {
            return "Duplicate code";
        }
        return null;
    }

    private static void stampChange(User user, String username) {
        user.setChangedOn(LocalDate.now());
        user.setChangedAt(LocalTime.now());
        user.setChangedByUserName(username);
    }

    private static boolean containsIgnoreCase(String value, String lowerCaseQuery) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(lowerCaseQuery);
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }
}
