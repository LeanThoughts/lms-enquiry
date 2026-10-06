package pfs.lms.enquiry.configuration.bptables;

import pfs.lms.enquiry.businesspartner.domain.AmendmentReason;
import pfs.lms.enquiry.businesspartner.domain.BupaRoleCustomerFieldValues;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerRoleType;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerRoleTypePartnerGroup;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerType;
import pfs.lms.enquiry.businesspartner.domain.CountryCode;
import pfs.lms.enquiry.businesspartner.domain.CreditRatingAgency;
import pfs.lms.enquiry.businesspartner.domain.CreditRatingCode;
import pfs.lms.enquiry.businesspartner.domain.DocumentType;
import pfs.lms.enquiry.businesspartner.domain.DunningProcedure;
import pfs.lms.enquiry.businesspartner.domain.HouseBank;
import pfs.lms.enquiry.businesspartner.domain.IdentificationCategory;
import pfs.lms.enquiry.businesspartner.domain.IndustrySystem;
import pfs.lms.enquiry.businesspartner.domain.IndustryType;
import pfs.lms.enquiry.businesspartner.domain.LegalEntity;
import pfs.lms.enquiry.businesspartner.domain.LegalForm;
import pfs.lms.enquiry.businesspartner.domain.PartnerGroup;
import pfs.lms.enquiry.businesspartner.domain.PaymentMethod;
import pfs.lms.enquiry.businesspartner.domain.PaymentTerms;
import pfs.lms.enquiry.businesspartner.domain.PlanningGroup;
import pfs.lms.enquiry.businesspartner.domain.SanctionAuthority;
import pfs.lms.enquiry.businesspartner.domain.SortKey;
import pfs.lms.enquiry.businesspartner.domain.Title;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static pfs.lms.enquiry.configuration.bptables.BpTableField.bool;
import static pfs.lms.enquiry.configuration.bptables.BpTableField.generatedId;
import static pfs.lms.enquiry.configuration.bptables.BpTableField.id;
import static pfs.lms.enquiry.configuration.bptables.BpTableField.nextNumberId;
import static pfs.lms.enquiry.configuration.bptables.BpTableField.text;

/**
 * The business partner configuration tables that the CommandLineRunner configs in
 * pfs.lms.enquiry.businesspartner.config fill at startup, one Configuration app each. The configs only insert rows that
 * are missing, so rows changed here keep their values. (BupaRoleEntityFieldStatus, BupaRoleEntitySetFieldStatus have
 * their own apps.) The keys are also the routes of the apps: /configuration/bp-tables/{key}.
 */
public final class BpTableDefinitions {

    private static final Map<String, BpTableDefinition> TABLES = new LinkedHashMap<>();

