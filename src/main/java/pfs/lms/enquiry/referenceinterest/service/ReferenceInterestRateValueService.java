package pfs.lms.enquiry.referenceinterest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.referenceinterest.domain.ReferenceInterestRate;
import pfs.lms.enquiry.referenceinterest.domain.ReferenceInterestRateValue;
import pfs.lms.enquiry.referenceinterest.repository.ReferenceInterestRateRepository;
import pfs.lms.enquiry.referenceinterest.repository.ReferenceInterestRateValueRepository;
import pfs.lms.enquiry.referenceinterest.resource.ReferenceInterestValueResource;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReferenceInterestRateValueService implements IReferenceInterestRateValueService {

    private final IChangeDocumentService changeDocumentService;

    private final ReferenceInterestRateRepository referenceInterestRateRepository;
    private final ReferenceInterestRateValueRepository referenceInterestRateValueRepository;

    @Override
    public ReferenceInterestRateValue create(ReferenceInterestValueResource referenceInterestValueResource, String username) throws Exception {

        ReferenceInterestRateValue v1 = referenceInterestRateValueRepository.
                findByReferenceInterestRateIdAndValidFromDate(referenceInterestValueResource.getReferenceInterestRate(),
                        referenceInterestValueResource.getValidFromDate());
        ReferenceInterestRateValue referenceInterestRateValue = null;
        if (v1 == null) {
            ReferenceInterestRate referenceInterestRate =
                    referenceInterestRateRepository.getOne(referenceInterestValueResource.getReferenceInterestRate());
            referenceInterestRateValue = new ReferenceInterestRateValue();
            referenceInterestRateValue.setCreatedBy(username.toString());
            referenceInterestRateValue.setCreatedOn(LocalDate.now());
            referenceInterestRateValue.setValidFromDate(referenceInterestValueResource.getValidFromDate());
            referenceInterestRateValue.setInterestRate(referenceInterestValueResource.getInterestRate());
            referenceInterestRateValue.setReferenceInterestRate(referenceInterestRate);
            referenceInterestRateValue.setModificationStatus(1);
            referenceInterestRateValue = referenceInterestRateValueRepository.save(referenceInterestRateValue);

            // Change Documents
        changeDocumentService.createChangeDocument(
                referenceInterestRateValue.getId(),referenceInterestRateValue.getId().toString(),referenceInterestRateValue.getId().toString(),
                referenceInterestRateValue.getId().toString(),
                null,
                referenceInterestRateValue,
                "Created",
                username,
                "ReferenceInterestRateValue", "Header");

            return referenceInterestRateValue;
        }
        else {
            return null;
        }
    }

    @Override
    public ReferenceInterestRateValue update(ReferenceInterestValueResource referenceInterestValueResource, String username) throws Exception {
        ReferenceInterestRateValue referenceInterestRateValue = new ReferenceInterestRateValue();

        referenceInterestRateValue = referenceInterestRateValueRepository.getOne(referenceInterestValueResource.getId());

        Object existingObject = referenceInterestRateValue.clone();

        referenceInterestRateValue.setInterestRate(referenceInterestValueResource.getInterestRate());
        referenceInterestRateValue.setModificationStatus(1);
        referenceInterestRateValue.setWorkFlowStatusCode(null);
        referenceInterestRateValue.setWorkFlowStatusDescription("");
        referenceInterestRateValue = referenceInterestRateValueRepository.save(referenceInterestRateValue);

        // Change Documents
        changeDocumentService.createChangeDocument(
                referenceInterestRateValue.getId(),referenceInterestRateValue.getId().toString(),referenceInterestRateValue.getId().toString(),
                referenceInterestRateValue.getId().toString(),
                existingObject,
                referenceInterestRateValue,
                "Updated",
                username,
                "ReferenceInterestRateValue", "Header");

        return referenceInterestRateValue;
    }

    @Override
    public ReferenceInterestRateValue delete(UUID referenceInterestValueId, String username) throws Exception {

        ReferenceInterestRateValue referenceInterestRateValue =
                referenceInterestRateValueRepository.getOne(referenceInterestValueId);
        referenceInterestRateValueRepository.delete(referenceInterestRateValue);

        // Change Documents
        changeDocumentService.createChangeDocument(
                referenceInterestRateValue.getId(),referenceInterestRateValue.getId().toString(),referenceInterestRateValue.getId().toString(),
                referenceInterestRateValue.getId().toString(),
                null,
                referenceInterestRateValue,
                "Deleted",
                username,
                "ReferenceInterestRateValue", "Header");

        return referenceInterestRateValue;
    }

    @Override
    public ReferenceInterestRateValue processApprovedReferenceInterestValue(ReferenceInterestRateValue referenceInterestValue, String username) throws CloneNotSupportedException {
        referenceInterestValue.setModificationStatus(0);
        referenceInterestRateValueRepository.save(referenceInterestValue);
        return null;
    }

    @Override
    public ReferenceInterestRateValue processRejectionReferenceInterestValue(ReferenceInterestRateValue referenceInterestValue, String username) throws CloneNotSupportedException {
        referenceInterestValue.setModificationStatus(0);
        referenceInterestRateValueRepository.save(referenceInterestValue);
        return null;
    }
}
