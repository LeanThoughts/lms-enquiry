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
        sortKey = sortKeyRepository.findById("000");if (sortKey == null){ sortKey = new SortKey(null,"Allocationnumber"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("001");if (sortKey == null){ sortKey = new SortKey(null,"Postingdate"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("002");if (sortKey == null){ sortKey = new SortKey(null,"Doc.no.,fiscalyear"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("003");if (sortKey == null){ sortKey = new SortKey(null,"Documentdate"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("004");if (sortKey == null){ sortKey = new SortKey(null,"Branchaccount"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("005");if (sortKey == null){ sortKey = new SortKey(null,"Loc.currencyamount"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("006");if (sortKey == null){ sortKey = new SortKey(null,"Doc.currencyamount"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("007");if (sortKey == null){ sortKey = new SortKey(null,"Bill/exch.duedate"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("008");if (sortKey == null){ sortKey = new SortKey(null,"Costcenter"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("009");if (sortKey == null){ sortKey = new SortKey(null,"Externaldoc.number"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("010");if (sortKey == null){ sortKey = new SortKey(null,"Purchaseorderno."); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("011");if (sortKey == null){ sortKey = new SortKey(null,"Plantnumber"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("012");if (sortKey == null){ sortKey = new SortKey(null,"Vendornumber"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("014");if (sortKey == null){ sortKey = new SortKey(null,"Purchaseorder"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("015");if (sortKey == null){ sortKey = new SortKey(null,"Personnelnumber"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("016");if (sortKey == null){ sortKey = new SortKey(null,"Settlementperiod"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("017");if (sortKey == null){ sortKey = new SortKey(null,"Settl.per.,pers.no."); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("018");if (sortKey == null){ sortKey = new SortKey(null,"Assetnumber"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("021");if (sortKey == null){ sortKey = new SortKey(null,"Segmenttext"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("022");if (sortKey == null){ sortKey = new SortKey(null,"One-timename/"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("023");if (sortKey == null){ sortKey = new SortKey(null,"One-timecity/"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("024");if (sortKey == null){ sortKey = new SortKey(null,"Documentheadertext"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("025");if (sortKey == null){ sortKey = new SortKey(null,"CPUdate"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("026");if (sortKey == null){ sortKey = new SortKey(null,"Pmntper.bslnedate"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("027");if (sortKey == null){ sortKey = new SortKey(null,"Valuedate"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("028");if (sortKey == null){ sortKey = new SortKey(null,"Assetnumber"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("029");if (sortKey == null){ sortKey = new SortKey(null,"Pstngmonth,vendor"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("031");if (sortKey == null){ sortKey = new SortKey(null,"Customernumber"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("032");if (sortKey == null){ sortKey = new SortKey(null,"Pstngyr,month,curr."); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("033");if (sortKey == null){ sortKey = new SortKey(null,"Costcenter"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("034");if (sortKey == null){ sortKey = new SortKey(null,"Month,costcenter"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("035");if (sortKey == null){ sortKey = new SortKey(null,"Contractnumber"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("036");if (sortKey == null){ sortKey = new SortKey(null,"Order"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("037");if (sortKey == null){ sortKey = new SortKey(null,"Currencykey"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("039");if (sortKey == null){ sortKey = new SortKey(null,"Projectnumber"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("055");if (sortKey == null){ sortKey = new SortKey(null,"Fiscalyear,month"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("060");if (sortKey == null){ sortKey = new SortKey(null,"Test0"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("061");if (sortKey == null){ sortKey = new SortKey(null,"Test1"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("065");if (sortKey == null){ sortKey = new SortKey(null,"Test5"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("101");if (sortKey == null){ sortKey = new SortKey(null,"Cashdiscntclearing"); sortKeyRepository.save(sortKey); }
        sortKey = sortKeyRepository.findById("105");if (sortKey == null){ sortKey = new SortKey(null,"Test"); sortKeyRepository.save(sortKey); }


    }
}