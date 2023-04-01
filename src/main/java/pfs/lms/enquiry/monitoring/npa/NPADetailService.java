package pfs.lms.enquiry.monitoring.npa;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class NPADetailService implements INPADetailService {

    private final NPARepository npaRepository;
    private final NPADetailRepository npaDetailRepository;

    private final IChangeDocumentService changeDocumentService;

    @Override
    public NPADetail saveNPADetail(NPADetailResource resource, String username) {
        NPA npa = npaRepository.getOne(UUID.fromString(resource.getNpaId()));
        NPADetail npaDetail = new NPADetail();
        npaDetail.setNpa(npa);
        npaDetail.setLoanNumber(resource.getNpaDetail().getLoanNumber());

        //List<NPADetail> npaDetails = npaDetailRepository.findByNpaOOrderByLineItemNumberDesc(npa);
        List<NPADetail> npaDetails = npaDetailRepository.findByNpa(npa);
        npaDetail.setLineItemNumber(npaDetails.size() + 1);

        npaDetail.setNpaAssetClass(resource.getNpaDetail().getNpaAssetClass());
        npaDetail.setAssetClassificationChangeDate(resource.getNpaDetail().getAssetClassificationChangeDate());
        npaDetail.setProvisionDate(resource.getNpaDetail().getProvisionDate());
        npaDetail.setPercentageSecured(resource.getNpaDetail().getPercentageSecured());
        npaDetail.setPercentageUnsecured(resource.getNpaDetail().getPercentageUnsecured());
        npaDetail.setAbsoluteValue(resource.getNpaDetail().getAbsoluteValue());
        npaDetail.setLoanAssetValue(resource.getNpaDetail().getLoanAssetValue());
        npaDetail.setSecuredLoanAsset(resource.getNpaDetail().getSecuredLoanAsset());
        npaDetail.setUnsecuredLoanAsset(resource.getNpaDetail().getUnsecuredLoanAsset());
        npaDetail.setNpaProvisionValue(resource.getNpaDetail().getNpaProvisionValue());
        npaDetail.setNetAssetValue(resource.getNpaDetail().getNetAssetValue());
        npaDetail.setRemarks(resource.getNpaDetail().getRemarks());
        npaDetail = npaDetailRepository.save(npaDetail);

        // Change Documents for NPA Detail
        changeDocumentService.createChangeDocument(
                npa.getLoanMonitor().getId(),
                npaDetail.getId().toString(),
                null,
                npa.getLoanMonitor().getLoanApplication().getLoanContractId(),
                null,
                npaDetail,
                "Created",
                username,
                "Monitoring", "NPA Detail");

        return npaDetail;
    }

    @Override
    public NPADetail updateNPADetail(NPADetailResource resource, String username) throws CloneNotSupportedException {
        NPADetail npaDetail = npaDetailRepository
                .findById(resource.getNpaDetail().getId())
                .orElseThrow(() -> new EntityNotFoundException(resource.getNpaDetail().getId().toString()));

        NPADetail oldNPADetail = (NPADetail) npaDetail.clone();

        npaDetail.setLoanNumber(resource.getNpaDetail().getLoanNumber());
        npaDetail.setLineItemNumber(resource.getNpaDetail().getLineItemNumber());
        npaDetail.setNpaAssetClass(resource.getNpaDetail().getNpaAssetClass());
        npaDetail.setAssetClassificationChangeDate(resource.getNpaDetail().getAssetClassificationChangeDate());
        npaDetail.setProvisionDate(resource.getNpaDetail().getProvisionDate());
        npaDetail.setPercentageSecured(resource.getNpaDetail().getPercentageSecured());
        npaDetail.setPercentageUnsecured(resource.getNpaDetail().getPercentageUnsecured());
        npaDetail.setAbsoluteValue(resource.getNpaDetail().getAbsoluteValue());
        npaDetail.setLoanAssetValue(resource.getNpaDetail().getLoanAssetValue());
        npaDetail.setSecuredLoanAsset(resource.getNpaDetail().getSecuredLoanAsset());
        npaDetail.setUnsecuredLoanAsset(resource.getNpaDetail().getUnsecuredLoanAsset());
        npaDetail.setNpaProvisionValue(resource.getNpaDetail().getNpaProvisionValue());
        npaDetail.setNetAssetValue(resource.getNpaDetail().getNetAssetValue());
        npaDetail.setRemarks(resource.getNpaDetail().getRemarks());
        npaDetail = npaDetailRepository.save(npaDetail);

        // Change Documents for NPA Detail
        changeDocumentService.createChangeDocument(
                npaDetail.getNpa().getLoanMonitor().getId(),
                npaDetail.getId().toString(),
                null,
                npaDetail.getNpa().getLoanMonitor().getLoanApplication().getLoanContractId(),
                oldNPADetail,
                npaDetail,
                "Created",
                username,
                "Monitoring", "NPA Detail");

        return npaDetail;
    }

    @Override
    public List<NPADetail> getNPADetail(String npaId) {
        NPA npa = npaRepository.getOne(UUID.fromString(npaId));
        return npaDetailRepository.findByNpaOrderByLineItemNumberDesc(npa);
        //return npaDetailRepository.findByNpa(npa);

    }

    @Override
    public NPADetail deleteNPADetail(UUID npaDetailId, String username) {
        NPADetail npaDetail = npaDetailRepository.getOne(npaDetailId);
        UUID npaId = npaDetail.getNpa().getId();

        LoanMonitor loanMonitor = npaDetail.getNpa().getLoanMonitor();

        // Change Documents for  NPA Detail Delete
        changeDocumentService.createChangeDocument(
                loanMonitor.getId(),
                npaDetail.getId().toString(),
                loanMonitor.getId().toString(),
                loanMonitor.getLoanApplication().getLoanContractId(),
                null,
                npaDetail,
                "Deleted",
                username,
                "Appraisal", "NPA Detail");

        npaDetailRepository.delete(npaDetail);
        updateSerialNumbers(npaId);

        return npaDetail;
    }

    private void updateSerialNumbers(UUID npaId) {
        NPA npa = npaRepository.getOne(npaId);
        List<NPADetail> npaDetails = npaDetailRepository.findByNpaOrderByLineItemNumberDesc(npa);
        int size = npaDetails.size();
        for(NPADetail npaDetail : npaDetails) {
            if (npaDetail.getLineItemNumber() != size) {
                npaDetail.setLineItemNumber(size);
                npaDetailRepository.save(npaDetail);
            }
            size--;
        }
    }
}
