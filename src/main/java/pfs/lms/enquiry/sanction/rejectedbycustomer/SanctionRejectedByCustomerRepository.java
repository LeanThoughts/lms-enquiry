package pfs.lms.enquiry.sanction.rejectedbycustomer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SanctionRejectedByCustomerRepository extends JpaRepository<SanctionRejectedByCustomer, UUID> {

    List<SanctionRejectedByCustomer> findBySanctionId(UUID sanctionId);
}
