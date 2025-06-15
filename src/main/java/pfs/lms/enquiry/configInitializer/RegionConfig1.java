package pfs.lms.enquiry.configInitializer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.domain.Region;
import pfs.lms.enquiry.repository.RegionRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegionConfig1 implements CommandLineRunner {

    private final RegionRepository regionRepository;
 
    @Override
    public void run(String... strings) throws Exception {

        Region region = new Region();

        region = regionRepository.findById(0);if (region == null){ region = new Region( 0  , "", "", "Buenos Aires"); regionRepository.save(region); }

        region = regionRepository.findById(1);
        if (region == null || region.getId() == null){
            region = new Region( 1, "AR", "00", "Capital Federal");
            regionRepository.save(region);
        }
        region = regionRepository.findById(2);if (region == null){ region = new Region( 2, "AR", "01", "Buenos Aires"); regionRepository.save(region); }
        region = regionRepository.findById(3);if (region == null){ region = new Region( 3, "AR", "02", "Catamarca"); regionRepository.save(region); }
        region = regionRepository.findById(4);if (region == null){ region = new Region( 4, "AR", "03", "Cordoba"); regionRepository.save(region); }
        region = regionRepository.findById(5);if (region == null){ region = new Region( 5, "AR", "04", "Corrientes"); regionRepository.save(region); }
        region = regionRepository.findById(6);if (region == null){ region = new Region( 6, "AR", "05", "Entre Rios"); regionRepository.save(region); }
        region = regionRepository.findById(7);if (region == null){ region = new Region( 7, "AR", "06", "Jujuy"); regionRepository.save(region); }
        region = regionRepository.findById(8);if (region == null){ region = new Region( 8, "AR", "07", "Mendoza"); regionRepository.save(region); }
        region = regionRepository.findById(9);if (region == null){ region = new Region( 9, "AR", "08", "La Rioja"); regionRepository.save(region); }
        region = regionRepository.findById(10);if (region == null){ region = new Region( 10, "AR", "09", "Salta"); regionRepository.save(region); }
        region = regionRepository.findById(11);if (region == null){ region = new Region( 11, "AR", "10", "San Juan"); regionRepository.save(region); }
        region = regionRepository.findById(12);if (region == null){ region = new Region( 12, "AR", "11", "San Luis"); regionRepository.save(region); }
        region = regionRepository.findById(13);if (region == null){ region = new Region( 13, "AR", "12", "Santa Fe"); regionRepository.save(region); }
        region = regionRepository.findById(14);if (region == null){ region = new Region( 14, "AR", "13", "Santiago del Estero"); regionRepository.save(region); }
        region = regionRepository.findById(15);if (region == null){ region = new Region( 15, "AR", "14", "Tucuman"); regionRepository.save(region); }
        region = regionRepository.findById(16);if (region == null){ region = new Region( 16, "AR", "16", "Chaco"); regionRepository.save(region); }
        region = regionRepository.findById(17);if (region == null){ region = new Region( 17, "AR", "17", "Chubut"); regionRepository.save(region); }
        region = regionRepository.findById(18);if (region == null){ region = new Region( 18, "AR", "18", "Formosa"); regionRepository.save(region); }
        region = regionRepository.findById(19);if (region == null){ region = new Region( 19, "AR", "19", "Misiones"); regionRepository.save(region); }
        region = regionRepository.findById(20);if (region == null){ region = new Region( 20, "AR", "20", "Neuquen"); regionRepository.save(region); }
        region = regionRepository.findById(21);if (region == null){ region = new Region( 21, "AR", "21", "La Pampa"); regionRepository.save(region); }
        region = regionRepository.findById(22);if (region == null){ region = new Region( 22, "AR", "22", "Rio Negro"); regionRepository.save(region); }
        region = regionRepository.findById(23);if (region == null){ region = new Region( 23, "AR", "23", "Santa Cruz"); regionRepository.save(region); }
        region = regionRepository.findById(24);if (region == null){ region = new Region( 24, "AR", "24", "Tierra de Fuego"); regionRepository.save(region); }
        region = regionRepository.findById(25);if (region == null){ region = new Region( 25, "AT", "B", "Burgenland"); regionRepository.save(region); }
        region = regionRepository.findById(26);if (region == null){ region = new Region( 26, "AT", "K", "Carinthia"); regionRepository.save(region); }
        region = regionRepository.findById(27);if (region == null){ region = new Region( 27, "AT", "NOE", "Lower Austria"); regionRepository.save(region); }
        region = regionRepository.findById(28);if (region == null){ region = new Region( 28, "AT", "OOE", "Upper Austria"); regionRepository.save(region); }
        region = regionRepository.findById(29);if (region == null){ region = new Region( 29, "AT", "S", "Salzburg"); regionRepository.save(region); }
        region = regionRepository.findById(30);if (region == null){ region = new Region( 30, "AT", "ST", "Styria"); regionRepository.save(region); }
        region = regionRepository.findById(31);if (region == null){ region = new Region( 31, "AT", "T", "Tyrol"); regionRepository.save(region); }
        region = regionRepository.findById(32);if (region == null){ region = new Region( 32, "AT", "V", "Vorarlberg"); regionRepository.save(region); }
        region = regionRepository.findById(33);if (region == null){ region = new Region( 33, "AT", "W", "Vienna"); regionRepository.save(region); }
        region = regionRepository.findById(34);if (region == null){ region = new Region( 34, "AU", "ACT", "Aust Capital Terr"); regionRepository.save(region); }
        region = regionRepository.findById(35);if (region == null){ region = new Region( 35, "AU", "NSW", "New South Wales"); regionRepository.save(region); }
        region = regionRepository.findById(36);if (region == null){ region = new Region( 36, "AU", "NT", "Northern Territory"); regionRepository.save(region); }
        region = regionRepository.findById(37);if (region == null){ region = new Region( 37, "AU", "QLD", "Queensland"); regionRepository.save(region); }
        region = regionRepository.findById(38);if (region == null){ region = new Region( 38, "AU", "SA", "South Australia"); regionRepository.save(region); }
        region = regionRepository.findById(39);if (region == null){ region = new Region( 39, "AU", "TAS", "Tasmania"); regionRepository.save(region); }
        region = regionRepository.findById(40);if (region == null){ region = new Region( 40, "AU", "VIC", "Victoria"); regionRepository.save(region); }
        region = regionRepository.findById(41);if (region == null){ region = new Region( 41, "AU", "WA", "Western Australia"); regionRepository.save(region); }
        region = regionRepository.findById(42);if (region == null){ region = new Region( 42, "BE", "01", "Antwerp"); regionRepository.save(region); }
        region = regionRepository.findById(43);if (region == null){ region = new Region( 43, "BE", "02", "Brabant (Flemish)"); regionRepository.save(region); }
        region = regionRepository.findById(44);if (region == null){ region = new Region( 44, "BE", "03", "Hainaut"); regionRepository.save(region); }
        region = regionRepository.findById(45);if (region == null){ region = new Region( 45, "BE", "04", "Liege"); regionRepository.save(region); }
        region = regionRepository.findById(46);if (region == null){ region = new Region( 46, "BE", "05", "Limburg"); regionRepository.save(region); }
        region = regionRepository.findById(47);if (region == null){ region = new Region( 47, "BE", "06", "Luxembourg"); regionRepository.save(region); }
        region = regionRepository.findById(48);if (region == null){ region = new Region( 48, "BE", "07", "Namur"); regionRepository.save(region); }
        region = regionRepository.findById(49);if (region == null){ region = new Region( 49, "BE", "08", "Oost-Vlaanderen"); regionRepository.save(region); }
        region = regionRepository.findById(50);if (region == null){ region = new Region( 50, "BE", "09", "West-Vlaanderen"); regionRepository.save(region); }
        region = regionRepository.findById(51);if (region == null){ region = new Region( 51, "BE", "10", "Brabant (Walloon)"); regionRepository.save(region); }
        region = regionRepository.findById(52);if (region == null){ region = new Region( 52, "BE", "11", "Brussels (Capital)"); regionRepository.save(region); }
        region = regionRepository.findById(53);if (region == null){ region = new Region( 53, "BG", "01", "Burgas"); regionRepository.save(region); }
        region = regionRepository.findById(54);if (region == null){ region = new Region( 54, "BG", "02", "Grad Sofiya"); regionRepository.save(region); }
        region = regionRepository.findById(55);if (region == null){ region = new Region( 55, "BG", "03", "Khaskovo"); regionRepository.save(region); }
        region = regionRepository.findById(56);if (region == null){ region = new Region( 56, "BG", "04", "Lovech"); regionRepository.save(region); }
        region = regionRepository.findById(57);if (region == null){ region = new Region( 57, "BG", "05", "Montana"); regionRepository.save(region); }
        region = regionRepository.findById(58);if (region == null){ region = new Region( 58, "BG", "06", "Plovdiv"); regionRepository.save(region); }
        region = regionRepository.findById(59);if (region == null){ region = new Region( 59, "BG", "07", "Ruse"); regionRepository.save(region); }
        region = regionRepository.findById(60);if (region == null){ region = new Region( 60, "BG", "08", "Sofiya"); regionRepository.save(region); }
        region = regionRepository.findById(61);if (region == null){ region = new Region( 61, "BG", "09", "Varna"); regionRepository.save(region); }
        region = regionRepository.findById(62);if (region == null){ region = new Region( 62, "BR", "AC", "Acre"); regionRepository.save(region); }
        region = regionRepository.findById(63);if (region == null){ region = new Region( 63, "BR", "AL", "Alagoas"); regionRepository.save(region); }
        region = regionRepository.findById(64);if (region == null){ region = new Region( 64, "BR", "AM", "Amazon"); regionRepository.save(region); }
        region = regionRepository.findById(65);if (region == null){ region = new Region( 65, "BR", "AP", "Amapa"); regionRepository.save(region); }
        region = regionRepository.findById(66);if (region == null){ region = new Region( 66, "BR", "BA", "Bahia"); regionRepository.save(region); }
        region = regionRepository.findById(67);if (region == null){ region = new Region( 67, "BR", "CE", "Ceara"); regionRepository.save(region); }
        region = regionRepository.findById(68);if (region == null){ region = new Region( 68, "BR", "DF", "Brasilia"); regionRepository.save(region); }
        region = regionRepository.findById(69);if (region == null){ region = new Region( 69, "BR", "ES", "Espirito Santo"); regionRepository.save(region); }
        region = regionRepository.findById(70);if (region == null){ region = new Region( 70, "BR", "GO", "Goias"); regionRepository.save(region); }
        region = regionRepository.findById(71);if (region == null){ region = new Region( 71, "BR", "MA", "Maranhao"); regionRepository.save(region); }
        region = regionRepository.findById(72);if (region == null){ region = new Region( 72, "BR", "MG", "Minas Gerais"); regionRepository.save(region); }
        region = regionRepository.findById(73);if (region == null){ region = new Region( 73, "BR", "MS", "Mato Grosso do Sul"); regionRepository.save(region); }
        region = regionRepository.findById(74);if (region == null){ region = new Region( 74, "BR", "MT", "Mato Grosso"); regionRepository.save(region); }
        region = regionRepository.findById(75);if (region == null){ region = new Region( 75, "BR", "PA", "Para"); regionRepository.save(region); }
        region = regionRepository.findById(76);if (region == null){ region = new Region( 76, "BR", "PB", "Paraiba"); regionRepository.save(region); }
        region = regionRepository.findById(77);if (region == null){ region = new Region( 77, "BR", "PE", "Pernambuco"); regionRepository.save(region); }
        region = regionRepository.findById(78);if (region == null){ region = new Region( 78, "BR", "PI", "Piaui"); regionRepository.save(region); }
        region = regionRepository.findById(79);if (region == null){ region = new Region( 79, "BR", "PR", "Parana"); regionRepository.save(region); }
        region = regionRepository.findById(80);if (region == null){ region = new Region( 80, "BR", "RJ", "Rio de Janeiro"); regionRepository.save(region); }
        region = regionRepository.findById(81);if (region == null){ region = new Region( 81, "BR", "RN", "Rio Grande do Norte"); regionRepository.save(region); }
        region = regionRepository.findById(82);if (region == null){ region = new Region( 82, "BR", "RO", "Rondonia"); regionRepository.save(region); }
        region = regionRepository.findById(83);if (region == null){ region = new Region( 83, "BR", "RR", "Roraima"); regionRepository.save(region); }
        region = regionRepository.findById(84);if (region == null){ region = new Region( 84, "BR", "RS", "Rio Grande do Sul"); regionRepository.save(region); }
        region = regionRepository.findById(85);if (region == null){ region = new Region( 85, "BR", "SC", "Santa Catarina"); regionRepository.save(region); }
        region = regionRepository.findById(86);if (region == null){ region = new Region( 86, "BR", "SE", "Sergipe"); regionRepository.save(region); }
        region = regionRepository.findById(87);if (region == null){ region = new Region( 87, "BR", "SP", "Sao Paulo"); regionRepository.save(region); }
        region = regionRepository.findById(88);if (region == null){ region = new Region( 88, "BR", "TO", "Tocantins"); regionRepository.save(region); }
        region = regionRepository.findById(89);if (region == null){ region = new Region( 89, "CA", "AB", "Alberta"); regionRepository.save(region); }
        region = regionRepository.findById(90);if (region == null){ region = new Region( 90, "CA", "BC", "British Columbia"); regionRepository.save(region); }
        region = regionRepository.findById(91);if (region == null){ region = new Region( 91, "CA", "MB", "Manitoba"); regionRepository.save(region); }
        region = regionRepository.findById(92);if (region == null){ region = new Region( 92, "CA", "NB", "New Brunswick"); regionRepository.save(region); }
        region = regionRepository.findById(93);if (region == null){ region = new Region( 93, "CA", "NL", "Newfoundland & Labr."); regionRepository.save(region); }
        region = regionRepository.findById(94);if (region == null){ region = new Region( 94, "CA", "NS", "Nova Scotia"); regionRepository.save(region); }
        region = regionRepository.findById(95);if (region == null){ region = new Region( 95, "CA", "NT", "Northwest Terr."); regionRepository.save(region); }
        region = regionRepository.findById(96);if (region == null){ region = new Region( 96, "CA", "NU", "Nunavut"); regionRepository.save(region); }
        region = regionRepository.findById(97);if (region == null){ region = new Region( 97, "CA", "ON", "Ontario"); regionRepository.save(region); }
        region = regionRepository.findById(98);if (region == null){ region = new Region( 98, "CA", "PE", "Prince Edward Island"); regionRepository.save(region); }
        region = regionRepository.findById(99);if (region == null){ region = new Region( 99, "CA", "QC", "Quebec"); regionRepository.save(region); }
        region = regionRepository.findById(100);if (region == null){ region = new Region( 100, "CA", "SK", "Saskatchewan"); regionRepository.save(region); }
        region = regionRepository.findById(101);if (region == null){ region = new Region( 101, "CA", "YT", "Yukon Territory"); regionRepository.save(region); }
        region = regionRepository.findById(102);if (region == null){ region = new Region( 102, "CH", "AG", "Aargau"); regionRepository.save(region); }
        region = regionRepository.findById(103);if (region == null){ region = new Region( 103, "CH", "AI", "Inner-Rhoden"); regionRepository.save(region); }
        region = regionRepository.findById(104);if (region == null){ region = new Region( 104, "CH", "AR", "Ausser-Rhoden"); regionRepository.save(region); }
        region = regionRepository.findById(105);if (region == null){ region = new Region( 105, "CH", "BE", "Bern"); regionRepository.save(region); }
        region = regionRepository.findById(106);if (region == null){ region = new Region( 106, "CH", "BL", "Basel Land"); regionRepository.save(region); }
        region = regionRepository.findById(107);if (region == null){ region = new Region( 107, "CH", "BS", "Basel Stadt"); regionRepository.save(region); }
        region = regionRepository.findById(108);if (region == null){ region = new Region( 108, "CH", "FR", "Fribourg"); regionRepository.save(region); }
        region = regionRepository.findById(109);if (region == null){ region = new Region( 109, "CH", "GE", "Geneva"); regionRepository.save(region); }
        region = regionRepository.findById(110);if (region == null){ region = new Region( 110, "CH", "GL", "Glarus"); regionRepository.save(region); }
        region = regionRepository.findById(111);if (region == null){ region = new Region( 111, "CH", "GR", "Graubuenden"); regionRepository.save(region); }
        region = regionRepository.findById(112);if (region == null){ region = new Region( 112, "CH", "JU", "Jura"); regionRepository.save(region); }
        region = regionRepository.findById(113);if (region == null){ region = new Region( 113, "CH", "LU", "Lucerne"); regionRepository.save(region); }
        region = regionRepository.findById(114);if (region == null){ region = new Region( 114, "CH", "NE", "Neuchatel"); regionRepository.save(region); }
        region = regionRepository.findById(115);if (region == null){ region = new Region( 115, "CH", "NW", "Nidwalden"); regionRepository.save(region); }
        region = regionRepository.findById(116);if (region == null){ region = new Region( 116, "CH", "OW", "Obwalden"); regionRepository.save(region); }
        region = regionRepository.findById(117);if (region == null){ region = new Region( 117, "CH", "SG", "St. Gallen"); regionRepository.save(region); }
        region = regionRepository.findById(118);if (region == null){ region = new Region( 118, "CH", "SH", "Schaffhausen"); regionRepository.save(region); }
        region = regionRepository.findById(119);if (region == null){ region = new Region( 119, "CH", "SO", "Solothurn"); regionRepository.save(region); }
        region = regionRepository.findById(120);if (region == null){ region = new Region( 120, "CH", "SZ", "Schwyz"); regionRepository.save(region); }
        region = regionRepository.findById(121);if (region == null){ region = new Region( 121, "CH", "TG", "Thurgau"); regionRepository.save(region); }
        region = regionRepository.findById(122);if (region == null){ region = new Region( 122, "CH", "TI", "Ticino"); regionRepository.save(region); }
        region = regionRepository.findById(123);if (region == null){ region = new Region( 123, "CH", "UR", "Uri"); regionRepository.save(region); }
        region = regionRepository.findById(124);if (region == null){ region = new Region( 124, "CH", "VD", "Vaud"); regionRepository.save(region); }
        region = regionRepository.findById(125);if (region == null){ region = new Region( 125, "CH", "VS", "Valais"); regionRepository.save(region); }
        region = regionRepository.findById(126);if (region == null){ region = new Region( 126, "CH", "ZG", "Zug"); regionRepository.save(region); }
        region = regionRepository.findById(127);if (region == null){ region = new Region( 127, "CH", "ZH", "Zurich"); regionRepository.save(region); }
        region = regionRepository.findById(128);if (region == null){ region = new Region( 128, "CL", "01", "I - Iquique"); regionRepository.save(region); }
        region = regionRepository.findById(129);if (region == null){ region = new Region( 129, "CL", "02", "II - Antofagasta"); regionRepository.save(region); }
        region = regionRepository.findById(130);if (region == null){ region = new Region( 130, "CL", "03", "III - Copiapo"); regionRepository.save(region); }
        region = regionRepository.findById(131);if (region == null){ region = new Region( 131, "CL", "04", "IV - La Serena"); regionRepository.save(region); }
        region = regionRepository.findById(132);if (region == null){ region = new Region( 132, "CL", "05", "V - Valparaiso"); regionRepository.save(region); }
        region = regionRepository.findById(133);if (region == null){ region = new Region( 133, "CL", "06", "VI - Rancagua"); regionRepository.save(region); }
        region = regionRepository.findById(134);if (region == null){ region = new Region( 134, "CL", "07", "VII - Talca"); regionRepository.save(region); }
        region = regionRepository.findById(135);if (region == null){ region = new Region( 135, "CL", "08", "VIII - Concepcion"); regionRepository.save(region); }
        region = regionRepository.findById(136);if (region == null){ region = new Region( 136, "CL", "09", "IX - Temuco"); regionRepository.save(region); }
        region = regionRepository.findById(137);if (region == null){ region = new Region( 137, "CL", "10", "X - Puerto Montt"); regionRepository.save(region); }
        region = regionRepository.findById(138);if (region == null){ region = new Region( 138, "CL", "11", "XI - Coyhaique"); regionRepository.save(region); }
        region = regionRepository.findById(139);if (region == null){ region = new Region( 139, "CL", "12", "XII - Punta Arenas"); regionRepository.save(region); }
        region = regionRepository.findById(140);if (region == null){ region = new Region( 140, "CL", "13", "RM - Santiago"); regionRepository.save(region); }
        region = regionRepository.findById(141);if (region == null){ region = new Region( 141, "CN", "010", "Beijing"); regionRepository.save(region); }
        region = regionRepository.findById(142);if (region == null){ region = new Region( 142, "CN", "020", "Shanghai"); regionRepository.save(region); }
        region = regionRepository.findById(143);if (region == null){ region = new Region( 143, "CN", "030", "Tianjin"); regionRepository.save(region); }
        region = regionRepository.findById(144);if (region == null){ region = new Region( 144, "CN", "040", "Nei Mongol"); regionRepository.save(region); }
        region = regionRepository.findById(145);if (region == null){ region = new Region( 145, "CN", "050", "Shanxi"); regionRepository.save(region); }
        region = regionRepository.findById(146);if (region == null){ region = new Region( 146, "CN", "060", "Hebei"); regionRepository.save(region); }
        region = regionRepository.findById(147);if (region == null){ region = new Region( 147, "CN", "070", "Liaoning"); regionRepository.save(region); }
        region = regionRepository.findById(148);if (region == null){ region = new Region( 148, "CN", "080", "Jilin"); regionRepository.save(region); }
        region = regionRepository.findById(149);if (region == null){ region = new Region( 149, "CN", "090", "Heilongjiang"); regionRepository.save(region); }
        region = regionRepository.findById(150);if (region == null){ region = new Region( 150, "CN", "100", "Jiangsu"); regionRepository.save(region); }
        region = regionRepository.findById(151);if (region == null){ region = new Region( 151, "CN", "110", "Anhui"); regionRepository.save(region); }
        region = regionRepository.findById(152);if (region == null){ region = new Region( 152, "CN", "120", "Shandong"); regionRepository.save(region); }
        region = regionRepository.findById(153);if (region == null){ region = new Region( 153, "CN", "130", "Zhejiang"); regionRepository.save(region); }
        region = regionRepository.findById(154);if (region == null){ region = new Region( 154, "CN", "140", "Jiangxi"); regionRepository.save(region); }
        region = regionRepository.findById(155);if (region == null){ region = new Region( 155, "CN", "150", "Fujian"); regionRepository.save(region); }
        region = regionRepository.findById(156);if (region == null){ region = new Region( 156, "CN", "160", "Hunan"); regionRepository.save(region); }
        region = regionRepository.findById(157);if (region == null){ region = new Region( 157, "CN", "170", "Hubei"); regionRepository.save(region); }
        region = regionRepository.findById(158);if (region == null){ region = new Region( 158, "CN", "180", "Henan"); regionRepository.save(region); }
        region = regionRepository.findById(159);if (region == null){ region = new Region( 159, "CN", "190", "Guangdong"); regionRepository.save(region); }
        region = regionRepository.findById(160);if (region == null){ region = new Region( 160, "CN", "200", "Hainan"); regionRepository.save(region); }
        region = regionRepository.findById(161);if (region == null){ region = new Region( 161, "CN", "210", "Guangxi"); regionRepository.save(region); }
        region = regionRepository.findById(162);if (region == null){ region = new Region( 162, "CN", "220", "Guizhou"); regionRepository.save(region); }
        region = regionRepository.findById(163);if (region == null){ region = new Region( 163, "CN", "230", "Sichuan"); regionRepository.save(region); }
        region = regionRepository.findById(164);if (region == null){ region = new Region( 164, "CN", "240", "Yunnan"); regionRepository.save(region); }
        region = regionRepository.findById(165);if (region == null){ region = new Region( 165, "CN", "250", "Shaanxi"); regionRepository.save(region); }
        region = regionRepository.findById(166);if (region == null){ region = new Region( 166, "CN", "260", "Gansu"); regionRepository.save(region); }
        region = regionRepository.findById(167);if (region == null){ region = new Region( 167, "CN", "270", "Ningxia"); regionRepository.save(region); }
        region = regionRepository.findById(168);if (region == null){ region = new Region( 168, "CN", "280", "Qinghai"); regionRepository.save(region); }
        region = regionRepository.findById(169);if (region == null){ region = new Region( 169, "CN", "290", "Xinjiang"); regionRepository.save(region); }
        region = regionRepository.findById(170);if (region == null){ region = new Region( 170, "CN", "300", "Xizang"); regionRepository.save(region); }
        region = regionRepository.findById(171);if (region == null){ region = new Region( 171, "CN", "320", "Chong Qing"); regionRepository.save(region); }
        region = regionRepository.findById(172);if (region == null){ region = new Region( 172, "CO", "05", "ANTIOQUIA"); regionRepository.save(region); }
        region = regionRepository.findById(173);if (region == null){ region = new Region( 173, "CO", "08", "ATLANTICO"); regionRepository.save(region); }
        region = regionRepository.findById(174);if (region == null){ region = new Region( 174, "CO", "11", "BOGOTA"); regionRepository.save(region); }
        region = regionRepository.findById(175);if (region == null){ region = new Region( 175, "CO", "13", "BOLIVAR"); regionRepository.save(region); }
        region = regionRepository.findById(176);if (region == null){ region = new Region( 176, "CO", "15", "BOYACA"); regionRepository.save(region); }
        region = regionRepository.findById(177);if (region == null){ region = new Region( 177, "CO", "17", "CALDAS"); regionRepository.save(region); }
        region = regionRepository.findById(178);if (region == null){ region = new Region( 178, "CO", "18", "CAQUETA"); regionRepository.save(region); }
        region = regionRepository.findById(179);if (region == null){ region = new Region( 179, "CO", "19", "CAUCA"); regionRepository.save(region); }
        region = regionRepository.findById(180);if (region == null){ region = new Region( 180, "CO", "20", "CESAR"); regionRepository.save(region); }
        region = regionRepository.findById(181);if (region == null){ region = new Region( 181, "CO", "23", "CORDOBA"); regionRepository.save(region); }
        region = regionRepository.findById(182);if (region == null){ region = new Region( 182, "CO", "25", "CUNDINAMARCA"); regionRepository.save(region); }
        region = regionRepository.findById(183);if (region == null){ region = new Region( 183, "CO", "27", "CHOCO"); regionRepository.save(region); }
        region = regionRepository.findById(184);if (region == null){ region = new Region( 184, "CO", "41", "HUILA"); regionRepository.save(region); }
        region = regionRepository.findById(185);if (region == null){ region = new Region( 185, "CO", "44", "LA GUAJIRA"); regionRepository.save(region); }
        region = regionRepository.findById(186);if (region == null){ region = new Region( 186, "CO", "47", "MAGDALENA"); regionRepository.save(region); }
        region = regionRepository.findById(187);if (region == null){ region = new Region( 187, "CO", "50", "META"); regionRepository.save(region); }
        region = regionRepository.findById(188);if (region == null){ region = new Region( 188, "CO", "52", "NARINO"); regionRepository.save(region); }
        region = regionRepository.findById(189);if (region == null){ region = new Region( 189, "CO", "54", "NORTE SANTANDER"); regionRepository.save(region); }
        region = regionRepository.findById(190);if (region == null){ region = new Region( 190, "CO", "63", "QUINDIO"); regionRepository.save(region); }
        region = regionRepository.findById(191);if (region == null){ region = new Region( 191, "CO", "66", "RISARALDA"); regionRepository.save(region); }
        region = regionRepository.findById(192);if (region == null){ region = new Region( 192, "CO", "68", "SANTANDER"); regionRepository.save(region); }
        region = regionRepository.findById(193);if (region == null){ region = new Region( 193, "CO", "70", "SUCRE"); regionRepository.save(region); }
        region = regionRepository.findById(194);if (region == null){ region = new Region( 194, "CO", "73", "TOLIMA"); regionRepository.save(region); }
        region = regionRepository.findById(195);if (region == null){ region = new Region( 195, "CO", "76", "VALLE"); regionRepository.save(region); }
        region = regionRepository.findById(196);if (region == null){ region = new Region( 196, "CO", "81", "ARAUCA"); regionRepository.save(region); }
        region = regionRepository.findById(197);if (region == null){ region = new Region( 197, "CO", "85", "CASANARE"); regionRepository.save(region); }
        region = regionRepository.findById(198);if (region == null){ region = new Region( 198, "CO", "86", "PUTUMAYO"); regionRepository.save(region); }
        region = regionRepository.findById(199);if (region == null){ region = new Region( 199, "CO", "88", "SAN ANDRES"); regionRepository.save(region); }
        region = regionRepository.findById(200);if (region == null){ region = new Region( 200, "CO", "91", "AMAZONAS"); regionRepository.save(region); }
        region = regionRepository.findById(201);if (region == null){ region = new Region( 201, "CO", "94", "GUAINIA"); regionRepository.save(region); }
        region = regionRepository.findById(202);if (region == null){ region = new Region( 202, "CO", "95", "GUAVIARE"); regionRepository.save(region); }
        region = regionRepository.findById(203);if (region == null){ region = new Region( 203, "CO", "97", "VAUPES"); regionRepository.save(region); }
        region = regionRepository.findById(204);if (region == null){ region = new Region( 204, "CO", "99", "VICHADA"); regionRepository.save(region); }
        region = regionRepository.findById(205);if (region == null){ region = new Region( 205, "CZ", "11", "Praha"); regionRepository.save(region); }
        region = regionRepository.findById(206);if (region == null){ region = new Region( 206, "CZ", "21", "Stredocesky"); regionRepository.save(region); }
        region = regionRepository.findById(207);if (region == null){ region = new Region( 207, "CZ", "31", "Jihocesky"); regionRepository.save(region); }
        region = regionRepository.findById(208);if (region == null){ region = new Region( 208, "CZ", "32", "Plzensky"); regionRepository.save(region); }
        region = regionRepository.findById(209);if (region == null){ region = new Region( 209, "CZ", "41", "Karlovarsky"); regionRepository.save(region); }
        region = regionRepository.findById(210);if (region == null){ region = new Region( 210, "CZ", "42", "Ustecky"); regionRepository.save(region); }
        region = regionRepository.findById(211);if (region == null){ region = new Region( 211, "CZ", "51", "Liberecky"); regionRepository.save(region); }
        region = regionRepository.findById(212);if (region == null){ region = new Region( 212, "CZ", "52", "Kralovehradecky"); regionRepository.save(region); }
        region = regionRepository.findById(213);if (region == null){ region = new Region( 213, "CZ", "53", "Pardubicky"); regionRepository.save(region); }
        region = regionRepository.findById(214);if (region == null){ region = new Region( 214, "CZ", "61", "Vysocina"); regionRepository.save(region); }
        region = regionRepository.findById(215);if (region == null){ region = new Region( 215, "CZ", "62", "Jihomoravsky"); regionRepository.save(region); }
        region = regionRepository.findById(216);if (region == null){ region = new Region( 216, "CZ", "71", "Olomoucky"); regionRepository.save(region); }
        region = regionRepository.findById(217);if (region == null){ region = new Region( 217, "CZ", "72", "Zlinsky"); regionRepository.save(region); }
        region = regionRepository.findById(218);if (region == null){ region = new Region( 218, "CZ", "81", "Moravskoslezsky"); regionRepository.save(region); }
        region = regionRepository.findById(219);if (region == null){ region = new Region( 219, "DE", "01", "Schleswig-Holstein"); regionRepository.save(region); }
        region = regionRepository.findById(220);if (region == null){ region = new Region( 220, "DE", "02", "Hamburg"); regionRepository.save(region); }
        region = regionRepository.findById(221);if (region == null){ region = new Region( 221, "DE", "03", "Lower Saxony"); regionRepository.save(region); }
        region = regionRepository.findById(222);if (region == null){ region = new Region( 222, "DE", "04", "Bremen"); regionRepository.save(region); }
        region = regionRepository.findById(223);if (region == null){ region = new Region( 223, "DE", "05", "Nrth Rhine Westfalia"); regionRepository.save(region); }
        region = regionRepository.findById(224);if (region == null){ region = new Region( 224, "DE", "06", "Hessen"); regionRepository.save(region); }
        region = regionRepository.findById(225);if (region == null){ region = new Region( 225, "DE", "07", "Rhineland Palatinate"); regionRepository.save(region); }
        region = regionRepository.findById(226);if (region == null){ region = new Region( 226, "DE", "08", "Baden-Wurttemberg"); regionRepository.save(region); }
        region = regionRepository.findById(227);if (region == null){ region = new Region( 227, "DE", "09", "Bavaria"); regionRepository.save(region); }
        region = regionRepository.findById(228);if (region == null){ region = new Region( 228, "DE", "10", "Saarland"); regionRepository.save(region); }
        region = regionRepository.findById(229);if (region == null){ region = new Region( 229, "DE", "11", "Berlin"); regionRepository.save(region); }
        region = regionRepository.findById(230);if (region == null){ region = new Region( 230, "DE", "12", "Brandenburg"); regionRepository.save(region); }
        region = regionRepository.findById(231);if (region == null){ region = new Region( 231, "DE", "13", "Mecklenburg-Vorpomm."); regionRepository.save(region); }
        region = regionRepository.findById(232);if (region == null){ region = new Region( 232, "DE", "14", "Saxony"); regionRepository.save(region); }
        region = regionRepository.findById(233);if (region == null){ region = new Region( 233, "DE", "15", "Saxony-Anhalt"); regionRepository.save(region); }
        region = regionRepository.findById(234);if (region == null){ region = new Region( 234, "DE", "16", "Thuringia"); regionRepository.save(region); }
        region = regionRepository.findById(235);if (region == null){ region = new Region( 235, "DK", "001", "Danish Capital Reg."); regionRepository.save(region); }
        region = regionRepository.findById(236);if (region == null){ region = new Region( 236, "DK", "002", "Central Jutland"); regionRepository.save(region); }
        region = regionRepository.findById(237);if (region == null){ region = new Region( 237, "DK", "003", "North Jutland"); regionRepository.save(region); }
        region = regionRepository.findById(238);if (region == null){ region = new Region( 238, "DK", "004", "Zealand"); regionRepository.save(region); }
        region = regionRepository.findById(239);if (region == null){ region = new Region( 239, "DK", "005", "South Denmark"); regionRepository.save(region); }
        region = regionRepository.findById(240);if (region == null){ region = new Region( 240, "ES", "01", "Alava"); regionRepository.save(region); }
        region = regionRepository.findById(241);if (region == null){ region = new Region( 241, "ES", "02", "Albacete"); regionRepository.save(region); }
        region = regionRepository.findById(242);if (region == null){ region = new Region( 242, "ES", "03", "Alicante"); regionRepository.save(region); }
        region = regionRepository.findById(243);if (region == null){ region = new Region( 243, "ES", "04", "Almeria"); regionRepository.save(region); }
        region = regionRepository.findById(244);if (region == null){ region = new Region( 244, "ES", "05", "Avila"); regionRepository.save(region); }
        region = regionRepository.findById(245);if (region == null){ region = new Region( 245, "ES", "06", "Badajoz"); regionRepository.save(region); }
        region = regionRepository.findById(246);if (region == null){ region = new Region( 246, "ES", "07", "Baleares"); regionRepository.save(region); }
        region = regionRepository.findById(247);if (region == null){ region = new Region( 247, "ES", "08", "Barcelona"); regionRepository.save(region); }
        region = regionRepository.findById(248);if (region == null){ region = new Region( 248, "ES", "09", "Burgos"); regionRepository.save(region); }
        region = regionRepository.findById(249);if (region == null){ region = new Region( 249, "ES", "10", "Caceres"); regionRepository.save(region); }
        region = regionRepository.findById(250);if (region == null){ region = new Region( 250, "ES", "11", "Cadiz"); regionRepository.save(region); }
        region = regionRepository.findById(251);if (region == null){ region = new Region( 251, "ES", "12", "Castellon"); regionRepository.save(region); }
        region = regionRepository.findById(252);if (region == null){ region = new Region( 252, "ES", "13", "Ciudad Real"); regionRepository.save(region); }
        region = regionRepository.findById(253);if (region == null){ region = new Region( 253, "ES", "14", "Cordoba"); regionRepository.save(region); }
        region = regionRepository.findById(254);if (region == null){ region = new Region( 254, "ES", "15", "La Coruna"); regionRepository.save(region); }
        region = regionRepository.findById(255);if (region == null){ region = new Region( 255, "ES", "16", "Cuenca"); regionRepository.save(region); }
        region = regionRepository.findById(256);if (region == null){ region = new Region( 256, "ES", "17", "Gerona"); regionRepository.save(region); }
        region = regionRepository.findById(257);if (region == null){ region = new Region( 257, "ES", "18", "Granada"); regionRepository.save(region); }
        region = regionRepository.findById(258);if (region == null){ region = new Region( 258, "ES", "19", "Guadalajara"); regionRepository.save(region); }
        region = regionRepository.findById(259);if (region == null){ region = new Region( 259, "ES", "20", "Guipuzcoa"); regionRepository.save(region); }
        region = regionRepository.findById(260);if (region == null){ region = new Region( 260, "ES", "21", "Huelva"); regionRepository.save(region); }
        region = regionRepository.findById(261);if (region == null){ region = new Region( 261, "ES", "22", "Huesca"); regionRepository.save(region); }
        region = regionRepository.findById(262);if (region == null){ region = new Region( 262, "ES", "23", "Jaen"); regionRepository.save(region); }
        region = regionRepository.findById(263);if (region == null){ region = new Region( 263, "ES", "24", "Leon"); regionRepository.save(region); }
        region = regionRepository.findById(264);if (region == null){ region = new Region( 264, "ES", "25", "Lerida"); regionRepository.save(region); }
        region = regionRepository.findById(265);if (region == null){ region = new Region( 265, "ES", "26", "La Rioja"); regionRepository.save(region); }
        region = regionRepository.findById(266);if (region == null){ region = new Region( 266, "ES", "27", "Lugo"); regionRepository.save(region); }
        region = regionRepository.findById(267);if (region == null){ region = new Region( 267, "ES", "28", "Madrid"); regionRepository.save(region); }
        region = regionRepository.findById(268);if (region == null){ region = new Region( 268, "ES", "29", "Malaga"); regionRepository.save(region); }
        region = regionRepository.findById(269);if (region == null){ region = new Region( 269, "ES", "30", "Murcia"); regionRepository.save(region); }
        region = regionRepository.findById(270);if (region == null){ region = new Region( 270, "ES", "31", "Navarra"); regionRepository.save(region); }
        region = regionRepository.findById(271);if (region == null){ region = new Region( 271, "ES", "32", "Orense"); regionRepository.save(region); }
        region = regionRepository.findById(272);if (region == null){ region = new Region( 272, "ES", "33", "Asturias"); regionRepository.save(region); }
        region = regionRepository.findById(273);if (region == null){ region = new Region( 273, "ES", "34", "Palencia"); regionRepository.save(region); }
        region = regionRepository.findById(274);if (region == null){ region = new Region( 274, "ES", "35", "Las Palmas"); regionRepository.save(region); }
        region = regionRepository.findById(275);if (region == null){ region = new Region( 275, "ES", "36", "Pontevedra"); regionRepository.save(region); }
        region = regionRepository.findById(276);if (region == null){ region = new Region( 276, "ES", "37", "Salamanca"); regionRepository.save(region); }
        region = regionRepository.findById(277);if (region == null){ region = new Region( 277, "ES", "38", "Sta. Cruz Tenerife"); regionRepository.save(region); }
        region = regionRepository.findById(278);if (region == null){ region = new Region( 278, "ES", "39", "Cantabria"); regionRepository.save(region); }
        region = regionRepository.findById(279);if (region == null){ region = new Region( 279, "ES", "40", "Segovia"); regionRepository.save(region); }
        region = regionRepository.findById(280);if (region == null){ region = new Region( 280, "ES", "41", "Sevilla"); regionRepository.save(region); }
        region = regionRepository.findById(281);if (region == null){ region = new Region( 281, "ES", "42", "Soria"); regionRepository.save(region); }
        region = regionRepository.findById(282);if (region == null){ region = new Region( 282, "ES", "43", "Tarragona"); regionRepository.save(region); }
        region = regionRepository.findById(283);if (region == null){ region = new Region( 283, "ES", "44", "Teruel"); regionRepository.save(region); }
        region = regionRepository.findById(284);if (region == null){ region = new Region( 284, "ES", "45", "Toledo"); regionRepository.save(region); }
        region = regionRepository.findById(285);if (region == null){ region = new Region( 285, "ES", "46", "Valencia"); regionRepository.save(region); }
        region = regionRepository.findById(286);if (region == null){ region = new Region( 286, "ES", "47", "Valladolid"); regionRepository.save(region); }
        region = regionRepository.findById(287);if (region == null){ region = new Region( 287, "ES", "48", "Vizcaya"); regionRepository.save(region); }
        region = regionRepository.findById(288);if (region == null){ region = new Region( 288, "ES", "49", "Zamora"); regionRepository.save(region); }
        region = regionRepository.findById(289);if (region == null){ region = new Region( 289, "ES", "50", "Zaragoza"); regionRepository.save(region); }
        region = regionRepository.findById(290);if (region == null){ region = new Region( 290, "FI", "001", "Ahvenanmaa"); regionRepository.save(region); }
        region = regionRepository.findById(291);if (region == null){ region = new Region( 291, "FI", "002", "Southern Finnland"); regionRepository.save(region); }
        region = regionRepository.findById(292);if (region == null){ region = new Region( 292, "FI", "003", "Eastern Finnland"); regionRepository.save(region); }
        region = regionRepository.findById(293);if (region == null){ region = new Region( 293, "FI", "004", "Lappi"); regionRepository.save(region); }
        region = regionRepository.findById(294);if (region == null){ region = new Region( 294, "FI", "005", "Western Finnland"); regionRepository.save(region); }
        region = regionRepository.findById(295);if (region == null){ region = new Region( 295, "FI", "006", "Oulu"); regionRepository.save(region); }
        region = regionRepository.findById(296);if (region == null){ region = new Region( 296, "FR", "01", "Ain"); regionRepository.save(region); }
        region = regionRepository.findById(297);if (region == null){ region = new Region( 297, "FR", "02", "Aisne"); regionRepository.save(region); }
        region = regionRepository.findById(298);if (region == null){ region = new Region( 298, "FR", "03", "Allier"); regionRepository.save(region); }
        region = regionRepository.findById(299);if (region == null){ region = new Region( 299, "FR", "04", "Alpes (Hte-Provence)"); regionRepository.save(region); }
        region = regionRepository.findById(300);if (region == null){ region = new Region( 300, "FR", "05", "Alpes (Hautes)"); regionRepository.save(region); }
        region = regionRepository.findById(301);if (region == null){ region = new Region( 301, "FR", "06", "Alpes-Maritimes"); regionRepository.save(region); }
        region = regionRepository.findById(302);if (region == null){ region = new Region( 302, "FR", "07", "Ardeche"); regionRepository.save(region); }
        region = regionRepository.findById(303);if (region == null){ region = new Region( 303, "FR", "08", "Ardennes"); regionRepository.save(region); }
        region = regionRepository.findById(304);if (region == null){ region = new Region( 304, "FR", "09", "Ariege"); regionRepository.save(region); }
        region = regionRepository.findById(305);if (region == null){ region = new Region( 305, "FR", "10", "Aube"); regionRepository.save(region); }
        region = regionRepository.findById(306);if (region == null){ region = new Region( 306, "FR", "11", "Aude"); regionRepository.save(region); }
        region = regionRepository.findById(307);if (region == null){ region = new Region( 307, "FR", "12", "Aveyron"); regionRepository.save(region); }
        region = regionRepository.findById(308);if (region == null){ region = new Region( 308, "FR", "13", "Bouches-du-Rhone"); regionRepository.save(region); }
        region = regionRepository.findById(309);if (region == null){ region = new Region( 309, "FR", "14", "Calvados"); regionRepository.save(region); }
        region = regionRepository.findById(310);if (region == null){ region = new Region( 310, "FR", "15", "Cantal"); regionRepository.save(region); }
        region = regionRepository.findById(311);if (region == null){ region = new Region( 311, "FR", "16", "Charente"); regionRepository.save(region); }
        region = regionRepository.findById(312);if (region == null){ region = new Region( 312, "FR", "17", "Charente-Maritime"); regionRepository.save(region); }
        region = regionRepository.findById(313);if (region == null){ region = new Region( 313, "FR", "18", "Cher"); regionRepository.save(region); }
        region = regionRepository.findById(314);if (region == null){ region = new Region( 314, "FR", "19", "Correze"); regionRepository.save(region); }
        region = regionRepository.findById(315);if (region == null){ region = new Region( 315, "FR", "21", "Cote-d'Or"); regionRepository.save(region); }
        region = regionRepository.findById(316);if (region == null){ region = new Region( 316, "FR", "22", "Cotes-d'Armor"); regionRepository.save(region); }
        region = regionRepository.findById(317);if (region == null){ region = new Region( 317, "FR", "23", "Creuse"); regionRepository.save(region); }
        region = regionRepository.findById(318);if (region == null){ region = new Region( 318, "FR", "24", "Dordogne"); regionRepository.save(region); }
        region = regionRepository.findById(319);if (region == null){ region = new Region( 319, "FR", "25", "Doubs"); regionRepository.save(region); }
        region = regionRepository.findById(320);if (region == null){ region = new Region( 320, "FR", "26", "Drome"); regionRepository.save(region); }
        region = regionRepository.findById(321);if (region == null){ region = new Region( 321, "FR", "27", "Eure"); regionRepository.save(region); }
        region = regionRepository.findById(322);if (region == null){ region = new Region( 322, "FR", "28", "Eure-et-Loir"); regionRepository.save(region); }
        region = regionRepository.findById(323);if (region == null){ region = new Region( 323, "FR", "29", "Finistere"); regionRepository.save(region); }
        region = regionRepository.findById(324);if (region == null){ region = new Region( 324, "FR", "2A", "Corse-du-Sud"); regionRepository.save(region); }
        region = regionRepository.findById(325);if (region == null){ region = new Region( 325, "FR", "2B", "Corse-du-Nord"); regionRepository.save(region); }
        region = regionRepository.findById(326);if (region == null){ region = new Region( 326, "FR", "30", "Gard"); regionRepository.save(region); }
        region = regionRepository.findById(327);if (region == null){ region = new Region( 327, "FR", "31", "Garonne (Haute)"); regionRepository.save(region); }
        region = regionRepository.findById(328);if (region == null){ region = new Region( 328, "FR", "32", "Gers"); regionRepository.save(region); }
        region = regionRepository.findById(329);if (region == null){ region = new Region( 329, "FR", "33", "Gironde"); regionRepository.save(region); }
        region = regionRepository.findById(330);if (region == null){ region = new Region( 330, "FR", "34", "Herault"); regionRepository.save(region); }
        region = regionRepository.findById(331);if (region == null){ region = new Region( 331, "FR", "35", "Ille-et-Vilaine"); regionRepository.save(region); }
        region = regionRepository.findById(332);if (region == null){ region = new Region( 332, "FR", "36", "Indre"); regionRepository.save(region); }
        region = regionRepository.findById(333);if (region == null){ region = new Region( 333, "FR", "37", "Indre-et-Loire"); regionRepository.save(region); }
        region = regionRepository.findById(334);if (region == null){ region = new Region( 334, "FR", "38", "Isere"); regionRepository.save(region); }
        region = regionRepository.findById(335);if (region == null){ region = new Region( 335, "FR", "39", "Jura"); regionRepository.save(region); }
        region = regionRepository.findById(336);if (region == null){ region = new Region( 336, "FR", "40", "Landes"); regionRepository.save(region); }
        region = regionRepository.findById(337);if (region == null){ region = new Region( 337, "FR", "41", "Loir-et-Cher"); regionRepository.save(region); }
        region = regionRepository.findById(338);if (region == null){ region = new Region( 338, "FR", "42", "Loire"); regionRepository.save(region); }
        region = regionRepository.findById(339);if (region == null){ region = new Region( 339, "FR", "43", "Loire (Haute)"); regionRepository.save(region); }
        region = regionRepository.findById(340);if (region == null){ region = new Region( 340, "FR", "44", "Loire-Atlantique"); regionRepository.save(region); }
        region = regionRepository.findById(341);if (region == null){ region = new Region( 341, "FR", "45", "Loiret"); regionRepository.save(region); }
        region = regionRepository.findById(342);if (region == null){ region = new Region( 342, "FR", "46", "Lot"); regionRepository.save(region); }
        region = regionRepository.findById(343);if (region == null){ region = new Region( 343, "FR", "47", "Lot-et-Garonne"); regionRepository.save(region); }
        region = regionRepository.findById(344);if (region == null){ region = new Region( 344, "FR", "48", "Lozere"); regionRepository.save(region); }
        region = regionRepository.findById(345);if (region == null){ region = new Region( 345, "FR", "49", "Maine-et-Loire"); regionRepository.save(region); }
        region = regionRepository.findById(346);if (region == null){ region = new Region( 346, "FR", "50", "Manche"); regionRepository.save(region); }
        region = regionRepository.findById(347);if (region == null){ region = new Region( 347, "FR", "51", "Marne"); regionRepository.save(region); }
        region = regionRepository.findById(348);if (region == null){ region = new Region( 348, "FR", "52", "Marne (Haute)"); regionRepository.save(region); }
        region = regionRepository.findById(349);if (region == null){ region = new Region( 349, "FR", "53", "Mayenne"); regionRepository.save(region); }
        region = regionRepository.findById(350);if (region == null){ region = new Region( 350, "FR", "54", "Meurthe-et-Moselle"); regionRepository.save(region); }
        region = regionRepository.findById(351);if (region == null){ region = new Region( 351, "FR", "55", "Meuse"); regionRepository.save(region); }
        region = regionRepository.findById(352);if (region == null){ region = new Region( 352, "FR", "56", "Morbihan"); regionRepository.save(region); }
        region = regionRepository.findById(353);if (region == null){ region = new Region( 353, "FR", "57", "Moselle"); regionRepository.save(region); }
        region = regionRepository.findById(354);if (region == null){ region = new Region( 354, "FR", "58", "Nievre"); regionRepository.save(region); }
        region = regionRepository.findById(355);if (region == null){ region = new Region( 355, "FR", "59", "Nord"); regionRepository.save(region); }
        region = regionRepository.findById(356);if (region == null){ region = new Region( 356, "FR", "60", "Oise"); regionRepository.save(region); }
        region = regionRepository.findById(357);if (region == null){ region = new Region( 357, "FR", "61", "Orne"); regionRepository.save(region); }
        region = regionRepository.findById(358);if (region == null){ region = new Region( 358, "FR", "62", "Pas-de-Calais"); regionRepository.save(region); }
        region = regionRepository.findById(359);if (region == null){ region = new Region( 359, "FR", "63", "Puy-de-Dome"); regionRepository.save(region); }
        region = regionRepository.findById(360);if (region == null){ region = new Region( 360, "FR", "64", "Pyrenees-Atlantiques"); regionRepository.save(region); }
        region = regionRepository.findById(361);if (region == null){ region = new Region( 361, "FR", "65", "Pyrenees (Hautes)"); regionRepository.save(region); }
        region = regionRepository.findById(362);if (region == null){ region = new Region( 362, "FR", "66", "Pyrenees-Orientales"); regionRepository.save(region); }
        region = regionRepository.findById(363);if (region == null){ region = new Region( 363, "FR", "67", "Bas-Rhin"); regionRepository.save(region); }
        region = regionRepository.findById(364);if (region == null){ region = new Region( 364, "FR", "68", "Haut-Rhin"); regionRepository.save(region); }
        region = regionRepository.findById(365);if (region == null){ region = new Region( 365, "FR", "69", "Rhone"); regionRepository.save(region); }
        region = regionRepository.findById(366);if (region == null){ region = new Region( 366, "FR", "70", "Saone (Haute)"); regionRepository.save(region); }
        region = regionRepository.findById(367);if (region == null){ region = new Region( 367, "FR", "71", "Saone-et-Loire"); regionRepository.save(region); }
        region = regionRepository.findById(368);if (region == null){ region = new Region( 368, "FR", "72", "Sarthe"); regionRepository.save(region); }
        region = regionRepository.findById(369);if (region == null){ region = new Region( 369, "FR", "73", "Savoie"); regionRepository.save(region); }
        region = regionRepository.findById(370);if (region == null){ region = new Region( 370, "FR", "74", "Savoie (Haute)"); regionRepository.save(region); }
        region = regionRepository.findById(371);if (region == null){ region = new Region( 371, "FR", "75", "Paris"); regionRepository.save(region); }
        region = regionRepository.findById(372);if (region == null){ region = new Region( 372, "FR", "76", "Seine-Maritime"); regionRepository.save(region); }
        region = regionRepository.findById(373);if (region == null){ region = new Region( 373, "FR", "77", "Seine-et-Marne"); regionRepository.save(region); }
        region = regionRepository.findById(374);if (region == null){ region = new Region( 374, "FR", "78", "Yvelines"); regionRepository.save(region); }
        region = regionRepository.findById(375);if (region == null){ region = new Region( 375, "FR", "79", "Sevres (Deux)"); regionRepository.save(region); }
        region = regionRepository.findById(376);if (region == null){ region = new Region( 376, "FR", "80", "Somme"); regionRepository.save(region); }
        region = regionRepository.findById(377);if (region == null){ region = new Region( 377, "FR", "81", "Tarn"); regionRepository.save(region); }
        region = regionRepository.findById(378);if (region == null){ region = new Region( 378, "FR", "82", "Tarn-et-Garonne"); regionRepository.save(region); }
        region = regionRepository.findById(379);if (region == null){ region = new Region( 379, "FR", "83", "Var"); regionRepository.save(region); }
        region = regionRepository.findById(380);if (region == null){ region = new Region( 380, "FR", "84", "Vaucluse"); regionRepository.save(region); }
        region = regionRepository.findById(381);if (region == null){ region = new Region( 381, "FR", "85", "Vendee"); regionRepository.save(region); }
        region = regionRepository.findById(382);if (region == null){ region = new Region( 382, "FR", "86", "Vienne"); regionRepository.save(region); }
        region = regionRepository.findById(383);if (region == null){ region = new Region( 383, "FR", "87", "Vienne (Haute)"); regionRepository.save(region); }
        region = regionRepository.findById(384);if (region == null){ region = new Region( 384, "FR", "88", "Vosges"); regionRepository.save(region); }
        region = regionRepository.findById(385);if (region == null){ region = new Region( 385, "FR", "89", "Yonne"); regionRepository.save(region); }
        region = regionRepository.findById(386);if (region == null){ region = new Region( 386, "FR", "90", "Territ.-de-Belfort"); regionRepository.save(region); }
        region = regionRepository.findById(387);if (region == null){ region = new Region( 387, "FR", "91", "Essonne"); regionRepository.save(region); }
        region = regionRepository.findById(388);if (region == null){ region = new Region( 388, "FR", "92", "Hauts-de-Seine"); regionRepository.save(region); }
        region = regionRepository.findById(389);if (region == null){ region = new Region( 389, "FR", "93", "Seine-Saint-Denis"); regionRepository.save(region); }
        region = regionRepository.findById(390);if (region == null){ region = new Region( 390, "FR", "94", "Val-de-Marne"); regionRepository.save(region); }
        region = regionRepository.findById(391);if (region == null){ region = new Region( 391, "FR", "95", "Val-d'Oise"); regionRepository.save(region); }
        region = regionRepository.findById(392);if (region == null){ region = new Region( 392, "FR", "97", "D.O.M.-T.O.M."); regionRepository.save(region); }
        region = regionRepository.findById(393);if (region == null){ region = new Region( 393, "FR", "971", "Guadeloupe"); regionRepository.save(region); }
        region = regionRepository.findById(394);if (region == null){ region = new Region( 394, "FR", "972", "Martinique"); regionRepository.save(region); }
        region = regionRepository.findById(395);if (region == null){ region = new Region( 395, "FR", "973", "Guyane"); regionRepository.save(region); }
        region = regionRepository.findById(396);if (region == null){ region = new Region( 396, "FR", "974", "Reunion"); regionRepository.save(region); }
        region = regionRepository.findById(397);if (region == null){ region = new Region( 397, "FR", "975", "Saint-Pierre-et-Miq."); regionRepository.save(region); }
        region = regionRepository.findById(398);if (region == null){ region = new Region( 398, "FR", "976", "Wallis-et-Futuna"); regionRepository.save(region); }
        region = regionRepository.findById(399);if (region == null){ region = new Region( 399, "FR", "99", "Hors-France"); regionRepository.save(region); }
        region = regionRepository.findById(400);if (region == null){ region = new Region( 400, "GB", "AB", "Aberdeenshire"); regionRepository.save(region); }
        region = regionRepository.findById(401);if (region == null){ region = new Region( 401, "GB", "AG", "Argyllshire"); regionRepository.save(region); }
        region = regionRepository.findById(402);if (region == null){ region = new Region( 402, "GB", "AL", "Anglesey"); regionRepository.save(region); }
        region = regionRepository.findById(403);if (region == null){ region = new Region( 403, "GB", "AM", "Armagh"); regionRepository.save(region); }
        region = regionRepository.findById(404);if (region == null){ region = new Region( 404, "GB", "AN", "Angus/Forfarshire"); regionRepository.save(region); }
        region = regionRepository.findById(405);if (region == null){ region = new Region( 405, "GB", "AT", "Antrim"); regionRepository.save(region); }
        region = regionRepository.findById(406);if (region == null){ region = new Region( 406, "GB", "AY", "Ayrshire"); regionRepository.save(region); }
        region = regionRepository.findById(407);if (region == null){ region = new Region( 407, "GB", "BE", "Bedfordshire"); regionRepository.save(region); }
        region = regionRepository.findById(408);if (region == null){ region = new Region( 408, "GB", "BF", "Banffshire"); regionRepository.save(region); }
        region = regionRepository.findById(409);if (region == null){ region = new Region( 409, "GB", "BK", "Berkshire"); regionRepository.save(region); }
        region = regionRepository.findById(410);if (region == null){ region = new Region( 410, "GB", "BR", "Brecknockshire"); regionRepository.save(region); }
        region = regionRepository.findById(411);if (region == null){ region = new Region( 411, "GB", "BS", "Bath&NthEstSomerset"); regionRepository.save(region); }
        region = regionRepository.findById(412);if (region == null){ region = new Region( 412, "GB", "BT", "Buteshire"); regionRepository.save(region); }
        region = regionRepository.findById(413);if (region == null){ region = new Region( 413, "GB", "BU", "Buckinghamshire"); regionRepository.save(region); }
        region = regionRepository.findById(414);if (region == null){ region = new Region( 414, "GB", "BW", "Berwickshire"); regionRepository.save(region); }
        region = regionRepository.findById(415);if (region == null){ region = new Region( 415, "GB", "CA", "Cambridgeshire"); regionRepository.save(region); }
        region = regionRepository.findById(416);if (region == null){ region = new Region( 416, "GB", "CB", "Carmarthenshire"); regionRepository.save(region); }
        region = regionRepository.findById(417);if (region == null){ region = new Region( 417, "GB", "CD", "Cardiganshire"); regionRepository.save(region); }
        region = regionRepository.findById(418);if (region == null){ region = new Region( 418, "GB", "CF", "Caernarfonshire"); regionRepository.save(region); }
        region = regionRepository.findById(419);if (region == null){ region = new Region( 419, "GB", "CH", "Cheshire"); regionRepository.save(region); }
        region = regionRepository.findById(420);if (region == null){ region = new Region( 420, "GB", "CM", "Cromartyshire"); regionRepository.save(region); }
        region = regionRepository.findById(421);if (region == null){ region = new Region( 421, "GB", "CN", "Clackmannanshire"); regionRepository.save(region); }
        region = regionRepository.findById(422);if (region == null){ region = new Region( 422, "GB", "CO", "Cornwall"); regionRepository.save(region); }
        region = regionRepository.findById(423);if (region == null){ region = new Region( 423, "GB", "CT", "Caithness"); regionRepository.save(region); }
        region = regionRepository.findById(424);if (region == null){ region = new Region( 424, "GB", "CU", "Cumberland"); regionRepository.save(region); }
        region = regionRepository.findById(425);if (region == null){ region = new Region( 425, "GB", "DB", "Derbyshire"); regionRepository.save(region); }
        region = regionRepository.findById(426);if (region == null){ region = new Region( 426, "GB", "DD", "Denbighshire"); regionRepository.save(region); }
        region = regionRepository.findById(427);if (region == null){ region = new Region( 427, "GB", "DF", "Dumfriesshire"); regionRepository.save(region); }
        region = regionRepository.findById(428);if (region == null){ region = new Region( 428, "GB", "DN", "Down"); regionRepository.save(region); }
        region = regionRepository.findById(429);if (region == null){ region = new Region( 429, "GB", "DO", "Dorset"); regionRepository.save(region); }
        region = regionRepository.findById(430);if (region == null){ region = new Region( 430, "GB", "DT", "Dunbartonshire"); regionRepository.save(region); }
        region = regionRepository.findById(431);if (region == null){ region = new Region( 431, "GB", "DU", "Durham"); regionRepository.save(region); }
        region = regionRepository.findById(432);if (region == null){ region = new Region( 432, "GB", "DV", "Devon"); regionRepository.save(region); }
        region = regionRepository.findById(433);if (region == null){ region = new Region( 433, "GB", "EL", "East Lothian"); regionRepository.save(region); }
        region = regionRepository.findById(434);if (region == null){ region = new Region( 434, "GB", "ES", "Essex"); regionRepository.save(region); }
        region = regionRepository.findById(435);if (region == null){ region = new Region( 435, "GB", "FI", "Fife"); regionRepository.save(region); }
        region = regionRepository.findById(436);if (region == null){ region = new Region( 436, "GB", "FL", "Flintshire"); regionRepository.save(region); }
        region = regionRepository.findById(437);if (region == null){ region = new Region( 437, "GB", "FM", "Fermanagh"); regionRepository.save(region); }
        region = regionRepository.findById(438);if (region == null){ region = new Region( 438, "GB", "GL", "Gloucestershire"); regionRepository.save(region); }
        region = regionRepository.findById(439);if (region == null){ region = new Region( 439, "GB", "HA", "Hampshire"); regionRepository.save(region); }
        region = regionRepository.findById(440);if (region == null){ region = new Region( 440, "GB", "HT", "Hertfordshire"); regionRepository.save(region); }
        region = regionRepository.findById(441);if (region == null){ region = new Region( 441, "GB", "HU", "Huntingdonshire"); regionRepository.save(region); }
        region = regionRepository.findById(442);if (region == null){ region = new Region( 442, "GB", "HW", "Hereford and Worcs."); regionRepository.save(region); }
        region = regionRepository.findById(443);if (region == null){ region = new Region( 443, "GB", "IN", "Invernesshire"); regionRepository.save(region); }
        region = regionRepository.findById(444);if (region == null){ region = new Region( 444, "GB", "IW", "Isle of Wight"); regionRepository.save(region); }
        region = regionRepository.findById(445);if (region == null){ region = new Region( 445, "GB", "KE", "Kent"); regionRepository.save(region); }
        region = regionRepository.findById(446);if (region == null){ region = new Region( 446, "GB", "KI", "Kincardineshire"); regionRepository.save(region); }
        region = regionRepository.findById(447);if (region == null){ region = new Region( 447, "GB", "KK", "Kirkcudbrightshire"); regionRepository.save(region); }
        region = regionRepository.findById(448);if (region == null){ region = new Region( 448, "GB", "KN", "Kinross-shire"); regionRepository.save(region); }
        region = regionRepository.findById(449);if (region == null){ region = new Region( 449, "GB", "LA", "Lancashire"); regionRepository.save(region); }
        region = regionRepository.findById(450);if (region == null){ region = new Region( 450, "GB", "LD", "Londonderry"); regionRepository.save(region); }
        region = regionRepository.findById(451);if (region == null){ region = new Region( 451, "GB", "LE", "Leicestershire"); regionRepository.save(region); }
        region = regionRepository.findById(452);if (region == null){ region = new Region( 452, "GB", "LI", "Lincolnshire"); regionRepository.save(region); }
        region = regionRepository.findById(453);if (region == null){ region = new Region( 453, "GB", "LN", "Lanarkshire"); regionRepository.save(region); }
        region = regionRepository.findById(454);if (region == null){ region = new Region( 454, "GB", "MD", "Midlothian"); regionRepository.save(region); }
        region = regionRepository.findById(455);if (region == null){ region = new Region( 455, "GB", "ME", "Merioneth"); regionRepository.save(region); }
        region = regionRepository.findById(456);if (region == null){ region = new Region( 456, "GB", "MG", "Mid Glamorgan"); regionRepository.save(region); }
        region = regionRepository.findById(457);if (region == null){ region = new Region( 457, "GB", "MM", "Monmouthshire"); regionRepository.save(region); }
        region = regionRepository.findById(458);if (region == null){ region = new Region( 458, "GB", "MR", "Morayshire"); regionRepository.save(region); }
        region = regionRepository.findById(459);if (region == null){ region = new Region( 459, "GB", "MT", "Montgomeryshire"); regionRepository.save(region); }
        region = regionRepository.findById(460);if (region == null){ region = new Region( 460, "GB", "MX", "Middlesex"); regionRepository.save(region); }
        region = regionRepository.findById(461);if (region == null){ region = new Region( 461, "GB", "NH", "Northamptonshire"); regionRepository.save(region); }
        region = regionRepository.findById(462);if (region == null){ region = new Region( 462, "GB", "NK", "Norfolk"); regionRepository.save(region); }
        region = regionRepository.findById(463);if (region == null){ region = new Region( 463, "GB", "NR", "Nairnshire"); regionRepository.save(region); }
        region = regionRepository.findById(464);if (region == null){ region = new Region( 464, "GB", "NT", "Nottinghamshire"); regionRepository.save(region); }
        region = regionRepository.findById(465);if (region == null){ region = new Region( 465, "GB", "NU", "Northumberland"); regionRepository.save(region); }
        region = regionRepository.findById(466);if (region == null){ region = new Region( 466, "GB", "OR", "Orkney"); regionRepository.save(region); }
        region = regionRepository.findById(467);if (region == null){ region = new Region( 467, "GB", "OX", "Oxfordshire"); regionRepository.save(region); }
        region = regionRepository.findById(468);if (region == null){ region = new Region( 468, "GB", "PE", "Peeblesshire"); regionRepository.save(region); }
        region = regionRepository.findById(469);if (region == null){ region = new Region( 469, "GB", "PM", "Pembrokeshire"); regionRepository.save(region); }
        region = regionRepository.findById(470);if (region == null){ region = new Region( 470, "GB", "PR", "Perthshire"); regionRepository.save(region); }
        region = regionRepository.findById(471);if (region == null){ region = new Region( 471, "GB", "RA", "Radnorshire"); regionRepository.save(region); }
        region = regionRepository.findById(472);if (region == null){ region = new Region( 472, "GB", "RE", "Renfrewshire"); regionRepository.save(region); }
        region = regionRepository.findById(473);if (region == null){ region = new Region( 473, "GB", "RO", "Ross-shire"); regionRepository.save(region); }
        region = regionRepository.findById(474);if (region == null){ region = new Region( 474, "GB", "RU", "Rutland"); regionRepository.save(region); }
        region = regionRepository.findById(475);if (region == null){ region = new Region( 475, "GB", "RX", "Roxburghshire"); regionRepository.save(region); }
        region = regionRepository.findById(476);if (region == null){ region = new Region( 476, "GB", "SE", "East Sussex"); regionRepository.save(region); }
        region = regionRepository.findById(477);if (region == null){ region = new Region( 477, "GB", "SF", "Selkirkshire"); regionRepository.save(region); }
        region = regionRepository.findById(478);if (region == null){ region = new Region( 478, "GB", "SG", "South Glamorgan"); regionRepository.save(region); }
        region = regionRepository.findById(479);if (region == null){ region = new Region( 479, "GB", "SH", "Shropshire"); regionRepository.save(region); }
        region = regionRepository.findById(480);if (region == null){ region = new Region( 480, "GB", "SK", "Suffolk"); regionRepository.save(region); }
        region = regionRepository.findById(481);if (region == null){ region = new Region( 481, "GB", "SL", "Shetland"); regionRepository.save(region); }
        region = regionRepository.findById(482);if (region == null){ region = new Region( 482, "GB", "SO", "Somerset"); regionRepository.save(region); }
        region = regionRepository.findById(483);if (region == null){ region = new Region( 483, "GB", "ST", "Staffordshire"); regionRepository.save(region); }
        region = regionRepository.findById(484);if (region == null){ region = new Region( 484, "GB", "SU", "Sutherland"); regionRepository.save(region); }
        region = regionRepository.findById(485);if (region == null){ region = new Region( 485, "GB", "SV", "Stirlingshire"); regionRepository.save(region); }
        region = regionRepository.findById(486);if (region == null){ region = new Region( 486, "GB", "SW", "West Sussex"); regionRepository.save(region); }
        region = regionRepository.findById(487);if (region == null){ region = new Region( 487, "GB", "SY", "Surrey"); regionRepository.save(region); }
        region = regionRepository.findById(488);if (region == null){ region = new Region( 488, "GB", "TY", "Tyrone"); regionRepository.save(region); }
        region = regionRepository.findById(489);if (region == null){ region = new Region( 489, "GB", "WA", "Warwickshire"); regionRepository.save(region); }
        region = regionRepository.findById(490);if (region == null){ region = new Region( 490, "GB", "WC", "Worcestershire"); regionRepository.save(region); }
        region = regionRepository.findById(491);if (region == null){ region = new Region( 491, "GB", "WE", "Westmorland"); regionRepository.save(region); }
        region = regionRepository.findById(492);if (region == null){ region = new Region( 492, "GB", "WG", "West Glamorgan"); regionRepository.save(region); }
        region = regionRepository.findById(493);if (region == null){ region = new Region( 493, "GB", "WI", "Wiltshire"); regionRepository.save(region); }
        region = regionRepository.findById(494);if (region == null){ region = new Region( 494, "GB", "WK", "West Lothian"); regionRepository.save(region); }
        region = regionRepository.findById(495);if (region == null){ region = new Region( 495, "GB", "WT", "Wigtownshire"); regionRepository.save(region); }
        region = regionRepository.findById(496);if (region == null){ region = new Region( 496, "GB", "YN", "North Yorkshire"); regionRepository.save(region); }
        region = regionRepository.findById(497);if (region == null){ region = new Region( 497, "GB", "YS", "South Yorkshire"); regionRepository.save(region); }
        region = regionRepository.findById(498);if (region == null){ region = new Region( 498, "GB", "YW", "West Yorkshire"); regionRepository.save(region); }
        region = regionRepository.findById(499);if (region == null){ region = new Region( 499, "GR", "01", "Aitolia kai Akarnan."); regionRepository.save(region); }
        region = regionRepository.findById(500);if (region == null){ region = new Region( 500, "GR", "02", "Akhaia"); regionRepository.save(region); }
        region = regionRepository.findById(501);if (region == null){ region = new Region( 501, "GR", "03", "Argolis"); regionRepository.save(region); }
        region = regionRepository.findById(502);if (region == null){ region = new Region( 502, "GR", "04", "Arkadhia"); regionRepository.save(region); }
        region = regionRepository.findById(503);if (region == null){ region = new Region( 503, "GR", "05", "Arta"); regionRepository.save(region); }
        region = regionRepository.findById(504);if (region == null){ region = new Region( 504, "GR", "06", "Attiki"); regionRepository.save(region); }
        region = regionRepository.findById(505);if (region == null){ region = new Region( 505, "GR", "07", "Dhodhekanisos"); regionRepository.save(region); }
        region = regionRepository.findById(506);if (region == null){ region = new Region( 506, "GR", "08", "Dhrama"); regionRepository.save(region); }
        region = regionRepository.findById(507);if (region == null){ region = new Region( 507, "GR", "09", "Evritania"); regionRepository.save(region); }
        region = regionRepository.findById(508);if (region == null){ region = new Region( 508, "GR", "10", "Evros"); regionRepository.save(region); }
        region = regionRepository.findById(509);if (region == null){ region = new Region( 509, "GR", "11", "Evvoia"); regionRepository.save(region); }
        region = regionRepository.findById(510);if (region == null){ region = new Region( 510, "GR", "12", "Florina"); regionRepository.save(region); }
        region = regionRepository.findById(511);if (region == null){ region = new Region( 511, "GR", "13", "Fokis"); regionRepository.save(region); }
        region = regionRepository.findById(512);if (region == null){ region = new Region( 512, "GR", "14", "Fthiotis"); regionRepository.save(region); }
        region = regionRepository.findById(513);if (region == null){ region = new Region( 513, "GR", "15", "Grevena"); regionRepository.save(region); }
        region = regionRepository.findById(514);if (region == null){ region = new Region( 514, "GR", "16", "Ilia"); regionRepository.save(region); }
        region = regionRepository.findById(515);if (region == null){ region = new Region( 515, "GR", "17", "Imathia"); regionRepository.save(region); }
        region = regionRepository.findById(516);if (region == null){ region = new Region( 516, "GR", "18", "Ioannina"); regionRepository.save(region); }
        region = regionRepository.findById(517);if (region == null){ region = new Region( 517, "GR", "19", "Iraklion"); regionRepository.save(region); }
        region = regionRepository.findById(518);if (region == null){ region = new Region( 518, "GR", "20", "Kardhitsa"); regionRepository.save(region); }
        region = regionRepository.findById(519);if (region == null){ region = new Region( 519, "GR", "21", "Kastoria"); regionRepository.save(region); }
        region = regionRepository.findById(520);if (region == null){ region = new Region( 520, "GR", "22", "Kavala"); regionRepository.save(region); }
        region = regionRepository.findById(521);if (region == null){ region = new Region( 521, "GR", "23", "Kefallinia"); regionRepository.save(region); }
        region = regionRepository.findById(522);if (region == null){ region = new Region( 522, "GR", "24", "Kerkira"); regionRepository.save(region); }
        region = regionRepository.findById(523);if (region == null){ region = new Region( 523, "GR", "25", "Khalkidhiki"); regionRepository.save(region); }
        region = regionRepository.findById(524);if (region == null){ region = new Region( 524, "GR", "26", "Khania"); regionRepository.save(region); }
        region = regionRepository.findById(525);if (region == null){ region = new Region( 525, "GR", "27", "Khios"); regionRepository.save(region); }
        region = regionRepository.findById(526);if (region == null){ region = new Region( 526, "GR", "28", "Kikladhes"); regionRepository.save(region); }
        region = regionRepository.findById(527);if (region == null){ region = new Region( 527, "GR", "29", "Kilkis"); regionRepository.save(region); }
        region = regionRepository.findById(528);if (region == null){ region = new Region( 528, "GR", "30", "Korinthia"); regionRepository.save(region); }
        region = regionRepository.findById(529);if (region == null){ region = new Region( 529, "GR", "31", "Kozani"); regionRepository.save(region); }
        region = regionRepository.findById(530);if (region == null){ region = new Region( 530, "GR", "32", "Lakonia"); regionRepository.save(region); }
        region = regionRepository.findById(531);if (region == null){ region = new Region( 531, "GR", "33", "Larisa"); regionRepository.save(region); }
        region = regionRepository.findById(532);if (region == null){ region = new Region( 532, "GR", "34", "Lasithi"); regionRepository.save(region); }
        region = regionRepository.findById(533);if (region == null){ region = new Region( 533, "GR", "35", "Lesvos"); regionRepository.save(region); }
        region = regionRepository.findById(534);if (region == null){ region = new Region( 534, "GR", "36", "Levkas"); regionRepository.save(region); }
        region = regionRepository.findById(535);if (region == null){ region = new Region( 535, "GR", "37", "Magnisia"); regionRepository.save(region); }
        region = regionRepository.findById(536);if (region == null){ region = new Region( 536, "GR", "38", "Messinia"); regionRepository.save(region); }
        region = regionRepository.findById(537);if (region == null){ region = new Region( 537, "GR", "39", "Pella"); regionRepository.save(region); }
        region = regionRepository.findById(538);if (region == null){ region = new Region( 538, "GR", "40", "Pieria"); regionRepository.save(region); }
        region = regionRepository.findById(539);if (region == null){ region = new Region( 539, "GR", "41", "Piraievs"); regionRepository.save(region); }
        region = regionRepository.findById(540);if (region == null){ region = new Region( 540, "GR", "42", "Preveza"); regionRepository.save(region); }
        region = regionRepository.findById(541);if (region == null){ region = new Region( 541, "GR", "43", "Rethimni"); regionRepository.save(region); }
        region = regionRepository.findById(542);if (region == null){ region = new Region( 542, "GR", "44", "Rodhopi"); regionRepository.save(region); }
        region = regionRepository.findById(543);if (region == null){ region = new Region( 543, "GR", "45", "Samos"); regionRepository.save(region); }
        region = regionRepository.findById(544);if (region == null){ region = new Region( 544, "GR", "46", "Serrai"); regionRepository.save(region); }
        region = regionRepository.findById(545);if (region == null){ region = new Region( 545, "GR", "47", "Thesprotia"); regionRepository.save(region); }
        region = regionRepository.findById(546);if (region == null){ region = new Region( 546, "GR", "48", "Thessaloniki"); regionRepository.save(region); }
        region = regionRepository.findById(547);if (region == null){ region = new Region( 547, "GR", "49", "Trikala"); regionRepository.save(region); }
        region = regionRepository.findById(548);if (region == null){ region = new Region( 548, "GR", "50", "Voiotia"); regionRepository.save(region); }
        region = regionRepository.findById(549);if (region == null){ region = new Region( 549, "GR", "51", "Xanthi"); regionRepository.save(region); }
        region = regionRepository.findById(550);if (region == null){ region = new Region( 550, "GR", "52", "Zakinthos"); regionRepository.save(region); }
        region = regionRepository.findById(551);if (region == null){ region = new Region( 551, "HK", "HK", "Hong Kong Island"); regionRepository.save(region); }
        region = regionRepository.findById(552);if (region == null){ region = new Region( 552, "HK", "KLN", "Kowloon"); regionRepository.save(region); }
        region = regionRepository.findById(553);if (region == null){ region = new Region( 553, "HK", "NT", "New Territories"); regionRepository.save(region); }
        region = regionRepository.findById(554);if (region == null){ region = new Region( 554, "HR", "A00", "Zagrebacka"); regionRepository.save(region); }
        region = regionRepository.findById(555);if (region == null){ region = new Region( 555, "HR", "B00", "Krapinsko-zagorska"); regionRepository.save(region); }
        region = regionRepository.findById(556);if (region == null){ region = new Region( 556, "HR", "C00", "Sisacko-moslavacka"); regionRepository.save(region); }
        region = regionRepository.findById(557);if (region == null){ region = new Region( 557, "HR", "D00", "Karlovacka"); regionRepository.save(region); }
        region = regionRepository.findById(558);if (region == null){ region = new Region( 558, "HR", "E00", "Varazdinska"); regionRepository.save(region); }
        region = regionRepository.findById(559);if (region == null){ region = new Region( 559, "HR", "F00", "Koprivnicko-krizevac"); regionRepository.save(region); }
        region = regionRepository.findById(560);if (region == null){ region = new Region( 560, "HR", "G00", "Bjelovarsko-bilogors"); regionRepository.save(region); }
        region = regionRepository.findById(561);if (region == null){ region = new Region( 561, "HR", "H00", "Rijecko-goranska"); regionRepository.save(region); }
        region = regionRepository.findById(562);if (region == null){ region = new Region( 562, "HR", "I00", "Licko-senjska"); regionRepository.save(region); }
        region = regionRepository.findById(563);if (region == null){ region = new Region( 563, "HR", "J00", "Viroviticko-podravac"); regionRepository.save(region); }
        region = regionRepository.findById(564);if (region == null){ region = new Region( 564, "HR", "K00", "Pozesko-slavonska"); regionRepository.save(region); }
        region = regionRepository.findById(565);if (region == null){ region = new Region( 565, "HR", "L00", "Slavonskobrodska"); regionRepository.save(region); }
        region = regionRepository.findById(566);if (region == null){ region = new Region( 566, "HR", "M00", "Zadarska"); regionRepository.save(region); }
        region = regionRepository.findById(567);if (region == null){ region = new Region( 567, "HR", "N00", "Osjecko-baranjska"); regionRepository.save(region); }
        region = regionRepository.findById(568);if (region == null){ region = new Region( 568, "HR", "O00", "Sibensko-kninska"); regionRepository.save(region); }
        region = regionRepository.findById(569);if (region == null){ region = new Region( 569, "HR", "P00", "Vukovarsko-srijemska"); regionRepository.save(region); }
        region = regionRepository.findById(570);if (region == null){ region = new Region( 570, "HR", "R00", "Splitsko-dalmatinska"); regionRepository.save(region); }
        region = regionRepository.findById(571);if (region == null){ region = new Region( 571, "HR", "S00", "Istarska"); regionRepository.save(region); }
        region = regionRepository.findById(572);if (region == null){ region = new Region( 572, "HR", "T00", "Dubrovacko-neretvans"); regionRepository.save(region); }
        region = regionRepository.findById(573);if (region == null){ region = new Region( 573, "HR", "U00", "Medjimurska"); regionRepository.save(region); }
        region = regionRepository.findById(574);if (region == null){ region = new Region( 574, "HR", "V00", "Zagreb"); regionRepository.save(region); }
        region = regionRepository.findById(575);if (region == null){ region = new Region( 575, "HU", "01", "Bacs-Kiskun"); regionRepository.save(region); }
        region = regionRepository.findById(576);if (region == null){ region = new Region( 576, "HU", "02", "Baranya"); regionRepository.save(region); }
        region = regionRepository.findById(577);if (region == null){ region = new Region( 577, "HU", "03", "Bekes"); regionRepository.save(region); }
        region = regionRepository.findById(578);if (region == null){ region = new Region( 578, "HU", "04", "Bekescsaba"); regionRepository.save(region); }
        region = regionRepository.findById(579);if (region == null){ region = new Region( 579, "HU", "05", "Borsod-Abauj-Zemplen"); regionRepository.save(region); }
        region = regionRepository.findById(580);if (region == null){ region = new Region( 580, "HU", "06", "Budapest"); regionRepository.save(region); }
        region = regionRepository.findById(581);if (region == null){ region = new Region( 581, "HU", "07", "Csongrad"); regionRepository.save(region); }
        region = regionRepository.findById(582);if (region == null){ region = new Region( 582, "HU", "08", "Debrecen"); regionRepository.save(region); }
        region = regionRepository.findById(583);if (region == null){ region = new Region( 583, "HU", "09", "Dunaujvaros"); regionRepository.save(region); }
        region = regionRepository.findById(584);if (region == null){ region = new Region( 584, "HU", "10", "Eger"); regionRepository.save(region); }
        region = regionRepository.findById(585);if (region == null){ region = new Region( 585, "HU", "11", "Fejer"); regionRepository.save(region); }
        region = regionRepository.findById(586);if (region == null){ region = new Region( 586, "HU", "12", "Gyor"); regionRepository.save(region); }
        region = regionRepository.findById(587);if (region == null){ region = new Region( 587, "HU", "13", "Gyor-Moson-Sopron"); regionRepository.save(region); }
        region = regionRepository.findById(588);if (region == null){ region = new Region( 588, "HU", "14", "Hajdu-Bihar"); regionRepository.save(region); }
        region = regionRepository.findById(589);if (region == null){ region = new Region( 589, "HU", "15", "Heves"); regionRepository.save(region); }
        region = regionRepository.findById(590);if (region == null){ region = new Region( 590, "HU", "16", "Hodmezovasarhely"); regionRepository.save(region); }
        region = regionRepository.findById(591);if (region == null){ region = new Region( 591, "HU", "17", "Jasz-Nagykun-Szolnok"); regionRepository.save(region); }
        region = regionRepository.findById(592);if (region == null){ region = new Region( 592, "HU", "18", "Kaposvar"); regionRepository.save(region); }
        region = regionRepository.findById(593);if (region == null){ region = new Region( 593, "HU", "19", "Kecskemet"); regionRepository.save(region); }
        region = regionRepository.findById(594);if (region == null){ region = new Region( 594, "HU", "20", "Komarom-Esztergom"); regionRepository.save(region); }
        region = regionRepository.findById(595);if (region == null){ region = new Region( 595, "HU", "21", "Miskolc"); regionRepository.save(region); }
        region = regionRepository.findById(596);if (region == null){ region = new Region( 596, "HU", "22", "Nagykanizsa"); regionRepository.save(region); }
        region = regionRepository.findById(597);if (region == null){ region = new Region( 597, "HU", "23", "Nograd"); regionRepository.save(region); }
        region = regionRepository.findById(598);if (region == null){ region = new Region( 598, "HU", "24", "Nyiregyhaza"); regionRepository.save(region); }
        region = regionRepository.findById(599);if (region == null){ region = new Region( 599, "HU", "25", "Pecs"); regionRepository.save(region); }
        region = regionRepository.findById(600);if (region == null){ region = new Region( 600, "HU", "26", "Pest"); regionRepository.save(region); }
        region = regionRepository.findById(601);if (region == null){ region = new Region( 601, "HU", "27", "Somogy"); regionRepository.save(region); }
        region = regionRepository.findById(602);if (region == null){ region = new Region( 602, "HU", "28", "Sopron"); regionRepository.save(region); }
        region = regionRepository.findById(603);if (region == null){ region = new Region( 603, "HU", "29", "Szabolcs-Szat.-Bereg"); regionRepository.save(region); }
        region = regionRepository.findById(604);if (region == null){ region = new Region( 604, "HU", "30", "Szeged"); regionRepository.save(region); }
        region = regionRepository.findById(605);if (region == null){ region = new Region( 605, "HU", "31", "Szekesfehervar"); regionRepository.save(region); }
        region = regionRepository.findById(606);if (region == null){ region = new Region( 606, "HU", "32", "Szolnok"); regionRepository.save(region); }
        region = regionRepository.findById(607);if (region == null){ region = new Region( 607, "HU", "33", "Szombathely"); regionRepository.save(region); }
        region = regionRepository.findById(608);if (region == null){ region = new Region( 608, "HU", "34", "Tatabanya"); regionRepository.save(region); }
        region = regionRepository.findById(609);if (region == null){ region = new Region( 609, "HU", "35", "Tolna"); regionRepository.save(region); }
        region = regionRepository.findById(610);if (region == null){ region = new Region( 610, "HU", "36", "Vas"); regionRepository.save(region); }
        region = regionRepository.findById(611);if (region == null){ region = new Region( 611, "HU", "37", "Veszprem"); regionRepository.save(region); }
        region = regionRepository.findById(612);if (region == null){ region = new Region( 612, "HU", "38", "Zala"); regionRepository.save(region); }
        region = regionRepository.findById(613);if (region == null){ region = new Region( 613, "HU", "39", "Zalaegerszeg"); regionRepository.save(region); }
        region = regionRepository.findById(614);if (region == null){ region = new Region( 614, "ID", "01", "DKI Jakarta Jakarta"); regionRepository.save(region); }
        region = regionRepository.findById(615);if (region == null){ region = new Region( 615, "ID", "02", "Jawa Barat West Java"); regionRepository.save(region); }
        region = regionRepository.findById(616);if (region == null){ region = new Region( 616, "ID", "03", "Jawa Tengah Central"); regionRepository.save(region); }
        region = regionRepository.findById(617);if (region == null){ region = new Region( 617, "ID", "04", "Jawa Timur East Java"); regionRepository.save(region); }
        region = regionRepository.findById(618);if (region == null){ region = new Region( 618, "ID", "05", "DI Yogyakarta Yogyak"); regionRepository.save(region); }
        region = regionRepository.findById(619);if (region == null){ region = new Region( 619, "ID", "06", "DI Aceh Aceh"); regionRepository.save(region); }
        region = regionRepository.findById(620);if (region == null){ region = new Region( 620, "ID", "07", "Sumatera Utara North"); regionRepository.save(region); }
        region = regionRepository.findById(621);if (region == null){ region = new Region( 621, "ID", "08", "Sumatera Barat West"); regionRepository.save(region); }
        region = regionRepository.findById(622);if (region == null){ region = new Region( 622, "ID", "09", "Riau Riau"); regionRepository.save(region); }
        region = regionRepository.findById(623);if (region == null){ region = new Region( 623, "ID", "10", "Jambi Jambi"); regionRepository.save(region); }
        region = regionRepository.findById(624);if (region == null){ region = new Region( 624, "ID", "11", "Sumatera Selatan Sou"); regionRepository.save(region); }
        region = regionRepository.findById(625);if (region == null){ region = new Region( 625, "ID", "12", "Bengkulu Bengkulu"); regionRepository.save(region); }
        region = regionRepository.findById(626);if (region == null){ region = new Region( 626, "ID", "13", "Lampung Lampung"); regionRepository.save(region); }
        region = regionRepository.findById(627);if (region == null){ region = new Region( 627, "ID", "14", "Kalimantan Selatan S"); regionRepository.save(region); }
        region = regionRepository.findById(628);if (region == null){ region = new Region( 628, "ID", "15", "Kalimantan Barat Wes"); regionRepository.save(region); }
        region = regionRepository.findById(629);if (region == null){ region = new Region( 629, "ID", "16", "Kalimantan Tengah Ce"); regionRepository.save(region); }
        region = regionRepository.findById(630);if (region == null){ region = new Region( 630, "ID", "17", "Kalimantan Timur Eas"); regionRepository.save(region); }
        region = regionRepository.findById(631);if (region == null){ region = new Region( 631, "ID", "18", "Sulawesi Selatan Sou"); regionRepository.save(region); }
        region = regionRepository.findById(632);if (region == null){ region = new Region( 632, "ID", "19", "Sulawesi Tenggara So"); regionRepository.save(region); }
        region = regionRepository.findById(633);if (region == null){ region = new Region( 633, "ID", "20", "Sulawesi Tengah Cent"); regionRepository.save(region); }
        region = regionRepository.findById(634);if (region == null){ region = new Region( 634, "ID", "21", "Sulawesi Utara North"); regionRepository.save(region); }
        region = regionRepository.findById(635);if (region == null){ region = new Region( 635, "ID", "22", "Bali Bali"); regionRepository.save(region); }
        region = regionRepository.findById(636);if (region == null){ region = new Region( 636, "ID", "23", "Nusa Tenggara Barat"); regionRepository.save(region); }
        region = regionRepository.findById(637);if (region == null){ region = new Region( 637, "ID", "24", "Nusa Tenggara Timur"); regionRepository.save(region); }
        region = regionRepository.findById(638);if (region == null){ region = new Region( 638, "ID", "25", "Maluku Maluku"); regionRepository.save(region); }
        region = regionRepository.findById(639);if (region == null){ region = new Region( 639, "ID", "26", "Irian Jaya Irian Jay"); regionRepository.save(region); }
        region = regionRepository.findById(640);if (region == null){ region = new Region( 640, "ID", "27", "Timor Timur East Tim"); regionRepository.save(region); }
        region = regionRepository.findById(641);if (region == null){ region = new Region( 641, "IE", "CK", "Cork"); regionRepository.save(region); }
        region = regionRepository.findById(642);if (region == null){ region = new Region( 642, "IE", "CL", "Clare"); regionRepository.save(region); }
        region = regionRepository.findById(643);if (region == null){ region = new Region( 643, "IE", "CV", "Cavan"); regionRepository.save(region); }
        region = regionRepository.findById(644);if (region == null){ region = new Region( 644, "IE", "CW", "Carlow"); regionRepository.save(region); }
        region = regionRepository.findById(645);if (region == null){ region = new Region( 645, "IE", "DB", "Dublin"); regionRepository.save(region); }
        region = regionRepository.findById(646);if (region == null){ region = new Region( 646, "IE", "DG", "Donegal"); regionRepository.save(region); }
        region = regionRepository.findById(647);if (region == null){ region = new Region( 647, "IE", "GW", "Galway"); regionRepository.save(region); }
        region = regionRepository.findById(648);if (region == null){ region = new Region( 648, "IE", "KD", "Kildare"); regionRepository.save(region); }
        region = regionRepository.findById(649);if (region == null){ region = new Region( 649, "IE", "KK", "Kilkenny"); regionRepository.save(region); }
        region = regionRepository.findById(650);if (region == null){ region = new Region( 650, "IE", "KY", "Kerry"); regionRepository.save(region); }
        region = regionRepository.findById(651);if (region == null){ region = new Region( 651, "IE", "LF", "Longford"); regionRepository.save(region); }
        region = regionRepository.findById(652);if (region == null){ region = new Region( 652, "IE", "LI", "Limerick"); regionRepository.save(region); }
        region = regionRepository.findById(653);if (region == null){ region = new Region( 653, "IE", "LM", "Leitrim"); regionRepository.save(region); }
        region = regionRepository.findById(654);if (region == null){ region = new Region( 654, "IE", "LS", "Laois"); regionRepository.save(region); }
        region = regionRepository.findById(655);if (region == null){ region = new Region( 655, "IE", "LT", "Louth"); regionRepository.save(region); }
        region = regionRepository.findById(656);if (region == null){ region = new Region( 656, "IE", "MH", "Monaghan"); regionRepository.save(region); }
        region = regionRepository.findById(657);if (region == null){ region = new Region( 657, "IE", "MT", "Meath"); regionRepository.save(region); }
        region = regionRepository.findById(658);if (region == null){ region = new Region( 658, "IE", "MY", "Mayo"); regionRepository.save(region); }
        region = regionRepository.findById(659);if (region == null){ region = new Region( 659, "IE", "OF", "Offaly"); regionRepository.save(region); }
        region = regionRepository.findById(660);if (region == null){ region = new Region( 660, "IE", "RC", "Rosscommon"); regionRepository.save(region); }
        region = regionRepository.findById(661);if (region == null){ region = new Region( 661, "IE", "SG", "Sligo"); regionRepository.save(region); }
        region = regionRepository.findById(662);if (region == null){ region = new Region( 662, "IE", "TP", "Tipperary"); regionRepository.save(region); }
        region = regionRepository.findById(663);if (region == null){ region = new Region( 663, "IE", "WF", "Waterford"); regionRepository.save(region); }
        region = regionRepository.findById(664);if (region == null){ region = new Region( 664, "IE", "WK", "Wicklow"); regionRepository.save(region); }
        region = regionRepository.findById(665);if (region == null){ region = new Region( 665, "IE", "WM", "Westmeath"); regionRepository.save(region); }
        region = regionRepository.findById(666);if (region == null){ region = new Region( 666, "IE", "WX", "Wexford"); regionRepository.save(region); }
        region = regionRepository.findById(667);if (region == null){ region = new Region( 667, "IL", "01", "Central"); regionRepository.save(region); }
        region = regionRepository.findById(668);if (region == null){ region = new Region( 668, "IL", "02", "Haifa"); regionRepository.save(region); }
        region = regionRepository.findById(669);if (region == null){ region = new Region( 669, "IL", "03", "Jerusalem"); regionRepository.save(region); }
        region = regionRepository.findById(670);if (region == null){ region = new Region( 670, "IL", "04", "Northern"); regionRepository.save(region); }
        region = regionRepository.findById(671);if (region == null){ region = new Region( 671, "IL", "05", "Southern"); regionRepository.save(region); }
        region = regionRepository.findById(672);if (region == null){ region = new Region( 672, "IL", "06", "Tel Aviv"); regionRepository.save(region); }
        region = regionRepository.findById(673);if (region == null){ region = new Region( 673, "IN", "01", "Andhra Pradesh"); regionRepository.save(region); }
        region = regionRepository.findById(674);if (region == null){ region = new Region( 674, "IN", "02", "Arunachal Pradesh"); regionRepository.save(region); }
        region = regionRepository.findById(675);if (region == null){ region = new Region( 675, "IN", "03", "Assam"); regionRepository.save(region); }
        region = regionRepository.findById(676);if (region == null){ region = new Region( 676, "IN", "04", "Bihar"); regionRepository.save(region); }
        region = regionRepository.findById(677);if (region == null){ region = new Region( 677, "IN", "05", "Goa"); regionRepository.save(region); }
        region = regionRepository.findById(678);if (region == null){ region = new Region( 678, "IN", "06", "Gujarat"); regionRepository.save(region); }
        region = regionRepository.findById(679);if (region == null){ region = new Region( 679, "IN", "07", "Haryana"); regionRepository.save(region); }
        region = regionRepository.findById(680);if (region == null){ region = new Region( 680, "IN", "08", "Himachal Pradesh"); regionRepository.save(region); }
        region = regionRepository.findById(681);if (region == null){ region = new Region( 681, "IN", "09", "Jammu & Kashmir"); regionRepository.save(region); }
        region = regionRepository.findById(682);if (region == null){ region = new Region( 682, "IN", "10", "Karnataka"); regionRepository.save(region); }
        region = regionRepository.findById(683);if (region == null){ region = new Region( 683, "IN", "11", "Kerala"); regionRepository.save(region); }
        region = regionRepository.findById(684);if (region == null){ region = new Region( 684, "IN", "12", "Madhya Pradesh"); regionRepository.save(region); }
        region = regionRepository.findById(685);if (region == null){ region = new Region( 685, "IN", "13", "Maharashtra"); regionRepository.save(region); }
        region = regionRepository.findById(686);if (region == null){ region = new Region( 686, "IN", "14", "Manipur"); regionRepository.save(region); }
        region = regionRepository.findById(687);if (region == null){ region = new Region( 687, "IN", "15", "Megalaya"); regionRepository.save(region); }
        region = regionRepository.findById(688);if (region == null){ region = new Region( 688, "IN", "16", "Mizoram"); regionRepository.save(region); }
        region = regionRepository.findById(689);if (region == null){ region = new Region( 689, "IN", "17", "Nagaland"); regionRepository.save(region); }
        region = regionRepository.findById(690);if (region == null){ region = new Region( 690, "IN", "18", "Odisha"); regionRepository.save(region); }
        region = regionRepository.findById(691);if (region == null){ region = new Region( 691, "IN", "19", "Punjab"); regionRepository.save(region); }
        region = regionRepository.findById(692);if (region == null){ region = new Region( 692, "IN", "20", "Rajasthan"); regionRepository.save(region); }
        region = regionRepository.findById(693);if (region == null){ region = new Region( 693, "IN", "21", "Sikkim"); regionRepository.save(region); }
        region = regionRepository.findById(694);if (region == null){ region = new Region( 694, "IN", "22", "Tamil Nadu"); regionRepository.save(region); }
        region = regionRepository.findById(695);if (region == null){ region = new Region( 695, "IN", "23", "Tripura"); regionRepository.save(region); }
        region = regionRepository.findById(696);if (region == null){ region = new Region( 696, "IN", "24", "Uttar Pradesh"); regionRepository.save(region); }
        region = regionRepository.findById(697);if (region == null){ region = new Region( 697, "IN", "25", "West Bengal"); regionRepository.save(region); }
        region = regionRepository.findById(698);if (region == null){ region = new Region( 698, "IN", "26", "Andaman&Nicobar Ilnd"); regionRepository.save(region); }


        return;
    }
}