    static {
        // Roles & partner groups
        add(new BpTableDefinition("business-partner-roles", "Business Partner Roles",
                "Business partner roles (SAP BP role) and their default partner group",
                "BusinessPartnerRoleConfig", BusinessPartnerRoleType.class, "code", "value",
                generatedId(), text("code", "Role").key().max(10), text("value", "Description").required(),
                text("partnerGroup", "Partner Group").ref("partner-groups")));
        add(new BpTableDefinition("partner-groups", "Partner Groups",
                "Business partner groupings and their number ranges",
                "PartnerGroupConfig", PartnerGroup.class, "code", "value",
                id("code", "Partner Group", 10), text("value", "Description").required(),
                bool("externalNumberRange", "External Number Range"),
                text("startingId", "From Number").max(20), text("endingId", "To Number").max(20)));
        add(new BpTableDefinition("role-partner-groups", "Role Partner Groups",
                "Partner groups allowed for each business partner role",
                "BusinessPartnerRoleTypePartnerGroupConfig", BusinessPartnerRoleTypePartnerGroup.class, "roleType", "partnerGroup",
                nextNumberId(), text("roleType", "Role").key().ref("business-partner-roles"),
                text("partnerGroup", "Partner Group").key().ref("partner-groups")));
        add(new BpTableDefinition("role-customer-field-values", "Role Customer Field Values",
                "Default customer data (reconciliation account, payment, dunning, …) per role and partner group",
                "BuapRoleCustomerFieldValuesConfig", BupaRoleCustomerFieldValues.class, "bupaRoleCode", "partnerGroup",
                nextNumberId(),
                text("bupaRoleCode", "Role").key().ref("business-partner-roles"),
                text("partnerGroup", "Partner Group").key().ref("partner-groups"),
                text("reconAccount", "Reconciliation Account").max(10),
                text("dunningProcedure", "Dunning Procedure").ref("dunning-procedures"),
                text("planningGroup", "Planning Group").ref("planning-groups"),
                text("paymentMethods", "Payment Methods").max(10),
                text("houseBank", "House Bank").ref("house-banks"),
                bool("checkDoubleInvoice", "Check Double Invoice"),
                text("paymentTerms", "Payment Terms").ref("payment-terms"),
                text("sortKey", "Sort Key").ref("sort-keys"),
                text("roleGrouping", "Role Grouping").max(10)));
        // General data
        add(new BpTableDefinition("business-partner-types", "Business Partner Types",
                "Business partner categories (person, organization, group)",
                "BusinessPartnerTypeConfig", BusinessPartnerType.class, "code", "value",
                id("code", "Type", 10), text("value", "Description").required()));
        add(new BpTableDefinition("titles", "Titles",
                "Form of address of business partners",
                "TitleConfig", Title.class, "code", "value",
                generatedId(), text("code", "Title").key().max(10), text("value", "Description").required(),
                text("partnerCategory", "Partner Category").option("1", "Person").option("2", "Organization")
                        .option("3", "Group")));
        add(new BpTableDefinition("legal-forms", "Legal Forms",
                "Legal forms of organizations",
                "LegalFormConfig", LegalForm.class, "code", "value",
                id("code", "Legal Form", 10), text("value", "Description").required()));
        add(new BpTableDefinition("legal-entities", "Legal Entities",
                "Legal entity types of business partners",
                "LegalEntityConfig", LegalEntity.class, "code", "value",
                id("code", "Legal Entity", 10), text("value", "Description").required()));
        add(new BpTableDefinition("country-codes", "Country Codes",
                "Countries of addresses and bank details",
                "CountryCodeConfig", CountryCode.class, "code", "value",
                id("code", "Country", 3), text("value", "Description").required()));
        // Identification & documents
        add(new BpTableDefinition("identification-categories", "Identification Categories",
                "Identification types (PAN, TAN, CIN, …) and their checks",
                "IdentificationCategoryConfig", IdentificationCategory.class, "code", "value",
                generatedId(), text("code", "Category").key().max(10), text("value", "Description").required(),
                bool("duplicateCheckRequired", "Duplicate Check"), bool("panNumberValidation", "PAN Validation")));
        add(new BpTableDefinition("document-types", "Document Types",
                "Types of documents uploaded for business partners and loans",
                "DocumentTypeConfig", DocumentType.class, "code", "description",
                generatedId(), text("code", "Document Type").key().max(20),
                text("description", "Description").required(), text("businessObjectId", "Business Object").max(20)));
        // Industry & rating
        add(new BpTableDefinition("industry-systems", "Industry Systems",
                "Industry classification systems",
                "IndustrySystemConfig", IndustrySystem.class, "code", "value",
                generatedId(), text("code", "Industry System").key().max(10), text("value", "Description").required()));
        add(new BpTableDefinition("industry-types", "Industry Types",
                "Industries per industry system",
                "IndustryTypeConfig", IndustryType.class, "code", "value",
                generatedId(), text("code", "Industry").key().max(10), text("value", "Description").required(),
                text("industrySystem", "Industry System").key().refEntity("industry-systems")));
        add(new BpTableDefinition("credit-rating-agencies", "Credit Rating Agencies",
                "Agencies that rate business partners",
                "CreditRatingAgencyConfig", CreditRatingAgency.class, "code", "value",
                id("code", "Agency", 10), text("value", "Description").required()));
        add(new BpTableDefinition("credit-rating-codes", "Credit Rating Codes",
                "Credit ratings",
                "CreditRatingCodeConfig", CreditRatingCode.class, "code", "value",
                id("code", "Rating", 40), text("value", "Description").required()));
        add(new BpTableDefinition("sanction-authorities", "Sanction Authorities",
                "Authorities that sanction loans",
                "SanctionAuthorityConfig", SanctionAuthority.class, "code", "value",
                id("code", "Authority", 10), text("value", "Description").required()));
        add(new BpTableDefinition("amendment-reasons", "Amendment Reasons",
                "Reasons for amendments of sanctioned loans",
                "AmendmentReasonConfig", AmendmentReason.class, "code", "value",
                id("code", "Reason", 10), text("value", "Description").required()));
        // Payment & banking
        add(new BpTableDefinition("house-banks", "House Banks",
                "Banks of the company used for payments",
                "HouseBankConfig", HouseBank.class, "houseBankId", "description",
                id("houseBankId", "House Bank", 5), text("description", "Description").required(),
                text("bankCountryKey", "Bank Country").ref("country-codes"), text("bankKey", "Bank Key").max(15),
                text("firstTelephoneNumber", "Telephone").max(30), text("taxNumber1", "Tax Number").max(20),
                text("nameOfContactPerson", "Contact Person").max(60), text("languageKey", "Language").max(2)));
        add(new BpTableDefinition("payment-methods", "Payment Methods",
                "Payment methods of customers and vendors",
                "PaymentMethodConfig", PaymentMethod.class, "id", "description",
                id("id", "Payment Method", 10), text("description", "Description").required()));
        add(new BpTableDefinition("payment-terms", "Payment Terms",
                "Terms of payment",
                "PaymentTermsConfig", PaymentTerms.class, "id", "description",
                id("id", "Payment Terms", 10), text("description", "Description").required()));
        add(new BpTableDefinition("dunning-procedures", "Dunning Procedures",
                "Dunning procedures of customers",
                "DunningProcedureConfig", DunningProcedure.class, "id", "description",
                id("id", "Dunning Procedure", 10), text("description", "Description").required()));
        add(new BpTableDefinition("planning-groups", "Planning Groups",
                "Cash management planning groups",
                "PlanningGroupConfig", PlanningGroup.class, "id", "description",
                id("id", "Planning Group", 10), text("description", "Description").required()));
        add(new BpTableDefinition("sort-keys", "Sort Keys",
                "Sort keys (assignment field) of customer accounts",
                "SortKeyConfig", SortKey.class, "id", "description",
                id("id", "Sort Key", 10), text("description", "Description").required()));
    }

    private BpTableDefinitions() {
    }

    private static void add(BpTableDefinition definition) {
        TABLES.put(definition.getKey(), definition);
    }

    public static List<BpTableDefinition> all() {
        return Collections.unmodifiableList(Arrays.asList(TABLES.values().toArray(new BpTableDefinition[0])));
    }

    public static Optional<BpTableDefinition> find(String key) {
        return Optional.ofNullable(key == null ? null : TABLES.get(key));
    }
}
