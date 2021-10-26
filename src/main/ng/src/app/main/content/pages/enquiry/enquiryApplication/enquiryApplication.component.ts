import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {
  AbstractControl,
  FormBuilder, FormControl, FormControlDirective, FormControlName, FormGroup, FormGroupDirective, NgForm, ValidatorFn,
  Validators
} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {
  MatDialog,
  MatStepper,
  DateAdapter,
  NativeDateAdapter,
  MAT_DATE_LOCALE,
  MAT_DATE_FORMATS
} from '@angular/material';
import {EnquiryApplicationRegEx} from '../../../others/enquiryApplication.regEx';
import {MessageDialogComponent} from '../../../components/messageDialog/messageDialog.component';
import {FuseNavigationService} from '@fuse/components/navigation/navigation.service';
import {LoanEnquiryService} from '../enquiryApplication.service';
import {PartnerModel} from 'app/main/content/model/partner.model';
import {UserService} from '../../administration/user/user.service';
import {PartnerService} from "../../administration/partner/partner.service";
import {UserModel} from "../../../model/user.model";
import {HttpClient} from "selenium-webdriver/http";
import {Location} from '@angular/common';
 import {Observable} from "rxjs/Rx";
 import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {forEach} from "@angular/router/src/utils/collection";
import {map, startWith} from 'rxjs/operators';
import {MatInputModule} from '@angular/material';


export interface ApplicantEmail {
  email: string;
  nameAndAddress: string;
}

@Component({
  selector: 'fuse-enquiry-application-component',
  templateUrl: './enquiryApplication.component.html',
  styleUrls: ['./enquiryApplication.component.scss'],
  encapsulation: ViewEncapsulation.None
})




export class EnquiryApplicationComponent implements OnInit {

  projectNameLength = 0;
  loanPurposeLength = 0;


  // Applicants Email and NameAddress
  applicantEmailFilteredOptions: Observable<ApplicantEmail[]>;
  applicantEmails : Array<ApplicantEmail>;
  applicantEmailFormControl = new FormControl();

  partnerNameFormControl = new FormControl();
  partnerIdFormControl = new FormControl();

  partnerNameFilteredOptions: Observable<PartnerModel[]>;
  partnerIdFilteredOptions: Observable<PartnerModel[]>;
  
  partners: Array<PartnerModel>;

  loanEnquiryFormStep1: FormGroup;
  loanEnquiryFormStep2: FormGroup;
  loanEnquiryFormStep3: FormGroup;
  applicantEmailFormGroup: FormGroup;


  loanClasses: Array<any>;
  financingTypes: Array<any>;
  projectTypes: Array<any>;
  assistanceTypes: Array<any>;
  states: Array<string>;

  industrySectors: Array<any>;
  unitOfMeasures: Array<any>;


  minDate = new Date();

  validUserId: boolean;
  partner: PartnerModel = new PartnerModel({});
  user: UserModel = new UserModel({});
  email: string;

