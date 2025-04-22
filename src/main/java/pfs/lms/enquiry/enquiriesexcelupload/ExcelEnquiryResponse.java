package pfs.lms.enquiry.enquiriesexcelupload;

import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ExcelEnquiryResponse {
    int savedCount;
    List<ExcelEnquiry> enquiries;
}
