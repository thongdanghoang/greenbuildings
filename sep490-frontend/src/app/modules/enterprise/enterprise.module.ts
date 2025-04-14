import {NgModule} from '@angular/core';
import {SharedModule} from '../shared/shared.module';
import {BuildingDetailsComponent} from './components/building-details/building-details.component';
import {BuildingsComponent} from './components/buildings/buildings.component';
import {EmissionActivityDetailComponent} from './components/emission-activity-detail/emission-activity-detail.component';
import {PaymentComponent} from './components/payment/payment.component';
import {PlanComponent} from './components/plan/plan.component';
import {BuildingSubscriptionDialogComponent} from './dialog/building-subcription-dialog/building-subscription-dialog.component';
import {NewActivityRecordDialogComponent} from './dialog/new-activity-record-dialog/new-activity-record-dialog.component';
import {EnterpriseRoutingModule} from './enterprise-routing.module';
import {EnterpriseComponent} from './enterprise.component';
import {BuildingGroupService} from './services/building-group.service';
import {CreditPackageService} from './services/credit-package.service';
import {CurrencyConverterService} from './services/currency-converter.service';
import {EmissionActivityRecordService} from './services/emission-activity-record.service';
import {GroupItemService} from './services/group-item.service';
import {MarkerService} from './services/marker.service';
import {PaymentService} from './services/payment.service';
import {PopupService} from './services/popup.service';
import {RegionService} from './services/region.service';
import {SubscriptionService} from './services/subscription.service';
import {TenantService} from './services/tenant.service';
import {WalletService} from './services/wallet.service';
import {BuildingPopupMarkerComponent} from './components/building-popup-marker/building-popup-marker.component';
import {EmissionActivityComponent} from './components/emission-activity/emission-activity.component';
import {EmissionActivityService} from './services/emission-activity.service';
import {NewActivityDialogComponent} from './dialog/new-activity-dialog/new-activity-dialog.component';
import {ReportDialogComponent} from './dialog/report-dialog/report-dialog.component';
import {AccountInformationComponent} from './components/account-information/account-information.component';
import {CreateEnterpriseComponent} from './components/create-enterprise/create-enterprise.component';
import {EnterpriseInvitationComponent} from './components/enterprise-invitation/enterprise-invitation.component';
import {ActivityTypeComponent} from './components/activity-type/activity-type.component';
import {ActivityTypeDialogComponent} from './dialog/activity-type-dialog/activity-type-dialog.component';
import {BuildingManagementComponent} from './components/building-management/building-management.component';
import {PaymentDetailDialogComponent} from './dialog/payment-detail-dialog/payment-detail-dialog.component';
import {CreditDeductionHistoryDialogComponent} from './dialog/credit-deduction-history-dialog/credit-deduction-history-dialog.component';
import {TransactionService} from './services/transaction.service';
import {CreditPackageGuideDialogComponent} from './dialog/credit-package-guide-dialog/credit-package-guide-dialog.component';

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
    EnterpriseInvitationComponent,
    ActivityTypeComponent,
    BuildingManagementComponent,
    ActivityTypeDialogComponent,
    PaymentDetailDialogComponent,
    CreditDeductionHistoryDialogComponent,
    CreditPackageGuideDialogComponent
  ],
  imports: [SharedModule, EnterpriseRoutingModule],
  providers: [
    MarkerService,
    PopupService,
    RegionService,
    PaymentService,
    WalletService,
    CreditPackageService,
    SubscriptionService,
    BuildingGroupService,
    TenantService,
    GroupItemService,
    EmissionActivityService,
    EmissionActivityRecordService,
    CurrencyConverterService,
    TransactionService
  ]
})
export class EnterpriseModule {}