  /**
   * constructor()
   * @param _route
   * @param _formBuilder
   * @param _dialogRef
   * @param _location
   * @param _loanEnquiryService
   * @param _userService
   * @param _partnerService
   * @param _router
   * @param _navigationService
   */
  constructor(_route: ActivatedRoute, private _formBuilder: FormBuilder, private _dialogRef: MatDialog,
              private _loanEnquiryService: LoanEnquiryService, private _userService: UserService,
              private _partnerService: PartnerService, private _location: Location,
              private _router: Router, private _navigationService: FuseNavigationService) {

    this.validUserId = false;

    console.log('_route.snapshot.data.routeResolvedData[5]', _route.snapshot.data.routeResolvedData[5]);
    let partner;
    if (_route.snapshot.data.routeResolvedData[5] === null) {
        partner = new PartnerModel({});
    }
    else {
        partner = new PartnerModel(_route.snapshot.data.routeResolvedData[5]);
    }
    console.log('partner', partner);

    // Set min value of scheduled cod to tomorrow's date.
    this.minDate.setDate(this.minDate.getDate() + 1);

    // this.applicantEmailFormGroup = this._formBuilder.group({
    //   applicantEmail: '' , disabled: true //,  disabled:false //disabled:this.isCurrentUserAdmin()
    // });

    this.applicantEmailFormGroup = new FormGroup({
      applicantEmail: new FormControl({applicantEmail: ' ', disabled: true},)
    });

    // Initialize the forms.
    this.loanEnquiryFormStep1 = this._formBuilder.group({
      projectName: [''],
      loanClass: [''],
      financingType: [''],
      projectType: [''],
      projectCapacity: ['', [Validators.pattern(EnquiryApplicationRegEx.projectCapacity), Validators.min(1), Validators.max(999999.99)]],
      projectCapacityUnit:[''],
      assistanceType: [''],
      tenorYear: ['', [Validators.pattern(EnquiryApplicationRegEx.tenorYear)]],
      tenorMonth: ['', [Validators.max(11), Validators.pattern(EnquiryApplicationRegEx.tenorMonth)]],
      projectLocationState: [''],
      projectDistrict: [''],
      projectCost: ['', [Validators.pattern(EnquiryApplicationRegEx.projectCost)]],
      equity: ['', [Validators.pattern(EnquiryApplicationRegEx.equity), Validators.max(99999)]],
      projectDebtAmount: ['', [Validators.pattern(EnquiryApplicationRegEx.projectDebtAmount)]],
      pfsDebtAmount: ['', [Validators.pattern(EnquiryApplicationRegEx.pfsDebtAmount)]],
      expectedSubDebt: ['', [Validators.pattern(EnquiryApplicationRegEx.expectedSubDebt)]],
      pfsSubDebtAmount: ['', [Validators.pattern(EnquiryApplicationRegEx.pfsSubDebtAmount)]],
      loanPurpose: [''],
      leadFIName: [''],
      leadFILoanAmount: ['', [Validators.pattern(EnquiryApplicationRegEx.leadFILoanAmount)]],
      expectedInterestRate: ['', [Validators.pattern(EnquiryApplicationRegEx.expectedInterestRate)]],
      scheduledCOD: ['']
    });

    if (this._navigationService.currentUser.role == 'TR0100') {
      this.loanEnquiryFormStep2 = this._formBuilder.group({
        partyName1: [partner.partyName1],
        partyName2: [partner.partyName2],
        contactPersonName: [partner.contactPersonName],
        addressLine1: [partner.addressLine1 || ''],
        addressLine2: [partner.addressLine2],
        street: [partner.street],
        city: [partner.city],
        state: [partner.state],
        postalCode: [partner.postalCode],
        //  email: [_navigationService.currentUser.email, [Validators.pattern(EnquiryApplicationRegEx.email) , checkValidUserId ]],
        email: [_navigationService.currentUser.email, [Validators.pattern(EnquiryApplicationRegEx.email)]],

        contactNumber: [partner.contactNumber],
        pan: [partner.pan, [Validators.pattern(EnquiryApplicationRegEx.pan)]],
        groupCompany: [partner.groupCompany],
        industrySector: [partner.industrySector]

      });
    } else {
      this.loanEnquiryFormStep2 = this._formBuilder.group({
        partyName1: [''],
        partyName2: [''],
        contactPersonName: [''],
        addressLine1: [''],
        addressLine2: [''],
        street: [''],
        city: [''],
        state: [''],
        postalCode: [''],
        email: ['', [Validators.pattern(EnquiryApplicationRegEx.email)]],
        contactNumber: [''],
        pan: ['', [Validators.pattern(EnquiryApplicationRegEx.pan)]],
        groupCompany: [''],
        industrySector: ['']
      });
    }

    this.loanEnquiryFormStep3 = this._formBuilder.group({
      promoterName: [''],
      promoterAreaOfBusinessNature: [''],
      promoterNetWorthAmount: ['', [Validators.pattern(EnquiryApplicationRegEx.borrowerNetWorth)]],
      promoterPATAmount: ['', [Validators.pattern(EnquiryApplicationRegEx.borrowerPAT)]],

      //rating: ['', [Validators.max(100)]],
      rating: [''],

      promoterKeyDirector: [''],

      contactBranchAddress: [''],
      contactDesignation: [''],
      contactDepartment: [''],
      contactTelePhone: [''],
      contactLandLinePhone: [''],
      contactEmail: ['',[Validators.pattern(EnquiryApplicationRegEx.email)]],
      contactFaxNumber: ['']
    });

    // Initialize dropdowns.
    this.loanClasses = _route.snapshot.data.routeResolvedData[0]._embedded.loanClasses;
    this.financingTypes = _route.snapshot.data.routeResolvedData[1]._embedded.financingTypes;
    this.projectTypes = _route.snapshot.data.routeResolvedData[2]._embedded.projectTypes;
    this.states = _route.snapshot.data.routeResolvedData[3];
    this.assistanceTypes = _route.snapshot.data.routeResolvedData[4]._embedded.assistanceTypes;
    this.industrySectors = _route.snapshot.data.routeResolvedData[6]._embedded.industrySectors;

    this.unitOfMeasures = _route.snapshot.data.routeResolvedData[7]._embedded.unitOfMeasures;
    this.applicantEmails = _route.snapshot.data.routeResolvedData[8];

    // for (let entry of this.unitOfMeasures) {
    //   console.log("UNIT" + entry.value);
    // }

    this.partners = _route.snapshot.data.routeResolvedData[10];
    // Check for nulls and convert partynumber to string
    this.partners.map(x => {
        if (!x.partyNumber)
            x.partyNumber = '';
        else
            x.partyNumber = x.partyNumber.toString();
        if (!x.addressLine1) x.addressLine1 = '';
        if (!x.addressLine2) x.addressLine2 = '';
        if (!x.street) x.street = '';
        if (!x.city) x.city = '';
    })
  }



