package pfs.lms.enquiry.documentation.nodalofficer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.documentation.Documentation;
import pfs.lms.enquiry.documentation.DocumentationRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NodalOfficerService implements INodalOfficerService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final DocumentationRepository documentationRepository;
    private final NodalOfficerRepository nodalOfficerRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public NodalOfficer create(NodalOfficerResource resource, String username) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());
        Documentation documentation = documentationRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    Documentation obj = new Documentation();
                    obj.setLoanApplication(loanApplication);
                    obj.setLoanContractId(loanApplication.getLoanContractId());
                    obj = documentationRepository.save(obj);
                    changeDocumentService.createChangeDocument(
                           obj.getId(),obj.getId().toString(),obj.getId().toString(),
                            loanApplication.getLoanContractId(),
                            null,
                            obj,
                            "Created",
                            username,
                            "Documentation", "Header");
                    return obj;
                });
        NodalOfficer nodalOfficer = new NodalOfficer();
        nodalOfficer.setDocumentation(documentation);
        nodalOfficer.setSerialNumber(nodalOfficerRepository.findByDocumentationId(documentation.getId()).size() + 1);
        nodalOfficer.setBpCode(resource.getBpCode());
        nodalOfficer.setName(resource.getName());
        nodalOfficer.setStartDate(resource.getStartDate());
        nodalOfficer.setEndDate(resource.getEndDate());
        nodalOfficer.setDeleteFlag(false);
        nodalOfficer = nodalOfficerRepository.save(nodalOfficer);

         changeDocumentService.createChangeDocument(
                 nodalOfficer.getId(),
                 nodalOfficer.getId().toString(),
                 nodalOfficer.getDocumentation().getId().toString(),
                 nodalOfficer.getDocumentation().getLoanApplication().getLoanContractId(),
                null,
                 nodalOfficer,
                "Created",
                username,
                "Documentation", "NodalOfficer" );

        return nodalOfficer;
    }

    @Override
    public NodalOfficer update(NodalOfficerResource resource, String username) throws CloneNotSupportedException {
        NodalOfficer nodalOfficer =
                nodalOfficerRepository.findById(resource.getId())
                        .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

        Object oldObject = nodalOfficer.clone();

        nodalOfficer.setBpCode(resource.getBpCode());
        nodalOfficer.setName(resource.getName());
        nodalOfficer.setStartDate(resource.getStartDate());
        nodalOfficer.setEndDate(resource.getEndDate());
        nodalOfficer.setDeleteFlag(false);
        nodalOfficer = nodalOfficerRepository.save(nodalOfficer);

        changeDocumentService.createChangeDocument(
                nodalOfficer.getId(),
                nodalOfficer.getId().toString(),
                nodalOfficer.getDocumentation().getId().toString(),
                nodalOfficer.getDocumentation().getLoanApplication().getLoanContractId(),
                oldObject,
                nodalOfficer,
                "Updated",
                username,
                "Documentation", "NodalOfficer" );


        return nodalOfficer;
    }

    @Override
    public NodalOfficer delete(UUID nodalOfficerId, String username) throws CloneNotSupportedException {
        NodalOfficer nodalOfficer = nodalOfficerRepository.findById(nodalOfficerId).
                orElseThrow(() -> new EntityNotFoundException(nodalOfficerId.toString()));
        nodalOfficer.setDeleteFlag(true);
        nodalOfficer = nodalOfficerRepository.save(nodalOfficer);

        changeDocumentService.createChangeDocument(
                nodalOfficer.getId(),
                nodalOfficer.getId().toString(),
                nodalOfficer.getDocumentation().getId().toString(),
                nodalOfficer.getDocumentation().getLoanApplication().getLoanContractId(),
                null,
                nodalOfficer,
                "Deleted",
                username,
                "Documentation", "NodalOfficer" );
        return nodalOfficer;
    }
}
