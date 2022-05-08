package pfs.lms.enquiry.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pfs.lms.enquiry.domain.*;
import pfs.lms.enquiry.mail.service.LoanNotificationService;
import pfs.lms.enquiry.process.LoanApplicationEngine;
import pfs.lms.enquiry.repository.*;
import pfs.lms.enquiry.resource.*;
import pfs.lms.enquiry.service.ILoanApplicationService;
import pfs.lms.enquiry.service.ILoanContractExtensionService;
import pfs.lms.enquiry.service.ISAPIntegrationService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RepositoryRestController
@RequiredArgsConstructor
public class LoanApplicationContoller {

    private final ILoanApplicationService loanApplicationService;

    private final ISAPIntegrationService integrationService;

    private final LoanApplicationRepository loanApplicationRepository;

    private final PartnerRepository partnerRepository;

    private final UserRepository userRepository;

    private final LoanApplicationEngine engine;


    private final LoanNotificationService loanNotificationService;

    private final StateRepository stateRepository;

    private final LoanContractExtensionRepository loanContractExtensionRepository;

    private final ILoanContractExtensionService loanContractExtensionService;

    @GetMapping("/loanApplications")
    public ResponseEntity get(@RequestParam(value = "status", required = false) Integer status, HttpServletRequest request,
                              @PageableDefault(sort = "UNSORTED", size = 9999, direction = Sort.Direction.DESC) Pageable pageable)
    //Pageable pageable)
    {
        List<LoanApplication> loanApplications = new ArrayList<LoanApplication>();


        User user;
        if (request.getUserPrincipal().getName().equals("admin")) {
            user = userRepository.findByEmail("admin@gmail.com");
        } else {
            user = userRepository.findByEmail(request.getUserPrincipal().getName());
        }

        //System.out.println(" Fetching loan application by status :" + status);
        //System.out.println(" User ROLE:" + user.getRole());


        if (user.getRole().equals("ZLM023") || user.getRole().equals("ZLM013") ||
                user.getRole().equals("ZLM010")) {
            if (status == null)
                loanApplications = loanApplicationRepository.findAll(pageable).getContent();
            else
                loanApplications = loanApplicationRepository.findByFunctionalStatus(status, pageable).getContent();
        } else {
            List<Partner> partners = partnerRepository.findByEmail(user.getEmail());
            Partner partner = partners.get(0);
            if (partner != null)
                loanApplications = loanApplicationRepository.findByLoanApplicant(partner.getId(), pageable).getContent();
        }

        if (loanApplications.size() > 0) {

//           loanApplications.sort(Comparator.comparing(LoanApplication::getProjectName));
//
//            Collections.sort(loanApplications, new Comparator<LoanApplication>() {
//                @Override
//                public int compare(LoanApplication l1, LoanApplication l2) {
//                    return l1.getLoanEnquiryId().compareTo(l2.getLoanEnquiryId());
//                }
//            });
        }


        List<LoanApplicationResource> resources = new ArrayList<>(0);
        if (loanApplications.size() > 0) {

            //System.out.println(" Loan applications count :" + loanApplications.size());

            loanApplications.forEach(loanApplication -> {
                //System.out.println(" Loan Application :" + loanApplication.getLoanApplicant());

                //System.out.println(" Loan Application Applicant:" + loanApplication.getLoanApplicant());

                if (loanApplication.getLoanApplicant() != null &&
                        (loanApplication.getPostedInSAP() == null ||
                                loanApplication.getPostedInSAP() == 0 ||
                                loanApplication.getPostedInSAP() == 2)) {

                   // System.out.println("Filtering Loan Application by Status");
                   // System.out.println("Loan Number : " + loanApplication.getLoanContractId() + "   Project Name :" + loanApplication.getProjectName());
                    if (partnerRepository.findById(loanApplication.getLoanApplicant()) == null) {
                        System.out.println("Loan Applicant is   NULL for Loan : " + loanApplication.getLoanContractId());
                        Partner partner = (Partner) partnerRepository.findById(loanApplication.getLoanApplicant()).get();
                        resources.add(new LoanApplicationResource(loanApplication, partner));
                    } else {
                       // System.out.println("Loan Applicant is not NULL:" + partnerRepository.findById(loanApplication.getLoanApplicant()));
                        Partner partner = (Partner) partnerRepository.findById(loanApplication.getLoanApplicant()).get();

                        resources.add(new LoanApplicationResource(loanApplication, new Partner()));
                    }

                }

                if (loanApplication.getTechnicalStatus() != null) {
                    switch (loanApplication.getTechnicalStatus()) {
                        case 1:
                            loanApplication.setTechnicalStatusDescription("Created");
                            break;
                        case 2:
                            loanApplication.setTechnicalStatusDescription("Modified");
                            break;
                        case 3:
                            loanApplication.setTechnicalStatusDescription("Submitted");
                            break;
                        case 4:
                            loanApplication.setTechnicalStatusDescription("Taken up for Processing");
                            break;
                        case 5:
                            loanApplication.setTechnicalStatusDescription("Cancelled");
                            break;
                        case 6:
                            loanApplication.setTechnicalStatusDescription("Rejected");
                            break;
                    }
                }


            });
        }

        // Set the project location state name
        for (LoanApplicationResource loanApplicationResource : resources) {
            if (loanApplicationResource.getLoanApplication().getProjectLocationState() != null)
                if (loanApplicationResource.getLoanApplication().getProjectLocationState().length() == 2) {
                    loanApplicationResource.getLoanApplication().setProjectLocationState(
                            stateRepository.findByCode(loanApplicationResource.getLoanApplication().getProjectLocationState()).getName());
                }
        }


        return ResponseEntity.ok(resources);
    }

