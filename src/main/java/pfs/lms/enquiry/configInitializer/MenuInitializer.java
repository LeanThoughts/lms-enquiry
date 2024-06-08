package pfs.lms.enquiry.configInitializer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.domain.UserRole;
import pfs.lms.enquiry.menustructure.domain.Menu;
import pfs.lms.enquiry.menustructure.domain.MenuHeader;
import pfs.lms.enquiry.menustructure.domain.MenuItem;
import pfs.lms.enquiry.menustructure.service.IMenuService;
import pfs.lms.enquiry.repository.UserRoleRepository;

import java.util.List;

/**
 * Created by sajeev on 14-May-21.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class    MenuInitializer  implements CommandLineRunner {


    @Autowired
    IMenuService menuService;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Override
    public void run(String... strings) throws Exception {

        // Admin Menu
        createAdminMenu();

        List<UserRole> userRolesList = userRoleRepository.findAll();

        for (UserRole userRole : userRolesList ) {

            Menu menu = menuService.findByUserRole(userRole.getCode());

            switch (userRole.getCode()) {
                case "TR0100": //Loan Applicant
                    createLoanApplicantMenu(userRole.getCode(), userRole.getValue());
                     break;
                case "TR0110": //Prospect
                    createLoanApplicantMenu(userRole.getCode(), userRole.getValue());
                    break;
                case "ZLM013" : //Appraisal Officer
                        createAppraisalOfficerMenu(userRole.getCode(), userRole.getValue());
                    break;
                case "ZLM024": //Nodal Officer-Monitoring
                        createMonitoringOfficerMenu(userRole.getCode(), userRole.getValue());
                    break;
                case "ZLM040": //Monitoring Head
                    createMonitoringOfficerMenu(userRole.getCode(), userRole.getValue());
                    break;
                case "ZLM023" : //PFS IT Team
                    createITTeamMenu(userRole.getCode(),userRole.getValue());
                    break;
                case "ZLM033" : //Business Development Head
                    createBDHeadMenu(userRole.getCode(),userRole.getValue());
                    break;
                case "ZLM034" : //Business Development Officer
                    createBDOfficerMenu(userRole.getCode(),userRole.getValue());
                    break;
                default:
                    createDefaultMenu(userRole.getCode(), userRole.getValue());
            }

        }




    }



    private void createAdminMenu(){
        // Admin User Menu

        Menu adminMenu = new Menu();
        Menu adminMenuExisting = new Menu();
        adminMenuExisting = menuService.findByUserRole("admin");
        if (adminMenuExisting != null) {
            adminMenu = adminMenuExisting;

        }
        adminMenu.setMenuHeaders(null);
        adminMenu.setUserRole("admin");
        adminMenu.setUserRoleName("Administrator");

        Integer headerSerialNumber =0 ;



        adminMenu = this.addMain(adminMenu);
        adminMenu = this.addAdministration(adminMenu);
        adminMenu = this.addReports(adminMenu);
        adminMenu = this.addLoanApplicationAll(adminMenu);
        adminMenu = this.addLoanServicing(adminMenu);
        adminMenu  = this.addBusinessDevelopment(adminMenu);

        if (adminMenuExisting == null) {
            adminMenu = menuService.createMenu(adminMenu);
        } else {
            adminMenu = menuService.updateMenu(adminMenu);
        }
    }

    private void createITTeamMenu(String userRole, String userRoleDesc){
        // Admin User Menu

        Menu adminMenu = new Menu();
        Menu adminMenuExisting = new Menu();
        adminMenuExisting = menuService.findByUserRole(userRole);
        if (adminMenuExisting != null) {
            adminMenu = adminMenuExisting;

        }
        adminMenu.setMenuHeaders(null);
        adminMenu.setUserRole(userRole);
        adminMenu.setUserRoleName(userRoleDesc);

        Integer headerSerialNumber =0 ;



        adminMenu = this.addMain(adminMenu);
        adminMenu = this.addAdministration(adminMenu);
        adminMenu = this.addReports(adminMenu);
        adminMenu = this.addLoanApplicationAll(adminMenu);
        adminMenu = this.addLoanServicing(adminMenu);
        adminMenu = this.addBusinessDevelopment(adminMenu);

        if (adminMenuExisting == null) {
            adminMenu = menuService.createMenu(adminMenu);
        } else {
            adminMenu = menuService.updateMenu(adminMenu);
        }
    }

    private void createAppraisalOfficerMenu(String userRole, String userRoleName) {

        Menu userMenu = new Menu();
        Menu userMenuExisting = new Menu();
        userMenuExisting = menuService.findByUserRole(userRole);
        if (userMenuExisting != null) {
            userMenu = userMenuExisting;

        }
        userMenu.setMenuHeaders(null);
        userMenu.setUserRole(userRole);
        userMenu.setUserRoleName(userRoleName);


        userMenu = this.addMain(userMenu);
        userMenu = this.addReports(userMenu);
        userMenu = this.addLoanApplicationAll(userMenu);
        userMenu = this.addLoanServicing(userMenu);

        if (userMenuExisting == null) {
            userMenu = menuService.createMenu(userMenu);
        } else {
            userMenu = menuService.updateMenu(userMenu);
        }
    }

    private void createDefaultMenu(String userRole, String userRoleName) {

        Menu userMenu = new Menu();
        Menu userMenuExisting = new Menu();
        userMenuExisting = menuService.findByUserRole(userRole);
        if (userMenuExisting != null) {
            userMenu = userMenuExisting;

        }
        userMenu.setMenuHeaders(null);
        userMenu.setUserRole(userRole);
        userMenu.setUserRoleName(userRoleName);


        userMenu = this.addMain(userMenu);
        userMenu = this.addReports(userMenu);
        userMenu = this.addLoanApplicationAll(userMenu);
        userMenu = this.addLoanServicing(userMenu);
        userMenu = this.addBDHeadMenu(userMenu);

        if (userMenuExisting == null) {
            userMenu = menuService.createMenu(userMenu);
        } else {
            userMenu = menuService.updateMenu(userMenu);
        }
    }

    private void createMonitoringOfficerMenu(String userRole, String userRoleName) {

        Menu userMenu = new Menu();
        Menu userMenuExisting = new Menu();
        userMenuExisting = menuService.findByUserRole(userRole);
        if (userMenuExisting != null) {
            userMenu = userMenuExisting;

        }
        userMenu.setMenuHeaders(null);
        userMenu.setUserRole(userRole);
        userMenu.setUserRoleName(userRoleName);


        userMenu = this.addMain(userMenu);
        userMenu = this.addReports(userMenu);
        userMenu = this.addLoanServicing(userMenu);

        if (userMenuExisting == null) {
            userMenu = menuService.createMenu(userMenu);
        } else {
            userMenu = menuService.updateMenu(userMenu);
        }
    }

    private void createBDHeadMenu(String userRole, String userRoleName) {

        Menu userMenu = new Menu();
        Menu userMenuExisting = new Menu();
        userMenuExisting = menuService.findByUserRole(userRole);
        if (userMenuExisting != null) {
            userMenu = userMenuExisting;

        }
        userMenu.setMenuHeaders(null);
        userMenu.setUserRole(userRole);
        userMenu.setUserRoleName(userRoleName);


//        userMenu = this.addMain(userMenu);
//        userMenu = this.addReports(userMenu);
        userMenu = this.addLoanApplicantMenu(userMenu);
        userMenu = this.addBusinessDevelopment(userMenu);

        if (userMenuExisting == null) {
            userMenu = menuService.createMenu(userMenu);
        } else {
            userMenu = menuService.updateMenu(userMenu);
        }
    }

    private void createBDOfficerMenu(String userRole, String userRoleName) {

        Menu userMenu = new Menu();
        Menu userMenuExisting = new Menu();
        userMenuExisting = menuService.findByUserRole(userRole);
        if (userMenuExisting != null) {
            userMenu = userMenuExisting;

        }
        userMenu.setMenuHeaders(null);
        userMenu.setUserRole(userRole);
        userMenu.setUserRoleName(userRoleName);


//        userMenu = this.addMain(userMenu);
//        userMenu = this.addReports(userMenu);
        userMenu = this.addLoanApplicantMenu(userMenu);
        userMenu = this.addBusinessDevelopment(userMenu);

        if (userMenuExisting == null) {
            userMenu = menuService.createMenu(userMenu);
        } else {
            userMenu = menuService.updateMenu(userMenu);
        }
    }


    private void createLoanApplicantMenu(String userRole, String userRoleName) {

        Menu userMenu = new Menu();
        Menu userMenuExisting = new Menu();
        userMenuExisting = menuService.findByUserRole(userRole);
        if (userMenuExisting != null) {
            userMenu = userMenuExisting;

        }
        userMenu.setMenuHeaders(null);
        userMenu.setUserRole(userRole);
        userMenu.setUserRoleName(userRoleName);


//        userMenu = this.addMain(userMenu);
//        userMenu = this.addReports(userMenu);
        userMenu = this.addLoanApplicantMenu(userMenu);

        if (userMenuExisting == null) {
            userMenu = menuService.createMenu(userMenu);
        } else {
            userMenu = menuService.updateMenu(userMenu);
        }
    }



    private Menu addMain(Menu menu){
        /*
                    MAIN
        */
        MenuHeader menuHeader = new MenuHeader();
        if (menu.getMenuHeaders() == null) {
            menuHeader.setSerialNumber(1);
        } else {
        menuHeader.setSerialNumber(menu.getMenuHeaders().size() + 1);
        }
        menuHeader.setId("home");
        menuHeader.setTitle("Home");
        menuHeader.setTranslate("NAV.HOME");
        menuHeader.setType("group");
        menuHeader.setIcon("account_box");

        // Main Items
        Integer serialNo = 0;

        MenuItem menuItem = new MenuItem();
        serialNo += 1;
        menuItem.setId("inbox");
        menuItem.setTitle("Inbox");
        menuItem.setSerialNumber(serialNo);
        menuItem.setTranslate("NAV.INBOX");
        menuItem.setType("item");
        menuItem.setIcon("account_box");
        menuItem.setUrl("/inbox");
        menuHeader.addMenuitem(menuItem);

        menu.addMenuHeader(menuHeader);

        return menu;
    }

    private Menu addReports(Menu menu){

        /*
                REPORTS
         */
        MenuHeader menuHeader = new MenuHeader();
        if (menu.getMenuHeaders() == null) {
            menuHeader.setSerialNumber(1);
        } else {
            menuHeader.setSerialNumber(menu.getMenuHeaders().size() + 1);
        }
        menuHeader.setId("reports");
        menuHeader.setTitle("Reports");
        menuHeader.setTranslate("NAV.REPORTS");
        menuHeader.setType("group");
        menuHeader.setIcon("view_list");


        // Report Items
        Integer serialNo = 0;

        MenuItem menuItem = new MenuItem();
        menuItem.setId("cchangeHistory");
        menuItem.setTitle("Change History");
        menuItem.setSerialNumber(serialNo + 1);
        menuItem.setTranslate("NAV.CHANGEHISTORY");
        menuItem.setType("item");
        menuItem.setIcon("view_list");
        menuItem.setUrl("/changeDocuments");
        menuHeader.addMenuitem(menuItem);


        menu.addMenuHeader(menuHeader);

        return menu;

    }

    private Menu addLoanApplicationAll(Menu menu){

             /*
                    APPLICATIONS
             */
        MenuHeader menuHeader = new MenuHeader();
        if (menu.getMenuHeaders() == null) {
            menuHeader.setSerialNumber(1);
        } else {
            menuHeader.setSerialNumber(menu.getMenuHeaders().size() + 1);
        }
        menuHeader.setId("applications");
        menuHeader.setTitle("Applications");
        menuHeader.setTranslate("NAV.APPLICATIONS");
        menuHeader.setType("group");
        menuHeader.setIcon("");


        // Application items
        Integer serialNo = 0;

        MenuItem menuItem = new MenuItem();
        serialNo += 1;
        menuItem.setId("loan-enquiry");
        menuItem.setTitle("New Loan Enquiry");
        menuItem.setSerialNumber(serialNo);
        menuItem.setTranslate("NAV.LOANENQUIRY");
        menuItem.setType("item");
        menuItem.setIcon("create");
        menuItem.setUrl("/enquiryApplication");
        menuHeader.addMenuitem(menuItem);

        menuItem = new MenuItem();
        serialNo += 1;
        menuItem.setId("enquiry-alerts");
        menuItem.setTitle("Enquiry Alerts");
        menuItem.setSerialNumber(serialNo);
        menuItem.setTranslate("NAV.ENQUIRYALERTS");
        menuItem.setType("item");
        menuItem.setIcon("rate_review");
        menuItem.setUrl("/enquiryAlerts");
        menuHeader.addMenuitem(menuItem);

        menuItem = new MenuItem();
        serialNo += 1;
        menuItem.setId("enquiry-list");
        menuItem.setTitle("Enquiry List");
        menuItem.setSerialNumber(serialNo);
        menuItem.setTranslate("NAV.ENQUIRYLIST");
        menuItem.setType("item");
        menuItem.setIcon("list");
        menuItem.setUrl("/enquiryList");
        menuHeader.addMenuitem(menuItem);



        menuItem = new MenuItem();
        serialNo += 1;
        menuItem.setId("partner-mgmt");
        menuItem.setTitle("Business Partners");
        menuItem.setSerialNumber(serialNo);
        menuItem.setTranslate("NAV.PARTNER");
        menuItem.setType("item");
        menuItem.setIcon("people_alt");
        menuItem.setUrl("/partner");
        menuHeader.addMenuitem(menuItem);

        menu.addMenuHeader(menuHeader);

        return menu;

    }

    private Menu addLoanApplicantMenu(Menu menu){

             /*
                    APPLICATIONS
             */
        MenuHeader menuHeader = new MenuHeader();
        if (menu.getMenuHeaders() == null) {
            menuHeader.setSerialNumber(1);
        } else {
            menuHeader.setSerialNumber(menu.getMenuHeaders().size() + 1);
        }
        menuHeader.setId("applications");
        menuHeader.setTitle("Applications");
        menuHeader.setTranslate("NAV.APPLICATIONS");
        menuHeader.setType("group");
        menuHeader.setIcon("");


        // Application items
        Integer serialNo = 0;

        MenuItem menuItem = new MenuItem();
        serialNo += 1;
        menuItem.setId("loan-enquiry");
        menuItem.setTitle("New Loan Enquiry");
        menuItem.setSerialNumber(serialNo);
        menuItem.setTranslate("NAV.LOANENQUIRY");
        menuItem.setType("item");
        menuItem.setIcon("create");
        menuItem.setUrl("/enquiryApplication");
        menuHeader.addMenuitem(menuItem);



        menuItem = new MenuItem();
        serialNo += 1;
        menuItem.setId("enquiry-alerts");
        menuItem.setTitle("Enquiry Alerts");
        menuItem.setSerialNumber(serialNo);
        menuItem.setTranslate("NAV.ENQUIRYALERTS");
        menuItem.setType("item");
        menuItem.setIcon("rate_review");
        menuItem.setUrl("/enquiryAlerts");
        menuHeader.addMenuitem(menuItem);


        menu.addMenuHeader(menuHeader);

        return menu;

    }

    private Menu addBDHeadMenu(Menu menu){

             /*
                    APPLICATIONS
             */
        MenuHeader menuHeader = new MenuHeader();
        if (menu.getMenuHeaders() == null) {
            menuHeader.setSerialNumber(1);
        } else {
            menuHeader.setSerialNumber(menu.getMenuHeaders().size() + 1);
        }
        menuHeader.setId("applications");
        menuHeader.setTitle("Applications");
        menuHeader.setTranslate("NAV.APPLICATIONS");
        menuHeader.setType("group");
        menuHeader.setIcon("");


        // Application items
        Integer serialNo = 0;

        MenuItem menuItem = new MenuItem();
        serialNo += 1;
        menuItem.setId("loan-enquiry");
        menuItem.setTitle("New Loan Enquiry");
        menuItem.setSerialNumber(serialNo);
        menuItem.setTranslate("NAV.LOANENQUIRY");
        menuItem.setType("item");
        menuItem.setIcon("create");
        menuItem.setUrl("/enquiryApplication");
        menuHeader.addMenuitem(menuItem);



        menuItem = new MenuItem();
        serialNo += 1;
        menuItem.setId("enquiry-alerts");
        menuItem.setTitle("Enquiry Alerts");
        menuItem.setSerialNumber(serialNo);
        menuItem.setTranslate("NAV.ENQUIRYALERTS");
        menuItem.setType("item");
        menuItem.setIcon("rate_review");
        menuItem.setUrl("/enquiryAlerts");
        menuHeader.addMenuitem(menuItem);


        menu.addMenuHeader(menuHeader);

        return menu;

    }


    private Menu addAdministration(Menu menu) {


            /*
                    ADMINISTRATION
             */
        MenuHeader  menuHeader = new MenuHeader();
        if (menu.getMenuHeaders() == null) {
            menuHeader.setSerialNumber(1);
        } else {
            menuHeader.setSerialNumber(menu.getMenuHeaders().size() + 1);
        }
        menuHeader.setId("administration");
        menuHeader.setTitle("Administration");
        menuHeader.setTranslate("NAV.ADMINISTRATION");
        menuHeader.setType("group");
        menuHeader.setIcon("account_box");


        // Administration items
        Integer  serialNo = 0;

        MenuItem menuItem = new MenuItem();
        serialNo += 1;
        menuItem.setId("user-management");
        menuItem.setTitle("User Management");
        menuItem.setSerialNumber(serialNo);
        menuItem.setTranslate("NAV.USERMANAGEMENT");
        menuItem.setType("item");
        menuItem.setIcon("account_box");
        menuItem.setUrl("/userManagement");
        menuHeader.addMenuitem(menuItem);

        menuItem = new MenuItem();
        serialNo += 1;
        menuItem.setId("mail-repo");
        menuItem.setTitle("Email Events");
        menuItem.setSerialNumber(serialNo);
        menuItem.setTranslate("NAV.MAILREPOSITORY");
        menuItem.setType("item");
        menuItem.setIcon("mail");
        menuItem.setUrl("/mailRepo");
        menuHeader.addMenuitem(menuItem);

        menu.addMenuHeader(menuHeader);

        return menu;

    }

    private Menu addLoanServicing(Menu menu) {
              /*
                    LOAN SERVICING
             */
        MenuHeader menuHeader = new MenuHeader();
        if (menu.getMenuHeaders() == null) {
            menuHeader.setSerialNumber(1);
        } else {
            menuHeader.setSerialNumber(menu.getMenuHeaders().size() + 1);
        }
        menuHeader.setId("processLoans");
        menuHeader.setTitle("LOAN PROCESSES");
        menuHeader.setTranslate("NAV.PROCESSLOANS");
        menuHeader.setType("group");
        menuHeader.setIcon("");


        // Report Items
        Integer serialNo = 0;

        MenuItem menuItem = new MenuItem();
        serialNo += 1;
        menuItem.setId("loan-contracts");
        menuItem.setTitle("Loan Contracts List");
        menuItem.setSerialNumber(serialNo);
        menuItem.setTranslate("NAV.LOANCONTRACTSLIST");
        menuItem.setType("item");
        menuItem.setIcon("create");
        menuItem.setUrl("/loanContractsList");
        menuHeader.addMenuitem(menuItem);

        menu.addMenuHeader(menuHeader);

        return menu;

    }

    private Menu addBusinessDevelopment(Menu menu) {
              /*
                    BUsiness DEvelopment
             */
        MenuHeader menuHeader = new MenuHeader();
        if (menu.getMenuHeaders() == null) {
            menuHeader.setSerialNumber(1);
        } else {
            menuHeader.setSerialNumber(menu.getMenuHeaders().size() + 1);
        }
        menuHeader.setId("EnquiryUpload");
        menuHeader.setTitle("BUSINESS DEVELOPMENT");
        menuHeader.setTranslate("NAV.BUSINESSDEVELOPMENT");
        menuHeader.setType("group");
        menuHeader.setIcon("");


        // Report Items
        Integer serialNo = 0;

        MenuItem menuItem = new MenuItem();
        serialNo += 1;
        menuItem.setId("enquiry-upload");
        menuItem.setTitle("Upload Loan Enquiries");
        menuItem.setSerialNumber(serialNo);
        menuItem.setTranslate("NAV.UPLOADENQUIRIES");
        menuItem.setType("item");
        menuItem.setIcon("upload");
        menuItem.setUrl("/importEnquiries");
        menuHeader.addMenuitem(menuItem);

        menu.addMenuHeader(menuHeader);

        return menu;

    }

}
