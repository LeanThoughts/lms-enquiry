package pfs.lms.enquiry.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.domain.PartnerContact;
import pfs.lms.enquiry.domain.PartnerRoleType;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.repository.PartnerRepository;
import pfs.lms.enquiry.repository.PartnerRoleTypeRepository;
import pfs.lms.enquiry.repository.UserRepository;
import pfs.lms.enquiry.resource.PartnerResourceByAlphabet;
import pfs.lms.enquiry.resource.PartnerResourceByEmail;
import pfs.lms.enquiry.resource.PartnerResourcesOrderByAlphabet;
import pfs.lms.enquiry.service.IPartnerService;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Component
@Transactional
public class PartnerService implements IPartnerService {

    private final PartnerRepository partnerRepository;
    private final PartnerRoleTypeRepository partnerRoleTypeRepository;
    private final UserRepository userRepository;

    @Override
    public Partner getOne(String username) {
        return partnerRepository.findByUserName(username);
    }

    @Override
    public Partner save(Partner partner) {

        //Check if rhe partner already exist
        Partner existing = null;
        List<Partner> partnerList = partnerRepository.findByEmail(partner.getEmail());
        for (Partner partnerItem : partnerList ) {
            if (partnerItem.getPartyNumber() != null && partner.getPartyNumber() != null) {
                if (partnerItem.getPartyNumber().intValue() == partner.getPartyNumber().intValue()) {
                    existing = partnerItem;
                }
            }
        }
        if (existing == null) {
            if (partnerList.size() > 0) {
                existing = partnerList.get(0);
            }
        }

        //If exists return the existing partner
        if (existing != null) {
            existing.setAddressLine1(partner.getAddressLine1());
            existing.setAddressLine2(partner.getAddressLine2());
            existing.setCity(partner.getCity());
            existing.setContactNumber(partner.getContactNumber());
            existing.setContactPersonName(partner.getContactPersonName());
            existing.setCountry(partner.getCountry());
            existing.setGroupCompany(partner.getGroupCompany());
            existing.setPan(partner.getPan());
            existing.setPartyCategory(partner.getPartyCategory());
            existing.setPartyName1(partner.getPartyName1());
            existing.setPartyName2(partner.getPartyName2());
            existing.setPartyNumber(partner.getPartyNumber());
            existing.setPostalCode(partner.getPostalCode());
            existing.setState(partner.getState());
            existing.setStreet(partner.getStreet());
            existing.setEmail(partner.getEmail());
            existing.setIndustrySector(partner.getIndustrySector());
            existing = partnerRepository.saveAndFlush(existing);
            
             return existing;
        }
        //If not create a new partner and return
        else {
            try {
                partner.setUserName(partner.getEmail());
                partner = partnerRepository.saveAndFlush(partner);
            }
            catch (Exception ex) {
                System.out.println("------------------Exception Saving Partner -----------------------------------:" + partner.getPartyNumber());
                System.out.println(ex.getMessage());
            }
            return partner;

        }
    }

    @Override
    public Partner migrate(Partner partner) {
        //Check if rhe partner already exist
        Partner existing = partnerRepository.findByPartyNumber(partner.getPartyNumber());

        //If exists return the existing partner
        if (existing != null) {
//
//            System.out.println("--------------------------------------------------------------------------------");
//            System.out.println("---------------------- Business Partner ID : " + partner.getPartyNumber());
//            System.out.println("---------------------- Partner Already exists : EMAIL : " + partner.getEmail());
//            System.out.println("---------------------- Partner ID: " + partner.getId());
//            System.out.println("--------------------------------------------------------------------------------");
            return existing;
        }
        //If not create a new partner and return
        else {
//            System.out.println("---------------------NEW PARTNER CREATION----------------------------------------");
//            System.out.println("---------------------- Partner ID: " + partner.getId());
            try {

//                System.out.println("---------------------------------------------------------------------------------------");
//                System.out.println("---------------------- Created New Partner : EMAIL : " + partner.getEmail() + "---");
//                System.out.println("---------------------- Partner ID: " + partner.getId());
//                System.out.println("---------------------- Partner User Name : " + partner.getUserName());
//                System.out.println("---------------------- Business Partner ID : " + partner.getPartyNumber());
//                System.out.println("---------------------------------------------------------------------------------------");

                partner = partnerRepository.saveAndFlush(partner);

            }
            catch (Exception ex) {
                System.out.println("------------------Exception Saving Partner -----------------------------------:" + partner.getPartyNumber());
                System.out.println(ex.getMessage());
            }
            return partner;

        }
    }

