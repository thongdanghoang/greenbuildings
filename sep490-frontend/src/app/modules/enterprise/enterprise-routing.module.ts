import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AppRoutingConstants} from '../../app-routing.constant';
import {UnsavedChangesGuard} from '../shared/directives/unsaved-changes/unsaved-changes-guard.service';
import {AccountInformationComponent} from './components/account-information/account-information.component';
import {ActivityTypeComponent} from './components/activity-type/activity-type.component';
import {BuildingDetailsComponent} from './components/building-details/building-details.component';
import {BuildingGroupDetailComponent} from './components/building-group-detail/building-group-detail.component';
import {BuildingManagementComponent} from './components/building-management/building-management.component';
import {BuildingsComponent} from './components/buildings/buildings.component';
import {CreateEnterpriseComponent} from './components/create-enterprise/create-enterprise.component';
import {CreateTenantComponent} from './components/create-tenant/create-tenant.component';
import {EmissionActivityDetailComponent} from './components/emission-activity-detail/emission-activity-detail.component';
import {EmissionActivityComponent} from './components/emission-activity/emission-activity.component';
import {EnterpriseInvitationComponent} from './components/enterprise-invitation/enterprise-invitation.component';
import {LinkedTenantComponent} from './components/linked-tenant/linked-tenant.component';
import {ManageCommonActivityComponent} from './components/manage-common-activity/manage-common-activity.component';
import {ManageTenantComponent} from './components/manage-tenant/manage-tenant.component';
import {NewBuildingGroupComponent} from './components/new-building-group/new-building-group.component';
import {NewTenantComponent} from './components/new-tenant/new-tenant.component';
import {PaymentComponent} from './components/payment/payment.component';
import {PlanComponent} from './components/plan/plan.component';
import {SentInvitationComponent} from './components/sent-invitation/sent-invitation.component';
import {TenantProfileComponent} from './components/tenant-profile/tenant-profile.component';
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
        component: BuildingDetailsComponent,
        canDeactivate: [UnsavedChangesGuard]
      },
      {
        path: `${AppRoutingConstants.BUILDING_MANAGEMENT_PATH}/:id`,
        component: BuildingManagementComponent
      },
      {
        path: `${AppRoutingConstants.TENANT_MANAGEMENT_PATH}`,
        component: ManageTenantComponent
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
        path: `${AppRoutingConstants.SENT_INVITATION_PATH}`,
        component: SentInvitationComponent
      },
      {
        path: `${AppRoutingConstants.NEW_GROUP_PATH}/:id`,
        component: NewBuildingGroupComponent
      },
      {
        path: AppRoutingConstants.PLAN_PATH,
        component: PlanComponent
      },
      {
        path: `${AppRoutingConstants.MANAGE_COMMON_ACTIVITY_PATH}/:buildingId`,
        component: ManageCommonActivityComponent
      },
      {
        path: AppRoutingConstants.LINKED_TENANT_PATH,
        component: LinkedTenantComponent
      },
      {path: AppRoutingConstants.PAYMENT_PATH, component: PaymentComponent},
      {
        path: `${AppRoutingConstants.EMISSION_ACTIVITY_PATH}/:buildingId`,
        component: EmissionActivityComponent
      },
      {
        path: `${AppRoutingConstants.EMISSION_ACTIVITY_DETAIL_PATH}/:emissionId`,
        component: EmissionActivityDetailComponent,
        canDeactivate: [UnsavedChangesGuard]
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
        path: `${AppRoutingConstants.ACTIVITY_TYPE}/:buildingId`,
        component: ActivityTypeComponent
      },
      {
        path: `${AppRoutingConstants.TENANT_PROFILE}`,
        component: TenantProfileComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class EnterpriseRoutingModule {}
