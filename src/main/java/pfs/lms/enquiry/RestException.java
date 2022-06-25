package pfs.lms.enquiry;

/**
 * Created by sajeev on 15-Dec-18.
 */

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RestException extends RuntimeException {
    private String message;
    private Object[] args;
}
