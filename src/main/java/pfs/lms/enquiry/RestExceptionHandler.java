package pfs.lms.enquiry;

public class RestExceptionHandler {

//
///**
// * Created by sajeev on 15-Dec-18.
// */
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.MessageSource;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.support.ReloadableResourceBundleMessageSource;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//import java.util.List;
//import java.util.Locale;
//import java.util.stream.Collectors;
////
//@ControllerAdvice
//public class RestExceptionHandler {
//    private static final String UNEXPECTED_ERROR = "Exception.unexpected";
//    private final MessageSource messageSource;
//
//    @Bean
//    public MessageSource messageSource() {
//        ReloadableResourceBundleMessageSource messageSource
//                = new ReloadableResourceBundleMessageSource();
//
//        messageSource.setBasename("classpath:messages");
//        messageSource.setDefaultEncoding("UTF-8");
//        return messageSource;
//    }
//
//     @Autowired
//     public RestExceptionHandler(MessageSource messageSource) {
//        this.messageSource = messageSource;
//    }
//
//    @ExceptionHandler(RestException.class)
//    public ResponseEntity<RestMessage> handleIllegalArgument(RestException ex, Locale locale) {
//        String errorMessage = messageSource.getMessage(ex.getMessage(), ex.getArgs(), locale);
//        return new ResponseEntity<>(new RestMessage(errorMessage), HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<RestMessage> handleArgumentNotValidException(MethodArgumentNotValidException ex, Locale locale) {
//        BindingResult result = ex.getBindingResult();
//        List<String> errorMessages = result.getAllErrors()
//                .stream()
//                .map(objectError -> messageSource.getMessage(objectError, locale))
//                .collect(Collectors.toList());
//        return new ResponseEntity<>(new RestMessage(errorMessages), HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<RestMessage> handleExceptions(Exception ex, Locale locale) {
//        String errorMessage = messageSource.getMessage(UNEXPECTED_ERROR, null, locale);
//        ex.printStackTrace();
//        return new ResponseEntity<>(new RestMessage(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}
