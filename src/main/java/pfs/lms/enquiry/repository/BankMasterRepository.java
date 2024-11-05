package pfs.lms.enquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.domain.AssistanceType;
import pfs.lms.enquiry.domain.BankMaster;

import java.util.List;
import java.util.UUID;

public interface BankMasterRepository extends JpaRepository<BankMaster, Long> {

    public BankMaster findBankMasterByBankCountryKeyAndBankKey(String countryKey, String bankKey);
    public List<BankMaster> findBankMasterByBankNameStartingWith(String bankName);
    public BankMaster findByBankKey(String bankKey);

}
