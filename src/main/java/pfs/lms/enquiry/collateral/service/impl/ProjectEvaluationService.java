package pfs.lms.enquiry.collateral.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pfs.lms.enquiry.collateral.domain.CollateralChecklist;
import pfs.lms.enquiry.collateral.domain.CollateralAttributesValueList;
import pfs.lms.enquiry.collateral.domain.CollateralValueList;
import pfs.lms.enquiry.collateral.domain.ProjectEvaluation;
import pfs.lms.enquiry.collateral.dto.CollateralChecklistDto;
import pfs.lms.enquiry.collateral.dto.ProjectEvaluationDto;
import pfs.lms.enquiry.collateral.repository.CollateralAttributesValueListRepository;
import pfs.lms.enquiry.collateral.repository.CollateralChecklistRepository;
import pfs.lms.enquiry.collateral.repository.ProjectEvaluationRepository;
import pfs.lms.enquiry.collateral.service.ICollateralService;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.exception.LmsException;
import pfs.lms.enquiry.repository.LoanApplicationRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Project (collateral) evaluations of a loan — SAP table ZLMS_COLL_EVAL, tab "Project(Coll.) Valuation" of the loan
 * contract — and the loan's Major Collateral Type.
 * <p>
 * <b>Approval.</b> The evaluations belong to the loan's collateral checklist and are approved with it: every change
 * writes a change document and an SAP integration pointer whose main entity is the checklist (created here if the loan
 * has none yet, exactly as the first collateral does), so the collateral SAP integration posts them once the checklist
 * is Approved, and nothing can be changed while the checklist waits for approval.
 * <p>
 * <b>Line item numbers</b> run 1, 2, 3 … per loan (SAP ITEM_NO). <b>Major Collateral Type</b> is one value for the
 * loan; SAP keeps it on every row, so it is written to every evaluation of the loan and a change rewrites them all.
 */
@Service
@Transactional
public class ProjectEvaluationService {

    /** Sub-business-process of the change documents and SAP pointers (routes to the entity set ProjectEvaluationSet). */
    public static final String SUB_PROCESS = "Project Evaluation";
    static final String SOURCE_OF_ENTRY_PORTAL = "PORTAL";

    /** Largest amounts SAP takes: CURR 15,2 (13 digits before the point) and CURR 17,2 (15 digits). */
    private static final BigDecimal MAX_CURR_15 = new BigDecimal("9999999999999.99");
    private static final BigDecimal MAX_CURR_17 = new BigDecimal("999999999999999.99");

    private final ProjectEvaluationRepository evaluationRepository;
    private final CollateralChecklistRepository checklistRepository;
    private final LoanApplicationRepository loanApplicationRepository;
    private final CollateralAttributesValueListRepository valueRepository;
    private final CollateralChangeDocumentService changeDocumentService;
    private final ICollateralService collateralService;
    private final String companyCode;

    public ProjectEvaluationService(ProjectEvaluationRepository evaluationRepository,
                                    CollateralChecklistRepository checklistRepository,
                                    LoanApplicationRepository loanApplicationRepository,
                                    CollateralAttributesValueListRepository valueRepository,
                                    CollateralChangeDocumentService changeDocumentService,
                                    ICollateralService collateralService,
                                    @Value("${collateral.company-code:PFS}") String companyCode) {
        this.evaluationRepository = evaluationRepository;
        this.checklistRepository = checklistRepository;
        this.loanApplicationRepository = loanApplicationRepository;
        this.valueRepository = valueRepository;
        this.changeDocumentService = changeDocumentService;
        this.collateralService = collateralService;
        this.companyCode = companyCode;
    }

    // ------------------------------------------------------------------------------------------------ reading

    /** The Project(Coll.) Valuation screen of a loan. */
    @Transactional(readOnly = true)
    public ProjectEvaluationDto get(UUID loanApplicationId) {
        LoanApplication loan = findLoan(loanApplicationId);
        ProjectEvaluationDto dto = new ProjectEvaluationDto();
        dto.setLoanApplicationId(loan.getId());
        dto.setLoanContractId(loan.getLoanContractId());

        CollateralChecklistDto checklist = collateralService.getChecklist(loanApplicationId);
        dto.setLoan(checklist.getLoan());
        dto.setChecklistId(checklist.getId());
        dto.setChecklistWorkFlowStatusCode(checklist.getWorkFlowStatusCode());
        dto.setChecklistWorkFlowStatusDescription(checklist.getWorkFlowStatusDescription());
        dto.setChangeable(checklist.getWorkFlowStatusCode() == null
                || checklist.getWorkFlowStatusCode() != CollateralChecklist.STATUS_SENT_FOR_APPROVAL);

        List<ProjectEvaluation> evaluations = evaluations(loanApplicationId);
        dto.setEvaluations(evaluations);
        dto.setMajorCollateralType(majorCollateralTypeOf(evaluations));
        return dto;
    }

