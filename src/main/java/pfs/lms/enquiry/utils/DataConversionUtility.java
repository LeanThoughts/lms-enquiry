package pfs.lms.enquiry.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;

/**
 * Created by sajeev on 14-Jun-21.
 */
public class DataConversionUtility {


    public  String convertDateToSAPFormat (LocalDate localDate) throws ParseException {

        String dateOut  = new String();

        Long epochSeconds = localDate.atTime(LocalTime.MIDNIGHT).atZone(ZoneId.systemDefault()).toEpochSecond();

        // create a LocalTime object
        LocalTime time
                = LocalTime.parse("20:00:00");
        // create ZoneId
        ZoneOffset zone = ZoneOffset.of("Z");


        dateOut = "\\/Date(" + epochSeconds.toString() + ")\\/";
        //return dateOut;


        if (localDate != null) {
            dateOut = localDate.toString();
            dateOut = dateOut + " 18:01:01";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date datedParsed = sdf.parse(dateOut);
            long millis = datedParsed.getTime();
            dateOut = "\\/Date(" + millis + ")\\/";
            return dateOut;
        }
        else {
            return null;
        }
    }
}