    @PostMapping("/loanApplications")
    public ResponseEntity add(@RequestBody LoanApplicationResource resource, HttpServletRequest request) {

//        System.out.println(resource);
//        System.out.println("LOAN APPLICATION : " + resource.getLoanApplication());
//        System.out.println("-----------------------------------------------------");
//        System.out.println("PARTNER : " + resource.getPartner());


        if (resource.getPartner().getPartyNumber() != null)
            resource.getLoanApplication().setBusPartnerNumber(resource.getPartner().getPartyNumber().toString());
        LoanApplication loanApplication = loanApplicationService.save(resource, request.getUserPrincipal().getName());

        loanNotificationService.sendSubmissionNotification(
                userRepository.findByEmail(request.getUserPrincipal().getName()),
                loanApplication, resource.getPartner());
        loanNotificationService.sendSubmissionNotificationToBDTeam(
                userRepository.findByEmail(request.getUserPrincipal().getName()),
                loanApplication, resource.getPartner());
        return ResponseEntity.ok(loanApplication);
    }


    @PostMapping("/loanApplications/migrate")
    public ResponseEntity migrate(@RequestBody LoanMigrationResource resource, HttpServletRequest request) {

        //System.out.println(resource);
        System.out.println("LOAN APPLICATION Extension: " + resource.getLoanContractExtension());
        System.out.println("LOAN APPLICATION : " + resource.getLoanApplication());
        System.out.println("PARTNER : " + resource.getPartner());

        System.out.println("-----------------------------------------------------");
        System.out.println("PARTNER : " + resource.getPartner());
        System.out.println("-----------------------------------------------------");

        LoanApplicationResource loanApplicationResource = new LoanApplicationResource();
        loanApplicationResource.setLoanApplication(resource.getLoanApplication());
        loanApplicationResource.setPartner(resource.getPartner());
        LoanApplication loanApplication = loanApplicationService.migrate(loanApplicationResource, request.getUserPrincipal().getName());


        LoanContractExtension loanContractExtension = resource.getLoanContractExtension();
        System.out.println("Loan Application GUID  : " + loanApplication.getId().toString());
        System.out.println("Loan Contract  Id      : " + loanApplication.getLoanContractId().toString());

        LoanContractExtensionResource loanContractExtensionResource = new LoanContractExtensionResource();
        loanContractExtensionResource.setLoanApplicationId(loanApplication.getId());
        loanContractExtensionResource.setLoanContractExtension(loanContractExtension);
        loanContractExtensionResource.setLoanPartners(resource.getLoanPartners());

        if (loanContractExtensionResource.getLoanContractExtension() != null) {
            System.out.println("-----------------------------------------------------");
            System.out.println("Migrating Extension : " + loanContractExtensionResource.getLoanContractExtension().toString());
            System.out.println("-----------------------------------------------------");


            LoanContractExtension existingLoanContractExtension =
                    loanContractExtensionRepository.getLoanContractExtensionByLoanNumber(loanApplication.getLoanContractId());
            if (existingLoanContractExtension == null) {
                loanContractExtensionService.save(loanContractExtensionResource, request.getUserPrincipal().getName());
            } else {
                loanContractExtensionResource.getLoanContractExtension().setId(existingLoanContractExtension.getId());
                loanContractExtensionResource.getLoanContractExtension().setLoanApplication(existingLoanContractExtension.getLoanApplication());
                loanContractExtensionService.update(loanContractExtensionResource, request.getUserPrincipal().getName());
            }
            System.out.println("-----------------------------------------------------");
            System.out.println("Finished Migrating Extension : ");
            System.out.println("-----------------------------------------------------");

        }

        return ResponseEntity.ok(loanApplication);
    }


