package pfs.lms.enquiry.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pfs.lms.enquiry.config.ApiController;
import pfs.lms.enquiry.domain.BankMaster;
import pfs.lms.enquiry.repository.BankMasterRepository;
import pfs.lms.enquiry.service.IBankMasterService;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by sajeev on 16-Feb-19.
 */
@Slf4j
@ApiController
@RequiredArgsConstructor
public class BankMasterController {

    @Autowired
    IBankMasterService bankMasterService;

    @Autowired
    BankMasterRepository bankMasterRepository;


    @RequestMapping(value = "/bankmasters/all", method = {RequestMethod.GET})
    public ResponseEntity<List<BankMaster>> getBankMasterList(HttpServletRequest request) {

        List<BankMaster> bankMasterList = bankMasterRepository.findAll();

        return ResponseEntity.ok(bankMasterList);

    }

    @RequestMapping(value = "/bankmasters", method = {RequestMethod.GET})
    public ResponseEntity<List<BankMaster>> findByBankName(@RequestParam(value = "bankName",required = true) String bankName, HttpServletRequest request) {

        List<BankMaster> bankMasterList  = bankMasterRepository.findBankMasterByBankNameStartingWith(bankName);

        return ResponseEntity.ok(bankMasterList);

    }


    @PostMapping(value = "/bankmaster")
    public ResponseEntity<BankMaster> createBankMaster(@RequestBody BankMaster bankMaster, HttpServletRequest request) {

        System.out.println("Uploading Bank Master....." + bankMaster.toString() );
        System.out.println("Uploading Bank Master....." + bankMaster.getBankCountryKey() + ":" + bankMaster.getBankKey() );

        BankMaster existingbankMaster = bankMasterRepository.findBankMasterByBankCountryKeyAndBankKey(bankMaster.getBankCountryKey(), bankMaster.getBankKey());

        if (existingbankMaster != null) {
            existingbankMaster.setUpdatedAt(new Date());

            existingbankMaster.setCreatedBy(bankMaster.getCreatedBy());
            existingbankMaster.setCreationDate(bankMaster.getCreationDate());
            existingbankMaster.setBankName(bankMaster.getBankName());
            existingbankMaster.setRegion(bankMaster.getRegion());
            existingbankMaster.setAddressNumber(bankMaster.getAddressNumber());
            existingbankMaster.setHouseNumberAndStreet(bankMaster.getHouseNumberAndStreet());
            existingbankMaster.setCity(bankMaster.getCity());
            existingbankMaster.setSwiftCodeOrBIC(bankMaster.getSwiftCodeOrBIC());
            existingbankMaster.setBankGroup(bankMaster.getBankGroup());
            existingbankMaster.setPostOfficeBankCurrentAccount(bankMaster.getPostOfficeBankCurrentAccount());
            existingbankMaster.setDeletionIndicator(bankMaster.getDeletionIndicator());
            existingbankMaster.setBankNumber(bankMaster.getBankNumber());
            existingbankMaster.setPostOfficeBankCurrentAccountNumber(bankMaster.getPostOfficeBankCurrentAccountNumber());
            existingbankMaster.setBankBranch(bankMaster.getBankBranch());
            existingbankMaster.setCheckDigitCalculationMethod(bankMaster.getCheckDigitCalculationMethod());
            existingbankMaster.setFormatofFileWithBankData(bankMaster.getFormatofFileWithBankData());
            existingbankMaster.setFormatofFileWithBankData(bankMaster.getFormatofFileWithBankData());
            existingbankMaster.setBankMasterData(bankMaster.getBankMasterData());
            existingbankMaster.setSupportofSEPACOR1DirectDebit(bankMaster.getSupportofSEPACOR1DirectDebit());
            existingbankMaster.setSupportofSEPAReturnedDebits(bankMaster.getSupportofSEPAReturnedDebits());
            existingbankMaster.setSupportofSEPACOR1DirectDebit(bankMaster.getSupportofSEPACOR1DirectDebit());
            existingbankMaster.setKeyofaBICDataRecord(bankMaster.getKeyofaBICDataRecord());
            existingbankMaster.setRoutingControlCode(bankMaster.getRoutingControlCode());
            bankMaster = bankMasterRepository.save(existingbankMaster);
            return ResponseEntity.ok(existingbankMaster);
        }

        bankMaster.setUpdatedAt(new Date());
        bankMaster.setCreatedAt(new Date() );
        bankMaster.setCreationDate(DateTime.now().toString());
        bankMaster.setCreatedBy(request.getUserPrincipal().getName());
        bankMaster = bankMasterRepository.save(bankMaster);
        return  ResponseEntity.ok(bankMaster);
    }

}
