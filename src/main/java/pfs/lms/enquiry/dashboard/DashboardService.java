package pfs.lms.enquiry.dashboard;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.domain.ProjectType;
import pfs.lms.enquiry.repository.PartnerRepository;
import pfs.lms.enquiry.repository.ProjectTypeRepository;
import pfs.lms.enquiry.service.ILoanApplicationService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private static final int RECENT_ENQUIRY_DAYS = 30;
    private static final int RECENT_ENQUIRY_LIMIT = 8;
    private static final int PORTFOLIO_SLICE_LIMIT = 8;

    private final ILoanApplicationService loanApplicationService;
    private final PartnerRepository partnerRepository;
    private final ProjectTypeRepository projectTypeRepository;

    /**
     * Build the dashboard from the loan applications visible to the logged in user. As in the loan contract search, loan
     * applications without a loan applicant are left out so that the counts match the search results.
     */
    public DashboardResource getDashboard(HttpServletRequest request) {
        List<LoanApplication> loanApplications = loanApplicationService.searchLoans(request, Pageable.unpaged()).stream()
                .filter(loanApplication -> loanApplication.getLoanApplicant() != null)
                .collect(Collectors.toList());

        DashboardResource dashboard = new DashboardResource();
        dashboard.setStages(getStageCounts(loanApplications));
        dashboard.setSapSync(getSapSyncStatus(loanApplications));
        dashboard.setPortfolioByProjectType(getPortfolioByProjectType(loanApplications));
        dashboard.setPortfolioByState(getPortfolio(loanApplications, LoanApplication::getProjectLocationState, code -> code));
        setRecentEnquiries(dashboard, loanApplications);
        return dashboard;
    }

    private List<DashboardResource.StageCount> getStageCounts(List<LoanApplication> loanApplications) {
        return loanApplications.stream()
                .filter(loanApplication -> loanApplication.getFunctionalStatus() != null)
                .collect(Collectors.groupingBy(LoanApplication::getFunctionalStatus))
                .entrySet().stream()
                .map(entry -> new DashboardResource.StageCount(
                        entry.getKey(),
                        entry.getValue().stream()
                                .map(LoanApplication::getFunctionalStatusDescription)
                                .filter(Objects::nonNull)
                                .findFirst().orElse(null),
                        entry.getValue().size(),
                        sumAmount(entry.getValue())))
                .collect(Collectors.toList());
    }

    private DashboardResource.SapSyncStatus getSapSyncStatus(List<LoanApplication> loanApplications) {
        DashboardResource.SapSyncStatus sapSync = new DashboardResource.SapSyncStatus();
        loanApplications.stream()
                .filter(loanApplication -> Integer.valueOf(4).equals(loanApplication.getTechnicalStatus()))
                .forEach(loanApplication -> {
                    Integer postedInSAP = loanApplication.getPostedInSAP();
                    if (postedInSAP == null || postedInSAP == 0 || postedInSAP == 4) {
                        sapSync.setWaiting(sapSync.getWaiting() + 1);
                    } else if (postedInSAP == 1) {
                        sapSync.setInProgress(sapSync.getInProgress() + 1);
                    } else if (postedInSAP == 2) {
                        sapSync.setErrors(sapSync.getErrors() + 1);
                    } else if (postedInSAP == 3) {
                        sapSync.setPosted(sapSync.getPosted() + 1);
                    }
                });
        return sapSync;
    }

    private List<DashboardResource.PortfolioSlice> getPortfolioByProjectType(List<LoanApplication> loanApplications) {
        Map<String, String> projectTypes = projectTypeRepository.findAll().stream()
                .filter(projectType -> projectType.getCode() != null)
                .collect(Collectors.toMap(ProjectType::getCode, ProjectType::getValue, (first, second) -> first));
        return getPortfolio(loanApplications, LoanApplication::getProjectType, code -> projectTypes.getOrDefault(code, code));
    }

    /**
     * Group the loan applications by a key and return the largest groups by amount
     */
    private List<DashboardResource.PortfolioSlice> getPortfolio(List<LoanApplication> loanApplications,
                                                               Function<LoanApplication, String> keyFunction,
                                                               Function<String, String> descriptionFunction) {
        return loanApplications.stream()
                .filter(loanApplication -> keyFunction.apply(loanApplication) != null && !keyFunction.apply(loanApplication).isEmpty())
                .collect(Collectors.groupingBy(keyFunction))
                .entrySet().stream()
                .map(entry -> new DashboardResource.PortfolioSlice(entry.getKey(), descriptionFunction.apply(entry.getKey()),
                        entry.getValue().size(), sumAmount(entry.getValue())))
                .sorted(Comparator.comparingDouble(DashboardResource.PortfolioSlice::getAmount).reversed()
                        .thenComparing(Comparator.comparingLong(DashboardResource.PortfolioSlice::getCount).reversed()))
                .limit(PORTFOLIO_SLICE_LIMIT)
                .collect(Collectors.toList());
    }

    private void setRecentEnquiries(DashboardResource dashboard, List<LoanApplication> loanApplications) {
        LocalDate since = LocalDate.now().minusDays(RECENT_ENQUIRY_DAYS);
        List<LoanApplication> recent = loanApplications.stream()
                .filter(loanApplication -> loanApplication.getLoanEnquiryDate() != null && !loanApplication.getLoanEnquiryDate().isBefore(since))
                .sorted(Comparator.comparing(LoanApplication::getLoanEnquiryDate).reversed())
                .collect(Collectors.toList());

        dashboard.setRecentEnquiryCount(recent.size());
        dashboard.setRecentEnquiries(recent.stream()
                .limit(RECENT_ENQUIRY_LIMIT)
                .map(this::toRecentEnquiry)
                .collect(Collectors.toList()));
    }

    private DashboardResource.RecentEnquiry toRecentEnquiry(LoanApplication loanApplication) {
        DashboardResource.RecentEnquiry recentEnquiry = new DashboardResource.RecentEnquiry();
        recentEnquiry.setLoanApplicationId(loanApplication.getId());
        recentEnquiry.setEnquiryNo(loanApplication.getEnquiryNo() != null ? loanApplication.getEnquiryNo().getId() : null);
        recentEnquiry.setLoanEnquiryDate(loanApplication.getLoanEnquiryDate());
        recentEnquiry.setProjectName(loanApplication.getProjectName());
        recentEnquiry.setLoanContractId(loanApplication.getLoanContractId());
        recentEnquiry.setFunctionalStatus(loanApplication.getFunctionalStatus());
        recentEnquiry.setFunctionalStatusDescription(loanApplication.getFunctionalStatusDescription());
        if (loanApplication.getLoanApplicant() != null) {
            partnerRepository.findById(loanApplication.getLoanApplicant())
                    .map(Partner::getPartyName1)
                    .ifPresent(recentEnquiry::setBorrowerName);
        }
        return recentEnquiry;
    }

    private double sumAmount(List<LoanApplication> loanApplications) {
        return loanApplications.stream()
                .map(LoanApplication::getLoanContractAmount)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .sum();
    }
}
