package pfs.lms.enquiry.collateral;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import pfs.lms.enquiry.collateral.domain.CollateralChecklist;
import pfs.lms.enquiry.collateral.domain.ProjectEvaluation;
import pfs.lms.enquiry.collateral.dto.ProjectEvaluationDto;
import pfs.lms.enquiry.collateral.repository.CollateralAttributesValueListRepository;
import pfs.lms.enquiry.collateral.repository.CollateralChecklistRepository;
import pfs.lms.enquiry.collateral.service.impl.ProjectEvaluationService;
import pfs.lms.enquiry.exception.LmsException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** Project (collateral) evaluations of a loan (SAP ZLMS_COLL_EVAL), approved with the loan's collateral checklist. */
@DisplayName("Project evaluation")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProjectEvaluationServiceTest extends CollateralTestSupport {

    private ProjectEvaluationService service;
    private UUID loanId;
    private UUID firstId;

    @BeforeAll
    void setUp() {
        service = bean(ProjectEvaluationService.class);
        loanId = loan("0000020000231");
        // VALUATION_BY and MAJOR_COLLATERAL_TYPE are seeded from CollateralValueList by the value initializer
        assertThat(bean(CollateralAttributesValueListRepository.class).findByListName("VALUATION_BY")).hasSize(2);
        assertThat(bean(CollateralAttributesValueListRepository.class).findByListName("MAJOR_COLLATERAL_TYPE")).hasSize(3);
    }

    private static ProjectEvaluation evaluation(String by, String date, String asset, String liabilities, String net,
                                                String currency, String remarks) {
        ProjectEvaluation v = new ProjectEvaluation();
        v.setValuationBy(by);
        v.setValuationDate(date == null ? null : LocalDate.parse(date));
        v.setTotalLoanAsset(asset == null ? null : new BigDecimal(asset));
        v.setTotalCurrentLiabilities(liabilities == null ? null : new BigDecimal(liabilities));
        v.setNetValuationAmount(net == null ? null : new BigDecimal(net));
        v.setCurrency(currency);
        v.setRemarks(remarks);
        return v;
    }

    @Test
    @Order(1)
    @DisplayName("the first evaluation creates the loan's collateral checklist; evaluations get line items 1, 2 … and the loan's keys")
    void create() {
        POINTERS.clear();
        assertThat(bean(CollateralChecklistRepository.class).findByLoanApplication_Id(loanId)).isEmpty();

        ProjectEvaluation first = service.create(loanId,
                evaluation("1", "2005-01-01", "100000", "200000", "121212", "inr", " Remarks "), WRITER);
        ProjectEvaluation second = service.create(loanId,
                evaluation("2", "2003-01-01", "100000", "200000", "633", "INR", null), WRITER);
        firstId = first.getId();

        CollateralChecklist checklist = bean(CollateralChecklistRepository.class).findByLoanApplication_Id(loanId).orElseThrow();
        assertThat(checklist.getWorkFlowStatusCode()).isEqualTo(CollateralChecklist.STATUS_NOT_SENT_FOR_APPROVAL);
        assertThat(first.getItemNumber()).isEqualTo(1);
        assertThat(second.getItemNumber()).isEqualTo(2);
        assertThat(first.getContractNumber()).isEqualTo("0000020000231");
        assertThat(first.getCompanyCode()).isEqualTo("PFS");
        assertThat(first.getSourceOfEntry()).isEqualTo("PORTAL");
        assertThat(first.getMajorCollateralType()).isNull();
        assertThat(first.getCurrency()).isEqualTo("INR");
        assertThat(first.getRemarks()).isEqualTo("Remarks");
        assertThat(first.getCreatedByUserName()).isEqualTo(WRITER);
        // the checklist's own pointer, then one C per evaluation, all keyed to the checklist
        assertThat(POINTERS).containsExactly("Collateral Management/Collateral Checklist/C",
                "Collateral Management/Project Evaluation/C", "Collateral Management/Project Evaluation/C");

        ProjectEvaluationDto dto = service.get(loanId);
        assertThat(dto.getChecklistId()).isEqualTo(checklist.getId());
        assertThat(dto.isChangeable()).isTrue();
        assertThat(dto.getLoan().getLoanContractId()).isEqualTo("0000020000231");
        assertThat(dto.getEvaluations().stream().map(ProjectEvaluation::getItemNumber).collect(Collectors.toList()))
                .containsExactly(1, 2);
    }

    @Test
    @Order(2)
    @DisplayName("Valuation By, a past Valuation Date and the Currency are required; amounts have 2 decimals")
    void validation() {
        assertThatThrownBy(() -> service.create(loanId, evaluation(null, "2005-01-01", null, null, null, "INR", null), WRITER))
                .isInstanceOf(LmsException.class).hasMessageContaining("Valuation By");
        assertThatThrownBy(() -> service.create(loanId, evaluation("9", "2005-01-01", null, null, null, "INR", null), WRITER))
                .isInstanceOf(LmsException.class).hasMessageContaining("not one of its values");
        assertThatThrownBy(() -> service.create(loanId, evaluation("1", null, null, null, null, "INR", null), WRITER))
                .isInstanceOf(LmsException.class).hasMessageContaining("Valuation Date");
        assertThatThrownBy(() -> service.create(loanId, evaluation("1", LocalDate.now().plusDays(1).toString(), null, null, null, "INR", null), WRITER))
                .isInstanceOf(LmsException.class).hasMessageContaining("future");
        assertThatThrownBy(() -> service.create(loanId, evaluation("1", "2005-01-01", null, null, null, " ", null), WRITER))
                .isInstanceOf(LmsException.class).hasMessageContaining("Currency");
        assertThatThrownBy(() -> service.create(loanId, evaluation("1", "2005-01-01", "1.234", null, null, "INR", null), WRITER))
                .isInstanceOf(LmsException.class).hasMessageContaining("2 decimals");
        assertThatThrownBy(() -> service.create(loanId, evaluation("1", "2005-01-01", "99999999999999", null, null, "INR", null), WRITER))
                .isInstanceOf(LmsException.class).hasMessageContaining("too large");
        assertThat(service.get(loanId).getEvaluations()).hasSize(2);
    }

    @Test
    @Order(3)
    @DisplayName("a change writes a U pointer; an unchanged save writes nothing")
    void update() {
        POINTERS.clear();
        ProjectEvaluation changed = service.update(firstId,
                evaluation("2", "2005-01-01", "100000", "200000", "150000", "INR", "Remarks"), WRITER);
        assertThat(changed.getNetValuationAmount()).isEqualByComparingTo("150000");
        assertThat(changed.getValuationBy()).isEqualTo("2");
        assertThat(changed.getItemNumber()).isEqualTo(1);
        assertThat(changed.getChangedByUserName()).isEqualTo(WRITER);
        assertThat(POINTERS).containsExactly("Collateral Management/Project Evaluation/U");

        POINTERS.clear();
        service.update(firstId, evaluation("2", "2005-01-01", "100000.00", "200000", "150000", "inr", "Remarks"), WRITER);
        assertThat(POINTERS).isEmpty();
    }

    @Test
    @Order(4)
    @DisplayName("the Major Collateral Type is written to every evaluation of the loan, and a new one inherits it")
    void majorCollateralType() {
        POINTERS.clear();
        ProjectEvaluationDto dto = service.setMajorCollateralType(loanId, "2", WRITER);
        assertThat(dto.getMajorCollateralType()).isEqualTo("2");
        assertThat(dto.getEvaluations()).extracting(ProjectEvaluation::getMajorCollateralType).containsOnly("2");
        assertThat(POINTERS).containsExactly("Collateral Management/Project Evaluation/U",
                "Collateral Management/Project Evaluation/U");

        POINTERS.clear();
        service.setMajorCollateralType(loanId, "2", WRITER);
        assertThat(POINTERS).isEmpty();

        assertThatThrownBy(() -> service.setMajorCollateralType(loanId, "7", WRITER))
                .isInstanceOf(LmsException.class).hasMessageContaining("Major Collateral Type");

        ProjectEvaluation third = service.create(loanId, evaluation("1", "2024-03-31", null, null, "5", "INR", null), WRITER);
        assertThat(third.getMajorCollateralType()).isEqualTo("2");
        assertThat(third.getItemNumber()).isEqualTo(3);
    }

    @Test
    @Order(5)
    @DisplayName("while the checklist waits for approval nothing can be created, changed or deleted")
    void lockedInApproval() {
        setChecklistStatus(CollateralChecklist.STATUS_SENT_FOR_APPROVAL);
        try {
            assertThat(service.get(loanId).isChangeable()).isFalse();
            assertThatThrownBy(() -> service.create(loanId, evaluation("1", "2024-03-31", null, null, "5", "INR", null), WRITER))
                    .isInstanceOf(LmsException.class).hasMessageContaining("waiting for approval");
            assertThatThrownBy(() -> service.update(firstId, evaluation("1", "2005-01-01", null, null, "1", "INR", null), WRITER))
                    .isInstanceOf(LmsException.class).hasMessageContaining("waiting for approval");
            assertThatThrownBy(() -> service.delete(firstId, WRITER))
                    .isInstanceOf(LmsException.class).hasMessageContaining("waiting for approval");
            assertThatThrownBy(() -> service.setMajorCollateralType(loanId, "1", WRITER))
                    .isInstanceOf(LmsException.class).hasMessageContaining("waiting for approval");
        } finally {
            setChecklistStatus(CollateralChecklist.STATUS_APPROVED);
        }
    }

    private void setChecklistStatus(int status) {
        tx.execute(tx -> {
            CollateralChecklistRepository checklists = bean(CollateralChecklistRepository.class);
            CollateralChecklist checklist = checklists.findByLoanApplication_Id(loanId).orElseThrow();
            checklist.setWorkFlowStatusCode(status);
            return checklists.save(checklist);
        });
    }

    @Test
    @Order(6)
    @DisplayName("a loan with project valuations but no collateral can be sent for approval; the approval locks it")
    void sendForApproval() {
        UUID otherLoan = loan("0000020000777");
        service.create(otherLoan, evaluation("1", "2024-03-31", null, null, "9", "INR", null), WRITER);
        UUID checklistId = bean(CollateralChecklistRepository.class).findByLoanApplication_Id(otherLoan).orElseThrow().getId();

        pfs.lms.enquiry.domain.WorkflowApprover approver = new pfs.lms.enquiry.domain.WorkflowApprover();
        approver.setProcessName("CollateralManagement");
        approver.setApproverName("Head Credit");
        approver.setApproverEmail(APPROVER);
        bean(pfs.lms.enquiry.repository.WorkflowApproverRepository.class).save(approver);

        pfs.lms.enquiry.collateral.workflow.ICollateralWorkflowService workflow =
                bean(pfs.lms.enquiry.collateral.workflow.ICollateralWorkflowService.class);
        assertThat(workflow.startWorkflowProcessInstance(checklistId, WRITER).getWorkFlowStatusCode()).isEqualTo(2);
        assertThat(service.get(otherLoan).isChangeable()).isFalse();
        assertThatThrownBy(() -> service.create(otherLoan, evaluation("1", "2024-03-31", null, null, "5", "INR", null), WRITER))
                .isInstanceOf(LmsException.class).hasMessageContaining("waiting for approval");
    }

    @Test
    @Order(7)
    @DisplayName("delete writes a D pointer; the next evaluation continues the numbering")
    void delete() {
        POINTERS.clear();
        UUID thirdId = service.get(loanId).getEvaluations().get(2).getId();
        service.delete(thirdId, WRITER);
        assertThat(POINTERS).containsExactly("Collateral Management/Project Evaluation/D");
        assertThat(service.get(loanId).getEvaluations()).hasSize(2);

        ProjectEvaluation fourth = service.create(loanId, evaluation("1", "2024-06-30", null, null, "6", "INR", null), WRITER);
        assertThat(fourth.getItemNumber()).isEqualTo(3);
        assertThatThrownBy(() -> service.delete(thirdId, WRITER)).isInstanceOf(LmsException.class).hasMessageContaining("not found");
        assertThatThrownBy(() -> service.get(UUID.randomUUID())).isInstanceOf(LmsException.class).hasMessageContaining("Loan application");
    }
}
