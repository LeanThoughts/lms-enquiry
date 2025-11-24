package pfs.lms.enquiry.businesspartner.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.businesspartner.domain.SortKey;
import pfs.lms.enquiry.businesspartner.repository.SortKeyRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class SortKeyConfig implements CommandLineRunner {

    private final SortKeyRepository sortKeyRepository;
 
    @Override
    public void run(String... strings) throws Exception {

        SortKey sortKey = new SortKey();
        sortKey = sortKeyRepository.findById("000");if (sortKey == null){ sortKey = new SortKey("000","Allocationnumber"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("001");if (sortKey == null){ sortKey = new SortKey("001","Postingdate"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("002");if (sortKey == null){ sortKey = new SortKey("002","Doc.no.,fiscalyear"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("003");if (sortKey == null){ sortKey = new SortKey("003","Documentdate"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("004");if (sortKey == null){ sortKey = new SortKey("004","Branchaccount"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("005");if (sortKey == null){ sortKey = new SortKey("005","Loc.currencyamount"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("006");if (sortKey == null){ sortKey = new SortKey("006","Doc.currencyamount"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("007");if (sortKey == null){ sortKey = new SortKey("007","Bill/exch.duedate"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("008");if (sortKey == null){ sortKey = new SortKey("008","Costcenter"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("009");if (sortKey == null){ sortKey = new SortKey("009","Externaldoc.number"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("010");if (sortKey == null){ sortKey = new SortKey("010","Purchaseorderno."); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("011");if (sortKey == null){ sortKey = new SortKey("011","Plantnumber"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("012");if (sortKey == null){ sortKey = new SortKey("012","Vendornumber"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("014");if (sortKey == null){ sortKey = new SortKey("014","Purchaseorder"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("015");if (sortKey == null){ sortKey = new SortKey("015","Personnelnumber"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("016");if (sortKey == null){ sortKey = new SortKey("016","Settlementperiod"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("017");if (sortKey == null){ sortKey = new SortKey("017","Settl.per.,pers.no."); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("018");if (sortKey == null){ sortKey = new SortKey("018","Assetnumber"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("021");if (sortKey == null){ sortKey = new SortKey("021","Segmenttext"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("022");if (sortKey == null){ sortKey = new SortKey("022","One-timename/"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("023");if (sortKey == null){ sortKey = new SortKey("023","One-timecity/"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("024");if (sortKey == null){ sortKey = new SortKey("024","Documentheadertext"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("025");if (sortKey == null){ sortKey = new SortKey("025","CPUdate"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("026");if (sortKey == null){ sortKey = new SortKey("026","Pmntper.bslnedate"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("027");if (sortKey == null){ sortKey = new SortKey("027","Valuedate"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("028");if (sortKey == null){ sortKey = new SortKey("028","Assetnumber"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("029");if (sortKey == null){ sortKey = new SortKey("029","Pstngmonth,vendor"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("031");if (sortKey == null){ sortKey = new SortKey("031","Customernumber"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("032");if (sortKey == null){ sortKey = new SortKey("032","Pstngyr,month,curr."); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("033");if (sortKey == null){ sortKey = new SortKey("033","Costcenter"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("034");if (sortKey == null){ sortKey = new SortKey("034","Month,costcenter"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("035");if (sortKey == null){ sortKey = new SortKey("035","Contractnumber"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("036");if (sortKey == null){ sortKey = new SortKey("036","Order"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("037");if (sortKey == null){ sortKey = new SortKey("037","Currencykey"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("039");if (sortKey == null){ sortKey = new SortKey("039","Projectnumber"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("055");if (sortKey == null){ sortKey = new SortKey("055","Fiscalyear,month"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("060");if (sortKey == null){ sortKey = new SortKey("060","Test0"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("061");if (sortKey == null){ sortKey = new SortKey("061","Test1"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("065");if (sortKey == null){ sortKey = new SortKey("065","Test5"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("101");if (sortKey == null){ sortKey = new SortKey("101","Cashdiscntclearing"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("105");if (sortKey == null){ sortKey = new SortKey("105","Test"); sortKeyRepository.save(sortKey); }


    }
}