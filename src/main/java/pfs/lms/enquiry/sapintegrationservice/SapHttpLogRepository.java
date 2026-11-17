package pfs.lms.enquiry.sapintegrationservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/** The SAP HTTP log; filtered and paged through {@link JpaSpecificationExecutor}. */
@Repository
public interface SapHttpLogRepository extends JpaRepository<SapHttpLog, Long>, JpaSpecificationExecutor<SapHttpLog> {

    @Modifying
    @Query("delete from SapHttpLog l where l.requestedAt < :before")
    int deleteByRequestedAtBefore(@Param("before") LocalDateTime before);

    @Query("select distinct l.businessProcess from SapHttpLog l where l.businessProcess is not null order by l.businessProcess")
    List<String> findDistinctBusinessProcesses();

    @Query("select distinct l.serviceName from SapHttpLog l where l.serviceName is not null order by l.serviceName")
    List<String> findDistinctServiceNames();
}
