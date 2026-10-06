package pfs.lms.enquiry.collateral.migration;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import pfs.lms.enquiry.config.ApiController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Migration API for the SAP ABAP program that loads the existing collateral checklists (ZPFS_T_LN_CHKLST and its
 * child tables) into the portal. Paths sit under /api (from {@link ApiController}).
 * <p>
 * Open without sign-in (see {@link CollateralMigrationSecurityConfig}) while {@code collateral.migration.enabled}
 * is true (default). Set it to false once the migration is complete: the endpoints then answer 404.
 */
@ApiController
public class CollateralMigrationController {

    private final CollateralMigrationService migrationService;
    private final boolean enabled;

    public CollateralMigrationController(CollateralMigrationService migrationService,
                                         @Value("${collateral.migration.enabled:true}") boolean enabled) {
        this.migrationService = migrationService;
        this.enabled = enabled;
    }

    /** Connection test for the ABAP program: GET /api/collaterals/migration/ping */
    @GetMapping("/collaterals/migration/ping")
    public ResponseEntity<Map<String, Object>> ping() {
        if (!enabled) {
            return ResponseEntity.notFound().build();
        }
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", "OK");
        body.put("service", "Collateral Management migration");
        return ResponseEntity.ok(body);
    }

    /**
     * Loads checklists: POST /api/collaterals/migration/checklists[?dryRun=true]. The body is one checklist object
     * or an array of them. Answers 200 with one result per checklist (status OK, PARTIAL or FAILED).
     */
    @PostMapping("/collaterals/migration/checklists")
    public ResponseEntity<List<MigrationResultDto>> migrate(@RequestBody JsonNode body,
                                                            @RequestParam(defaultValue = "false") boolean dryRun) {
        if (!enabled) {
            return ResponseEntity.notFound().build();
        }
        if (body == null || !(body.isArray() || body.isObject())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyList());
        }
        List<JsonNode> checklists = new ArrayList<>();
        if (body.isArray()) {
            body.forEach(checklists::add);
        } else {
            checklists.add(body);
        }
        return ResponseEntity.ok(migrationService.migrate(checklists, dryRun));
    }
}
