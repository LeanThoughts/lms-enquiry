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
public class RegionConfig2 implements CommandLineRunner {

    private final RegionRepository regionRepository;
 
    @Override
    public void run(String... strings) throws Exception {

        Region region = new Region();
        region = regionRepository.findById(699);if (region == null){ region = new Region( 699, "IN", "27", "Chandigarh"); regionRepository.save(region); }
        region = regionRepository.findById(700);if (region == null){ region = new Region( 700, "IN", "28", "Dadra & Nagar Haveli"); regionRepository.save(region); }
        region = regionRepository.findById(701);if (region == null){ region = new Region( 701, "IN", "29", "Daman & Diu"); regionRepository.save(region); }
        region = regionRepository.findById(702);if (region == null){ region = new Region( 702, "IN", "30", "Delhi"); regionRepository.save(region); }
        region = regionRepository.findById(703);if (region == null){ region = new Region( 703, "IN", "31", "Lakshadweep"); regionRepository.save(region); }
        region = regionRepository.findById(704);if (region == null){ region = new Region( 704, "IN", "32", "Pondicherry"); regionRepository.save(region); }
        region = regionRepository.findById(705);if (region == null){ region = new Region( 705, "IN", "33", "Chhattisgarh"); regionRepository.save(region); }
        region = regionRepository.findById(706);if (region == null){ region = new Region( 706, "IN", "34", "Jharkhand"); regionRepository.save(region); }
        region = regionRepository.findById(707);if (region == null){ region = new Region( 707, "IN", "35", "Uttarakhand"); regionRepository.save(region); }
        region = regionRepository.findById(708);if (region == null){ region = new Region( 708, "IN", "36", "Telangana"); regionRepository.save(region); }
        region = regionRepository.findById(709);if (region == null){ region = new Region( 709, "IN", "37", "Andhra Pradesh(New)"); regionRepository.save(region); }
        region = regionRepository.findById(710);if (region == null){ region = new Region( 710, "IN", "98", "Not Applicable"); regionRepository.save(region); }
        region = regionRepository.findById(711);if (region == null){ region = new Region( 711, "IT", "AG", "Agriento"); regionRepository.save(region); }
        region = regionRepository.findById(712);if (region == null){ region = new Region( 712, "IT", "AL", "Alessandria"); regionRepository.save(region); }
        region = regionRepository.findById(713);if (region == null){ region = new Region( 713, "IT", "AN", "Ancona"); regionRepository.save(region); }
        region = regionRepository.findById(714);if (region == null){ region = new Region( 714, "IT", "AO", "Aosta"); regionRepository.save(region); }
        region = regionRepository.findById(715);if (region == null){ region = new Region( 715, "IT", "AP", "Ascoli Piceno"); regionRepository.save(region); }
        region = regionRepository.findById(716);if (region == null){ region = new Region( 716, "IT", "AQ", "L'Aquila"); regionRepository.save(region); }
        region = regionRepository.findById(717);if (region == null){ region = new Region( 717, "IT", "AR", "Arezzo"); regionRepository.save(region); }
        region = regionRepository.findById(718);if (region == null){ region = new Region( 718, "IT", "AT", "Asti"); regionRepository.save(region); }
        region = regionRepository.findById(719);if (region == null){ region = new Region( 719, "IT", "AV", "Avellino"); regionRepository.save(region); }
        region = regionRepository.findById(720);if (region == null){ region = new Region( 720, "IT", "BA", "Bari"); regionRepository.save(region); }
        region = regionRepository.findById(721);if (region == null){ region = new Region( 721, "IT", "BG", "Bergamo"); regionRepository.save(region); }
        region = regionRepository.findById(722);if (region == null){ region = new Region( 722, "IT", "BI", "Biella"); regionRepository.save(region); }
        region = regionRepository.findById(723);if (region == null){ region = new Region( 723, "IT", "BL", "Belluno"); regionRepository.save(region); }
        region = regionRepository.findById(724);if (region == null){ region = new Region( 724, "IT", "BN", "Benevento"); regionRepository.save(region); }
        region = regionRepository.findById(725);if (region == null){ region = new Region( 725, "IT", "BO", "Bologna"); regionRepository.save(region); }
        region = regionRepository.findById(726);if (region == null){ region = new Region( 726, "IT", "BR", "Brindisi"); regionRepository.save(region); }
        region = regionRepository.findById(727);if (region == null){ region = new Region( 727, "IT", "BS", "Brescia"); regionRepository.save(region); }
        region = regionRepository.findById(728);if (region == null){ region = new Region( 728, "IT", "BZ", "Bolzano"); regionRepository.save(region); }
        region = regionRepository.findById(729);if (region == null){ region = new Region( 729, "IT", "CA", "Cagliari"); regionRepository.save(region); }
        region = regionRepository.findById(730);if (region == null){ region = new Region( 730, "IT", "CB", "Campobasso"); regionRepository.save(region); }
        region = regionRepository.findById(731);if (region == null){ region = new Region( 731, "IT", "CE", "Caserta"); regionRepository.save(region); }
        region = regionRepository.findById(732);if (region == null){ region = new Region( 732, "IT", "CH", "Chieti"); regionRepository.save(region); }
        region = regionRepository.findById(733);if (region == null){ region = new Region( 733, "IT", "CI", "Carbonia-Iglesias"); regionRepository.save(region); }
        region = regionRepository.findById(734);if (region == null){ region = new Region( 734, "IT", "CL", "Caltanisetta"); regionRepository.save(region); }
        region = regionRepository.findById(735);if (region == null){ region = new Region( 735, "IT", "CN", "Cuneo"); regionRepository.save(region); }
        region = regionRepository.findById(736);if (region == null){ region = new Region( 736, "IT", "CO", "Como"); regionRepository.save(region); }
        region = regionRepository.findById(737);if (region == null){ region = new Region( 737, "IT", "CR", "Cremona"); regionRepository.save(region); }
        region = regionRepository.findById(738);if (region == null){ region = new Region( 738, "IT", "CS", "Cosenza"); regionRepository.save(region); }
        region = regionRepository.findById(739);if (region == null){ region = new Region( 739, "IT", "CT", "Catania"); regionRepository.save(region); }
        region = regionRepository.findById(740);if (region == null){ region = new Region( 740, "IT", "CZ", "Catanzaro"); regionRepository.save(region); }
        region = regionRepository.findById(741);if (region == null){ region = new Region( 741, "IT", "EE", "Stati Esteri"); regionRepository.save(region); }
        region = regionRepository.findById(742);if (region == null){ region = new Region( 742, "IT", "EN", "Enna"); regionRepository.save(region); }
        region = regionRepository.findById(743);if (region == null){ region = new Region( 743, "IT", "FC", "Forli/Cesana"); regionRepository.save(region); }
        region = regionRepository.findById(744);if (region == null){ region = new Region( 744, "IT", "FE", "Ferrara"); regionRepository.save(region); }
        region = regionRepository.findById(745);if (region == null){ region = new Region( 745, "IT", "FG", "Foggia"); regionRepository.save(region); }
        region = regionRepository.findById(746);if (region == null){ region = new Region( 746, "IT", "FI", "Florence"); regionRepository.save(region); }
        region = regionRepository.findById(747);if (region == null){ region = new Region( 747, "IT", "FR", "Frosinone"); regionRepository.save(region); }
        region = regionRepository.findById(748);if (region == null){ region = new Region( 748, "IT", "FU", "Fiume"); regionRepository.save(region); }
        region = regionRepository.findById(749);if (region == null){ region = new Region( 749, "IT", "GE", "Genova"); regionRepository.save(region); }
        region = regionRepository.findById(750);if (region == null){ region = new Region( 750, "IT", "GO", "Gorizia"); regionRepository.save(region); }
        region = regionRepository.findById(751);if (region == null){ region = new Region( 751, "IT", "GR", "Grosseto"); regionRepository.save(region); }
        region = regionRepository.findById(752);if (region == null){ region = new Region( 752, "IT", "IM", "Imperia"); regionRepository.save(region); }
        region = regionRepository.findById(753);if (region == null){ region = new Region( 753, "IT", "IS", "Isernia"); regionRepository.save(region); }
        region = regionRepository.findById(754);if (region == null){ region = new Region( 754, "IT", "KR", "Crotone"); regionRepository.save(region); }
        region = regionRepository.findById(755);if (region == null){ region = new Region( 755, "IT", "LC", "Lecco"); regionRepository.save(region); }
        region = regionRepository.findById(756);if (region == null){ region = new Region( 756, "IT", "LE", "Lecce"); regionRepository.save(region); }
        region = regionRepository.findById(757);if (region == null){ region = new Region( 757, "IT", "LI", "Livorno"); regionRepository.save(region); }
        region = regionRepository.findById(758);if (region == null){ region = new Region( 758, "IT", "LO", "Lodi"); regionRepository.save(region); }
        region = regionRepository.findById(759);if (region == null){ region = new Region( 759, "IT", "LT", "Latina"); regionRepository.save(region); }
        region = regionRepository.findById(760);if (region == null){ region = new Region( 760, "IT", "LU", "Lucca"); regionRepository.save(region); }
        region = regionRepository.findById(761);if (region == null){ region = new Region( 761, "IT", "MB", "Monza e Brianza"); regionRepository.save(region); }
        region = regionRepository.findById(762);if (region == null){ region = new Region( 762, "IT", "MC", "Macerata"); regionRepository.save(region); }
        region = regionRepository.findById(763);if (region == null){ region = new Region( 763, "IT", "ME", "Messina"); regionRepository.save(region); }
        region = regionRepository.findById(764);if (region == null){ region = new Region( 764, "IT", "MI", "Milan"); regionRepository.save(region); }
        region = regionRepository.findById(765);if (region == null){ region = new Region( 765, "IT", "MN", "Mantova"); regionRepository.save(region); }
        region = regionRepository.findById(766);if (region == null){ region = new Region( 766, "IT", "MO", "Modena"); regionRepository.save(region); }
        region = regionRepository.findById(767);if (region == null){ region = new Region( 767, "IT", "MS", "Massa Carrara"); regionRepository.save(region); }
        region = regionRepository.findById(768);if (region == null){ region = new Region( 768, "IT", "MT", "Matera"); regionRepository.save(region); }
        region = regionRepository.findById(769);if (region == null){ region = new Region( 769, "IT", "NA", "Naples"); regionRepository.save(region); }
        region = regionRepository.findById(770);if (region == null){ region = new Region( 770, "IT", "NO", "Novara"); regionRepository.save(region); }
        region = regionRepository.findById(771);if (region == null){ region = new Region( 771, "IT", "NU", "Nuoro"); regionRepository.save(region); }
        region = regionRepository.findById(772);if (region == null){ region = new Region( 772, "IT", "OG", "Ogliastra"); regionRepository.save(region); }
        region = regionRepository.findById(773);if (region == null){ region = new Region( 773, "IT", "OR", "Oristano"); regionRepository.save(region); }
        region = regionRepository.findById(774);if (region == null){ region = new Region( 774, "IT", "OT", "Olbia-Tempio"); regionRepository.save(region); }
        region = regionRepository.findById(775);if (region == null){ region = new Region( 775, "IT", "PA", "Palermo"); regionRepository.save(region); }
        region = regionRepository.findById(776);if (region == null){ region = new Region( 776, "IT", "PC", "Piacenza"); regionRepository.save(region); }
        region = regionRepository.findById(777);if (region == null){ region = new Region( 777, "IT", "PD", "Padova"); regionRepository.save(region); }
        region = regionRepository.findById(778);if (region == null){ region = new Region( 778, "IT", "PE", "Pescara"); regionRepository.save(region); }
        region = regionRepository.findById(779);if (region == null){ region = new Region( 779, "IT", "PG", "Perugia"); regionRepository.save(region); }
        region = regionRepository.findById(780);if (region == null){ region = new Region( 780, "IT", "PI", "Pisa"); regionRepository.save(region); }
        region = regionRepository.findById(781);if (region == null){ region = new Region( 781, "IT", "PL", "Pola"); regionRepository.save(region); }
        region = regionRepository.findById(782);if (region == null){ region = new Region( 782, "IT", "PN", "Pordenone"); regionRepository.save(region); }
        region = regionRepository.findById(783);if (region == null){ region = new Region( 783, "IT", "PO", "Prato"); regionRepository.save(region); }
        region = regionRepository.findById(784);if (region == null){ region = new Region( 784, "IT", "PR", "Parma"); regionRepository.save(region); }
        region = regionRepository.findById(785);if (region == null){ region = new Region( 785, "IT", "PT", "Pistoia"); regionRepository.save(region); }
        region = regionRepository.findById(786);if (region == null){ region = new Region( 786, "IT", "PU", "Pesaro-Urbino"); regionRepository.save(region); }
        region = regionRepository.findById(787);if (region == null){ region = new Region( 787, "IT", "PV", "Pavia"); regionRepository.save(region); }
        region = regionRepository.findById(788);if (region == null){ region = new Region( 788, "IT", "PZ", "Potenza"); regionRepository.save(region); }
        region = regionRepository.findById(789);if (region == null){ region = new Region( 789, "IT", "RA", "Ravenna"); regionRepository.save(region); }
        region = regionRepository.findById(790);if (region == null){ region = new Region( 790, "IT", "RC", "Reggio Calabria"); regionRepository.save(region); }
        region = regionRepository.findById(791);if (region == null){ region = new Region( 791, "IT", "RE", "Reggio Emilia"); regionRepository.save(region); }
        region = regionRepository.findById(792);if (region == null){ region = new Region( 792, "IT", "RG", "Ragusa"); regionRepository.save(region); }
        region = regionRepository.findById(793);if (region == null){ region = new Region( 793, "IT", "RI", "Rieti"); regionRepository.save(region); }
        region = regionRepository.findById(794);if (region == null){ region = new Region( 794, "IT", "RM", "Rome"); regionRepository.save(region); }
        region = regionRepository.findById(795);if (region == null){ region = new Region( 795, "IT", "RN", "Rimini"); regionRepository.save(region); }
        region = regionRepository.findById(796);if (region == null){ region = new Region( 796, "IT", "RO", "Rovigo"); regionRepository.save(region); }
        region = regionRepository.findById(797);if (region == null){ region = new Region( 797, "IT", "SA", "Salerno"); regionRepository.save(region); }
        region = regionRepository.findById(798);if (region == null){ region = new Region( 798, "IT", "SI", "Siena"); regionRepository.save(region); }
        region = regionRepository.findById(799);if (region == null){ region = new Region( 799, "IT", "SO", "Sondrio"); regionRepository.save(region); }
        region = regionRepository.findById(800);if (region == null){ region = new Region( 800, "IT", "SP", "La Spezia"); regionRepository.save(region); }
        region = regionRepository.findById(801);if (region == null){ region = new Region( 801, "IT", "SR", "Siracusa"); regionRepository.save(region); }
        region = regionRepository.findById(802);if (region == null){ region = new Region( 802, "IT", "SS", "Sassari"); regionRepository.save(region); }
        region = regionRepository.findById(803);if (region == null){ region = new Region( 803, "IT", "SV", "Savona"); regionRepository.save(region); }
        region = regionRepository.findById(804);if (region == null){ region = new Region( 804, "IT", "TA", "Taranto"); regionRepository.save(region); }
        region = regionRepository.findById(805);if (region == null){ region = new Region( 805, "IT", "TE", "Teramo"); regionRepository.save(region); }
        region = regionRepository.findById(806);if (region == null){ region = new Region( 806, "IT", "TN", "Trento"); regionRepository.save(region); }
        region = regionRepository.findById(807);if (region == null){ region = new Region( 807, "IT", "TO", "Turin"); regionRepository.save(region); }
        region = regionRepository.findById(808);if (region == null){ region = new Region( 808, "IT", "TP", "Trapani"); regionRepository.save(region); }
        region = regionRepository.findById(809);if (region == null){ region = new Region( 809, "IT", "TR", "Terni"); regionRepository.save(region); }
        region = regionRepository.findById(810);if (region == null){ region = new Region( 810, "IT", "TS", "Trieste"); regionRepository.save(region); }
        region = regionRepository.findById(811);if (region == null){ region = new Region( 811, "IT", "TV", "Treviso"); regionRepository.save(region); }
        region = regionRepository.findById(812);if (region == null){ region = new Region( 812, "IT", "UD", "Udine"); regionRepository.save(region); }
        region = regionRepository.findById(813);if (region == null){ region = new Region( 813, "IT", "VA", "Varese"); regionRepository.save(region); }
        region = regionRepository.findById(814);if (region == null){ region = new Region( 814, "IT", "VB", "Verbania"); regionRepository.save(region); }
        region = regionRepository.findById(815);if (region == null){ region = new Region( 815, "IT", "VC", "Vercelli"); regionRepository.save(region); }
        region = regionRepository.findById(816);if (region == null){ region = new Region( 816, "IT", "VE", "Venice"); regionRepository.save(region); }
        region = regionRepository.findById(817);if (region == null){ region = new Region( 817, "IT", "VI", "Vicenza"); regionRepository.save(region); }
        region = regionRepository.findById(818);if (region == null){ region = new Region( 818, "IT", "VR", "Verona"); regionRepository.save(region); }
        region = regionRepository.findById(819);if (region == null){ region = new Region( 819, "IT", "VS", "Medio Campidano"); regionRepository.save(region); }
        region = regionRepository.findById(820);if (region == null){ region = new Region( 820, "IT", "VT", "Viterbo"); regionRepository.save(region); }
        region = regionRepository.findById(821);if (region == null){ region = new Region( 821, "IT", "VV", "Vibo Valentia"); regionRepository.save(region); }
        region = regionRepository.findById(822);if (region == null){ region = new Region( 822, "IT", "ZA", "Zara"); regionRepository.save(region); }
        region = regionRepository.findById(823);if (region == null){ region = new Region( 823, "JP", "01", "Hokkaido"); regionRepository.save(region); }
        region = regionRepository.findById(824);if (region == null){ region = new Region( 824, "JP", "02", "Aomori"); regionRepository.save(region); }
        region = regionRepository.findById(825);if (region == null){ region = new Region( 825, "JP", "03", "Iwate"); regionRepository.save(region); }
        region = regionRepository.findById(826);if (region == null){ region = new Region( 826, "JP", "04", "Miyagi"); regionRepository.save(region); }
        region = regionRepository.findById(827);if (region == null){ region = new Region( 827, "JP", "05", "Akita"); regionRepository.save(region); }
        region = regionRepository.findById(828);if (region == null){ region = new Region( 828, "JP", "06", "Yamagata"); regionRepository.save(region); }
        region = regionRepository.findById(829);if (region == null){ region = new Region( 829, "JP", "07", "Fukushima"); regionRepository.save(region); }
        region = regionRepository.findById(830);if (region == null){ region = new Region( 830, "JP", "08", "Ibaraki"); regionRepository.save(region); }
        region = regionRepository.findById(831);if (region == null){ region = new Region( 831, "JP", "09", "Tochigi"); regionRepository.save(region); }
        region = regionRepository.findById(832);if (region == null){ region = new Region( 832, "JP", "10", "Gunma"); regionRepository.save(region); }
        region = regionRepository.findById(833);if (region == null){ region = new Region( 833, "JP", "11", "Saitama"); regionRepository.save(region); }
        region = regionRepository.findById(834);if (region == null){ region = new Region( 834, "JP", "12", "Chiba"); regionRepository.save(region); }
        region = regionRepository.findById(835);if (region == null){ region = new Region( 835, "JP", "13", "Tokyo"); regionRepository.save(region); }
        region = regionRepository.findById(836);if (region == null){ region = new Region( 836, "JP", "14", "Kanagawa"); regionRepository.save(region); }
        region = regionRepository.findById(837);if (region == null){ region = new Region( 837, "JP", "15", "Niigata"); regionRepository.save(region); }
        region = regionRepository.findById(838);if (region == null){ region = new Region( 838, "JP", "16", "Toyama"); regionRepository.save(region); }
        region = regionRepository.findById(839);if (region == null){ region = new Region( 839, "JP", "17", "Ishikawa"); regionRepository.save(region); }
        region = regionRepository.findById(840);if (region == null){ region = new Region( 840, "JP", "18", "Fukui"); regionRepository.save(region); }
        region = regionRepository.findById(841);if (region == null){ region = new Region( 841, "JP", "19", "Yamanashi"); regionRepository.save(region); }
        region = regionRepository.findById(842);if (region == null){ region = new Region( 842, "JP", "20", "Nagano"); regionRepository.save(region); }
        region = regionRepository.findById(843);if (region == null){ region = new Region( 843, "JP", "21", "Gifu"); regionRepository.save(region); }
        region = regionRepository.findById(844);if (region == null){ region = new Region( 844, "JP", "22", "Shizuoka"); regionRepository.save(region); }
        region = regionRepository.findById(845);if (region == null){ region = new Region( 845, "JP", "23", "Aichi"); regionRepository.save(region); }
        region = regionRepository.findById(846);if (region == null){ region = new Region( 846, "JP", "24", "Mie"); regionRepository.save(region); }
        region = regionRepository.findById(847);if (region == null){ region = new Region( 847, "JP", "25", "Shiga"); regionRepository.save(region); }
        region = regionRepository.findById(848);if (region == null){ region = new Region( 848, "JP", "26", "Kyoto"); regionRepository.save(region); }
        region = regionRepository.findById(849);if (region == null){ region = new Region( 849, "JP", "27", "Osaka"); regionRepository.save(region); }
        region = regionRepository.findById(850);if (region == null){ region = new Region( 850, "JP", "28", "Hyogo"); regionRepository.save(region); }
        region = regionRepository.findById(851);if (region == null){ region = new Region( 851, "JP", "29", "Nara"); regionRepository.save(region); }
        region = regionRepository.findById(852);if (region == null){ region = new Region( 852, "JP", "30", "Wakayama"); regionRepository.save(region); }
        region = regionRepository.findById(853);if (region == null){ region = new Region( 853, "JP", "31", "Tottori"); regionRepository.save(region); }
        region = regionRepository.findById(854);if (region == null){ region = new Region( 854, "JP", "32", "Shimane"); regionRepository.save(region); }
        region = regionRepository.findById(855);if (region == null){ region = new Region( 855, "JP", "33", "Okayama"); regionRepository.save(region); }
        region = regionRepository.findById(856);if (region == null){ region = new Region( 856, "JP", "34", "Hiroshima"); regionRepository.save(region); }
        region = regionRepository.findById(857);if (region == null){ region = new Region( 857, "JP", "35", "Yamaguchi"); regionRepository.save(region); }
        region = regionRepository.findById(858);if (region == null){ region = new Region( 858, "JP", "36", "Tokushima"); regionRepository.save(region); }
        region = regionRepository.findById(859);if (region == null){ region = new Region( 859, "JP", "37", "Kagawa"); regionRepository.save(region); }
        region = regionRepository.findById(860);if (region == null){ region = new Region( 860, "JP", "38", "Ehime"); regionRepository.save(region); }
        region = regionRepository.findById(861);if (region == null){ region = new Region( 861, "JP", "39", "Kochi"); regionRepository.save(region); }
        region = regionRepository.findById(862);if (region == null){ region = new Region( 862, "JP", "40", "Fukuoka"); regionRepository.save(region); }
        region = regionRepository.findById(863);if (region == null){ region = new Region( 863, "JP", "41", "Saga"); regionRepository.save(region); }
        region = regionRepository.findById(864);if (region == null){ region = new Region( 864, "JP", "42", "Nagasaki"); regionRepository.save(region); }
        region = regionRepository.findById(865);if (region == null){ region = new Region( 865, "JP", "43", "Kumamoto"); regionRepository.save(region); }
        region = regionRepository.findById(866);if (region == null){ region = new Region( 866, "JP", "44", "Oita"); regionRepository.save(region); }
        region = regionRepository.findById(867);if (region == null){ region = new Region( 867, "JP", "45", "Miyazaki"); regionRepository.save(region); }
        region = regionRepository.findById(868);if (region == null){ region = new Region( 868, "JP", "46", "Kagoshima"); regionRepository.save(region); }
        region = regionRepository.findById(869);if (region == null){ region = new Region( 869, "JP", "47", "Okinawa"); regionRepository.save(region); }
        region = regionRepository.findById(870);if (region == null){ region = new Region( 870, "KR", "01", "Cheju-do"); regionRepository.save(region); }
        region = regionRepository.findById(871);if (region == null){ region = new Region( 871, "KR", "02", "Cholla-bukto"); regionRepository.save(region); }
        region = regionRepository.findById(872);if (region == null){ region = new Region( 872, "KR", "03", "Cholla-namdo"); regionRepository.save(region); }
        region = regionRepository.findById(873);if (region == null){ region = new Region( 873, "KR", "04", "Ch´ungch´ong-bukto"); regionRepository.save(region); }
        region = regionRepository.findById(874);if (region == null){ region = new Region( 874, "KR", "05", "Ch´ungch´ong-namdo"); regionRepository.save(region); }
        region = regionRepository.findById(875);if (region == null){ region = new Region( 875, "KR", "06", "Inch´on-jikhalsi"); regionRepository.save(region); }
        region = regionRepository.findById(876);if (region == null){ region = new Region( 876, "KR", "07", "Kangwon-do"); regionRepository.save(region); }
        region = regionRepository.findById(877);if (region == null){ region = new Region( 877, "KR", "08", "Kwangju-jikhalsi"); regionRepository.save(region); }
        region = regionRepository.findById(878);if (region == null){ region = new Region( 878, "KR", "09", "Kyonggi-do"); regionRepository.save(region); }
        region = regionRepository.findById(879);if (region == null){ region = new Region( 879, "KR", "10", "Kyongsang-bukto"); regionRepository.save(region); }
        region = regionRepository.findById(880);if (region == null){ region = new Region( 880, "KR", "11", "Kyongsang-namdo"); regionRepository.save(region); }
        region = regionRepository.findById(881);if (region == null){ region = new Region( 881, "KR", "12", "Pusan-jikhalsi"); regionRepository.save(region); }
        region = regionRepository.findById(882);if (region == null){ region = new Region( 882, "KR", "13", "Soul-t´ukpyolsi"); regionRepository.save(region); }
        region = regionRepository.findById(883);if (region == null){ region = new Region( 883, "KR", "14", "Taegu-jikhalsi"); regionRepository.save(region); }
        region = regionRepository.findById(884);if (region == null){ region = new Region( 884, "KR", "15", "Taejon-jikhalsi"); regionRepository.save(region); }
        region = regionRepository.findById(885);if (region == null){ region = new Region( 885, "KZ", "00", "Almatynskaia"); regionRepository.save(region); }
        region = regionRepository.findById(886);if (region == null){ region = new Region( 886, "KZ", "01", "Kostanaiskaia"); regionRepository.save(region); }
        region = regionRepository.findById(887);if (region == null){ region = new Region( 887, "KZ", "02", "Severo-Kazakhstansk"); regionRepository.save(region); }
        region = regionRepository.findById(888);if (region == null){ region = new Region( 888, "KZ", "03", "Pavlodarskaia"); regionRepository.save(region); }
        region = regionRepository.findById(889);if (region == null){ region = new Region( 889, "KZ", "04", "Akmolinskaia"); regionRepository.save(region); }
        region = regionRepository.findById(890);if (region == null){ region = new Region( 890, "KZ", "05", "Aktubinskaia"); regionRepository.save(region); }
        region = regionRepository.findById(891);if (region == null){ region = new Region( 891, "KZ", "06", "Atyrauskaia"); regionRepository.save(region); }
        region = regionRepository.findById(892);if (region == null){ region = new Region( 892, "KZ", "07", "Zapadno-Kazakhst"); regionRepository.save(region); }
        region = regionRepository.findById(893);if (region == null){ region = new Region( 893, "KZ", "08", "Mangystayskaia"); regionRepository.save(region); }
        region = regionRepository.findById(894);if (region == null){ region = new Region( 894, "KZ", "09", "Karagandinskaia"); regionRepository.save(region); }
        region = regionRepository.findById(895);if (region == null){ region = new Region( 895, "KZ", "10", "Vostochno-Kazakhstan"); regionRepository.save(region); }
        region = regionRepository.findById(896);if (region == null){ region = new Region( 896, "KZ", "11", "Gambilskaia"); regionRepository.save(region); }
        region = regionRepository.findById(897);if (region == null){ region = new Region( 897, "KZ", "12", "Kyzilordinskaia"); regionRepository.save(region); }
        region = regionRepository.findById(898);if (region == null){ region = new Region( 898, "MX", "AGS", "Aguascalientes"); regionRepository.save(region); }
        region = regionRepository.findById(899);if (region == null){ region = new Region( 899, "MX", "BC", "Baja California"); regionRepository.save(region); }
        region = regionRepository.findById(900);if (region == null){ region = new Region( 900, "MX", "BCS", "Baja California S"); regionRepository.save(region); }
        region = regionRepository.findById(901);if (region == null){ region = new Region( 901, "MX", "CHI", "Chihuahua"); regionRepository.save(region); }
        region = regionRepository.findById(902);if (region == null){ region = new Region( 902, "MX", "CHS", "Chiapas"); regionRepository.save(region); }
        region = regionRepository.findById(903);if (region == null){ region = new Region( 903, "MX", "CMP", "Campeche"); regionRepository.save(region); }
        region = regionRepository.findById(904);if (region == null){ region = new Region( 904, "MX", "COA", "Coahuila"); regionRepository.save(region); }
        region = regionRepository.findById(905);if (region == null){ region = new Region( 905, "MX", "COL", "Colima"); regionRepository.save(region); }
        region = regionRepository.findById(906);if (region == null){ region = new Region( 906, "MX", "DF", "Distrito Federal"); regionRepository.save(region); }
        region = regionRepository.findById(907);if (region == null){ region = new Region( 907, "MX", "DGO", "Durango"); regionRepository.save(region); }
        region = regionRepository.findById(908);if (region == null){ region = new Region( 908, "MX", "GRO", "Guerrero"); regionRepository.save(region); }
        region = regionRepository.findById(909);if (region == null){ region = new Region( 909, "MX", "GTO", "Guanajuato"); regionRepository.save(region); }
        region = regionRepository.findById(910);if (region == null){ region = new Region( 910, "MX", "HGO", "Hidalgo"); regionRepository.save(region); }
        region = regionRepository.findById(911);if (region == null){ region = new Region( 911, "MX", "JAL", "Jalisco"); regionRepository.save(region); }
        region = regionRepository.findById(912);if (region == null){ region = new Region( 912, "MX", "MCH", "Michoacan"); regionRepository.save(region); }
        region = regionRepository.findById(913);if (region == null){ region = new Region( 913, "MX", "MEX", "Estado de Mexico"); regionRepository.save(region); }
        region = regionRepository.findById(914);if (region == null){ region = new Region( 914, "MX", "MOR", "Morelos"); regionRepository.save(region); }
        region = regionRepository.findById(915);if (region == null){ region = new Region( 915, "MX", "NAY", "Nayarit"); regionRepository.save(region); }
        region = regionRepository.findById(916);if (region == null){ region = new Region( 916, "MX", "NL", "Nuevo Leon"); regionRepository.save(region); }
        region = regionRepository.findById(917);if (region == null){ region = new Region( 917, "MX", "OAX", "Oaxaca"); regionRepository.save(region); }
        region = regionRepository.findById(918);if (region == null){ region = new Region( 918, "MX", "PUE", "Puebla"); regionRepository.save(region); }
        region = regionRepository.findById(919);if (region == null){ region = new Region( 919, "MX", "QR", "Quintana Roo"); regionRepository.save(region); }
        region = regionRepository.findById(920);if (region == null){ region = new Region( 920, "MX", "QRO", "Queretaro"); regionRepository.save(region); }
        region = regionRepository.findById(921);if (region == null){ region = new Region( 921, "MX", "SIN", "Sinaloa"); regionRepository.save(region); }
        region = regionRepository.findById(922);if (region == null){ region = new Region( 922, "MX", "SLP", "San Luis Potosi"); regionRepository.save(region); }
        region = regionRepository.findById(923);if (region == null){ region = new Region( 923, "MX", "SON", "Sonora"); regionRepository.save(region); }
        region = regionRepository.findById(924);if (region == null){ region = new Region( 924, "MX", "TAB", "Tabasco"); regionRepository.save(region); }
        region = regionRepository.findById(925);if (region == null){ region = new Region( 925, "MX", "TLX", "Tlaxcala"); regionRepository.save(region); }
        region = regionRepository.findById(926);if (region == null){ region = new Region( 926, "MX", "TMS", "Tamaulipas"); regionRepository.save(region); }
        region = regionRepository.findById(927);if (region == null){ region = new Region( 927, "MX", "VER", "Veracruz"); regionRepository.save(region); }
        region = regionRepository.findById(928);if (region == null){ region = new Region( 928, "MX", "YUC", "Yucatan"); regionRepository.save(region); }
        region = regionRepository.findById(929);if (region == null){ region = new Region( 929, "MX", "ZAC", "Zacatecas"); regionRepository.save(region); }
        region = regionRepository.findById(930);if (region == null){ region = new Region( 930, "MY", "JOH", "Johor"); regionRepository.save(region); }
        region = regionRepository.findById(931);if (region == null){ region = new Region( 931, "MY", "KED", "Kedah"); regionRepository.save(region); }
        region = regionRepository.findById(932);if (region == null){ region = new Region( 932, "MY", "KEL", "Kelantan"); regionRepository.save(region); }
        region = regionRepository.findById(933);if (region == null){ region = new Region( 933, "MY", "KUL", "Kuala Lumpur"); regionRepository.save(region); }
        region = regionRepository.findById(934);if (region == null){ region = new Region( 934, "MY", "LAB", "Labuan"); regionRepository.save(region); }
        region = regionRepository.findById(935);if (region == null){ region = new Region( 935, "MY", "MEL", "Melaka"); regionRepository.save(region); }
        region = regionRepository.findById(936);if (region == null){ region = new Region( 936, "MY", "PAH", "Pahang"); regionRepository.save(region); }
        region = regionRepository.findById(937);if (region == null){ region = new Region( 937, "MY", "PEL", "Perlis"); regionRepository.save(region); }
        region = regionRepository.findById(938);if (region == null){ region = new Region( 938, "MY", "PER", "Perak"); regionRepository.save(region); }
        region = regionRepository.findById(939);if (region == null){ region = new Region( 939, "MY", "PIN", "Pulau Pinang"); regionRepository.save(region); }
        region = regionRepository.findById(940);if (region == null){ region = new Region( 940, "MY", "PSK", "Wil. Persekutuan"); regionRepository.save(region); }
        region = regionRepository.findById(941);if (region == null){ region = new Region( 941, "MY", "SAB", "Sabah"); regionRepository.save(region); }
        region = regionRepository.findById(942);if (region == null){ region = new Region( 942, "MY", "SAR", "Sarawak"); regionRepository.save(region); }
        region = regionRepository.findById(943);if (region == null){ region = new Region( 943, "MY", "SEL", "Selangor"); regionRepository.save(region); }
        region = regionRepository.findById(944);if (region == null){ region = new Region( 944, "MY", "SER", "Negeri Sembilan"); regionRepository.save(region); }
        region = regionRepository.findById(945);if (region == null){ region = new Region( 945, "MY", "TRE", "Trengganu"); regionRepository.save(region); }
        region = regionRepository.findById(946);if (region == null){ region = new Region( 946, "NL", "01", "Drenthe"); regionRepository.save(region); }
        region = regionRepository.findById(947);if (region == null){ region = new Region( 947, "NL", "02", "Flevoland"); regionRepository.save(region); }
        region = regionRepository.findById(948);if (region == null){ region = new Region( 948, "NL", "03", "Friesland"); regionRepository.save(region); }
        region = regionRepository.findById(949);if (region == null){ region = new Region( 949, "NL", "04", "Gelderland"); regionRepository.save(region); }
        region = regionRepository.findById(950);if (region == null){ region = new Region( 950, "NL", "05", "Groningen"); regionRepository.save(region); }
        region = regionRepository.findById(951);if (region == null){ region = new Region( 951, "NL", "06", "Limburg"); regionRepository.save(region); }
        region = regionRepository.findById(952);if (region == null){ region = new Region( 952, "NL", "07", "Noord-Brabant"); regionRepository.save(region); }
        region = regionRepository.findById(953);if (region == null){ region = new Region( 953, "NL", "08", "Noord-Holland"); regionRepository.save(region); }
        region = regionRepository.findById(954);if (region == null){ region = new Region( 954, "NL", "09", "Overijssel"); regionRepository.save(region); }
        region = regionRepository.findById(955);if (region == null){ region = new Region( 955, "NL", "10", "Utrecht"); regionRepository.save(region); }
        region = regionRepository.findById(956);if (region == null){ region = new Region( 956, "NL", "11", "Zeeland"); regionRepository.save(region); }
        region = regionRepository.findById(957);if (region == null){ region = new Region( 957, "NL", "12", "Zuid-Holland"); regionRepository.save(region); }
        region = regionRepository.findById(958);if (region == null){ region = new Region( 958, "NO", "01", "Ostfold County"); regionRepository.save(region); }
        region = regionRepository.findById(959);if (region == null){ region = new Region( 959, "NO", "02", "Akershus County"); regionRepository.save(region); }
        region = regionRepository.findById(960);if (region == null){ region = new Region( 960, "NO", "03", "Oslo"); regionRepository.save(region); }
        region = regionRepository.findById(961);if (region == null){ region = new Region( 961, "NO", "04", "Hedmark County"); regionRepository.save(region); }
        region = regionRepository.findById(962);if (region == null){ region = new Region( 962, "NO", "05", "Oppland County"); regionRepository.save(region); }
        region = regionRepository.findById(963);if (region == null){ region = new Region( 963, "NO", "06", "Buskerud County"); regionRepository.save(region); }
        region = regionRepository.findById(964);if (region == null){ region = new Region( 964, "NO", "07", "Vestfold County"); regionRepository.save(region); }
        region = regionRepository.findById(965);if (region == null){ region = new Region( 965, "NO", "08", "Telemark County"); regionRepository.save(region); }
        region = regionRepository.findById(966);if (region == null){ region = new Region( 966, "NO", "09", "Aust-Agder County"); regionRepository.save(region); }
        region = regionRepository.findById(967);if (region == null){ region = new Region( 967, "NO", "10", "Vest-Agder County"); regionRepository.save(region); }
        region = regionRepository.findById(968);if (region == null){ region = new Region( 968, "NO", "11", "Rogaland County"); regionRepository.save(region); }
        region = regionRepository.findById(969);if (region == null){ region = new Region( 969, "NO", "12", "Hordaland County"); regionRepository.save(region); }
        region = regionRepository.findById(970);if (region == null){ region = new Region( 970, "NO", "14", "Sogn og Fjordane C."); regionRepository.save(region); }
        region = regionRepository.findById(971);if (region == null){ region = new Region( 971, "NO", "15", "More og Romsdal C."); regionRepository.save(region); }
        region = regionRepository.findById(972);if (region == null){ region = new Region( 972, "NO", "16", "Sor-Trondelag County"); regionRepository.save(region); }
        region = regionRepository.findById(973);if (region == null){ region = new Region( 973, "NO", "17", "Nord-Trondelag Cnty"); regionRepository.save(region); }
        region = regionRepository.findById(974);if (region == null){ region = new Region( 974, "NO", "18", "Nordland County"); regionRepository.save(region); }
        region = regionRepository.findById(975);if (region == null){ region = new Region( 975, "NO", "19", "Troms County"); regionRepository.save(region); }
        region = regionRepository.findById(976);if (region == null){ region = new Region( 976, "NO", "20", "Finnmark County"); regionRepository.save(region); }
        region = regionRepository.findById(977);if (region == null){ region = new Region( 977, "NZ", "AKL", "Auckland"); regionRepository.save(region); }
        region = regionRepository.findById(978);if (region == null){ region = new Region( 978, "NZ", "BOP", "Bay of Plenty"); regionRepository.save(region); }
        region = regionRepository.findById(979);if (region == null){ region = new Region( 979, "NZ", "CAN", "Canterbury"); regionRepository.save(region); }
        region = regionRepository.findById(980);if (region == null){ region = new Region( 980, "NZ", "HAB", "Hawke´s Bay"); regionRepository.save(region); }
        region = regionRepository.findById(981);if (region == null){ region = new Region( 981, "NZ", "MAN", "Manawatu-Wanganui"); regionRepository.save(region); }
        region = regionRepository.findById(982);if (region == null){ region = new Region( 982, "NZ", "NTL", "Northland"); regionRepository.save(region); }
        region = regionRepository.findById(983);if (region == null){ region = new Region( 983, "NZ", "OTA", "Otago"); regionRepository.save(region); }
        region = regionRepository.findById(984);if (region == null){ region = new Region( 984, "NZ", "STL", "Southland"); regionRepository.save(region); }
        region = regionRepository.findById(985);if (region == null){ region = new Region( 985, "NZ", "TAR", "Taranaki"); regionRepository.save(region); }
        region = regionRepository.findById(986);if (region == null){ region = new Region( 986, "NZ", "WAI", "Waikato"); regionRepository.save(region); }
        region = regionRepository.findById(987);if (region == null){ region = new Region( 987, "NZ", "WEC", "West Coast"); regionRepository.save(region); }
        region = regionRepository.findById(988);if (region == null){ region = new Region( 988, "NZ", "WLG", "Wellington"); regionRepository.save(region); }
        region = regionRepository.findById(989);if (region == null){ region = new Region( 989, "PE", "01", "Tumbes"); regionRepository.save(region); }
        region = regionRepository.findById(990);if (region == null){ region = new Region( 990, "PE", "02", "Piura"); regionRepository.save(region); }
        region = regionRepository.findById(991);if (region == null){ region = new Region( 991, "PE", "03", "Lambayeque"); regionRepository.save(region); }
        region = regionRepository.findById(992);if (region == null){ region = new Region( 992, "PE", "04", "La Libertad"); regionRepository.save(region); }
        region = regionRepository.findById(993);if (region == null){ region = new Region( 993, "PE", "05", "Ancash"); regionRepository.save(region); }
        region = regionRepository.findById(994);if (region == null){ region = new Region( 994, "PE", "06", "Lima y Callao"); regionRepository.save(region); }
        region = regionRepository.findById(995);if (region == null){ region = new Region( 995, "PE", "07", "Ica"); regionRepository.save(region); }
        region = regionRepository.findById(996);if (region == null){ region = new Region( 996, "PE", "08", "Arequipa"); regionRepository.save(region); }
        region = regionRepository.findById(997);if (region == null){ region = new Region( 997, "PE", "09", "Moquegua"); regionRepository.save(region); }
        region = regionRepository.findById(998);if (region == null){ region = new Region( 998, "PE", "10", "Tacna"); regionRepository.save(region); }
        region = regionRepository.findById(999);if (region == null){ region = new Region( 999, "PE", "11", "Amazon"); regionRepository.save(region); }
        region = regionRepository.findById(1000);if (region == null){ region = new Region( 1000, "PE", "12", "Cajamarca"); regionRepository.save(region); }
        region = regionRepository.findById(1001);if (region == null){ region = new Region( 1001, "PE", "13", "San Martin"); regionRepository.save(region); }
        region = regionRepository.findById(1002);if (region == null){ region = new Region( 1002, "PE", "14", "Huanuco"); regionRepository.save(region); }
        region = regionRepository.findById(1003);if (region == null){ region = new Region( 1003, "PE", "15", "Pasco"); regionRepository.save(region); }
        region = regionRepository.findById(1004);if (region == null){ region = new Region( 1004, "PE", "16", "Junin"); regionRepository.save(region); }
        region = regionRepository.findById(1005);if (region == null){ region = new Region( 1005, "PE", "17", "Huancavelica"); regionRepository.save(region); }
        region = regionRepository.findById(1006);if (region == null){ region = new Region( 1006, "PE", "18", "Ayacucho"); regionRepository.save(region); }
        region = regionRepository.findById(1007);if (region == null){ region = new Region( 1007, "PE", "19", "Apurimac"); regionRepository.save(region); }
        region = regionRepository.findById(1008);if (region == null){ region = new Region( 1008, "PE", "20", "Cuzco"); regionRepository.save(region); }
        region = regionRepository.findById(1009);if (region == null){ region = new Region( 1009, "PE", "21", "Puno"); regionRepository.save(region); }
        region = regionRepository.findById(1010);if (region == null){ region = new Region( 1010, "PE", "22", "Loreto"); regionRepository.save(region); }
        region = regionRepository.findById(1011);if (region == null){ region = new Region( 1011, "PE", "23", "Ucayali"); regionRepository.save(region); }
        region = regionRepository.findById(1012);if (region == null){ region = new Region( 1012, "PE", "24", "Madre de Dios"); regionRepository.save(region); }
        region = regionRepository.findById(1013);if (region == null){ region = new Region( 1013, "PH", "01", "Ilocos"); regionRepository.save(region); }
        region = regionRepository.findById(1014);if (region == null){ region = new Region( 1014, "PH", "02", "Cagayan Valley"); regionRepository.save(region); }
        region = regionRepository.findById(1015);if (region == null){ region = new Region( 1015, "PH", "03", "Central Luzon"); regionRepository.save(region); }
        region = regionRepository.findById(1016);if (region == null){ region = new Region( 1016, "PH", "04", "South Luzon"); regionRepository.save(region); }
        region = regionRepository.findById(1017);if (region == null){ region = new Region( 1017, "PH", "05", "Bicol"); regionRepository.save(region); }
        region = regionRepository.findById(1018);if (region == null){ region = new Region( 1018, "PH", "06", "West Visayas"); regionRepository.save(region); }
        region = regionRepository.findById(1019);if (region == null){ region = new Region( 1019, "PH", "07", "Central Visayas"); regionRepository.save(region); }
        region = regionRepository.findById(1020);if (region == null){ region = new Region( 1020, "PH", "08", "Eastern Visayas"); regionRepository.save(region); }
        region = regionRepository.findById(1021);if (region == null){ region = new Region( 1021, "PH", "09", "Western Mindanao"); regionRepository.save(region); }
        region = regionRepository.findById(1022);if (region == null){ region = new Region( 1022, "PH", "10", "Northern Mindanao"); regionRepository.save(region); }
        region = regionRepository.findById(1023);if (region == null){ region = new Region( 1023, "PH", "11", "Central Mindanao"); regionRepository.save(region); }
        region = regionRepository.findById(1024);if (region == null){ region = new Region( 1024, "PH", "12", "South Mindanao"); regionRepository.save(region); }
        region = regionRepository.findById(1025);if (region == null){ region = new Region( 1025, "PL", "DSL", "Dolnoslaskie"); regionRepository.save(region); }
        region = regionRepository.findById(1026);if (region == null){ region = new Region( 1026, "PL", "K-P", "Kujawsko-Pomorskie"); regionRepository.save(region); }
        region = regionRepository.findById(1027);if (region == null){ region = new Region( 1027, "PL", "LBL", "Lubelskie"); regionRepository.save(region); }
        region = regionRepository.findById(1028);if (region == null){ region = new Region( 1028, "PL", "LBS", "Lubuskie"); regionRepository.save(region); }
        region = regionRepository.findById(1029);if (region == null){ region = new Region( 1029, "PL", "LDZ", "Lodzkie"); regionRepository.save(region); }
        region = regionRepository.findById(1030);if (region == null){ region = new Region( 1030, "PL", "MAL", "Malopolskie"); regionRepository.save(region); }
        region = regionRepository.findById(1031);if (region == null){ region = new Region( 1031, "PL", "MAZ", "Mazowieckie"); regionRepository.save(region); }
        region = regionRepository.findById(1032);if (region == null){ region = new Region( 1032, "PL", "OPO", "Opolskie"); regionRepository.save(region); }
        region = regionRepository.findById(1033);if (region == null){ region = new Region( 1033, "PL", "PDK", "Podkarpackie"); regionRepository.save(region); }
        region = regionRepository.findById(1034);if (region == null){ region = new Region( 1034, "PL", "PDL", "Podlaskie"); regionRepository.save(region); }
        region = regionRepository.findById(1035);if (region == null){ region = new Region( 1035, "PL", "POM", "Pomorskie"); regionRepository.save(region); }
        region = regionRepository.findById(1036);if (region == null){ region = new Region( 1036, "PL", "SLS", "Slaskie"); regionRepository.save(region); }
        region = regionRepository.findById(1037);if (region == null){ region = new Region( 1037, "PL", "SWK", "Swietokrzyskie"); regionRepository.save(region); }
        region = regionRepository.findById(1038);if (region == null){ region = new Region( 1038, "PL", "W-M", "Warminsko-mazurskie"); regionRepository.save(region); }
        region = regionRepository.findById(1039);if (region == null){ region = new Region( 1039, "PL", "WLK", "Wielkopolskie"); regionRepository.save(region); }
        region = regionRepository.findById(1040);if (region == null){ region = new Region( 1040, "PL", "Z-P", "Zachodnio-Pomorskie"); regionRepository.save(region); }
        region = regionRepository.findById(1041);if (region == null){ region = new Region( 1041, "PT", "10", "Minho-Lima"); regionRepository.save(region); }
        region = regionRepository.findById(1042);if (region == null){ region = new Region( 1042, "PT", "11", "Cavado"); regionRepository.save(region); }
        region = regionRepository.findById(1043);if (region == null){ region = new Region( 1043, "PT", "12", "Ave"); regionRepository.save(region); }
        region = regionRepository.findById(1044);if (region == null){ region = new Region( 1044, "PT", "13", "Grande Porto"); regionRepository.save(region); }
        region = regionRepository.findById(1045);if (region == null){ region = new Region( 1045, "PT", "14", "Tamega"); regionRepository.save(region); }
        region = regionRepository.findById(1046);if (region == null){ region = new Region( 1046, "PT", "15", "Entre Douro e Vouga"); regionRepository.save(region); }
        region = regionRepository.findById(1047);if (region == null){ region = new Region( 1047, "PT", "16", "Douro"); regionRepository.save(region); }
        region = regionRepository.findById(1048);if (region == null){ region = new Region( 1048, "PT", "17", "Alto Tras-os-Montes"); regionRepository.save(region); }
        region = regionRepository.findById(1049);if (region == null){ region = new Region( 1049, "PT", "20", "Baixo Vouga"); regionRepository.save(region); }
        region = regionRepository.findById(1050);if (region == null){ region = new Region( 1050, "PT", "21", "Baixo Mondego"); regionRepository.save(region); }
        region = regionRepository.findById(1051);if (region == null){ region = new Region( 1051, "PT", "22", "Pinhal Litoral"); regionRepository.save(region); }
        region = regionRepository.findById(1052);if (region == null){ region = new Region( 1052, "PT", "23", "Pinhal Interior N."); regionRepository.save(region); }
        region = regionRepository.findById(1053);if (region == null){ region = new Region( 1053, "PT", "24", "Pinhal Interior Sul"); regionRepository.save(region); }
        region = regionRepository.findById(1054);if (region == null){ region = new Region( 1054, "PT", "25", "Dao-Lafoes"); regionRepository.save(region); }
        region = regionRepository.findById(1055);if (region == null){ region = new Region( 1055, "PT", "26", "Serra da Estrela"); regionRepository.save(region); }
        region = regionRepository.findById(1056);if (region == null){ region = new Region( 1056, "PT", "27", "Beira Interior Norte"); regionRepository.save(region); }
        region = regionRepository.findById(1057);if (region == null){ region = new Region( 1057, "PT", "28", "Beira Interior Sul"); regionRepository.save(region); }
        region = regionRepository.findById(1058);if (region == null){ region = new Region( 1058, "PT", "29", "Cova da Beira"); regionRepository.save(region); }
        region = regionRepository.findById(1059);if (region == null){ region = new Region( 1059, "PT", "30", "Oeste"); regionRepository.save(region); }
        region = regionRepository.findById(1060);if (region == null){ region = new Region( 1060, "PT", "31", "Grande Lisboa"); regionRepository.save(region); }
        region = regionRepository.findById(1061);if (region == null){ region = new Region( 1061, "PT", "32", "Peninsula de Setubal"); regionRepository.save(region); }
        region = regionRepository.findById(1062);if (region == null){ region = new Region( 1062, "PT", "33", "Medio Tejo"); regionRepository.save(region); }
        region = regionRepository.findById(1063);if (region == null){ region = new Region( 1063, "PT", "34", "Leziria do Tejo"); regionRepository.save(region); }
        region = regionRepository.findById(1064);if (region == null){ region = new Region( 1064, "PT", "40", "Alentejo Litoral"); regionRepository.save(region); }
        region = regionRepository.findById(1065);if (region == null){ region = new Region( 1065, "PT", "41", "Alto Alentejo"); regionRepository.save(region); }
        region = regionRepository.findById(1066);if (region == null){ region = new Region( 1066, "PT", "42", "Alentejo Central"); regionRepository.save(region); }
        region = regionRepository.findById(1067);if (region == null){ region = new Region( 1067, "PT", "43", "Baixo Alentejo"); regionRepository.save(region); }
        region = regionRepository.findById(1068);if (region == null){ region = new Region( 1068, "PT", "50", "Algarve"); regionRepository.save(region); }
        region = regionRepository.findById(1069);if (region == null){ region = new Region( 1069, "PT", "60", "Reg. Aut. dos Açores"); regionRepository.save(region); }
        region = regionRepository.findById(1070);if (region == null){ region = new Region( 1070, "PT", "70", "Reg. Aut. da Madeira"); regionRepository.save(region); }
        region = regionRepository.findById(1071);if (region == null){ region = new Region( 1071, "RO", "01", "Alba"); regionRepository.save(region); }
        region = regionRepository.findById(1072);if (region == null){ region = new Region( 1072, "RO", "02", "Arad"); regionRepository.save(region); }
        region = regionRepository.findById(1073);if (region == null){ region = new Region( 1073, "RO", "03", "Arges"); regionRepository.save(region); }
        region = regionRepository.findById(1074);if (region == null){ region = new Region( 1074, "RO", "04", "Bacau"); regionRepository.save(region); }
        region = regionRepository.findById(1075);if (region == null){ region = new Region( 1075, "RO", "05", "Bihor"); regionRepository.save(region); }
        region = regionRepository.findById(1076);if (region == null){ region = new Region( 1076, "RO", "06", "Bistrita-Nasaud"); regionRepository.save(region); }
        region = regionRepository.findById(1077);if (region == null){ region = new Region( 1077, "RO", "07", "Botosani"); regionRepository.save(region); }
        region = regionRepository.findById(1078);if (region == null){ region = new Region( 1078, "RO", "08", "Braila"); regionRepository.save(region); }
        region = regionRepository.findById(1079);if (region == null){ region = new Region( 1079, "RO", "09", "Brasov"); regionRepository.save(region); }
        region = regionRepository.findById(1080);if (region == null){ region = new Region( 1080, "RO", "10", "Bucuresti"); regionRepository.save(region); }
        region = regionRepository.findById(1081);if (region == null){ region = new Region( 1081, "RO", "11", "Buzau"); regionRepository.save(region); }
        region = regionRepository.findById(1082);if (region == null){ region = new Region( 1082, "RO", "12", "Calarasi"); regionRepository.save(region); }
        region = regionRepository.findById(1083);if (region == null){ region = new Region( 1083, "RO", "13", "Caras-Severin"); regionRepository.save(region); }
        region = regionRepository.findById(1084);if (region == null){ region = new Region( 1084, "RO", "14", "Cluj"); regionRepository.save(region); }
        region = regionRepository.findById(1085);if (region == null){ region = new Region( 1085, "RO", "15", "Constanta"); regionRepository.save(region); }
        region = regionRepository.findById(1086);if (region == null){ region = new Region( 1086, "RO", "16", "Covasna"); regionRepository.save(region); }
        region = regionRepository.findById(1087);if (region == null){ region = new Region( 1087, "RO", "17", "Dimbovita"); regionRepository.save(region); }
        region = regionRepository.findById(1088);if (region == null){ region = new Region( 1088, "RO", "18", "Dolj"); regionRepository.save(region); }
        region = regionRepository.findById(1089);if (region == null){ region = new Region( 1089, "RO", "19", "Galati"); regionRepository.save(region); }
        region = regionRepository.findById(1090);if (region == null){ region = new Region( 1090, "RO", "20", "Gorj"); regionRepository.save(region); }
        region = regionRepository.findById(1091);if (region == null){ region = new Region( 1091, "RO", "21", "Giurgiu"); regionRepository.save(region); }
        region = regionRepository.findById(1092);if (region == null){ region = new Region( 1092, "RO", "22", "Harghita"); regionRepository.save(region); }
        region = regionRepository.findById(1093);if (region == null){ region = new Region( 1093, "RO", "23", "Hunedoara"); regionRepository.save(region); }
        region = regionRepository.findById(1094);if (region == null){ region = new Region( 1094, "RO", "24", "Ialomita"); regionRepository.save(region); }
        region = regionRepository.findById(1095);if (region == null){ region = new Region( 1095, "RO", "25", "Iasi"); regionRepository.save(region); }
        region = regionRepository.findById(1096);if (region == null){ region = new Region( 1096, "RO", "26", "Maramures"); regionRepository.save(region); }
        region = regionRepository.findById(1097);if (region == null){ region = new Region( 1097, "RO", "27", "Mehedinti"); regionRepository.save(region); }
        region = regionRepository.findById(1098);if (region == null){ region = new Region( 1098, "RO", "28", "Mures"); regionRepository.save(region); }
        region = regionRepository.findById(1099);if (region == null){ region = new Region( 1099, "RO", "29", "Neamt"); regionRepository.save(region); }
        region = regionRepository.findById(1100);if (region == null){ region = new Region( 1100, "RO", "30", "Olt"); regionRepository.save(region); }
        region = regionRepository.findById(1101);if (region == null){ region = new Region( 1101, "RO", "31", "Prahova"); regionRepository.save(region); }
        region = regionRepository.findById(1102);if (region == null){ region = new Region( 1102, "RO", "32", "Salaj"); regionRepository.save(region); }
        region = regionRepository.findById(1103);if (region == null){ region = new Region( 1103, "RO", "33", "Satu Mare"); regionRepository.save(region); }
        region = regionRepository.findById(1104);if (region == null){ region = new Region( 1104, "RO", "34", "Sibiu"); regionRepository.save(region); }
        region = regionRepository.findById(1105);if (region == null){ region = new Region( 1105, "RO", "35", "Suceava"); regionRepository.save(region); }
        region = regionRepository.findById(1106);if (region == null){ region = new Region( 1106, "RO", "36", "Teleorman"); regionRepository.save(region); }
        region = regionRepository.findById(1107);if (region == null){ region = new Region( 1107, "RO", "37", "Timis"); regionRepository.save(region); }
        region = regionRepository.findById(1108);if (region == null){ region = new Region( 1108, "RO", "38", "Tulcea"); regionRepository.save(region); }
        region = regionRepository.findById(1109);if (region == null){ region = new Region( 1109, "RO", "39", "Vaslui"); regionRepository.save(region); }
        region = regionRepository.findById(1110);if (region == null){ region = new Region( 1110, "RO", "40", "Vilcea"); regionRepository.save(region); }
        region = regionRepository.findById(1111);if (region == null){ region = new Region( 1111, "RO", "41", "Vrancea"); regionRepository.save(region); }
        region = regionRepository.findById(1112);if (region == null){ region = new Region( 1112, "RU", "01", "Adigeja Republic"); regionRepository.save(region); }
        region = regionRepository.findById(1113);if (region == null){ region = new Region( 1113, "RU", "02", "Highlands-Altay Rep."); regionRepository.save(region); }
        region = regionRepository.findById(1114);if (region == null){ region = new Region( 1114, "RU", "03", "Republ.of Bashkortos"); regionRepository.save(region); }
        region = regionRepository.findById(1115);if (region == null){ region = new Region( 1115, "RU", "04", "Buryat Republic"); regionRepository.save(region); }
        region = regionRepository.findById(1116);if (region == null){ region = new Region( 1116, "RU", "05", "Dagestan Republic"); regionRepository.save(region); }
        region = regionRepository.findById(1117);if (region == null){ region = new Region( 1117, "RU", "06", "Ingushetija Republic"); regionRepository.save(region); }
        region = regionRepository.findById(1118);if (region == null){ region = new Region( 1118, "RU", "07", "Kabardino-Balkar.Rep"); regionRepository.save(region); }
        region = regionRepository.findById(1119);if (region == null){ region = new Region( 1119, "RU", "08", "Kalmyk Republic"); regionRepository.save(region); }
        region = regionRepository.findById(1120);if (region == null){ region = new Region( 1120, "RU", "09", "Karach.-Cherkessk Re"); regionRepository.save(region); }
        region = regionRepository.findById(1121);if (region == null){ region = new Region( 1121, "RU", "10", "Karelian Republic"); regionRepository.save(region); }
        region = regionRepository.findById(1122);if (region == null){ region = new Region( 1122, "RU", "11", "Komi Republic"); regionRepository.save(region); }
        region = regionRepository.findById(1123);if (region == null){ region = new Region( 1123, "RU", "12", "Marijskaya Republic"); regionRepository.save(region); }
        region = regionRepository.findById(1124);if (region == null){ region = new Region( 1124, "RU", "13", "Mordovian Republic"); regionRepository.save(region); }
        region = regionRepository.findById(1125);if (region == null){ region = new Region( 1125, "RU", "14", "Yakutiya-Saha Rrepub"); regionRepository.save(region); }
        region = regionRepository.findById(1126);if (region == null){ region = new Region( 1126, "RU", "15", "North-Osetiya Republ"); regionRepository.save(region); }
        region = regionRepository.findById(1127);if (region == null){ region = new Region( 1127, "RU", "16", "Tatarstan Republic"); regionRepository.save(region); }
        region = regionRepository.findById(1128);if (region == null){ region = new Region( 1128, "RU", "17", "Tuva Republic"); regionRepository.save(region); }
        region = regionRepository.findById(1129);if (region == null){ region = new Region( 1129, "RU", "18", "The Udmurt Republic"); regionRepository.save(region); }
        region = regionRepository.findById(1130);if (region == null){ region = new Region( 1130, "RU", "19", "Chakassky Republic"); regionRepository.save(region); }
        region = regionRepository.findById(1131);if (region == null){ region = new Region( 1131, "RU", "20", "Chechenskaya Republ."); regionRepository.save(region); }
        region = regionRepository.findById(1132);if (region == null){ region = new Region( 1132, "RU", "21", "Chuvash Republic"); regionRepository.save(region); }
        region = regionRepository.findById(1133);if (region == null){ region = new Region( 1133, "RU", "22", "Altay Territory"); regionRepository.save(region); }
        region = regionRepository.findById(1134);if (region == null){ region = new Region( 1134, "RU", "23", "Krasnodar Territory"); regionRepository.save(region); }
        region = regionRepository.findById(1135);if (region == null){ region = new Region( 1135, "RU", "24", "Krasnoyarsk Territor"); regionRepository.save(region); }
        region = regionRepository.findById(1136);if (region == null){ region = new Region( 1136, "RU", "25", "Primorye Territory"); regionRepository.save(region); }
        region = regionRepository.findById(1137);if (region == null){ region = new Region( 1137, "RU", "26", "Stavropol Territory"); regionRepository.save(region); }
        region = regionRepository.findById(1138);if (region == null){ region = new Region( 1138, "RU", "27", "Khabarovsk Territory"); regionRepository.save(region); }
        region = regionRepository.findById(1139);if (region == null){ region = new Region( 1139, "RU", "28", "The Amur Area"); regionRepository.save(region); }
        region = regionRepository.findById(1140);if (region == null){ region = new Region( 1140, "RU", "29", "The Arkhangelsk Area"); regionRepository.save(region); }
        region = regionRepository.findById(1141);if (region == null){ region = new Region( 1141, "RU", "30", "The Astrakhan Area"); regionRepository.save(region); }
        region = regionRepository.findById(1142);if (region == null){ region = new Region( 1142, "RU", "31", "The Belgorod Area"); regionRepository.save(region); }
        region = regionRepository.findById(1143);if (region == null){ region = new Region( 1143, "RU", "32", "The Bryansk Area"); regionRepository.save(region); }
        region = regionRepository.findById(1144);if (region == null){ region = new Region( 1144, "RU", "33", "The Vladimir Area"); regionRepository.save(region); }
        region = regionRepository.findById(1145);if (region == null){ region = new Region( 1145, "RU", "34", "The Volgograd Area"); regionRepository.save(region); }
        region = regionRepository.findById(1146);if (region == null){ region = new Region( 1146, "RU", "35", "The Vologda Area"); regionRepository.save(region); }
        region = regionRepository.findById(1147);if (region == null){ region = new Region( 1147, "RU", "36", "The Voronezh Area"); regionRepository.save(region); }
        region = regionRepository.findById(1148);if (region == null){ region = new Region( 1148, "RU", "37", "The Ivanovo Area"); regionRepository.save(region); }
        region = regionRepository.findById(1149);if (region == null){ region = new Region( 1149, "RU", "38", "The Irkutsk Area"); regionRepository.save(region); }
        region = regionRepository.findById(1150);if (region == null){ region = new Region( 1150, "RU", "39", "The Kaliningrad Area"); regionRepository.save(region); }
        region = regionRepository.findById(1151);if (region == null){ region = new Region( 1151, "RU", "40", "The Kaluga Area"); regionRepository.save(region); }
        region = regionRepository.findById(1152);if (region == null){ region = new Region( 1152, "RU", "41", "The Kamchatka Area"); regionRepository.save(region); }
        region = regionRepository.findById(1153);if (region == null){ region = new Region( 1153, "RU", "42", "The Kemerovo Area"); regionRepository.save(region); }
        region = regionRepository.findById(1154);if (region == null){ region = new Region( 1154, "RU", "43", "The Kirov Area"); regionRepository.save(region); }
        region = regionRepository.findById(1155);if (region == null){ region = new Region( 1155, "RU", "44", "The Kostroma Area"); regionRepository.save(region); }
        region = regionRepository.findById(1156);if (region == null){ region = new Region( 1156, "RU", "45", "The Kurgan Area"); regionRepository.save(region); }
        region = regionRepository.findById(1157);if (region == null){ region = new Region( 1157, "RU", "46", "The Kursk Area"); regionRepository.save(region); }
        region = regionRepository.findById(1158);if (region == null){ region = new Region( 1158, "RU", "47", "The Leningrad Area"); regionRepository.save(region); }
        region = regionRepository.findById(1159);if (region == null){ region = new Region( 1159, "RU", "48", "The Lipetsk Area"); regionRepository.save(region); }
        region = regionRepository.findById(1160);if (region == null){ region = new Region( 1160, "RU", "49", "The Magadan Area"); regionRepository.save(region); }
        region = regionRepository.findById(1161);if (region == null){ region = new Region( 1161, "RU", "50", "The Moscow Area"); regionRepository.save(region); }
        region = regionRepository.findById(1162);if (region == null){ region = new Region( 1162, "RU", "51", "The Murmansk Area"); regionRepository.save(region); }
        region = regionRepository.findById(1163);if (region == null){ region = new Region( 1163, "RU", "52", "The Nizhniy Novgorod"); regionRepository.save(region); }
        region = regionRepository.findById(1164);if (region == null){ region = new Region( 1164, "RU", "53", "The Novgorod Area"); regionRepository.save(region); }
        region = regionRepository.findById(1165);if (region == null){ region = new Region( 1165, "RU", "54", "The Novosibirsk Area"); regionRepository.save(region); }
        region = regionRepository.findById(1166);if (region == null){ region = new Region( 1166, "RU", "55", "The Omsk Area"); regionRepository.save(region); }
        region = regionRepository.findById(1167);if (region == null){ region = new Region( 1167, "RU", "56", "The Orenburg Area"); regionRepository.save(region); }
        region = regionRepository.findById(1168);if (region == null){ region = new Region( 1168, "RU", "57", "The Oryol Area"); regionRepository.save(region); }
        region = regionRepository.findById(1169);if (region == null){ region = new Region( 1169, "RU", "58", "The Penza Area"); regionRepository.save(region); }
        region = regionRepository.findById(1170);if (region == null){ region = new Region( 1170, "RU", "59", "The Perm Area"); regionRepository.save(region); }
        region = regionRepository.findById(1171);if (region == null){ region = new Region( 1171, "RU", "60", "The Pskov Area"); regionRepository.save(region); }
        region = regionRepository.findById(1172);if (region == null){ region = new Region( 1172, "RU", "61", "The Rostov Area"); regionRepository.save(region); }
        region = regionRepository.findById(1173);if (region == null){ region = new Region( 1173, "RU", "62", "The Ryazan Area"); regionRepository.save(region); }
        region = regionRepository.findById(1174);if (region == null){ region = new Region( 1174, "RU", "63", "The Samara Area"); regionRepository.save(region); }
        region = regionRepository.findById(1175);if (region == null){ region = new Region( 1175, "RU", "64", "The Saratov Area"); regionRepository.save(region); }
        region = regionRepository.findById(1176);if (region == null){ region = new Region( 1176, "RU", "65", "The Sakhalin Area"); regionRepository.save(region); }
        region = regionRepository.findById(1177);if (region == null){ region = new Region( 1177, "RU", "66", "The Sverdlovsk Area"); regionRepository.save(region); }
        region = regionRepository.findById(1178);if (region == null){ region = new Region( 1178, "RU", "67", "The Smolensk Area"); regionRepository.save(region); }
        region = regionRepository.findById(1179);if (region == null){ region = new Region( 1179, "RU", "68", "The Tambov Area"); regionRepository.save(region); }
        region = regionRepository.findById(1180);if (region == null){ region = new Region( 1180, "RU", "69", "The Tver Area"); regionRepository.save(region); }
        region = regionRepository.findById(1181);if (region == null){ region = new Region( 1181, "RU", "70", "The Tomsk Area"); regionRepository.save(region); }
        region = regionRepository.findById(1182);if (region == null){ region = new Region( 1182, "RU", "71", "The Tula Area"); regionRepository.save(region); }
        region = regionRepository.findById(1183);if (region == null){ region = new Region( 1183, "RU", "72", "The Tyumen Area"); regionRepository.save(region); }
        region = regionRepository.findById(1184);if (region == null){ region = new Region( 1184, "RU", "73", "The Ulyanovsk Area"); regionRepository.save(region); }
        region = regionRepository.findById(1185);if (region == null){ region = new Region( 1185, "RU", "74", "The Chelyabinsk Area"); regionRepository.save(region); }
        region = regionRepository.findById(1186);if (region == null){ region = new Region( 1186, "RU", "75", "The Chita Area"); regionRepository.save(region); }
        region = regionRepository.findById(1187);if (region == null){ region = new Region( 1187, "RU", "76", "The Yaroslavl Area"); regionRepository.save(region); }
        region = regionRepository.findById(1188);if (region == null){ region = new Region( 1188, "RU", "77", "c.Moscow"); regionRepository.save(region); }
        region = regionRepository.findById(1189);if (region == null){ region = new Region( 1189, "RU", "78", "c.St-Peterburg"); regionRepository.save(region); }
        region = regionRepository.findById(1190);if (region == null){ region = new Region( 1190, "RU", "79", "The Jewish Auton.are"); regionRepository.save(region); }
        region = regionRepository.findById(1191);if (region == null){ region = new Region( 1191, "RU", "80", "Aginsk Buryat Aut.di"); regionRepository.save(region); }
        region = regionRepository.findById(1192);if (region == null){ region = new Region( 1192, "RU", "81", "Komy Permjats.Aut.di"); regionRepository.save(region); }
        region = regionRepository.findById(1193);if (region == null){ region = new Region( 1193, "RU", "82", "Korjacs Auton.distri"); regionRepository.save(region); }
        region = regionRepository.findById(1194);if (region == null){ region = new Region( 1194, "RU", "83", "Nenekchky Auton.dist"); regionRepository.save(region); }
        region = regionRepository.findById(1195);if (region == null){ region = new Region( 1195, "RU", "84", "The Taymir Auton.dis"); regionRepository.save(region); }
        region = regionRepository.findById(1196);if (region == null){ region = new Region( 1196, "RU", "85", "Ust-Ordinsky Buryat"); regionRepository.save(region); }
        region = regionRepository.findById(1197);if (region == null){ region = new Region( 1197, "RU", "86", "Chanti-Mansyjsky Aut"); regionRepository.save(region); }
        region = regionRepository.findById(1198);if (region == null){ region = new Region( 1198, "RU", "87", "Chukotka Auton. dist"); regionRepository.save(region); }
        region = regionRepository.findById(1199);if (region == null){ region = new Region( 1199, "RU", "88", "Evensky Auton.distri"); regionRepository.save(region); }
        region = regionRepository.findById(1200);if (region == null){ region = new Region( 1200, "RU", "89", "Jamalo-Nenekchky Aut"); regionRepository.save(region); }
        region = regionRepository.findById(1201);if (region == null){ region = new Region( 1201, "SE", "001", "Blekinge County"); regionRepository.save(region); }
        region = regionRepository.findById(1202);if (region == null){ region = new Region( 1202, "SE", "002", "Dalarnas County"); regionRepository.save(region); }
        region = regionRepository.findById(1203);if (region == null){ region = new Region( 1203, "SE", "003", "Gotland County"); regionRepository.save(region); }
        region = regionRepository.findById(1204);if (region == null){ region = new Region( 1204, "SE", "004", "Gaevleborg County"); regionRepository.save(region); }
        region = regionRepository.findById(1205);if (region == null){ region = new Region( 1205, "SE", "005", "Halland County"); regionRepository.save(region); }
        region = regionRepository.findById(1206);if (region == null){ region = new Region( 1206, "SE", "006", "Jaemtland County"); regionRepository.save(region); }
        region = regionRepository.findById(1207);if (region == null){ region = new Region( 1207, "SE", "007", "Joenkoeping County"); regionRepository.save(region); }
        region = regionRepository.findById(1208);if (region == null){ region = new Region( 1208, "SE", "008", "Kalmar County"); regionRepository.save(region); }
        region = regionRepository.findById(1209);if (region == null){ region = new Region( 1209, "SE", "009", "Kronoberg County"); regionRepository.save(region); }
        region = regionRepository.findById(1210);if (region == null){ region = new Region( 1210, "SE", "010", "Norrbotten County"); regionRepository.save(region); }
        region = regionRepository.findById(1211);if (region == null){ region = new Region( 1211, "SE", "011", "Skaane County"); regionRepository.save(region); }
        region = regionRepository.findById(1212);if (region == null){ region = new Region( 1212, "SE", "012", "Stockholm County"); regionRepository.save(region); }
        region = regionRepository.findById(1213);if (region == null){ region = new Region( 1213, "SE", "013", "Soedermanland County"); regionRepository.save(region); }
        region = regionRepository.findById(1214);if (region == null){ region = new Region( 1214, "SE", "014", "Uppsala County"); regionRepository.save(region); }
        region = regionRepository.findById(1215);if (region == null){ region = new Region( 1215, "SE", "015", "Vaermland County"); regionRepository.save(region); }
        region = regionRepository.findById(1216);if (region == null){ region = new Region( 1216, "SE", "016", "Vaesterbotten County"); regionRepository.save(region); }
        region = regionRepository.findById(1217);if (region == null){ region = new Region( 1217, "SE", "017", "Vaesternorrland Cnty"); regionRepository.save(region); }
        region = regionRepository.findById(1218);if (region == null){ region = new Region( 1218, "SE", "018", "Vaestmanland County"); regionRepository.save(region); }
        region = regionRepository.findById(1219);if (region == null){ region = new Region( 1219, "SE", "019", "Vaestra Goetaland C."); regionRepository.save(region); }
        region = regionRepository.findById(1220);if (region == null){ region = new Region( 1220, "SE", "020", "Oerebro County"); regionRepository.save(region); }
        region = regionRepository.findById(1221);if (region == null){ region = new Region( 1221, "SE", "021", "Oestergoetland Cnty"); regionRepository.save(region); }
        region = regionRepository.findById(1222);if (region == null){ region = new Region( 1222, "SG", "SG", "Singapore"); regionRepository.save(region); }
        region = regionRepository.findById(1223);if (region == null){ region = new Region( 1223, "SI", "01", "Ajdovscina"); regionRepository.save(region); }
        region = regionRepository.findById(1224);if (region == null){ region = new Region( 1224, "SI", "02", "Brezice"); regionRepository.save(region); }
        region = regionRepository.findById(1225);if (region == null){ region = new Region( 1225, "SI", "03", "Celje"); regionRepository.save(region); }
        region = regionRepository.findById(1226);if (region == null){ region = new Region( 1226, "SI", "04", "Cerknica"); regionRepository.save(region); }
        region = regionRepository.findById(1227);if (region == null){ region = new Region( 1227, "SI", "05", "Crnomelj"); regionRepository.save(region); }
        region = regionRepository.findById(1228);if (region == null){ region = new Region( 1228, "SI", "06", "Dravograd"); regionRepository.save(region); }
        region = regionRepository.findById(1229);if (region == null){ region = new Region( 1229, "SI", "07", "Gornja Radgona"); regionRepository.save(region); }
        region = regionRepository.findById(1230);if (region == null){ region = new Region( 1230, "SI", "08", "Grosuplje"); regionRepository.save(region); }
        region = regionRepository.findById(1231);if (region == null){ region = new Region( 1231, "SI", "09", "Hrastnik Lasko"); regionRepository.save(region); }
        region = regionRepository.findById(1232);if (region == null){ region = new Region( 1232, "SI", "10", "Idrija"); regionRepository.save(region); }
        region = regionRepository.findById(1233);if (region == null){ region = new Region( 1233, "SI", "11", "Ilirska Bistrica"); regionRepository.save(region); }
        region = regionRepository.findById(1234);if (region == null){ region = new Region( 1234, "SI", "12", "Izola"); regionRepository.save(region); }
        region = regionRepository.findById(1235);if (region == null){ region = new Region( 1235, "SI", "13", "Jesenice"); regionRepository.save(region); }
        region = regionRepository.findById(1236);if (region == null){ region = new Region( 1236, "SI", "14", "Kamnik"); regionRepository.save(region); }
        region = regionRepository.findById(1237);if (region == null){ region = new Region( 1237, "SI", "15", "Kocevje"); regionRepository.save(region); }
        region = regionRepository.findById(1238);if (region == null){ region = new Region( 1238, "SI", "16", "Koper"); regionRepository.save(region); }
        region = regionRepository.findById(1239);if (region == null){ region = new Region( 1239, "SI", "17", "Kranj"); regionRepository.save(region); }
        region = regionRepository.findById(1240);if (region == null){ region = new Region( 1240, "SI", "18", "Krsko"); regionRepository.save(region); }
        region = regionRepository.findById(1241);if (region == null){ region = new Region( 1241, "SI", "19", "Lenart"); regionRepository.save(region); }
        region = regionRepository.findById(1242);if (region == null){ region = new Region( 1242, "SI", "20", "Lendava"); regionRepository.save(region); }
        region = regionRepository.findById(1243);if (region == null){ region = new Region( 1243, "SI", "21", "Litija"); regionRepository.save(region); }
        region = regionRepository.findById(1244);if (region == null){ region = new Region( 1244, "SI", "22", "Ljubljana-Bezigrad"); regionRepository.save(region); }
        region = regionRepository.findById(1245);if (region == null){ region = new Region( 1245, "SI", "23", "Ljubljana-Center"); regionRepository.save(region); }
        region = regionRepository.findById(1246);if (region == null){ region = new Region( 1246, "SI", "24", "Ljubljana-Moste-Polj"); regionRepository.save(region); }
        region = regionRepository.findById(1247);if (region == null){ region = new Region( 1247, "SI", "25", "Ljubljana-Siska"); regionRepository.save(region); }
        region = regionRepository.findById(1248);if (region == null){ region = new Region( 1248, "SI", "26", "Ljubljana-Vic-Rudnik"); regionRepository.save(region); }
        region = regionRepository.findById(1249);if (region == null){ region = new Region( 1249, "SI", "27", "Ljutomer"); regionRepository.save(region); }
        region = regionRepository.findById(1250);if (region == null){ region = new Region( 1250, "SI", "28", "Logatec"); regionRepository.save(region); }
        region = regionRepository.findById(1251);if (region == null){ region = new Region( 1251, "SI", "29", "Maribor"); regionRepository.save(region); }
        region = regionRepository.findById(1252);if (region == null){ region = new Region( 1252, "SI", "30", "Metlika"); regionRepository.save(region); }
        region = regionRepository.findById(1253);if (region == null){ region = new Region( 1253, "SI", "31", "Mozirje"); regionRepository.save(region); }
        region = regionRepository.findById(1254);if (region == null){ region = new Region( 1254, "SI", "32", "Murska Sobota"); regionRepository.save(region); }
        region = regionRepository.findById(1255);if (region == null){ region = new Region( 1255, "SI", "33", "Nova Gorica"); regionRepository.save(region); }
        region = regionRepository.findById(1256);if (region == null){ region = new Region( 1256, "SI", "34", "Novo Mesto"); regionRepository.save(region); }
        region = regionRepository.findById(1257);if (region == null){ region = new Region( 1257, "SI", "35", "Ormoz"); regionRepository.save(region); }
        region = regionRepository.findById(1258);if (region == null){ region = new Region( 1258, "SI", "36", "Pesnica"); regionRepository.save(region); }
        region = regionRepository.findById(1259);if (region == null){ region = new Region( 1259, "SI", "37", "Piran"); regionRepository.save(region); }
        region = regionRepository.findById(1260);if (region == null){ region = new Region( 1260, "SI", "38", "Postojna"); regionRepository.save(region); }
        region = regionRepository.findById(1261);if (region == null){ region = new Region( 1261, "SI", "39", "Ptuj"); regionRepository.save(region); }
        region = regionRepository.findById(1262);if (region == null){ region = new Region( 1262, "SI", "40", "Radlje Ob Dravi"); regionRepository.save(region); }
        region = regionRepository.findById(1263);if (region == null){ region = new Region( 1263, "SI", "41", "Radovljica"); regionRepository.save(region); }
        region = regionRepository.findById(1264);if (region == null){ region = new Region( 1264, "SI", "42", "Ravne Na Koroskem"); regionRepository.save(region); }
        region = regionRepository.findById(1265);if (region == null){ region = new Region( 1265, "SI", "43", "Ribnica"); regionRepository.save(region); }
        region = regionRepository.findById(1266);if (region == null){ region = new Region( 1266, "SI", "44", "Ruse"); regionRepository.save(region); }
        region = regionRepository.findById(1267);if (region == null){ region = new Region( 1267, "SI", "45", "Sentjur Pri Celju"); regionRepository.save(region); }
        region = regionRepository.findById(1268);if (region == null){ region = new Region( 1268, "SI", "46", "Sevnica"); regionRepository.save(region); }
        region = regionRepository.findById(1269);if (region == null){ region = new Region( 1269, "SI", "47", "Sezana"); regionRepository.save(region); }
        region = regionRepository.findById(1270);if (region == null){ region = new Region( 1270, "SI", "48", "Skofja Loka"); regionRepository.save(region); }
        region = regionRepository.findById(1271);if (region == null){ region = new Region( 1271, "SI", "49", "Slovenj Gradec"); regionRepository.save(region); }
        region = regionRepository.findById(1272);if (region == null){ region = new Region( 1272, "SI", "50", "Slovenska Bistrica"); regionRepository.save(region); }
        region = regionRepository.findById(1273);if (region == null){ region = new Region( 1273, "SI", "51", "Slovenske Konjice"); regionRepository.save(region); }
        region = regionRepository.findById(1274);if (region == null){ region = new Region( 1274, "SI", "52", "Smarje Pri Jelsah"); regionRepository.save(region); }
        region = regionRepository.findById(1275);if (region == null){ region = new Region( 1275, "SI", "53", "Tolmin"); regionRepository.save(region); }
        region = regionRepository.findById(1276);if (region == null){ region = new Region( 1276, "SI", "54", "Trbovlje"); regionRepository.save(region); }
        region = regionRepository.findById(1277);if (region == null){ region = new Region( 1277, "SI", "55", "Trebnje"); regionRepository.save(region); }
        region = regionRepository.findById(1278);if (region == null){ region = new Region( 1278, "SI", "56", "Trzic"); regionRepository.save(region); }
        region = regionRepository.findById(1279);if (region == null){ region = new Region( 1279, "SI", "57", "Velenje"); regionRepository.save(region); }
        region = regionRepository.findById(1280);if (region == null){ region = new Region( 1280, "SI", "58", "Vrhnika"); regionRepository.save(region); }
        region = regionRepository.findById(1281);if (region == null){ region = new Region( 1281, "SI", "59", "Zagorje Ob Savi"); regionRepository.save(region); }
        region = regionRepository.findById(1282);if (region == null){ region = new Region( 1282, "SI", "60", "Zalec"); regionRepository.save(region); }
        region = regionRepository.findById(1283);if (region == null){ region = new Region( 1283, "SK", "01", "Bratislava"); regionRepository.save(region); }
        region = regionRepository.findById(1284);if (region == null){ region = new Region( 1284, "SK", "02", "Zapadoslovensky"); regionRepository.save(region); }
        region = regionRepository.findById(1285);if (region == null){ region = new Region( 1285, "SK", "03", "Stredoslovensky"); regionRepository.save(region); }
        region = regionRepository.findById(1286);if (region == null){ region = new Region( 1286, "SK", "04", "Vychodoslovensky"); regionRepository.save(region); }
        region = regionRepository.findById(1287);if (region == null){ region = new Region( 1287, "TH", "01", "Amnat Charoen"); regionRepository.save(region); }
        region = regionRepository.findById(1288);if (region == null){ region = new Region( 1288, "TH", "02", "Ang Thong"); regionRepository.save(region); }
        region = regionRepository.findById(1289);if (region == null){ region = new Region( 1289, "TH", "03", "Buriram"); regionRepository.save(region); }
        region = regionRepository.findById(1290);if (region == null){ region = new Region( 1290, "TH", "04", "Chachoengsao"); regionRepository.save(region); }
        region = regionRepository.findById(1291);if (region == null){ region = new Region( 1291, "TH", "05", "Chai Nat"); regionRepository.save(region); }
        region = regionRepository.findById(1292);if (region == null){ region = new Region( 1292, "TH", "06", "Chaiyaphum"); regionRepository.save(region); }
        region = regionRepository.findById(1293);if (region == null){ region = new Region( 1293, "TH", "07", "Chanthaburi"); regionRepository.save(region); }
        region = regionRepository.findById(1294);if (region == null){ region = new Region( 1294, "TH", "08", "Chiang Mai"); regionRepository.save(region); }
        region = regionRepository.findById(1295);if (region == null){ region = new Region( 1295, "TH", "09", "Chiang Rai"); regionRepository.save(region); }
        region = regionRepository.findById(1296);if (region == null){ region = new Region( 1296, "TH", "10", "Chon Buri"); regionRepository.save(region); }
        region = regionRepository.findById(1297);if (region == null){ region = new Region( 1297, "TH", "11", "Chumphon"); regionRepository.save(region); }
        region = regionRepository.findById(1298);if (region == null){ region = new Region( 1298, "TH", "12", "Kalasin"); regionRepository.save(region); }
        region = regionRepository.findById(1299);if (region == null){ region = new Region( 1299, "TH", "13", "Kamphaeng Phet"); regionRepository.save(region); }
        region = regionRepository.findById(1300);if (region == null){ region = new Region( 1300, "TH", "14", "Kanchanaburi"); regionRepository.save(region); }
        region = regionRepository.findById(1301);if (region == null){ region = new Region( 1301, "TH", "15", "Khon Kaen"); regionRepository.save(region); }
        region = regionRepository.findById(1302);if (region == null){ region = new Region( 1302, "TH", "16", "Krabi"); regionRepository.save(region); }
        region = regionRepository.findById(1303);if (region == null){ region = new Region( 1303, "TH", "17", "Krung Thep"); regionRepository.save(region); }
        region = regionRepository.findById(1304);if (region == null){ region = new Region( 1304, "TH", "18", "Mahanakhon"); regionRepository.save(region); }
        region = regionRepository.findById(1305);if (region == null){ region = new Region( 1305, "TH", "19", "Lampang"); regionRepository.save(region); }
        region = regionRepository.findById(1306);if (region == null){ region = new Region( 1306, "TH", "20", "Lamphun"); regionRepository.save(region); }
        region = regionRepository.findById(1307);if (region == null){ region = new Region( 1307, "TH", "21", "Loei"); regionRepository.save(region); }
        region = regionRepository.findById(1308);if (region == null){ region = new Region( 1308, "TH", "22", "Lop Buri"); regionRepository.save(region); }
        region = regionRepository.findById(1309);if (region == null){ region = new Region( 1309, "TH", "23", "Mae Hong Son"); regionRepository.save(region); }
        region = regionRepository.findById(1310);if (region == null){ region = new Region( 1310, "TH", "24", "Maha Sarakham"); regionRepository.save(region); }
        region = regionRepository.findById(1311);if (region == null){ region = new Region( 1311, "TH", "25", "Mukdahan"); regionRepository.save(region); }
        region = regionRepository.findById(1312);if (region == null){ region = new Region( 1312, "TH", "26", "Nakhon Nayok"); regionRepository.save(region); }
        region = regionRepository.findById(1313);if (region == null){ region = new Region( 1313, "TH", "27", "Nakhon Pathom"); regionRepository.save(region); }
        region = regionRepository.findById(1314);if (region == null){ region = new Region( 1314, "TH", "28", "Nakhon Phanom"); regionRepository.save(region); }
        region = regionRepository.findById(1315);if (region == null){ region = new Region( 1315, "TH", "29", "Nakhon Ratchasima"); regionRepository.save(region); }
        region = regionRepository.findById(1316);if (region == null){ region = new Region( 1316, "TH", "30", "Nakhon Sawan"); regionRepository.save(region); }
        region = regionRepository.findById(1317);if (region == null){ region = new Region( 1317, "TH", "31", "Nakhon Si Thammarat"); regionRepository.save(region); }
        region = regionRepository.findById(1318);if (region == null){ region = new Region( 1318, "TH", "32", "Nan"); regionRepository.save(region); }
        region = regionRepository.findById(1319);if (region == null){ region = new Region( 1319, "TH", "33", "Narathiwat"); regionRepository.save(region); }
        region = regionRepository.findById(1320);if (region == null){ region = new Region( 1320, "TH", "34", "Nong Bua Lamphu"); regionRepository.save(region); }
        region = regionRepository.findById(1321);if (region == null){ region = new Region( 1321, "TH", "35", "Nong Khai"); regionRepository.save(region); }
        region = regionRepository.findById(1322);if (region == null){ region = new Region( 1322, "TH", "36", "Nonthaburi"); regionRepository.save(region); }
        region = regionRepository.findById(1323);if (region == null){ region = new Region( 1323, "TH", "37", "Pathum Thani"); regionRepository.save(region); }
        region = regionRepository.findById(1324);if (region == null){ region = new Region( 1324, "TH", "38", "Pattani"); regionRepository.save(region); }
        region = regionRepository.findById(1325);if (region == null){ region = new Region( 1325, "TH", "39", "Phangnga"); regionRepository.save(region); }
        region = regionRepository.findById(1326);if (region == null){ region = new Region( 1326, "TH", "40", "Phatthalung"); regionRepository.save(region); }
        region = regionRepository.findById(1327);if (region == null){ region = new Region( 1327, "TH", "41", "Phayao"); regionRepository.save(region); }
        region = regionRepository.findById(1328);if (region == null){ region = new Region( 1328, "TH", "42", "Phetchabun"); regionRepository.save(region); }
        region = regionRepository.findById(1329);if (region == null){ region = new Region( 1329, "TH", "43", "Phetchaburi"); regionRepository.save(region); }
        region = regionRepository.findById(1330);if (region == null){ region = new Region( 1330, "TH", "44", "Phichit"); regionRepository.save(region); }
        region = regionRepository.findById(1331);if (region == null){ region = new Region( 1331, "TH", "45", "Phitsanulok"); regionRepository.save(region); }
        region = regionRepository.findById(1332);if (region == null){ region = new Region( 1332, "TH", "46", "Phra Nakhon Si Ayut."); regionRepository.save(region); }
        region = regionRepository.findById(1333);if (region == null){ region = new Region( 1333, "TH", "47", "Phrae"); regionRepository.save(region); }
        region = regionRepository.findById(1334);if (region == null){ region = new Region( 1334, "TH", "48", "Phuket"); regionRepository.save(region); }
        region = regionRepository.findById(1335);if (region == null){ region = new Region( 1335, "TH", "49", "Prachin Buri"); regionRepository.save(region); }
        region = regionRepository.findById(1336);if (region == null){ region = new Region( 1336, "TR", "01", "Adana"); regionRepository.save(region); }
        region = regionRepository.findById(1337);if (region == null){ region = new Region( 1337, "TR", "02", "Adiyaman"); regionRepository.save(region); }
        region = regionRepository.findById(1338);if (region == null){ region = new Region( 1338, "TR", "03", "Afyon"); regionRepository.save(region); }
        region = regionRepository.findById(1339);if (region == null){ region = new Region( 1339, "TR", "04", "Agri"); regionRepository.save(region); }
        region = regionRepository.findById(1340);if (region == null){ region = new Region( 1340, "TR", "05", "Amasya"); regionRepository.save(region); }
        region = regionRepository.findById(1341);if (region == null){ region = new Region( 1341, "TR", "06", "Ankara"); regionRepository.save(region); }
        region = regionRepository.findById(1342);if (region == null){ region = new Region( 1342, "TR", "07", "Antalya"); regionRepository.save(region); }
        region = regionRepository.findById(1343);if (region == null){ region = new Region( 1343, "TR", "08", "Artvin"); regionRepository.save(region); }
        region = regionRepository.findById(1344);if (region == null){ region = new Region( 1344, "TR", "09", "Aydin"); regionRepository.save(region); }
        region = regionRepository.findById(1345);if (region == null){ region = new Region( 1345, "TR", "10", "Balikesir"); regionRepository.save(region); }
        region = regionRepository.findById(1346);if (region == null){ region = new Region( 1346, "TR", "11", "Bilecik"); regionRepository.save(region); }
        region = regionRepository.findById(1347);if (region == null){ region = new Region( 1347, "TR", "12", "Bingöl"); regionRepository.save(region); }
        region = regionRepository.findById(1348);if (region == null){ region = new Region( 1348, "TR", "13", "Bitlis"); regionRepository.save(region); }
        region = regionRepository.findById(1349);if (region == null){ region = new Region( 1349, "TR", "14", "Bolu"); regionRepository.save(region); }
        region = regionRepository.findById(1350);if (region == null){ region = new Region( 1350, "TR", "15", "Burdur"); regionRepository.save(region); }
        region = regionRepository.findById(1351);if (region == null){ region = new Region( 1351, "TR", "16", "Bursa"); regionRepository.save(region); }
        region = regionRepository.findById(1352);if (region == null){ region = new Region( 1352, "TR", "17", "Canakkale"); regionRepository.save(region); }
        region = regionRepository.findById(1353);if (region == null){ region = new Region( 1353, "TR", "18", "Cankiri"); regionRepository.save(region); }
        region = regionRepository.findById(1354);if (region == null){ region = new Region( 1354, "TR", "19", "Corum"); regionRepository.save(region); }
        region = regionRepository.findById(1355);if (region == null){ region = new Region( 1355, "TR", "20", "Denizli"); regionRepository.save(region); }
        region = regionRepository.findById(1356);if (region == null){ region = new Region( 1356, "TR", "21", "Diyarbakir"); regionRepository.save(region); }
        region = regionRepository.findById(1357);if (region == null){ region = new Region( 1357, "TR", "22", "Edirne"); regionRepository.save(region); }
        region = regionRepository.findById(1358);if (region == null){ region = new Region( 1358, "TR", "23", "Elazig"); regionRepository.save(region); }
        region = regionRepository.findById(1359);if (region == null){ region = new Region( 1359, "TR", "24", "Erzincan"); regionRepository.save(region); }
        region = regionRepository.findById(1360);if (region == null){ region = new Region( 1360, "TR", "25", "Erzurum"); regionRepository.save(region); }
        region = regionRepository.findById(1361);if (region == null){ region = new Region( 1361, "TR", "26", "Eskisehir"); regionRepository.save(region); }
        region = regionRepository.findById(1362);if (region == null){ region = new Region( 1362, "TR", "27", "Gaziantep"); regionRepository.save(region); }
        region = regionRepository.findById(1363);if (region == null){ region = new Region( 1363, "TR", "28", "Giresun"); regionRepository.save(region); }
        region = regionRepository.findById(1364);if (region == null){ region = new Region( 1364, "TR", "29", "Guemueshane"); regionRepository.save(region); }
        region = regionRepository.findById(1365);if (region == null){ region = new Region( 1365, "TR", "30", "Hakkari"); regionRepository.save(region); }
        region = regionRepository.findById(1366);if (region == null){ region = new Region( 1366, "TR", "31", "Hatay"); regionRepository.save(region); }
        region = regionRepository.findById(1367);if (region == null){ region = new Region( 1367, "TR", "32", "Isparta"); regionRepository.save(region); }
        region = regionRepository.findById(1368);if (region == null){ region = new Region( 1368, "TR", "33", "Icel"); regionRepository.save(region); }
        region = regionRepository.findById(1369);if (region == null){ region = new Region( 1369, "TR", "34", "Istanbul"); regionRepository.save(region); }
        region = regionRepository.findById(1370);if (region == null){ region = new Region( 1370, "TR", "35", "Izmir"); regionRepository.save(region); }
        region = regionRepository.findById(1371);if (region == null){ region = new Region( 1371, "TR", "36", "Kars"); regionRepository.save(region); }
        region = regionRepository.findById(1372);if (region == null){ region = new Region( 1372, "TR", "37", "Kastamonu"); regionRepository.save(region); }
        region = regionRepository.findById(1373);if (region == null){ region = new Region( 1373, "TR", "38", "Kayseri"); regionRepository.save(region); }
        region = regionRepository.findById(1374);if (region == null){ region = new Region( 1374, "TR", "39", "Kirklareli"); regionRepository.save(region); }
        region = regionRepository.findById(1375);if (region == null){ region = new Region( 1375, "TR", "40", "Kirshehir"); regionRepository.save(region); }
        region = regionRepository.findById(1376);if (region == null){ region = new Region( 1376, "TR", "41", "Kocaeli"); regionRepository.save(region); }
        region = regionRepository.findById(1377);if (region == null){ region = new Region( 1377, "TR", "42", "Konya"); regionRepository.save(region); }
        region = regionRepository.findById(1378);if (region == null){ region = new Region( 1378, "TR", "43", "Kuetahya"); regionRepository.save(region); }
        region = regionRepository.findById(1379);if (region == null){ region = new Region( 1379, "TR", "44", "Malatya"); regionRepository.save(region); }
        region = regionRepository.findById(1380);if (region == null){ region = new Region( 1380, "TR", "45", "Manisa"); regionRepository.save(region); }
        region = regionRepository.findById(1381);if (region == null){ region = new Region( 1381, "TR", "46", "K.Marash"); regionRepository.save(region); }
        region = regionRepository.findById(1382);if (region == null){ region = new Region( 1382, "TR", "47", "Mardin"); regionRepository.save(region); }
        region = regionRepository.findById(1383);if (region == null){ region = new Region( 1383, "TR", "48", "Mugla"); regionRepository.save(region); }
        region = regionRepository.findById(1384);if (region == null){ region = new Region( 1384, "TR", "49", "Mush"); regionRepository.save(region); }
        region = regionRepository.findById(1385);if (region == null){ region = new Region( 1385, "TR", "50", "Nevshehir"); regionRepository.save(region); }
        region = regionRepository.findById(1386);if (region == null){ region = new Region( 1386, "TR", "51", "Nigde"); regionRepository.save(region); }
        region = regionRepository.findById(1387);if (region == null){ region = new Region( 1387, "TR", "52", "Ordu"); regionRepository.save(region); }
        region = regionRepository.findById(1388);if (region == null){ region = new Region( 1388, "TR", "53", "Rize"); regionRepository.save(region); }
        region = regionRepository.findById(1389);if (region == null){ region = new Region( 1389, "TR", "54", "Sakarya"); regionRepository.save(region); }
        region = regionRepository.findById(1390);if (region == null){ region = new Region( 1390, "TR", "55", "Samsun"); regionRepository.save(region); }
        region = regionRepository.findById(1391);if (region == null){ region = new Region( 1391, "TR", "56", "Siirt"); regionRepository.save(region); }
        region = regionRepository.findById(1392);if (region == null){ region = new Region( 1392, "TR", "57", "Sinop"); regionRepository.save(region); }
        region = regionRepository.findById(1393);if (region == null){ region = new Region( 1393, "TR", "58", "Sivas"); regionRepository.save(region); }
        region = regionRepository.findById(1394);if (region == null){ region = new Region( 1394, "TR", "59", "Tekirdag"); regionRepository.save(region); }
        region = regionRepository.findById(1395);if (region == null){ region = new Region( 1395, "TR", "60", "Tokat"); regionRepository.save(region); }
        region = regionRepository.findById(1396);if (region == null){ region = new Region( 1396, "TR", "61", "Trabzon"); regionRepository.save(region); }
        region = regionRepository.findById(1397);if (region == null){ region = new Region( 1397, "TR", "62", "Tunceli"); regionRepository.save(region); }
        region = regionRepository.findById(1398);if (region == null){ region = new Region( 1398, "TR", "63", "Shanliurfa"); regionRepository.save(region); }
        region = regionRepository.findById(1399);if (region == null){ region = new Region( 1399, "TR", "64", "Ushak"); regionRepository.save(region); }
        region = regionRepository.findById(1400);if (region == null){ region = new Region( 1400, "TR", "65", "Van"); regionRepository.save(region); }
        region = regionRepository.findById(1401);if (region == null){ region = new Region( 1401, "TR", "66", "Yozgat"); regionRepository.save(region); }
        region = regionRepository.findById(1402);if (region == null){ region = new Region( 1402, "TR", "67", "Zonguldak"); regionRepository.save(region); }
        region = regionRepository.findById(1403);if (region == null){ region = new Region( 1403, "TR", "68", "Aksaray"); regionRepository.save(region); }
        region = regionRepository.findById(1404);if (region == null){ region = new Region( 1404, "TR", "69", "Bayburt"); regionRepository.save(region); }
        region = regionRepository.findById(1405);if (region == null){ region = new Region( 1405, "TR", "70", "Karaman"); regionRepository.save(region); }
        region = regionRepository.findById(1406);if (region == null){ region = new Region( 1406, "TR", "71", "Kirikkale"); regionRepository.save(region); }
        region = regionRepository.findById(1407);if (region == null){ region = new Region( 1407, "TR", "72", "Batman"); regionRepository.save(region); }
        region = regionRepository.findById(1408);if (region == null){ region = new Region( 1408, "TR", "73", "Shirnak"); regionRepository.save(region); }
        region = regionRepository.findById(1409);if (region == null){ region = new Region( 1409, "TR", "74", "Bartin"); regionRepository.save(region); }
        region = regionRepository.findById(1410);if (region == null){ region = new Region( 1410, "TR", "75", "Ardahan"); regionRepository.save(region); }
        region = regionRepository.findById(1411);if (region == null){ region = new Region( 1411, "TR", "76", "Igdir"); regionRepository.save(region); }
        region = regionRepository.findById(1412);if (region == null){ region = new Region( 1412, "TR", "77", "Yalova"); regionRepository.save(region); }
        region = regionRepository.findById(1413);if (region == null){ region = new Region( 1413, "TW", "FJN", "Fu-chien"); regionRepository.save(region); }
        region = regionRepository.findById(1414);if (region == null){ region = new Region( 1414, "TW", "KSH", "Kao-hsiung"); regionRepository.save(region); }
        region = regionRepository.findById(1415);if (region == null){ region = new Region( 1415, "TW", "TPE", "T´ai-pei"); regionRepository.save(region); }
        region = regionRepository.findById(1416);if (region == null){ region = new Region( 1416, "TW", "TWN", "Taiwan"); regionRepository.save(region); }
        region = regionRepository.findById(1417);if (region == null){ region = new Region( 1417, "UA", "CHG", "Chernigivs'ka"); regionRepository.save(region); }
        region = regionRepository.findById(1418);if (region == null){ region = new Region( 1418, "UA", "CHR", "Cherkas'ka"); regionRepository.save(region); }
        region = regionRepository.findById(1419);if (region == null){ region = new Region( 1419, "UA", "CHV", "Chernovits'ka"); regionRepository.save(region); }
        region = regionRepository.findById(1420);if (region == null){ region = new Region( 1420, "UA", "DNP", "Dnipropetrovs'ka"); regionRepository.save(region); }
        region = regionRepository.findById(1421);if (region == null){ region = new Region( 1421, "UA", "DON", "Donets'ka"); regionRepository.save(region); }
        region = regionRepository.findById(1422);if (region == null){ region = new Region( 1422, "UA", "HAR", "Harkivs'ka"); regionRepository.save(region); }
        region = regionRepository.findById(1423);if (region == null){ region = new Region( 1423, "UA", "HML", "Hmel'nits'ka"); regionRepository.save(region); }
        region = regionRepository.findById(1424);if (region == null){ region = new Region( 1424, "UA", "HRS", "Hersons'ka"); regionRepository.save(region); }
        region = regionRepository.findById(1425);if (region == null){ region = new Region( 1425, "UA", "IVF", "Ivano-Frankivs'ka"); regionRepository.save(region); }
        region = regionRepository.findById(1426);if (region == null){ region = new Region( 1426, "UA", "KIE", "Kievs'ka"); regionRepository.save(region); }
        region = regionRepository.findById(1427);if (region == null){ region = new Region( 1427, "UA", "KIR", "Kirovograds'ka"); regionRepository.save(region); }
        region = regionRepository.findById(1428);if (region == null){ region = new Region( 1428, "UA", "KRM", "Respublika Krim"); regionRepository.save(region); }
        region = regionRepository.findById(1429);if (region == null){ region = new Region( 1429, "UA", "L'V", "L'vivsbka"); regionRepository.save(region); }
        region = regionRepository.findById(1430);if (region == null){ region = new Region( 1430, "UA", "LUG", "Lugans'ka"); regionRepository.save(region); }
        region = regionRepository.findById(1431);if (region == null){ region = new Region( 1431, "UA", "MIK", "Mikolaivs'ka"); regionRepository.save(region); }
        region = regionRepository.findById(1432);if (region == null){ region = new Region( 1432, "UA", "M_K", "m.Kiev"); regionRepository.save(region); }
        region = regionRepository.findById(1433);if (region == null){ region = new Region( 1433, "UA", "M_S", "m.Sevastopil'"); regionRepository.save(region); }
        region = regionRepository.findById(1434);if (region == null){ region = new Region( 1434, "UA", "ODS", "Odes'ka"); regionRepository.save(region); }
        region = regionRepository.findById(1435);if (region == null){ region = new Region( 1435, "UA", "POL", "Poltavs'ka"); regionRepository.save(region); }
        region = regionRepository.findById(1436);if (region == null){ region = new Region( 1436, "UA", "RIV", "Rivnens'ka"); regionRepository.save(region); }
        region = regionRepository.findById(1437);if (region == null){ region = new Region( 1437, "UA", "SUM", "Sums'ka"); regionRepository.save(region); }
        region = regionRepository.findById(1438);if (region == null){ region = new Region( 1438, "UA", "TER", "Ternopil's'ka"); regionRepository.save(region); }
        region = regionRepository.findById(1439);if (region == null){ region = new Region( 1439, "UA", "VIN", "Vinnits'ka"); regionRepository.save(region); }
        region = regionRepository.findById(1440);if (region == null){ region = new Region( 1440, "UA", "VOL", "Volins'ka"); regionRepository.save(region); }
        region = regionRepository.findById(1441);if (region == null){ region = new Region( 1441, "UA", "ZAK", "Zakarpats'ka"); regionRepository.save(region); }
        region = regionRepository.findById(1442);if (region == null){ region = new Region( 1442, "UA", "ZAP", "Zaporiz'ka"); regionRepository.save(region); }
        region = regionRepository.findById(1443);if (region == null){ region = new Region( 1443, "UA", "ZHI", "Zhitomirs'ka"); regionRepository.save(region); }
        region = regionRepository.findById(1444);if (region == null){ region = new Region( 1444, "US", "AK", "Alaska"); regionRepository.save(region); }
        region = regionRepository.findById(1445);if (region == null){ region = new Region( 1445, "US", "AL", "Alabama"); regionRepository.save(region); }
        region = regionRepository.findById(1446);if (region == null){ region = new Region( 1446, "US", "AR", "Arkansas"); regionRepository.save(region); }
        region = regionRepository.findById(1447);if (region == null){ region = new Region( 1447, "US", "AS", "American Samoa"); regionRepository.save(region); }
        region = regionRepository.findById(1448);if (region == null){ region = new Region( 1448, "US", "AZ", "Arizona"); regionRepository.save(region); }
        region = regionRepository.findById(1449);if (region == null){ region = new Region( 1449, "US", "CA", "California"); regionRepository.save(region); }
        region = regionRepository.findById(1450);if (region == null){ region = new Region( 1450, "US", "CO", "Colorado"); regionRepository.save(region); }
        region = regionRepository.findById(1451);if (region == null){ region = new Region( 1451, "US", "CT", "Connecticut"); regionRepository.save(region); }
        region = regionRepository.findById(1452);if (region == null){ region = new Region( 1452, "US", "DC", "District of Columbia"); regionRepository.save(region); }
        region = regionRepository.findById(1453);if (region == null){ region = new Region( 1453, "US", "DE", "Delaware"); regionRepository.save(region); }
        region = regionRepository.findById(1454);if (region == null){ region = new Region( 1454, "US", "FL", "Florida"); regionRepository.save(region); }
        region = regionRepository.findById(1455);if (region == null){ region = new Region( 1455, "US", "GA", "Georgia"); regionRepository.save(region); }
        region = regionRepository.findById(1456);if (region == null){ region = new Region( 1456, "US", "GU", "Guam"); regionRepository.save(region); }
        region = regionRepository.findById(1457);if (region == null){ region = new Region( 1457, "US", "HI", "Hawaii"); regionRepository.save(region); }
        region = regionRepository.findById(1458);if (region == null){ region = new Region( 1458, "US", "IA", "Iowa"); regionRepository.save(region); }
        region = regionRepository.findById(1459);if (region == null){ region = new Region( 1459, "US", "ID", "Idaho"); regionRepository.save(region); }
        region = regionRepository.findById(1460);if (region == null){ region = new Region( 1460, "US", "IL", "Illinois"); regionRepository.save(region); }
        region = regionRepository.findById(1461);if (region == null){ region = new Region( 1461, "US", "IN", "Indiana"); regionRepository.save(region); }
        region = regionRepository.findById(1462);if (region == null){ region = new Region( 1462, "US", "KS", "Kansas"); regionRepository.save(region); }
        region = regionRepository.findById(1463);if (region == null){ region = new Region( 1463, "US", "KY", "Kentucky"); regionRepository.save(region); }
        region = regionRepository.findById(1464);if (region == null){ region = new Region( 1464, "US", "LA", "Louisiana"); regionRepository.save(region); }
        region = regionRepository.findById(1465);if (region == null){ region = new Region( 1465, "US", "MA", "Massachusetts"); regionRepository.save(region); }
        region = regionRepository.findById(1466);if (region == null){ region = new Region( 1466, "US", "MD", "Maryland"); regionRepository.save(region); }
        region = regionRepository.findById(1467);if (region == null){ region = new Region( 1467, "US", "ME", "Maine"); regionRepository.save(region); }
        region = regionRepository.findById(1468);if (region == null){ region = new Region( 1468, "US", "MI", "Michigan"); regionRepository.save(region); }
        region = regionRepository.findById(1469);if (region == null){ region = new Region( 1469, "US", "MN", "Minnesota"); regionRepository.save(region); }
        region = regionRepository.findById(1470);if (region == null){ region = new Region( 1470, "US", "MO", "Missouri"); regionRepository.save(region); }
        region = regionRepository.findById(1471);if (region == null){ region = new Region( 1471, "US", "MP", "Northern Mariana Isl"); regionRepository.save(region); }
        region = regionRepository.findById(1472);if (region == null){ region = new Region( 1472, "US", "MS", "Mississippi"); regionRepository.save(region); }
        region = regionRepository.findById(1473);if (region == null){ region = new Region( 1473, "US", "MT", "Montana"); regionRepository.save(region); }
        region = regionRepository.findById(1474);if (region == null){ region = new Region( 1474, "US", "NC", "North Carolina"); regionRepository.save(region); }
        region = regionRepository.findById(1475);if (region == null){ region = new Region( 1475, "US", "ND", "North Dakota"); regionRepository.save(region); }
        region = regionRepository.findById(1476);if (region == null){ region = new Region( 1476, "US", "NE", "Nebraska"); regionRepository.save(region); }
        region = regionRepository.findById(1477);if (region == null){ region = new Region( 1477, "US", "NH", "New Hampshire"); regionRepository.save(region); }
        region = regionRepository.findById(1478);if (region == null){ region = new Region( 1478, "US", "NJ", "New Jersey"); regionRepository.save(region); }
        region = regionRepository.findById(1479);if (region == null){ region = new Region( 1479, "US", "NM", "New Mexico"); regionRepository.save(region); }
        region = regionRepository.findById(1480);if (region == null){ region = new Region( 1480, "US", "NV", "Nevada"); regionRepository.save(region); }
        region = regionRepository.findById(1481);if (region == null){ region = new Region( 1481, "US", "NY", "New York"); regionRepository.save(region); }
        region = regionRepository.findById(1482);if (region == null){ region = new Region( 1482, "US", "OH", "Ohio"); regionRepository.save(region); }
        region = regionRepository.findById(1483);if (region == null){ region = new Region( 1483, "US", "OK", "Oklahoma"); regionRepository.save(region); }
        region = regionRepository.findById(1484);if (region == null){ region = new Region( 1484, "US", "OR", "Oregon"); regionRepository.save(region); }
        region = regionRepository.findById(1485);if (region == null){ region = new Region( 1485, "US", "PA", "Pennsylvania"); regionRepository.save(region); }
        region = regionRepository.findById(1486);if (region == null){ region = new Region( 1486, "US", "PR", "Puerto Rico"); regionRepository.save(region); }
        region = regionRepository.findById(1487);if (region == null){ region = new Region( 1487, "US", "RI", "Rhode Island"); regionRepository.save(region); }
        region = regionRepository.findById(1488);if (region == null){ region = new Region( 1488, "US", "SC", "South Carolina"); regionRepository.save(region); }
        region = regionRepository.findById(1489);if (region == null){ region = new Region( 1489, "US", "SD", "South Dakota"); regionRepository.save(region); }
        region = regionRepository.findById(1490);if (region == null){ region = new Region( 1490, "US", "TN", "Tennessee"); regionRepository.save(region); }
        region = regionRepository.findById(1491);if (region == null){ region = new Region( 1491, "US", "TX", "Texas"); regionRepository.save(region); }
        region = regionRepository.findById(1492);if (region == null){ region = new Region( 1492, "US", "UT", "Utah"); regionRepository.save(region); }
        region = regionRepository.findById(1493);if (region == null){ region = new Region( 1493, "US", "VA", "Virginia"); regionRepository.save(region); }
        region = regionRepository.findById(1494);if (region == null){ region = new Region( 1494, "US", "VI", "Virgin Islands"); regionRepository.save(region); }
        region = regionRepository.findById(1495);if (region == null){ region = new Region( 1495, "US", "VT", "Vermont"); regionRepository.save(region); }
        region = regionRepository.findById(1496);if (region == null){ region = new Region( 1496, "US", "WA", "Washington"); regionRepository.save(region); }
        region = regionRepository.findById(1497);if (region == null){ region = new Region( 1497, "US", "WI", "Wisconsin"); regionRepository.save(region); }
        region = regionRepository.findById(1498);if (region == null){ region = new Region( 1498, "US", "WV", "West Virginia"); regionRepository.save(region); }
        region = regionRepository.findById(1499);if (region == null){ region = new Region( 1499, "US", "WY", "Wyoming"); regionRepository.save(region); }
        region = regionRepository.findById(1500);if (region == null){ region = new Region( 1500, "VE", "AMA", "Amazon"); regionRepository.save(region); }
        region = regionRepository.findById(1501);if (region == null){ region = new Region( 1501, "VE", "ANZ", "Anzoategui"); regionRepository.save(region); }
        region = regionRepository.findById(1502);if (region == null){ region = new Region( 1502, "VE", "APU", "Apure"); regionRepository.save(region); }
        region = regionRepository.findById(1503);if (region == null){ region = new Region( 1503, "VE", "ARA", "Aragua"); regionRepository.save(region); }
        region = regionRepository.findById(1504);if (region == null){ region = new Region( 1504, "VE", "BAR", "Barinas"); regionRepository.save(region); }
        region = regionRepository.findById(1505);if (region == null){ region = new Region( 1505, "VE", "BOL", "Bolivar"); regionRepository.save(region); }
        region = regionRepository.findById(1506);if (region == null){ region = new Region( 1506, "VE", "CAR", "Carabobo"); regionRepository.save(region); }
        region = regionRepository.findById(1507);if (region == null){ region = new Region( 1507, "VE", "COJ", "Cojedes"); regionRepository.save(region); }
        region = regionRepository.findById(1508);if (region == null){ region = new Region( 1508, "VE", "DA", "Delta Amacuro"); regionRepository.save(region); }
        region = regionRepository.findById(1509);if (region == null){ region = new Region( 1509, "VE", "DF", "Distrito Federal"); regionRepository.save(region); }
        region = regionRepository.findById(1510);if (region == null){ region = new Region( 1510, "VE", "FAL", "Falcon"); regionRepository.save(region); }
        region = regionRepository.findById(1511);if (region == null){ region = new Region( 1511, "VE", "GUA", "Guarico"); regionRepository.save(region); }
        region = regionRepository.findById(1512);if (region == null){ region = new Region( 1512, "VE", "LAR", "Lara"); regionRepository.save(region); }
        region = regionRepository.findById(1513);if (region == null){ region = new Region( 1513, "VE", "MER", "Merida"); regionRepository.save(region); }
        region = regionRepository.findById(1514);if (region == null){ region = new Region( 1514, "VE", "MIR", "Miranda"); regionRepository.save(region); }
        region = regionRepository.findById(1515);if (region == null){ region = new Region( 1515, "VE", "MON", "Monagas"); regionRepository.save(region); }
        region = regionRepository.findById(1516);if (region == null){ region = new Region( 1516, "VE", "NE", "Nueva Esparta"); regionRepository.save(region); }
        region = regionRepository.findById(1517);if (region == null){ region = new Region( 1517, "VE", "POR", "Portuguesa"); regionRepository.save(region); }
        region = regionRepository.findById(1518);if (region == null){ region = new Region( 1518, "VE", "SUC", "Sucre"); regionRepository.save(region); }
        region = regionRepository.findById(1519);if (region == null){ region = new Region( 1519, "VE", "TAC", "Tachira"); regionRepository.save(region); }
        region = regionRepository.findById(1520);if (region == null){ region = new Region( 1520, "VE", "TRU", "Trujillo"); regionRepository.save(region); }
        region = regionRepository.findById(1521);if (region == null){ region = new Region( 1521, "VE", "VAR", "Vargas"); regionRepository.save(region); }
        region = regionRepository.findById(1522);if (region == null){ region = new Region( 1522, "VE", "YAR", "Yaracuy"); regionRepository.save(region); }
        region = regionRepository.findById(1523);if (region == null){ region = new Region( 1523, "VE", "ZUL", "Zulia"); regionRepository.save(region); }
        region = regionRepository.findById(1524);if (region == null){ region = new Region( 1524, "ZA", "EC", "Eastern Cape"); regionRepository.save(region); }
        region = regionRepository.findById(1525);if (region == null){ region = new Region( 1525, "ZA", "FS", "Freestate"); regionRepository.save(region); }
        region = regionRepository.findById(1526);if (region == null){ region = new Region( 1526, "ZA", "GP", "Gauteng"); regionRepository.save(region); }
        region = regionRepository.findById(1527);if (region == null){ region = new Region( 1527, "ZA", "KZN", "Kwazulu/Natal"); regionRepository.save(region); }
        region = regionRepository.findById(1528);if (region == null){ region = new Region( 1528, "ZA", "MP", "Mpumalanga"); regionRepository.save(region); }
        region = regionRepository.findById(1529);if (region == null){ region = new Region( 1529, "ZA", "NC", "Northern Cape"); regionRepository.save(region); }
        region = regionRepository.findById(1530);if (region == null){ region = new Region( 1530, "ZA", "NP", "Northern Province"); regionRepository.save(region); }
        region = regionRepository.findById(1531);if (region == null){ region = new Region( 1531, "ZA", "NW", "North-West"); regionRepository.save(region); }
        region = regionRepository.findById(1532);if (region == null){ region = new Region( 1532, "ZA", "WC", "Western Cape"); regionRepository.save(region); }


        return;
    }
}