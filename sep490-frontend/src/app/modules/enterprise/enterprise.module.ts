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
import {CreditPackageService} from './services/credit-package.service';
import {EmissionActivityRecordService} from './services/emission-activity-record.service';
import {MarkerService} from './services/marker.service';
import {PaymentService} from './services/payment.service';
import {PopupService} from './services/popup.service';
import {RegionService} from './services/region.service';
import {SubscriptionService} from './services/subscription.service';
import {WalletService} from './services/wallet.service';
import {BuildingPopupMarkerComponent} from './components/building-popup-marker/building-popup-marker.component';
import {EmissionActivityComponent} from './components/emission-activity/emission-activity.component';
import {EmissionActivityService} from './services/emission-activity.service';
import {NewActivityDialogComponent} from './dialog/new-activity-dialog/new-activity-dialog.component';

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
    NewActivityRecordDialogComponent
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
    EmissionActivityService,
    EmissionActivityRecordService
  ]
})
export class EnterpriseModule {}
