package pfs.lms.enquiry.referenceinterest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.referenceinterest.domain.ReferenceInterestRateValue;
import pfs.lms.enquiry.referenceinterest.repository.ReferenceInterestRateValueRepository;
import pfs.lms.enquiry.referenceinterest.resource.ReferenceInterestValueResource;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.transaction.Transactional;
import java.time.LocalDate;
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReferenceInterestRateValueService implements IReferenceInterestRateValueService {

    @Autowired
    IChangeDocumentService changeDocumentService;

    @Autowired
    ReferenceInterestRateValueRepository referenceInterestRateValueRepository;

    @Override
    public ReferenceInterestRateValue create(ReferenceInterestValueResource referenceInterestValueResource, String username) throws Exception {

        ReferenceInterestRateValue referenceInterestRateValue = new ReferenceInterestRateValue();
        referenceInterestRateValue.setCreatedBy(username.toString());
        referenceInterestRateValue.setCreatedOn(LocalDate.now());
        referenceInterestRateValue.setValidFromDate(referenceInterestValueResource.getValidFromDate());
        referenceInterestRateValue.setInterestRate(referenceInterestRateValue.getInterestRate());

        referenceInterestRateValueRepository.save(referenceInterestRateValue);

        // Change Documents
        changeDocumentService.createChangeDocument(
                referenceInterestValueResource.getId(),referenceInterestValueResource.getId().toString(),referenceInterestValueResource.getId().toString(),
                referenceInterestValueResource.getId().toString(),
                null,
                referenceInterestValueResource,
                "Created",
                username,
                "ReferenceInterestRateValue", "Header");

        return referenceInterestRateValue;
    }

    @Override
    public ReferenceInterestRateValue update(ReferenceInterestValueResource referenceInterestValueResource, String username) throws Exception {
        ReferenceInterestRateValue referenceInterestRateValue = new ReferenceInterestRateValue();

        referenceInterestRateValue = referenceInterestRateValueRepository.getOne(referenceInterestValueResource.getId());

        Object existingObject = referenceInterestRateValue.clone();

        referenceInterestRateValue.setCreatedBy(username.toString());
        referenceInterestRateValue.setCreatedOn(LocalDate.now());
        referenceInterestRateValue.setValidFromDate(referenceInterestValueResource.getValidFromDate());
        referenceInterestRateValue.setInterestRate(referenceInterestRateValue.getInterestRate());

        referenceInterestRateValueRepository.save(referenceInterestRateValue);

        // Change Documents
        changeDocumentService.createChangeDocument(
                referenceInterestValueResource.getId(),referenceInterestValueResource.getId().toString(),referenceInterestValueResource.getId().toString(),
                referenceInterestValueResource.getId().toString(),
                existingObject,
                referenceInterestValueResource,
                "Updated",
                username,
                "ReferenceInterestRateValue", "Header");

        return referenceInterestRateValue;
    }

    @Override
    public ReferenceInterestRateValue delete(ReferenceInterestValueResource referenceInterestValueResource, String username) throws Exception {

        ReferenceInterestRateValue referenceInterestRateValue = new ReferenceInterestRateValue();

        referenceInterestRateValue = referenceInterestRateValueRepository.getOne(referenceInterestValueResource.getId());



        referenceInterestRateValue.setCreatedBy(username.toString());
        referenceInterestRateValue.setCreatedOn(LocalDate.now());
        referenceInterestRateValue.setValidFromDate(referenceInterestValueResource.getValidFromDate());
        referenceInterestRateValue.setInterestRate(referenceInterestRateValue.getInterestRate());

        referenceInterestRateValueRepository.save(referenceInterestRateValue);

        // Change Documents
        changeDocumentService.createChangeDocument(
                referenceInterestValueResource.getId(),referenceInterestValueResource.getId().toString(),referenceInterestValueResource.getId().toString(),
                referenceInterestValueResource.getId().toString(),
                null,
                referenceInterestValueResource,
                "Deleted",
                username,
                "ReferenceInterestRateValue", "Header");

        return referenceInterestRateValue;
    }

    @Override
    public ReferenceInterestRateValue processApprovedReferenceInterestValue(ReferenceInterestRateValue referenceInterestValue, String username) throws CloneNotSupportedException {
        referenceInterestValue.setWorkFlowStatusCode(3);
        referenceInterestValue.setWorkFlowStatusDescription("Approved");
        referenceInterestValue.setModificationStatus(0);
        referenceInterestRateValueRepository.save(referenceInterestValue);
        return referenceInterestValue;
    }

    @Override
    public ReferenceInterestRateValue processRejectionReferenceInterestValue(ReferenceInterestRateValue referenceInterestValue, String username) throws CloneNotSupportedException {
        referenceInterestValue.setWorkFlowStatusCode(4);
        referenceInterestValue.setWorkFlowStatusDescription("Rejected");
        referenceInterestValue.setModificationStatus(0);
        referenceInterestRateValueRepository.save(referenceInterestValue);
        return referenceInterestValue;
    }
}
