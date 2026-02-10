//package pfs.lms.enquiry;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import pfs.lms.enquiry.resource.SAPLoanApplicationDetailsResource;
//import pfs.lms.enquiry.resource.SAPLoanApplicationResource;
//import pfs.lms.enquiry.service.ISAPIntegrationService;
//
//import java.time.LocalDate;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class SAPIntegrationTest {
//
//	@Autowired
//	private ISAPIntegrationService integrationService;
//
////	@Test
////	public void getCSRFToken() {
////		integrationService.fetchCSRFToken();
////	}
////
////	@Test
////	public void postLoanApplication() {
////
////		SAPLoanApplicationDetailsResource detailsResource = new SAPLoanApplicationDetailsResource();
////		detailsResource.setLoanApplicationId("100001");
////		detailsResource.setPartnerExternalNumber("1");
////		detailsResource.setPartnerRole("TR0100");
////		detailsResource.setFirstname("Rockey");
////		detailsResource.setLastname("Anderson");
////		detailsResource.setEmail("Victor.anderson@gmail.com");
////		detailsResource.setCity("Bangalore");
////		detailsResource.setPostalCode("322332");
////		detailsResource.setHouseNo("21221");
////		detailsResource.setStreet("12222");
////		detailsResource.setCountry("IN");
////		detailsResource.setContactPerName("John Smith");
////		//detailsResource.setApplicationDate(LocalDate.now().toString());
////		detailsResource.setApplicationDate("\\/Date(1228089600000)\\/");
////
////		detailsResource.setLoanClass("001");
////		detailsResource.setFinancingType("01");
////		detailsResource.setDebtEquityIndicator("X");
////		detailsResource.setProjectCapaacity("10");
////		detailsResource.setProjectCapacityUnit("MW");
////        detailsResource.setScheduledCommDate("\\/Date(1228089600000)\\/");
////        detailsResource.setProjectCostInCrores("1.000");
////		detailsResource.setDebtAmountInCrores("2.000");
////        detailsResource.setEquityAmountInCrores("3.000");
////        detailsResource.setCurrency("INR");
//// 		detailsResource.setApplicationCapitalInCrores("2200.00");
////		detailsResource.setLoanPurpose("01");
////		detailsResource.setGroupCompanyName("Rockey Group");
////		detailsResource.setPromoterName("Rockey");
////		detailsResource.setPromoterPATInCrores("1000.000");
////		detailsResource.setPromoterAreaOfBusiness("Test");
////		detailsResource.setPromoterRating("AAA");
////		detailsResource.setPromoterNetWorthInCrores("10.000");
////		detailsResource.setPromoterKeyDirector("Rockey Schindler");
////		detailsResource.setLoanStatus("01");
////		detailsResource.setProjectName("Rockey Solar Power Project");
////
////		SAPLoanApplicationResource d = new SAPLoanApplicationResource();
////		d.setSapLoanApplicationDetailsResource(detailsResource);
////
////		integrationService.postLoanApplication(d);
////	}
////
////	@Test
////	public void getLoanApplication() {
////
////		integrationService.getLoanApplication("");
////	}
//}
