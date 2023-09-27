package pfs.lms.enquiry.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by gguptha on 09/11/18.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class SAPLoanApplicationDetailsResource {

    @JsonProperty (value = "LoanContract")
    private String loanContract;

    @JsonProperty(value = "LoanApplicationId")
    private String loanApplicationId;

    @JsonProperty(value = "BusPartnerNumber")
    private String busPartnerNumber;

    @JsonProperty(value = "PartnerCategory")
    private String partnerCategory;

    @JsonProperty(value = "PartnerType")
    private String partnerType;

    @JsonProperty(value = "PartnerGroup")
    private String partnerGroup;

    @JsonProperty(value = "PartnerExternalNumber")
    private String partnerExternalNumber;

    @JsonProperty(value = "PartnerRole")
    private String partnerRole;

    @JsonProperty(value = "Name1")
    private String name1;

    @JsonProperty(value = "Name2")
    private String name2;

    @JsonProperty(value = "Firstname")
    private String firstname;

    @JsonProperty(value = "Lastname")
    private String lastname;

    @JsonProperty(value = "Email")
    private String email;

    @JsonProperty(value = "City")
    private String city;

    @JsonProperty(value = "District")
    private String district;

    @JsonProperty(value = "State")
    private String state;


    @JsonProperty(value = "PostalCode")
    private String postalCode;

    @JsonProperty(value = "HouseNo")
    private String houseNo;

    @JsonProperty(value = "Street")
    private String street;

    @JsonProperty(value = "Country")
    private String country;

    @JsonProperty(value = "ContactPerName")
    private String contactPerName;


    @JsonProperty(value = "PanNumber")
    private String panNumber;

    @JsonProperty(value = "ApplicationDate")
    private String applicationDate;

    @JsonProperty(value = "LoanClass")
    private String loanClass; //LOAN_CLASS

    @JsonProperty(value = "FinancingType")
    private String financingType;  //SFIWO

    @JsonProperty(value = "ProjectType")
    private String projectType;

    @JsonProperty(value = "DebtEquityIndicator")
    private String debtEquityIndicator;

    @JsonProperty(value = "ProjectCapaacity")
    private String projectCapaacity;

    @JsonProperty(value = "ProjectCapacityUnit")
    private String projectCapacityUnit;

    @JsonProperty(value = "ProjectState")
    private String projectState;

    @JsonProperty(value = "ProjectDistrict")
    private String projectDistrict;

    @JsonProperty(value = "TenorYear")
    private String tenorYear;

    @JsonProperty(value = "TenorMonth")
    private String tenorMonth;


    @JsonProperty(value = "ProjectCostInCrores")
    private String projectCostInCrores;


    @JsonProperty(value = "DebtAmountInCrores")
    private String debtAmountInCrores;

    @JsonProperty(value = "EquityAmountInCrores")
    private String equityAmountInCrores;

    @JsonProperty(value = "Currency")
    private String currency;

    @JsonProperty(value = "ApplicationCapitalInCrores")
    private String applicationCapitalInCrores;

    @JsonProperty(value = "LoanPurpose")
    private String loanPurpose; //SVZWECK


    @JsonProperty(value = "Purpose")
    private String purpose; //Demand Letter Text

    @JsonProperty(value = "ScheduledCommDate")
    private String scheduledCommDate;

    @JsonProperty(value = "ActualCOD")
    private String actualCOD;

    @JsonProperty(value = "GroupCompanyName")
    private String groupCompanyName;

    @JsonProperty(value = "GroupCompany")
    private String groupCompany;

    @JsonProperty(value = "PromoterName")
    private String promoterName;

    @JsonProperty(value = "PromoterNetWorthInCrores")
    private  String promoterNetWorthInCrores;

    @JsonProperty(value = "PromoterPATInCrores")
    private String promoterPATInCrores;

    @JsonProperty(value = "PromoterAreaOfBusiness")
    private String promoterAreaOfBusiness;

    @JsonProperty(value = "PromoterRating")
    private String promoterRating;

    @JsonProperty(value = "PromoterKeyDirector")
    private String promoterKeyDirector;

    @JsonProperty(value = "LoanStatus")
    private String loanStatus; //SSTATI

    @JsonProperty(value = "DisbursementStatus")
    private String disbursementStatus;

    @JsonProperty(value = "FirstDisbursementDate")
    private String firstDisbursementDate;

    @JsonProperty(value = "Term")
    private String term;

    @JsonProperty(value = "ProjectName")
    private String projectName;

    @JsonProperty(value = "LoanOfficer")
    private String loanOfficer;

    @JsonProperty(value = "LoanProduct")
    private String loanProduct;

    @JsonProperty(value ="ContactBranchAddress")
    private String contactBranchAddress;

    @JsonProperty(value =  "ContactDesignation")
    private String contactDesignation;

    @JsonProperty(value =  "ContactDepartment")
    private String contactDepartment;

    @JsonProperty(value = "ContactTelePhone")
    private String contactTelePhone;

    @JsonProperty(value =  "ContactLandLinePhone")
    private String contactLandLinePhone;
    @JsonProperty(value = "ContactNumber")
    private String contactNumber;
    @JsonProperty(value = "ContactEmail")
    private String contactEmail;

    @JsonProperty(value = "ContactFaxNumber")
    private String contactFaxNumber;


    @JsonProperty(value = "LoanContractAmount")
    private String  loanContractAmount;

    @JsonProperty(value = "SanctionLetterDate")
    private String  sanctionLetterDate;
    @JsonProperty(value = "SanctionLetterAmount")
    private String  sanctionLetterAmount;
    @JsonProperty(value = "LoanRevisedSanctionAmount")
    private String  loanRevisedSanctionAmount;

    @JsonProperty(value = "LoanCurrentContractAmount")
    private String  loanCurrentContractAmount;

    @JsonProperty(value = " LoanDisbursedAmount ")
    private String  loanDisbursedAmount;

    @JsonProperty(value = "IndustrySector")
    private String industrySector;

    @JsonProperty(value = "BoardMeetingNumber")
    private String boardMeetingNumber;
    @JsonProperty(value = "BoardApprovalDate")
    private String boardApprovalDate;
    @JsonProperty(value = "BoardApprovalRemarks")
    private String boardApprovalRemarks;
    @JsonProperty(value = "BODStatus")
    private String bODStatus;
    @JsonProperty(value = "ICCClearanceDate")
    private String iCCClearanceDate;
    @JsonProperty(value = "ICCMeetNumber")
    private String iCCMeetNumber;
    @JsonProperty(value = "ICCStatus")
    private String iCCStatus;
    @JsonProperty(value = "ICCRemarks")
    private String iCCRemarks;

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public SAPLoanApplicationDetailsResource() {

        this.loanContract = "";
        this.loanApplicationId = "";
        this.busPartnerNumber = "";
        this.partnerCategory = "";
        this.partnerType = "";
        this.partnerGroup = "";
        this.partnerExternalNumber = "";
        this.partnerRole = "TR0100";
        this.name1 = "";
        this.name2 = "";
        this.firstname = "";
        this.lastname = "";
        this.email = "";
        this.city = "";
        this.district = "";
        this.state = "";
        this.postalCode = "";
        this.houseNo = "";
        this.street = "";
        this.country = "";
        this.contactPerName = "";
        this.panNumber = "";
        this.loanClass = "";
        this.financingType = "";
        this.projectType = "";
        this.debtEquityIndicator = "";
        this.projectCapaacity = "" ;
        this.projectCapacityUnit="";
        this.projectState="";
        this.projectDistrict="";
        this.tenorYear="";
        this.tenorMonth="";
        this.projectCostInCrores="" ;
        this.debtAmountInCrores="";
        this.equityAmountInCrores="";
        this.currency="";
        this.applicationCapitalInCrores="";
        this.loanPurpose="";
        this.scheduledCommDate="";
        this.groupCompanyName="";
        this.promoterName="";
        this.promoterNetWorthInCrores="";
        this.promoterPATInCrores="";
        this.promoterAreaOfBusiness="";
        this.promoterRating="";
        this.promoterKeyDirector="";
        this.loanStatus="";
        this.projectName="";
        this.loanOfficer = "";
        this.loanProduct = "";

        this.contactBranchAddress = "";
        this.contactDesignation ="";
        this.contactDepartment = "";
        this.contactTelePhone="";
        this.contactLandLinePhone ="";
        this.contactEmail ="";
        this.contactFaxNumber ="";

        this.industrySector = "";

        this.boardMeetingNumber = "";
        this.boardApprovalDate = "";
        this.boardApprovalRemarks = "";
        this.bODStatus = "";
        this.iCCClearanceDate = "";
        this.iCCMeetNumber = "";
        this.iCCStatus = "";
        this.iCCRemarks = "";
    }

    public String getLoanContract() {
        return loanContract;
    }

    public void setLoanContract(String loanContract) {
        this.loanContract = loanContract;
    }

    public String getLoanApplicationId() {
        return loanApplicationId;
    }

    public void setLoanApplicationId(String loanApplicationId) {
        this.loanApplicationId = loanApplicationId;
    }

    public String getBusPartnerNumber() {
        return busPartnerNumber;
    }

    public void setBusPartnerNumber(String busPartnerNumber) {
        this.busPartnerNumber = busPartnerNumber;
    }

    public String getPartnerCategory() {
        return partnerCategory;
    }

    public void setPartnerCategory(String partnerCategory) {
        this.partnerCategory = partnerCategory;
    }

    public String getPartnerType() {
        return partnerType;
    }

    public void setPartnerType(String partnerType) {
        this.partnerType = partnerType;
    }

    public String getPartnerGroup() {
        return partnerGroup;
    }

    public void setPartnerGroup(String partnerGroup) {
        this.partnerGroup = partnerGroup;
    }

    public String getPartnerExternalNumber() {
        return partnerExternalNumber;
    }

    public void setPartnerExternalNumber(String partnerExternalNumber) {
        this.partnerExternalNumber = partnerExternalNumber;
    }

    public String getPartnerRole() {
        return partnerRole;
    }

    public void setPartnerRole(String partnerRole) {
        this.partnerRole = partnerRole;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }


    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getContactPerName() {
        return contactPerName;
    }

    public void setContactPerName(String contactPerName) {
        this.contactPerName = contactPerName;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(String applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getLoanClass() {
        return loanClass;
    }

    public void setLoanClass(String loanClass) {
        this.loanClass = loanClass;
    }

    public String getFinancingType() {
        return financingType;
    }

    public void setFinancingType(String financingType) {
        this.financingType = financingType;
    }

    public String getDebtEquityIndicator() {
        return debtEquityIndicator;
    }

    public void setDebtEquityIndicator(String debtEquityIndicator) {
        this.debtEquityIndicator = debtEquityIndicator;
    }

    public String getProjectCapaacity() {
        return projectCapaacity;
    }

    public void setProjectCapaacity(String projectCapaacity) {
        this.projectCapaacity = projectCapaacity;
    }

    public String getProjectCapacityUnit() {
        return projectCapacityUnit;
    }

    public void setProjectCapacityUnit(String projectCapacityUnit) {
        this.projectCapacityUnit = projectCapacityUnit;
    }

    public String getProjectState() {
        return projectState;
    }

    public void setProjectState(String projectState) {
        this.projectState = projectState;
    }

    public String getProjectDistrict() {
        return projectDistrict;
    }

    public void setProjectDistrict(String projectDistrict) {
        this.projectDistrict = projectDistrict;
    }

    public String getTenorYear() {
        return tenorYear;
    }

    public void setTenorYear(String tenorYear) {
        this.tenorYear = tenorYear;
    }

    public String getTenorMonth() {
        return tenorMonth;
    }

    public void setTenorMonth(String tenorMonth) {
        this.tenorMonth = tenorMonth;
    }

    public String getProjectCostInCrores() {
        return projectCostInCrores;
    }

    public void setProjectCostInCrores(String projectCostInCrores) {
        this.projectCostInCrores = projectCostInCrores;
    }

    public String getDebtAmountInCrores() {
        return debtAmountInCrores;
    }

    public void setDebtAmountInCrores(String debtAmountInCrores) {
        this.debtAmountInCrores = debtAmountInCrores;
    }

    public String getEquityAmountInCrores() {
        return equityAmountInCrores;
    }

    public void setEquityAmountInCrores(String equityAmountInCrores) {
        this.equityAmountInCrores = equityAmountInCrores;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getApplicationCapitalInCrores() {
        return applicationCapitalInCrores;
    }

    public void setApplicationCapitalInCrores(String applicationCapitalInCrores) {
        this.applicationCapitalInCrores = applicationCapitalInCrores;
    }

    public String getLoanPurpose() {
        return loanPurpose;
    }

    public void setLoanPurpose(String loanPurpose) {
        this.loanPurpose = loanPurpose;
    }


    public String getScheduledCommDate() {
        return scheduledCommDate;
    }

    public void setScheduledCommDate(String scheduledCommDate) {
        this.scheduledCommDate = scheduledCommDate;
    }

    public String getGroupCompanyName() {

        return groupCompanyName;
    }

    public void setGroupCompanyName(String groupCompanyName) {
        this.groupCompanyName = groupCompanyName;
    }

    public String getPromoterName() {
        return promoterName;
    }

    public void setPromoterName(String promoterName) {
        this.promoterName = promoterName;
    }

    public String getPromoterNetWorthInCrores() {
        return promoterNetWorthInCrores;
    }

    public void setPromoterNetWorthInCrores(String promoterNetWorthInCrores) {
        this.promoterNetWorthInCrores = promoterNetWorthInCrores;
    }

    public String getPromoterPATInCrores() {
        return promoterPATInCrores;
    }

    public void setPromoterPATInCrores(String promoterPATInCrores) {
        this.promoterPATInCrores = promoterPATInCrores;
    }

    public String getPromoterAreaOfBusiness() {
        return promoterAreaOfBusiness;
    }

    public void setPromoterAreaOfBusiness(String promoterAreaOfBusiness) {
        this.promoterAreaOfBusiness = promoterAreaOfBusiness;
    }

    public String getPromoterRating() {
        return promoterRating;
    }

    public void setPromoterRating(String promoterRating) {
        this.promoterRating = promoterRating;
    }

    public String getPromoterKeyDirector() {
        return promoterKeyDirector;
    }

    public void setPromoterKeyDirector(String promoterKeyDirector) {
        this.promoterKeyDirector = promoterKeyDirector;
    }

    public String getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getLoanOfficer() {
        return loanOfficer;
    }

    public void setLoanOfficer(String loanOfficer) {
        this.loanOfficer = loanOfficer;
    }

    public String getLoanProduct() {
        return loanProduct;
    }

    public void setLoanProduct(String loanProduct) {
        this.loanProduct = loanProduct;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getContactBranchAddress() {
        return contactBranchAddress;
    }

    public void setContactBranchAddress(String contactBranchAddress) {
        this.contactBranchAddress = contactBranchAddress;
    }

    public String getContactDesignation() {
        return contactDesignation;
    }

    public void setContactDesignation(String contactDesignation) {
        this.contactDesignation = contactDesignation;
    }

    public String getContactDepartment() {
        return contactDepartment;
    }

    public void setContactDepartment(String contactDepartment) {
        this.contactDepartment = contactDepartment;
    }

    public String getContactTelePhone() {
        return contactTelePhone;
    }

    public void setContactTelePhone(String contactTelePhone) {
        this.contactTelePhone = contactTelePhone;
    }

    public String getContactLandLinePhone() {
        return contactLandLinePhone;
    }

    public void setContactLandLinePhone(String contactLandLinePhone) {
        this.contactLandLinePhone = contactLandLinePhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactFaxNumber() {
        return contactFaxNumber;
    }

    public void setContactFaxNumber(String contactFaxNumber) {
        this.contactFaxNumber = contactFaxNumber;
    }

    public String getLoanContractAmount() {
        return loanContractAmount;
    }

    public void setLoanContractAmount(String loanContractAmount) {
        this.loanContractAmount = loanContractAmount;
    }

    public String getLoanRevisedSanctionAmount() {
        return loanRevisedSanctionAmount;
    }

    public void setLoanRevisedSanctionAmount(String loanRevisedSanctionAmount) {
        this.loanRevisedSanctionAmount = loanRevisedSanctionAmount;
    }

    public String getLoanCurrentContractAmount() {
        return loanCurrentContractAmount;
    }

    public void setLoanCurrentContractAmount(String loanCurrentContractAmount) {
        this.loanCurrentContractAmount = loanCurrentContractAmount;
    }

    public String getLoanDisbursedAmount() {
        return loanDisbursedAmount;
    }

    public void setLoanDisbursedAmount(String loanDisbursedAmount) {
        this.loanDisbursedAmount = loanDisbursedAmount;
    }

    public String getIndustrySector() {
        return industrySector;
    }

    public void setIndustrySector(String industrySector) {
        this.industrySector = industrySector;
    }

    public String getBoardMeetingNumber() {
        return boardMeetingNumber;
    }

    public void setBoardMeetingNumber(String boardMeetingNumber) {
        this.boardMeetingNumber = boardMeetingNumber;
    }

    public String getBoardApprovalDate() {
        return boardApprovalDate;
    }

    public void setBoardApprovalDate(String boardApprovalDate) {
        this.boardApprovalDate = boardApprovalDate;
    }

    public String getBoardApprovalRemarks() {
        return boardApprovalRemarks;
    }

    public void setBoardApprovalRemarks(String boardApprovalRemarks) {
        this.boardApprovalRemarks = boardApprovalRemarks;
    }

    public String getbODStatus() {
        return bODStatus;
    }

    public void setbODStatus(String bODStatus) {
        this.bODStatus = bODStatus;
    }

    public String getiCCClearanceDate() {
        return iCCClearanceDate;
    }

    public void setiCCClearanceDate(String iCCClearanceDate) {
        this.iCCClearanceDate = iCCClearanceDate;
    }

    public String getiCCMeetNumber() {
        return iCCMeetNumber;
    }

    public void setiCCMeetNumber(String iCCMeetNumber) {
        this.iCCMeetNumber = iCCMeetNumber;
    }

    public String getiCCStatus() {
        return iCCStatus;
    }

    public void setiCCStatus(String iCCStatus) {
        this.iCCStatus = iCCStatus;
    }

    public String getiCCRemarks() {
        return iCCRemarks;
    }

    public void setiCCRemarks(String iCCRemarks) {
        this.iCCRemarks = iCCRemarks;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getActualCOD() {
        return actualCOD;
    }

    public void setActualCOD(String actualCOD) {
        this.actualCOD = actualCOD;
    }

    public String getGroupCompany() {
        return groupCompany;
    }

    public void setGroupCompany(String groupCompany) {
        this.groupCompany = groupCompany;
    }

    public String getDisbursementStatus() {
        return disbursementStatus;
    }

    public void setDisbursementStatus(String disbursementStatus) {
        this.disbursementStatus = disbursementStatus;
    }

    public String getFirstDisbursementDate() {
        return firstDisbursementDate;
    }

    public void setFirstDisbursementDate(String firstDisbursementDate) {
        this.firstDisbursementDate = firstDisbursementDate;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getSanctionLetterDate() {
        return sanctionLetterDate;
    }

    public void setSanctionLetterDate(String sanctionLetterDate) {
        this.sanctionLetterDate = sanctionLetterDate;
    }

    public String getSanctionLetterAmount() {
        return sanctionLetterAmount;
    }

    public void setSanctionLetterAmount(String sanctionLetterAmount) {
        this.sanctionLetterAmount = sanctionLetterAmount;
    }

    @Override
    public String toString() {
        return "SAPLoanApplicationDetailsResource{" +
                "loanContract='" + loanContract + '\'' +
                ", LoanApplicationId='" + loanApplicationId + '\'' +
                ", busPartnerNumber='" + busPartnerNumber + '\'' +
                ", partnerCategory='" + partnerCategory + '\'' +
                ", partnerType='" + partnerType + '\'' +
                ", partnerGroup='" + partnerGroup + '\'' +
                ", partnerExternalNumber='" + partnerExternalNumber + '\'' +
                ", partnerRole='" + partnerRole + '\'' +
                ", name1='" + name1 + '\'' +
                ", name2='" + name2 + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", state='" + state + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", houseNo='" + houseNo + '\'' +
                ", street='" + street + '\'' +
                ", country='" + country + '\'' +
                ", contactPerName='" + contactPerName + '\'' +
                ", panNumber='" + panNumber + '\'' +
                ", applicationDate=" + applicationDate +
                ", loanClass='" + loanClass + '\'' +
                ", financingType='" + financingType + '\'' +
                ", projectType='" + projectType + '\'' +
                ", debtEquityIndicator='" + debtEquityIndicator + '\'' +
                ", projectCapaacity=" + projectCapaacity +
                ", projectCapacityUnit='" + projectCapacityUnit + '\'' +
                ", projectState='" + projectState + '\'' +
                ", projectDistrict='" + projectDistrict + '\'' +
                ", tenorYear=" + tenorYear +
                ", tenorMonth=" + tenorMonth +
                ", projectCostInCrores=" + projectCostInCrores +
                ", debtAmountInCrores=" + debtAmountInCrores +
                ", equityAmountInCrores=" + equityAmountInCrores +
                ", currency='" + currency + '\'' +
                ", applicationCapitalInCrores=" + applicationCapitalInCrores +
                ", loanPurpose='" + loanPurpose + '\'' +
                ", scheduledCommDate=" + scheduledCommDate +
                ", groupCompanyName='" + groupCompanyName + '\'' +
                ", promoterName='" + promoterName + '\'' +
                ", promoterNetWorthInCrores=" + promoterNetWorthInCrores +
                ", promoterPATInCrores=" + promoterPATInCrores +
                ", promoterAreaOfBusiness='" + promoterAreaOfBusiness + '\'' +
                ", promoterRating='" + promoterRating + '\'' +
                ", promoterKeyDirector='" + promoterKeyDirector + '\'' +
                ", loanStatus='" + loanStatus + '\'' +
                ", projectName='" + projectName + '\'' +
                '}';
    }
}
