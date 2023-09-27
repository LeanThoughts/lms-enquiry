package pfs.lms.enquiry.appraisal.resource;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.appraisal.projectdata.ProjectData;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.repository.PartnerRepository;
import pfs.lms.enquiry.utils.DataConversionUtility;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.text.ParseException;
import java.util.UUID;

//@Component
@Service
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Transactional
@Slf4j


public class SAPLoanAppraisalProjectDataResource implements Serializable {
    DataConversionUtility dataConversionUtility;

    @Autowired
    PartnerRepository partnerRepository;


    public SAPLoanAppraisalProjectDataResource() {
        saLoanAppraisalProjectDataResourceDetails = new SAPLoanAppraisalProjectDataResourceDetails();
    }

    @JsonProperty(value = "d")
    private SAPLoanAppraisalProjectDataResourceDetails saLoanAppraisalProjectDataResourceDetails;


    public void setSapLoanAppraisalProjectDataResourceDetails(SAPLoanAppraisalProjectDataResourceDetails saLoanAppraisalProjectDataResourceDetails) {
        this.saLoanAppraisalProjectDataResourceDetails = saLoanAppraisalProjectDataResourceDetails;
    }

    public SAPLoanAppraisalProjectDataResourceDetails
    mapToSAP(ProjectData projectData) throws ParseException {


        SAPLoanAppraisalProjectDataResourceDetails detailsResource = new SAPLoanAppraisalProjectDataResourceDetails();
        detailsResource.setId(projectData.getId().toString());
        detailsResource.setAppraisalId(projectData.getLoanAppraisal().getId().toString());

        detailsResource.setProjectname(projectData.getProjectName());

        detailsResource.setTypeoffunding(projectData.getTypeOfFunding());

        detailsResource.setPolicyapplicable(projectData.getPolicyApplicable());
        detailsResource.setTechnology(projectData.getTechnology());

        if (projectData.getProjectCapacityUnitSize() != null)
            detailsResource.setProjectcapacityunitsize(projectData.getProjectCapacityUnitSize().toString());
        else
            detailsResource.setProjectcapacityunitsize("0");

        if (projectData.getProjectCapacityUnitMeasure() != null)
        detailsResource.setProjectcapacityunitmeasure(projectData.getProjectCapacityUnitMeasure());
        else
            detailsResource.setProjectcapacityunitmeasure("");

        if (projectData.getNumberOfUnits() != null)
            detailsResource.setNumberofunits(projectData.getNumberOfUnits().toString());
        else
            detailsResource.setNumberofunits("0");

        if (projectData.getDesignPlfCuf() != null && projectData.getDesignPlfCuf().length() > 0)
            detailsResource.setDesignplfcuf(projectData.getDesignPlfCuf().toString());
        else
            detailsResource.setDesignplfcuf("0.0");


        Partner partner = new Partner();

        if (projectData.getMainContractor() != null && projectData.getMainContractor().length() > 0) {
            UUID partnerIdMainContractor = UUID.fromString(projectData.getMainContractor());
              partner = partnerRepository.getOne(partnerIdMainContractor);
            if (partner != null) {
                if (partner.getPartyNumber() != null)
                    detailsResource.setMaincontractor(partner.getPartyNumber().toString());
                else
                    detailsResource.setMaincontractor(" ");
            } else
                detailsResource.setMaincontractor(" ");
        } else
            detailsResource.setMaincontractor(" ");

        if (projectData.getEpcContractor() != null && projectData.getEpcContractor().length() > 0) {
            UUID partnerIdEpcContractor = UUID.fromString(projectData.getEpcContractor());
            partner = partnerRepository.getOne(partnerIdEpcContractor);
            if (partner != null) {
                if (partner.getPartyNumber() != null)
                    detailsResource.setEpccontractor(partner.getPartyNumber().toString());
                else
                    detailsResource.setEpccontractor(" ");
            } else
                detailsResource.setEpccontractor(" ");
        } else
            detailsResource.setEpccontractor(" ");

        detailsResource.setResourceassessmentagency(projectData.getResourceAssessmentAgency());
        detailsResource.setOandmcontractor(projectData.getOandmContractor());
        if (projectData.getOfftakeVolume() != null)
            detailsResource.setOfftakevolume(projectData.getOfftakeVolume().toString());
        else
            detailsResource.setOfftakevolume("0.0");

        if (projectData.getSaleRate() != null && projectData.getSaleRate().length() > 0)
            detailsResource.setSalerate(projectData.getSaleRate().toString());
        else
            detailsResource.setSalerate("0.0");

        detailsResource.setFuelmix(projectData.getFuelMix());

        if (projectData.getFuelCost() != null)
            detailsResource.setFuelcost(projectData.getFuelCost().toString());
        else
            detailsResource.setFuelcost("0.0");

        if (projectData.getStationHeatRate() != null)
            detailsResource.setStationheatrate(dataConversionUtility.convertAmountToString(projectData.getStationHeatRate()));
        else
            detailsResource.setStationheatrate("0.0");

        if (projectData.getOandmExpenses() != null)
            detailsResource.setOandmexpenses(projectData.getOandmExpenses().toString());
        else
            detailsResource.setOandmexpenses("0.0");

        if (projectData.getTotalLand() != null)
            detailsResource.setTotalland(projectData.getTotalLand().toString());
        else
            detailsResource.setTotalland("0.0");

        if (projectData.getProjectCOD() != null)
            detailsResource.setProjectcod(dataConversionUtility.convertDateToSAPFormat(projectData.getProjectCOD()));
        else
            detailsResource.setProjectcod(null);

        if (projectData.getPpaRate() != null)
            detailsResource.setPparate(projectData.getPpaRate().toString());
        else
            detailsResource.setPparate("0.0");

        detailsResource.setOfftakercompany(projectData.getOffTakerCompany());

        if (projectData.getIppPercentage() != null)
            detailsResource.setIpppercentage(projectData.getIppPercentage().toString());
        else
            detailsResource.setIpppercentage("0.0");

        if (projectData.getGroupCaptivePercentage() != null)
            detailsResource.setGroupcaptivepercentage(projectData.getGroupCaptivePercentage().toString());
        else
            detailsResource.setGroupcaptivepercentage("0.0");

        if (projectData.getGroupCaptivePercentage() != null)
            detailsResource.setGroupcaptivepercentage(projectData.getGroupCaptivePercentage().toString());
        else
            detailsResource.setGroupcaptivepercentage("0.0");

         if (projectData.getThirdPartyPercentage() != null)
            detailsResource.setThirdpartypercentage(projectData.getThirdPartyPercentage().toString());
        else
            detailsResource.setThirdpartypercentage("0.0");

         if (projectData.getMarketPercentage() != null)
            detailsResource.setMarketpercentage(projectData.getMarketPercentage().toString());
        else
            detailsResource.setMarketpercentage("0.0");



         if (projectData.getEpcCost() != null) {

             detailsResource.setEpccost(dataConversionUtility.convertAmountToString(projectData.getEpcCost()) );
         }else
             detailsResource.setEpccost("0.0");


        if (projectData.getOverallProjectCost() != null)
            detailsResource.setOverallprojectcost(dataConversionUtility.convertAmountToString(projectData.getOverallProjectCost()));
        else
            detailsResource.setOverallprojectcost("0.0");


        if (projectData.getDebtEquityRatio() != null) //TODO
            detailsResource.setDebtequityratio(projectData.getDebtEquityRatio());
        else
            detailsResource.setDebtequityratio("0.0");
        if (detailsResource.getDebtequityratio().length() == 0)
            detailsResource.setDebtequityratio("0.0");

         if (projectData.getTotalDebt() != null) //TODO
            detailsResource.setTotaldebt(projectData.getTotalDebt());
         else
             detailsResource.setTotaldebt("0.0");
         if (detailsResource.getTotaldebt().length() == 0)
             detailsResource.setTotaldebt("0.0");

         if (projectData.getRoiPreCod() != null)
            detailsResource.setRoiprecod(projectData.getRoiPreCod().toString());
         else
            detailsResource.setRoiprecod("0.0");

        if (projectData.getRoiPostCod() != null)
            detailsResource.setRoipostcod(projectData.getRoiPostCod().toString());
        else
            detailsResource.setRoipostcod("0.0");

        if(projectData.getConstructionPeriod() != null)
            detailsResource.setConstructionperiod(projectData.getConstructionPeriod().toString());
        else
            detailsResource.setConstructionperiod("0");


        if(projectData.getConstructionPeriodUnit() != null)
            detailsResource.setConstructionperiodunit(projectData.getConstructionPeriodUnit());

        if(projectData.getMoratoriumPeriod() != null)
            detailsResource.setMoratoriumperiod(projectData.getMoratoriumPeriod().toString());
        else
            detailsResource.setMoratoriumperiod("0");

         if (projectData.getMoratoriumPeriodUnit() != null)
            detailsResource.setMoratoriumperiodunit(projectData.getMoratoriumPeriodUnit());

        if (projectData.getTenorPeriod() != null)
            detailsResource.setTenorperiod( projectData.getTenorPeriod().toString());
        else
            detailsResource.setTenorperiod("0");

        if(projectData.getTenorUnit() != null)
            detailsResource.setTenorunit(projectData.getTenorUnit());

        detailsResource.setRepaymentschedule(projectData.getRepaymentSchedule());

        if (projectData.getDscrMinimum() != null)
        detailsResource.setDscrminimum(projectData.getDscrMinimum().toString());
        else
            detailsResource.setDscrminimum("0.0");

        if (projectData.getDscrAverage() != null)
            detailsResource.setDscraverage(projectData.getDscrAverage().toString());
        else
            detailsResource.setDscraverage("0.0");

        if (projectData.getLevCostTotal() !=null)
            detailsResource.setLevcosttotal(projectData.getLevCostTotal().toString());
        else
            detailsResource.setLevcosttotal("0.0");

         if (projectData.getLevCostFixed() != null)
            detailsResource.setLevcostfixed(dataConversionUtility.convertAmountToString(projectData.getLevCostFixed()));
         else
             detailsResource.setLevcostfixed("0.0");

        if (projectData.getLevCostVariable() != null)
            detailsResource.setLevcostvariable(projectData.getLevCostVariable().toString());
        else
            detailsResource.setLevcostvariable("0.0");

         if (projectData.getWorkingCapitalCycle() != null)
            detailsResource.setWorkingcapitalcycle(projectData.getWorkingCapitalCycle().toString());
         else
             detailsResource.setWorkingcapitalcycle("0");


         if (projectData.getWorkingCapitalUnit() != null)
            detailsResource.setWorkingcapitalunit(projectData.getWorkingCapitalUnit());

        return detailsResource;
    }


}
