package pfs.lms.enquiry.businesspartner.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.businesspartner.domain.IndustrySystem;
import pfs.lms.enquiry.businesspartner.domain.IndustryType;
import pfs.lms.enquiry.businesspartner.repository.IndustrySystemRepository;
import pfs.lms.enquiry.businesspartner.repository.IndustryTypeRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class IndustryTypeConfig implements CommandLineRunner {

    private final IndustryTypeRepository industryTypeRepository;
    private final IndustrySystemRepository industrySystemRepository;

    @Override
    public void run(String... strings) throws Exception {

        IndustryType industryType = new IndustryType();
        IndustrySystem industrySystem = new IndustrySystem();

        industrySystem = industrySystemRepository.findIndustrySystemByCode("0001");

        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("01",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"01","Power",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("02",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"02","Railways",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("03",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"03","Urban Infra",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("04",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"04","Roads",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("05",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"05","Ports",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("06",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"06","Oil & Gas",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("07",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"07","Corporates",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("08",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"08","Infrastructure",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("09",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"09","Others",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("10",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"10","Energy Supply / Distribution",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("11",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"11","Div. Holding comp",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("12",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"12","Raw Materials",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("13",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"13","Precious Metals",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("14",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"14","Financial Services",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("15",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"15","Real Estate",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("21",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"21","Chemical Industry",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("22",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"22","Health",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("23",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"23","Glass",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("24",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"24","Construction Industry",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("25",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"25","Building Supplier",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("26",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"26","Paper and Pulp",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("27",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"27","Timber and Infrastructure",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("31",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"31","Spinning Mill, Weaving Mill and Textile Refinement",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("32",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"32","Apparel",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("41",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"41","Iron and Steel",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("42",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"42","Vehicles",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("43",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"43","Vehicle Supplier",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("44",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"44","Mechanical Engineering",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("45",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"45","Specialized Mechanical Engineering",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("46",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"46","Machine Tool Engineering",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("47",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"47","Aircraft Construction",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("51",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"51","Breweries, Beverages, Tobacco",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("52",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"52","Nutrition",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("61",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"61","Electricals / Electrical Engineering",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("62",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"62","Computers and Data Processing",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("63",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"63","Software",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("64",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"64","Telecommunications",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("71",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"71","Consumer Products",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("72",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"72","Traffic and Transport",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("73",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"73","Leisure and Hotel",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("81",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"81","Commercial Banks",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("82",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"82","Mortgage Banks",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("83",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"83","Life Insurances",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("84",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"84","Non-Life Insurances",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("85",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"85","Reinsurances",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("86",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"86","Insurance Holdings",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("91",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"91","Trading",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("92",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"92","Pharmaceutical Trade",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("93",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"93","Publishing and Media",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("94",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"94","Environment",industrySystem); industryTypeRepository.save(industryType); }


        industrySystem = industrySystemRepository.findIndustrySystemByCode("10");
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("10001",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"10001","Mining & Agglomeration of Hard Coal, Lignite",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("40101",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"40101","Generation of Electricity",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("40103",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"40103","Collection and Distn of Electricity",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("40105",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"40105","Generation & Distribution of Solar Energy etc",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("41001",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"41001","Collection, Purification & Distn of Water",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("45011",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"45011","Construction/Erection of Power Lines etc",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("45013",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"45013","Construction/Maintenance of Roads",industrySystem); industryTypeRepository.save(industryType); }

        industrySystem = industrySystemRepository.findIndustrySystemByCode("11");
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("10001",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"10001","Mining & Agglomeration of Hard Coal, Lignite",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("40101",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"40101","Generation of Electricity",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("40103",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"40103","Collection and Distn of Electricity",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("40105",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"40105","Generation & Distribution of Solar Energy etc",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("41001",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"41001","Collection, Purification & Distn of Water",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("45011",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"45011","Construction/Erection of Power Lines etc",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("45013",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"45013","Construction/Maintenance of Roads",industrySystem); industryTypeRepository.save(industryType); }



        industrySystem = industrySystemRepository.findIndustrySystemByCode("12");
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("10001",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"10001","Mining & Agglomeration of Hard Coal, Lignite",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("40101",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"40101","Generation of Electricity",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("40103",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"40103","Collection and Distn of Electricity",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("40105",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"40105","Generation & Distribution of Solar Energy etc",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("41001",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"41001","Collection, Purification & Distn of Water",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("45011",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"45011","Construction/Erection of Power Lines etc",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("45013",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"45013","Construction/Maintenance of Roads",industrySystem); industryTypeRepository.save(industryType); }

        industrySystem = industrySystemRepository.findIndustrySystemByCode("20");

        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("10001",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"10001","Mining & Agglomeration of Hard Coal, Lignite",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("40101",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"40101","Generation of Electricity",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("40103",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"40103","Collection and Distn of Electricity",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("40105",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"40105","Generation & Distribution of Solar Energy etc",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("41001",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"41001","Collection, Purification & Distn of Water",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("45011",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"45011","Construction/Erection of Power Lines etc",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("45013",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"45013","Construction/Maintenance of Roads",industrySystem); industryTypeRepository.save(industryType); }

        industrySystem = industrySystemRepository.findIndustrySystemByCode("30");

        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("10001",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"10001","Mining & Agglomeration of Hard Coal, Lignite",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("40101",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"40101","Generation of Electricity",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("40103",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"40103","Collection and Distn of Electricity",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("40105",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"40105","Generation & Distribution of Solar Energy etc",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("41001",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"41001","Collection, Purification & Distn of Water",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("45011",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"45011","Construction/Erection of Power Lines etc",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("45013",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"45013","Construction/Maintenance of Roads",industrySystem); industryTypeRepository.save(industryType); }

        industrySystem = industrySystemRepository.findIndustrySystemByCode("40");
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("10001",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"10001","Mining & Agglomeration of Hard Coal, Lignite",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("40101",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"40101","Generation of Electricity",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("40103",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"40103","Collection and Distn of Electricity",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("40105",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"40105","Generation & Distribution of Solar Energy etc",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("41001",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"41001","Collection, Purification & Distn of Water",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("45011",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"45011","Construction/Erection of Power Lines etc",industrySystem); industryTypeRepository.save(industryType); }
        industryType = industryTypeRepository.findIndustryTypeByCodeAndIndustrySystem("45013",industrySystem);if (industryType == null){ industryType = new IndustryType(null,"45013","Construction/Maintenance of Roads",industrySystem); industryTypeRepository.save(industryType); }

        return;
    }
}