    // ------------------------------------------------------------------------------------------------ writing

    /** New Valuation: the next line item number of the loan, the loan's Major Collateral Type, keys from the loan. */
    public ProjectEvaluation create(UUID loanApplicationId, ProjectEvaluation request, String userName) {
        LoanApplication loan = findLoan(loanApplicationId);
        CollateralChecklist checklist = checklistFor(loan, userName);
        CollateralService.checkNotInApproval(checklist);

        List<ProjectEvaluation> existing = evaluations(loanApplicationId);
        ProjectEvaluation evaluation = new ProjectEvaluation();
        copyBusinessFields(request, evaluation);
        validate(evaluation);
        evaluation.setId(UUID.randomUUID());
        evaluation.setLoanApplication(loan);
        evaluation.setCompanyCode(companyCode);
        evaluation.setContractNumber(loan.getLoanContractId());
        evaluation.setItemNumber(nextItemNumber(existing));
        evaluation.setMajorCollateralType(majorCollateralTypeOf(existing));
        evaluation.setSourceOfEntry(SOURCE_OF_ENTRY_PORTAL);
        stampCreated(evaluation, userName);
        evaluation = evaluationRepository.save(evaluation);

        changeDocumentService.record(loan, checklist.getId(), evaluation.getId(), describe(evaluation), null,
                snapshot(evaluation), CollateralChangeDocumentService.CREATED, userName, SUB_PROCESS);
        return evaluation;
    }

    /** Change Valuation: the fields a user maintains; an unchanged save writes nothing. */
    public ProjectEvaluation update(UUID evaluationId, ProjectEvaluation request, String userName) {
        ProjectEvaluation evaluation = findEvaluation(evaluationId);
        LoanApplication loan = evaluation.getLoanApplication();
        CollateralChecklist checklist = checklistFor(loan, userName);
        CollateralService.checkNotInApproval(checklist);

        ProjectEvaluation before = snapshot(evaluation);
        ProjectEvaluation changed = snapshot(evaluation);
        copyBusinessFields(request, changed);
        validate(changed);
        if (sameBusinessFields(before, changed)) {
            return evaluation;
        }
        copyBusinessFields(changed, evaluation);
        stampChanged(evaluation, userName);
        evaluation = evaluationRepository.save(evaluation);

        changeDocumentService.record(loan, checklist.getId(), evaluation.getId(), describe(evaluation), before,
                snapshot(evaluation), CollateralChangeDocumentService.UPDATED, userName, SUB_PROCESS);
        return evaluation;
    }

    /** Delete Valuation. The line item number is not given to a later evaluation. */
    public void delete(UUID evaluationId, String userName) {
        ProjectEvaluation evaluation = findEvaluation(evaluationId);
        LoanApplication loan = evaluation.getLoanApplication();
        CollateralChecklist checklist = checklistFor(loan, userName);
        CollateralService.checkNotInApproval(checklist);

        ProjectEvaluation deleted = snapshot(evaluation);
        evaluationRepository.delete(evaluation);
        changeDocumentService.record(loan, checklist.getId(), deleted.getId(), describe(deleted), null, deleted,
                CollateralChangeDocumentService.DELETED, userName, SUB_PROCESS);
    }

    /**
     * Sets (or clears, with null / blank) the Major Collateral Type of the loan. SAP keeps the type on every row of
     * ZLMS_COLL_EVAL, so every evaluation of the loan is rewritten and reposted; without evaluations the type is kept
     * on the first evaluation that is created later.
     */
    public ProjectEvaluationDto setMajorCollateralType(UUID loanApplicationId, String majorCollateralType, String userName) {
        LoanApplication loan = findLoan(loanApplicationId);
        String code = blankToNull(majorCollateralType);
        if (code != null) {
            checkInList(CollateralValueList.MAJOR_COLLATERAL_TYPE, code, "Major Collateral Type");
        }
        List<ProjectEvaluation> evaluations = evaluations(loanApplicationId);
        if (!evaluations.isEmpty() && !Objects.equals(majorCollateralTypeOf(evaluations), code)) {
            CollateralChecklist checklist = checklistFor(loan, userName);
            CollateralService.checkNotInApproval(checklist);
            for (ProjectEvaluation evaluation : evaluations) {
                if (Objects.equals(evaluation.getMajorCollateralType(), code)) {
                    continue;
                }
                ProjectEvaluation before = snapshot(evaluation);
                evaluation.setMajorCollateralType(code);
                stampChanged(evaluation, userName);
                evaluationRepository.save(evaluation);
                changeDocumentService.record(loan, checklist.getId(), evaluation.getId(), describe(evaluation), before,
                        snapshot(evaluation), CollateralChangeDocumentService.UPDATED, userName, SUB_PROCESS);
            }
        } else if (evaluations.isEmpty() && code != null) {
            throw invalid("Create a valuation first; the Major Collateral Type is kept with the valuations.");
        }
        return get(loanApplicationId);
    }