    @PutMapping("/loanApplications/{id}")
    public ResponseEntity update(@PathVariable("id") String loanApplicationId, @RequestBody LoanApplicationResource resource, HttpServletRequest request) {


//        //Set Technical Status to 2 - "Changed"
//        //resource.getLoanApplication().setTechnicalStatus(2);
//
//        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplication().getId());
//       // Partner partner = partnerRepository.getOne(resource.getPartner().getId());
//
//        Partner partner = partnerRepository.findByEmail(resource.getPartner().getEmail());
//
//        BeanUtils.copyProperties(resource.getLoanApplication(),loanApplication,"id","enquiryNo");
//        BeanUtils.copyProperties(resource.getPartner(),partner,"id");
//        loanApplication = loanApplicationRepository.save(loanApplication);
//        partner = partnerRepository.save(partner);
//        resource.setLoanApplication(loanApplication);
//        resource.setPartner(partner);

        if (resource.getPartner().getPartyNumber() != null)
            resource.getLoanApplication().setBusPartnerNumber(resource.getPartner().getPartyNumber().toString());
        LoanApplication loanApplication = loanApplicationService.save(resource, request.getUserPrincipal().getName());

        resource.setLoanApplication(loanApplication);


        loanNotificationService.sendUpdateNotification(
                userRepository.findByEmail(request.getUserPrincipal().getName()),
                loanApplication, resource.getPartner());

        return ResponseEntity.ok(resource);
    }

    @PutMapping("/loanApplications/{id}/approve")
    public ResponseEntity approve(@PathVariable("id") String loanApplicationId,
                                  @RequestBody LoanApplicationResource resource,
                                  HttpServletRequest httpServletRequest)
            throws Exception {

        //Set Technical Status to 4 - "Approved"
        resource.getLoanApplication().setTechnicalStatus(4);
        resource.getLoanApplication().setPostedInSAP(4); //Approved but not Posted in SAP Yet
        LoanApplication loanApplication = loanApplicationService.save(resource, httpServletRequest.getUserPrincipal().getName());


        loanApplication = loanApplicationRepository.getOne(resource.getLoanApplication().getId());
        Partner partner = partnerRepository.findById(resource.getPartner().getId()).get();
//        BeanUtils.copyProperties(resource.getLoanApplication(), loanApplication,"id", "enquiryNo");
//        BeanUtils.copyProperties(resource.getPartner(), partner,"id");
//        resource = engine.onLoanApplicationApproved(LoanApplication.LoanApplicationApproved.of(loanApplication));
//        partner = partnerRepository.save(resource.getPartner());
//        resource.setLoanApplication(resource.getLoanApplication());
//        resource.setPartner(resource.getPartner());

        loanNotificationService.sendApprovalNotification(
                userRepository.findByEmail(httpServletRequest.getUserPrincipal().getName()),
                loanApplication, partner);

        return ResponseEntity.ok(resource);
    }

    @PutMapping("/loanApplications/{id}/reject")
    public ResponseEntity update(@PathVariable("id") LoanApplication loanApplication,
                                 @RequestBody EnquiryRejectReason enquiryRejectReason,
                                 HttpServletRequest request) {
        Partner partner = partnerRepository.findByUserName(request.getUserPrincipal().getName());
        loanApplication.setTechnicalStatus(6);
        loanApplication.reject(enquiryRejectReason.getRejectionCategory(), enquiryRejectReason.getRejectReason(), partner);
        loanApplication = loanApplicationRepository.save(loanApplication);

        loanNotificationService.sendRejectNotification(
                userRepository.findByEmail(request.getUserPrincipal().getName()),
                loanApplication, partner);

        return ResponseEntity.ok(loanApplication);
    }