    @Override
    public Partner update(Partner partner) {
        return null;
    }

    @Override
    public List<Partner> searchPartners(String[] searchParameters) {
        String name = null;
        String email = null;
        String searchTerm ="";
        String partyNumberFrom = null;
        String partyNumberTo = null;
        Integer index = 0;
        Integer partnerNumberFrom = 0;
        Integer partnerNumberTo = 0;


        List<Partner> partners = new ArrayList<>();

        for (String parameter:searchParameters) {

            switch (index){
                case 0:
                    if (parameter.length() > 0)
                        name = parameter;
                    break;
                case 1:
                    if (parameter.length() > 0)
                        email = parameter;
                    break;
                case 2:
                    if (parameter.length() > 0)
                        partyNumberFrom = parameter;
                    break;
                case 3:
                    if (parameter.length() > 0)
                        partyNumberTo = parameter;
                    break;
            }
            index++;
        }

        if (name != null) {
            partners = partnerRepository.findByPartyName1Contains(name);
        }

        if (partyNumberFrom != null){
            partnerNumberFrom = Integer.parseInt(partyNumberFrom);
        }
        if (partyNumberTo != null){
            partnerNumberTo = Integer.parseInt(partyNumberTo);
        }


        if (partnerNumberTo == 0) {
            partnerNumberTo = partnerNumberFrom;
        }


        if (partnerNumberFrom != 0) {
            partners = partnerRepository.findByPartyNumberBetween(partnerNumberFrom,partnerNumberTo);
        }
        if (email != null){
            partners = partnerRepository.findByEmailContains(email);
        }

        return partners;
    }

    @Override
    public List<PartnerResourceByEmail> getPartnerByEmail() {

        List<PartnerResourceByEmail> partnerResourceByEmails = new ArrayList<>();

        List<Partner> partners = partnerRepository.findAll();
      //  Collections.sort(partners, Collections.reverseOrder());

        partners.sort(Comparator.comparing(Partner::getEmail));

        for (Partner partner:partners) {

            PartnerResourceByEmail partnerResourceByEmail = new PartnerResourceByEmail();
            partnerResourceByEmail.setEmail(partner.getEmail());

            String partnerString =
                    partner.getPartyName1() + " " +
                    partner.getPartyName2() + " ," +
                    partner.getAddressLine1() + "," +
                    partner.getAddressLine2() + "," +
                    partner.getCity();

            partnerResourceByEmail.setNameAndAddress(partnerString);

            partnerResourceByEmails.add(partnerResourceByEmail);

        }

        return partnerResourceByEmails;


    }

    @Override
    public List<Partner> getAllPartners() {
        List<Partner> partners = partnerRepository.findAll();
        partners.sort(Comparator.comparing(Partner::getPartyName1));
        return partners;
    }

    @Override
    public  List<Partner>  getPartnerByEmail(String email) {

        return partnerRepository.findByEmail(email);
    }

