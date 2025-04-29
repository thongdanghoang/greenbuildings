import {NgModule} from '@angular/core';
import {SharedModule} from '@shared/shared.module';
import {AccountInformationComponent} from './components/account-information/account-information.component';
import {ActivityTypeComponent} from './components/activity-type/activity-type.component';
import {BuildingDetailsComponent} from './components/building-details/building-details.component';
import {BuildingGroupDetailComponent} from './components/building-group-detail/building-group-detail.component';
import {BuildingManagementComponent} from './components/building-management/building-management.component';
import {BuildingPopupMarkerComponent} from './components/building-popup-marker/building-popup-marker.component';
import {BuildingsComponent} from './components/buildings/buildings.component';
import {CreateEnterpriseComponent} from './components/create-enterprise/create-enterprise.component';
import {CreateTenantComponent} from './components/create-tenant/create-tenant.component';
import {EmissionActivityDetailComponent} from './components/emission-activity-detail/emission-activity-detail.component';
import {EmissionActivityComponent} from './components/emission-activity/emission-activity.component';
import {EnterpriseInvitationComponent} from './components/enterprise-invitation/enterprise-invitation.component';
import {ManageTenantComponent} from './components/manage-tenant/manage-tenant.component';
import {NewBuildingGroupComponent} from './components/new-building-group/new-building-group.component';
import {NewTenantComponent} from './components/new-tenant/new-tenant.component';
import {PaymentComponent} from './components/payment/payment.component';
import {PlanComponent} from './components/plan/plan.component';
import {SentInvitationComponent} from './components/sent-invitation/sent-invitation.component';
import {TenantProfileComponent} from './components/tenant-profile/tenant-profile.component';
import {ActivityTypeDialogComponent} from './dialog/activity-type-dialog/activity-type-dialog.component';
import {BuildingSubscriptionDialogComponent} from './dialog/building-subcription-dialog/building-subscription-dialog.component';
import {CreditDeductionHistoryDialogComponent} from './dialog/credit-deduction-history-dialog/credit-deduction-history-dialog.component';
import {CreditPackageGuideDialogComponent} from './dialog/credit-package-guide-dialog/credit-package-guide-dialog.component';
import {NewActivityDialogComponent} from './dialog/new-activity-dialog/new-activity-dialog.component';
import {NewActivityRecordDialogComponent} from './dialog/new-activity-record-dialog/new-activity-record-dialog.component';
import {PaymentDetailDialogComponent} from './dialog/payment-detail-dialog/payment-detail-dialog.component';
import {ReportDialogComponent} from './dialog/report-dialog/report-dialog.component';
import {EnterpriseRoutingModule} from './enterprise-routing.module';
import {EnterpriseComponent} from './enterprise.component';
import {MarkerService} from './services/marker.service';
import {PopupService} from './services/popup.service';
import {RegionService} from './services/region.service';

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
    ActivityTypeDialogComponent,
    BuildingManagementComponent,
    NewTenantComponent,
    NewBuildingGroupComponent,
    PaymentDetailDialogComponent,
    BuildingGroupDetailComponent,
    CreditDeductionHistoryDialogComponent,
    CreditPackageGuideDialogComponent,
    CreateTenantComponent,
    ManageTenantComponent,
    SentInvitationComponent,
    TenantProfileComponent
  ],
  imports: [SharedModule, EnterpriseRoutingModule],
  providers: [MarkerService, PopupService, RegionService]
})
export class EnterpriseModule {}
