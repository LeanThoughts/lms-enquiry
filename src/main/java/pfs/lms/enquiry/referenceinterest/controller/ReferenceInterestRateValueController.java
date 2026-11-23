package pfs.lms.enquiry.referenceinterest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pfs.lms.enquiry.config.ApiController;
import pfs.lms.enquiry.referenceinterest.domain.ReferenceInterestRateValue;
import pfs.lms.enquiry.referenceinterest.repository.ReferenceInterestRateValueRepository;
import pfs.lms.enquiry.referenceinterest.resource.ReferenceInterestValueResource;
import pfs.lms.enquiry.referenceinterest.service.IReferenceInterestRateValueService;

import javax.servlet.http.HttpServletRequest;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@ApiController
@RequiredArgsConstructor
@Slf4j
public class ReferenceInterestRateValueController {

    private final ReferenceInterestRateValueRepository referenceInterestRateValueRepository;
    private final IReferenceInterestRateValueService referenceInterestRateValueService;

    @PreAuthorize("hasAuthority('ReferenceInterestRates.Display')")
    @GetMapping("/referenceInterestRateValues/referenceInterestRateType/{referenceInterestRateId}")
    public ResponseEntity<List<ReferenceInterestRateValue>> getReferenceInterestRateValues(@PathVariable Long referenceInterestRateId) {

        List<ReferenceInterestRateValue> referenceInterestRateValues = referenceInterestRateValueRepository.
                findByReferenceInterestRateIdOrderByValidFromDate(referenceInterestRateId);
        return ResponseEntity.ok(referenceInterestRateValues);
    }

    @PreAuthorize("hasAuthority('ReferenceInterestRates.Edit')")
    @PostMapping("/referenceInterestRateValues/create")
    public ResponseEntity<ReferenceInterestRateValue> create(
            @RequestBody ReferenceInterestValueResource referenceInterestValueResource,
            HttpServletRequest request) throws Exception {
        ReferenceInterestRateValue referenceInterestRateValue =
                referenceInterestRateValueService.create(referenceInterestValueResource, request.getUserPrincipal().getName());
        if (referenceInterestRateValue == null)
            throw new RuntimeException("Data already exists for the date " +
                    referenceInterestValueResource.getValidFromDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                    " !");
        else
            return ResponseEntity.ok(referenceInterestRateValue);
    }

    @PreAuthorize("hasAuthority('ReferenceInterestRates.Edit')")
    @PutMapping("/referenceInterestRateValues/update")
    public ResponseEntity<ReferenceInterestRateValue> update(
            @RequestBody ReferenceInterestValueResource referenceInterestValueResource,
            HttpServletRequest request) throws Exception {

        ReferenceInterestRateValue referenceInterestRateValue =
                referenceInterestRateValueService.update(referenceInterestValueResource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(referenceInterestRateValue);
    }

    @PreAuthorize("hasAuthority('ReferenceInterestRates.Edit')")
    @DeleteMapping("/referenceInterestRateValues/delete/{referenceInterestValueId}")
    public void delete(
            @PathVariable UUID referenceInterestValueId,
            HttpServletRequest request) throws Exception {

        referenceInterestRateValueService.delete(referenceInterestValueId, request.getUserPrincipal().getName());
    }
}
