package pfs.lms.enquiry.collateral.domain;

import java.util.Arrays;

/**
 * The child tables of a collateral that are maintained in grids with a create / change / display dialog.
 * {@link #path} is the URL segment, e.g. /api/collaterals/items/{itemId}/coverages.
 */
public enum CollateralChildType {

    COVERAGE("coverages", CollateralCoverage.class, "Collateral Coverage", "Coverage"),
    ROC("roc", CollateralPerfectionRoc.class, "Security Perfection RoC", "RoC event"),
    CERSAI("cersai", CollateralPerfectionCersai.class, "Security Perfection CERSAI", "CERSAI event"),
    NESL("nesl", CollateralPerfectionNesl.class, "Security Perfection NeSL", "NeSL event"),
    DOCUMENT("documents", CollateralDocument.class, "Collateral Documents", "Document"),
    SECURITIES_POSITION("securities", CollateralSecuritiesPosition.class, "Securities Positions", "Securities position");

    private final String path;
    private final Class<? extends CollateralChildRecord<?>> entityClass;
    private final String subProcessName;
    private final String label;

    CollateralChildType(String path, Class<? extends CollateralChildRecord<?>> entityClass, String subProcessName,
                        String label) {
        this.path = path;
        this.entityClass = entityClass;
        this.subProcessName = subProcessName;
        this.label = label;
    }

    public String getPath() { return path; }

    public Class<? extends CollateralChildRecord<?>> getEntityClass() { return entityClass; }

    /** Sub-process of the change documents and SAP integration pointers. */
    public String getSubProcessName() { return subProcessName; }

    /** Readable name used in messages, e.g. "Coverage". */
    public String getLabel() { return label; }

    public static CollateralChildType fromPath(String path) {
        return Arrays.stream(values())
                .filter(type -> type.path.equalsIgnoreCase(path))
                .findFirst()
                .orElse(null);
    }
}
