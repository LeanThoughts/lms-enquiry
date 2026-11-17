package pfs.lms.enquiry.configuration.bptables;

import pfs.lms.enquiry.authorization.domain.AuthorizationAccess;
import pfs.lms.enquiry.authorization.domain.AuthorizationObject;
import pfs.lms.enquiry.domain.AssistanceType;
import pfs.lms.enquiry.domain.ConditionType;
import pfs.lms.enquiry.domain.Country;
import pfs.lms.enquiry.domain.CustomerRejectionReason;
import pfs.lms.enquiry.domain.Department;
import pfs.lms.enquiry.domain.DocumentationStatus;
import pfs.lms.enquiry.domain.DocumentationType;
import pfs.lms.enquiry.domain.EnquiryPortalCommonConfig;
import pfs.lms.enquiry.domain.ExternalRatingType;
import pfs.lms.enquiry.domain.FeeType;
import pfs.lms.enquiry.domain.FinancingType;
import pfs.lms.enquiry.domain.FunctionalStatus;
import pfs.lms.enquiry.domain.ICCReadinessStatus;
import pfs.lms.enquiry.domain.ICCStatus;
import pfs.lms.enquiry.domain.IndustrySector;
import pfs.lms.enquiry.domain.InterestCalculationMethod;
import pfs.lms.enquiry.domain.LoanClass;
import pfs.lms.enquiry.domain.LoanType;
import pfs.lms.enquiry.domain.PaymentForm;
import pfs.lms.enquiry.domain.PresentedInICC;
import pfs.lms.enquiry.domain.Product;
import pfs.lms.enquiry.domain.ProjectType;
import pfs.lms.enquiry.domain.ProjectTypeCoreSector;
import pfs.lms.enquiry.domain.ProposalType;
import pfs.lms.enquiry.domain.PurposeOfLoan;
import pfs.lms.enquiry.domain.ReferenceInterestSign;
import pfs.lms.enquiry.domain.Region;
import pfs.lms.enquiry.domain.RejectionCategory;
import pfs.lms.enquiry.domain.SanctionType;
import pfs.lms.enquiry.domain.State;
import pfs.lms.enquiry.domain.TRAAccountType;
import pfs.lms.enquiry.domain.UnitOfMeasure;
import pfs.lms.enquiry.domain.UserRole;

import java.util.ArrayList;
import java.util.List;

import static pfs.lms.enquiry.configuration.bptables.BpTableField.bool;
import static pfs.lms.enquiry.configuration.bptables.BpTableField.generatedId;
import static pfs.lms.enquiry.configuration.bptables.BpTableField.id;
import static pfs.lms.enquiry.configuration.bptables.BpTableField.nextNumberId;
import static pfs.lms.enquiry.configuration.bptables.BpTableField.number;
import static pfs.lms.enquiry.configuration.bptables.BpTableField.text;

/**
 * The loan configuration tables that the CommandLineRunner configs in pfs.lms.enquiry.configInitializer fill at
 * startup, one app each in the Loans module of the Configuration workspace (route /configuration/loan-tables/{key}).
 * They are maintained with the same REST API as the business partner tables (/configuration/bp-tables/{key}). The
 * configs only insert rows that are missing, so rows changed here keep their values. (Users are maintained in User
 * Management.)
 */
final class LoanTableDefinitions {

    private LoanTableDefinitions() {
    }

