package pfs.lms.enquiry.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Aggregates shown on the homepage dashboard
 */
@Getter
@Setter
public class DashboardResource {

    private List<StageCount> stages = new ArrayList<>();
    private SapSyncStatus sapSync = new SapSyncStatus();
    private List<PortfolioSlice> portfolioByProjectType = new ArrayList<>();
    private List<PortfolioSlice> portfolioByState = new ArrayList<>();
    private long recentEnquiryCount;
    private List<RecentEnquiry> recentEnquiries = new ArrayList<>();

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StageCount {
        private Integer functionalStatus;
        private String description;
        private long count;
        private double amount;
    }

    /**
     * Posting status of loan applications taken up for processing (technical status 4)
     */
    @Getter
    @Setter
    public static class SapSyncStatus {
        private long waiting;       // 0 - Not Posted, 4 - Approved but Posting Pending
        private long inProgress;    // 1 - Attempted to Post
        private long errors;        // 2 - Errors
        private long posted;        // 3 - Posted Successfully
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PortfolioSlice {
        private String code;
        private String description;
        private long count;
        private double amount;
    }

    @Getter
    @Setter
    public static class RecentEnquiry {
        private UUID loanApplicationId;
        private Long enquiryNo;
        private LocalDate loanEnquiryDate;
        private String projectName;
        private String loanContractId;
        private String borrowerName;
        private Integer functionalStatus;
        private String functionalStatusDescription;
    }
}
