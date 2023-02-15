package pfs.lms.enquiry.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pfs.lms.enquiry.config.ApiController;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.repository.PartnerRepository;
import pfs.lms.enquiry.repository.UserRepository;
import pfs.lms.enquiry.resource.PartnerResourceByEmail;
import pfs.lms.enquiry.resource.PartnerResourcesOrderByAlphabet;
import pfs.lms.enquiry.service.impl.PartnerService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ApiController
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerRepository partnerRepository;

    private final UserRepository userRepository;

    private final PartnerService partnerService;

    @GetMapping("/me")
    public ResponseEntity getLoggedinPartner(HttpServletRequest request) {

        System.out.println("----------- Get Logged in Partner ----------");
        if (request.getUserPrincipal() != null) {
            if (request.getUserPrincipal().getName().equals("admin")) {
                System.out.println("----------- Partner Name ---------- : " + request.getUserPrincipal().getName());
                return ResponseEntity.ok(userRepository.findByEmail("admin@gmail.com"));
            } else
                return ResponseEntity.ok(userRepository.findByEmail(request.getUserPrincipal().getName()));
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/partners/byPrincipal")
    public ResponseEntity getPartnerByEmail(HttpServletRequest request) {
        if(request.getUserPrincipal().getName().equals("admin"))
            return ResponseEntity.ok(partnerRepository.findByEmail("admin@gmail.com"));
        else
            return ResponseEntity.ok(partnerRepository.findByEmail(request.getUserPrincipal().getName()));
    }

    @GetMapping("partner/queryParams")
    public ResponseEntity getPartnersByQueryParams(@RequestParam("query") String[] queryParams, HttpServletRequest httpServletRequest) {

        List<Partner> partners = partnerService.searchPartners(queryParams);

        if (partners != null){
            return ResponseEntity.ok(partners);

        }else{
            return ResponseEntity.noContent().build();
        }

    }

    @GetMapping("partners/ordered")
    public ResponseEntity getAllPartnersOrderedByAlphabets(HttpServletRequest httpServletRequest) {

        PartnerResourcesOrderByAlphabet partners = partnerService.getPartnersOrderedByAlphabets();

        if (partners != null){
            return ResponseEntity.ok(partners);

        }else{
            return ResponseEntity.noContent().build();
        }

    }

    @GetMapping("partners/byEmail")
    public ResponseEntity getAllPartnersOrderedByEmail(HttpServletRequest httpServletRequest) {

        List<PartnerResourceByEmail> partners = partnerService.getPartnerByEmail();

        if (partners != null){
            return ResponseEntity.ok(partners);

        }else{
            return ResponseEntity.noContent().build();
        }

    }

    @GetMapping("partners/all")
    public ResponseEntity<List<Partner>> getAllPartners(HttpServletRequest httpServletRequest) {

        List<Partner> partners = partnerService.getAllPartners();
        if (partners != null){
            return ResponseEntity.ok(partners);
        }
        else {
            return ResponseEntity.noContent().build();
        }
    }


    @GetMapping("partner/email")
    public ResponseEntity getPartnerByEmailId(@RequestParam("email") String email, HttpServletRequest httpServletRequest) {

        Partner partner = partnerRepository.findByUserName(email);

        if (partner != null){
            return ResponseEntity.ok(partner);

        }else{
            return ResponseEntity.noContent().build();
        }

    }

    @GetMapping("partner/partyNumber")
    public ResponseEntity getPartnerByPartyNumber(@RequestParam("partyNumber") String partyNumber, HttpServletRequest httpServletRequest) {

        Partner partner = partnerRepository.findByPartyNumber(Integer.parseInt(partyNumber));

        if (partner != null){
            return ResponseEntity.ok(partner);

        }else{
            return ResponseEntity.noContent().build();
        }

    }

    @GetMapping("partner/lias")
    public ResponseEntity getLendersInsuranceAdvisors(HttpServletRequest httpServletRequest) {

        List<Partner> partners = partnerService.getLendersIndependentEngineers();
        if (partners != null) {
            return ResponseEntity.ok(partners);
        }
        else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("partner/llcs")
    public ResponseEntity getLendersLegalCouncils(HttpServletRequest httpServletRequest) {

        List<Partner> partners = partnerService.getLendersIndependentEngineers();
        if (partners != null) {
            return ResponseEntity.ok(partners);
        }
        else {
            return ResponseEntity.noContent().build();
        }
    }

        @GetMapping("partner/lies")
    public ResponseEntity getLendersIndependentEngineers(HttpServletRequest httpServletRequest) {

        List<Partner> partners = partnerService.getLendersIndependentEngineers();
        if (partners != null) {
            return ResponseEntity.ok(partners);
        }
        else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("partner/lfas")
    public ResponseEntity getLendersFinancialAdvisors(HttpServletRequest httpServletRequest) {

        List<Partner> partners = partnerService.getLendersFinancialAdvisors();
        if (partners != null) {
            return ResponseEntity.ok(partners);
        }
        else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("partner/traAuthorizedPersons")
    public ResponseEntity getTRAAuthorizedPersons(HttpServletRequest httpServletRequest) {

        List<Partner> partners = partnerService.getTRAAuthorizedPersons();
        if (partners != null) {
            return ResponseEntity.ok(partners);
        }
        else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("partners/role/{roleType}")
    public ResponseEntity<List<Partner>> getPartnersByRole(@PathVariable String roleType,
                                                           HttpServletRequest httpServletRequest) {

        List<Partner> partners = partnerService.getPartnersByRoleType(roleType);
        if (partners != null)
            return ResponseEntity.ok(partners);
        else
            return ResponseEntity.noContent().build();
    }

    @GetMapping("partners/partyRole/{roleType}")
    public ResponseEntity<List<Partner>> findByPartyRole(@PathVariable String roleType,
                                                           HttpServletRequest httpServletRequest) {

        List<Partner> partners = partnerService.findByPartyRole(roleType);
        if (partners != null)
            return ResponseEntity.ok(partners);
        else
            return ResponseEntity.noContent().build();
    }

    @PutMapping("partner/migrate")
    public ResponseEntity migratePartner(@RequestBody Partner partner, HttpServletRequest httpServletRequest){

        partner = partnerService.migratePartner(partner,httpServletRequest);

        return  ResponseEntity.ok(partner);
    }

}
