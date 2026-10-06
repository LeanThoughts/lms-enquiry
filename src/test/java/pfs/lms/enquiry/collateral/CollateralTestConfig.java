package pfs.lms.enquiry.collateral;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import pfs.lms.enquiry.businesspartner.repository.BupaRoleEntityFieldStatusRepository;
import pfs.lms.enquiry.businesspartner.repository.BupaRoleEntitySetFieldStatusRepository;
import pfs.lms.enquiry.businesspartner.repository.BusinessPartnerRoleTypeRepository;
import pfs.lms.enquiry.businesspartner.repository.DocumentTypeRepository;
import pfs.lms.enquiry.businesspartner.repository.IdentificationCategoryRepository;
import pfs.lms.enquiry.collateral.config.CollateralValueInitializer;
import pfs.lms.enquiry.collateral.migration.CollateralMigrationService;
import pfs.lms.enquiry.collateral.repository.CollateralChecklistRepository;
import pfs.lms.enquiry.collateral.repository.CollateralCoverageRepository;
import pfs.lms.enquiry.collateral.repository.CollateralDocumentRepository;
import pfs.lms.enquiry.collateral.repository.CollateralItemRepository;
import pfs.lms.enquiry.collateral.repository.CollateralNumberRangeRepository;
import pfs.lms.enquiry.collateral.repository.CollateralPerfectionCersaiRepository;
import pfs.lms.enquiry.collateral.repository.CollateralPerfectionNeslRepository;
import pfs.lms.enquiry.collateral.repository.CollateralPerfectionRocRepository;
import pfs.lms.enquiry.collateral.repository.CollateralSecuritiesPositionRepository;
import pfs.lms.enquiry.collateral.repository.CollateralValueRepository;
import pfs.lms.enquiry.collateral.service.impl.CollateralAgreementTypeMapping;
import pfs.lms.enquiry.collateral.service.impl.CollateralAuthorizationService;
import pfs.lms.enquiry.collateral.service.impl.CollateralChangeDocumentService;
import pfs.lms.enquiry.collateral.service.impl.CollateralChildService;
import pfs.lms.enquiry.collateral.service.impl.CollateralNumberRangeService;
import pfs.lms.enquiry.collateral.service.impl.CollateralPartnerService;
import pfs.lms.enquiry.collateral.service.impl.CollateralService;
import pfs.lms.enquiry.collateral.workflow.CollateralWorkflowService;
import pfs.lms.enquiry.configuration.bptables.BpTableService;
import pfs.lms.enquiry.configuration.fieldstatus.FieldStatusConfigurationService;
import pfs.lms.enquiry.domain.ChangeDocument;
import pfs.lms.enquiry.repository.ChangeDocumentRepository;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.repository.PartnerRepository;
import pfs.lms.enquiry.repository.UnitOfMeasureRepository;
import pfs.lms.enquiry.repository.UserRepository;
import pfs.lms.enquiry.repository.UserRoleRepository;
import pfs.lms.enquiry.repository.WorkflowApproverRepository;
import pfs.lms.enquiry.service.ISAPIntegrationPointerService;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

