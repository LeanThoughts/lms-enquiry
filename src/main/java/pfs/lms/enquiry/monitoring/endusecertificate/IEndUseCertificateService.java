package pfs.lms.enquiry.monitoring.endusecertificate;

import java.util.List;
import java.util.UUID;

public interface IEndUseCertificateService {

    EndUseCertificate saveEndUseCertificate(EndUseCertificateResource resource, String userName);

    EndUseCertificate updateEndUseCertificate(EndUseCertificateResource resource, String userName) throws CloneNotSupportedException;

    List<EndUseCertificate> getEndUseCertificates(UUID loanApplicationId);

    EndUseCertificate deleteEndUseCertificate(UUID endUseCertificateId, String username);
}
