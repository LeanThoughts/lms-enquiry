import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {AbstractControl, FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';
import {MatDialog, MatStepper, MAT_DATE_LOCALE, MatSnackBar} from '@angular/material';
import {Location} from '@angular/common';
import {fuseAnimations} from '@fuse/animations';
import {EnquiryAlertsService} from '../enquiryAlerts.service';
import {LoanApplicationModel} from '../../../../model/loanApplication.model';
import {PartnerModel} from '../../../../model/partner.model';
import {EnquiryRejectDialogComponent} from '../enquiryReject/enquiryReject.component';
import {EnquiryApplicationRegEx} from '../../../../others/enquiryApplication.regEx';
import {MessageDialogComponent} from '../../../../components/messageDialog/messageDialog.component';
import {EnquiryApprovalDialogComponent} from '../enquiryApproval/enquiryApproval.component';
import {FuseNavigationService} from '../../../../../../../@fuse/components/navigation/navigation.service';
import {UserModel} from "../../../../model/user.model";
import {UserService} from "../../../administration/user/user.service";
import {PartnerService} from "../../../administration/partner/partner.service";
import {Observable} from 'rxjs';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {forEach} from "@angular/router/src/utils/collection";
import {map, startWith} from 'rxjs/operators';
import {MatInputModule} from '@angular/material';



export interface ApplicantEmail {
  email: string;
  nameAndAddress: string;
}


@Component({
  selector: 'fuse-enquiry-review',
  templateUrl: './enquiryReview.component.html',
  styleUrls: ['./enquiryReview.component.scss'],
  animations: fuseAnimations,
  encapsulation: ViewEncapsulation.None
})
export class EnquiryReviewComponent implements OnInit {


  projectNameLength = 0;
  loanPurposeLength = 0;

  // Applicants Email and NameAddress
  applicantEmailFormControl = new FormControl();
  applicantEmailFilteredOptions: Observable<ApplicantEmail[]>;
  applicantEmails : Array<ApplicantEmail>;

  partnerNameFormControl = new FormControl();
  partnerIdFormControl = new FormControl();

  partnerNameFilteredOptions: Observable<PartnerModel[]>;
  partnerIdFilteredOptions: Observable<PartnerModel[]>;

  partners: Array<PartnerModel>;

  loanEnquiryFormStep1: FormGroup;
  loanEnquiryFormStep2: FormGroup;
  loanEnquiryFormStep3: FormGroup;
  loanEnquiryFormStep4: FormGroup;
   applicantEmailFormGroup: FormGroup;

  loanClasses: Array<any>;
  financingTypes: Array<any>;
  projectTypes: Array<any>;
  assistanceTypes: Array<any>;
  states: Array<string>;
  industrySectors: Array<any>;
  unitOfMeasures: Array<any>;

  loanApplication: LoanApplicationModel;
  //   partner: PartnerModel;

  minDate = new Date();

  validUserId: boolean;
  partner: PartnerModel = new PartnerModel({});
  user: UserModel = new UserModel({});
  email: string;

  readonlyFields: boolean;
    selectionMethod: string;

   /**
   * constructor()
   * @param _route
   * @param _formBuilder
   */
  constructor(_route: ActivatedRoute, private _formBuilder: FormBuilder, private _dialogRef: MatDialog,
              private _enquiryAlertsService: EnquiryAlertsService, private _location: Location,
              private _userService: UserService, private _partnerService: PartnerService,
              private _navigationService: FuseNavigationService, private matSnackBar: MatSnackBar) {




     // Set min value of scheduled cod to tomorrow's date.
    this.minDate.setDate(this.minDate.getDate() + 1);

    this.isCurrentUserAdmin();

    // Initialize loanApplication.
    this.loanApplication = _route.snapshot.data.routeResolvedData[5];
    //this.partner = _route.snapshot.data.routeResolvedData[8];

    // Disable the Form
    if (this.loanApplication.technicalStatus == '4' || this.loanApplication.technicalStatus == '5' || this.loanApplication.technicalStatus == '6') {
        this.readonlyFields = true;
    }
    else {
        this.readonlyFields = false;
    }
    console.log('readonlyFields', this.readonlyFields);

    // Initialize partner.
    this.partner = new PartnerModel({});
    this._enquiryAlertsService.getPartner(this.loanApplication.loanApplicant).subscribe((result) => {

      //console.log("Inside GET PARTNER : loanApplicant" + this.loanApplication.loanApplicant);
      this.partner = result;
      //console.log("Inside GET PARTNER : Partner Number" + this.partner.partyNumber);

      this.initializePartnerForm(); // Refresh the partner form with data.
    });

    // Initialize the forms.
    this.initializeLoanApplicationForms();
    this.initializePartnerForm();


    // this.applicantEmailFormGroup = this._formBuilder.group({
    //   applicantEmail: '' , disabled: true //,  disabled:false //disabled:this.isCurrentUserAdmin()
    // });

    this.applicantEmailFormGroup = new FormGroup({
      applicantEmail: new FormControl({applicantEmail: ' ', disabled: true},)
     });


    // Initialize dropdowns.
    this.loanClasses = _route.snapshot.data.routeResolvedData[0]._embedded.loanClasses;
    this.financingTypes = _route.snapshot.data.routeResolvedData[1]._embedded.financingTypes;
    this.projectTypes = _route.snapshot.data.routeResolvedData[2]._embedded.projectTypes;
    this.states = _route.snapshot.data.routeResolvedData[3];
    this.assistanceTypes = _route.snapshot.data.routeResolvedData[4]._embedded.assistanceTypes;
    this.industrySectors = _route.snapshot.data.routeResolvedData[6]._embedded.industrySectors;
    this.unitOfMeasures = _route.snapshot.data.routeResolvedData[7]._embedded.unitOfMeasures;
   // this.applicantGroups = _route.snapshot.data.routeResolvedData[8].partnersOrderedByAlphabet;
    this.applicantEmails = _route.snapshot.data.routeResolvedData[8];
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

    // Initialize the forms loanEnquiryFormStep1, loanEnquiryFormStep3.
  initializeLoanApplicationForms(): void {

    this.loanEnquiryFormStep1 = this._formBuilder.group({
      projectName: [this.loanApplication.projectName],
      loanClass: [this.loanApplication.loanClass],
      financingType: [this.loanApplication.financingType],
      projectType: [this.loanApplication.projectType],
      projectCapacity: [this.loanApplication.projectCapacity],
      projectCapacityUnit:[this.loanApplication.projectCapacityUnit],
      assistanceType: [this.loanApplication.assistanceType],
      tenorYear: [this.loanApplication.tenorYear, [Validators.pattern(EnquiryApplicationRegEx.tenorYear)]],
      tenorMonth: [this.loanApplication.tenorMonth, [Validators.max(11), Validators.pattern(EnquiryApplicationRegEx.tenorMonth)]],
      projectLocationState: [this.loanApplication.projectLocationState],
      projectDistrict: [this.loanApplication.projectDistrict],
      projectCost: [this.loanApplication.projectCost, [Validators.pattern(EnquiryApplicationRegEx.projectCost)]],
      equity: [this.loanApplication.equity, [Validators.pattern(EnquiryApplicationRegEx.equity), Validators.max(9999)]],
      projectDebtAmount: [this.loanApplication.projectDebtAmount, [Validators.pattern(EnquiryApplicationRegEx.projectDebtAmount)]],
      pfsDebtAmount: [this.loanApplication.pfsDebtAmount, [Validators.pattern(EnquiryApplicationRegEx.pfsDebtAmount)]],
      expectedSubDebt: [this.loanApplication.expectedSubDebt, [Validators.pattern(EnquiryApplicationRegEx.expectedSubDebt)]],
      pfsSubDebtAmount: [this.loanApplication.pfsSubDebtAmount, [Validators.pattern(EnquiryApplicationRegEx.pfsSubDebtAmount)]],
      loanPurpose: [this.loanApplication.loanPurpose],
      leadFIName: [this.loanApplication.leadFIName],
      leadFILoanAmount: [this.loanApplication.leadFILoanAmount, [Validators.pattern(EnquiryApplicationRegEx.leadFILoanAmount)]],
      expectedInterestRate: [this.loanApplication.expectedInterestRate, [Validators.pattern(
        EnquiryApplicationRegEx.expectedInterestRate)]],
      scheduledCOD: [this.loanApplication.scheduledCOD]
    });

    this.loanEnquiryFormStep3 = this._formBuilder.group({
      promoterName: [this.loanApplication.promoterName],
      promoterAreaOfBusinessNature: [this.loanApplication.promoterAreaOfBusinessNature],
      promoterNetWorthAmount: [this.loanApplication.promoterNetWorthAmount, [Validators.pattern(EnquiryApplicationRegEx.borrowerNetWorth)]],
      promoterPATAmount: [this.loanApplication.promoterPATAmount, [Validators.pattern(EnquiryApplicationRegEx.borrowerPAT)]],
      //rating: [this.loanApplication.rating, [Validators.max(100)]],
      rating: [this.loanApplication.rating],
      promoterKeyDirector: [this.loanApplication.promoterKeyDirector],

      contactBranchAddress: [this.loanApplication.contactBranchAddress],
      contactDesignation: [this.loanApplication.contactDesignation],
      contactDepartment: [this.loanApplication.contactDepartment],
      contactTelePhone: [this.loanApplication.contactTelePhone],
      contactLandLinePhone: [this.loanApplication.contactLandLinePhone],
      contactEmail: [this.loanApplication.contactEmail],
      contactFaxNumber: [this.loanApplication.contactFaxNumber]
    });

    this.loanEnquiryFormStep4 = this._formBuilder.group({
      enquiryNo: [this.loanApplication.enquiryNo.id],
      loanContractId: [this.loanApplication.loanContractId],
      partyNumber: [this.loanApplication.busPartnerNumber]


    });
  }

  // Initialize the form loanEnquiryFormStep1.
  initializePartnerForm(): void {


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
      email: [this.partner.email],
      contactNumber: [this.partner.contactNumber],
      pan: [this.partner.pan, [Validators.pattern(EnquiryApplicationRegEx.pan)]],
      groupCompany: [this.partner.groupCompany],
      industrySector: [this.partner.industrySector]
    });
  }


  // clear the form loanEnquiryFormStep2.
  loadPartnerFormWithUser(): void {

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
      email: [this.partner.email, [Validators.pattern(EnquiryApplicationRegEx.email), validateUser(true)]],
      contactNumber: [''],
      pan: [' ', [Validators.pattern(EnquiryApplicationRegEx.pan)]],
      groupCompany: [''],
      industrySector: ['']

    });
  }

  // clear the form loanEnquiryFormStep2.
  clearPartnerForm(): void {

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
      email: [this.email, [Validators.pattern(EnquiryApplicationRegEx.email), validateUser(false)]],
      contactNumber: [''],
      pan: [' ', [Validators.pattern(EnquiryApplicationRegEx.pan)]],
      groupCompany: [''],
      industrySector: ['']

    });
  }

  backButtonClick(): void {
    this._location.back();
  }

  getProjectName(): string{
    return this.loanApplication.projectName;
  }

    validatePartnerById($event) {
        if ($event.target.value.trim() !== '') {
            this.applicantEmailFormControl.setValue('');
            this.partnerNameFormControl.setValue('');
            const filteredPartners = this.partners.filter(partner => partner.partyNumber.localeCompare($event.target.value) === 0);
            if (filteredPartners.length > 0) {
                this.partner = filteredPartners[0];
                this.partnerNameFormControl.setValue(this.partner.partyName1);
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
                this.partnerIdFormControl.setValue(this.partner.partyNumber);
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

    validateUserId($event) {
        if (this.applicantEmailFormControl.value !== '' && this.partnerNameFormControl.value === '' && this.partnerIdFormControl.value === '') {
            let emailId = $event.target.value;
            this.email = emailId;
            this.partnerNameFormControl.setValue('');
            this.partnerIdFormControl.setValue('');
            // Get User By Email partner.
            this._userService.getUserByEmail(emailId).subscribe((result) => {
                this.user = result;
                console.log(this.user);
                if (this.user !== undefined || this.user != null) {
                    this.validUserId = true;
                    // Initialize partner.
                    this.partner = new PartnerModel({});
                    this.partner.email = this.user.email;
                    this.partner.partyName1 = this.user.firstName;
                    this.partner.partyName2 = this.user.lastName;
                    this.validUserId = true;
                    this.loadPartnerFormWithUser();

                    this._partnerService.getPartnerByEmail(emailId).subscribe((result) => {
                        this.partner = result;

                        if (this.partner != undefined || this.partner != null) {
                            this.loadPartnerForm();
                        } 
                        else {
                            this.partner = new PartnerModel({});
                            this.partner.email = this.user.email;
                            this.partner.partyName1 = this.user.firstName;
                            this.partner.partyName2 = this.user.lastName;
                        }
                    });
                } 
                else {
                    this.validUserId = false;
                    this.clearPartnerForm(); // Clear partner form
                }
            }); 
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

  /**
   * saveLoanApplication()
   * This method is invoked when the user clicks on Finish on the last step of the loan application form.
   */
  saveLoanApplication(stepper: MatStepper): void {
    // Re-construct the partner object.
    this.reconstructPartner();

    // Re-construct the loan application object.
    this.reconstructLoanApplication();

    // Update the loan application and redirect back to the enquiry alerts list.
    this._enquiryAlertsService.updateLoanApplication(this.loanApplication, this.partner).subscribe(() => {
      // Display alert message and redirect to enquiry alerts page.
      const dialogRef = this._dialogRef.open(MessageDialogComponent, {
        width: '400px',
        data: {
          message: 'Your loan enquiry is updated.',
        }
      });
      dialogRef.afterClosed().subscribe(() => {
        this._location.back();
      });
    });
  }

  /**
   *
   */
  reconstructLoanApplication(): void {
    // Reconstruct loanApplication with loanEnquiryFormStep1 values.
    const loanApplication = this.loanEnquiryFormStep1.value;
    this.loanApplication.projectName = loanApplication.projectName;
    this.loanApplication.loanClass = loanApplication.loanClass;
    this.loanApplication.financingType = loanApplication.financingType;
    this.loanApplication.projectType = loanApplication.projectType;
    this.loanApplication.projectCapacity = loanApplication.projectCapacity;
    this.loanApplication.projectCapacityUnit = loanApplication.projectCapacityUnit;
    this.loanApplication.assistanceType = loanApplication.assistanceType;
    this.loanApplication.tenorYear = loanApplication.tenorYear;
    this.loanApplication.tenorMonth = loanApplication.tenorMonth;
    this.loanApplication.projectLocationState = loanApplication.projectLocationState;
    this.loanApplication.projectDistrict = loanApplication.projectDistrict;
    this.loanApplication.projectCost = loanApplication.projectCost;
    this.loanApplication.equity = loanApplication.equity;
    this.loanApplication.projectDebtAmount = loanApplication.projectDebtAmount;
    this.loanApplication.pfsDebtAmount = loanApplication.pfsDebtAmount;
    this.loanApplication.expectedSubDebt = loanApplication.expectedSubDebt;
    this.loanApplication.pfsSubDebtAmount = loanApplication.pfsSubDebtAmount;
    this.loanApplication.loanPurpose = loanApplication.loanPurpose;
    this.loanApplication.leadFIName = loanApplication.leadFIName;
    this.loanApplication.leadFILoanAmount = loanApplication.leadFILoanAmount;
    this.loanApplication.expectedInterestRate = loanApplication.expectedInterestRate;

    // To solve the utc time zone issue
    if (loanApplication.scheduledCOD !== null) {
      const scheduledCOD = new Date(loanApplication.scheduledCOD);
      this.loanApplication.scheduledCOD = new Date(Date.UTC(scheduledCOD.getFullYear(), scheduledCOD.getMonth(), scheduledCOD.getDate()));
    }

    // Reconstruct loanApplication with loanEnquiryFormStep3 values.
    const promoterAndContact = this.loanEnquiryFormStep3.value;
    this.loanApplication.promoterName = promoterAndContact.promoterName;
    this.loanApplication.promoterAreaOfBusinessNature = promoterAndContact.promoterAreaOfBusinessNature;
    this.loanApplication.promoterNetWorthAmount = promoterAndContact.promoterNetWorthAmount;
    this.loanApplication.promoterPATAmount = promoterAndContact.promoterPATAmount;
    this.loanApplication.rating = promoterAndContact.rating;
    this.loanApplication.promoterKeyDirector = promoterAndContact.promoterKeyDirector;


    this.loanApplication.contactBranchAddress = promoterAndContact.contactBranchAddress;
    this.loanApplication.contactDesignation = promoterAndContact.contactDesignation;
    this.loanApplication.contactDepartment = promoterAndContact.contactDepartment;
    this.loanApplication.contactTelePhone =promoterAndContact.contactTelePhone;
    this.loanApplication.contactLandLinePhone = promoterAndContact.contactLandLinePhone;
    this.loanApplication.contactEmail =  promoterAndContact.contactEmail;
    this.loanApplication.contactFaxNumber = promoterAndContact.contactFaxNumber;
;
  }

  /**
   *
   */
  reconstructPartner(): void {
    const partner = this.loanEnquiryFormStep2.value;


    this.partner.addressLine1 = partner.addressLine1;
    this.partner.addressLine2 = partner.addressLine2;
    this.partner.city = partner.city;
    this.partner.contactNumber = partner.contactNumber;
    this.partner.contactPersonName = partner.contactPersonName;
    this.partner.email = partner.email;
    this.partner.groupCompany = partner.groupCompany;
    this.partner.pan = partner.pan;
    this.partner.partyName1 = partner.partyName1;
    this.partner.partyName2 = partner.partyName2;
    this.partner.postalCode = partner.postalCode;
    this.partner.state = partner.state;
    this.partner.street = partner.street;
    this.partner.industrySector = partner.industrySector;
  }

  /**
   * rejectEnquiry()
   */
  rejectEnquiry(): void {
    // Open the dialog.
    const dialogRef = this._dialogRef.open(EnquiryRejectDialogComponent, {
      panelClass: 'fuse-enquiry-reject-dialog',
      width: '600px',
      data: {
        loanApplication: this.loanApplication
      }
    });
    // Subscribe to the dialog close event to intercept the action taken.
    dialogRef.afterClosed().subscribe((result) => {
      if (result !== undefined && result.action !== 'Cancel') {
        this._location.back();
      }
    });
  }

  /**
   * approveEnquiry()
   */
  approveEnquiry(): void {
    if (this.loanEnquiryFormStep1.valid && this.loanEnquiryFormStep2.valid && this.loanEnquiryFormStep3.valid) {

      // Re-construct the partner object.
      this.reconstructPartner();

      // Re-construct the loan application object.
      this.reconstructLoanApplication();

      // Update the loan application and launch the loan approval dialog.
      this._enquiryAlertsService.updateLoanApplication(this.loanApplication, this.partner).subscribe(() => {
        // Open the loan approval dialog.
        const dialogRef = this._dialogRef.open(EnquiryApprovalDialogComponent, {
          panelClass: 'fuse-enquiry-approval-dialog',
          width: '600px',
          data: {
            loanApplication: this.loanApplication,
            partner: this.partner
          }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => {
          if (result !== undefined && result.action !== 'Cancel') {
            this._location.back();
          }
        });
      });
    }
    else {
      this.matSnackBar.open('There were some errors in the data you have entered. Please provide valid input and try again', 'OK',
        {duration: 7000});
    }
  }

  /**
   * withdrawApplication()
   * This method cancels a loan application.
   */
  withdrawApplication(): void {
    // Open the confirmation dialog.
    const dialogRef = this._dialogRef.open(MessageDialogComponent, {
      width: '400px',
      data: {
        message: 'Are you sure you want to cancel the loan application?',
      }
    });
    dialogRef.afterClosed().subscribe((response) => {
      if (response === 'OK') {
        this._enquiryAlertsService.cancelEnquiry(this.loanApplication).subscribe(() => {
          this._location.back();
        });
      }
    });
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

    getPartyAddress(partner: PartnerModel): string {
        let str = partner.addressLine1.trim();
        str = str + ' ' + partner.addressLine2;
        str = str.trim() + ' ' + partner.street;
        str = str.trim() + ' ' + partner.city;
        return str.trim();;
    }
}

//TODO
// Move this later to a common validator module
function validateUser(isValid: any) {

  return (control: AbstractControl): { [key: string]: any } | null => {

    if (isValid) {
     // console.log("Is Valid :" + isValid);
      return null;
    }
    else {
      console.log("Valid :" + isValid);
    //  return {'invalidUser': true};

    }
  };
}