/**
 * Spring configuration of the Collateral Management tests: the collateral services with their real repositories on an
 * in-memory H2 database (MySQL mode) and an in-memory Activiti engine. The rest of the application (security, SAP,
 * mail of other modules) is not started. Change documents are stored in the H2 database; SAP integration pointers are
 * recorded in {@link CollateralTestSupport#POINTERS}.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackageClasses = {CollateralItemRepository.class, LoanApplicationRepository.class, DocumentTypeRepository.class},
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                CollateralItemRepository.class, CollateralChecklistRepository.class, CollateralValueRepository.class,
                CollateralCoverageRepository.class, CollateralPerfectionRocRepository.class,
                CollateralPerfectionCersaiRepository.class, CollateralPerfectionNeslRepository.class,
                CollateralDocumentRepository.class, CollateralSecuritiesPositionRepository.class,
                CollateralNumberRangeRepository.class, LoanApplicationRepository.class, PartnerRepository.class,
                DocumentTypeRepository.class, UnitOfMeasureRepository.class, UserRoleRepository.class,
                UserRepository.class, ChangeDocumentRepository.class, BusinessPartnerRoleTypeRepository.class,
                WorkflowApproverRepository.class, BupaRoleEntityFieldStatusRepository.class,
                BupaRoleEntitySetFieldStatusRepository.class, IdentificationCategoryRepository.class}))
@Import({CollateralService.class, CollateralChildService.class, CollateralPartnerService.class,
        CollateralAuthorizationService.class, CollateralAgreementTypeMapping.class, CollateralChangeDocumentService.class,
        CollateralValueInitializer.class, CollateralNumberRangeService.class, CollateralWorkflowService.class,
        CollateralMigrationService.class, FieldStatusConfigurationService.class, BpTableService.class})
public class CollateralTestConfig {

    /** Entities the tests need (the collateral entities and the shared ones they refer to). */
    static final String[] ENTITIES = {
            "pfs.lms.enquiry.collateral.domain.CollateralChecklist",
            "pfs.lms.enquiry.collateral.domain.CollateralItem",
            "pfs.lms.enquiry.collateral.domain.CollateralValue",
            "pfs.lms.enquiry.collateral.domain.CollateralNumberRange",
            "pfs.lms.enquiry.collateral.domain.CollateralCoverage",
            "pfs.lms.enquiry.collateral.domain.CollateralPerfectionRoc",
            "pfs.lms.enquiry.collateral.domain.CollateralPerfectionCersai",
            "pfs.lms.enquiry.collateral.domain.CollateralPerfectionNesl",
            "pfs.lms.enquiry.collateral.domain.CollateralDocument",
            "pfs.lms.enquiry.collateral.domain.CollateralSecuritiesPosition",
            "pfs.lms.enquiry.domain.LoanApplication",
            "pfs.lms.enquiry.domain.ChangeDocument",
            "pfs.lms.enquiry.domain.ChangeDocumentItem",
            "pfs.lms.enquiry.domain.Partner",
            "pfs.lms.enquiry.domain.PartnerRoleType",
            "pfs.lms.enquiry.domain.PartnerContact",
            "pfs.lms.enquiry.domain.UnitOfMeasure",
            "pfs.lms.enquiry.domain.UserRole",
            "pfs.lms.enquiry.domain.User",
            "pfs.lms.enquiry.domain.EnquiryNo",
            "pfs.lms.enquiry.domain.WorkflowApprover",
            "pfs.lms.enquiry.businesspartner.domain.DocumentType",
            "pfs.lms.enquiry.businesspartner.domain.BusinessPartnerRoleType",
            "pfs.lms.enquiry.businesspartner.domain.BupaRoleEntityFieldStatus",
            "pfs.lms.enquiry.businesspartner.domain.BupaRoleEntitySetFieldStatus",
            "pfs.lms.enquiry.businesspartner.domain.IdentificationCategory",
            "pfs.lms.enquiry.businesspartner.domain.AmendmentReason",
            "pfs.lms.enquiry.businesspartner.domain.BupaRoleCustomerFieldValues",
            "pfs.lms.enquiry.businesspartner.domain.BusinessPartnerRoleTypePartnerGroup",
            "pfs.lms.enquiry.businesspartner.domain.BusinessPartnerType",
            "pfs.lms.enquiry.businesspartner.domain.CountryCode",
            "pfs.lms.enquiry.businesspartner.domain.CreditRatingAgency",
            "pfs.lms.enquiry.businesspartner.domain.CreditRatingCode",
            "pfs.lms.enquiry.businesspartner.domain.DunningProcedure",
            "pfs.lms.enquiry.businesspartner.domain.HouseBank",
            "pfs.lms.enquiry.businesspartner.domain.IndustrySystem",
            "pfs.lms.enquiry.businesspartner.domain.IndustryType",
            "pfs.lms.enquiry.businesspartner.domain.LegalEntity",
            "pfs.lms.enquiry.businesspartner.domain.LegalForm",
            "pfs.lms.enquiry.businesspartner.domain.PartnerGroup",
            "pfs.lms.enquiry.businesspartner.domain.PaymentMethod",
            "pfs.lms.enquiry.businesspartner.domain.PaymentTerms",
            "pfs.lms.enquiry.businesspartner.domain.PlanningGroup",
            "pfs.lms.enquiry.businesspartner.domain.SanctionAuthority",
            "pfs.lms.enquiry.businesspartner.domain.SortKey",
            "pfs.lms.enquiry.businesspartner.domain.Title"
    };

    @Bean
    DataSource dataSource(@Value("${collateral.test.db}") String database) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(
                "jdbc:h2:mem:" + database + ";MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1;NON_KEYWORDS=VALUE,KEY,USER",
                "sa", "");
        dataSource.setDriverClassName("org.h2.Driver");
        return dataSource;
    }

    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factory.setPackagesToScan("pfs.lms.enquiry.collateral.none");
        factory.setPersistenceUnitPostProcessors(unit -> Arrays.stream(ENTITIES).forEach(unit::addManagedClassName));
        Properties properties = new Properties();
        properties.put("hibernate.hbm2ddl.auto", "create");
        properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        properties.put("hibernate.physical_naming_strategy", SpringPhysicalNamingStrategy.class.getName());
        properties.put("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class.getName());
        factory.setJpaProperties(properties);
        return factory;
    }

    @Bean
    PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    com.fasterxml.jackson.databind.ObjectMapper objectMapper() {
        return new com.fasterxml.jackson.databind.ObjectMapper()
                .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule())
                .disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /** In-memory workflow engine with the portal's one-level approval process; mails go to the test SMTP server. */
    @Bean(destroyMethod = "close")
    ProcessEngine processEngine(@Value("${collateral.test.db}") String database,
                                @Value("${collateral.test.smtp-port}") int smtpPort) {
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration();
        configuration.setJdbcUrl("jdbc:h2:mem:act_" + database + ";DB_CLOSE_DELAY=-1");
        configuration.setMailServerHost("127.0.0.1");
        configuration.setMailServerPort(smtpPort);
        configuration.setMailServerDefaultFrom("portal@test.local");
        ProcessEngine engine = configuration.buildProcessEngine();
        engine.getRepositoryService().createDeployment()
                .addClasspathResource("processes/LoansOneLevelApproval.bpmn20.xml")
                .deploy();
        return engine;
    }

    /** Stores change documents like the portal's change document service does. */
    @Bean
    IChangeDocumentService changeDocumentService(ChangeDocumentRepository repository) {
        return (IChangeDocumentService) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class[]{IChangeDocumentService.class}, (proxy, method, args) -> {
                    switch (method.getName()) {
                        case "saveChangeDocument":
                            ChangeDocument document = (ChangeDocument) args[0];
                            document.setCreatedAt(new Date());
                            document.setUpdatedAt(new Date());
                            return repository.save(document);
                        case "toString":
                            return "test change document service";
                        case "hashCode":
                            return 1;
                        case "equals":
                            return proxy == args[0];
                        default:
                            throw new UnsupportedOperationException(method.getName());
                    }
                });
    }

    /** Records the SAP integration pointers (business process / sub process / mode). */
    @Bean
    ISAPIntegrationPointerService sapIntegrationPointerService() {
        return (ISAPIntegrationPointerService) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class[]{ISAPIntegrationPointerService.class}, (proxy, method, args) -> {
                    switch (method.getName()) {
                        case "saveForObject":
                            CollateralTestSupport.POINTERS.add(args[0] + "/" + args[1] + "/" + args[4]);
                            return null;
                        case "toString":
                            return "test SAP pointer service";
                        case "hashCode":
                            return 2;
                        case "equals":
                            return proxy == args[0];
                        default:
                            throw new UnsupportedOperationException(method.getName());
                    }
                });
    }
}
