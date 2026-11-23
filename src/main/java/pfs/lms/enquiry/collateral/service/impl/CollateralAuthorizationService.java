package pfs.lms.enquiry.collateral.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.collateral.dto.CollateralAccessDto;
import pfs.lms.enquiry.collateral.service.ICollateralAuthorizationService;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.exception.LmsException;
import pfs.lms.enquiry.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Write access uses the {@code LoanContracts.Collaterals.Edit} Entra permission when present in the security
 * context; otherwise the roles in {@code collateral.write-roles} (default ZLM023, ZLM018, ZLM035) apply.
 * {@code collateral.authorization.enabled=false} gives every user write access (local testing).
 */
@Service
public class CollateralAuthorizationService implements ICollateralAuthorizationService {

    private static final Logger log = LoggerFactory.getLogger(CollateralAuthorizationService.class);
    private static final String EDIT_AUTHORITY = "LoanContracts.Collaterals.Edit";

    private final UserRepository userRepository;
    private final boolean enabled;
    private final List<String> writeRoles;

    public CollateralAuthorizationService(UserRepository userRepository,
                                          @Value("${collateral.authorization.enabled:true}") boolean enabled,
                                          @Value("${collateral.write-roles:ZLM023,ZLM018,ZLM035}") String writeRoles) {
        this.userRepository = userRepository;
        this.enabled = enabled;
        this.writeRoles = Arrays.stream(writeRoles.split(","))
                .map(String::trim)
                .filter(role -> !role.isEmpty())
                .collect(Collectors.toList());
    }

    @Override
    public CollateralAccessDto getAccess(String principalName) {
        User user = principalName == null ? null : userRepository.findByEmail(principalName);
        String role = user != null ? user.getRole() : null;
        boolean canWrite = !enabled || hasEditAuthority()
                || (user != null && user.isStatus() && role != null && writeRoles.contains(role.trim()));
        return new CollateralAccessDto(canWrite, principalName, role, writeRoles);
    }

    @Override
    public void checkWriteAccess(String principalName) {
        if (!getAccess(principalName).isCanWrite()) {
            log.warn("Collateral Management write access denied for {}", principalName);
            throw new LmsException("You are not authorised to create, change or delete collaterals (display only).",
                    HttpStatus.FORBIDDEN);
        }
    }

    private static boolean hasEditAuthority() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (EDIT_AUTHORITY.equals(authority.getAuthority())) {
                return true;
            }
        }
        return false;
    }
}