    static List<BpTableDefinition> tables() {
        List<BpTableDefinition> tables = new ArrayList<>();
        // Loan classification
        tables.add(codeValue("loan-classes", "Loan Classes", "Classes (sectors) of loans", LoanClass.class, "Loan Class"));
        tables.add(codeValue("loan-types", "Loan Types", "Term loan, non fund based, corporate loan", LoanType.class, "Loan Type"));
        tables.add(new BpTableDefinition("products", "Products", "Loan products (SAP product type)",
                "ProductConfigInitializer", Product.class, "code", "name",
                generatedId(), text("code", "Product").key().max(10), text("name", "Description").required()));
        tables.add(codeValue("project-types", "Project Types", "Types of projects financed", ProjectType.class, "Project Type"));
        tables.add(codeValue("project-type-core-sectors", "Project Type Core Sectors", "Infrastructure or non-infrastructure",
                ProjectTypeCoreSector.class, "Core Sector"));
        tables.add(codeValue("industry-sectors", "Industry Sectors", "Industry sectors of the projects", IndustrySector.class, "Industry Sector"));
        tables.add(codeValue("purposes-of-loan", "Purposes of Loan", "Purposes of the loans (term loan, refinancing, LOC, …)",
                PurposeOfLoan.class, "Purpose"));
        tables.add(codeValue("financing-types", "Financing Types", "Sole lending, multiple banking, consortium", FinancingType.class, "Financing Type"));
        tables.add(codeValue("assistance-types", "Assistance Types", "Debt, equity, NCD, CCD, sub debt", AssistanceType.class, "Assistance Type"));
        tables.add(codeValue("proposal-types", "Proposal Types", "Lead, sole, consortium", ProposalType.class, "Proposal Type"));
        tables.add(codeValue("units-of-measure", "Units of Measure", "Units of the project capacity (MW, KM, …)", UnitOfMeasure.class, "Unit"));
        // ICC, sanction & rejection
        tables.add(codeValue("icc-readiness-statuses", "ICC Readiness Statuses", "Readiness of a loan for the ICC", ICCReadinessStatus.class, "Status"));
        tables.add(codeValue("presented-in-icc", "Presented in ICC", "Whether a loan was presented in the ICC", PresentedInICC.class, "Value"));
        tables.add(codeValue("icc-statuses", "ICC Statuses", "Outcome of the ICC meeting", ICCStatus.class, "Status"));
        tables.add(codeValue("sanction-types", "Sanction Types", "Original sanction and its amendments", SanctionType.class, "Sanction Type"));
        tables.add(codeValue("rejection-categories", "Rejection Categories", "Who rejected an enquiry", RejectionCategory.class, "Category"));
        tables.add(codeValue("customer-rejection-reasons", "Customer Rejection Reasons", "Reasons for the rejection by the customer",
                CustomerRejectionReason.class, "Reason"));
        tables.add(new BpTableDefinition("functional-statuses", "Functional Statuses", "Functional stages of a loan (enquiry to monitoring)",
                "FunctionalStatusConfigInitializer", FunctionalStatus.class, "code", "value",
                generatedId(), number("code", "Status").key(), text("value", "Description").required()));
        // Fees, interest & accounts
        tables.add(codeValue("fee-types", "Fee Types", "Fees charged to the borrower", FeeType.class, "Fee Type"));
        tables.add(codeValue("tra-account-types", "TRA Account Types", "Accounts of the trust and retention account", TRAAccountType.class, "Account Type"));
        tables.add(codeValue("external-rating-types", "External Rating Types", "Types of external ratings",
                "ExternalRatingTypeConfigInitializer", ExternalRatingType.class, "Rating Type"));
        tables.add(codeDescription("payment-forms", "Payment Forms", "Payment forms of interest and repayment", PaymentForm.class, "Payment Form"));
        tables.add(codeDescription("condition-types", "Condition Types", "Condition types of the loan conditions", ConditionType.class, "Condition Type"));
        tables.add(codeDescription("interest-calculation-methods", "Interest Calculation Methods", "Day count methods",
                InterestCalculationMethod.class, "Method"));
        tables.add(codeDescription("reference-interest-signs", "Reference Interest Signs", "Signs of the spread on the reference interest rate",
                ReferenceInterestSign.class, "Sign"));
        // Documentation
        tables.add(codeValue("documentation-types", "Documentation Types", "Types of loan documentation", DocumentationType.class, "Type"));
        tables.add(codeValue("documentation-statuses", "Documentation Statuses", "Statuses of the loan documentation", DocumentationStatus.class, "Status"));
        // Organization & geography
        tables.add(codeValue("departments", "Departments", "Departments of PFS", "DepartmentConfigInitializer", Department.class, "Department"));
        tables.add(new BpTableDefinition("countries", "Countries", "Countries of projects and addresses",
                "CountryConfig", Country.class, "countryCode", "value",
                id("countryCode", "Country", 3), text("value", "Name").required()));
        tables.add(new BpTableDefinition("regions", "Regions", "Regions (states) per country",
                "RegionConfig1, RegionConfig2", Region.class, "regionCode", "value",
                nextNumberId(), text("countryCode", "Country").key().ref("countries"),
                text("regionCode", "Region").key().max(3), text("value", "Name").required()));
        tables.add(new BpTableDefinition("states", "States", "States of India", "Initializer", State.class, "code", "name",
                generatedId(), text("code", "State").key().max(10), text("name", "Name").required()));
        // Users & authorizations
        tables.add(codeValue("user-roles", "User Roles", "Roles of the portal users", "UserRoleConfigInitializer", UserRole.class, "Role"));
        tables.add(new BpTableDefinition("authorization-objects", "Authorization Objects", "Activities that need an authorization",
                "AuthorizationInitializer", AuthorizationObject.class, "authorizationObject", "businessProcessName",
                generatedId(), text("authorizationObject", "Authorization Object").key().max(100),
                text("businessProcessName", "Business Process").required().max(100)));
        tables.add(new BpTableDefinition("authorization-access", "Authorization Access", "Authorization objects allowed per user role",
                "AuthorizationAccessInitializer", AuthorizationAccess.class, "authorizationObject", "userRoleCode",
                generatedId(), text("authorizationObject", "Authorization Object").key().ref("authorization-objects"),
                text("userRoleCode", "User Role").key().max(20), text("userRoleName", "Role Name").max(100),
                bool("accessAllowed", "Access Allowed")));
        // System
        tables.add(new BpTableDefinition("portal-common-config", "Portal Common Configuration",
                "BD team e-mail address per SAP system", "Initializer", EnquiryPortalCommonConfig.class, "systemId", "bdTeamEmail",
                generatedId(), text("systemId", "SAP System").key().max(10), text("bdTeamEmail", "BD Team E-mail").required()));
        return tables;
    }

    /** Table with code and value, filled by Initializer */
    private static BpTableDefinition codeValue(String key, String title, String description, Class<?> entity, String codeLabel) {
        return codeValue(key, title, description, "Initializer", entity, codeLabel);
    }

    private static BpTableDefinition codeValue(String key, String title, String description, String configClass,
                                               Class<?> entity, String codeLabel) {
        return new BpTableDefinition(key, title, description, configClass, entity, "code", "value",
                generatedId(), text("code", codeLabel).key().max(20), text("value", "Description").required());
    }

    /** Table with code and description, filled by InterestRateConfigInitializer */
    private static BpTableDefinition codeDescription(String key, String title, String description, Class<?> entity, String codeLabel) {
        return new BpTableDefinition(key, title, description, "InterestRateConfigInitializer", entity, "code", "description",
                generatedId(), text("code", codeLabel).key().max(20), text("description", "Description").required());
    }
}
