package pfs.lms.enquiry.reports.variant;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pfs.lms.enquiry.config.ApiController;
import pfs.lms.enquiry.exception.LmsException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * REST API of the report variants, under /api from {@link ApiController}: {@code GET /reports/{report}/variants} lists
 * the own and the shared variants with their parameters, {@code POST} saves (or overwrites by name) the variant of the
 * signed-in user, {@code DELETE /reports/{report}/variants/{id}} deletes an own variant.
 */
@ApiController
public class ReportVariantController {

    private final ReportVariantService service;

    public ReportVariantController(ReportVariantService service) {
        this.service = service;
    }

    @GetMapping("/reports/{report}/variants")
    public List<VariantDto> list(@PathVariable String report, HttpServletRequest request) {
        String user = user(request);
        return service.list(report, user).stream().map(v -> dto(v, user)).collect(Collectors.toList());
    }

    @PostMapping("/reports/{report}/variants")
    public VariantDto save(@PathVariable String report, @RequestBody SaveRequest body, HttpServletRequest request) {
        String user = user(request);
        ReportVariant saved = service.save(report, user, body.getName(), body.getDescription(), body.isShared(),
                body.isDefaultVariant(), body.getParameters());
        return dto(saved, user);
    }

    @DeleteMapping("/reports/{report}/variants/{id}")
    public ResponseEntity<Void> delete(@PathVariable String report, @PathVariable UUID id, HttpServletRequest request) {
        service.delete(report, user(request), id);
        return ResponseEntity.noContent().build();
    }

    private VariantDto dto(ReportVariant v, String user) {
        boolean mine = v.getOwnerName().equals(user);
        VariantDto dto = new VariantDto();
        dto.setId(v.getId().toString());
        dto.setName(v.getName());
        dto.setDescription(v.getDescription());
        dto.setOwner(v.getOwnerName());
        dto.setMine(mine);
        dto.setShared(v.isShared());
        dto.setDefaultVariant(mine && v.isDefaultVariant());
        dto.setChangedOn(v.getChangedOn() == null ? null : v.getChangedOn().toString());
        dto.setParameters(service.parameters(v));
        return dto;
    }

    private static String user(HttpServletRequest request) {
        if (request.getUserPrincipal() == null) {
            throw new LmsException("Please sign in.", HttpStatus.UNAUTHORIZED);
        }
        return request.getUserPrincipal().getName();
    }

    public static class SaveRequest {
        private String name;
        private String description;
        private boolean shared;
        private boolean defaultVariant;
        private JsonNode parameters;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public boolean isShared() { return shared; }
        public void setShared(boolean shared) { this.shared = shared; }
        public boolean isDefaultVariant() { return defaultVariant; }
        public void setDefaultVariant(boolean defaultVariant) { this.defaultVariant = defaultVariant; }
        public JsonNode getParameters() { return parameters; }
        public void setParameters(JsonNode parameters) { this.parameters = parameters; }
    }

    public static class VariantDto {
        private String id;
        private String name;
        private String description;
        private String owner;
        private boolean mine;
        private boolean shared;
        private boolean defaultVariant;
        private String changedOn;
        private JsonNode parameters;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getOwner() { return owner; }
        public void setOwner(String owner) { this.owner = owner; }
        public boolean isMine() { return mine; }
        public void setMine(boolean mine) { this.mine = mine; }
        public boolean isShared() { return shared; }
        public void setShared(boolean shared) { this.shared = shared; }
        public boolean isDefaultVariant() { return defaultVariant; }
        public void setDefaultVariant(boolean defaultVariant) { this.defaultVariant = defaultVariant; }
        public String getChangedOn() { return changedOn; }
        public void setChangedOn(String changedOn) { this.changedOn = changedOn; }
        public JsonNode getParameters() { return parameters; }
        public void setParameters(JsonNode parameters) { this.parameters = parameters; }
    }
}
