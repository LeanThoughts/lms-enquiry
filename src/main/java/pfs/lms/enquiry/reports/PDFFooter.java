package pfs.lms.enquiry.reports;

import com.itextpdf.text.pdf.PdfPageEventHelper;

import java.util.Date;

public class PDFFooter extends PdfPageEventHelper {

    private String reportName;
    public PDFFooter(String reportName) {
        this.reportName = reportName;
        }

}
