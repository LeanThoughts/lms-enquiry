package pfs.lms.enquiry.businesspartner.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.businesspartner.domain.HouseBank;
import pfs.lms.enquiry.businesspartner.domain.LegalForm;
import pfs.lms.enquiry.businesspartner.repository.HouseBankRepository;
import pfs.lms.enquiry.businesspartner.repository.LegalFormRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class HouseBankConfig implements CommandLineRunner {

    private final HouseBankRepository houseBankRepository;
 
    @Override
    public void run(String... strings) throws Exception {

        HouseBank houseBank = new HouseBank();


        //houseBank  = houseBank = houseBankRepository.findByHouseBankId("01");if (houseBank == null){ houseBank = new HouseBank( "","", "", "","","", "",""); houseBankRepository.save(houseBank); }

        houseBank = houseBankRepository.findByHouseBankId("HDF10");if (houseBank == null){ houseBank = new HouseBank( "HDF10", "IN", "40024000201", "", "", "", "EN", "HDFC Sandoz CA#18040039004"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("HDF11");if (houseBank == null){ houseBank = new HouseBank( "HDF11", "IN", "40024000301", "", "", "", "EN", "HDFC Backbay CA#13700929054"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("ICI10");if (houseBank == null){ houseBank = new HouseBank( "ICI10", "IN", "40022900201", "", "", "", "EN", "ICICI Nariman Point OD#186029875345"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("ICI21");if (houseBank == null){ houseBank = new HouseBank( "ICI21", "IN", "39022904301", "", "", "", "EN", "ICICI Vapi CA#278009965431"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("AXS01");if (houseBank == null){ houseBank = new HouseBank( "AXS01", "IN", "AXS01", "", "", "", "", "AXIS-PIFSL-Dividend A/c 16-17-917020057750025"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("BNP01");if (houseBank == null){ houseBank = new HouseBank( "BNP01", "IN", "BNP01", "", "", "", "", "BNP Paribas-0906511290800142"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("BOM01");if (houseBank == null){ houseBank = new HouseBank( "BOM01", "IN", "BOM01", "", "", "", "", "Bank of Maharashtra-60386583891"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("CAN01");if (houseBank == null){ houseBank = new HouseBank( "CAN01", "IN", "CAN01", "", "", "", "", "Canara Bank-2624201000203"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("CAN02");if (houseBank == null){ houseBank = new HouseBank( "CAN02", "IN", "CAN02", "", "", "", "", "Canara Bank-2624256000111"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("COB01");if (houseBank == null){ houseBank = new HouseBank( "COB01", "IN", "COB01", "", "", "", "", "Corporation Bank-0102"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("COB02");if (houseBank == null){ houseBank = new HouseBank( "COB02", "IN", "COB02", "", "", "", "", "Corporation Bank-0103"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("COB03");if (houseBank == null){ houseBank = new HouseBank( "COB03", "IN", "COB03", "", "", "", "", "Corporation Bank-0131"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("COB04");if (houseBank == null){ houseBank = new HouseBank( "COB04", "IN", "COB04", "", "", "", "", "Corporation Bank: 510101005725241"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("DEU01");if (houseBank == null){ houseBank = new HouseBank( "DEU01", "IN", "DEU01", "", "", "", "", "Deutche Bank-OD-15479-00-0"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("HDF01");if (houseBank == null){ houseBank = new HouseBank( "HDF01", "IN", "HDF01", "", "", "", "", "HDFC Bank Ltd-3597"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("HDF02");if (houseBank == null){ houseBank = new HouseBank( "HDF02", "IN", "HDF02", "", "", "", "", "HDFC Bank Ltd-1079"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("HDF03");if (houseBank == null){ houseBank = new HouseBank( "HDF03", "IN", "HDF03", "", "", "", "", "HDFC Bank Ltd-7069"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("HDF04");if (houseBank == null){ houseBank = new HouseBank( "HDF04", "IN", "HDF04", "", "", "", "", "HDFC BankLtd-PFS Infra2 Serv1213 A/c-0032300006722"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("HDF05");if (houseBank == null){ houseBank = new HouseBank( "HDF05", "IN", "HDF05", "", "", "", "", "HDFC BankLtd-PFS Infra1 Serv1213 A/c-0032300006732"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("HDF06");if (houseBank == null){ houseBank = new HouseBank( "HDF06", "IN", "HDF06", "", "", "", "", "HDFC Bank Ltd-8359"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("HDF07");if (houseBank == null){ houseBank = new HouseBank( "HDF07", "IN", "HDF07", "", "", "", "", "HDFC Bank Ltd-00030350012758"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("HDF08");if (houseBank == null){ houseBank = new HouseBank( "HDF08", "IN", "HDF08", "", "", "", "", "HDFC BankLtd-PFS InfraServAccnt-2012-0032300005686"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("HDF09");if (houseBank == null){ houseBank = new HouseBank( "HDF09", "IN", "HDF09", "", "", "", "", "HDFC Bank Ltd-4476"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("HDF10");if (houseBank == null){ houseBank = new HouseBank( "HDF10", "IN", "HDF10", "", "", "", "", "HDFC Bank Ltd-Infra1 Serv-1415-1278"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("HDF11");if (houseBank == null){ houseBank = new HouseBank( "HDF11", "IN", "HDF11", "", "", "", "", "HDFC Bank Ltd-Infra2 Serv-1415-1281"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("HDF12");if (houseBank == null){ houseBank = new HouseBank( "HDF12", "IN", "HDF12", "", "", "", "", "HDFC Bank Ltd-Infra1 Serv-1516-1832"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("HDF13");if (houseBank == null){ houseBank = new HouseBank( "HDF13", "IN", "HDF13", "", "", "", "", "HDFC Bank Ltd-Infra2 Serv-1516-1693"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("HDF14");if (houseBank == null){ houseBank = new HouseBank( "HDF14", "IN", "HDF14", "", "", "", "", "HDFC Bank Ltd-Infra1 Serv-1617-7761"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("HDF15");if (houseBank == null){ houseBank = new HouseBank( "HDF15", "IN", "HDF15", "", "", "", "", "HDFC Bank Ltd-Infra2 Serv-1617-7787"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("HDF16");if (houseBank == null){ houseBank = new HouseBank( "HDF16", "IN", "HDF16", "", "", "", "", "HDFC Bank Ltd-PFS-Infra1 Serv-1718-A/c: 7860"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("HDF17");if (houseBank == null){ houseBank = new HouseBank( "HDF17", "IN", "HDF17", "", "", "", "", "HDFC Bank Ltd-PFS-Infra2 Serv-1718-A/c: 7870"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("HDF18");if (houseBank == null){ houseBank = new HouseBank( "HDF18", "IN", "HDF18", "", "", "", "", "HDFC-PIFSL-Dividend A/c 17-18-50200033126430"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("HDF19");if (houseBank == null){ houseBank = new HouseBank( "HDF19", "IN", "HDF19", "", "", "", "", "HDFC Bank Ltd-PFS-Infra1 Serv-1819-50200037978021"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("HDF20");if (houseBank == null){ houseBank = new HouseBank( "HDF20", "IN", "HDF20", "", "", "", "", "HDFC Bank Ltd-PFS-Infra2 Serv-1819- 50200037977985"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("HDF21");if (houseBank == null){ houseBank = new HouseBank( "HDF21", "IN", "HDF21", "", "", "", "", "HDFC-PIFSL-Dividend A/c 18-19-50200043813331"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("ICI01");if (houseBank == null){ houseBank = new HouseBank( "ICI01", "IN", "ICIC0000007", "9650909829", "", "Mr. Mohit Saxena", "", "ICICI Bank Ltd: 000705036410"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("ICI02");if (houseBank == null){ houseBank = new HouseBank( "ICI02", "IN", "ICI02", "", "", "", "", "ICICI Bank Ltd-6240"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("ICI03");if (houseBank == null){ houseBank = new HouseBank( "ICI03", "IN", "ICI03", "", "", "", "", "ICICI Bank Ltd-PFS-Infra1 Serv-1920: 000705050159"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("ICI04");if (houseBank == null){ houseBank = new HouseBank( "ICI04", "IN", "ICI04", "", "", "", "", "ICICI Bank Ltd-PFS-Infra2 Serv-1920: 000705050158"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("ICI05");if (houseBank == null){ houseBank = new HouseBank( "ICI05", "IN", "ICI05", "", "", "", "", "ICICI-PIFSL-Dividend A/c 19-20-: 000705050532"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("ICI06");if (houseBank == null){ houseBank = new HouseBank( "ICI06", "IN", "ICI06", "", "", "", "", "ICICI Bank Ltd-PFS-Infra1 Serv-2021: 000705051312"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("ICI07");if (houseBank == null){ houseBank = new HouseBank( "ICI07", "IN", "ICI07", "", "", "", "", "ICICI Bank Ltd-PFS-Infra2 Serv-2021: 000705051313"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("ICI08");if (houseBank == null){ houseBank = new HouseBank( "ICI08", "IN", "ICI08", "", "", "", "", "ICICI Bank Ltd-PFS-Infra2 Serv-2122: 054805001349"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("ICI09");if (houseBank == null){ houseBank = new HouseBank( "ICI09", "IN", "ICI09", "", "", "", "", "ICICI Bank Ltd-PFS Unspent CSR Funds-054805001457"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("ICI10");if (houseBank == null){ houseBank = new HouseBank( "ICI10", "IN", "ICI10", "", "", "", "", "ICICI Bank Ltd-PFS-Infra2 Serv-2223: 054805001554"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("ICI11");if (houseBank == null){ houseBank = new HouseBank( "ICI11", "IN", "ICIC0000548", "", "", "", "", "ICICI-PFS Unspent CSR Accnt-2223-054805001561"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("ICI12");if (houseBank == null){ houseBank = new HouseBank( "ICI12", "IN", "ICI12", "", "", "", "", "ICICI-PIFSL-Dividend A/c 22-23-: 054805001683"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("ICI13");if (houseBank == null){ houseBank = new HouseBank( "ICI13", "IN", "ICI13", "", "", "", "", "ICICI Bank Ltd-PFS-Infra2 Serv-2324: 054805001844"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("ICI14");if (houseBank == null){ houseBank = new HouseBank( "ICI14", "IN", "ICI14", "", "", "", "", "ICICIBkLtd-PFS Unspent CSR Accnt-2324:054805001865"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("ICI15");if (houseBank == null){ houseBank = new HouseBank( "ICI15", "IN", "ICI15", "", "", "", "", "ICICI Bank Ltd-PFS-Infra2 Serv-2425: 054805002119"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("ICI16");if (houseBank == null){ houseBank = new HouseBank( "ICI16", "IN", "ICI16", "", "", "", "", "PFS UNSPENT CSR ACCOUNT FY 2425-000705057138"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("IDB01");if (houseBank == null){ houseBank = new HouseBank( "IDB01", "IN", "IDB01", "", "", "", "", "IDBI Bank Ltd-2175"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("IDB02");if (houseBank == null){ houseBank = new HouseBank( "IDB02", "IN", "IDB02", "", "", "", "", "IDBI Bank Ltd-PFS Dividend(12-13)-2944"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("IDB03");if (houseBank == null){ houseBank = new HouseBank( "IDB03", "IN", "IDB03", "", "", "", "", "IDBI Bank Ltd-PFS Div-13-14-0127103000014881"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("IDB04");if (houseBank == null){ houseBank = new HouseBank( "IDB04", "IN", "IDB04", "", "", "", "", "IDBI Bank Ltd-PFS Dividend(14-15)-5987"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("INB01");if (houseBank == null){ houseBank = new HouseBank( "INB01", "IN", "INB01", "", "", "", "", "Indian Bank-5637"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("INB02");if (houseBank == null){ houseBank = new HouseBank( "INB02", "IN", "INB02", "", "", "", "", "Indian Bank-5785"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("INB03");if (houseBank == null){ houseBank = new HouseBank( "INB03", "IN", "INB03", "", "", "", "", "Indian Bank OD A/c – 50502304111"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("IND01");if (houseBank == null){ houseBank = new HouseBank( "IND01", "IN", "IND01", "", "", "", "", "Indusind-PFS-Infra1 Serv-1314-200999449518"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("IND02");if (houseBank == null){ houseBank = new HouseBank( "IND02", "IN", "IND02", "", "", "", "", "Indusind-PFS-Infra2 Serv-1314-200999449525"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("IOB01");if (houseBank == null){ houseBank = new HouseBank( "IOB01", "IN", "IOB01", "", "", "", "", "Indian Overseas Bank-PIFSL-120502000000873"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("J&K01");if (houseBank == null){ houseBank = new HouseBank( "J&K01", "IN", "J&K01", "", "", "", "", "The Jammu & Kashmir Bank Ltd-0091"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("OBC01");if (houseBank == null){ houseBank = new HouseBank( "OBC01", "IN", "OBC01", "", "", "", "", "Oriental Bank of Commerce-1033"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("OBC02");if (houseBank == null){ houseBank = new HouseBank( "OBC02", "IN", "OBC02", "", "", "", "", "Oriental Bank of Commerce-1736"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("OBC03");if (houseBank == null){ houseBank = new HouseBank( "OBC03", "IN", "OBC03", "", "", "", "", "Oriental Bank of Commerce-1743"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("OBC04");if (houseBank == null){ houseBank = new HouseBank( "OBC04", "IN", "OBC04", "", "", "", "", "Oriental Bank of Commerce-1750"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("OBC05");if (houseBank == null){ houseBank = new HouseBank( "OBC05", "IN", "OBC05", "", "", "", "", "Oriental Bank of Commerce-1279"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("PNB01");if (houseBank == null){ houseBank = new HouseBank( "PNB01", "IN", "PNB01", "", "", "", "", "Punjab National Bank-6860"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("PNB02");if (houseBank == null){ houseBank = new HouseBank( "PNB02", "IN", "PNB02", "", "", "", "", "Punjab National Bank-8150"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("PNB03");if (houseBank == null){ houseBank = new HouseBank( "PNB03", "IN", "PNB03", "", "", "", "", "Punjab National Bank-7750"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("PNB04");if (houseBank == null){ houseBank = new HouseBank( "PNB04", "IN", "PNB04", "", "", "", "", "Punjab National Bank-8520"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("PNB05");if (houseBank == null){ houseBank = new HouseBank( "PNB05", "IN", "PNB05", "", "", "", "", "Punjab National Bank-8940"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("PNB06");if (houseBank == null){ houseBank = new HouseBank( "PNB06", "IN", "PNB06", "", "", "", "", "Punjab National Bank-1074"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("SBI01");if (houseBank == null){ houseBank = new HouseBank( "SBI01", "IN", "SBI01", "", "", "", "", "State Bank of India"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("SCB01");if (houseBank == null){ houseBank = new HouseBank( "SCB01", "IN", "SCB01", "", "", "", "", "Standard Chartered Bank Ltd-3044"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("SIB01");if (houseBank == null){ houseBank = new HouseBank( "SIB01", "IN", "SIB01", "", "", "", "", "South Indian Bank-OD-074408100000001"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("SYN01");if (houseBank == null){ houseBank = new HouseBank( "SYN01", "IN", "SYN01", "", "", "", "", "Syndicate Bank-0426"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("UBI01");if (houseBank == null){ houseBank = new HouseBank( "UBI01", "IN", "UBI01", "", "", "", "", "Union Bank of India-0099"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("UBI02");if (houseBank == null){ houseBank = new HouseBank( "UBI02", "IN", "UBI02", "", "", "", "", "Union Bank of India-497801010000100"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("UCO01");if (houseBank == null){ houseBank = new HouseBank( "UCO01", "IN", "UCO01", "", "", "", "", "UCO Bank-OD-0267-0267"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("YES01");if (houseBank == null){ houseBank = new HouseBank( "YES01", "IN", "YESB0000003", "9958988954", "", "Mr Vaibbav Maheshwari", "", "Yes Bank : 000382000001344"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("YES02");if (houseBank == null){ houseBank = new HouseBank( "YES02", "IN", "YES02", "", "", "", "", "Yes Bank Ltd-3221"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("YES03");if (houseBank == null){ houseBank = new HouseBank( "YES03", "IN", "YES03", "", "", "", "", "Yes Bank Ltd-2653"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("YES04");if (houseBank == null){ houseBank = new HouseBank( "YES04", "IN", "YES04", "", "", "", "", "Yes Bank Ltd-2660"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("YES05");if (houseBank == null){ houseBank = new HouseBank( "YES05", "IN", "YES05", "", "", "", "", "Yes Bank Ltd-2672"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("YES06");if (houseBank == null){ houseBank = new HouseBank( "YES06", "IN", "YES06", "", "", "", "", "Yes Bank Ltd-1214"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("YES07");if (houseBank == null){ houseBank = new HouseBank( "YES07", "IN", "YES07", "", "", "", "", "Yes Bank Ltd-000380200001226"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("YES08");if (houseBank == null){ houseBank = new HouseBank( "YES08", "IN", "YES08", "", "", "", "", "Yes Bank Ltd-0290"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("YES09");if (houseBank == null){ houseBank = new HouseBank( "YES09", "IN", "YES09", "", "", "", "", "Yes Bank Ltd-000380200008921"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("YES10");if (houseBank == null){ houseBank = new HouseBank( "YES10", "IN", "YES10", "", "", "", "", "Yes Bank Ltd-8945"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("YES11");if (houseBank == null){ houseBank = new HouseBank( "YES11", "IN", "YES11", "", "", "", "", "Yes Bank Ltd-0067"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("YES12");if (houseBank == null){ houseBank = new HouseBank( "YES12", "IN", "YES12", "", "", "", "", "Yes Bank Limited-000381400006390"); houseBankRepository.save(houseBank); }
        houseBank = houseBankRepository.findByHouseBankId("HDF01");if (houseBank == null){ houseBank = new HouseBank( "HDF01", "IN", "HDFC0003666", "", "", "", "", "HDFC Bank Ltd-50200063683180"); houseBankRepository.save(houseBank); }

        return;
    }
}