    @Override
    public PartnerResourcesOrderByAlphabet getPartnersOrderedByAlphabets() {

        PartnerResourcesOrderByAlphabet applicantGroups = new PartnerResourcesOrderByAlphabet();
        PartnerResourceByAlphabet partnerResourceByAlphabet = new PartnerResourceByAlphabet();

        partnerResourceByAlphabet = getPartnersByAlphabet("A");
        if (partnerResourceByAlphabet.getNames().size() > 0)
            applicantGroups.addPartnerOrderByAlphabet(partnerResourceByAlphabet);

        partnerResourceByAlphabet = getPartnersByAlphabet("B");
        if (partnerResourceByAlphabet.getNames().size() > 0)
            applicantGroups.addPartnerOrderByAlphabet(partnerResourceByAlphabet);

        partnerResourceByAlphabet = getPartnersByAlphabet("C");
        if (partnerResourceByAlphabet.getNames().size() > 0)
            applicantGroups.addPartnerOrderByAlphabet(partnerResourceByAlphabet);

        partnerResourceByAlphabet = getPartnersByAlphabet("D");
        if (partnerResourceByAlphabet.getNames().size() > 0)
            applicantGroups.addPartnerOrderByAlphabet(partnerResourceByAlphabet);

        partnerResourceByAlphabet = getPartnersByAlphabet("E");
        if (partnerResourceByAlphabet.getNames().size() > 0)
            applicantGroups.addPartnerOrderByAlphabet(partnerResourceByAlphabet);

        return applicantGroups;
    }


    private  PartnerResourceByAlphabet getPartnersByAlphabet(String alphabet){

        PartnerResourceByAlphabet partnerResourceByAlphabet = new PartnerResourceByAlphabet();

        // Set Alphabet
        partnerResourceByAlphabet.setLetter(alphabet);

        // Get Partners by UpperCase and LoweCase
        List<Partner> partners = partnerRepository.findByPartyName1StartingWith(alphabet);
       // List<Partner> partners1 = partnerRepository.findByPartyName1StartingWith(alphabet.toLowerCase());
        //Merge Partner
        //partners.addAll(partners1);

        List<String> partnersString = new ArrayList<String>();
        //Format Partners as String
        partnersString = formatPartnersListAsString(partners);
        //Set Partner String List
        partnerResourceByAlphabet.setNames(partnersString);
        return partnerResourceByAlphabet;
    }


    private List<String> formatPartnersListAsString (List<Partner> partners) {

        List<String> partnersString = new ArrayList<String>();

        for (Partner partner: partners) {


            String partnerString = "Bus.Partner Number:" + partner.getPartyNumber() + " ," +
                                   partner.getPartyName1() + " " +
                                   partner.getPartyName2() + " ," +
                                   partner.getEmail() + " , " +
                                   partner.getAddressLine1() + "," +
                                   partner.getAddressLine2() + "," +
                                   partner.getCity();

            partnersString.add(partnerString);

        }

        return partnersString;

    }

    @Override
    public List<Partner> getLendersIndependentEngineers() {
//        List<Partner> partners = new ArrayList<>();
//        PartnerRoleType partnerRoleType = partnerRoleTypeRepository.findByRoleCode("ZLM003");
//        partners = partnerRepository.findByPartnerRoleTypes(partnerRoleType);
//        return partners;

        return this.getPartnersByRole("ZLM003");

    }

    @Override
    public List<Partner> getLendersInsuranceAdvisors() {
        return this.getPartnersByRole("ZLM004");
    }

    @Override
    public List<Partner> getLendersLegalCounsels() {
        return this.getPartnersByRole("ZLM006");
    }

    @Override
    public List<Partner> getValuers() {
        return this.getPartnersByRole("ZLM039");
    }

    @Override
    public List<Partner> getLendersFinancialAdvisors() {
//        List<Partner> partners = new ArrayList<>();
//        PartnerRoleType partnerRoleType = partnerRoleTypeRepository.findByRoleCode("ZLM002");
//        partners = partnerRepository.findByPartnerRoleTypes(partnerRoleType);
//        return partners;

        return this.getPartnersByRole("ZLM002");

    }

