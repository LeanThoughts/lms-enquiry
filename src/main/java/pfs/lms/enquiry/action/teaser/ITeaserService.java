package pfs.lms.enquiry.action.teaser;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import pfs.lms.enquiry.action.teaser.TeaserResource;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

public interface ITeaserService {

    SXSSFWorkbook generateTeaserExcel(HttpServletResponse response, TeaserResource teaserResource) throws IOException, ParseException;
}