  clearFrom(){
    this.loanEnquiryFormStep1.reset();
    this.loanEnquiryFormStep2.reset();
    this.loanEnquiryFormStep3.reset();


  }

  /**
   * ngOnInit()
   */
  ngOnInit() {

    // Project Name Length
    this.projectNameLength = this.loanEnquiryFormStep1.value.projectName.length;
    // Purpose Length
    this.loanPurposeLength = this.loanEnquiryFormStep1.value.loanPurpose.length;

    this.applicantEmailFilteredOptions = this.applicantEmailFormControl.valueChanges
      .pipe(
        startWith(''),
        map(email => email ? this._filterStates(email) : this.applicantEmails.slice())
      );

    this.partnerNameFilteredOptions = this.partnerNameFormControl.valueChanges
        .pipe(
        startWith(''),
        map(name => name ? this._filterPartnersByName(name) : this.partners.slice())
    );

    this.partnerIdFilteredOptions = this.partnerIdFormControl.valueChanges
        .pipe(
        startWith(''),
        map(name => name ? this._filterPartnersById(name) : this.partners.slice())
    );
  }

  onProjectNameChange() {
    this.projectNameLength = this.loanEnquiryFormStep1.value.projectName.length;
  }
  onLoanPurposeChange() {
    this.loanPurposeLength = this.loanEnquiryFormStep1.value.loanPurpose.length;
  }


  private _filterStates(value: string): ApplicantEmail[] {
    const filterValue = value.toLowerCase();

    return this.applicantEmails.filter(applicantEmail => applicantEmail.email.toLowerCase().indexOf(filterValue) === 0);
  }

    private _filterPartnersByName(value: string): PartnerModel[] {
        const filterValue = value.toLowerCase();
        return this.partners.filter(partner => partner.partyName1.toLowerCase().indexOf(filterValue) === 0);
    }

    private _filterPartnersById(value: string): PartnerModel[] {
        const filterValue = value.toLowerCase();
        return this.partners.filter(partner => {
            if (partner.partyNumber.trim() !== '') {
                return partner.partyNumber.toLowerCase().indexOf(filterValue) === 0
            }
            else
                return false;
        });
    }