    // ------------------------------------------------------------------------------------------------ helpers

    private List<ProjectEvaluation> evaluations(UUID loanApplicationId) {
        return evaluationRepository.findByLoanApplication_IdOrderByItemNumberAsc(loanApplicationId);
    }

    /** The loan's Major Collateral Type: the one on its evaluations (the latest row wins when they differ). */
    static String majorCollateralTypeOf(List<ProjectEvaluation> evaluations) {
        String type = null;
        for (ProjectEvaluation evaluation : evaluations) {
            if (evaluation.getMajorCollateralType() != null) {
                type = evaluation.getMajorCollateralType();
            }
        }
        return type;
    }

    /** One more than the highest line item number in use. */
    static int nextItemNumber(List<ProjectEvaluation> existing) {
        return existing.stream().map(ProjectEvaluation::getItemNumber).filter(Objects::nonNull)
                .max(Integer::compare).orElse(0) + 1;
    }

    /**
     * The collateral checklist of the loan — the main entity the evaluations are approved and posted with. Created
     * when the loan has none yet, in the same way the first collateral creates it.
     */
    CollateralChecklist checklistFor(LoanApplication loan, String userName) {
        CollateralChecklist checklist = checklistRepository.findByLoanApplication_Id(loan.getId()).orElse(null);
        if (checklist != null) {
            return checklist;
        }
        checklist = new CollateralChecklist();
        checklist.setLoanApplication(loan);
        checklist.setLoanContractId(loan.getLoanContractId());
        checklist.setWorkFlowStatusCode(CollateralChecklist.STATUS_NOT_SENT_FOR_APPROVAL);
        checklist.setWorkFlowStatusDescription(CollateralChecklist.STATUS_NOT_SENT_FOR_APPROVAL_TEXT);
        stampCreated(checklist, userName);
        checklist = checklistRepository.save(checklist);
        changeDocumentService.record(loan, checklist.getId(), checklist.getId(),
                "Collateral checklist of loan " + loan.getLoanContractId(), null, checklist,
                CollateralChangeDocumentService.CREATED, userName, CollateralChangeDocumentService.SUB_PROCESS_CHECKLIST);
        return checklist;
    }

    /** Checks and tidies the fields a user enters; throws 412 with a message the UI shows. */
    void validate(ProjectEvaluation v) {
        v.setValuationBy(blankToNull(v.getValuationBy()));
        v.setCurrency(v.getCurrency() == null || v.getCurrency().trim().isEmpty() ? null : v.getCurrency().trim().toUpperCase());
        v.setRemarks(blankToNull(v.getRemarks()));
        if (v.getValuationBy() == null) {
            throw invalid("Enter who made the valuation (Valuation By).");
        }
        checkInList(CollateralValueList.VALUATION_BY, v.getValuationBy(), "Valuation By");
        if (v.getValuationDate() == null) {
            throw invalid("Enter the Valuation Date.");
        }
        if (v.getValuationDate().isAfter(LocalDate.now())) {
            throw invalid("The Valuation Date cannot be in the future.");
        }
        if (v.getCurrency() == null) {
            throw invalid("Enter the Currency.");
        }
        if (v.getCurrency().length() > 5) {
            throw invalid("The Currency can have at most 5 characters.");
        }
        checkAmount(v.getTotalLoanAsset(), MAX_CURR_15, "Total Loan Asset");
        checkAmount(v.getTotalCurrentLiabilities(), MAX_CURR_17, "Total Current Liabilities");
        checkAmount(v.getNetValuationAmount(), MAX_CURR_17, "Net (Valuation Amount)");
        if (v.getRemarks() != null && v.getRemarks().length() > 60) {
            throw invalid("Remarks can have at most 60 characters.");
        }
    }

