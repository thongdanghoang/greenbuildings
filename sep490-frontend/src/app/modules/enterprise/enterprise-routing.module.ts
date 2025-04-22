import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AppRoutingConstants} from '../../app-routing.constant';
import {AccountInformationComponent} from './components/account-information/account-information.component';
import {BuildingDetailsComponent} from './components/building-details/building-details.component';
import {BuildingManagementComponent} from './components/building-management/building-management.component';
import {BuildingsComponent} from './components/buildings/buildings.component';
import {CreateEnterpriseComponent} from './components/create-enterprise/create-enterprise.component';
import {CreateTenantComponent} from './components/create-tenant/create-tenant.component';
import {EmissionActivityComponent} from './components/emission-activity/emission-activity.component';
import {EmissionActivityDetailComponent} from './components/emission-activity-detail/emission-activity-detail.component';
import {EnterpriseInvitationComponent} from './components/enterprise-invitation/enterprise-invitation.component';
import {NewBuildingGroupComponent} from './components/new-building-group/new-building-group.component';
import {NewGroupItemComponent} from './components/new-group-item/new-group-item.component';
import {NewTenantComponent} from './components/new-tenant/new-tenant.component';
import {PaymentComponent} from './components/payment/payment.component';
import {PlanComponent} from './components/plan/plan.component';
import {EnterpriseComponent} from './enterprise.component';
import {ActivityTypeComponent} from './components/activity-type/activity-type.component';
import {BuildingGroupDetailComponent} from './components/building-group-detail/building-group-detail.component';

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
        path: `${AppRoutingConstants.BUILDING_MANAGEMENT_PATH}/:id`,
        component: BuildingManagementComponent
      },
      {
        path: `${AppRoutingConstants.BUILDING_GROUP_PATH}/:id`,
        component: BuildingGroupDetailComponent
      },
      {
        path: `${AppRoutingConstants.NEW_TENANT_PATH}/:id`,
        component: NewTenantComponent
      },
      {
        path: `${AppRoutingConstants.NEW_GROUP_PATH}/:id`,
        component: NewBuildingGroupComponent
      },
      {
        path: AppRoutingConstants.PLAN_PATH,
        component: PlanComponent
      },
      {path: AppRoutingConstants.PAYMENT_PATH, component: PaymentComponent},
      {
        path: `${AppRoutingConstants.EMISSION_ACTIVITY_PATH}/:buildingId`,
        component: EmissionActivityComponent
      },
      {
        path: `${AppRoutingConstants.EMISSION_ACTIVITY_DETAIL_PATH}/:emissionId`,
        component: EmissionActivityDetailComponent
      },
      {
        path: `${AppRoutingConstants.ACCOUNT_INFO_PATH}`,
        component: AccountInformationComponent
      },
      {
        path: `${AppRoutingConstants.INVITATION_PATH}`,
        component: EnterpriseInvitationComponent
      },
      {
        path: `${AppRoutingConstants.CREATE_ENTERPRISE_PATH}`,
        component: CreateEnterpriseComponent
      },
      {
        path: `${AppRoutingConstants.CREATE_TENANT}`,
        component: CreateTenantComponent
      },
      {
        path: `${AppRoutingConstants.ACTIVITY_TYPE}/:id`,
        component: ActivityTypeComponent
      },
      {
        path: `${AppRoutingConstants.NEW_GROUP_ITEM}/:id`,
        component: NewGroupItemComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class EnterpriseRoutingModule {}