    @Override
    public List<Partner> getTRAAuthorizedPersons() {
        List<Partner> partners = new ArrayList<>();
        List<PartnerRoleType> partnerRoleTypes = partnerRoleTypeRepository.findByRoleCode("BUP003");
        for (PartnerRoleType partnerRoleType: partnerRoleTypes) {
            partners.addAll(partnerRepository.findByPartnerRoleTypes(partnerRoleType));
        }
        partnerRoleTypes = partnerRoleTypeRepository.findByRoleCode("ZLM021");
        for (PartnerRoleType partnerRoleType: partnerRoleTypes) {
            partners.addAll(partnerRepository.findByPartnerRoleTypes(partnerRoleType));
        }
        return partners;
    }

    @Override
    public List<Partner> getPartnersByRoleType(String roleType) {
//        PartnerRoleType partnerRoleType = partnerRoleTypeRepository.findByRoleCode(roleType);
//        List<Partner> partners = partnerRepository.findByPartnerRoleTypes(partnerRoleType);
        return this.getPartnersByRole(roleType);

    }

    @Override
    public List<Partner> findByPartyRole(String roleType) {
        return partnerRepository.findByPartyRole(roleType);
    }

    private List<Partner> getPartnersByRole(String roleType) {

        List<Partner> partners = new ArrayList<>();
        List<PartnerRoleType> partnerRoleTypes = partnerRoleTypeRepository.findByRoleCode(roleType);
        for (PartnerRoleType partnerRoleType: partnerRoleTypes) {
            partners.addAll(partnerRepository.findByPartnerRoleTypes(partnerRoleType));
        }

        return partners;
    }

    @Override
    public Partner migratePartner(Partner partner, HttpServletRequest httpServletRequest) {

        log.info("Starting Migration of Business Partner Number :" + partner.getPartyNumber() + partner.getPartyName1());



        //Check if Partner Number exists
        if (partner.getPartyNumber() == null) {
            log.info("EXCEPTION Migration Failed: Business Partner Number is empty");
            return null;
        }

        // Check if the Partner already exists
          Partner existingPartner =  partnerRepository.findByPartyNumber(partner.getPartyNumber());
          if (existingPartner == null) {
             Partner savedPartner = partnerRepository.save(partner);
              log.info("Finished Migration of NEW Business Partner Number :" + partner.getPartyNumber() + partner.getPartyName1());

              return savedPartner;
          }

          existingPartner.setPartyCategory(partner.getPartyCategory());
          existingPartner.setGroupCompany(partner.getGroupCompany());
          existingPartner.setPartyRole(partner.getPartyRole());

          existingPartner.setEmail(partner.getEmail());

          existingPartner.setAddressLine1(partner.getAddressLine1());
          existingPartner.setAddressLine2(partner.getAddressLine2());
          existingPartner.setCity(partner.getCity());
          existingPartner.setState(partner.getState());
          existingPartner.setPostalCode(partner.getPostalCode());
          existingPartner.setCountry(partner.getCountry());

          existingPartner.setContactPersonName(partner.getContactPersonName());
          existingPartner.setContactNumber(partner.getContactNumber());
          existingPartner.setPartyRole(partner.getPartyRole());
          existingPartner.setPartyName1(partner.getPartyName1());
          existingPartner.setPartyName2(partner.getPartyName2());

          existingPartner.setIndustrySector(partner.getIndustrySector());
          existingPartner.setPan(partner.getPan());


          boolean addPartnerRole = true;

          for (PartnerRoleType partnerRoleType: partner.getPartnerRoleTypes()  ) {
              addPartnerRole = true;
              //Check if Partner Role exists
              for (PartnerRoleType partnerRoleTypeExisting: existingPartner.getPartnerRoleTypes()) {
                  if (partnerRoleType.getRoleCode() == partnerRoleTypeExisting.getRoleCode()){
                      addPartnerRole = false;
                  }
              }

              if (addPartnerRole == true) {
                  existingPartner.addPartnerRole(partnerRoleType);
              }

          }

          boolean addPartnerContact = true;


        if (partner.getPartnerContacts() != null){
            log.info("Processing CONTACT DETAILS  of NEW Business Partner Number :" + partner.getPartyNumber() + partner.getPartyName1());

            for (PartnerContact partnerContact : partner.getPartnerContacts()) {
                addPartnerContact = true;
                //Check if Partner Contact exists
                for (PartnerContact partnerContactExisting : existingPartner.getPartnerContacts()) {

                    if (partnerContact.getSerialNumber() == partnerContactExisting.getSerialNumber()) {

                        partnerContactExisting.setPrintInDemandLetter(partnerContact.getPrintInDemandLetter());
                        partnerContactExisting.setLoanContractId(partnerContact.getLoanContractId());
                        partnerContactExisting.setContactName(partnerContact.getContactName());
                        partnerContactExisting.setBranchAddress(partnerContact.getBranchAddress());
                        partnerContactExisting.setDesignation(partnerContact.getDesignation());
                        partnerContactExisting.setDepartment(partnerContact.getDepartment());
                        partnerContactExisting.setMobilePhoneNumber(partnerContact.getMobilePhoneNumber());
                        partnerContactExisting.setLandPhoneNumber(partnerContact.getLandPhoneNumber());
                        partnerContactExisting.setEmail(partnerContact.getEmail());
                        partnerContactExisting.setFaxNumber(partnerContact.getFaxNumber());
                        addPartnerContact = false;
                    }
                    if (addPartnerContact == true) {
                        existingPartner.addPartnerContact(partnerContact);
                    }
                }
            }
        }

        log.info("SAVING... NEW Business Partner Number :" + partner.getPartyNumber() + partner.getPartyName1());

        Partner updatedPartner = partnerRepository.save(existingPartner);

        log.info("Finished Migration of EXISTING Business Partner Number :" + partner.getPartyNumber() + partner.getPartyName1());

        // Create/Update User for LOAN Applicants ONLY
        this.userMaintenance(partner);

        return updatedPartner;
    }


