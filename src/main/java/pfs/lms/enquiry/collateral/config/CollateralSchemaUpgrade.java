package pfs.lms.enquiry.collateral.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

/**
 * Schema changes that hibernate's schema update does not make (it never changes a column type).
 * <p>
 * Checklist ID No. (checklist_id_no) became a number: on MySQL, columns still of a text type are changed to BIGINT.
 * Existing values such as "0000000912" become 912. Runs at every start and does nothing once the columns are BIGINT.
 * A failure is logged and does not stop the application.
 */
@Component
@Order(0)
public class CollateralSchemaUpgrade implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(CollateralSchemaUpgrade.class);

    static final List<String> CHECKLIST_ID_TABLES = Arrays.asList(
            "collateral_item", "collateral_coverage", "collateral_perfection_roc", "collateral_perfection_cersai",
            "collateral_perfection_nesl", "collateral_document", "collateral_securities_position");

    private final DataSource dataSource;

    public CollateralSchemaUpgrade(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) {
        try (Connection connection = dataSource.getConnection()) {
            String product = connection.getMetaData().getDatabaseProductName();
            if (product == null || !product.toLowerCase().contains("mysql")) {
                log.info("Collateral schema upgrade skipped: database {} is not MySQL", product);
                return;
            }
            for (String table : CHECKLIST_ID_TABLES) {
                String type = columnType(connection, table, "checklist_id_no");
                if (type == null || "bigint".equalsIgnoreCase(type)) {
                    continue;
                }
                try (Statement statement = connection.createStatement()) {
                    statement.execute("ALTER TABLE " + table + " MODIFY checklist_id_no BIGINT");
                    log.info("Collateral schema upgrade: {}.checklist_id_no changed from {} to BIGINT", table, type);
                } catch (Exception ex) {
                    log.error("Collateral schema upgrade: {}.checklist_id_no could not be changed from {} to BIGINT. "
                            + "Check for values that are not numbers and run: ALTER TABLE {} MODIFY checklist_id_no BIGINT",
                            table, type, table, ex);
                }
            }
        } catch (Exception ex) {
            log.error("Collateral schema upgrade failed", ex);
        }
    }

    private static String columnType(Connection connection, String table, String column) throws Exception {
        String sql = "SELECT DATA_TYPE FROM information_schema.COLUMNS "
                + "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ? AND COLUMN_NAME = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, table);
            statement.setString(2, column);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? result.getString(1) : null;
            }
        }
    }
}
