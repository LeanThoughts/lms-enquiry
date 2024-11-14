package pfs.lms.enquiry.businesspartner.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.businesspartner.domain.CountryCode;
import pfs.lms.enquiry.businesspartner.domain.Title;
import pfs.lms.enquiry.businesspartner.repository.CountryCodeRepository;
import pfs.lms.enquiry.businesspartner.repository.TitleRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class CountryCodeConfig implements CommandLineRunner {

    private final CountryCodeRepository countryCodeRepository;
 
    @Override
    public void run(String... strings) throws Exception {

    CountryCode countryCode = new CountryCode();

        countryCode = countryCodeRepository.findByCode("AD");if (countryCode == null){ countryCode = new CountryCode("Andorran","Andorran"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("AE");if (countryCode == null){ countryCode = new CountryCode("Utd.Arab Emir.","Utd.Arab Emir."); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("AF");if (countryCode == null){ countryCode = new CountryCode("Afghanistan","Afghanistan"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("AG");if (countryCode == null){ countryCode = new CountryCode("Antigua/Barbuda","Antigua/Barbuda"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("AI");if (countryCode == null){ countryCode = new CountryCode("Anguilla","Anguilla"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("AL");if (countryCode == null){ countryCode = new CountryCode("Albania","Albania"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("AM");if (countryCode == null){ countryCode = new CountryCode("Armenia","Armenia"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("AN");if (countryCode == null){ countryCode = new CountryCode("Dutch Antilles","Dutch Antilles"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("AO");if (countryCode == null){ countryCode = new CountryCode("Angola","Angola"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("AQ");if (countryCode == null){ countryCode = new CountryCode("Antarctica","Antarctica"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("AR");if (countryCode == null){ countryCode = new CountryCode("Argentina","Argentina"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("AS");if (countryCode == null){ countryCode = new CountryCode("Samoa, America","Samoa, America"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("AT");if (countryCode == null){ countryCode = new CountryCode("Austria","Austria"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("AU");if (countryCode == null){ countryCode = new CountryCode("Australia","Australia"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("AW");if (countryCode == null){ countryCode = new CountryCode("Aruba","Aruba"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("AZ");if (countryCode == null){ countryCode = new CountryCode("Azerbaijan","Azerbaijan"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("BA");if (countryCode == null){ countryCode = new CountryCode("Bosnia-Herz.","Bosnia-Herz."); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("BB");if (countryCode == null){ countryCode = new CountryCode("Barbados","Barbados"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("BD");if (countryCode == null){ countryCode = new CountryCode("Bangladesh","Bangladesh"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("BE");if (countryCode == null){ countryCode = new CountryCode("Belgium","Belgium"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("BF");if (countryCode == null){ countryCode = new CountryCode("Burkina Faso","Burkina Faso"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("BG");if (countryCode == null){ countryCode = new CountryCode("Bulgaria","Bulgaria"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("BH");if (countryCode == null){ countryCode = new CountryCode("Bahrain","Bahrain"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("BI");if (countryCode == null){ countryCode = new CountryCode("Burundi","Burundi"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("BJ");if (countryCode == null){ countryCode = new CountryCode("Benin","Benin"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("BL");if (countryCode == null){ countryCode = new CountryCode("Blue","Blue"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("BM");if (countryCode == null){ countryCode = new CountryCode("Bermuda","Bermuda"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("BN");if (countryCode == null){ countryCode = new CountryCode("Brunei Daruss.","Brunei Daruss."); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("BO");if (countryCode == null){ countryCode = new CountryCode("Bolivia","Bolivia"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("BR");if (countryCode == null){ countryCode = new CountryCode("Brazil","Brazil"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("BS");if (countryCode == null){ countryCode = new CountryCode("Bahamas","Bahamas"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("BT");if (countryCode == null){ countryCode = new CountryCode("Bhutan","Bhutan"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("BW");if (countryCode == null){ countryCode = new CountryCode("Botswana","Botswana"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("BY");if (countryCode == null){ countryCode = new CountryCode("Belarus","Belarus"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("BZ");if (countryCode == null){ countryCode = new CountryCode("Belize","Belize"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("CA");if (countryCode == null){ countryCode = new CountryCode("Canada","Canada"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("CC");if (countryCode == null){ countryCode = new CountryCode("Coconut Islands","Coconut Islands"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("CD");if (countryCode == null){ countryCode = new CountryCode("Dem. Rep. Congo","Dem. Rep. Congo"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("CF");if (countryCode == null){ countryCode = new CountryCode("CAR","CAR"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("CG");if (countryCode == null){ countryCode = new CountryCode("Rep.of Congo","Rep.of Congo"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("CH");if (countryCode == null){ countryCode = new CountryCode("Switzerland","Switzerland"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("CI");if (countryCode == null){ countryCode = new CountryCode("Cote d'Ivoire","Cote d'Ivoire"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("CK");if (countryCode == null){ countryCode = new CountryCode("Cook Islands","Cook Islands"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("CL");if (countryCode == null){ countryCode = new CountryCode("Chile","Chile"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("CM");if (countryCode == null){ countryCode = new CountryCode("Cameroon","Cameroon"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("CN");if (countryCode == null){ countryCode = new CountryCode("China","China"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("CO");if (countryCode == null){ countryCode = new CountryCode("Colombia","Colombia"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("CR");if (countryCode == null){ countryCode = new CountryCode("Costa Rica","Costa Rica"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("CS");if (countryCode == null){ countryCode = new CountryCode("Serbia/Monten.","Serbia/Monten."); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("CU");if (countryCode == null){ countryCode = new CountryCode("Cuba","Cuba"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("CV");if (countryCode == null){ countryCode = new CountryCode("Cape Verde","Cape Verde"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("CX");if (countryCode == null){ countryCode = new CountryCode("Christmas Islnd","Christmas Islnd"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("CY");if (countryCode == null){ countryCode = new CountryCode("Cyprus","Cyprus"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("CZ");if (countryCode == null){ countryCode = new CountryCode("Czech Republic","Czech Republic"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("DE");if (countryCode == null){ countryCode = new CountryCode("Germany","Germany"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("DJ");if (countryCode == null){ countryCode = new CountryCode("Djibouti","Djibouti"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("DK");if (countryCode == null){ countryCode = new CountryCode("Denmark","Denmark"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("DM");if (countryCode == null){ countryCode = new CountryCode("Dominica","Dominica"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("DO");if (countryCode == null){ countryCode = new CountryCode("Dominican Rep.","Dominican Rep."); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("DZ");if (countryCode == null){ countryCode = new CountryCode("Algeria","Algeria"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("EC");if (countryCode == null){ countryCode = new CountryCode("Ecuador","Ecuador"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("EE");if (countryCode == null){ countryCode = new CountryCode("Estonia","Estonia"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("EG");if (countryCode == null){ countryCode = new CountryCode("Egypt","Egypt"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("EH");if (countryCode == null){ countryCode = new CountryCode("West Sahara","West Sahara"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("ER");if (countryCode == null){ countryCode = new CountryCode("Eritrea","Eritrea"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("ES");if (countryCode == null){ countryCode = new CountryCode("Spain","Spain"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("ET");if (countryCode == null){ countryCode = new CountryCode("Ethiopia","Ethiopia"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("EU");if (countryCode == null){ countryCode = new CountryCode("European Union","European Union"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("FI");if (countryCode == null){ countryCode = new CountryCode("Finland","Finland"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("FJ");if (countryCode == null){ countryCode = new CountryCode("Fiji","Fiji"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("FK");if (countryCode == null){ countryCode = new CountryCode("Falkland Islnds","Falkland Islnds"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("FM");if (countryCode == null){ countryCode = new CountryCode("Micronesia","Micronesia"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("FO");if (countryCode == null){ countryCode = new CountryCode("Faroe Islands","Faroe Islands"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("FR");if (countryCode == null){ countryCode = new CountryCode("France","France"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("GA");if (countryCode == null){ countryCode = new CountryCode("Gabon","Gabon"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("GB");if (countryCode == null){ countryCode = new CountryCode("United Kingdom","United Kingdom"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("GD");if (countryCode == null){ countryCode = new CountryCode("Grenada","Grenada"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("GE");if (countryCode == null){ countryCode = new CountryCode("Georgia","Georgia"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("GF");if (countryCode == null){ countryCode = new CountryCode("French Guayana","French Guayana"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("GH");if (countryCode == null){ countryCode = new CountryCode("Ghana","Ghana"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("GI");if (countryCode == null){ countryCode = new CountryCode("Gibraltar","Gibraltar"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("GL");if (countryCode == null){ countryCode = new CountryCode("Greenland","Greenland"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("GM");if (countryCode == null){ countryCode = new CountryCode("Gambia","Gambia"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("GN");if (countryCode == null){ countryCode = new CountryCode("Guinea","Guinea"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("GP");if (countryCode == null){ countryCode = new CountryCode("Guadeloupe","Guadeloupe"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("GQ");if (countryCode == null){ countryCode = new CountryCode("Equatorial Guin","Equatorial Guin"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("GR");if (countryCode == null){ countryCode = new CountryCode("Greece","Greece"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("GS");if (countryCode == null){ countryCode = new CountryCode("S. Sandwich Ins","S. Sandwich Ins"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("GT");if (countryCode == null){ countryCode = new CountryCode("Guatemala","Guatemala"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("GU");if (countryCode == null){ countryCode = new CountryCode("Guam","Guam"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("GW");if (countryCode == null){ countryCode = new CountryCode("Guinea-Bissau","Guinea-Bissau"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("GY");if (countryCode == null){ countryCode = new CountryCode("Guyana","Guyana"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("HK");if (countryCode == null){ countryCode = new CountryCode("Hong Kong","Hong Kong"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("HM");if (countryCode == null){ countryCode = new CountryCode("Heard/McDon.Isl","Heard/McDon.Isl"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("HN");if (countryCode == null){ countryCode = new CountryCode("Honduras","Honduras"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("HR");if (countryCode == null){ countryCode = new CountryCode("Croatia","Croatia"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("HT");if (countryCode == null){ countryCode = new CountryCode("Haiti","Haiti"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("HU");if (countryCode == null){ countryCode = new CountryCode("Hungary","Hungary"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("ID");if (countryCode == null){ countryCode = new CountryCode("Indonesia","Indonesia"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("IE");if (countryCode == null){ countryCode = new CountryCode("Ireland","Ireland"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("IL");if (countryCode == null){ countryCode = new CountryCode("Israel","Israel"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("IN");if (countryCode == null){ countryCode = new CountryCode("India","India"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("IO");if (countryCode == null){ countryCode = new CountryCode("Brit.Ind.Oc.Ter","Brit.Ind.Oc.Ter"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("IQ");if (countryCode == null){ countryCode = new CountryCode("Iraq","Iraq"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("IR");if (countryCode == null){ countryCode = new CountryCode("Iran","Iran"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("IS");if (countryCode == null){ countryCode = new CountryCode("Iceland","Iceland"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("IT");if (countryCode == null){ countryCode = new CountryCode("Italy","Italy"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("JM");if (countryCode == null){ countryCode = new CountryCode("Jamaica","Jamaica"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("JO");if (countryCode == null){ countryCode = new CountryCode("Jordan","Jordan"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("JP");if (countryCode == null){ countryCode = new CountryCode("Japan","Japan"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("KE");if (countryCode == null){ countryCode = new CountryCode("Kenya","Kenya"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("KG");if (countryCode == null){ countryCode = new CountryCode("Kyrgyzstan","Kyrgyzstan"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("KH");if (countryCode == null){ countryCode = new CountryCode("Cambodia","Cambodia"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("KI");if (countryCode == null){ countryCode = new CountryCode("Kiribati","Kiribati"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("KM");if (countryCode == null){ countryCode = new CountryCode("Comoros","Comoros"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("KN");if (countryCode == null){ countryCode = new CountryCode("St Kitts&Nevis","St Kitts&Nevis"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("KP");if (countryCode == null){ countryCode = new CountryCode("North Korea","North Korea"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("KR");if (countryCode == null){ countryCode = new CountryCode("South Korea","South Korea"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("KW");if (countryCode == null){ countryCode = new CountryCode("Kuwait","Kuwait"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("KY");if (countryCode == null){ countryCode = new CountryCode("Cayman Islands","Cayman Islands"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("KZ");if (countryCode == null){ countryCode = new CountryCode("Kazakhstan","Kazakhstan"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("LA");if (countryCode == null){ countryCode = new CountryCode("Laos","Laos"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("LB");if (countryCode == null){ countryCode = new CountryCode("Lebanon","Lebanon"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("LC");if (countryCode == null){ countryCode = new CountryCode("St. Lucia","St. Lucia"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("LI");if (countryCode == null){ countryCode = new CountryCode("Liechtenstein","Liechtenstein"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("LK");if (countryCode == null){ countryCode = new CountryCode("Sri Lanka","Sri Lanka"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("LR");if (countryCode == null){ countryCode = new CountryCode("Liberia","Liberia"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("LS");if (countryCode == null){ countryCode = new CountryCode("Lesotho","Lesotho"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("LT");if (countryCode == null){ countryCode = new CountryCode("Lithuania","Lithuania"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("LU");if (countryCode == null){ countryCode = new CountryCode("Luxembourg","Luxembourg"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("LV");if (countryCode == null){ countryCode = new CountryCode("Latvia","Latvia"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("LY");if (countryCode == null){ countryCode = new CountryCode("Libya","Libya"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("MA");if (countryCode == null){ countryCode = new CountryCode("Morocco","Morocco"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("MC");if (countryCode == null){ countryCode = new CountryCode("Monaco","Monaco"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("MD");if (countryCode == null){ countryCode = new CountryCode("Moldova","Moldova"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("MG");if (countryCode == null){ countryCode = new CountryCode("Madagascar","Madagascar"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("MH");if (countryCode == null){ countryCode = new CountryCode("Marshall Islnds","Marshall Islnds"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("MK");if (countryCode == null){ countryCode = new CountryCode("Macedonia","Macedonia"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("ML");if (countryCode == null){ countryCode = new CountryCode("Mali","Mali"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("MM");if (countryCode == null){ countryCode = new CountryCode("Burma","Burma"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("MN");if (countryCode == null){ countryCode = new CountryCode("Mongolia","Mongolia"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("MO");if (countryCode == null){ countryCode = new CountryCode("Macau","Macau"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("MP");if (countryCode == null){ countryCode = new CountryCode("N.Mariana Islnd","N.Mariana Islnd"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("MQ");if (countryCode == null){ countryCode = new CountryCode("Martinique","Martinique"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("MR");if (countryCode == null){ countryCode = new CountryCode("Mauretania","Mauretania"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("MS");if (countryCode == null){ countryCode = new CountryCode("Montserrat","Montserrat"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("MT");if (countryCode == null){ countryCode = new CountryCode("Malta","Malta"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("MU");if (countryCode == null){ countryCode = new CountryCode("Mauritius","Mauritius"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("MV");if (countryCode == null){ countryCode = new CountryCode("Maldives","Maldives"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("MW");if (countryCode == null){ countryCode = new CountryCode("Malawi","Malawi"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("MX");if (countryCode == null){ countryCode = new CountryCode("0","0"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("MY");if (countryCode == null){ countryCode = new CountryCode("Malaysia","Malaysia"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("MZ");if (countryCode == null){ countryCode = new CountryCode("Mozambique","Mozambique"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("NA");if (countryCode == null){ countryCode = new CountryCode("Namibia","Namibia"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("NC");if (countryCode == null){ countryCode = new CountryCode("New Caledonia","New Caledonia"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("NE");if (countryCode == null){ countryCode = new CountryCode("Niger","Niger"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("NF");if (countryCode == null){ countryCode = new CountryCode("Norfolk Islands","Norfolk Islands"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("NG");if (countryCode == null){ countryCode = new CountryCode("Nigeria","Nigeria"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("NI");if (countryCode == null){ countryCode = new CountryCode("Nicaragua","Nicaragua"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("NL");if (countryCode == null){ countryCode = new CountryCode("Netherlands","Netherlands"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("NO");if (countryCode == null){ countryCode = new CountryCode("Norway","Norway"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("NP");if (countryCode == null){ countryCode = new CountryCode("Nepal","Nepal"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("NR");if (countryCode == null){ countryCode = new CountryCode("Nauru","Nauru"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("NT");if (countryCode == null){ countryCode = new CountryCode("NATO","NATO"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("NU");if (countryCode == null){ countryCode = new CountryCode("Niue","Niue"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("NZ");if (countryCode == null){ countryCode = new CountryCode("New Zealand","New Zealand"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("OM");if (countryCode == null){ countryCode = new CountryCode("Oman","Oman"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("OR");if (countryCode == null){ countryCode = new CountryCode("Orange","Orange"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("PA");if (countryCode == null){ countryCode = new CountryCode("Panama","Panama"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("PE");if (countryCode == null){ countryCode = new CountryCode("Peru","Peru"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("PF");if (countryCode == null){ countryCode = new CountryCode("Frenc.Polynesia","Frenc.Polynesia"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("PG");if (countryCode == null){ countryCode = new CountryCode("Pap. New Guinea","Pap. New Guinea"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("PH");if (countryCode == null){ countryCode = new CountryCode("Philippines","Philippines"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("PK");if (countryCode == null){ countryCode = new CountryCode("Pakistan","Pakistan"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("PL");if (countryCode == null){ countryCode = new CountryCode("Poland","Poland"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("PM");if (countryCode == null){ countryCode = new CountryCode("St.Pier,Miquel.","St.Pier,Miquel."); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("PN");if (countryCode == null){ countryCode = new CountryCode("Pitcairn Islnds","Pitcairn Islnds"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("PR");if (countryCode == null){ countryCode = new CountryCode("Puerto Rico","Puerto Rico"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("PS");if (countryCode == null){ countryCode = new CountryCode("Palestine","Palestine"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("PT");if (countryCode == null){ countryCode = new CountryCode("Portugal","Portugal"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("PW");if (countryCode == null){ countryCode = new CountryCode("Palau","Palau"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("PY");if (countryCode == null){ countryCode = new CountryCode("Paraguay","Paraguay"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("QA");if (countryCode == null){ countryCode = new CountryCode("Qatar","Qatar"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("RE");if (countryCode == null){ countryCode = new CountryCode("Reunion","Reunion"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("RO");if (countryCode == null){ countryCode = new CountryCode("Romania","Romania"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("RU");if (countryCode == null){ countryCode = new CountryCode("Russian Fed.","Russian Fed."); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("RW");if (countryCode == null){ countryCode = new CountryCode("Rwanda","Rwanda"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("SA");if (countryCode == null){ countryCode = new CountryCode("Saudi Arabia","Saudi Arabia"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("SB");if (countryCode == null){ countryCode = new CountryCode("Solomon Islands","Solomon Islands"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("SC");if (countryCode == null){ countryCode = new CountryCode("Seychelles","Seychelles"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("SD");if (countryCode == null){ countryCode = new CountryCode("Sudan","Sudan"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("SE");if (countryCode == null){ countryCode = new CountryCode("Sweden","Sweden"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("SG");if (countryCode == null){ countryCode = new CountryCode("Singapore","Singapore"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("SH");if (countryCode == null){ countryCode = new CountryCode("Saint Helena","Saint Helena"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("SI");if (countryCode == null){ countryCode = new CountryCode("Slovenia","Slovenia"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("SJ");if (countryCode == null){ countryCode = new CountryCode("Svalbard","Svalbard"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("SK");if (countryCode == null){ countryCode = new CountryCode("Slovakia","Slovakia"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("SL");if (countryCode == null){ countryCode = new CountryCode("Sierra Leone","Sierra Leone"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("SM");if (countryCode == null){ countryCode = new CountryCode("San Marino","San Marino"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("SN");if (countryCode == null){ countryCode = new CountryCode("Senegal","Senegal"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("SO");if (countryCode == null){ countryCode = new CountryCode("Somalia","Somalia"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("SR");if (countryCode == null){ countryCode = new CountryCode("Suriname","Suriname"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("ST");if (countryCode == null){ countryCode = new CountryCode("S.Tome,Principe","S.Tome,Principe"); countryCodeRepository.save(countryCode); }

        return;
    }
}