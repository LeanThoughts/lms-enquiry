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
import java.time.format.DateTimeFormatter;
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
            referenceInterestRateValue.setWorkFlowStatusCode(0);
            referenceInterestRateValue.setWorkFlowStatusDescription("Not Sent for Approval");
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
        referenceInterestRateValue.setWorkFlowStatusCode(0);
        referenceInterestRateValue.setWorkFlowStatusDescription("Not Sent for Approval");
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
        // Only Delete after workflow is approved
         referenceInterestRateValue.setModificationStatus(2);//Marked for Deletion
        referenceInterestRateValue.setWorkFlowStatusCode(0);
        referenceInterestRateValue.setWorkFlowStatusDescription("Not Sent for Approval");
        referenceInterestRateValueRepository.save(referenceInterestRateValue);


        String mainEntityId = referenceInterestRateValue.getReferenceInterestRate().getCode();
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String entityId     = referenceInterestRateValue.getValidFromDate().format(formatters);

        // Change Documents
        changeDocumentService.createChangeDocument(
                referenceInterestRateValue.getId(),
                mainEntityId, //Valid From Date
                entityId,     //Ref. Interest Rate Type
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

        if (referenceInterestValue.getModificationStatus() == 1 ||  referenceInterestValue.getModificationStatus() == 0) {
            referenceInterestValue.setModificationStatus(0);
            referenceInterestRateValueRepository.save(referenceInterestValue);
        }
        if (referenceInterestValue.getModificationStatus() == 2 ) {
            referenceInterestValue.setModificationStatus(0);
            referenceInterestRateValueRepository.delete(referenceInterestValue );
            referenceInterestRateValueRepository.flush();
        }

        return null;
    }

    @Override
    public ReferenceInterestRateValue processRejectionReferenceInterestValue(ReferenceInterestRateValue referenceInterestValue, String username) throws CloneNotSupportedException {
        referenceInterestValue.setModificationStatus(0);
        referenceInterestRateValueRepository.save(referenceInterestValue);
        return null;
    }
}
