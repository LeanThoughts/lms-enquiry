import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {HttpClientModule} from '@angular/common/http';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {RouterModule, Routes} from '@angular/router';
import {MatMomentDateModule} from '@angular/material-moment-adapter';
import {DateAdapter, MatButtonModule, MatIconModule, MatSnackBarModule} from '@angular/material';
import {TranslateModule} from '@ngx-translate/core';
import 'hammerjs';

import {FuseModule} from '@fuse/fuse.module';
import {FuseSharedModule} from '@fuse/shared.module';
import {FuseProgressBarModule, FuseSidebarModule, FuseThemeOptionsModule} from '@fuse/components';

import {fuseConfig} from 'app/fuse-config';

import {AppComponent} from 'app/app.component';
import {LayoutModule} from 'app/layout/layout.module';
import {SampleModule} from 'app/main/sample/sample.module';
import {EnquiryApplicationModule} from './main/content/pages/enquiry/enquiryApplication/enquiryApplication.module';
import {EnquiryAlertsModule} from './main/content/pages/enquiry/enquiryAlerts/enquiryAlerts.module';
import {EnquiryListModule} from './main/content/pages/enquiry/enquirySearch/enquirySearch.module';
import {LocalDateFormat} from './main/content/others/localDate.Format';
import {MessageDialogModule} from './main/content/components/messageDialog/messageDialog.module';
import {AppService} from './app.service';
import {Register2Module} from './main/content/pages/registration/register.module';
import {UserModule} from './main/content/pages/administration/user/user.module';
import { ForgotPassword2Module } from './main/content/pages/forgotPassword/forgotPassword.module';
import { APP_BASE_HREF } from '@angular/common';
 import {PartnerModule} from "./main/content/pages/administration/partner/partner.module";
import { MailRepoComponent } from './main/content/pages/administration/mail-repo/mail-repo.component';
import {MailRepoModule} from "./main/content/pages/administration/mail-repo/mail-repo.module";
import { ChangePasswordModule } from './main/content/pages/changePassword/changePassword.module';
import { LoanContractsSearchModule } from './main/content/pages/loanContractsSearch/loanContractsSearch.module';
import { LoanMonitoringModule } from './main/content/pages/monitoring/loanMonitoring.module';
import { InboxModule } from './main/content/pages/inbox/inbox.module';
import { ChangeDocumentModule } from './main/content/pages/changeDocument/change-document.module';
import { NgIdleKeepaliveModule } from '@ng-idle/keepalive';
import { LoanAppraisalModule } from './main/content/pages/appraisal/loanAppraisal.module';
import { InboxComponent } from './main/content/pages/inbox/inbox.component';
import { EnquiryActionModule } from './main/content/pages/enquiryAction/enquiryAction.module';
import { BoardApprovalModule } from './main/content/pages/boardApproval/boardApproval.module';

const appRoutes: Routes = [
    {
        path: '',
        component: InboxComponent,
        canActivate: [
            AppService
        ]
    },
    {
        path: '**',
        redirectTo: 'enquiryApplication'
    }
];

@NgModule({
    declarations: [
        AppComponent
    ],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        HttpClientModule,
        RouterModule.forRoot(appRoutes),

        TranslateModule.forRoot(),

        // Material moment date module
        MatMomentDateModule,

        // Material
        MatButtonModule,
        MatIconModule,
        MatSnackBarModule,

        // Fuse modules
        FuseModule.forRoot(fuseConfig),
        FuseProgressBarModule,
        FuseSharedModule,
        FuseSidebarModule,
        FuseThemeOptionsModule,

        // App modules
        LayoutModule,
        SampleModule,

        // User modules
        EnquiryApplicationModule,
        EnquiryAlertsModule,
        EnquiryListModule,
        MessageDialogModule,
        UserModule,
        Register2Module,
        ForgotPassword2Module,
        ChangePasswordModule,
        PartnerModule,
        MailRepoModule,

        // Monitoring
        LoanContractsSearchModule,
        LoanMonitoringModule,
        LoanAppraisalModule,
        EnquiryActionModule,
        BoardApprovalModule,

        // Workflow
        InboxModule,
        ChangeDocumentModule,

        // Ng-idel
        NgIdleKeepaliveModule.forRoot()
    ],
    bootstrap: [
        AppComponent
    ],
    providers: [
        { provide: DateAdapter, useClass: LocalDateFormat },
        { provide: APP_BASE_HREF, useValue: '/enquiry'},
        AppService
    ],
})
export class AppModule {
}