    @PutMapping("/loanApplications/{id}/cancel")
    public ResponseEntity cancel(@PathVariable("id") LoanApplication loanApplication, HttpServletRequest request) {
        Partner partner = partnerRepository.findByUserName(request.getUserPrincipal().getName());
        // Set functional status to 9 (cancelled).
        loanApplication.setFunctionalStatus(9);
        loanApplication.setTechnicalStatus(5);

        loanApplication = loanApplicationRepository.save(loanApplication);

        loanNotificationService.sendCancelNotification(
                userRepository.findByEmail(request.getUserPrincipal().getName()),
                loanApplication, partner);

        return ResponseEntity.ok(loanApplication);
    }


    // Get Loan Application by EnquiryId - Cross Application Call
    @PutMapping("/loanApplicationEnquiryId")
    public ResponseEntity getEnquiryById(@RequestBody String id, HttpServletRequest request) {

        LoanApplicationResource loanApplicationResource = new LoanApplicationResource();

        EnquiryNo enquiryNo = new EnquiryNo();
        enquiryNo.setId(Long.parseLong(id));

        LoanApplication loanApplication = loanApplicationRepository.findByEnquiryNo(enquiryNo);

        if (loanApplication != null) {
            loanApplicationResource.setLoanApplication(loanApplication);
            loanApplicationResource.setPartner(null);
            return ResponseEntity.ok(loanApplicationResource);
        } else {
            return (ResponseEntity) ResponseEntity.notFound();
        }

    }

    // Get Loan Application by Loan Number - Cross Application Call
    @PutMapping("/loanApplicationLoanNumber")
    public ResponseEntity getEnquiryByLoanNumber(@RequestBody LoanNumberResource loanNumber, HttpServletRequest request) {

        LoanApplicationResource loanApplicationResource = new LoanApplicationResource();


        LoanApplication loanApplication = loanApplicationRepository.findByLoanContractId(loanNumber.getLoanNumber());

        if (loanApplication != null) {
            loanApplicationResource.setLoanApplication(loanApplication);
            loanApplicationResource.setPartner(null);
            return ResponseEntity.ok(loanApplicationResource);
        } else {
            return (ResponseEntity) ResponseEntity.notFound();
        }

    }

    // Get Loan Application by Loan Contract Id
    @GetMapping("/loanApplications/loanContractId/{loanContractId}")
    public ResponseEntity getEnquiryByLoanContractNumber(@PathVariable("loanContractId") String loanContractId, HttpServletRequest request) {

        LoanApplicationResource loanApplicationResource = new LoanApplicationResource();
        LoanApplication loanApplication = loanApplicationRepository.findByLoanContractId(loanContractId);
        if (loanApplication != null) {
            Partner partner = partnerRepository.findById(loanApplication.getLoanApplicant()).get();
            loanApplicationResource.setLoanApplication(loanApplication);
            loanApplicationResource.setPartner(partner);
            return ResponseEntity.ok(loanApplicationResource);
        } else {
            return (ResponseEntity) ResponseEntity.notFound();
        }
    }

    // Fetch Loan Application by Loan Number - Cross Application Call
    @RequestMapping(value = "/loanApplicationByLoanNumber", method = RequestMethod.GET,
            produces = "application/json; charset=utf-8")
    public ResponseEntity fetchEnquiryByLoanNumber(@RequestParam("loanNumber") String loanNumber, HttpServletRequest request) {

        LoanApplicationResource loanApplicationResource = new LoanApplicationResource();


        LoanApplication loanApplication = loanApplicationRepository.findByLoanContractId(loanNumber);

        if (loanApplication != null) {
            loanApplicationResource.setLoanApplication(loanApplication);
            loanApplicationResource.setPartner(null);
            return ResponseEntity.ok(loanApplicationResource);
        } else {
            return (ResponseEntity) ResponseEntity.notFound();
        }

    }