  backButtonClick(): void {
    this._location.back();
  }
  /**
   * saveLoanApplication()
   * This method is invoked when the user clicks on Finish on the last step of the loan application form.
   */
  saveLoanApplication(stepper: MatStepper): void {

    // Re-construct the partner object.
    const partner = this.loanEnquiryFormStep2.value;

    // Re-construct the loan application object.
    const loanApplication = this.loanEnquiryFormStep1.value;
    const promoterAndContact = this.loanEnquiryFormStep3.value;
    loanApplication.promoterName = promoterAndContact.promoterName;
    loanApplication.promoterAreaOfBusinessNature = promoterAndContact.promoterAreaOfBusinessNature;
    loanApplication.promoterNetWorthAmount = promoterAndContact.promoterNetWorthAmount;
    loanApplication.promoterPATAmount = promoterAndContact.promoterPATAmount;
    loanApplication.rating = promoterAndContact.rating;
    loanApplication.promoterKeyDirector = promoterAndContact.promoterKeyDirector;


    loanApplication.contactBranchAddress = promoterAndContact.contactBranchAddress;
    loanApplication.contactDesignation = promoterAndContact.contactDesignation;
    loanApplication.contactDepartment = promoterAndContact.contactDepartment;
    loanApplication.contactTelePhone =promoterAndContact.contactTelePhone;
    loanApplication.contactLandLinePhone = promoterAndContact.contactLandLinePhone;
    loanApplication.contactEmail =  promoterAndContact.contactEmail;
    loanApplication.contactFaxNumber = promoterAndContact.contactFaxNumber;



    // To solve the utc time zone issue
    const scheduledCOD = new Date(loanApplication.scheduledCOD);
    loanApplication.scheduledCOD = new Date(Date.UTC(scheduledCOD.getFullYear(), scheduledCOD.getMonth(), scheduledCOD.getDate()));

    // Save the loan application to the database.
    this._loanEnquiryService.saveLoanApplication(loanApplication, partner).subscribe((response) => {
      // Display alert message and redirect to enquiry alerts page.
      const dialogRef = this._dialogRef.open(MessageDialogComponent, {
        width: '400px',
        data: {
          message: 'Your Loan Enquiry ' + response.enquiryNo.id + ' is submitted to PFS Loan Officer.'
        }
      });
      dialogRef.afterClosed().subscribe((dresponse) => {
        this._router.navigate(['/enquiryAlerts']);
      });
    });
  }


  validateUserId($event) {

    if (this.applicantEmailFormControl.value !== '' && this.partnerNameFormControl.value === '' && this.partnerIdFormControl.value === '') {

        let emailId = $event.target.value;
        this.email = emailId;

        // Get User By Email partner.

        this._userService.getUserByEmail(emailId).subscribe((result) => {
        this.user = result;

        if (this.user != undefined || this.user != null) {
            this.validUserId = true;

            // Initialize partner.
            this.partner = new PartnerModel({});
            this.partner.email = this.user.email;
            this.partner.partyName1 = this.user.firstName;
            this.partner.partyName2 = this.user.lastName;
            this.validUserId = true;

            this.initializePartnerForm(true); // Load partner form with user details only


            this._partnerService.getPartnerByEmail(emailId).subscribe((result) => {
            this.partner = result;

            if (this.partner != undefined || this.partner != null) {
                this.loadPartnerForm();
            } else {
                this.partner = new PartnerModel({});
                this.partner.email = this.user.email;
                this.partner.partyName1 = this.user.firstName;
                this.partner.partyName2 = this.user.lastName;
            }
            });
        } else {
            this.validUserId = false;
            this.initializePartnerForm(false); // Clear partner form
        }
        });
    }
  }

