package pfs.lms.enquiry.configuration.bptables;

import java.util.ArrayList;
import java.util.List;

/**
 * A column of a business partner configuration table (see {@link BpTableDefinitions}). Sent to the UI as it is.
 */
public class BpTableField {

    /** TEXT, NUMBER or BOOLEAN */
    public enum Type { TEXT, NUMBER, BOOLEAN }

    /**
     * NONE: ordinary column. ID: the primary key, entered when the row is created. GENERATED_ID: primary key generated
     * by the database (not shown). NEXT_NUMBER_ID: numeric primary key the portal assigns (highest + 1, not shown).
     */
    public enum IdKind { NONE, ID, GENERATED_ID, NEXT_NUMBER_ID }

    private final String name;
    private final String label;
    private Type type = Type.TEXT;
    private IdKind idKind = IdKind.NONE;
    /** Part of the business key: entered when the row is created, unique, not changed afterwards */
    private boolean businessKey;
    private boolean required;
    private int maxLength = 255;
    /** Values come from another configuration table (its key), e.g. "partner-groups" */
    private String referenceTable;
    /** The column holds the referenced entity itself (a JPA relation), not its code */
    private boolean referenceEntity;
    /** Fixed values (value + label) */
    private final List<BpTableDtos.Option> options = new ArrayList<>();

    private BpTableField(String name, String label) {
        this.name = name;
        this.label = label;
    }

    public static BpTableField text(String name, String label) {
        return new BpTableField(name, label);
    }

    public static BpTableField bool(String name, String label) {
        BpTableField field = new BpTableField(name, label);
        field.type = Type.BOOLEAN;
        return field;
    }

    /** Primary key entered by the user */
    public static BpTableField id(String name, String label, int maxLength) {
        BpTableField field = new BpTableField(name, label);
        field.idKind = IdKind.ID;
        field.required = true;
        field.maxLength = maxLength;
        return field;
    }

    public static BpTableField generatedId() {
        BpTableField field = new BpTableField("id", "ID");
        field.type = Type.NUMBER;
        field.idKind = IdKind.GENERATED_ID;
        return field;
    }

    public static BpTableField nextNumberId() {
        BpTableField field = new BpTableField("id", "ID");
        field.type = Type.NUMBER;
        field.idKind = IdKind.NEXT_NUMBER_ID;
        return field;
    }

    public BpTableField key() {
        this.businessKey = true;
        this.required = true;
        return this;
    }

    public BpTableField required() {
        this.required = true;
        return this;
    }

    public BpTableField max(int length) {
        this.maxLength = length;
        return this;
    }

    public BpTableField ref(String table) {
        this.referenceTable = table;
        return this;
    }

    public BpTableField refEntity(String table) {
        this.referenceTable = table;
        this.referenceEntity = true;
        return this;
    }

    public BpTableField option(String value, String label) {
        this.options.add(new BpTableDtos.Option(value, label));
        return this;
    }

    public boolean isIdField() {
        return idKind != IdKind.NONE;
    }

    /** Shown in the table and the dialog (generated keys are not) */
    public boolean isVisible() {
        return idKind != IdKind.GENERATED_ID && idKind != IdKind.NEXT_NUMBER_ID;
    }

    /** Can be changed on an existing row */
    public boolean isChangeable() {
        return idKind == IdKind.NONE && !businessKey;
    }

    public String getName() { return name; }
    public String getLabel() { return label; }
    public Type getType() { return type; }
    public IdKind getIdKind() { return idKind; }
    public boolean isBusinessKey() { return businessKey; }
    public boolean isRequired() { return required; }
    public int getMaxLength() { return maxLength; }
    public String getReferenceTable() { return referenceTable; }
    public boolean isReferenceEntity() { return referenceEntity; }
    public List<BpTableDtos.Option> getOptions() { return options; }
}
