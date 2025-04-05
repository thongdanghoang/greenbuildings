import {NgModule} from '@angular/core';
import {SharedModule} from '../shared/shared.module';
import {AccountInformationComponent} from './components/account-information/account-information.component';
import {BuildingDetailsComponent} from './components/building-details/building-details.component';
import {BuildingPopupMarkerComponent} from './components/building-popup-marker/building-popup-marker.component';
import {BuildingsComponent} from './components/buildings/buildings.component';
import {CreateEnterpriseComponent} from './components/create-enterprise/create-enterprise.component';
import {EmissionActivityDetailComponent} from './components/emission-activity-detail/emission-activity-detail.component';
import {EmissionActivityComponent} from './components/emission-activity/emission-activity.component';
import {EnterpriseInvitationComponent} from './components/enterprise-invitation/enterprise-invitation.component';
import {PaymentComponent} from './components/payment/payment.component';
import {PlanComponent} from './components/plan/plan.component';
import {BuildingSubscriptionDialogComponent} from './dialog/building-subcription-dialog/building-subscription-dialog.component';
import {NewActivityDialogComponent} from './dialog/new-activity-dialog/new-activity-dialog.component';
import {NewActivityRecordDialogComponent} from './dialog/new-activity-record-dialog/new-activity-record-dialog.component';
import {ReportDialogComponent} from './dialog/report-dialog/report-dialog.component';
import {EnterpriseRoutingModule} from './enterprise-routing.module';
import {EnterpriseComponent} from './enterprise.component';
import {CreditPackageService} from './services/credit-package.service';
import {EmissionActivityRecordService} from './services/emission-activity-record.service';
import {EmissionActivityService} from './services/emission-activity.service';
import {PaymentService} from './services/payment.service';
import {SubscriptionService} from './services/subscription.service';
import {WalletService} from './services/wallet.service';

@NgModule({
  declarations: [
    EnterpriseComponent,
    PlanComponent,
    PaymentComponent,
    BuildingsComponent,
    BuildingDetailsComponent,
    BuildingPopupMarkerComponent,
    BuildingSubscriptionDialogComponent,
    NewActivityDialogComponent,
    EmissionActivityComponent,
    EmissionActivityDetailComponent,
    NewActivityRecordDialogComponent,
    ReportDialogComponent,
    AccountInformationComponent,
    CreateEnterpriseComponent,
    EnterpriseInvitationComponent
  ],
  imports: [SharedModule, EnterpriseRoutingModule],
  providers: [
    PaymentService,
    WalletService,
    CreditPackageService,
    SubscriptionService,
    EmissionActivityService,
    EmissionActivityRecordService
  ]
})
export class EnterpriseModule {}
