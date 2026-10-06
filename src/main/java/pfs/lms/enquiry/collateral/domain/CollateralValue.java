package pfs.lms.enquiry.collateral.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Entry of a Collateral Management dropdown (value list), e.g. list CONDITION_GROUP, code "04",
 * description "Collateral Conditions". All lists live in this one table; see {@link CollateralValueList}.
 * Dropdown tables keep a numeric key (NFR 3).
 */
@Entity
@Table(name = "collateral_value",
        uniqueConstraints = @UniqueConstraint(name = "uk_collateral_value_list_code", columnNames = {"list_name", "code"}))
public class CollateralValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "list_name", length = 40, nullable = false)
    private String listName;

    @Column(name = "code", length = 20, nullable = false)
    private String code;

    @Column(length = 100)
    private String description;

    private Integer sortOrder;

    public CollateralValue() {
    }

    public CollateralValue(String listName, String code, String description, Integer sortOrder) {
        this.listName = listName;
        this.code = code;
        this.description = description;
        this.sortOrder = sortOrder;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getListName() { return listName; }
    public void setListName(String listName) { this.listName = listName; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
}
