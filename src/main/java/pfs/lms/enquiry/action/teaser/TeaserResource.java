package pfs.lms.enquiry.action.teaser;

import lombok.Getter;
import lombok.Setter;
import pfs.lms.enquiry.action.EnquiryAction;
import pfs.lms.enquiry.action.otherdetail.OtherDetail;
import pfs.lms.enquiry.action.projectproposal.ProjectProposal;
import pfs.lms.enquiry.action.projectproposal.collateraldetail.CollateralDetail;
import pfs.lms.enquiry.action.projectproposal.dealguaranteetimeline.DealGuaranteeTimeline;
import pfs.lms.enquiry.action.projectproposal.financials.PromoterBorrowerFinancial;
import pfs.lms.enquiry.action.projectproposal.otherdetailsdocument.OtherDetailsDocument;
import pfs.lms.enquiry.action.projectproposal.projectcost.ProjectCost;
import pfs.lms.enquiry.action.projectproposal.projectdetail.ProjectDetail;
import pfs.lms.enquiry.action.projectproposal.projectproposalotherdetail.ProjectProposalOtherDetail;
import pfs.lms.enquiry.action.projectproposal.shareholder.ShareHolder;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.monitoring.borrowerfinancials.BorrowerFinancials;

import java.util.List;

@Getter
@Setter
public class TeaserResource {
    LoanApplication loanApplication;
    Partner partner;

    EnquiryAction enquiryAction;

    ProjectProposal projectProposal;
    ProjectDetail               projectDetail;
    ProjectCost                 projectCost;
    ProjectProposalOtherDetail  projectProposalOtherDetail;
    List<PromoterBorrowerFinancial> promoterBorrowerFinancials;
    List<CollateralDetail>          collateralDetails;
    List<ShareHolder>               shareHolders;
    List<OtherDetailsDocument>      otherDetailsDocuments;
    DealGuaranteeTimeline           dealGuaranteeTimeline;

}
