package pfs.lms.enquiry.reports;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import pfs.lms.enquiry.exception.LmsException;
import pfs.lms.enquiry.reports.variant.ReportVariant;
import pfs.lms.enquiry.reports.variant.ReportVariantRepository;
import pfs.lms.enquiry.reports.variant.ReportVariantService;

import java.util.List;
import java.util.Properties;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** Report variants: save / overwrite, sharing, default, delete rights, on an in-memory database. */
@DisplayName("Report variants")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReportVariantServiceTest {

    @Configuration
    @EnableTransactionManagement
    @EnableJpaRepositories(basePackageClasses = ReportVariantRepository.class)
    static class Config {
        @Bean
        DriverManagerDataSource dataSource() {
            DriverManagerDataSource ds = new DriverManagerDataSource("jdbc:h2:mem:variants;MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1", "sa", "");
            ds.setDriverClassName("org.h2.Driver");
            return ds;
        }

        @Bean
        LocalContainerEntityManagerFactoryBean entityManagerFactory(DriverManagerDataSource ds) {
            LocalContainerEntityManagerFactoryBean f = new LocalContainerEntityManagerFactoryBean();
            f.setDataSource(ds);
            f.setPackagesToScan(ReportVariant.class.getPackage().getName());
            f.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
            Properties p = new Properties();
            p.put("hibernate.hbm2ddl.auto", "create");
            p.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
            p.put("hibernate.physical_naming_strategy", SpringPhysicalNamingStrategy.class.getName());
            p.put("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class.getName());
            f.setJpaProperties(p);
            return f;
        }

        @Bean
        PlatformTransactionManager transactionManager(javax.persistence.EntityManagerFactory emf) {
            return new JpaTransactionManager(emf);
        }

        @Bean
        ObjectMapper objectMapper() {
            return new ObjectMapper();
        }

        @Bean
        ReportVariantService service(ReportVariantRepository repository, ObjectMapper mapper) {
            return new ReportVariantService(repository, mapper);
        }
    }

    private AnnotationConfigApplicationContext context;
    private ReportVariantService service;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeAll
    void start() {
        context = new AnnotationConfigApplicationContext(Config.class);
        service = context.getBean(ReportVariantService.class);
    }

    @AfterAll
    void stop() {
        context.close();
    }

    private JsonNode params(String json) throws Exception {
        return mapper.readTree(json);
    }

    @Test
    void saveListOverwriteAndDefault() throws Exception {
        service.save("kyc", "anna", "Active loans", "status 60", false, true, params("{\"status\":[{\"low\":\"60\"}]}"));
        service.save("kyc", "anna", "Q3", null, false, true, params("{\"a\":1}"));           // becomes the default
        List<ReportVariant> mine = service.list("kyc", "anna");
        assertThat(mine).extracting(ReportVariant::getName).containsExactly("Active loans", "Q3");
        assertThat(mine).filteredOn(ReportVariant::isDefaultVariant).extracting(ReportVariant::getName).containsExactly("Q3");

        service.save("kyc", "anna", "Q3", "changed", false, false, params("{\"a\":2}"));      // same name: overwritten
        mine = service.list("kyc", "anna");
        assertThat(mine).hasSize(2);
        ReportVariant q3 = mine.stream().filter(v -> v.getName().equals("Q3")).findFirst().get();
        assertThat(q3.getDescription()).isEqualTo("changed");
        assertThat(service.parameters(q3).get("a").asInt()).isEqualTo(2);
        assertThat(mine).noneMatch(ReportVariant::isDefaultVariant);
    }

    @Test
    void sharedVariantsAreSeenByEveryoneButChangedOnlyByTheOwner() throws Exception {
        ReportVariant shared = service.save("icc-approval", "anna", "FY 2026", null, true, false, params("{\"fiscalYear\":\"2026\"}"));
        service.save("icc-approval", "anna", "Private", null, false, false, params("{}"));
        assertThat(service.list("icc-approval", "ben")).extracting(ReportVariant::getName).containsExactly("FY 2026");
        assertThat(service.list("workflow-status", "ben")).isEmpty();

        assertThatThrownBy(() -> service.delete("icc-approval", "ben", shared.getId()))
                .isInstanceOf(LmsException.class).hasMessageContaining("owner");
        // ben saving the same name creates his own variant, anna's stays untouched
        service.save("icc-approval", "ben", "FY 2026", null, false, false, params("{\"fiscalYear\":\"2025\"}"));
        assertThat(service.parameters(service.list("icc-approval", "anna").stream()
                .filter(v -> v.getOwnerName().equals("anna") && v.getName().equals("FY 2026")).findFirst().get()).get("fiscalYear").asText()).isEqualTo("2026");

        service.delete("icc-approval", "anna", shared.getId());
        assertThat(service.list("icc-approval", "ben")).extracting(ReportVariant::getOwnerName).containsExactly("ben");
    }

    @Test
    void validation() throws Exception {
        assertThatThrownBy(() -> service.save("kyc", "anna", " ", null, false, false, params("{}"))).isInstanceOf(LmsException.class);
        assertThatThrownBy(() -> service.save("kyc", "anna", new String(new char[41]).replace('\0', 'x'), null, false, false, params("{}")))
                .isInstanceOf(LmsException.class);
        assertThatThrownBy(() -> service.save("kyc", "anna", "ok", null, false, false, params("[1]"))).isInstanceOf(LmsException.class);
        assertThatThrownBy(() -> service.save("nope", "anna", "ok", null, false, false, params("{}"))).isInstanceOf(LmsException.class);
        assertThatThrownBy(() -> service.delete("kyc", "anna", UUID.randomUUID())).isInstanceOf(LmsException.class);
    }

    @Test
    void columnLayoutsAreStoredPerReportLikeVariants() throws Exception {
        service.save("kyc_layout", "anna", "Slim", null, false, true, params("{\"hidden\":[\"a\",\"b\"]}"));
        service.save("kyc", "anna", "Slim", null, false, false, params("{\"x\":1}"));
        assertThat(service.list("kyc_layout", "anna")).hasSize(1);
        assertThat(service.parameters(service.list("kyc_layout", "anna").get(0)).get("hidden")).hasSize(2);
        assertThat(service.list("kyc_layout", "ben")).isEmpty();
        assertThat(service.list("kyc", "anna")).extracting(ReportVariant::getReport).doesNotContain("kyc_layout");
        assertThatThrownBy(() -> service.save("nope_layout", "anna", "ok", null, false, false, params("{}"))).isInstanceOf(LmsException.class);
        assertThatThrownBy(() -> service.list("_layout", "anna")).isInstanceOf(LmsException.class);
    }
}
