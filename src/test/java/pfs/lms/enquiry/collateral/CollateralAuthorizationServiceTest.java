package pfs.lms.enquiry.collateral;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pfs.lms.enquiry.collateral.service.ICollateralAuthorizationService;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/** Change access: ZLM023, ZLM018 and ZLM035 (active users); everyone else displays only. */
@DisplayName("Collateral authorization")
class CollateralAuthorizationServiceTest extends CollateralTestSupport {

    @Test
    @DisplayName("write roles may change, other roles, inactive and unknown users may only display")
    void access() {
        ICollateralAuthorizationService authorization = bean(ICollateralAuthorizationService.class);
        user("lending@test.local", "ZLM018");
        User inactive = user("old@test.local", "ZLM023");
        inactive.setStatus(false);
        bean(UserRepository.class).save(inactive);
        user("blank@test.local", "ZLM023 ");

        assertThat(authorization.getAccess(WRITER).isCanWrite()).isTrue();
        assertThat(authorization.getAccess(LEGAL).isCanWrite()).isTrue();
        assertThat(authorization.getAccess("lending@test.local").isCanWrite()).isTrue();
        assertThat(authorization.getAccess("blank@test.local").isCanWrite()).isTrue();
        assertThat(authorization.getAccess(READER).isCanWrite()).isFalse();
        assertThat(authorization.getAccess(READER).getRole()).isEqualTo("ZLM014");
        assertThat(authorization.getAccess("old@test.local").isCanWrite()).isFalse();
        assertThat(authorization.getAccess("nobody@test.local").isCanWrite()).isFalse();

        assertThatCode(() -> authorization.checkWriteAccess(WRITER)).doesNotThrowAnyException();
        expectError(403, () -> authorization.checkWriteAccess(READER));
        expectError(403, () -> authorization.checkWriteAccess("nobody@test.local"));
    }
}
