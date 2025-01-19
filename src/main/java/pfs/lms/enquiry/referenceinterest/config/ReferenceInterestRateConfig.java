package pfs.lms.enquiry.referenceinterest.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.referenceinterest.domain.ReferenceInterestRate;
import pfs.lms.enquiry.referenceinterest.domain.ReferenceInterestRateValue;
import pfs.lms.enquiry.referenceinterest.repository.ReferenceInterestRateRepository;
import pfs.lms.enquiry.referenceinterest.repository.ReferenceInterestRateValueRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReferenceInterestRateConfig implements CommandLineRunner {

    @Autowired
    private ReferenceInterestRateRepository referenceInterestRateRepository;
    @Autowired
    private ReferenceInterestRateValueRepository referenceInterestRateValueRepository;

    @Override
    public void run(String... args) throws Exception {


        ReferenceInterestRate referenceInterestRate = referenceInterestRateRepository.findByCode("LMS_PFS_BR");
        if (referenceInterestRate == null) {
            referenceInterestRate = new ReferenceInterestRate(null, "LMS_PFS_BR", "PFS Base Rate");
            referenceInterestRateRepository.save(referenceInterestRate);
        }
          referenceInterestRate = referenceInterestRateRepository.findByCode("LMS_PFS_BR");
        if (referenceInterestRate == null) {
            referenceInterestRate = new ReferenceInterestRate(null, "LMS_PFS_BR", "PFS Base Rate");
            referenceInterestRateRepository.save(referenceInterestRate);
        }


        ReferenceInterestRateValue r1 = new ReferenceInterestRateValue();
        List<ReferenceInterestValueDataMigResource> referenceInterestValueDataMigResourceList = new ArrayList<>();
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("ANDH1YMCLR","Andhra 1Year MCLR","2019-09-20",8.4));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("ANDH1YMCLR","Andhra 1Year MCLR","2018-09-26",8.7));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("AXIS RATE","AXIS Base Rate","2016-08-20",15.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("AXIS RATE","AXIS Base Rate","2016-05-05",12.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("AXIS RATE","AXIS Base Rate","2013-12-26",13.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("AXIS RATE","AXIS Base Rate","2013-09-25",9.55));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("BOI RATE","Bank of India base rate","2016-07-01",15.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("BOI RATE","Bank of India base rate","2016-04-01",10.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("BOI RATE","Bank of India base rate","2016-03-31",9.7));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("BOI1YRMCLR","BOI 1 YR MCLR","2019-05-23",8.7));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("BOI1YRMCLR","BOI 1 YR MCLR","2019-03-31",8.7));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("BOM6MMCLR","Bank of Maharastra 6 month MCLR","2019-01-01",9.5));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("CANA3MMCLR","Canara Bank 3 month MCLR","2019-11-19",9.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("CANA3MMCLR","Canara Bank 3 month MCLR","2019-08-19",9.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("CANA3MMCLR","Canara Bank 3 month MCLR","2019-06-25",8.5));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("CANA3MMCLR","Canara Bank 3 month MCLR","2019-04-01",8.45));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("CANARA RAT","Canara base rate","2016-03-31",9.65));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("CBI RATE","Central Bank of India base rate","2016-03-31",9.7));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("CBI6MMCLR","Central Bank of India 6 month MCLR","2019-08-02",8.55));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("CENT1YMCLR","Central Bank 1year MCLR","2016-09-14",9.35));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("CORP. RATE","Corporation Bank base rate","2017-04-30",10.5));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("CORP. RATE","Corporation Bank base rate","2016-12-31",8.34));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("CORP. RATE","Corporation Bank base rate","2016-11-08",9.65));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("CORP. RATE","Corporation Bank base rate","2016-10-08",9.65));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("CORP. RATE","Corporation Bank base rate","2016-09-06",9.65));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("CORP. RATE","Corporation Bank base rate","2016-03-31",9.65));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("DEG3MLIBOR","DEG3MLIBOR","2016-03-15",0.63385));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("DENA RATE","Dena Bank base rate","2016-03-31",9.7));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("DENA1YMCLR","Dena Bank 1YR MCLR","2018-10-10",8.6));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("DENA1YMCLR","Dena Bank 1YR MCLR","2016-09-14",9.5));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("DISDM","","1997-01-01",3.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("DISDM","","1992-10-01",12.5));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("DISDM","","1992-06-01",10.5));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("DISDM","","1990-01-01",9.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("HDFC1MMCLR","HDFC I month MCLR","2016-09-02",8.95));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("HDFC3MMCLR","HDFC 3month MCLR","2016-09-30",9.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("ICIC3MMCLR","ICICI 3 Months MCLR","2019-04-26",8.55));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("IFC3MLIBOR","IFC3MLIBOR","2016-01-15",0.622));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("IHC1","Zinsreferenz für verbundene Unternehmen","1999-01-01",0.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("IM_DEM_1","","1997-01-01",10.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("IM_DEM_1","","1990-01-01",10.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("IM_DEM_1","","1990-01-01",10.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("IM_DEM_2","","1997-01-01",10.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("IM_DEM_2","","1990-01-01",10.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("IM_DEM_3","","1997-01-01",10.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("IM_DEM_3","","1990-01-01",10.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("IM_DEM_4","","1997-01-01",10.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("IM_DEM_4","","1990-01-01",10.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("IM_EUR_1","","1999-01-01",10.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("IM_EUR_2","","1999-01-01",10.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("IM_EUR_3","","1999-01-01",10.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("IM_EUR_4","","1999-01-01",10.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("IM_USD_1","","1999-01-01",10.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("IM_USD_2","","1999-01-01",10.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("IM_USD_3","","1999-01-01",10.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("IM_USD_4","The Jammu & Kashmir Bank Limited - OD rate","1999-01-01",10.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("J&K RATE","J&K1YearMCLR","2016-03-31",9.5));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("J&K1YRMCLR","J&K1YearMCLR","2023-09-08",8.9));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("J&K1YRMCLR","J&K3MonthMCLR","2016-08-26",9.4));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("J&K3MRMCLR","J&K3MonthMCLR","2019-09-16",8.2));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("J&K3MRMCLR","J&K3MonthMCLR","2019-06-14",8.35));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("J&K3MRMCLR","Libor 1 Month USD","2019-03-26",8.55));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD01","Libor 1 Month USD","2014-08-29",3.5));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD01","Libor 1 Month USD","2014-03-31",3.5));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD01","Libor 1 Month USD","2014-03-04",3.5));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD01","Libor 1 Month USD","2014-02-28",3.5));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD01","Libor 1 Month USD","2014-01-20",3.5));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD01","Libor 1 Month USD","2014-01-09",3.5));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD01","Libor 1 Month USD","2014-01-08",3.5));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD01","Libor 1 Month USD","2014-01-01",3.5));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD01","Libor 1 Month USD","2013-02-01",3.5));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD01","Libor 3 Month USD","1997-01-01",10.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2020-02-14",1.89363));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2019-12-12",2.001));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2019-11-14",2.1185));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2019-09-16",2.304));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2019-08-15",2.41025));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2019-06-14",2.597));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2019-05-17",2.61088));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2019-03-18",2.788));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2019-02-13",2.78819));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2018-12-12",2.437));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2018-11-15",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2018-09-14",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2018-08-16",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2018-06-15",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2018-05-16",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2018-03-15",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2018-02-13",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2017-12-13",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2017-11-14",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2017-09-15",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2017-08-15",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2017-06-16",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2017-05-16",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2017-03-17",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2017-02-13",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2016-12-14",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2016-11-14",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2016-09-16",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2016-08-15",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2016-06-15",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2016-05-16",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2016-03-15",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2016-02-12",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2015-12-14",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2015-11-12",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2015-09-15",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2015-08-13",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2015-06-15",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2015-05-15",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2015-03-13",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2015-02-13",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2014-12-12",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2014-11-13",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2014-09-15",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2014-08-14",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2014-06-13",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2014-05-16",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2014-03-17",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2014-02-14",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2013-12-12",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2013-11-14",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2013-09-16",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2013-08-15",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2013-06-14",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2013-05-17",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2013-03-14",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2013-02-13",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2012-12-12",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2012-11-15",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2012-09-14",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2012-08-16",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2012-05-14",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2012-02-14",0.69625));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","Libor 3 Month USD","2011-11-14",0.49711));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LIBORUSD03","LMS_AXIS_BR","2011-08-15",0.397));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LMS_AXIS","LMS_AXIS_BR","2017-01-02",9.25));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LMS_AXIS","LMS_AXIS_BR","2016-07-27",9.35));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LMS_AXIS","LMS_AXIS_BR","2016-04-18",9.45));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LMS_AXIS","LMS_ICICI_BR","2015-10-05",9.5));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LMS_ICICI","LMS_IDBI BR","2015-10-05",9.35));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LMS_IDBI","LMS_INDUSIND_BR","2015-10-05",9.75));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LMS_INDUS","LMS_INDUSIND_BR","2017-01-17",10.55));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LMS_INDUS","LMS_KOTAK_BR","2015-10-19",10.6));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LMS_KOTAK","LMS_PFSRR","2015-10-05",9.5));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LMS_PFSRR","LMS_PFSRR","2019-05-08",15.9));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LMS_PFSRR","LMS_PFSRR","2019-02-12",15.65));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LMS_PFSRR","LMS_PFSRR","2018-11-13",15.5));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LMS_PFSRR","LMS_PFSRR","2018-10-31",15.25));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LMS_PFSRR","LMS_PFSRR","2018-09-21",15.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LMS_PFSRR","LMS_PFSRR","2017-06-13",14.75));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LMS_PFSRR","LMS_PFSRR","2016-10-10",15.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LMS_PFSRR","LMS_PFS_BR","2015-10-01",15.25));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LMS_PFS_BR","LMS_PFS_BR","2024-07-04",11.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LMS_PFS_BR","LMS_PFS_BR","2023-12-13",10.75));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LMS_PFS_BR","LMS_PFS_BR","2023-06-01",10.5));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LMS_PFS_BR","LMS_PFS_BR","2023-03-25",10.25));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LMS_PFS_BR","LMS_PFS_BR","2023-02-08",10.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LMS_PFS_BR","LMS_PFS_BR","2021-08-01",9.75));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LMS_PFS_BR","LMS_PFS_BR","2021-03-26",10.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LMS_PFS_BR","LMS_PFS_BR","2020-09-23",10.25));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LMS_PFS_BR","LMS_PFS_BR","2020-07-01",10.5));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LMS_PFS_BR","LMS_PFS_BR","2019-05-08",10.75));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LMS_PFS_BR","LMS_PNB_BR","2019-03-01",10.5));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LMS_PNB","LMS_PNB_MCLR_5YR","2015-10-01",9.6));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LMS_PNB_5M","LMS_SBI_BR","2016-11-09",9.55));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LMS_SBI","LMS_SBI_BR","2017-04-01",9.1));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LMS_SBI","LMS_YES_BR","2015-10-05",9.3));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("LMS_YES","Oriental Bank of Commerce base rate","2017-02-01",10.25));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("OBC RATE","OBC 1YR MCLR","2016-03-31",9.7));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("OBC1YRMCLR","OeEB3MLIBOR","2016-09-23",9.55));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("OEB3MLIBOR","PFS Margin Cost Lending Rate","2016-09-28",0.85294));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("PFS_MCLR","PFS Margin Cost Lending Rate","2024-04-23",9.95));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("PFS_MCLR","PNB Reference Rate","2023-12-13",9.62));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("PNB RATE","","2016-03-31",9.6));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("RBI_REPO_C","","2007-04-01",6.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("RBI_REPO_D","SBI Base Rate","2007-04-01",7.5));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("SBI RATE","South Indian Bank - OD base rate","2016-03-31",9.3));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("SIB RATE","SYN 1 Yr MCLR","2016-03-31",10.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("SYN1YMCLR","Syndicate Bank base rate","2016-09-01",9.55));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("SYNDI RATE","","2016-03-31",9.7));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("UIC","","2007-04-01",7.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("UICDM","","1992-06-01",9.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("UICDM","","1992-03-01",7.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("UICDM","","1992-02-01",6.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("UICDM","","1992-01-01",5.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("UICDM","","1990-01-01",8.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("UID","","2007-04-01",5.5));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("UIDDM","","1997-01-01",4.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("UIDDM","","1992-06-01",20.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("UIDDM","","1992-03-01",15.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("UIDDM","","1992-01-01",10.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("UIDDM","UNION 1 Year MCLR","1990-01-01",11.00));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("UNIO1YMCLR","UNION 3 Month MCLR","2016-09-15",9.4));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("UNIO3MMCLR","UNION 3 Month MCLR","2019-09-19",8.35));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("UNIO3MMCLR","UNION 3 Month MCLR","2019-09-01",8.6));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("UNIO3MMCLR","Union Bank base rate","2019-03-27",8.45));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("UNION RATE","United Bank of India base rate","2016-03-31",9.65));
        referenceInterestValueDataMigResourceList.add(new ReferenceInterestValueDataMigResource("UNITED BAN","0","2016-03-31",9.65));




        for ( ReferenceInterestValueDataMigResource r: referenceInterestValueDataMigResourceList) {

            referenceInterestRate = referenceInterestRateRepository.findByCode(r.getReferenceIntRateType());
            if (referenceInterestRate == null) {
                referenceInterestRate = new ReferenceInterestRate(null, r.referenceIntRateType, r.getDescription());
                referenceInterestRate = referenceInterestRateRepository.save(referenceInterestRate);
            }


            ReferenceInterestRateValue referenceInterestRateValue = new ReferenceInterestRateValue();
            referenceInterestRateValue.setReferenceInterestRate(referenceInterestRate);
            referenceInterestRateValue.setInterestRate(r.getInterestRate());
            LocalDate validFromDate = LocalDate.parse(r.getValidFromDate());
            referenceInterestRateValue.setValidFromDate(validFromDate );
            referenceInterestRateValue.setWorkFlowStatusCode(3);
            referenceInterestRateValue.setWorkFlowStatusDescription("Approved");
            referenceInterestRateValue.setModificationStatus(0);

            ReferenceInterestRateValue existingReferenceInterestRateValue =
                    referenceInterestRateValueRepository.findByReferenceInterestRateIdAndValidFromDate(referenceInterestRate.getId(), validFromDate);
            if (existingReferenceInterestRateValue == null)
                referenceInterestRateValueRepository.save(referenceInterestRateValue);

            //referenceInterestRateValueRepository.findByReferenceInterestRateIdAndValidFromDate(referenceInterestRate.getId(),validFromDate);




        }
    }



}
