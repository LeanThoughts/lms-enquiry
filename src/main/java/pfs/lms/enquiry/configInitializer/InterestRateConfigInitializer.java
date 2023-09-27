package pfs.lms.enquiry.configInitializer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.domain.*;
import pfs.lms.enquiry.repository.*;
import pfs.lms.enquiry.repository.*;

/**
 * Created by sajeev on 14-May-21.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InterestRateConfigInitializer implements CommandLineRunner {


    @Autowired
    ReferenceInterestRateRepository referenceInterestRateRepository;

    @Autowired
    PaymentFormRepository paymentFormRepository;

    @Autowired
    ConditionTypeRepository conditionTypeRepository;

    @Autowired
    ReferenceInterestSignRepository referenceInterestSignRepository;

    @Autowired
    InterestCalculationMethodRepository interestCalculationMethodRepository;

    @Override
    public void run(String... strings) throws Exception {

        ReferenceInterestRate referenceInterestRate = referenceInterestRateRepository.findByCode("PFS RATE");
        if (referenceInterestRate == null) {
            referenceInterestRate = new ReferenceInterestRate(null, "PFS RATE", "PFS Base Rate");
            referenceInterestRateRepository.save(referenceInterestRate);
        }
        referenceInterestRate = referenceInterestRateRepository.findByCode("AXIS RATE");
        if (referenceInterestRate == null) {
            referenceInterestRate = new ReferenceInterestRate(null, "AXIS RATE", "AXIS Base Rate");
            referenceInterestRateRepository.save(referenceInterestRate);
        }

        PaymentForm paymentForm = paymentFormRepository.findByCode("JM");
        if (paymentForm == null) {
            paymentForm = new PaymentForm(null,"JM","Mid-year");
            paymentFormRepository.save(paymentForm);
        }
        paymentForm = paymentFormRepository.findByCode("JN");
        if (paymentForm == null) {
            paymentForm = new PaymentForm(null,"JN","Yearly at end of period");
            paymentFormRepository.save(paymentForm);
        }
        paymentForm = paymentFormRepository.findByCode("JV");
        if (paymentForm == null) {
            paymentForm = new PaymentForm(null,"JV","Yearly at start of period");
            paymentFormRepository.save(paymentForm);
        }
        paymentForm = paymentFormRepository.findByCode("MM");
        if (paymentForm == null) {
            paymentForm = new PaymentForm(null,"MM","Middle of the month");
            paymentFormRepository.save(paymentForm);
        }
        paymentForm = paymentFormRepository.findByCode("MN");
        if (paymentForm == null) {
            paymentForm = new PaymentForm(null,"MN","Monthly at end of period");
            paymentFormRepository.save(paymentForm);
        }
        paymentForm = paymentFormRepository.findByCode("MV");
        if (paymentForm == null) {
            paymentForm = new PaymentForm(null,"MV","Monthly at start of period");
            paymentFormRepository.save(paymentForm);
        }
        paymentForm = paymentFormRepository.findByCode("QM");
        if (paymentForm == null) {
            paymentForm = new PaymentForm(null,"QM","Quarterly at mid of period");
            paymentFormRepository.save(paymentForm);
        }
        paymentForm = paymentFormRepository.findByCode("V5");
        if (paymentForm == null) {
            paymentForm = new PaymentForm(null,"V5","Quarterly at start + 5 days");
            paymentFormRepository.save(paymentForm);
        }
        paymentForm = paymentFormRepository.findByCode("VN");
        if (paymentForm == null) {
            paymentForm = new PaymentForm(null,"VN","Quarterly at end of period");
            paymentFormRepository.save(paymentForm);
        }
        paymentForm = paymentFormRepository.findByCode("VV");
        if (paymentForm == null) {
            paymentForm = new PaymentForm(null,"VV","Quarterly at start of period");
            paymentFormRepository.save(paymentForm);
        }
        paymentForm = paymentFormRepository.findByCode("Z1");
        if (paymentForm == null) {
            paymentForm = new PaymentForm(null,"Z1","monthly at end + 15 days");
            paymentFormRepository.save(paymentForm);
        }
        paymentForm = paymentFormRepository.findByCode("Z2");
        if (paymentForm == null) {
            paymentForm = new PaymentForm(null,"Z2","Quarterly at end + 15 days");
            paymentFormRepository.save(paymentForm);
        }

         ConditionType conditionType = conditionTypeRepository.findByCode("2603");
        if(conditionType == null) {
            conditionType = new ConditionType(null,"2603","IOA for Principal");
            conditionTypeRepository.save(conditionType);
        }
        conditionType = conditionTypeRepository.findByCode("2605");
        if(conditionType == null) {
            conditionType = new ConditionType(null,"2605","IOA on Interest");
            conditionTypeRepository.save(conditionType);
        }
        conditionType = conditionTypeRepository.findByCode("2607");
        if(conditionType == null) {
            conditionType = new ConditionType(null,"2607","IOA on IOA");
            conditionTypeRepository.save(conditionType);
        }
        conditionType = conditionTypeRepository.findByCode("201");
        if(conditionType == null) {
            conditionType = new ConditionType(null,"201","Nominal interest");
            conditionTypeRepository.save(conditionType);
        }

//        conditionType = conditionTypeRepository.findByCode("9011");
//        if(conditionType == null) {
//            conditionType = new ConditionType(null,"9011","Additional interest");
//            conditionTypeRepository.save(conditionType);
//        }

        conditionType = conditionTypeRepository.findByCode("2609");
        if(conditionType == null) {
            conditionType = new ConditionType(null,"2609","Deferral interest");
            conditionTypeRepository.save(conditionType);
        }

        conditionType = conditionTypeRepository.findByCode("9012");
        if(conditionType == null) {
            conditionType = new ConditionType(null,"9012","Further interest");
            conditionTypeRepository.save(conditionType);
        }

       ReferenceInterestSign referenceInterestSign = referenceInterestSignRepository.findByCode("+");
        if(referenceInterestSign == null) {
            referenceInterestSign = new ReferenceInterestSign(null,"+","Plus");
            referenceInterestSignRepository.save(referenceInterestSign);
        }
        referenceInterestSign = referenceInterestSignRepository.findByCode("-");
        if(referenceInterestSign == null) {
            referenceInterestSign = new ReferenceInterestSign(null,"-","Minus");
            referenceInterestSignRepository.save(referenceInterestSign);
        }
        referenceInterestSign = referenceInterestSignRepository.findByCode("* ");
        if(referenceInterestSign == null) {
            referenceInterestSign = new ReferenceInterestSign(null,"* ","Multiply");
            referenceInterestSignRepository.save(referenceInterestSign);
        }

        InterestCalculationMethod
                interestCalculationMethod = interestCalculationMethodRepository.findByCode("");
                if (interestCalculationMethod == null) {
                    interestCalculationMethod = new InterestCalculationMethod(null,"","");
                    interestCalculationMethodRepository.save(interestCalculationMethod);
                }
        interestCalculationMethod = interestCalculationMethodRepository.findByCode("1");
        if (interestCalculationMethod == null) {
            interestCalculationMethod = new InterestCalculationMethod(null,"1","360E/360");
            interestCalculationMethodRepository.save(interestCalculationMethod);
        }
        interestCalculationMethod = interestCalculationMethodRepository.findByCode("B");
        if (interestCalculationMethod == null) {
            interestCalculationMethod = new InterestCalculationMethod(null," B","360E/365");
            interestCalculationMethodRepository.save(interestCalculationMethod);
        }
        interestCalculationMethod = interestCalculationMethodRepository.findByCode("L");
        if (interestCalculationMethod == null) {
            interestCalculationMethod = new InterestCalculationMethod(null,"L","360/360 (German)");
            interestCalculationMethodRepository.save(interestCalculationMethod);
        }
        interestCalculationMethod = interestCalculationMethodRepository.findByCode("F");
        if (interestCalculationMethod == null) {
            interestCalculationMethod = new InterestCalculationMethod(null,"F","360/360 (ISDA)");
            interestCalculationMethodRepository.save(interestCalculationMethod);
        }
        interestCalculationMethod = interestCalculationMethodRepository.findByCode("H");
        if (interestCalculationMethod == null) {
            interestCalculationMethod = new InterestCalculationMethod(null,"H","360/365 (ISDA)");
            interestCalculationMethodRepository.save(interestCalculationMethod);
        }
        interestCalculationMethod = interestCalculationMethodRepository.findByCode("7");
        if (interestCalculationMethod == null) {
            interestCalculationMethod = new InterestCalculationMethod(null,"7","360/360");
            interestCalculationMethodRepository.save(interestCalculationMethod);
        }
        interestCalculationMethod = interestCalculationMethodRepository.findByCode("2");
        if (interestCalculationMethod == null) {
            interestCalculationMethod = new InterestCalculationMethod(null,"2","act/360");
            interestCalculationMethodRepository.save(interestCalculationMethod);
        }
        interestCalculationMethod = interestCalculationMethodRepository.findByCode("E");
        if (interestCalculationMethod == null) {
            interestCalculationMethod = new InterestCalculationMethod(null,"E","act/364");
            interestCalculationMethodRepository.save(interestCalculationMethod);
        }
        interestCalculationMethod = interestCalculationMethodRepository.findByCode("3");
        if (interestCalculationMethod == null) {
            interestCalculationMethod = new InterestCalculationMethod(null,"3","act/365");
            interestCalculationMethodRepository.save(interestCalculationMethod);
        }
        interestCalculationMethod = interestCalculationMethodRepository.findByCode("4");
        if (interestCalculationMethod == null) {
            interestCalculationMethod = new InterestCalculationMethod(null,"4","act/366");
            interestCalculationMethodRepository.save(interestCalculationMethod);
        }
        interestCalculationMethod = interestCalculationMethodRepository.findByCode("M");
        if (interestCalculationMethod == null) {
            interestCalculationMethod = new InterestCalculationMethod(null,"M","act/365P");
            interestCalculationMethodRepository.save(interestCalculationMethod);
        }
        interestCalculationMethod = interestCalculationMethodRepository.findByCode("5");
        if (interestCalculationMethod == null) {
            interestCalculationMethod = new InterestCalculationMethod(null,"5","act/actP (ICMA)");
            interestCalculationMethodRepository.save(interestCalculationMethod);
        }

        interestCalculationMethod = interestCalculationMethodRepository.findByCode("6");
        if (interestCalculationMethod == null) {
            interestCalculationMethod = new InterestCalculationMethod(null,"6","act/actY (ISDA)");
            interestCalculationMethodRepository.save(interestCalculationMethod);
        }
        interestCalculationMethod = interestCalculationMethodRepository.findByCode("G");
        if (interestCalculationMethod == null) {
            interestCalculationMethod = new InterestCalculationMethod(null,"G","act/actE (AFB)");
            interestCalculationMethodRepository.save(interestCalculationMethod);
        }
        interestCalculationMethod = interestCalculationMethodRepository.findByCode("Q");
        if (interestCalculationMethod == null) {
            interestCalculationMethod = new InterestCalculationMethod(null,"Q","act/actEP (AFB)");
            interestCalculationMethodRepository.save(interestCalculationMethod);
        }
        interestCalculationMethod = interestCalculationMethodRepository.findByCode("N");
        if (interestCalculationMethod == null) {
            interestCalculationMethod = new InterestCalculationMethod(null,"N","act/365L");
            interestCalculationMethodRepository.save(interestCalculationMethod);
        }
        interestCalculationMethod = interestCalculationMethodRepository.findByCode("A");
        if (interestCalculationMethod == null) {
            interestCalculationMethod = new InterestCalculationMethod(null,"A","actW/252");
            interestCalculationMethodRepository.save(interestCalculationMethod);
        }
        interestCalculationMethod = interestCalculationMethodRepository.findByCode("D");
        if (interestCalculationMethod == null) {
            interestCalculationMethod = new InterestCalculationMethod(null,"D","365/360");
            interestCalculationMethodRepository.save(interestCalculationMethod);
        }
        interestCalculationMethod = interestCalculationMethodRepository.findByCode("C");
        if (interestCalculationMethod == null) {
            interestCalculationMethod = new InterestCalculationMethod(null,"C","365/365");
            interestCalculationMethodRepository.save(interestCalculationMethod);
        }
        interestCalculationMethod = interestCalculationMethodRepository.findByCode("I");
        if (interestCalculationMethod == null) {
            interestCalculationMethod = new InterestCalculationMethod(null,"I","360E/actY");
            interestCalculationMethodRepository.save(interestCalculationMethod);
        }
        interestCalculationMethod = interestCalculationMethodRepository.findByCode("8");
        if (interestCalculationMethod == null) {
            interestCalculationMethod = new InterestCalculationMethod(null,"8","30.42E/360");
            interestCalculationMethodRepository.save(interestCalculationMethod);
        }
        interestCalculationMethod = interestCalculationMethodRepository.findByCode("9");
        if (interestCalculationMethod == null) {
            interestCalculationMethod = new InterestCalculationMethod(null,"9","*365.25/360");
            interestCalculationMethodRepository.save(interestCalculationMethod);
        }
        interestCalculationMethod = interestCalculationMethodRepository.findByCode("P");
        if (interestCalculationMethod == null) {
            interestCalculationMethod = new InterestCalculationMethod(null,"P","1/1");
            interestCalculationMethodRepository.save(interestCalculationMethod);
        }


    }

}