    @PutMapping("/loanApplications/search")
    public ResponseEntity search(@RequestBody SearchResource resource, HttpServletRequest request,
                                 @PageableDefault(sort = "loanContractId", size = 9999, direction = Sort.Direction.DESC) Pageable pageable) {
        //List<LoanApplication> loanApplications = new ArrayList<>(loanApplicationRepository.findAll(pageable).getContent());
        //System.out.println("Search Loans............: search resource: " + resource);
        List<LoanApplication> loanApplications = new ArrayList<>(loanApplicationService.searchLoans(request, pageable));

        //System.out.println("Loans Search Intermin Results: " + loanApplications );

        // IF state name is passed, get the code and vice versa
//        System.out.println("SEARCH LOAN APPLICATIONS : " + resource.getProjectLocationState());
//        if (resource.getProjectLocationState() != null || resource.getProjectLocationState() != "" || resource.getProjectLocationState() != " " ) {
//            if (resource.getProjectLocationState().length() == 2) {
//                resource.setProjectLocationState(stateRepository.findByCode(resource.getProjectLocationState()).getName());
//            }
//        }

        if (resource.getEnquiryDateTo() == null) {
            if (resource.getEnquiryDateFrom() != null) {
                resource.setEnquiryDateTo(resource.getEnquiryDateFrom().plusDays(1));

            }
        }

        if (resource.getEnquiryDateFrom() != null && resource.getEnquiryDateTo() != null) {
            loanApplications = loanApplications.stream()
                    .filter(loanApplication -> (
                            loanApplication.getLoanEnquiryDate().isAfter(resource.getEnquiryDateFrom()) || loanApplication.getLoanEnquiryDate().equals(resource.getEnquiryDateFrom())) &&
                            (loanApplication.getLoanEnquiryDate().isBefore(resource.getEnquiryDateTo()) || loanApplication.getLoanEnquiryDate().equals(resource.getEnquiryDateTo())))
                    .collect(Collectors
                            .toList());
        }

        if (resource.getEnquiryDateFrom() != null && resource.getEnquiryDateTo() == null) {
            loanApplications = loanApplications.stream()
                    .filter(loanApplication -> (
                            loanApplication.getLoanEnquiryDate().isEqual(resource.getEnquiryDateFrom())))
                    .collect(Collectors
                            .toList());
        }

        if (resource.getEnquiryNoFrom() != null)
            loanApplications = loanApplications.stream().filter(loanApplication -> loanApplication.getEnquiryNo().getId() >=
                    resource.getEnquiryNoFrom()).collect(Collectors.toList());

        if (resource.getEnquiryNoTo() != null)
            loanApplications = loanApplications.stream().filter(loanApplication -> loanApplication.getEnquiryNo().getId() <=
                    resource.getEnquiryNoTo()).collect(Collectors.toList());

        if (resource.getLoanNumberFrom() != null && resource.getLoanNumberTo() == null) {
            loanApplications = loanApplications.stream().filter(loanApplication -> loanApplication.getLoanContractId() != null
                    && !loanApplication.getLoanContractId().isEmpty())
                    .filter(loanApplication -> loanApplication.getLoanContractId().contains(resource.getLoanNumberFrom() + "")).collect(Collectors.toList());
        } else if (resource.getLoanNumberFrom() == null && resource.getLoanNumberTo() != null) {
            loanApplications = loanApplications.stream().filter(loanApplication -> loanApplication.getLoanContractId() != null
                    && !loanApplication.getLoanContractId().isEmpty())
                    .filter(loanApplication -> loanApplication.getLoanContractId().contains(resource.getLoanNumberTo() + "")).collect(Collectors.toList());
        } else if (resource.getLoanNumberFrom() != null && resource.getLoanNumberTo() != null) {
            loanApplications = loanApplications.stream().filter(loanApplication -> loanApplication.getLoanContractId() != null && !loanApplication.getLoanContractId().isEmpty()).filter(loanApplication -> new Integer(loanApplication.getLoanContractId()) >=
                    resource.getLoanNumberFrom()).collect(Collectors.toList());
            loanApplications = loanApplications.stream().filter(loanApplication -> loanApplication.getLoanContractId() != null && !loanApplication.getLoanContractId().isEmpty()).filter(loanApplication -> new Integer(loanApplication.getLoanContractId()) <=
                    resource.getLoanNumberTo()).collect(Collectors.toList());
        }

        if (resource.getPartyName() != null)
            loanApplications = loanApplications.stream().filter(loanApplication -> loanApplication.getProjectName().toLowerCase().contains(resource.getPartyName().toLowerCase())).collect(Collectors.toList());

        if (resource.getLoanClass() != null)
            loanApplications = loanApplications.stream().filter(loanApplication -> loanApplication.getLoanClass().equals(resource.getLoanClass())).collect(Collectors.toList());

        if (resource.getFinancingType() != null)
            loanApplications = loanApplications.stream().filter(loanApplication -> loanApplication.getFinancingType().equals(resource.getFinancingType())).collect(Collectors.toList());

        if (resource.getProjectLocationState() != null)
            loanApplications = loanApplications.stream().filter(loanApplication -> loanApplication.getProjectLocationState().equals(resource.getProjectLocationState())).collect(Collectors.toList());

        if (resource.getProjectType() != null)
            loanApplications = loanApplications.stream().filter(loanApplication -> loanApplication.getProjectType().equals(resource.getProjectType())).collect(Collectors.toList());

        if (resource.getAssistanceType() != null)
            loanApplications = loanApplications.stream().filter(loanApplication ->
                    loanApplication.getAssistanceType()
                            .equals(resource.getAssistanceType()))
                    .collect(Collectors.toList());

        if (resource.getRating() != null)
            loanApplications = loanApplications.stream().filter(loanApplication ->
                    loanApplication.getRating().equals(resource.getRating()))
                    .collect(Collectors.toList());


        if (resource.getTechnicalStatus() != null)
            loanApplications = loanApplications
                    .stream()
                    .filter(
                            loanApplication ->
                                    loanApplication.getTechnicalStatus()
                                            == Integer.parseInt(resource.getTechnicalStatus()))
                    .collect(Collectors.toList());

        User user;
        if (request.getUserPrincipal().getName().equals("admin")) {
            user = userRepository.findByEmail("admin@gmail.com");
        } else {
            user = userRepository.findByEmail(request.getUserPrincipal().getName());
        }

        if (user.getRole().equals("TR0100")) {
            List<LoanApplication> loanApplicationsForPartner = new ArrayList<>();
            List<Partner> partners = partnerRepository.findByEmail(user.getEmail());
            for (Partner partner : partners) {
                if (partner != null) {
                    loanApplicationsForPartner = loanApplications.stream().filter(loanApplication -> loanApplication.getLoanApplicant()
                            .equals(partner.getId())).collect(Collectors.toList());
                    loanApplications.addAll(loanApplicationsForPartner);
                }
            }
        }

        List<LoanApplicationResource> resources = new ArrayList<>(0);


        // System.out.println("-------------- Loans Applications Count :" + loanApplications.size());

        loanApplications.forEach(loanApplication -> {
            //System.out.println("Loan Contract Id     : " + loanApplication.getLoanContractId());
            //System.out.println("Applicant Details    : " + loanApplication.getLoanApplicant());

            if (loanApplication.getLoanApplicant() != null) {
                Partner partner = partnerRepository.findById(loanApplication.getLoanApplicant()).get();
                if (partner == null) {
                    //System.out.println("-------------- Partner is null for loan :" + loanApplication.getLoanContractId());
                }
                if (loanApplication.getTechnicalStatus() != null) {
                    switch (loanApplication.getTechnicalStatus()) {
                        case 1:
                            loanApplication.setTechnicalStatusDescription("Created");
                            break;
                        case 2:
                            loanApplication.setTechnicalStatusDescription("Modified");
                            break;
                        case 3:
                            loanApplication.setTechnicalStatusDescription("Submitted");
                            break;
                        case 4:
                            loanApplication.setTechnicalStatusDescription("Taken up for Processing");
                            break;
                        case 5:
                            loanApplication.setTechnicalStatusDescription("Cancelled");
                            break;
                        case 6:
                            loanApplication.setTechnicalStatusDescription("Rejected");
                            break;
                    }
                }

                resources.add(new LoanApplicationResource(loanApplication, partner));
            }
        });

        // Set the project location state name
        for (LoanApplicationResource loanApplicationResource : resources) {
            if (loanApplicationResource.getLoanApplication().getProjectLocationState() != null)
                System.out.println("Loan Number : " + loanApplicationResource.getLoanApplication().getLoanContractId());
            System.out.println("State       : " + loanApplicationResource.getLoanApplication().getProjectLocationState());

            if (loanApplicationResource.getLoanApplication().getProjectLocationState().length() == 2) {
                loanApplicationResource.getLoanApplication().setProjectLocationState(
                        stateRepository.findByCode(loanApplicationResource.getLoanApplication().getProjectLocationState()).getName());
            }

        }


        //System.out.println("-------------- All Loans Prepareed...... Ready for Return............. ");

        return ResponseEntity.ok(resources);
    }


