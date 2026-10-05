package pfs.lms.enquiry.usermanagement.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.repository.UserRepository;
import pfs.lms.enquiry.usermanagement.service.impl.UserManagementService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Back-fills the validity period of existing active users at application start:
 * start date = current date, end date = 31.12.9999 (same open end date used for new users).
 * <p>
 * Only empty dates are filled, so the runner is idempotent: users that already have a start
 * or end date keep it, and nothing changes on later restarts. Inactive users are not touched.
 */
@Component
public class UserValidityDateInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(UserValidityDateInitializer.class);

    private final UserRepository userRepository;

    public UserValidityDateInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        LocalDate today = LocalDate.now();

        List<User> toUpdate = userRepository.findAll().stream()
                .filter(User::isStatus)
                .filter(user -> user.getStartDate() == null || user.getEndDate() == null)
                .collect(Collectors.toList());

        for (User user : toUpdate) {
            if (user.getStartDate() == null) {
                user.setStartDate(today);
            }
            if (user.getEndDate() == null) {
                user.setEndDate(UserManagementService.OPEN_END_DATE);
            }
        }

        if (!toUpdate.isEmpty()) {
            userRepository.saveAll(toUpdate);
        }
        log.info("User Management: validity dates set for {} active user(s) ({} to {})",
                toUpdate.size(), today, UserManagementService.OPEN_END_DATE);
    }
}
