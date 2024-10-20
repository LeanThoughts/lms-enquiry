package pfs.lms.enquiry.applicationfee.invoice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MeetingNumber {
    private String meetingNumber;
    private String moduleName;

    private boolean selected;
}
