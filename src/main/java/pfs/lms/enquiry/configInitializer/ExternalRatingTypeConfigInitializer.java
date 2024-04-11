package pfs.lms.enquiry.configInitializer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.domain.*;
import pfs.lms.enquiry.repository.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sajeev on 14-May-21.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ExternalRatingTypeConfigInitializer implements CommandLineRunner {


    @Autowired
    ExternalRatingTypeRepository externalRatingTypeRepository;

    @Override
    public void run(String... strings) throws Exception {
        ExternalRatingType externalRatingType = new ExternalRatingType();
        List<ExternalRatingType> externalRatingTypeList = new ArrayList<>();

        externalRatingType = new ExternalRatingType( "1","BWR A");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "2","BWR A-");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "3","BWR A- (CE)");
        externalRatingTypeList.add(externalRatingType);

        externalRatingType = new ExternalRatingType( "4","CARE A");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "5","CARE A-");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "6","CARE A-/Stable");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "7","CARE A+");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "8","CARE B+");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "9","CARE BB");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "10","CARE BB-");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "11","CARE BB+");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "12","CARE BBB");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "13","CARE BBB-");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "14","CARE BBB+");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "15","CARE BBB+/Stable");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "16","CARE D");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "17","CRISIL A");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "18","CRISIL A-");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "19","CRISIL A/Negative");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "20","CRISIL AA-");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "21","CRISIL B");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "22","CRISIL BB");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "23","CRISIL BB+");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "24","CRISIL BBB");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "25","CRISIL BBB-");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "26","CRISIL BBB-/Stable");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "27","ICRA A");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "28","ICRA A-");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "29","ICRA A-/Positive");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "30","ICRA A+");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "31","ICRA BBB");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "32","ICRA BBB-");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "33","ICRA BBB+");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "34","ICRA D");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "35","IND A");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "36","IND A-");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "37","IND A-/Stable");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "38","IND A+");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "39","IND AA- /Stable");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "40","IND AA-/Positive");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "41","IND C");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "42","IND D");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "43","Informics BBB-");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "44","BWR A+");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "45","BWR BB");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "46","IND BBB (India Ratings BBB)");
        externalRatingTypeList.add(externalRatingType);
        externalRatingType = new ExternalRatingType( "99","Not Available");
        externalRatingTypeList.add(externalRatingType);


        for (ExternalRatingType externalRatingTypeItem:externalRatingTypeList) {
            ExternalRatingType externalRatingTypeExisting = externalRatingTypeRepository.getExternalRatingTypeByCode(externalRatingTypeItem.getCode());
            if (externalRatingTypeExisting == null) {
                externalRatingType = new ExternalRatingType( externalRatingTypeItem.getCode(), externalRatingTypeItem.getValue());
                externalRatingTypeRepository.save(externalRatingTypeItem);
            }
        }





    }

}
