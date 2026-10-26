package pfs.lms.enquiry.vault;

import org.apache.commons.io.IOUtils;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pfs.lms.enquiry.resource.FileResource;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;

@Component
public class FileSystemStorage implements FileStorage {

	private static final Logger log = LoggerFactory.getLogger(FileSystemStorage.class);

    private static final String dbName = "VAULT_DB";

    @Value("${vault.baseDirectory}")
    private String baseDirectory;

    @Value("${vault.dbFile}")
    private String dbFile;

    DB db = null;

    private ConcurrentNavigableMap<String, String> register = null;


    public FileSystemStorage(){
        // configure and open database using builder pattern.
        // all options are available with code auto-completion.
/*        DB db = DBMaker.fileDB(new File(dbFile))
                .closeOnJvmShutdown()
                .make();

        // open existing an collection (or create new)
        register = db.treeMap(dbName);*/
    }

    @Override
    public byte[] download(UUID id) {
        if(register.containsKey(id.toString())){
            String filename = register.get(id.toString());
            final File file = new File(filename);
            try {
                return Files.readAllBytes(file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("File not found : "+id);
    }

    @Override
    public FileResource getFile(UUID id) {
        FileResource fileResource = new FileResource();
        if(register.containsKey(id.toString())){
            String filename = register.get(id.toString());
            final File file = new File(filename);
            try {
                //return Files.readAllBytes(file.toPath());
                fileResource.setDocumentContent(Files.readAllBytes(file.toPath()));
                fileResource.setMimeType(Files.probeContentType(file.toPath()));

                return  fileResource;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("File not found : "+id);
    }

    @Override
	public Optional<FilePointer> findFile(UUID uuid) {
		log.debug("Downloading {}", uuid);
		if(register.containsKey(uuid.toString())){
            String filename = register.get(uuid.toString());
            final File file = new File(filename);
            final FileSystemPointer pointer = new FileSystemPointer(file);
            return Optional.of(pointer);
        }
        else {
            return Optional.empty();
        }


	}

    @Override
    public String getFilePath(UUID id) {
        if(register.containsKey(id.toString())){
            String filename = register.get(id.toString());
            return  filename;
        }
        else {
            return null;
        }

    }

    @Override
    public UUID saveFile(MultipartFile file) throws IOException, MimeTypeException{
        UUID uuid = UUID.randomUUID();
        String filename = uuid.toString() + FileSystemPointer.getFileExtension(new BufferedInputStream(file.getInputStream()));
        try {
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(findAbsolutePath(filename))));
            stream.write(file.getBytes());
            stream.close();
            register.put(uuid.toString(), findAbsolutePath(filename));
            db.commit();
            return uuid;
        } catch (Exception e) {
            log.error("You failed to upload " + filename + " => " + e.getMessage());
            db.rollback();
            return null;
        }
    }

    @Override
    public UUID saveFile(File file) throws IOException, MimeTypeException {
        UUID uuid = UUID.randomUUID();
        saveFile(file,uuid);
        return uuid;
    }



    public UUID saveFile(File file,UUID uuid) throws IOException, MimeTypeException {
        String targetFileName = uuid.toString() + FileSystemPointer.getFileExtension(new BufferedInputStream(new FileInputStream(file.getAbsolutePath())));
        try {
            FileInputStream inputStream = new FileInputStream(file);
            FileChannel inChannel = inputStream.getChannel();

            File target = new File(findAbsolutePath(targetFileName));
            FileOutputStream outputStream = new FileOutputStream(target);
            FileChannel outChannel = outputStream.getChannel();

            inChannel.transferTo(0, file.length(), outChannel);

            inputStream.close();
            outputStream.close();

            register.put(uuid.toString(), findAbsolutePath(targetFileName));
            db.commit();
            return uuid;
        } catch (Exception e) {
            log.error("You failed to upload " + targetFileName + " => " + e.getMessage());
            db.rollback();
            return null;
        }
    }

    public UUID saveFile(byte[] bytes, UUID uuid) {
        String targetFileName = null;
        try {
            InputStream inputStream = new ByteArrayInputStream(bytes);
            String mimeType = getFileExtension(inputStream);
            targetFileName = uuid.toString() + mimeType;
            IOUtils.copy(inputStream, new FileOutputStream(findAbsolutePath(targetFileName)));
            register.put(uuid.toString(), findAbsolutePath(targetFileName));
            db.commit();
            return uuid;
        } catch (Exception e) {
            log.error("Unable to save " + targetFileName + " => " + e.getMessage());
            db.rollback();
            return null;
        }
    }

    public static String getFileExtension(InputStream inputStream) throws IOException, MimeTypeException {
        TikaConfig config = TikaConfig.getDefaultConfig();
        MediaType mediaType = config.getMimeRepository().detect(inputStream, new Metadata());
        MimeType mimeType = config.getMimeRepository().forName(mediaType.toString());
        return mimeType.getExtension();
    }

    @PostConstruct
    private void postConstruct() throws IOException {
        if (!Files.exists(Paths.get(this.baseDirectory))) {
            Files.createDirectories(Paths.get(this.baseDirectory));
        }
        log.info("Vault file directory: {}", this.baseDirectory);

        File database = new File(this.dbFile);
        File parent = database.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new IOException("Could not create vault database directory: " + parent.getAbsolutePath());
        }
        db = DBMaker.fileDB(database)
                .closeOnJvmShutdown()
                .fileLockDisable()
                .fileLockHeartbeatEnable()
                .make();
        register = db.treeMap(dbName);
        log.info("Vault database file: {}", database.getAbsolutePath());
    }

    private String findAbsolutePath(String filename) {
        return Paths.get(this.baseDirectory, filename).toString();
    }
}