    validatePartnerById($event) {
        if ($event.target.value.trim() !== '') {
            this.applicantEmailFormControl.setValue('');
            this.partnerNameFormControl.setValue('');
            const filteredPartners = this.partners.filter(partner => partner.partyNumber.localeCompare($event.target.value) === 0);
            if (filteredPartners.length > 0) {
                this.partner = filteredPartners[0];
                this.loadPartnerForm();
            }
            else {
                this.partner = new PartnerModel({});
                this.partner.email = this.user.email;
                this.partner.partyName1 = this.user.firstName;
                this.partner.partyName2 = this.user.lastName;
            }
        }
    }

    validatePartnerByName($event) {
        if ($event.target.value.trim() !== '') {
            this.applicantEmailFormControl.setValue('');
            this.partnerIdFormControl.setValue('');
            const filteredPartners = this.partners.filter(partner => partner.partyName1.toLowerCase().localeCompare(
                $event.target.value.toLowerCase()) === 0);
            if (filteredPartners.length > 0) {
                this.partner = filteredPartners[0];
                this.loadPartnerForm();
            }
            else {
                this.partner = new PartnerModel({});
                this.partner.email = this.user.email;
                this.partner.partyName1 = this.user.firstName;
                this.partner.partyName2 = this.user.lastName;
            }
        }
    }


  // Initialize the form loanEnquiryFormStep2.
  loadPartnerForm(): void {

    this.loanEnquiryFormStep2 = this._formBuilder.group({
      partyName1: [this.partner.partyName1],
      partyName2: [this.partner.partyName2],
      contactPersonName: [this.partner.contactPersonName],
      addressLine1: [this.partner.addressLine1],
      addressLine2: [this.partner.addressLine2],
      street: [this.partner.street],
      city: [this.partner.city],
      state: [this.partner.state],
      postalCode: [this.partner.postalCode],
      email: [this.partner.email, [Validators.pattern(EnquiryApplicationRegEx.email), validateUser(true)]],
      contactNumber: [this.partner.contactNumber],
      pan: [this.partner.pan, [Validators.pattern(EnquiryApplicationRegEx.pan)]],
      groupCompany: [this.partner.groupCompany],
      industrySector: [this.partner.industrySector]

    });
  }

  // Initialize the form loanEnquiryFormStep2.
  initializePartnerForm(isValidUser: boolean): void {

    this.loanEnquiryFormStep2 = this._formBuilder.group({
      partyName1: [this.partner.partyName1],
      partyName2: [this.partner.partyName2],
      contactPersonName: [''],
      addressLine1: [''],
      addressLine2: [''],
      street: [''],
      city: [''],
      state: [''],
      postalCode: [''],
      email: [this.email, [Validators.pattern(EnquiryApplicationRegEx.email), validateUser(isValidUser)]],
      contactNumber: [''],
      pan: [' ', [Validators.pattern(EnquiryApplicationRegEx.pan)]],
      groupCompany: [''],
      industrySector: ['']
    });
  }

    getPartyAddress(partner: PartnerModel): string {
        let str = partner.addressLine1.trim();
        str = str + ' ' + partner.addressLine2;
        str = str.trim() + ' ' + partner.street;
        str = str.trim() + ' ' + partner.city;
        return str.trim();;
    }

  /**
   * isCurrentUserAdmin()
   * Returns true is user is admin.
   */
  isCurrentUserAdmin(): boolean {

    // let control = this.applicantEmailFormControl.get('applicantEmail')
    // control.disabled ? control.enable() : control.disable();

    if (this._navigationService.currentUser.role === 'ZLM013' || this._navigationService.currentUser.role === 'ZLM010'
      || this._navigationService.currentUser.role === 'ZLM023') {
      //console.log("Is Admin............... TRUE");
      return true;
    }
    else {
      //console.log("Is Admin............... FALSE");
      return false;
    }
  }

}

//TODO
// Move this later to a common validator module
function validateUser(isValid: any) {

  return (control: AbstractControl): { [key: string]: any } | null => {

    if (isValid) {
      //   console.log("Is Valid :" + isValid);
      return null;
    }
    else {
      // console.log("Valid :" + isValid);
      return {'invalidUser': true};

    }
  };


}


