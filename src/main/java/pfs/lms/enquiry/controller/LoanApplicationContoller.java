package pfs.lms.enquiry.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import pfs.lms.enquiry.domain.*;
import pfs.lms.enquiry.mail.service.LoanNotificationService;
import pfs.lms.enquiry.process.LoanApplicationEngine;
import pfs.lms.enquiry.reports.EnquiryReportExcel;
import pfs.lms.enquiry.reports.EnquiryReportExcelV1;
import pfs.lms.enquiry.reports.EnquiryReportPDF;
import pfs.lms.enquiry.repository.*;
import pfs.lms.enquiry.resource.*;
import pfs.lms.enquiry.service.ILoanApplicationService;
import pfs.lms.enquiry.service.ILoanContractExtensionService;
import pfs.lms.enquiry.service.ISAPIntegrationService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
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

    private final LoanClassRepository loanClassRepository;
    private final ProjectTypeRepository projectTypeRepository;
    private final FinancingTypeRepository financingTypeRepository;

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
//            Collections.sort(loanApplications, new Comparator<LoanApplication>() {
//                public int compare(LoanApplication l1, LoanApplication l2) {
//
//                         return l1.getLoanEnquiryDate().compareTo(l2.getLoanEnquiryDate());
//
//                }
//            });


        }


        List<LoanApplicationResource> resources = new ArrayList<>(0);
        if (loanApplications.size() > 0) {
            loanApplications.forEach(loanApplication -> {

                if (loanApplication.getLoanApplicant() != null &&
                        (loanApplication.getPostedInSAP() == null ||
                                loanApplication.getPostedInSAP() == 0 ||
                                loanApplication.getPostedInSAP() == 2)) {

                    //log.info("Filtering Loan Application by Status");
                    //log.info("Loan Number : " + loanApplication.getLoanContractId() + "   Project Name :" + loanApplication.getProjectName());
                    if (partnerRepository.findById(loanApplication.getLoanApplicant()) == null) {
                        log.info("Loan Applicant is   NULL for Loan : " + loanApplication.getLoanContractId());
                        Partner partner = (Partner) partnerRepository.findById(loanApplication.getLoanApplicant()).get();

                        LoanApplicationResource loanApplicationResource = new LoanApplicationResource(loanApplication, partner, "", "", "", null,null);
                        loanApplicationResource = fetchAttributeDescriptions(loanApplicationResource);
                        resources.add(loanApplicationResource);

                    } else {
                        //log.info("Loan Applicant is not NULL:" + partnerRepository.findById(loanApplication.getLoanApplicant()));
                        Partner partner = (Partner) partnerRepository.findById(loanApplication.getLoanApplicant()).get();

                        LoanApplicationResource loanApplicationResource = new LoanApplicationResource(loanApplication, partner, "", "", "",null,null);
                        loanApplicationResource = fetchAttributeDescriptions(loanApplicationResource);

                        resources.add(loanApplicationResource);
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
    public ResponseEntity add(@RequestBody LoanApplicationResource resource, HttpServletRequest request) throws InterruptedException {


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
    public ResponseEntity migrate(@RequestBody LoanMigrationResource resource, HttpServletRequest request) throws InterruptedException, CloneNotSupportedException {

        log.info("LOAN APPLICATION Extension: " + resource.getLoanContractExtension());
        log.info("LOAN APPLICATION : " + resource.getLoanApplication());
        log.info("PARTNER : " + resource.getPartner());

        log.info("-----------------------------------------------------");
        log.info("PARTNER : " + resource.getPartner());
        log.info("-----------------------------------------------------");

        LoanApplicationResource loanApplicationResource = new LoanApplicationResource();
        loanApplicationResource.setLoanApplication(resource.getLoanApplication());
        loanApplicationResource.setPartner(resource.getPartner());
        loanApplicationResource.setMainLocationDetail(resource.getMainLocationDetail());
        loanApplicationResource.setSubLocationDetailList(resource.getSubLocationDetailList());
        LoanApplication loanApplication = loanApplicationService.migrate(loanApplicationResource, request.getUserPrincipal().getName());


        LoanContractExtension loanContractExtension = resource.getLoanContractExtension();
        log.info("Loan Application GUID  : " + loanApplication.getId().toString());
        log.info("Loan Contract  Id      : " + loanApplication.getLoanContractId().toString());


        LoanContractExtensionResource loanContractExtensionResource = new LoanContractExtensionResource();
        loanContractExtensionResource.setLoanApplicationId(loanApplication.getId());
        loanContractExtensionResource.setLoanContractExtension(loanContractExtension);
        loanContractExtensionResource.setLoanPartners(resource.getLoanPartners());

        if (loanContractExtensionResource.getLoanContractExtension() != null) {
            log.info("-----------------------------------------------------");
            log.info("Migrating Extension : " + loanContractExtensionResource.getLoanContractExtension().toString());
            log.info("-----------------------------------------------------");

            LoanContractExtension existingLoanContractExtension = new LoanContractExtension();

            try {
                  existingLoanContractExtension =
                        loanContractExtensionRepository.getLoanContractExtensionByLoanNumber(loanApplication.getLoanContractId());
            } catch ( Exception ex) {
                log.error ("Error getting loan contract extension by contract id");
            }

            if (existingLoanContractExtension == null) {
                log.info("Loan Contract Extension Not Found - Creating New Loan Contract Extension for contract : " + loanApplication.getLoanContractId());
                loanContractExtensionService.save(loanContractExtensionResource, request.getUserPrincipal().getName());

            } else {
                log.info("Loan Contract Extension  Found - Updating   Loan Contract Extension : " + loanApplication.getLoanContractId());

                loanContractExtensionResource.getLoanContractExtension().setId(existingLoanContractExtension.getId());
                loanContractExtensionResource.getLoanContractExtension().setLoanApplication(existingLoanContractExtension.getLoanApplication());
                loanContractExtensionService.update(loanContractExtensionResource, request.getUserPrincipal().getName());
            }
            log.info("-----------------------------------------------------");
            log.info("Finished Migrating Extension : ");
            log.info("-----------------------------------------------------");
        }
        return ResponseEntity.ok(loanApplication);
    }


    @PutMapping("/loanApplications/{id}")
    public ResponseEntity update(@PathVariable("id") String loanApplicationId, @RequestBody LoanApplicationResource resource, HttpServletRequest request) throws InterruptedException {

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

        List<LoanApplicationResource> resources = new ArrayList<>(0);
        resources = this.searchLoanApplications(resource, request, pageable);

        return ResponseEntity.ok(resources);
    }

    @GetMapping(value = "/loanApplications/search/excel")
    public void searchAndGenerateExcel(
            HttpServletResponse response,
            @RequestParam(required = false) String enquiryDateFrom,
            @RequestParam(required = false) String enquiryDateTo,
            @RequestParam(required = false) Integer enquiryNoFrom,
            @RequestParam(required = false) Integer enquiryNoTo,
            @RequestParam(required = false) String partyName,
            @RequestParam(required = false) String projectLocationState,
            @RequestParam(required = false) String loanClass,
            @RequestParam(required = false) String projectType,
            @RequestParam(required = false) String financingType,
            @RequestParam(required = false) String assistanceType,
            @RequestParam(required = false) String technicalStatus,
            @RequestParam(required = false) String rating,
            @PageableDefault(sort = "loanContractId", size = 9999, direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request) throws IOException, ParseException {

        SearchResource resource = this.buildSearchResource(  enquiryDateFrom,  enquiryDateTo,  enquiryNoFrom,  enquiryNoTo,   partyName,
                  projectLocationState,   loanClass,   projectType,   financingType,   assistanceType,
                  technicalStatus,   rating);

        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=LoanEnquiryListReport_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);


        List<LoanApplicationResource> resources = new ArrayList<>(0);
        resources = this.searchLoanApplications(resource, request, pageable);

        EnquiryReportExcelV1 enquiryReportExcel = new EnquiryReportExcelV1(resources);

        SXSSFWorkbook sxssfWorkbook = enquiryReportExcel.exportSXSSWorkBook(response);

    }



    @GetMapping(value = "/loanApplications/search/pdf" )
    public ResponseEntity searchAndGeneratePDF (
            @RequestParam(required = false) String enquiryDateFrom,
            @RequestParam(required = false) String enquiryDateTo,
            @RequestParam(required = false) Integer enquiryNoFrom,
            @RequestParam(required = false) Integer enquiryNoTo,
            @RequestParam(required = false) String partyName,
            @RequestParam(required = false) String projectLocationState,
            @RequestParam(required = false) String loanClass,
            @RequestParam(required = false) String projectType,
            @RequestParam(required = false) String financingType,
            @RequestParam(required = false) String assistanceType,
            @RequestParam(required = false) String technicalStatus,
            @RequestParam(required = false) String rating,
            @PageableDefault(sort = "loanContractId", size = 9999, direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request) throws Exception {

        SearchResource resource = this.buildSearchResource(  enquiryDateFrom,  enquiryDateTo,  enquiryNoFrom,  enquiryNoTo,   partyName,
                projectLocationState,   loanClass,   projectType,   financingType,   assistanceType,
                technicalStatus,   rating);

        List<LoanApplicationResource> resources = new ArrayList<>(0);
        resources = this.searchLoanApplications(resource, request, pageable);

        EnquiryReportPDF enquiryReportPDF = new EnquiryReportPDF();

        ByteArrayOutputStream stream = enquiryReportPDF.buildPdfDocument(resources);
        return streamToResponseEntity(stream);
    }

    public ResponseEntity streamToResponseEntity(ByteArrayOutputStream stream  ){

        HttpHeaders responseHeader = new HttpHeaders();
        responseHeader.setContentType(MediaType.APPLICATION_PDF);
        responseHeader.add("Expires", "0");
        //responseHeader.add("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        responseHeader.add("Pragma", "no-cache");

        String fileName  = "LoanEnquiryReport_" + LocalDate.now().toString() ;
        responseHeader.add("Content-disposition", "inline; filename=" + fileName + ".pdf");

        return new ResponseEntity(stream.toByteArray(), responseHeader, HttpStatus.OK);
    }

    @ResponseBody
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public String handleHttpMediaTypeNotAcceptableException() {
        return "acceptable MIME type:" + MediaType.APPLICATION_PDF;
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


        List<LoanApplicationResource> resources = new ArrayList<>(0);
        resources = this.searchLoanContracts(resource, request, pageable);

        return ResponseEntity.ok(resources);
    }


    private List<LoanApplicationResource> searchLoanApplications(@RequestBody SearchResource resource, HttpServletRequest request,
                                                                 @PageableDefault(sort = "loanContractId", size = 9999, direction = Sort.Direction.DESC) Pageable pageable) {

        List<LoanApplication> loanApplications = new ArrayList<>(loanApplicationService.searchLoans(request, pageable));

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


        loanApplications.forEach(loanApplication -> {
            Partner partner = new Partner();
            if (loanApplication.getLoanApplicant() != null) {
                try {
                      partner = partnerRepository.findById(loanApplication.getLoanApplicant()).get();
                    if (partner == null) {
                        //System.out.println("-------------- Partner is null for loan :" + loanApplication.getLoanContractId());
                    }
                } catch (Exception x) {
                    log.info("Exception in Loan Application Controller 658: Partner find by Id: " + loanApplication.getbusPartnerNumber());
                    log.info("Exception in Loan Application Controller 658: Loan Number : " + loanApplication.getLoanContractId());
                    return;
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
                LoanApplicationResource loanApplicationResource = new LoanApplicationResource(loanApplication, partner, "", "", "",null,null);
                loanApplicationResource = fetchAttributeDescriptions(loanApplicationResource);

                resources.add(loanApplicationResource);

            }
        });

        // Set the project location state name
        for (LoanApplicationResource loanApplicationResource : resources) {

            if (loanApplicationResource.getLoanApplication().getProjectCapacityUnit() == null) {
                loanApplicationResource.getLoanApplication().setProjectCapacityUnit("");
            }

            if (loanApplicationResource.getLoanApplication().getProjectLocationState() != null)
//                 log.info("Loan Number : " + loanApplicationResource.getLoanApplication().getLoanContractId());
//                 log.info("State       : " + loanApplicationResource.getLoanApplication().getProjectLocationState());
                if (loanApplicationResource.getLoanApplication().getProjectLocationState().length() == 2) {
                    State state = stateRepository.findByCode(loanApplicationResource.getLoanApplication().getProjectLocationState());
                    if (state != null) {
                        loanApplicationResource.getLoanApplication().setProjectLocationState(state.getName());
                    }
                }

        }

        return resources;
    }

    private List<LoanApplicationResource> searchLoanContracts(LoanContractSearchResource resource, HttpServletRequest request, Pageable pageable) {


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

                LoanApplicationResource loanApplicationResource = new LoanApplicationResource(loanApplication, partner, "", "", "",null,null);
                loanApplicationResource = fetchAttributeDescriptions(loanApplicationResource);

                resources.add(loanApplicationResource);
            }
        });

        for (LoanApplicationResource loanApplicationResource : resources) {
            if (loanApplicationResource.getLoanApplication().getProjectLocationState() != null)
                if (loanApplicationResource.getLoanApplication().getProjectLocationState().length() == 2) {
                    loanApplicationResource.getLoanApplication().setProjectLocationState(
                            stateRepository.findByCode(loanApplicationResource.getLoanApplication().getProjectLocationState()).getName());
                }
        }

        return resources;
    }

    private LoanApplicationResource fetchAttributeDescriptions(LoanApplicationResource resource) {
        LoanApplication loanApplication = resource.getLoanApplication();
        String projectTypeDesc = "";
        String financingTypeDesc = "";
        String loanClassDesc = "";
        try {
            if (loanApplication.getProjectType() != null || loanApplication.getProjectType().length() > 0) {
                projectTypeDesc = projectTypeRepository.findByCode(loanApplication.getProjectType()).getValue();
            }
            if (loanApplication.getFinancingType() != null || loanApplication.getFinancingType().length() > 0) {
                financingTypeDesc = financingTypeRepository.findByCode(loanApplication.getFinancingType()).getValue();
            }
            if (loanApplication.getLoanClass() != null || loanApplication.getLoanClass().length() > 0) {
                loanClassDesc = loanClassRepository.findByCode(loanApplication.getLoanClass()).getValue();
            }

            resource.setProjectTypeDesc(projectTypeDesc);
            resource.setFinancingTypeDesc(financingTypeDesc);
            resource.setLoanClassDesc(loanClassDesc);
            return resource;
        } catch ( Exception exception) {
            return resource;
        }
    }

    private SearchResource buildSearchResource ( String enquiryDateFrom,String enquiryDateTo,Integer enquiryNoFrom,Integer enquiryNoTo, String partyName,
                                                 String projectLocationState, String loanClass, String projectType, String financingType, String assistanceType,
                                                 String technicalStatus, String rating) {
        SearchResource resource = new SearchResource();
        LocalDate enquiryFromDate;
        LocalDate enquiryToDate;
        String dateStringFrom = "";
        String dateStringTo = "";

        if (enquiryDateFrom != null) {
             dateStringFrom = enquiryDateFrom.substring(8, 10) + "-" + enquiryDateFrom.substring(4, 7) + "-" + enquiryDateFrom.substring(11, 15);
            enquiryFromDate = LocalDate.parse(dateStringFrom, DateTimeFormatter.ofPattern("d-MMM-yyyy"));
            resource.setEnquiryDateFrom(enquiryFromDate);

        }
        if (enquiryDateTo != null) {
             dateStringTo = enquiryDateTo.substring(8, 10) + "-" + enquiryDateTo.substring(4, 7) + "-" + enquiryDateTo.substring(11, 15);
            enquiryToDate = LocalDate.parse(dateStringTo, DateTimeFormatter.ofPattern("d-MMM-yyyy"));
            resource.setEnquiryDateTo(enquiryToDate);

        }

        resource.setEnquiryNoFrom(enquiryNoFrom);
        resource.setEnquiryNoTo(enquiryNoTo);
        resource.setPartyName(partyName);
        resource.setProjectLocationState(projectLocationState);
        resource.setLoanClass(loanClass);
        resource.setProjectType(projectType);
        resource.setFinancingType(financingType);
        resource.setAssistanceType(assistanceType);
        resource.setTechnicalStatus(technicalStatus);
        resource.setRating(rating);
        return resource;
    }
}
