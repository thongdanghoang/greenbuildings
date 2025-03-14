import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AppRoutingConstants} from '../../app-routing.constant';
import {BuildingDetailsComponent} from './components/building-details/building-details.component';
import {BuildingsComponent} from './components/buildings/buildings.component';
import {EmissionActivityComponent} from './components/emission-activity/emission-activity.component';
import {PaymentComponent} from './components/payment/payment.component';
import {PlanComponent} from './components/plan/plan.component';
import {EnterpriseComponent} from './enterprise.component';

const routes: Routes = [
  {
    path: '',
    component: EnterpriseComponent,
    children: [
      {
        path: AppRoutingConstants.BUILDING_PATH,
        component: BuildingsComponent
      },
      {
        path: `${AppRoutingConstants.BUILDING_PATH}/:id`,
        component: BuildingDetailsComponent
      },
      {
        path: AppRoutingConstants.PLAN_PATH,
        component: PlanComponent
      },
      {path: AppRoutingConstants.PAYMENT_PATH, component: PaymentComponent},
      {
        path: `${AppRoutingConstants.EMISSION_ACTIVITY_PATH}/:buildingId`,
        component: EmissionActivityComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class EnterpriseRoutingModule {}
