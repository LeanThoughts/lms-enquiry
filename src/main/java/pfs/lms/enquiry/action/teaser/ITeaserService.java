package pfs.lms.enquiry.action.teaser;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import pfs.lms.enquiry.action.projectproposal.ProjectProposal;
import pfs.lms.enquiry.action.teaser.TeaserResource;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.UUID;

public interface ITeaserService {

    SXSSFWorkbook generateTeaserExcel(HttpServletResponse response, TeaserResource teaserResource) throws IOException, ParseException;
    SXSSFWorkbook generateTeaserExcelForProposal(HttpServletResponse response, UUID projectProposalId) throws IOException, ParseException;

}
