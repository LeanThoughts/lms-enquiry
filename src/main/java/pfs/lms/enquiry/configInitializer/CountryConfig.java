package pfs.lms.enquiry.configInitializer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.businesspartner.domain.AmendmentReason;
import pfs.lms.enquiry.businesspartner.repository.AmendmentReasonRepository;
import pfs.lms.enquiry.domain.Country;
import pfs.lms.enquiry.repository.CountryRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class CountryConfig implements CommandLineRunner {

    private final CountryRepository countryRepository;
 
    @Override
    public void run(String... strings) throws Exception {

        Country country = new Country();
        country = countryRepository.findByCountryCode("");if (country == null){ country = new Country( "", ""); countryRepository.save(country); }

        country = countryRepository.findByCountryCode("AD");if (country == null){ country = new Country( "AD", "Andorra"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("AE");if (country == null){ country = new Country( "AE", "United Arab Emirates"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("AF");if (country == null){ country = new Country( "AF", "Afghanistan"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("AG");if (country == null){ country = new Country( "AG", "Antigua and Barbuda"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("AI");if (country == null){ country = new Country( "AI", "Anguilla"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("AL");if (country == null){ country = new Country( "AL", "Albania"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("AM");if (country == null){ country = new Country( "AM", "Armenia"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("AN");if (country == null){ country = new Country( "AN", "Dutch Antilles"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("AO");if (country == null){ country = new Country( "AO", "Angola"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("AQ");if (country == null){ country = new Country( "AQ", "Antarctica"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("AR");if (country == null){ country = new Country( "AR", "Argentina"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("AS");if (country == null){ country = new Country( "AS", "American Samoa"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("AT");if (country == null){ country = new Country( "AT", "Austria"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("AU");if (country == null){ country = new Country( "AU", "Australia"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("AW");if (country == null){ country = new Country( "AW", "Aruba"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("AZ");if (country == null){ country = new Country( "AZ", "Azerbaijan"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("BA");if (country == null){ country = new Country( "BA", "Bosnia and Herzegovina"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("BB");if (country == null){ country = new Country( "BB", "Barbados"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("BD");if (country == null){ country = new Country( "BD", "Bangladesh"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("BE");if (country == null){ country = new Country( "BE", "Belgium"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("BF");if (country == null){ country = new Country( "BF", "Burkina Faso"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("BG");if (country == null){ country = new Country( "BG", "Bulgaria"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("BH");if (country == null){ country = new Country( "BH", "Bahrain"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("BI");if (country == null){ country = new Country( "BI", "Burundi"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("BJ");if (country == null){ country = new Country( "BJ", "Benin"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("BL");if (country == null){ country = new Country( "BL", "Blue"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("BM");if (country == null){ country = new Country( "BM", "Bermuda"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("BN");if (country == null){ country = new Country( "BN", "Brunei Darussalam"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("BO");if (country == null){ country = new Country( "BO", "Bolivia"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("BR");if (country == null){ country = new Country( "BR", "Brazil"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("BS");if (country == null){ country = new Country( "BS", "Bahamas"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("BT");if (country == null){ country = new Country( "BT", "Bhutan"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("BV");if (country == null){ country = new Country( "BV", "Bouvet Islands"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("BW");if (country == null){ country = new Country( "BW", "Botswana"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("BY");if (country == null){ country = new Country( "BY", "Belarus"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("BZ");if (country == null){ country = new Country( "BZ", "Belize"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("CA");if (country == null){ country = new Country( "CA", "Canada"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("CC");if (country == null){ country = new Country( "CC", "Coconut Islands"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("CD");if (country == null){ country = new Country( "CD", "Democratic Republic of the Congo"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("CF");if (country == null){ country = new Country( "CF", "Central African Republic"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("CG");if (country == null){ country = new Country( "CG", "Republic of the Congo"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("CH");if (country == null){ country = new Country( "CH", "Switzerland"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("CI");if (country == null){ country = new Country( "CI", "Cote d'Ivoire"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("CK");if (country == null){ country = new Country( "CK", "Cook Islands"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("CL");if (country == null){ country = new Country( "CL", "Chile"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("CM");if (country == null){ country = new Country( "CM", "Cameroon"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("CN");if (country == null){ country = new Country( "CN", "China"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("CO");if (country == null){ country = new Country( "CO", "Colombia"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("CR");if (country == null){ country = new Country( "CR", "Costa Rica"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("CS");if (country == null){ country = new Country( "CS", "Serbia and Montenegro"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("CU");if (country == null){ country = new Country( "CU", "Cuba"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("CV");if (country == null){ country = new Country( "CV", "Cape Verde"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("CX");if (country == null){ country = new Country( "CX", "Christmas Islnd"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("CY");if (country == null){ country = new Country( "CY", "Cyprus"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("CZ");if (country == null){ country = new Country( "CZ", "Czech Republic"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("DE");if (country == null){ country = new Country( "DE", "Germany"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("DJ");if (country == null){ country = new Country( "DJ", "Djibouti"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("DK");if (country == null){ country = new Country( "DK", "Denmark"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("DM");if (country == null){ country = new Country( "DM", "Dominica"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("DO");if (country == null){ country = new Country( "DO", "Dominican Republic"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("DZ");if (country == null){ country = new Country( "DZ", "Algeria"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("EC");if (country == null){ country = new Country( "EC", "Ecuador"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("EE");if (country == null){ country = new Country( "EE", "Estonia"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("EG");if (country == null){ country = new Country( "EG", "Egypt"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("EH");if (country == null){ country = new Country( "EH", "West Sahara"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("ER");if (country == null){ country = new Country( "ER", "Eritrea"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("ES");if (country == null){ country = new Country( "ES", "Spain"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("ET");if (country == null){ country = new Country( "ET", "Ethiopia"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("EU");if (country == null){ country = new Country( "EU", "European Union"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("FI");if (country == null){ country = new Country( "FI", "Finland"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("FJ");if (country == null){ country = new Country( "FJ", "Fiji"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("FK");if (country == null){ country = new Country( "FK", "Falkland Islands"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("FM");if (country == null){ country = new Country( "FM", "Micronesia"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("FO");if (country == null){ country = new Country( "FO", "Faroe Islands"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("FR");if (country == null){ country = new Country( "FR", "France"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("GA");if (country == null){ country = new Country( "GA", "Gabon"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("GB");if (country == null){ country = new Country( "GB", "United Kingdom"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("GD");if (country == null){ country = new Country( "GD", "Grenada"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("GE");if (country == null){ country = new Country( "GE", "Georgia"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("GF");if (country == null){ country = new Country( "GF", "French Guyana"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("GH");if (country == null){ country = new Country( "GH", "Ghana"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("GI");if (country == null){ country = new Country( "GI", "Gibraltar"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("GL");if (country == null){ country = new Country( "GL", "Greenland"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("GM");if (country == null){ country = new Country( "GM", "Gambia"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("GN");if (country == null){ country = new Country( "GN", "Guinea"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("GP");if (country == null){ country = new Country( "GP", "Guadeloupe"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("GQ");if (country == null){ country = new Country( "GQ", "Equatorial Guinea"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("GR");if (country == null){ country = new Country( "GR", "Greece"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("GS");if (country == null){ country = new Country( "GS", "South Georgia and the Southern Sandwich Islands"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("GT");if (country == null){ country = new Country( "GT", "Guatemala"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("GU");if (country == null){ country = new Country( "GU", "Guam"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("GW");if (country == null){ country = new Country( "GW", "Guinea-Bissau"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("GY");if (country == null){ country = new Country( "GY", "Guyana"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("HK");if (country == null){ country = new Country( "HK", "Hong Kong"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("HM");if (country == null){ country = new Country( "HM", "Heard and McDonald Islands"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("HN");if (country == null){ country = new Country( "HN", "Honduras"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("HR");if (country == null){ country = new Country( "HR", "Croatia"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("HT");if (country == null){ country = new Country( "HT", "Haiti"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("HU");if (country == null){ country = new Country( "HU", "Hungary"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("ID");if (country == null){ country = new Country( "ID", "Indonesia"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("IE");if (country == null){ country = new Country( "IE", "Ireland"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("IL");if (country == null){ country = new Country( "IL", "Israel"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("IN");if (country == null){ country = new Country( "IN", "Republic of India"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("IO");if (country == null){ country = new Country( "IO", "British Indian Ocean Territory"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("IQ");if (country == null){ country = new Country( "IQ", "Iraq"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("IR");if (country == null){ country = new Country( "IR", "Iran"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("IS");if (country == null){ country = new Country( "IS", "Iceland"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("IT");if (country == null){ country = new Country( "IT", "Italy"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("JM");if (country == null){ country = new Country( "JM", "Jamaica"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("JO");if (country == null){ country = new Country( "JO", "Jordan"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("JP");if (country == null){ country = new Country( "JP", "Japan"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("KE");if (country == null){ country = new Country( "KE", "Kenya"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("KG");if (country == null){ country = new Country( "KG", "Kyrgyzstan"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("KH");if (country == null){ country = new Country( "KH", "Cambodia"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("KI");if (country == null){ country = new Country( "KI", "Kiribati"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("KM");if (country == null){ country = new Country( "KM", "Comoros"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("KN");if (country == null){ country = new Country( "KN", "Saint Kitts and Nevis"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("KP");if (country == null){ country = new Country( "KP", "North Korea"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("KR");if (country == null){ country = new Country( "KR", "South Korea"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("KW");if (country == null){ country = new Country( "KW", "Kuwait"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("KY");if (country == null){ country = new Country( "KY", "Cayman Islands"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("KZ");if (country == null){ country = new Country( "KZ", "Kazakhstan"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("LA");if (country == null){ country = new Country( "LA", "Laos"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("LB");if (country == null){ country = new Country( "LB", "Lebanon"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("LC");if (country == null){ country = new Country( "LC", "St. Lucia"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("LI");if (country == null){ country = new Country( "LI", "Liechtenstein"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("LK");if (country == null){ country = new Country( "LK", "Sri Lanka"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("LR");if (country == null){ country = new Country( "LR", "Liberia"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("LS");if (country == null){ country = new Country( "LS", "Lesotho"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("LT");if (country == null){ country = new Country( "LT", "Lithuania"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("LU");if (country == null){ country = new Country( "LU", "Luxembourg"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("LV");if (country == null){ country = new Country( "LV", "Latvia"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("LY");if (country == null){ country = new Country( "LY", "Libya"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("MA");if (country == null){ country = new Country( "MA", "Morocco"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("MC");if (country == null){ country = new Country( "MC", "Monaco"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("MD");if (country == null){ country = new Country( "MD", "Moldova"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("MG");if (country == null){ country = new Country( "MG", "Madagascar"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("MH");if (country == null){ country = new Country( "MH", "Marshall Islands"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("MK");if (country == null){ country = new Country( "MK", "Macedonia"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("ML");if (country == null){ country = new Country( "ML", "Mali"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("MM");if (country == null){ country = new Country( "MM", "Burma"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("MN");if (country == null){ country = new Country( "MN", "Mongolia"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("MO");if (country == null){ country = new Country( "MO", "Macau"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("MP");if (country == null){ country = new Country( "MP", "North Mariana Islands"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("MQ");if (country == null){ country = new Country( "MQ", "Martinique"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("MR");if (country == null){ country = new Country( "MR", "Mauretania"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("MS");if (country == null){ country = new Country( "MS", "Montserrat"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("MT");if (country == null){ country = new Country( "MT", "Malta"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("MU");if (country == null){ country = new Country( "MU", "Mauritius"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("MV");if (country == null){ country = new Country( "MV", "Maldives"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("MW");if (country == null){ country = new Country( "MW", "Malawi"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("MX");if (country == null){ country = new Country( "MX", "Mexico"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("MY");if (country == null){ country = new Country( "MY", "Malaysia"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("MZ");if (country == null){ country = new Country( "MZ", "Mozambique"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("NA");if (country == null){ country = new Country( "NA", "Namibia"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("NC");if (country == null){ country = new Country( "NC", "New Caledonia"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("NE");if (country == null){ country = new Country( "NE", "Niger"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("NF");if (country == null){ country = new Country( "NF", "Norfolk Islands"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("NG");if (country == null){ country = new Country( "NG", "Nigeria"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("NI");if (country == null){ country = new Country( "NI", "Nicaragua"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("NL");if (country == null){ country = new Country( "NL", "Netherlands"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("NO");if (country == null){ country = new Country( "NO", "Norway"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("NP");if (country == null){ country = new Country( "NP", "Nepal"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("NR");if (country == null){ country = new Country( "NR", "Nauru"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("NT");if (country == null){ country = new Country( "NT", "NATO"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("NU");if (country == null){ country = new Country( "NU", "Niue"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("NZ");if (country == null){ country = new Country( "NZ", "New Zealand"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("OM");if (country == null){ country = new Country( "OM", "Oman"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("OR");if (country == null){ country = new Country( "OR", "Orange"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("PA");if (country == null){ country = new Country( "PA", "Panama"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("PE");if (country == null){ country = new Country( "PE", "Peru"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("PF");if (country == null){ country = new Country( "PF", "French Polynesia"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("PG");if (country == null){ country = new Country( "PG", "Papua New Guinea"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("PH");if (country == null){ country = new Country( "PH", "Philippines"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("PK");if (country == null){ country = new Country( "PK", "Pakistan"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("PL");if (country == null){ country = new Country( "PL", "Poland"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("PM");if (country == null){ country = new Country( "PM", "St. Pierre and Miquelon"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("PN");if (country == null){ country = new Country( "PN", "Pitcairn Islands"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("PR");if (country == null){ country = new Country( "PR", "Puerto Rico"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("PS");if (country == null){ country = new Country( "PS", "Palestine"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("PT");if (country == null){ country = new Country( "PT", "Portugal"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("PW");if (country == null){ country = new Country( "PW", "Palau"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("PY");if (country == null){ country = new Country( "PY", "Paraguay"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("QA");if (country == null){ country = new Country( "QA", "Qatar"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("RE");if (country == null){ country = new Country( "RE", "Reunion"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("RO");if (country == null){ country = new Country( "RO", "Romania"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("RU");if (country == null){ country = new Country( "RU", "Russian Federation"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("RW");if (country == null){ country = new Country( "RW", "Rwanda"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("SA");if (country == null){ country = new Country( "SA", "Saudi Arabia"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("SB");if (country == null){ country = new Country( "SB", "Solomon Islands"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("SC");if (country == null){ country = new Country( "SC", "Seychelles"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("SD");if (country == null){ country = new Country( "SD", "Sudan"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("SE");if (country == null){ country = new Country( "SE", "Sweden"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("SG");if (country == null){ country = new Country( "SG", "Singapore"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("SH");if (country == null){ country = new Country( "SH", "Saint Helena"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("SI");if (country == null){ country = new Country( "SI", "Slovenia"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("SJ");if (country == null){ country = new Country( "SJ", "Svalbard"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("SK");if (country == null){ country = new Country( "SK", "Slovakia"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("SL");if (country == null){ country = new Country( "SL", "Sierra Leone"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("SM");if (country == null){ country = new Country( "SM", "San Marino"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("SN");if (country == null){ country = new Country( "SN", "Senegal"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("SO");if (country == null){ country = new Country( "SO", "Somalia"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("SR");if (country == null){ country = new Country( "SR", "Suriname"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("ST");if (country == null){ country = new Country( "ST", "Sao Tome and Principe"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("SV");if (country == null){ country = new Country( "SV", "El Salvador"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("SY");if (country == null){ country = new Country( "SY", "Syria"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("SZ");if (country == null){ country = new Country( "SZ", "Swaziland"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("TC");if (country == null){ country = new Country( "TC", "Turks and Caicos Islands"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("TD");if (country == null){ country = new Country( "TD", "Chad"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("TF");if (country == null){ country = new Country( "TF", ""); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("TG");if (country == null){ country = new Country( "TG", "Togo"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("TH");if (country == null){ country = new Country( "TH", "Thailand"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("TJ");if (country == null){ country = new Country( "TJ", "Tajikistan"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("TK");if (country == null){ country = new Country( "TK", "Tokelau Islands"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("TL");if (country == null){ country = new Country( "TL", "East Timor"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("TM");if (country == null){ country = new Country( "TM", "Turkmenistan"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("TN");if (country == null){ country = new Country( "TN", "Tunisia"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("TO");if (country == null){ country = new Country( "TO", "Tonga"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("TP");if (country == null){ country = new Country( "TP", "East Timor"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("TR");if (country == null){ country = new Country( "TR", "Turkey"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("TT");if (country == null){ country = new Country( "TT", "Trinidad and Tobago"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("TV");if (country == null){ country = new Country( "TV", "Tuvalu"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("TW");if (country == null){ country = new Country( "TW", "Taiwan"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("TZ");if (country == null){ country = new Country( "TZ", "Tanzania"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("UA");if (country == null){ country = new Country( "UA", "Ukraine"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("UG");if (country == null){ country = new Country( "UG", "Uganda"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("UM");if (country == null){ country = new Country( "UM", "American Minor Outlying Islands"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("UN");if (country == null){ country = new Country( "UN", "United Nations"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("US");if (country == null){ country = new Country( "US", "USA"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("UY");if (country == null){ country = new Country( "UY", "Uruguay"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("UZ");if (country == null){ country = new Country( "UZ", "Uzbekistan"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("VA");if (country == null){ country = new Country( "VA", "Vatican City"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("VC");if (country == null){ country = new Country( "VC", "St. Vincent and the Grenadines"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("VE");if (country == null){ country = new Country( "VE", "Venezuela"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("VG");if (country == null){ country = new Country( "VG", "British Virgin Islands"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("VI");if (country == null){ country = new Country( "VI", "American Virgin Islands"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("VN");if (country == null){ country = new Country( "VN", "Vietnam"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("VU");if (country == null){ country = new Country( "VU", "Vanuatu"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("WF");if (country == null){ country = new Country( "WF", "Wallis and Futuna Islands"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("WS");if (country == null){ country = new Country( "WS", "Samoa"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("YE");if (country == null){ country = new Country( "YE", "Yemen"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("YT");if (country == null){ country = new Country( "YT", "Mayotte"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("ZA");if (country == null){ country = new Country( "ZA", "South Africa"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("ZM");if (country == null){ country = new Country( "ZM", "Zambia"); countryRepository.save(country); }
        country = countryRepository.findByCountryCode("ZW");if (country == null){ country = new Country( "ZW", "Zimbabwe"); countryRepository.save(country); }


        return;
    }
}