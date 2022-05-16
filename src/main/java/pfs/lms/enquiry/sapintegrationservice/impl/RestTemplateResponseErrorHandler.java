package pfs.lms.enquiry.sapintegrationservice.impl;


 import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

/**
 * Created by sajeev on 15-Jun-21.
 */
@Component
public class RestTemplateResponseErrorHandler  implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse)
            throws IOException {

        return (
                httpResponse.getStatusCode().series() == CLIENT_ERROR
                        || httpResponse.getStatusCode().series() == SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse)
            throws IOException {

        if (httpResponse.getStatusCode()
                .series() == SERVER_ERROR) {

            // handle SERVER_ERROR
            System.out.println("HTTP SERVER EXCEPTION :  " + httpResponse.getStatusCode().toString());

        } else if (httpResponse.getStatusCode()
                .series() == CLIENT_ERROR) {
            System.out.println("HTTP CLIENT EXCEPTION :  " + httpResponse.getStatusCode().toString());
            if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
                System.out.println("HTTP EXCEPTION : NOT_FOUND EXCEPTION...............................................");
            }
        }
    }

}
