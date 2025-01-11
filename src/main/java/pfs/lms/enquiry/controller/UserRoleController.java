package pfs.lms.enquiry.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pfs.lms.enquiry.config.ApiController;
import pfs.lms.enquiry.domain.BankMaster;
import pfs.lms.enquiry.domain.UserRole;
import pfs.lms.enquiry.repository.BankMasterRepository;
import pfs.lms.enquiry.repository.UserRoleRepository;
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
public class UserRoleController {
    private final UserRoleRepository userRoleRepository;



    @RequestMapping(value = "/userroles", method = {RequestMethod.GET})
    public ResponseEntity<List<UserRole>> getUserRoles(HttpServletRequest request) {

        List<UserRole> userRoleList = userRoleRepository.findAll();

        return ResponseEntity.ok(userRoleList);
    }

}
