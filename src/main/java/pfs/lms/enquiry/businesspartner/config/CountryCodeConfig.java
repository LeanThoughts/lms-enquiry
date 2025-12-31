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
        countryCode = countryCodeRepository.findByCode("AD");if (countryCode == null){ countryCode = new CountryCode("AD","Andorran"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("AE");if (countryCode == null){ countryCode = new CountryCode("AE","United Arab Emirates"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("AF");if (countryCode == null){ countryCode = new CountryCode("AF","Afghan"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("AG");if (countryCode == null){ countryCode = new CountryCode("AG","Antiguan"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("AI");if (countryCode == null){ countryCode = new CountryCode("AI","Anguillian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("AL");if (countryCode == null){ countryCode = new CountryCode("AL","Albanian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("AM");if (countryCode == null){ countryCode = new CountryCode("AM","Armenian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("AN");if (countryCode == null){ countryCode = new CountryCode("AN","Dutch"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("AO");if (countryCode == null){ countryCode = new CountryCode("AO","Angolan"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("AQ");if (countryCode == null){ countryCode = new CountryCode("AQ","From Antarctica"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("AR");if (countryCode == null){ countryCode = new CountryCode("AR","Argentinian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("AS");if (countryCode == null){ countryCode = new CountryCode("AS","Samoan"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("AT");if (countryCode == null){ countryCode = new CountryCode("AT","Austrian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("AU");if (countryCode == null){ countryCode = new CountryCode("AU","Australian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("AW");if (countryCode == null){ countryCode = new CountryCode("AW","Arubanic"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("AZ");if (countryCode == null){ countryCode = new CountryCode("AZ","Azerbaijani"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("BA");if (countryCode == null){ countryCode = new CountryCode("BA","Bosnian-Herzegovinian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("BB");if (countryCode == null){ countryCode = new CountryCode("BB","Barbadan"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("BD");if (countryCode == null){ countryCode = new CountryCode("BD","Bangladeshi"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("BE");if (countryCode == null){ countryCode = new CountryCode("BE","Belgian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("BF");if (countryCode == null){ countryCode = new CountryCode("BF","Burkinabe"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("BG");if (countryCode == null){ countryCode = new CountryCode("BG","Bulgarian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("BH");if (countryCode == null){ countryCode = new CountryCode("BH","Bahraini"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("BI");if (countryCode == null){ countryCode = new CountryCode("BI","Burundi"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("BJ");if (countryCode == null){ countryCode = new CountryCode("BJ","Beninese"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("BL");if (countryCode == null){ countryCode = new CountryCode("BL",""); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("BM");if (countryCode == null){ countryCode = new CountryCode("BM","Bermudan"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("BN");if (countryCode == null){ countryCode = new CountryCode("BN","Brunei"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("BO");if (countryCode == null){ countryCode = new CountryCode("BO","Bolivian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("BR");if (countryCode == null){ countryCode = new CountryCode("BR","Brazilian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("BS");if (countryCode == null){ countryCode = new CountryCode("BS","Bahaman"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("BT");if (countryCode == null){ countryCode = new CountryCode("BT","Bhutanese"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("BV");if (countryCode == null){ countryCode = new CountryCode("BV","From the Bouvet Islands"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("BW");if (countryCode == null){ countryCode = new CountryCode("BW","Botswanan"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("BY");if (countryCode == null){ countryCode = new CountryCode("BY","Belarusian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("BZ");if (countryCode == null){ countryCode = new CountryCode("BZ","Belizean"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("CA");if (countryCode == null){ countryCode = new CountryCode("CA","Canadian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("CC");if (countryCode == null){ countryCode = new CountryCode("CC","Australian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("CD");if (countryCode == null){ countryCode = new CountryCode("CD","Congolese"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("CF");if (countryCode == null){ countryCode = new CountryCode("CF","Central African"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("CG");if (countryCode == null){ countryCode = new CountryCode("CG","Congolese"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("CH");if (countryCode == null){ countryCode = new CountryCode("CH","Swiss"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("CI");if (countryCode == null){ countryCode = new CountryCode("CI","Ivoirian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("CK");if (countryCode == null){ countryCode = new CountryCode("CK","Cook Islands"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("CL");if (countryCode == null){ countryCode = new CountryCode("CL","Chilean"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("CM");if (countryCode == null){ countryCode = new CountryCode("CM","Cameroonian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("CN");if (countryCode == null){ countryCode = new CountryCode("CN","Chinese"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("CO");if (countryCode == null){ countryCode = new CountryCode("CO","Colombian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("CR");if (countryCode == null){ countryCode = new CountryCode("CR","Costa Rican"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("CS");if (countryCode == null){ countryCode = new CountryCode("CS","Serbian; Montenegrin"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("CU");if (countryCode == null){ countryCode = new CountryCode("CU","Cuban"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("CV");if (countryCode == null){ countryCode = new CountryCode("CV","Cape Verdean"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("CX");if (countryCode == null){ countryCode = new CountryCode("CX","Australian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("CY");if (countryCode == null){ countryCode = new CountryCode("CY","Cypriot"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("CZ");if (countryCode == null){ countryCode = new CountryCode("CZ","Czech"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("DE");if (countryCode == null){ countryCode = new CountryCode("DE","German"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("DJ");if (countryCode == null){ countryCode = new CountryCode("DJ","Djiboutian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("DK");if (countryCode == null){ countryCode = new CountryCode("DK","Danish"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("DM");if (countryCode == null){ countryCode = new CountryCode("DM","Dominican"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("DO");if (countryCode == null){ countryCode = new CountryCode("DO","Dominican"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("DZ");if (countryCode == null){ countryCode = new CountryCode("DZ","Algerian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("EC");if (countryCode == null){ countryCode = new CountryCode("EC","Ecuadorian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("EE");if (countryCode == null){ countryCode = new CountryCode("EE","Estonian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("EG");if (countryCode == null){ countryCode = new CountryCode("EG","Egyptian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("EH");if (countryCode == null){ countryCode = new CountryCode("EH","French"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("ER");if (countryCode == null){ countryCode = new CountryCode("ER","Eritrean"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("ES");if (countryCode == null){ countryCode = new CountryCode("ES","Spanish"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("ET");if (countryCode == null){ countryCode = new CountryCode("ET","Ethiopian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("EU");if (countryCode == null){ countryCode = new CountryCode("EU",""); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("FI");if (countryCode == null){ countryCode = new CountryCode("FI","Finnish"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("FJ");if (countryCode == null){ countryCode = new CountryCode("FJ","Fijian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("FK");if (countryCode == null){ countryCode = new CountryCode("FK","British"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("FM");if (countryCode == null){ countryCode = new CountryCode("FM","Micronesian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("FO");if (countryCode == null){ countryCode = new CountryCode("FO","Danish"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("FR");if (countryCode == null){ countryCode = new CountryCode("FR","French"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("GA");if (countryCode == null){ countryCode = new CountryCode("GA","Gabonese"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("GB");if (countryCode == null){ countryCode = new CountryCode("GB","British"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("GD");if (countryCode == null){ countryCode = new CountryCode("GD","Grenadian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("GE");if (countryCode == null){ countryCode = new CountryCode("GE","Georgian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("GF");if (countryCode == null){ countryCode = new CountryCode("GF","French"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("GH");if (countryCode == null){ countryCode = new CountryCode("GH","Ghanian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("GI");if (countryCode == null){ countryCode = new CountryCode("GI","Gibraltar"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("GL");if (countryCode == null){ countryCode = new CountryCode("GL","Danish"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("GM");if (countryCode == null){ countryCode = new CountryCode("GM","Gambian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("GN");if (countryCode == null){ countryCode = new CountryCode("GN","Guinean"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("GP");if (countryCode == null){ countryCode = new CountryCode("GP","French"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("GQ");if (countryCode == null){ countryCode = new CountryCode("GQ","Equatorial Guinean"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("GR");if (countryCode == null){ countryCode = new CountryCode("GR","Greek"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("GS");if (countryCode == null){ countryCode = new CountryCode("GS","South Georgia"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("GT");if (countryCode == null){ countryCode = new CountryCode("GT","Guatemalan"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("GU");if (countryCode == null){ countryCode = new CountryCode("GU","American"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("GW");if (countryCode == null){ countryCode = new CountryCode("GW","Guinean"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("GY");if (countryCode == null){ countryCode = new CountryCode("GY","Guyanese"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("HK");if (countryCode == null){ countryCode = new CountryCode("HK","Hong Kong"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("HM");if (countryCode == null){ countryCode = new CountryCode("HM","From the Heard and McDonald Islands"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("HN");if (countryCode == null){ countryCode = new CountryCode("HN","Honduran"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("HR");if (countryCode == null){ countryCode = new CountryCode("HR","Croatian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("HT");if (countryCode == null){ countryCode = new CountryCode("HT","Haitian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("HU");if (countryCode == null){ countryCode = new CountryCode("HU","Hungarian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("ID");if (countryCode == null){ countryCode = new CountryCode("ID","Indonesian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("IE");if (countryCode == null){ countryCode = new CountryCode("IE","Irish"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("IL");if (countryCode == null){ countryCode = new CountryCode("IL","Israeli"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("IN");if (countryCode == null){ countryCode = new CountryCode("IN","India"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("IO");if (countryCode == null){ countryCode = new CountryCode("IO","British"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("IQ");if (countryCode == null){ countryCode = new CountryCode("IQ","Iraqi"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("IR");if (countryCode == null){ countryCode = new CountryCode("IR","Iranian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("IS");if (countryCode == null){ countryCode = new CountryCode("IS","Icelandic"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("IT");if (countryCode == null){ countryCode = new CountryCode("IT","Italian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("JM");if (countryCode == null){ countryCode = new CountryCode("JM","Jamaican"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("JO");if (countryCode == null){ countryCode = new CountryCode("JO","Jordanian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("JP");if (countryCode == null){ countryCode = new CountryCode("JP","Japanese"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("KE");if (countryCode == null){ countryCode = new CountryCode("KE","Kenyan"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("KG");if (countryCode == null){ countryCode = new CountryCode("KG","Kyrgyzstani"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("KH");if (countryCode == null){ countryCode = new CountryCode("KH","Cambodian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("KI");if (countryCode == null){ countryCode = new CountryCode("KI","Kiribati"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("KM");if (countryCode == null){ countryCode = new CountryCode("KM","Comoran"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("KN");if (countryCode == null){ countryCode = new CountryCode("KN","From Saint Kitts and Nevis"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("KP");if (countryCode == null){ countryCode = new CountryCode("KP","Korean"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("KR");if (countryCode == null){ countryCode = new CountryCode("KR","Korean"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("KW");if (countryCode == null){ countryCode = new CountryCode("KW","Kuwaiti"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("KY");if (countryCode == null){ countryCode = new CountryCode("KY","British"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("KZ");if (countryCode == null){ countryCode = new CountryCode("KZ","Kazakh"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("LA");if (countryCode == null){ countryCode = new CountryCode("LA","Laotian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("LB");if (countryCode == null){ countryCode = new CountryCode("LB","Lebanese"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("LC");if (countryCode == null){ countryCode = new CountryCode("LC","Lucian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("LI");if (countryCode == null){ countryCode = new CountryCode("LI","Liechtenstein"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("LK");if (countryCode == null){ countryCode = new CountryCode("LK","Sri Lankan"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("LR");if (countryCode == null){ countryCode = new CountryCode("LR","Liberian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("LS");if (countryCode == null){ countryCode = new CountryCode("LS","Lesothan"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("LT");if (countryCode == null){ countryCode = new CountryCode("LT","Lithuanian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("LU");if (countryCode == null){ countryCode = new CountryCode("LU","Luxembourgian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("LV");if (countryCode == null){ countryCode = new CountryCode("LV","Latvian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("LY");if (countryCode == null){ countryCode = new CountryCode("LY","Libyan"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("MA");if (countryCode == null){ countryCode = new CountryCode("MA","Moroccan"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("MC");if (countryCode == null){ countryCode = new CountryCode("MC","Monegasque"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("MD");if (countryCode == null){ countryCode = new CountryCode("MD","Moldovan"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("MG");if (countryCode == null){ countryCode = new CountryCode("MG","Madagascan"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("MH");if (countryCode == null){ countryCode = new CountryCode("MH","Marshallese"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("MK");if (countryCode == null){ countryCode = new CountryCode("MK","Macedonian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("ML");if (countryCode == null){ countryCode = new CountryCode("ML","Malian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("MM");if (countryCode == null){ countryCode = new CountryCode("MM","Burmese"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("MN");if (countryCode == null){ countryCode = new CountryCode("MN","Mongolian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("MO");if (countryCode == null){ countryCode = new CountryCode("MO","Portuguese"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("MP");if (countryCode == null){ countryCode = new CountryCode("MP","Marianian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("MQ");if (countryCode == null){ countryCode = new CountryCode("MQ","French"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("MR");if (countryCode == null){ countryCode = new CountryCode("MR","Mauretanian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("MS");if (countryCode == null){ countryCode = new CountryCode("MS","Montserrat"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("MT");if (countryCode == null){ countryCode = new CountryCode("MT","Maltese"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("MU");if (countryCode == null){ countryCode = new CountryCode("MU","Mauritian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("MV");if (countryCode == null){ countryCode = new CountryCode("MV","Maldivian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("MW");if (countryCode == null){ countryCode = new CountryCode("MW","Malawian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("MX");if (countryCode == null){ countryCode = new CountryCode("MX","Mexican"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("MY");if (countryCode == null){ countryCode = new CountryCode("MY","Malaysian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("MZ");if (countryCode == null){ countryCode = new CountryCode("MZ","Mozambican"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("NA");if (countryCode == null){ countryCode = new CountryCode("NA","Namibian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("NC");if (countryCode == null){ countryCode = new CountryCode("NC","French"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("NE");if (countryCode == null){ countryCode = new CountryCode("NE","Nigerien"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("NF");if (countryCode == null){ countryCode = new CountryCode("NF","From the Norfolk Islands"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("NG");if (countryCode == null){ countryCode = new CountryCode("NG","Nigerian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("NI");if (countryCode == null){ countryCode = new CountryCode("NI","Nicaraguan"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("NL");if (countryCode == null){ countryCode = new CountryCode("NL","Dutch"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("NO");if (countryCode == null){ countryCode = new CountryCode("NO","Norwegian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("NP");if (countryCode == null){ countryCode = new CountryCode("NP","Nepalese"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("NR");if (countryCode == null){ countryCode = new CountryCode("NR","Nauruian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("NT");if (countryCode == null){ countryCode = new CountryCode("NT",""); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("NU");if (countryCode == null){ countryCode = new CountryCode("NU","Niuean"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("NZ");if (countryCode == null){ countryCode = new CountryCode("NZ","New Zealand"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("OM");if (countryCode == null){ countryCode = new CountryCode("OM","Omani"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("OR");if (countryCode == null){ countryCode = new CountryCode("OR",""); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("PA");if (countryCode == null){ countryCode = new CountryCode("PA","Panamanian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("PE");if (countryCode == null){ countryCode = new CountryCode("PE","Peruvian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("PF");if (countryCode == null){ countryCode = new CountryCode("PF","French"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("PG");if (countryCode == null){ countryCode = new CountryCode("PG","Papua New Guinean"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("PH");if (countryCode == null){ countryCode = new CountryCode("PH","Filipino"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("PK");if (countryCode == null){ countryCode = new CountryCode("PK","Pakistani"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("PL");if (countryCode == null){ countryCode = new CountryCode("PL","Polish"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("PM");if (countryCode == null){ countryCode = new CountryCode("PM","French"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("PN");if (countryCode == null){ countryCode = new CountryCode("PN","British"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("PR");if (countryCode == null){ countryCode = new CountryCode("PR","American"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("PS");if (countryCode == null){ countryCode = new CountryCode("PS","Palestinian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("PT");if (countryCode == null){ countryCode = new CountryCode("PT","Portuguese"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("PW");if (countryCode == null){ countryCode = new CountryCode("PW","Palauan"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("PY");if (countryCode == null){ countryCode = new CountryCode("PY","Paraguayan"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("QA");if (countryCode == null){ countryCode = new CountryCode("QA","Qatari"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("RE");if (countryCode == null){ countryCode = new CountryCode("RE","French"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("RO");if (countryCode == null){ countryCode = new CountryCode("RO","Rumanian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("RU");if (countryCode == null){ countryCode = new CountryCode("RU","Russian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("RW");if (countryCode == null){ countryCode = new CountryCode("RW","Rwandan"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("SA");if (countryCode == null){ countryCode = new CountryCode("SA","Saudi Arabian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("SB");if (countryCode == null){ countryCode = new CountryCode("SB","Solomonese"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("SC");if (countryCode == null){ countryCode = new CountryCode("SC","Seychellian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("SD");if (countryCode == null){ countryCode = new CountryCode("SD","Sudanese"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("SE");if (countryCode == null){ countryCode = new CountryCode("SE","Swedish"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("SG");if (countryCode == null){ countryCode = new CountryCode("SG","Singaporean"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("SH");if (countryCode == null){ countryCode = new CountryCode("SH","Saint Helenian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("SI");if (countryCode == null){ countryCode = new CountryCode("SI","Slovenian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("SJ");if (countryCode == null){ countryCode = new CountryCode("SJ","Norwegian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("SK");if (countryCode == null){ countryCode = new CountryCode("SK","Slovakian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("SL");if (countryCode == null){ countryCode = new CountryCode("SL","Sierra Leonean"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("SM");if (countryCode == null){ countryCode = new CountryCode("SM","Sammarinese"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("SN");if (countryCode == null){ countryCode = new CountryCode("SN","Senegalese"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("SO");if (countryCode == null){ countryCode = new CountryCode("SO","Somali"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("SR");if (countryCode == null){ countryCode = new CountryCode("SR","Surinamese"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("ST");if (countryCode == null){ countryCode = new CountryCode("ST","Sao Tomean"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("SV");if (countryCode == null){ countryCode = new CountryCode("SV","Salvadoran"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("SY");if (countryCode == null){ countryCode = new CountryCode("SY","Syrian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("SZ");if (countryCode == null){ countryCode = new CountryCode("SZ","Swazi"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("TC");if (countryCode == null){ countryCode = new CountryCode("TC","From the Turks and Caicos Islands"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("TD");if (countryCode == null){ countryCode = new CountryCode("TD","Chadian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("TF");if (countryCode == null){ countryCode = new CountryCode("TF","French"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("TG");if (countryCode == null){ countryCode = new CountryCode("TG","Togolese"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("TH");if (countryCode == null){ countryCode = new CountryCode("TH","Thai"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("TJ");if (countryCode == null){ countryCode = new CountryCode("TJ","Tajikistani"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("TK");if (countryCode == null){ countryCode = new CountryCode("TK","From the Tokelau Islands"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("TL");if (countryCode == null){ countryCode = new CountryCode("TL",""); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("TM");if (countryCode == null){ countryCode = new CountryCode("TM","Turkmenian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("TN");if (countryCode == null){ countryCode = new CountryCode("TN","Tunisian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("TO");if (countryCode == null){ countryCode = new CountryCode("TO","Tongan"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("TP");if (countryCode == null){ countryCode = new CountryCode("TP","From East Timor"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("TR");if (countryCode == null){ countryCode = new CountryCode("TR","Turkish"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("TT");if (countryCode == null){ countryCode = new CountryCode("TT","From Trinidad and Tobago"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("TV");if (countryCode == null){ countryCode = new CountryCode("TV","Tuvaluese"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("TW");if (countryCode == null){ countryCode = new CountryCode("TW","Chinese"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("TZ");if (countryCode == null){ countryCode = new CountryCode("TZ","Tanzanian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("UA");if (countryCode == null){ countryCode = new CountryCode("UA","Ukrainian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("UG");if (countryCode == null){ countryCode = new CountryCode("UG","Ugandan"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("UM");if (countryCode == null){ countryCode = new CountryCode("UM","American"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("UN");if (countryCode == null){ countryCode = new CountryCode("UN",""); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("US");if (countryCode == null){ countryCode = new CountryCode("US","American"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("UY");if (countryCode == null){ countryCode = new CountryCode("UY","Uruguayan"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("UZ");if (countryCode == null){ countryCode = new CountryCode("UZ","Uzbekistani"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("VA");if (countryCode == null){ countryCode = new CountryCode("VA",""); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("VC");if (countryCode == null){ countryCode = new CountryCode("VC","Vincentian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("VE");if (countryCode == null){ countryCode = new CountryCode("VE","Venezuelan"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("VG");if (countryCode == null){ countryCode = new CountryCode("VG","British"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("VI");if (countryCode == null){ countryCode = new CountryCode("VI","American"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("VN");if (countryCode == null){ countryCode = new CountryCode("VN","Vietnamese"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("VU");if (countryCode == null){ countryCode = new CountryCode("VU","Ni-Vanuatu"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("WF");if (countryCode == null){ countryCode = new CountryCode("WF","From the Wallis and Futuna Islands"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("WS");if (countryCode == null){ countryCode = new CountryCode("WS","Samoan"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("YE");if (countryCode == null){ countryCode = new CountryCode("YE","Yemeni"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("YT");if (countryCode == null){ countryCode = new CountryCode("YT","French"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("ZA");if (countryCode == null){ countryCode = new CountryCode("ZA","South African"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("ZM");if (countryCode == null){ countryCode = new CountryCode("ZM","Zambian"); countryCodeRepository.save(countryCode); }
        countryCode = countryCodeRepository.findByCode("ZW");if (countryCode == null){ countryCode = new CountryCode("ZW","Zimbabwean"); countryCodeRepository.save(countryCode); }

        return;
    }
}