    @PutMapping("/loanApplications/{id}/updateStatus")
    public ResponseEntity approve(@PathVariable("id") Long enquiryNo, @RequestParam("status") Integer status,
                                  @RequestParam("amount") Double amount) throws Exception {
        EnquiryNo enqNo = new EnquiryNo();
        enqNo.setId(enquiryNo);
        LoanApplication loanApplication = loanApplicationRepository.findByEnquiryNo(enqNo);
        loanApplication.updateStatusFromSAP(status, amount);
        loanApplication = loanApplicationRepository.save(loanApplication);
        return ResponseEntity.ok(loanApplication);
    }

    @PutMapping("/loanApplications/loanContracts/search")
    public ResponseEntity search(@RequestBody LoanContractSearchResource resource, HttpServletRequest request,
                                 @PageableDefault(sort = "loanContractId", size = 9999, direction = Sort.Direction.DESC) Pageable pageable) {
        List<LoanApplication> loanApplications = new ArrayList<>(loanApplicationService.searchLoans(request, pageable));


        if (resource.getBorrowerCodeFrom() != null && resource.getBorrowerCodeTo() == null) {
            loanApplications = loanApplications.stream().filter(loanApplication -> loanApplication.getbusPartnerNumber() != null
                    && !loanApplication.getbusPartnerNumber().isEmpty())
                    .filter(loanApplication -> loanApplication.getbusPartnerNumber().contains(resource.getBorrowerCodeFrom() + "")).collect(Collectors.toList());
        } else if (resource.getBorrowerCodeFrom() == null && resource.getBorrowerCodeTo() != null) {
            loanApplications = loanApplications.stream().filter(loanApplication -> loanApplication.getbusPartnerNumber() != null
                    && !loanApplication.getbusPartnerNumber().isEmpty())
                    .filter(loanApplication -> loanApplication.getbusPartnerNumber().contains(resource.getBorrowerCodeTo() + "")).collect(Collectors.toList());
        } else if (resource.getBorrowerCodeFrom() != null && resource.getBorrowerCodeTo() != null) {
            loanApplications = loanApplications.stream().filter(loanApplication -> loanApplication.getbusPartnerNumber() != null && !loanApplication.getbusPartnerNumber().isEmpty()).filter(loanApplication -> new Integer(loanApplication.getbusPartnerNumber()) >=
                    resource.getBorrowerCodeFrom()).collect(Collectors.toList());
            loanApplications = loanApplications.stream().filter(loanApplication -> loanApplication.getbusPartnerNumber() != null && !loanApplication.getbusPartnerNumber().isEmpty()).filter(loanApplication -> new Integer(loanApplication.getbusPartnerNumber()) <=
                    resource.getBorrowerCodeTo()).collect(Collectors.toList());
        }

        if (resource.getLoanNumberFrom() != null && resource.getLoanNumberTo() == null) {
            loanApplications = loanApplications.stream().filter(loanApplication -> loanApplication.getLoanContractId() != null
                    && !loanApplication.getLoanContractId().isEmpty())
                    .filter(loanApplication -> loanApplication.getLoanContractId().contains(resource.getLoanNumberFrom() + "")).collect(Collectors.toList());
        } else if (resource.getLoanNumberFrom() == null && resource.getLoanNumberTo() != null) {
            loanApplications = loanApplications.stream().filter(loanApplication -> loanApplication.getLoanContractId() != null
                    && !loanApplication.getLoanContractId().isEmpty())
                    .filter(loanApplication -> loanApplication.getLoanContractId().contains(resource.getLoanNumberTo() + "")).collect(Collectors.toList());
        } else if (resource.getLoanNumberFrom() != null && resource.getLoanNumberTo() != null) {
            loanApplications = loanApplications.stream().filter(loanApplication -> loanApplication.getLoanContractId() != null && !loanApplication.getLoanContractId().isEmpty()).filter(loanApplication -> new Integer(loanApplication.getLoanContractId()) >=
                    resource.getLoanNumberFrom()).collect(Collectors.toList());
            loanApplications = loanApplications.stream().filter(loanApplication -> loanApplication.getLoanContractId() != null && !loanApplication.getLoanContractId().isEmpty()).filter(loanApplication -> new Integer(loanApplication.getLoanContractId()) <=
                    resource.getLoanNumberTo()).collect(Collectors.toList());
        }

        if (resource.getPartyName() != null)
            loanApplications = loanApplications.stream().filter(loanApplication -> loanApplication.getProjectName().toLowerCase().contains(resource.getPartyName().toLowerCase())).collect(Collectors.toList());

        if (resource.getLoanClass() != null)
            loanApplications = loanApplications.stream().filter(loanApplication -> loanApplication.getLoanClass().equals(resource.getLoanClass())).collect(Collectors.toList());

        if (resource.getFinancingType() != null)
            loanApplications = loanApplications.stream().filter(loanApplication -> loanApplication.getFinancingType().equals(resource.getFinancingType())).collect(Collectors.toList());

        if (resource.getProjectLocationState() != null)
            loanApplications = loanApplications.stream().filter(loanApplication -> loanApplication.getProjectLocationState().equals(resource.getProjectLocationState())).collect(Collectors.toList());

        if (resource.getProjectType() != null)
            loanApplications = loanApplications.stream().filter(loanApplication -> loanApplication.getProjectType().equals(resource.getProjectType())).collect(Collectors.toList());

        if (resource.getAssistanceType() != null)
            loanApplications = loanApplications.stream().filter(loanApplication ->
                    loanApplication.getAssistanceType()
                            .equals(resource.getAssistanceType()))
                    .collect(Collectors.toList());


        if (resource.getTechnicalStatus() != null)
            loanApplications = loanApplications
                    .stream()
                    .filter(
                            loanApplication ->
                                    loanApplication.getTechnicalStatus()
                                            == Integer.parseInt(resource.getTechnicalStatus()))
                    .collect(Collectors.toList());

        if (resource.getAccountStatus() != null)
            loanApplications = loanApplications.stream().filter(loanApplication ->
                    loanApplication.getFunctionalStatus() ==
                            Integer.parseInt(resource.getAccountStatus())).
                    collect(Collectors.toList());

        User user;
        if (request.getUserPrincipal().getName().equals("admin")) {
            user = userRepository.findByEmail("admin@gmail.com");
        } else {
            user = userRepository.findByEmail(request.getUserPrincipal().getName());
        }

        if (user.getRole().equals("TR0100")) {
            List<LoanApplication> loanApplicationsForPartner = new ArrayList<>();
            List<Partner> partners = partnerRepository.findByEmail(user.getEmail());
            for (Partner partner : partners) {
                if (partner != null) {
                    loanApplicationsForPartner = loanApplications.stream().filter(loanApplication -> loanApplication.getLoanApplicant()
                            .equals(partner.getId())).collect(Collectors.toList());
                    loanApplications.addAll(loanApplicationsForPartner);
                }
            }
        }

        List<LoanApplicationResource> resources = new ArrayList<>(0);

        loanApplications.forEach(loanApplication -> {

            if (loanApplication.getLoanApplicant() != null) {
                Partner partner = partnerRepository.findById(loanApplication.getLoanApplicant()).get();

                if (loanApplication.getTechnicalStatus() != null) {
                    switch (loanApplication.getTechnicalStatus()) {
                        case 1:
                            loanApplication.setTechnicalStatusDescription("Created");
                            break;
                        case 2:
                            loanApplication.setTechnicalStatusDescription("Modified");
                            break;
                        case 3:
                            loanApplication.setTechnicalStatusDescription("Submitted");
                            break;
                        case 4:
                            loanApplication.setTechnicalStatusDescription("Taken up for Processing");
                            break;
                        case 5:
                            loanApplication.setTechnicalStatusDescription("Cancelled");
                            break;
                        case 6:
                            loanApplication.setTechnicalStatusDescription("Rejected");
                            break;
                    }
                }

                resources.add(new LoanApplicationResource(loanApplication, partner));
            }
        });

        for (LoanApplicationResource loanApplicationResource : resources) {
            if (loanApplicationResource.getLoanApplication().getProjectLocationState() != null)
                if (loanApplicationResource.getLoanApplication().getProjectLocationState().length() == 2) {
                    loanApplicationResource.getLoanApplication().setProjectLocationState(
                            stateRepository.findByCode(loanApplicationResource.getLoanApplication().getProjectLocationState()).getName());
                }
        }

        return ResponseEntity.ok(resources);
    }

}
