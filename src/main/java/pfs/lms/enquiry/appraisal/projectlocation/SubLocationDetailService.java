package pfs.lms.enquiry.appraisal.projectlocation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.appraisal.LoanAppraisalRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.impl.ChangeDocumentService;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubLocationDetailService implements ISubLocationDetailService {

    private final ChangeDocumentService changeDocumentService;
    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanAppraisalRepository loanAppraisalRepository;
    private final SubLocationDetailRepository subLocationDetailRepository;

    @Override
    public SubLocationDetail createSubLocation(SubLocationDetailResource subLocationDetailResource, String username)
            throws CloneNotSupportedException {

        LoanApplication loanApplication = loanApplicationRepository.getOne(subLocationDetailResource.getLoanApplicationId());
        LoanAppraisal loanAppraisal = loanAppraisalRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    LoanAppraisal obj = new LoanAppraisal();
                    obj.setLoanApplication(loanApplication);
                    obj.setLoanContractId(loanApplication.getLoanContractId());
                    obj = loanAppraisalRepository.save(obj);

                    // Change Documents for Appraisal Header
                    changeDocumentService.createChangeDocument(
                            obj.getId(),
                            obj.getId().toString(),
                            obj.getId().toString(),
                            loanApplication.getLoanContractId(),
                            null,
                            obj,
                            "Created",
                            username,
                            "Appraisal", "Header");
                    return obj;
                });

        SubLocationDetail subLocationDetail = new SubLocationDetail();
        subLocationDetail.setLoanAppraisal(loanAppraisal);
        subLocationDetail.setSerialNumber(subLocationDetailRepository.findByLoanAppraisalId(loanAppraisal.getId()).size() + 1);
        subLocationDetail.setDistrict(subLocationDetailResource.getDistrict());
        subLocationDetail.setLocation(subLocationDetailResource.getLocation());
        subLocationDetail.setNearestAirport(subLocationDetailResource.getNearestAirport());
        subLocationDetail.setNearestAirportDistance(subLocationDetailResource.getNearestAirportDistance());
        subLocationDetail.setNearestFunctionalAirport(subLocationDetailResource.getNearestFunctionalAirport());
        subLocationDetail.setNearestFunctionalAirportDistance(subLocationDetailResource.getNearestFunctionalAirportDistance());
        subLocationDetail.setNearestRailwayStation(subLocationDetailResource.getNearestRailwayStation());
        subLocationDetail.setNearestRailwayStationDistance(subLocationDetailResource.getNearestRailwayStationDistance());
        subLocationDetail.setNearestSeaport(subLocationDetailResource.getNearestSeaport());
        subLocationDetail.setNearestSeaportDistance(subLocationDetailResource.getNearestSeaportDistance());
        subLocationDetail.setNearestVillage(subLocationDetailResource.getNearestVillage());
        subLocationDetail.setNearestVillageDistance(subLocationDetailResource.getNearestVillageDistance());
        subLocationDetail.setRegion(subLocationDetailResource.getRegion());
        subLocationDetail.setState(subLocationDetailResource.getState());
        subLocationDetail = subLocationDetailRepository.save(subLocationDetail);

        // Change Documents for Sub Location Details
        changeDocumentService.createChangeDocument(
                subLocationDetail.getLoanAppraisal().getId(),
                subLocationDetail.getId().toString(),
                subLocationDetail.getLoanAppraisal().getId().toString(),
                subLocationDetail.getLoanAppraisal().getLoanApplication().getLoanContractId(),
                null,
                subLocationDetail,
                "Created",
                username,
                "Appraisal", "Sub Location Detail");

        return subLocationDetail;
    }

    @Override
    public SubLocationDetail updateSubLocation(SubLocationDetailResource subLocationDetailResource, String username)
            throws CloneNotSupportedException {

        SubLocationDetail subLocationDetail = subLocationDetailRepository.findById(subLocationDetailResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(subLocationDetailResource.getId().toString()));

        SubLocationDetail oldSubLocationDetail = (SubLocationDetail) subLocationDetail.clone();


        subLocationDetail.setDistrict(subLocationDetailResource.getDistrict());
        subLocationDetail.setLocation(subLocationDetailResource.getLocation());
        subLocationDetail.setNearestAirport(subLocationDetailResource.getNearestAirport());
        subLocationDetail.setNearestAirportDistance(subLocationDetailResource.getNearestAirportDistance());
        subLocationDetail.setNearestFunctionalAirport(subLocationDetailResource.getNearestFunctionalAirport());
        subLocationDetail.setNearestFunctionalAirportDistance(subLocationDetailResource.getNearestFunctionalAirportDistance());
        subLocationDetail.setNearestRailwayStation(subLocationDetailResource.getNearestRailwayStation());
        subLocationDetail.setNearestRailwayStationDistance(subLocationDetailResource.getNearestRailwayStationDistance());
        subLocationDetail.setNearestSeaport(subLocationDetailResource.getNearestSeaport());
        subLocationDetail.setNearestSeaportDistance(subLocationDetailResource.getNearestSeaportDistance());
        subLocationDetail.setNearestVillage(subLocationDetailResource.getNearestVillage());
        subLocationDetail.setNearestVillageDistance(subLocationDetailResource.getNearestVillageDistance());
        subLocationDetail.setRegion(subLocationDetailResource.getRegion());
        subLocationDetail.setState(subLocationDetailResource.getState());
        subLocationDetail = subLocationDetailRepository.save(subLocationDetail);

        changeDocumentService.createChangeDocument(
                subLocationDetail.getLoanAppraisal().getId(),
                subLocationDetail.getId().toString(),
                subLocationDetail.getLoanAppraisal().getId().toString(),
                subLocationDetail.getLoanAppraisal().getLoanApplication().getLoanContractId(),
                oldSubLocationDetail,
                subLocationDetail,
                "Updated",
                username,
                "Appraisal", "Sub Location Detail");

        return subLocationDetail;
    }
}
