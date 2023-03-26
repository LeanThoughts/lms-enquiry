package pfs.lms.enquiry.monitoring.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by gguptha on 09/11/18.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class SAPDocumentAttachmentResourceDetails {


    @JsonProperty (value = "Id")
    private String id;

    @JsonProperty (value = "EntityId")
    private String entityId;

    @JsonProperty (value = "EntityName")
    private String entityName;


    @JsonProperty (value = "Documentcontent")
    private String documentcontent;

    @JsonProperty (value = "MimeType")
    private String mimeType;

    @JsonProperty (value = "Filename")
    private String filename;

    @JsonProperty (value = "FileReference")
    private String fileReference;


    public SAPDocumentAttachmentResourceDetails() {
    }

    @Override
    public String toString() {
        return "SAPDocumentAttachmentResourceDetails{" +
                "id='" + id + '\'' +
                ", entityId=" + entityId +
                ", entityName='" + entityName + '\'' +
                ", documentcontent='" + documentcontent + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", filename='" + filename + '\'' +
                '}';
    }
}
