import {NgModule} from '@angular/core';
import {SharedModule} from '../shared/shared.module';
import {BuildingDetailsComponent} from './components/building-details/building-details.component';
import {BuildingsComponent} from './components/buildings/buildings.component';
import {PaymentComponent} from './components/payment/payment.component';
import {PlanComponent} from './components/plan/plan.component';
import {BuildingSubscriptionDialogComponent} from './dialog/building-subcription-dialog/building-subscription-dialog.component';
import {EnterpriseRoutingModule} from './enterprise-routing.module';
import {EnterpriseComponent} from './enterprise.component';
import {CreditPackageService} from './services/credit-package.service';
import {MarkerService} from './services/marker.service';
import {PaymentService} from './services/payment.service';
import {PopupService} from './services/popup.service';
import {RegionService} from './services/region.service';
import {SubscriptionService} from './services/subscription.service';
import {WalletService} from './services/wallet.service';
import {BuildingPopupMarkerComponent} from './components/building-popup-marker/building-popup-marker.component';

@NgModule({
  declarations: [
    EnterpriseComponent,
    PlanComponent,
    PaymentComponent,
    BuildingsComponent,
    BuildingDetailsComponent,
    BuildingPopupMarkerComponent,
    BuildingSubscriptionDialogComponent
  ],
  imports: [SharedModule, EnterpriseRoutingModule],
  providers: [
    MarkerService,
    PopupService,
    RegionService,
    PaymentService,
    WalletService,
    CreditPackageService,
    SubscriptionService
  ]
})
export class EnterpriseModule {}
