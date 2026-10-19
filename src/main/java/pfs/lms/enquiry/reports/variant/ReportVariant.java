package pfs.lms.enquiry.reports.variant;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * A report variant (as in SAP): the saved selection screen of a report, owned by a user and optionally shared with
 * everybody. A user has at most one variant of a report per name; one of the own variants can be the default that is
 * loaded when the report is opened.
 */
@Entity
@Table(name = "report_variant", uniqueConstraints = @UniqueConstraint(name = "uk_report_variant_owner_name", columnNames = {"report", "owner_name", "name"}))
public class ReportVariant {

    @Id
    @Type(type = "uuid-char")
    @Column(length = 36)
    private UUID id = UUID.randomUUID();

    /** Report key, e.g. kyc */
    @Column(nullable = false, length = 40)
    private String report;

    @Column(name = "owner_name", nullable = false, length = 100)
    private String ownerName;

    @Column(nullable = false, length = 40)
    private String name;

    @Column(length = 120)
    private String description;

    /** Visible and usable by all users (only the owner can change or delete it) */
    @Column(nullable = false)
    private boolean shared;

    /** The owner's default variant of the report */
    @Column(name = "default_variant", nullable = false)
    private boolean defaultVariant;

    /** The selection parameters as JSON */
    @Lob
    @Column(nullable = false)
    private String parameters;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "changed_on")
    private LocalDateTime changedOn;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getReport() { return report; }
    public void setReport(String report) { this.report = report; }
    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isShared() { return shared; }
    public void setShared(boolean shared) { this.shared = shared; }
    public boolean isDefaultVariant() { return defaultVariant; }
    public void setDefaultVariant(boolean defaultVariant) { this.defaultVariant = defaultVariant; }
    public String getParameters() { return parameters; }
    public void setParameters(String parameters) { this.parameters = parameters; }
    public LocalDateTime getCreatedOn() { return createdOn; }
    public void setCreatedOn(LocalDateTime createdOn) { this.createdOn = createdOn; }
    public LocalDateTime getChangedOn() { return changedOn; }
    public void setChangedOn(LocalDateTime changedOn) { this.changedOn = changedOn; }
}
