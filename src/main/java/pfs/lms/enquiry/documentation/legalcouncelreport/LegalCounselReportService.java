package pfs.lms.enquiry.documentation.legalcouncelreport;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.documentation.legalcounsel.LegalCounsel;
import pfs.lms.enquiry.documentation.legalcounsel.LegalCounselRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LegalCounselReportService implements ILegalCounselReportService {

    private final LegalCounselRepository legalCounselRepository;
    private final LegalCounselReportRepository legalCounselReportRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public LegalCounselReport create(LegalCounselReportResource resource, String username) {
        LegalCounsel legalCounsel = legalCounselRepository.getOne(resource.getLegalCounselId());
        LegalCounselReport legalCounselReport = new LegalCounselReport();
        legalCounselReport.setLegalCounsel(legalCounsel);
        legalCounselReport.setSerialNumber(legalCounselReportRepository.findByLegalCounselId(legalCounsel.getId()).size() + 1);
        legalCounselReport.setSubmissionDate(resource.getSubmissionDate());
        legalCounselReport.setFiscalYear(resource.getFiscalYear());
        legalCounselReport.setPeriod(resource.getPeriod());
        legalCounselReport.setRemarks(resource.getRemarks());
        legalCounselReport.setDocumentType(resource.getDocumentType());
        legalCounselReport.setDocumentName(resource.getDocumentName());
        legalCounselReport.setFileReference(resource.getFileReference());
        legalCounselReport.setDeleteFlag(false);
        legalCounselReport = legalCounselReportRepository.save(legalCounselReport);

        // Change Documents for SanctionReasonForDelay
//        changeDocumentService.createChangeDocument(
//                documentationReasonForDelay.getDocumentation().getId(),
//                documentationReasonForDelay.getId().toString(),
//                documentationReasonForDelay.getDocumentation().getId().toString(),
//                documentationReasonForDelay.getDocumentation().getLoanApplication().getLoanContractId(),
//                null,
//                documentationReasonForDelay,
//                "Created",
//                username,
//                "Sanction", "SanctionReasonForDelay" );

        return legalCounselReport;
    }

    @Override
    public LegalCounselReport update(LegalCounselReportResource resource, String username) throws CloneNotSupportedException {
        LegalCounselReport legalCounselReport =
                legalCounselReportRepository.findById(resource.getId())
                        .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

        Object oldObject = legalCounselReport.clone();

        legalCounselReport.setSubmissionDate(resource.getSubmissionDate());
        legalCounselReport.setFiscalYear(resource.getFiscalYear());
        legalCounselReport.setPeriod(resource.getPeriod());
        legalCounselReport.setRemarks(resource.getRemarks());
        legalCounselReport.setDocumentType(resource.getDocumentType());
        legalCounselReport.setDocumentName(resource.getDocumentName());
        legalCounselReport.setFileReference(resource.getFileReference());
        legalCounselReport.setDeleteFlag(false);
        legalCounselReport = legalCounselReportRepository.save(legalCounselReport);

        // Change Documents for SanctionReasonForDelay
//        changeDocumentService.createChangeDocument(
//                documentationReasonForDelay.getDocumentation().getId(),
//                documentationReasonForDelay.getId().toString(),
//                documentationReasonForDelay.getDocumentation().getId().toString(),
//                documentationReasonForDelay.getDocumentation().getLoanApplication().getLoanContractId(),
//                oldObject,
//                documentationReasonForDelay,
//                "Updated",
//                username,
//                "Sanction", "SanctionReasonForDelay" );

        return legalCounselReport;
    }

    @Override
    public LegalCounselReport delete(UUID legalCounselReportId, String username) throws CloneNotSupportedException {
        LegalCounselReport legalCounselReport = legalCounselReportRepository.findById(legalCounselReportId).
                orElseThrow(() -> new EntityNotFoundException(legalCounselReportId.toString()));
        legalCounselReport.setDeleteFlag(true);
        legalCounselReport = legalCounselReportRepository.save(legalCounselReport);

        // Change Documents for SanctionReasonForDelay
//        changeDocumentService.createChangeDocument(
//                documentationReasonForDelay.getDocumentation().getId(),
//                documentationReasonForDelay.getId().toString(),
//                documentationReasonForDelay.getDocumentation().getId().toString(),
//                documentationReasonForDelay.getDocumentation().getLoanApplication().getLoanContractId(),
//                null,
//                documentationReasonForDelay,
//                "Deleted",
//                username,
//                "Sanction", "SanctionReasonForDelay" );
        return legalCounselReport;
    }
}