    private void userMaintenance (Partner partner) {

        // Create User for Loan Applicants Prospect (TR0110)
        boolean loanProspectRoleExisting =
                partner.getPartnerRoleTypes().stream()
                        .anyMatch(partnerRoleType -> partnerRoleType.getRoleCode().equals("TR0110"));
        if (loanProspectRoleExisting == false) {

            // Create User for Loan Applicants - Main Loan Partner  (TR0100)
           Boolean loanPartnerRoleExisting =
                    partner.getPartnerRoleTypes().stream()
                            .anyMatch(partnerRoleType -> partnerRoleType.getRoleCode().equals("TR0100"));
            if (loanPartnerRoleExisting == false) {
                log.info("Business Partner" + partner.getPartyNumber() + " is NOT A Loan Applicant.   ");
                return;
            }
        } else {

            log.info("Business Partner" + partner.getPartyNumber() +  " is a Prospect");
        }

        log.info("Business Partner" + partner.getPartyNumber() +  " is a Loan Applicant. Therefore creating an entry in the User Table");
        // Find User By Email Id
        User existingUser  = userRepository.findByEmail(partner.getEmail());

        if  (existingUser != null) {
            existingUser.setEmail(partner.getEmail());
            existingUser.setUserName(partner.getEmail());
            existingUser.setDepartmentHead(false);
            existingUser.setFirstName(partner.getPartyName1());
            existingUser.setLastName(partner.getPartyName2());
            existingUser.setRiskDepartment("");
            existingUser.setRole("TR0100");
            existingUser.setRoleDescription("Loan Applicant");
            existingUser.setSapBPNumber(partner.getPartyNumber().toString());
            userRepository.save(existingUser);
        } else {
            User newUser = new User();
            newUser.setEmail(partner.getEmail());
            newUser.setUserName(partner.getEmail());
            newUser.setDepartmentHead(false);
            newUser.setFirstName(partner.getPartyName1());
            newUser.setLastName(partner.getPartyName2());
            newUser.setRiskDepartment("");
            newUser.setRole("TR0100");
            newUser.setRoleDescription("Loan Applicant");
            newUser.setSapBPNumber(partner.getPartyNumber().toString());
            userRepository.save(newUser);
        }


    }
}
