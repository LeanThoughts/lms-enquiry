package pfs.lms.enquiry.vault;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @author ffazil
 * @since 22/08/15
 */
@RestController
@RequestMapping("/api/upload")
public class UploadController {

    private final FileStorage storage;

    @Autowired
    public UploadController(FileStorage storage) {
        this.storage = storage;
    }

    @RequestMapping(method= RequestMethod.GET)
    public @ResponseBody String provideUploadInfo() {
        return "You can upload a file by posting to this same URL.";
    }

    @RequestMapping(method=RequestMethod.POST)
    public @ResponseBody
    FileReference handleFileUpload(@RequestParam(value = "file") MultipartFile file) throws Exception {
        UUID filePointer = null;
//        if (!file.isEmpty()) {
//            try {
//                filePointer = storage.saveFile(file);
//                return new FileReference(filePointer.toString());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        if (!file.isEmpty()) {
            filePointer = storage.saveFile(file);
            return new FileReference(filePointer.toString());
        }
        return null;

    }

    @RequestMapping("uploadError")
    public ResponseEntity onUploadError(HttpServletRequest request, Exception e) {
        return new ResponseEntity(HttpStatus.PRECONDITION_REQUIRED);
    }
}
