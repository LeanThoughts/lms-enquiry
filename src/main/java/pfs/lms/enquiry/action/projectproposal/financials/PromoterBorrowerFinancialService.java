package pfs.lms.enquiry.action.projectproposal.financials;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.action.projectproposal.ProjectProposal;
import pfs.lms.enquiry.action.projectproposal.ProjectProposalRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PromoterBorrowerFinancialService implements IPromoterBorrowerFinancialService {

    private final IChangeDocumentService changeDocumentService;

    private final ProjectProposalRepository projectProposalRepository;
    private final PromoterBorrowerFinancialRepository promoterBorrowerFinancialRepository;

    @Override
    public PromoterBorrowerFinancial create(PromoterBorrowerFinancialResource resource, String username) {
        ProjectProposal projectProposal = projectProposalRepository.getOne(resource.getProjectProposalId());
        PromoterBorrowerFinancial promoterBorrowerFinancial = new PromoterBorrowerFinancial();
        promoterBorrowerFinancial.setProjectProposal(projectProposal);
        promoterBorrowerFinancial.setFiscalPeriod(resource.getFiscalPeriod());
        promoterBorrowerFinancial.setRevenue(resource.getRevenue());
        promoterBorrowerFinancial.setEbitda(resource.getEbitda());
        promoterBorrowerFinancial.setDepreciation(resource.getDepreciation());
        promoterBorrowerFinancial.setInterestExpense(resource.getInterestExpense());
        promoterBorrowerFinancial.setPbt(resource.getPbt());
        promoterBorrowerFinancial.setPat(resource.getPat());
        promoterBorrowerFinancial.setNetCashAccruals(resource.getNetCashAccruals());
        promoterBorrowerFinancial.setWcDebt(resource.getWcDebt());
        promoterBorrowerFinancial.setCpltd(resource.getCpltd());
        promoterBorrowerFinancial.setLtDebt(resource.getLtDebt());
        promoterBorrowerFinancial.setTotalDebt(resource.getTotalDebt());
        promoterBorrowerFinancial.setTotalOutstandingLiabilities(resource.getTotalOutstandingLiabilities());
        promoterBorrowerFinancial.setShareCapital(resource.getShareCapital());
        promoterBorrowerFinancial.setReservesAndSurplus(resource.getReservesAndSurplus());
        promoterBorrowerFinancial.setTangibleNetWorth(resource.getTangibleNetWorth());
        promoterBorrowerFinancial.setAdjustedTangibleNetWorth(resource.getAdjustedTangibleNetWorth());
        promoterBorrowerFinancial.setCashAndBankBalance(resource.getCashAndBankBalance());
        promoterBorrowerFinancial.setCurrentAssets(resource.getCurrentAssets());
        promoterBorrowerFinancial.setCurrentLiabilities(resource.getCurrentLiabilities());
        promoterBorrowerFinancial.setSubAsso(resource.getSubAsso());
        promoterBorrowerFinancial.setNetFixedAssets(resource.getNetFixedAssets());
        promoterBorrowerFinancial.setQuasiEquity(resource.getQuasiEquity());
        promoterBorrowerFinancial.setEbitdaMarginPercentage(resource.getEbitdaMarginPercentage());
        promoterBorrowerFinancial.setEbitdaInterest(resource.getEbitdaInterest());
        promoterBorrowerFinancial.setCashDscr(resource.getCashDscr());
        promoterBorrowerFinancial.setDscr(resource.getDscr());
        promoterBorrowerFinancial.setTotalDebtEbitda(resource.getTotalDebtEbitda());
        promoterBorrowerFinancial.setTermDebtEbitda(resource.getTermDebtEbitda());
        promoterBorrowerFinancial.setTotalDebtTnw(resource.getTotalDebtTnw());
        promoterBorrowerFinancial.setTnw(resource.getTnw());
        promoterBorrowerFinancial.setCurrentRatio(resource.getCurrentRatio());
        promoterBorrowerFinancial = promoterBorrowerFinancialRepository.save(promoterBorrowerFinancial);

        // Change Documents for Project Proposal
//        changeDocumentService.createChangeDocument(
//                projectProposal.getEnquiryAction().getId(),
//                projectProposal.getId().toString(),
//                projectProposal.getEnquiryAction().getId().toString(),
//                projectProposal.getEnquiryAction().getLoanApplication().getLoanContractId(),
//                null,
//                projectProposal,
//                "Created",
//                username,
//                "EnquiryAction", "Project Proposal" );

        return promoterBorrowerFinancial;
    }

    @Override
    public PromoterBorrowerFinancial update(PromoterBorrowerFinancialResource resource, String username)
            throws CloneNotSupportedException {

        PromoterBorrowerFinancial promoterBorrowerFinancial =
                promoterBorrowerFinancialRepository.findById(resource.getId())
                        .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

        // Object oldRejectByPFS = projectProposal.clone();

        promoterBorrowerFinancial.setFiscalPeriod(resource.getFiscalPeriod());
        promoterBorrowerFinancial.setRevenue(resource.getRevenue());
        promoterBorrowerFinancial.setEbitda(resource.getEbitda());
        promoterBorrowerFinancial.setDepreciation(resource.getDepreciation());
        promoterBorrowerFinancial.setInterestExpense(resource.getInterestExpense());
        promoterBorrowerFinancial.setPbt(resource.getPbt());
        promoterBorrowerFinancial.setPat(resource.getPat());
        promoterBorrowerFinancial.setNetCashAccruals(resource.getNetCashAccruals());
        promoterBorrowerFinancial.setWcDebt(resource.getWcDebt());
        promoterBorrowerFinancial.setCpltd(resource.getCpltd());
        promoterBorrowerFinancial.setLtDebt(resource.getLtDebt());
        promoterBorrowerFinancial.setTotalDebt(resource.getTotalDebt());
        promoterBorrowerFinancial.setTotalOutstandingLiabilities(resource.getTotalOutstandingLiabilities());
        promoterBorrowerFinancial.setShareCapital(resource.getShareCapital());
        promoterBorrowerFinancial.setReservesAndSurplus(resource.getReservesAndSurplus());
        promoterBorrowerFinancial.setTangibleNetWorth(resource.getTangibleNetWorth());
        promoterBorrowerFinancial.setAdjustedTangibleNetWorth(resource.getAdjustedTangibleNetWorth());
        promoterBorrowerFinancial.setCashAndBankBalance(resource.getCashAndBankBalance());
        promoterBorrowerFinancial.setCurrentAssets(resource.getCurrentAssets());
        promoterBorrowerFinancial.setCurrentLiabilities(resource.getCurrentLiabilities());
        promoterBorrowerFinancial.setSubAsso(resource.getSubAsso());
        promoterBorrowerFinancial.setNetFixedAssets(resource.getNetFixedAssets());
        promoterBorrowerFinancial.setQuasiEquity(resource.getQuasiEquity());
        promoterBorrowerFinancial.setEbitdaMarginPercentage(resource.getEbitdaMarginPercentage());
        promoterBorrowerFinancial.setEbitdaInterest(resource.getEbitdaInterest());
        promoterBorrowerFinancial.setCashDscr(resource.getCashDscr());
        promoterBorrowerFinancial.setDscr(resource.getDscr());
        promoterBorrowerFinancial.setTotalDebtEbitda(resource.getTotalDebtEbitda());
        promoterBorrowerFinancial.setTermDebtEbitda(resource.getTermDebtEbitda());
        promoterBorrowerFinancial.setTotalDebtTnw(resource.getTotalDebtTnw());
        promoterBorrowerFinancial.setTnw(resource.getTnw());
        promoterBorrowerFinancial.setCurrentRatio(resource.getCurrentRatio());
        promoterBorrowerFinancial = promoterBorrowerFinancialRepository.save(promoterBorrowerFinancial);

        // Change Documents for Project Proposal
//        changeDocumentService.createChangeDocument(
//                projectProposal.getEnquiryAction().getId(),
//                projectProposal.getId().toString(),
//                projectProposal.getEnquiryAction().getId().toString(),
//                projectProposal.getEnquiryAction().getLoanApplication().getLoanContractId(),
//                oldRejectByPFS,
//                projectProposal,
//                "Updated",
//                username,
//                "EnquiryAction", "Project Proposal" );

        return promoterBorrowerFinancial;
    }
}
