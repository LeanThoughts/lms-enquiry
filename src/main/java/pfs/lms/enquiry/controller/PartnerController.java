package pfs.lms.enquiry.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pfs.lms.enquiry.config.ApiController;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.repository.PartnerRepository;
import pfs.lms.enquiry.repository.UserRepository;
import pfs.lms.enquiry.resource.PartnerResourceByEmail;
import pfs.lms.enquiry.resource.PartnerResourcesOrderByAlphabet;
import pfs.lms.enquiry.service.IPartnerService;
import pfs.lms.enquiry.service.impl.PartnerService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
        if(request.getUserPrincipal().getName().equals("admin")) {
            System.out.println("----------- Partner Name ---------- : " + request.getUserPrincipal().getName());
            return ResponseEntity.ok(userRepository.findByEmail("admin@gmail.com"));
        }
        else
            return ResponseEntity.ok(userRepository.findByEmail(request.getUserPrincipal().getName()));
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

        List<Partner> partners = partnerService.searchParterns(queryParams);

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
}
