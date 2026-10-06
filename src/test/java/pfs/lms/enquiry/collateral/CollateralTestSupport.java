package pfs.lms.enquiry.collateral;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import pfs.lms.enquiry.businesspartner.domain.DocumentType;
import pfs.lms.enquiry.businesspartner.repository.DocumentTypeRepository;
import pfs.lms.enquiry.collateral.config.CollateralValueInitializer;
import pfs.lms.enquiry.collateral.domain.CollateralItem;
import pfs.lms.enquiry.collateral.service.ICollateralChildService;
import pfs.lms.enquiry.collateral.service.ICollateralService;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.exception.LmsException;
import pfs.lms.enquiry.repository.ChangeDocumentRepository;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

/**
 * Base class of the Collateral Management tests. Each test class gets its own Spring context with a fresh in-memory
 * database (see {@link CollateralTestConfig}); the value lists and two document types are loaded once.
 * <p>
 * Users: {@link #WRITER} (role ZLM023: change access and configuration), {@link #LEGAL} (ZLM035: change access),
 * {@link #READER} (ZLM014: display only), {@link #APPROVER} (workflow approver).
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class CollateralTestSupport {

    public static final String WRITER = "writer@test.local";
    public static final String LEGAL = "legal@test.local";
    public static final String READER = "reader@test.local";
    public static final String APPROVER = "approver@test.local";

    /** SAP integration pointers written by the change documents (business process/sub process/mode). */
    public static final List<String> POINTERS = new CopyOnWriteArrayList<>();

    protected AnnotationConfigApplicationContext context;
    protected TestSmtpServer smtp;
    protected TransactionTemplate tx;
    protected ObjectMapper objectMapper;
    protected ICollateralService collateralService;
    protected ICollateralChildService childService;

    /** Properties of the test context; subclasses may add their own (e.g. the start number). */
    protected Map<String, Object> properties() {
        return new HashMap<>();
    }

    @BeforeAll
    void startContext() throws Exception {
        smtp = new TestSmtpServer();
        Map<String, Object> properties = new HashMap<>();
        properties.put("collateral.test.db", getClass().getSimpleName() + "_" + UUID.randomUUID().toString().substring(0, 8));
        properties.put("collateral.test.smtp-port", smtp.getPort());
        properties.put("spring.activiti.mail-server-user-name", "portal@test.local");
        properties.putAll(properties());

        context = new AnnotationConfigApplicationContext();
        context.getEnvironment().getPropertySources().addFirst(new MapPropertySource("collateral-test", properties));
        context.register(CollateralTestConfig.class);
        context.refresh();

        tx = new TransactionTemplate(context.getBean(PlatformTransactionManager.class));
        objectMapper = context.getBean(ObjectMapper.class);
        collateralService = context.getBean(ICollateralService.class);
        childService = context.getBean(ICollateralChildService.class);

        context.getBean(CollateralValueInitializer.class).run();
        DocumentTypeRepository documentTypes = context.getBean(DocumentTypeRepository.class);
        documentTypes.save(new DocumentType(null, "ZPFSLM101", "Legal Counsel Report", "BUS2049"));
        documentTypes.save(new DocumentType(null, "ZPFSLM104", "List of Loan Documents executed with Borrower and others", "BUS2049"));

        user(WRITER, "ZLM023");
        user(LEGAL, "ZLM035");
        user(READER, "ZLM014");
    }

    @AfterAll
    void stopContext() throws Exception {
        if (context != null) {
            context.close();
        }
        if (smtp != null) {
            smtp.close();
        }
    }

    // ---------------------------------------------------------------------------------------------- test data

    protected <T> T bean(Class<T> type) {
        return context.getBean(type);
    }

    /** A loan application with the given loan contract number. */
    protected UUID loan(String loanContractId) {
        return tx.execute(status -> {
            LoanApplication loan = new LoanApplication();
            loan.setId(UUID.randomUUID());
            loan.setLoanContractId(loanContractId);
            loan.setProjectName("Solar Park " + loanContractId);
            loan.setBusPartnerNumber("1000234");
            return bean(LoanApplicationRepository.class).save(loan).getId();
        });
    }

    protected User user(String email, String role) {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(email);
        user.setFirstName(email.substring(0, email.indexOf('@')));
        user.setLastName("Tester");
        user.setRole(role);
        user.setStatus(true);
        return bean(UserRepository.class).save(user);
    }

    /** A valid new collateral: condition group and category 04, the given collateral object type. */
    protected CollateralItem newItem(String collateralObjectType) {
        CollateralItem item = new CollateralItem();
        item.setConditionGroup("04");
        item.setConditionCategory("04");
        item.setCollateralObjectType(collateralObjectType);
        item.setConditionDescription("Collateral " + collateralObjectType);
        return item;
    }

    protected CollateralItem createItem(UUID loanId, String collateralObjectType) {
        return collateralService.createItem(loanId, newItem(collateralObjectType), WRITER);
    }

    protected JsonNode json(String text) {
        try {
            return objectMapper.readTree(text.replace('\'', '"'));
        } catch (Exception ex) {
            throw new IllegalArgumentException(text, ex);
        }
    }

    /** Asserts that the call fails with an LmsException of the given HTTP status and returns its message. */
    protected String expectError(int httpStatus, ThrowingCallable call) {
        Throwable error = catchThrowable(call);
        assertThat(error).as("expected HTTP %s", httpStatus).isInstanceOf(LmsException.class);
        assertThat(((LmsException) error).getStatus().value()).as(error.getMessage()).isEqualTo(httpStatus);
        return error.getMessage();
    }

    /** Change documents of a sub process (e.g. "Collateral Coverage"), with their action. */
    protected List<String> changeDocuments(String subProcessName) {
        return tx.execute(status -> bean(ChangeDocumentRepository.class).findAll().stream()
                .filter(document -> subProcessName.equals(document.getSubProcessName()))
                .map(document -> document.getAction() + ":" + document.getTableKey())
                .collect(Collectors.toList()));
    }

    /** Changed attributes of the change documents of a sub process ("attribute: old -> new"). */
    protected List<String> changedAttributes(String subProcessName) {
        return tx.execute(status -> bean(ChangeDocumentRepository.class).findAll().stream()
                .filter(document -> subProcessName.equals(document.getSubProcessName()))
                .filter(document -> document.getChangeDocumentItems() != null)
                .flatMap(document -> document.getChangeDocumentItems().stream())
                .map(item -> item.getAttributeName() + ": " + item.getOldValue() + " -> " + item.getNewValue())
                .collect(Collectors.toList()));
    }
}
