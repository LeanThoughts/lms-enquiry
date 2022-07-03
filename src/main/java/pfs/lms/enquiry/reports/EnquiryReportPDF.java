package pfs.lms.enquiry.reports;

import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.resource.LoanApplicationResource;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnquiryReportPDF {

    public ByteArrayOutputStream buildPdfDocument( List<LoanApplicationResource> resources) throws Exception {

        Document doc = new Document(PageSize.A4 , 36, 36, 70, 80);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(doc, stream);

        PDFFooter event = new PDFFooter("Enquiry Report");
        writer.setPageEvent(event);

        doc.open();

        doc.add(new Paragraph(" "));
        doc.add(new Paragraph(" "));

        doc.addTitle("Loan Enquiry Report");

        this.buildEnquiryTable(doc, resources);

        doc.close();
        return stream;
    }

    private Document buildEnquiryTable (Document document, List<LoanApplicationResource> resources) throws DocumentException {

        // Main Header Font - Risk Component
        Font mainHeaderFont = new Font(Font.FontFamily.HELVETICA);
        mainHeaderFont.setColor(BaseColor.WHITE);
        mainHeaderFont.setSize(12);

        //Project Details Table
        //float[] columnWidths = {2,5,5,5,5,5,10,10,5,5,5,5,5,5,10,10};
        float[] columnWidths = {2,5};

        PdfPTable enquiryTable = new PdfPTable(columnWidths);
        enquiryTable.setWidthPercentage(100.0f);

        enquiryTable.setSpacingBefore(20);
        PdfPCell cell1 = new PdfPCell();
        cell1.setBackgroundColor(BaseColor.BLUE.darker().darker().darker().darker());
        cell1.setPhrase(new Phrase("S.No", mainHeaderFont));
        cell1.setColspan(3);
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1.setFixedHeight(20);
        enquiryTable.addCell(cell1);


        cell1 = new PdfPCell();
        cell1.setBackgroundColor(BaseColor.BLUE.darker().darker().darker().darker());
        cell1.setPhrase(new Phrase("Status", mainHeaderFont));
        cell1.setColspan(3);
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1.setFixedHeight(20);
        enquiryTable.addCell(cell1);



        enquiryTable.completeRow();


        document.add(enquiryTable);
        return  document;

    }




}


