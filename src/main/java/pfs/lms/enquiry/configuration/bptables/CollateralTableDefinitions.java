package pfs.lms.enquiry.configuration.bptables;

import pfs.lms.enquiry.collateral.domain.CollateralValue;
import pfs.lms.enquiry.collateral.domain.CollateralValueList;

import java.util.EnumMap;
import java.util.Map;

import static pfs.lms.enquiry.configuration.bptables.BpTableField.generatedId;
import static pfs.lms.enquiry.configuration.bptables.BpTableField.number;
import static pfs.lms.enquiry.configuration.bptables.BpTableField.text;

/**
 * The value lists (dropdowns) of Collateral Management, table collateral_value: one app in the Collaterals module of
 * the Configuration workspace (route /configuration/collateral-tables/collateral-values), filtered by value list.
 * CollateralValueInitializer only adds delivered entries that are missing, so entries changed here keep their values.
 */
final class CollateralTableDefinitions {

    /**
     * Longest code per value list: the length of the collateral column that stores the code (e.g. ZCOND_CAT has 3
     * characters, EVENT_TYPE 1), so that a new code always fits into the collateral and into SAP.
     */
    static final Map<CollateralValueList, Integer> CODE_LENGTHS = new EnumMap<>(CollateralValueList.class);

    static {
        CODE_LENGTHS.put(CollateralValueList.CONDITION_GROUP, 30);
        CODE_LENGTHS.put(CollateralValueList.CONDITION_CATEGORY, 3);
        CODE_LENGTHS.put(CollateralValueList.COLLATERAL_OBJECT_TYPE, 6);
        CODE_LENGTHS.put(CollateralValueList.AGREEMENT_TYPE, 6);
        CODE_LENGTHS.put(CollateralValueList.COMPLIANCE_STATUS, 20);
        CODE_LENGTHS.put(CollateralValueList.RESPONSIBLE_PARTY, 15);
        CODE_LENGTHS.put(CollateralValueList.ACTION_DAYS_PREFIX, 1);
        CODE_LENGTHS.put(CollateralValueList.ACTION_DAYS_SUFFIX, 1);
        CODE_LENGTHS.put(CollateralValueList.TIMELINE_EVENT, 1);
        CODE_LENGTHS.put(CollateralValueList.PERFECTION_EVENT_TYPE, 1);
        CODE_LENGTHS.put(CollateralValueList.COMPETENT_AUTHORITY, 1);
        CODE_LENGTHS.put(CollateralValueList.DOCUMENT_STAGE, 1);
        CODE_LENGTHS.put(CollateralValueList.SECURITIES_TYPE, 20);
        CODE_LENGTHS.put(CollateralValueList.COVERAGE_BASIS, 1);
        CODE_LENGTHS.put(CollateralValueList.FREQUENCY_UNIT, 6);
        CODE_LENGTHS.put(CollateralValueList.CURRENCY, 5);
    }

    private CollateralTableDefinitions() {
    }

    static BpTableDefinition collateralValues() {
        BpTableField list = text("listName", "Value List").key().filter().max(40);
        for (CollateralValueList valueList : CollateralValueList.values()) {
            list.option(valueList.name(), valueList.getDescription());
        }
        return new BpTableDefinition("collateral-values", "Collateral Value Lists",
                "Dropdown values of Collateral Management (condition groups, collateral object types, statuses, …) per value list",
                "CollateralValueInitializer", CollateralValue.class, "code", "description",
                generatedId(), list, text("code", "Code").key().max(20),
                text("description", "Description").required().max(100), number("sortOrder", "Sort Order"))
                .sortBy("listName", "sortOrder", "code")
                .validate(CollateralTableDefinitions::checkCode);
    }

    /** The code must fit into the collateral column of its value list */
    static String checkCode(Object row) {
        CollateralValue value = (CollateralValue) row;
        if (value.getListName() == null || value.getCode() == null) {
            return null;
        }
        Integer length;
        try {
            length = CODE_LENGTHS.get(CollateralValueList.valueOf(value.getListName()));
        } catch (IllegalArgumentException e) {
            return null;
        }
        if (length != null && value.getCode().length() > length) {
            return "Codes of the value list " + value.getListName() + " can have at most " + length
                    + (length == 1 ? " character" : " characters") + " (the length of the collateral field).";
        }
        if (value.getSortOrder() != null && value.getSortOrder() < 0) {
            return "Sort Order cannot be negative.";
        }
        return null;
    }
}
