package pfs.lms.enquiry.appraisal.projectlocation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.appraisal.LoanAppraisalRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainLocationDetailService implements IMainLocationDetailService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanAppraisalRepository loanAppraisalRepository;
    private final MainLocationDetailRepository mainLocationDetailRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public MainLocationDetail updateMainLocationDetails(MainLocationDetailResource mainLocationDetailResource, String username) throws CloneNotSupportedException {
        LoanApplication loanApplication = loanApplicationRepository.getOne(mainLocationDetailResource.getLoanApplicationId());
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
        MainLocationDetail mainLocationDetail = mainLocationDetailRepository.findByLoanAppraisalId(loanAppraisal.getId())
                .orElseGet(MainLocationDetail::new);

        boolean createMode = false;
        if (mainLocationDetail.getId() == null) {
            createMode = true;
            mainLocationDetail.setLoanAppraisal(loanAppraisal);
        }

        Object oldMainLocationDetail =   mainLocationDetail.clone();

        mainLocationDetail.setDistrict(mainLocationDetailResource.getDistrict());
        mainLocationDetail.setLocation(mainLocationDetailResource.getLocation());
        mainLocationDetail.setNearestAirport(mainLocationDetailResource.getNearestAirport());
        mainLocationDetail.setNearestAirportDistance(mainLocationDetailResource.getNearestAirportDistance());
        mainLocationDetail.setNearestFunctionalAirport(mainLocationDetailResource.getNearestFunctionalAirport());
        mainLocationDetail.setNearestFunctionalAirportDistance(mainLocationDetailResource.getNearestFunctionalAirportDistance());
        mainLocationDetail.setNearestRailwayStation(mainLocationDetailResource.getNearestRailwayStation());
        mainLocationDetail.setNearestRailwayStationDistance(mainLocationDetailResource.getNearestRailwayStationDistance());
        mainLocationDetail.setNearestSeaport(mainLocationDetailResource.getNearestSeaport());
        mainLocationDetail.setNearestSeaportDistance(mainLocationDetailResource.getNearestSeaportDistance());
        mainLocationDetail.setNearestVillage(mainLocationDetailResource.getNearestVillage());
        mainLocationDetail.setNearestVillageDistance(mainLocationDetailResource.getNearestVillageDistance());
        mainLocationDetail.setRegion(mainLocationDetailResource.getRegion());
        mainLocationDetail.setState(mainLocationDetailResource.getState());
        mainLocationDetail = mainLocationDetailRepository.save(mainLocationDetail);

        if (createMode) {
            // Change Documents for Main Location Details
            changeDocumentService.createChangeDocument(
                    mainLocationDetail.getLoanAppraisal().getId(),
                    mainLocationDetail.getId().toString(),
                    mainLocationDetail.getLoanAppraisal().getId().toString(),
                    mainLocationDetail.getLoanAppraisal().getLoanApplication().getLoanContractId(),
                    null,
                    mainLocationDetail,
                    "Created",
                    username,
                    "Appraisal", "Main Location");

        } else {
            // Change Documents for Main Location Details
            changeDocumentService.createChangeDocument(
                    mainLocationDetail.getLoanAppraisal().getId(),
                    mainLocationDetail.getId().toString(),
                    mainLocationDetail.getLoanAppraisal().getId().toString(),
                    mainLocationDetail.getLoanAppraisal().getLoanApplication().getLoanContractId(),
                    oldMainLocationDetail,
                    mainLocationDetail,
                    "Updated",
                    username,
                    "Appraisal", "Main Location");

        }
        return mainLocationDetail;
    }
}
