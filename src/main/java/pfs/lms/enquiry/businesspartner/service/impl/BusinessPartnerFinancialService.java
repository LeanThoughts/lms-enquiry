package pfs.lms.enquiry.businesspartner.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.appraisal.knowyourcustomer.KnowYourCustomer;
import pfs.lms.enquiry.appraisal.knowyourcustomer.KnowYourCustomerRepository;
import pfs.lms.enquiry.appraisal.loanpartner.LoanPartner;
import pfs.lms.enquiry.appraisal.loanpartner.LoanPartnerRepository;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerBankDetail;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerFinancial;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerIdentification;
import pfs.lms.enquiry.businesspartner.domain.IdentificationCategory;
import pfs.lms.enquiry.businesspartner.repository.BusinessPartnerFinancialRepository;
import pfs.lms.enquiry.businesspartner.repository.BusinessPartnerIdentificationRepository;
import pfs.lms.enquiry.businesspartner.repository.DocumentTypeRepository;
import pfs.lms.enquiry.businesspartner.repository.IdentificationCategoryRepository;
import pfs.lms.enquiry.businesspartner.resource.*;
import pfs.lms.enquiry.businesspartner.service.IBusinessPartnerFinancialService;
import pfs.lms.enquiry.businesspartner.service.IBusinessPartnerIdentificationService;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.repository.CountryRepository;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.repository.PartnerRepository;
import pfs.lms.enquiry.repository.RegionRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BusinessPartnerFinancialService implements IBusinessPartnerFinancialService {

    private final BusinessPartnerFinancialRepository businessPartnerFinancialRepository;
    private final PartnerRepository partnerRepository;
    private final IChangeDocumentService changeDocumentService;

    public BusinessPartnerFinancial create(BusinessPartnerFinancialResource businessPartnerFinancialResource, String username) {
        Partner partner = partnerRepository.findById(businessPartnerFinancialResource.getPartnerId())
                .orElseThrow(() -> new EntityNotFoundException(businessPartnerFinancialResource.getPartnerId().toString()
                + " : Business partner not found"));

        BusinessPartnerFinancial businessPartnerFinancial = new BusinessPartnerFinancial();
        businessPartnerFinancial.setPartner(partner);

        businessPartnerFinancial.setFiscalYear(businessPartnerFinancialResource.getFiscalYear());
        businessPartnerFinancial.setRevenue(businessPartnerFinancialResource.getRevenue());
        businessPartnerFinancial.setNetCashAccruals(businessPartnerFinancialResource.getNetCashAccruals());
        businessPartnerFinancial.setDepreciation(businessPartnerFinancialResource.getDepreciation());
        businessPartnerFinancial.setEbitda(businessPartnerFinancialResource.getEbitda());
        businessPartnerFinancial.setPbt(businessPartnerFinancialResource.getPbt());
        businessPartnerFinancial.setPat(businessPartnerFinancialResource.getPat());
        businessPartnerFinancial.setInterestExpenses(businessPartnerFinancialResource.getInterestExpenses());
        businessPartnerFinancial.setWcstDebt(businessPartnerFinancialResource.getWcstDebt());
        businessPartnerFinancial.setLtDebt(businessPartnerFinancialResource.getLtDebt());
        businessPartnerFinancial.setTotalOutstandingLiabilities(businessPartnerFinancialResource.getTotalOutstandingLiabilities());
        businessPartnerFinancial.setReservesAndSurplus(businessPartnerFinancialResource.getReservesAndSurplus());
        businessPartnerFinancial.setAdjTangibleNetWorth(businessPartnerFinancialResource.getAdjTangibleNetWorth());
        businessPartnerFinancial.setCurrentAssets(businessPartnerFinancialResource.getCurrentAssets());
        businessPartnerFinancial.setInvInSubAsso(businessPartnerFinancialResource.getInvInSubAsso());
        businessPartnerFinancial.setFccbQuasiEquity(businessPartnerFinancialResource.getFccbQuasiEquity());
        businessPartnerFinancial.setCpltd(businessPartnerFinancialResource.getCpltd());
        businessPartnerFinancial.setTotalDebt(businessPartnerFinancialResource.getTotalDebt());
        businessPartnerFinancial.setShareCapital(businessPartnerFinancialResource.getShareCapital());
        businessPartnerFinancial.setTangibleNetWorth(businessPartnerFinancialResource.getTangibleNetWorth());
        businessPartnerFinancial.setCashAndBankBalance(businessPartnerFinancialResource.getCashAndBankBalance());
        businessPartnerFinancial.setCurrentLiabilities(businessPartnerFinancialResource.getCurrentLiabilities());
        businessPartnerFinancial.setNetFixedAssets(businessPartnerFinancialResource.getNetFixedAssets());
        businessPartnerFinancial.setEbitdaMarginPercentage(businessPartnerFinancialResource.getEbitdaMarginPercentage());
        businessPartnerFinancial.setEbitdaInterest(businessPartnerFinancialResource.getEbitdaInterest());
        businessPartnerFinancial.setCashDSCR(businessPartnerFinancialResource.getCashDSCR());
        businessPartnerFinancial.setTotalDebtEbitda(businessPartnerFinancialResource.getTotalDebtEbitda());
        businessPartnerFinancial.setTermDebtEbitda(businessPartnerFinancialResource.getTermDebtEbitda());
        businessPartnerFinancial.setDscr(businessPartnerFinancialResource.getDscr());
        businessPartnerFinancial.setTotalDebtTnw(businessPartnerFinancialResource.getTotalDebtTnw());
        businessPartnerFinancial.setTolTnw(businessPartnerFinancialResource.getTolTnw());
        businessPartnerFinancial.setCreatedAt(LocalTime.now());
        businessPartnerFinancial.setCreatedOn(LocalDate.now());
        businessPartnerFinancial.setCreatedByUserName(username);
        businessPartnerFinancial = businessPartnerFinancialRepository.save(businessPartnerFinancial);

        // Set Partner Workflow Status Code to Updated
        partner.setWorkFlowStatusCode(11);

        changeDocumentService.createChangeDocument(
                businessPartnerFinancial.getId(),
                businessPartnerFinancial.getId().toString(),
                businessPartnerFinancial.getPartner().getId().toString(),
                businessPartnerFinancial.getPartner().getId().toString(),
                null,
                businessPartnerFinancial,
                "Created",
                username,
                "Partner", "BusinessPartnerFinancial");



        return businessPartnerFinancial;
    }

    @Override
    public BusinessPartnerFinancial update(BusinessPartnerFinancialResource businessPartnerFinancialResource, String username) throws CloneNotSupportedException {
        BusinessPartnerFinancial businessPartnerFinancial = businessPartnerFinancialRepository.findById(businessPartnerFinancialResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(businessPartnerFinancialResource.getId().toString()
                + " : Business partner financial not found"));

         Object oldObject = businessPartnerFinancial.clone();

        businessPartnerFinancial.setFiscalYear(businessPartnerFinancialResource.getFiscalYear());
        businessPartnerFinancial.setRevenue(businessPartnerFinancialResource.getRevenue());
        businessPartnerFinancial.setNetCashAccruals(businessPartnerFinancialResource.getNetCashAccruals());
        businessPartnerFinancial.setDepreciation(businessPartnerFinancialResource.getDepreciation());
        businessPartnerFinancial.setEbitda(businessPartnerFinancialResource.getEbitda());
        businessPartnerFinancial.setPbt(businessPartnerFinancialResource.getPbt());
        businessPartnerFinancial.setPat(businessPartnerFinancialResource.getPat());
        businessPartnerFinancial.setInterestExpenses(businessPartnerFinancialResource.getInterestExpenses());
        businessPartnerFinancial.setWcstDebt(businessPartnerFinancialResource.getWcstDebt());
        businessPartnerFinancial.setLtDebt(businessPartnerFinancialResource.getLtDebt());
        businessPartnerFinancial.setTotalOutstandingLiabilities(businessPartnerFinancialResource.getTotalOutstandingLiabilities());
        businessPartnerFinancial.setReservesAndSurplus(businessPartnerFinancialResource.getReservesAndSurplus());
        businessPartnerFinancial.setAdjTangibleNetWorth(businessPartnerFinancialResource.getAdjTangibleNetWorth());
        businessPartnerFinancial.setCurrentAssets(businessPartnerFinancialResource.getCurrentAssets());
        businessPartnerFinancial.setInvInSubAsso(businessPartnerFinancialResource.getInvInSubAsso());
        businessPartnerFinancial.setFccbQuasiEquity(businessPartnerFinancialResource.getFccbQuasiEquity());
        businessPartnerFinancial.setCpltd(businessPartnerFinancialResource.getCpltd());
        businessPartnerFinancial.setTotalDebt(businessPartnerFinancialResource.getTotalDebt());
        businessPartnerFinancial.setShareCapital(businessPartnerFinancialResource.getShareCapital());
        businessPartnerFinancial.setTangibleNetWorth(businessPartnerFinancialResource.getTangibleNetWorth());
        businessPartnerFinancial.setCashAndBankBalance(businessPartnerFinancialResource.getCashAndBankBalance());
        businessPartnerFinancial.setCurrentLiabilities(businessPartnerFinancialResource.getCurrentLiabilities());
        businessPartnerFinancial.setNetFixedAssets(businessPartnerFinancialResource.getNetFixedAssets());
        businessPartnerFinancial.setEbitdaMarginPercentage(businessPartnerFinancialResource.getEbitdaMarginPercentage());
        businessPartnerFinancial.setEbitdaInterest(businessPartnerFinancialResource.getEbitdaInterest());
        businessPartnerFinancial.setCashDSCR(businessPartnerFinancialResource.getCashDSCR());
        businessPartnerFinancial.setTotalDebtEbitda(businessPartnerFinancialResource.getTotalDebtEbitda());
        businessPartnerFinancial.setTermDebtEbitda(businessPartnerFinancialResource.getTermDebtEbitda());
        businessPartnerFinancial.setDscr(businessPartnerFinancialResource.getDscr());
        businessPartnerFinancial.setTotalDebtTnw(businessPartnerFinancialResource.getTotalDebtTnw());
        businessPartnerFinancial.setTolTnw(businessPartnerFinancialResource.getTolTnw());
        businessPartnerFinancial = businessPartnerFinancialRepository.save(businessPartnerFinancial);

       Partner partner = businessPartnerFinancial.getPartner();
        // Set Partner Workflow Status Code to Updated
        partner.setWorkFlowStatusCode(11);
        partnerRepository.save(partner);

        changeDocumentService.createChangeDocument(
                businessPartnerFinancial.getId(),
                businessPartnerFinancial.getId().toString(),
                businessPartnerFinancial.getPartner().getId().toString(),
                businessPartnerFinancial.getPartner().getId().toString(),
                oldObject,
                businessPartnerFinancial,
                "Updated",
                username,
                "Partner", "BusinessPartnerFinancial");


        return businessPartnerFinancial;
    }

    @Override
    public BusinessPartnerFinancial migrate(BusinessPartnerFinancialMigrationResource businessPartnerFinancialMigrationResource, String username) throws CloneNotSupportedException {

        Partner partner = partnerRepository.findByPartyNumber(Integer.parseInt(businessPartnerFinancialMigrationResource.getPartnerId()));
        Boolean update;
        Object oldObject = new Object();

        BusinessPartnerFinancial businessPartnerFinancial =
                businessPartnerFinancialRepository.findByPartnerIdAndFiscalYear(partner.getId(),
                        businessPartnerFinancialMigrationResource.getFiscalYear());
        if (businessPartnerFinancial != null){
            businessPartnerFinancial.setChangedAt(LocalTime.now());
            businessPartnerFinancial.setChangedOn(LocalDate.now());
            businessPartnerFinancial.setChangedByUserName(username);
            oldObject = businessPartnerFinancial.clone();
            update = true;

        }else{
            businessPartnerFinancial.setCreatedAt(LocalTime.now());
            businessPartnerFinancial.setCreatedOn(LocalDate.now());
            businessPartnerFinancial.setCreatedByUserName(username);
            update = false;
        }

        businessPartnerFinancial.setFiscalYear(businessPartnerFinancialMigrationResource.getFiscalYear());
        businessPartnerFinancial.setRevenue(businessPartnerFinancialMigrationResource.getRevenue());
        businessPartnerFinancial.setNetCashAccruals(businessPartnerFinancialMigrationResource.getNetCashAccruals());
        businessPartnerFinancial.setDepreciation(businessPartnerFinancialMigrationResource.getDepreciation());
        businessPartnerFinancial.setEbitda(businessPartnerFinancialMigrationResource.getEbitda());
        businessPartnerFinancial.setPbt(businessPartnerFinancialMigrationResource.getPbt());
        businessPartnerFinancial.setPat(businessPartnerFinancialMigrationResource.getPat());
        businessPartnerFinancial.setInterestExpenses(businessPartnerFinancialMigrationResource.getInterestExpenses());
        businessPartnerFinancial.setWcstDebt(businessPartnerFinancialMigrationResource.getWcstDebt());
        businessPartnerFinancial.setLtDebt(businessPartnerFinancialMigrationResource.getLtDebt());
        businessPartnerFinancial.setTotalOutstandingLiabilities(businessPartnerFinancialMigrationResource.getTotalOutstandingLiabilities());
        businessPartnerFinancial.setReservesAndSurplus(businessPartnerFinancialMigrationResource.getReservesAndSurplus());
        businessPartnerFinancial.setAdjTangibleNetWorth(businessPartnerFinancialMigrationResource.getAdjTangibleNetWorth());
        businessPartnerFinancial.setCurrentAssets(businessPartnerFinancialMigrationResource.getCurrentAssets());
        businessPartnerFinancial.setInvInSubAsso(businessPartnerFinancialMigrationResource.getInvInSubAsso());
        businessPartnerFinancial.setFccbQuasiEquity(businessPartnerFinancialMigrationResource.getFccbQuasiEquity());
        businessPartnerFinancial.setCpltd(businessPartnerFinancialMigrationResource.getCpltd());
        businessPartnerFinancial.setTotalDebt(businessPartnerFinancialMigrationResource.getTotalDebt());
        businessPartnerFinancial.setShareCapital(businessPartnerFinancialMigrationResource.getShareCapital());
        businessPartnerFinancial.setTangibleNetWorth(businessPartnerFinancialMigrationResource.getTangibleNetWorth());
        businessPartnerFinancial.setCashAndBankBalance(businessPartnerFinancialMigrationResource.getCashAndBankBalance());
        businessPartnerFinancial.setCurrentLiabilities(businessPartnerFinancialMigrationResource.getCurrentLiabilities());
        businessPartnerFinancial.setNetFixedAssets(businessPartnerFinancialMigrationResource.getNetFixedAssets());
        businessPartnerFinancial.setEbitdaMarginPercentage(businessPartnerFinancialMigrationResource.getEbitdaMarginPercentage());
        businessPartnerFinancial.setEbitdaInterest(businessPartnerFinancialMigrationResource.getEbitdaInterest());
        businessPartnerFinancial.setCashDSCR(businessPartnerFinancialMigrationResource.getCashDSCR());
        businessPartnerFinancial.setTotalDebtEbitda(businessPartnerFinancialMigrationResource.getTotalDebtEbitda());
        businessPartnerFinancial.setTermDebtEbitda(businessPartnerFinancialMigrationResource.getTermDebtEbitda());
        businessPartnerFinancial.setDscr(businessPartnerFinancialMigrationResource.getDscr());
        businessPartnerFinancial.setTotalDebtTnw(businessPartnerFinancialMigrationResource.getTotalDebtTnw());
        businessPartnerFinancial.setTolTnw(businessPartnerFinancialMigrationResource.getTolTnw());

        if (update == true) {
            changeDocumentService.createChangeDocument(
                    businessPartnerFinancial.getId(),
                    businessPartnerFinancial.getId().toString(),
                    businessPartnerFinancial.getPartner().getId().toString(),
                    businessPartnerFinancial.getPartner().getId().toString(),
                    oldObject,
                    businessPartnerFinancial,
                    "Updated",
                    username,
                    "Partner", "BusinessPartnerFinancial");
        } else {
            changeDocumentService.createChangeDocument(
                    businessPartnerFinancial.getId(),
                    businessPartnerFinancial.getId().toString(),
                    businessPartnerFinancial.getPartner().getId().toString(),
                    businessPartnerFinancial.getPartner().getId().toString(),
                    oldObject,
                    businessPartnerFinancial,
                    "Created",
                    username,
                    "Partner", "BusinessPartnerFinancial");
        }

        return businessPartnerFinancial;

    }

    }