    private static void checkAmount(BigDecimal amount, BigDecimal max, String label) {
        if (amount == null) {
            return;
        }
        if (amount.stripTrailingZeros().scale() > 2) {
            throw invalid(label + " can have at most 2 decimals.");
        }
        if (amount.abs().compareTo(max) > 0) {
            throw invalid(label + " is too large for SAP (at most " + max.toPlainString() + ").");
        }
    }

    /** A code must be one of its value list, when the list has values (an empty list is not checked). */
    private void checkInList(CollateralValueList list, String code, String label) {
        Set<String> codes = valueRepository.findByListName(list.name()).stream()
                .map(CollateralAttributesValueList::getCode).collect(Collectors.toSet());
        if (!codes.isEmpty() && !codes.contains(code)) {
            throw invalid(label + " '" + code + "' is not one of its values.");
        }
    }

    /** The fields a user maintains (the keys, the type and the SAP fields are set by the portal). */
    static void copyBusinessFields(ProjectEvaluation from, ProjectEvaluation to) {
        to.setValuationBy(from.getValuationBy());
        to.setValuationDate(from.getValuationDate());
        to.setTotalLoanAsset(from.getTotalLoanAsset());
        to.setTotalCurrentLiabilities(from.getTotalCurrentLiabilities());
        to.setNetValuationAmount(from.getNetValuationAmount());
        to.setCurrency(from.getCurrency());
        to.setRemarks(from.getRemarks());
    }

    private static boolean sameBusinessFields(ProjectEvaluation a, ProjectEvaluation b) {
        return Objects.equals(a.getValuationBy(), b.getValuationBy())
                && Objects.equals(a.getValuationDate(), b.getValuationDate())
                && sameAmount(a.getTotalLoanAsset(), b.getTotalLoanAsset())
                && sameAmount(a.getTotalCurrentLiabilities(), b.getTotalCurrentLiabilities())
                && sameAmount(a.getNetValuationAmount(), b.getNetValuationAmount())
                && Objects.equals(a.getCurrency(), b.getCurrency())
                && Objects.equals(a.getRemarks(), b.getRemarks());
    }

    private static boolean sameAmount(BigDecimal a, BigDecimal b) {
        return a == null ? b == null : b != null && a.compareTo(b) == 0;
    }

    /** A detached copy with every SAP field, for the change documents. */
    static ProjectEvaluation snapshot(ProjectEvaluation v) {
        ProjectEvaluation copy = new ProjectEvaluation();
        copy.setId(v.getId());
        copy.setLoanApplication(v.getLoanApplication());
        copyBusinessFields(v, copy);
        copy.setCompanyCode(v.getCompanyCode());
        copy.setArchivingCategory(v.getArchivingCategory());
        copy.setContractNumber(v.getContractNumber());
        copy.setItemNumber(v.getItemNumber());
        copy.setMajorCollateralType(v.getMajorCollateralType());
        copy.setSanctionStagePortalId(v.getSanctionStagePortalId());
        copy.setSapId(v.getSapId());
        copy.setSourceOfEntry(v.getSourceOfEntry());
        return copy;
    }

    private static String describe(ProjectEvaluation v) {
        return "Project valuation " + v.getItemNumber() + " of loan " + v.getContractNumber()
                + (v.getValuationDate() != null ? " (" + v.getValuationDate() + ")" : "");
    }

    private static void stampCreated(pfs.lms.enquiry.domain.AggregateRoot<?> entity, String userName) {
        entity.setCreatedOn(LocalDate.now());
        entity.setCreatedAt(LocalTime.now());
        entity.setCreatedByUserName(userName);
    }

    private static void stampChanged(pfs.lms.enquiry.domain.AggregateRoot<?> entity, String userName) {
        entity.setChangedOn(LocalDate.now());
        entity.setChangedAt(LocalTime.now());
        entity.setChangedByUserName(userName);
    }

    private LoanApplication findLoan(UUID loanApplicationId) {
        return loanApplicationRepository.findById(loanApplicationId)
                .orElseThrow(() -> new LmsException("Loan application not found.", HttpStatus.NOT_FOUND));
    }

    private ProjectEvaluation findEvaluation(UUID evaluationId) {
        return evaluationRepository.findById(evaluationId)
                .orElseThrow(() -> new LmsException("Project valuation not found.", HttpStatus.NOT_FOUND));
    }

    private static String blankToNull(String value) {
        return value == null || value.trim().isEmpty() ? null : value.trim();
    }

    private static LmsException invalid(String message) {
        return new LmsException(message, HttpStatus.PRECONDITION_FAILED);
    }
}
