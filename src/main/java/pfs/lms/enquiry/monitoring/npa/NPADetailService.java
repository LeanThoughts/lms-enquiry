package pfs.lms.enquiry.monitoring.npa;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class NPADetailService implements INPADetailService {

    private final NPARepository npaRepository;
    private final NPADetailRepository npaDetailRepository;

    @Override
    public NPADetail saveNPADetail(NPADetailResource resource, String username) {
        NPA npa = npaRepository.getOne(UUID.fromString(resource.getNpaId()));
        NPADetail npaDetail = new NPADetail();
        npaDetail.setNpa(npa);
        npaDetail.setLoanNumber(resource.getNpaDetail().getLoanNumber());

        List<NPADetail> npaDetails = npaDetailRepository.findByNpaOOrderByLineItemNumberDesc(npa);
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
        return npaDetail;
    }

    @Override
    public NPADetail updateNPADetail(NPADetailResource resource, String username) throws CloneNotSupportedException {
        NPADetail npaDetail = npaDetailRepository
                .findById(resource.getNpaDetail().getId())
                .orElseThrow(() -> new EntityNotFoundException(resource.getNpaDetail().getId().toString()));
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
        return npaDetail;
    }

    @Override
    public List<NPADetail> getNPADetail(String npaId, String username) {
        NPA npa = npaRepository.getOne(UUID.fromString(npaId));
        return npaDetailRepository.findByNpaOOrderByLineItemNumberDesc(npa);
    }
}
