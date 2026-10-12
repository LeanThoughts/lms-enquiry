package pfs.lms.enquiry.collateral.batch;

import pfs.lms.enquiry.collateral.domain.CollateralDocument;
import pfs.lms.enquiry.collateral.domain.CollateralItem;

import java.net.URLConnection;
import java.util.Optional;
import java.util.UUID;

/**
 * The uploaded file that belongs to a collateral entity, if it has one. Two entities carry a file:
 * <ul>
 * <li>{@link CollateralDocument}: {@code fileReference} (vault file id) and {@code fileName};</li>
 * <li>{@link CollateralItem}: the post-execution document, {@code postExecutionDocumentReference} and
 * {@code postExecutionDocumentName}.</li>
 * </ul>
 * The file reference is the id of the file in the portal vault ({@code pfs.lms.enquiry.vault.FileStorage}); the vault
 * does not keep the original file name, so the name comes from the entity. After the entity itself has been posted,
 * the file is sent to SAP as the entity's media stream ({@code PUT <entity>/$value}).
 */
public final class CollateralSapAttachment {

    public static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";
    private static final String DEFAULT_FILE_NAME = "document";

    private final UUID fileId;
    private final String fileName;

    private CollateralSapAttachment(UUID fileId, String fileName) {
        this.fileId = fileId;
        this.fileName = fileName;
    }

    /** The attachment of a collateral entity, or empty when the entity type has no file or none was uploaded. */
    public static Optional<CollateralSapAttachment> of(Object entity) {
        if (entity instanceof CollateralDocument) {
            CollateralDocument document = (CollateralDocument) entity;
            return create(document.getFileReference(), document.getFileName());
        }
        if (entity instanceof CollateralItem) {
            CollateralItem item = (CollateralItem) entity;
            return create(item.getPostExecutionDocumentReference(), item.getPostExecutionDocumentName());
        }
        return Optional.empty();
    }

    private static Optional<CollateralSapAttachment> create(String reference, String fileName) {
        if (reference == null || reference.trim().isEmpty()) {
            return Optional.empty();
        }
        try {
            UUID id = UUID.fromString(reference.trim());
            String name = fileName == null || fileName.trim().isEmpty() ? DEFAULT_FILE_NAME : fileName.trim();
            return Optional.of(new CollateralSapAttachment(id, name));
        } catch (IllegalArgumentException notAVaultId) {
            return Optional.empty(); // not a vault file id: nothing that can be streamed
        }
    }

    /** The vault file id. */
    public UUID getFileId() { return fileId; }

    /** The file name sent to SAP (Slug header), e.g. {@code sanction-letter.pdf}. */
    public String getFileName() { return fileName; }

    /**
     * The content type to send: the type the vault detected, else one guessed from the file name's extension, else
     * {@value #DEFAULT_CONTENT_TYPE}.
     */
    public String contentType(String detected) {
        if (detected != null && !detected.trim().isEmpty()) {
            return detected.trim();
        }
        String guessed = URLConnection.guessContentTypeFromName(fileName);
        return guessed != null ? guessed : DEFAULT_CONTENT_TYPE;
    }

    @Override
    public String toString() {
        return fileName + " (" + fileId + ")";
    }
}
