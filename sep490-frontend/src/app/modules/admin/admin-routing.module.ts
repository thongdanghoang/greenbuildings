import {RouterModule, Routes} from '@angular/router';
import {AppRoutingConstants} from '../../app-routing.constant';

import {NgModule} from '@angular/core';
import {UnsavedChangesGuard} from '../shared/directives/unsaved-changes/unsaved-changes-guard.service';
import {AdminComponent} from './admin.component';
import {AccountManagementComponent} from './components/account-management/account-management.component';
import {EnterpriseManagementComponent} from './components/enterprise-management/enterprise-management.component';
import {PackageCreditComponent} from './components/package-credit/package-credit.component';
import {CreateUpdatePackageCreditComponent} from './components/create-update-package-credit/create-update-package-credit.component';
import {CreditConvertRatioComponent} from './components/credit-convert-ratio/credit-convert-ratio.component';
import {TenantManagementComponent} from './components/tenant-management/tenant-management.component';
import {UpdateRatioComponent} from './components/update-ratio/update-ratio.component';
import {EmissionSourceComponent} from './components/emission-source/emission-source.component';
import {FuelConversionComponent} from './components/fuel-conversion/fuel-conversion.component';
import {EmissionFactorComponent} from './components/emission-factor/emission-factor.component';
import {ChemicalDensityComponent} from './components/chemical-density/chemical-density.component';
import {PaymentEnterpriseManagementComponent} from './components/payment-enterprise-management/payment-enterprise-management.component';
import {TransactionEnterpriseManagementComponent} from './components/transaction-enterprise-management/transaction-enterprise-management.component';

const routes: Routes = [
  {
    path: '',
    component: AdminComponent,
    children: [
      {
        path: AppRoutingConstants.PACKAGE_CREDIT_PATH,
        component: PackageCreditComponent
      },
      {
        path: AppRoutingConstants.CREDIT_CONVERT_RATIO,
        component: CreditConvertRatioComponent
      },
      {
        path: `${AppRoutingConstants.CREDIT_CONVERT_RATIO_DETAILS}/:id`,
        component: UpdateRatioComponent,
        canDeactivate: [UnsavedChangesGuard]
      },
      {
        path: `${AppRoutingConstants.PACKAGE_CREDIT_DETAILS_PATH}`,
        component: CreateUpdatePackageCreditComponent
      },
      {
        path: `${AppRoutingConstants.PACKAGE_CREDIT_DETAILS_PATH}/:id`,
        component: CreateUpdatePackageCreditComponent,
        canDeactivate: [UnsavedChangesGuard]
      },
      {
        path: `${AppRoutingConstants.EMISSION_SOURCE}`,
        component: EmissionSourceComponent
      },
      {
        path: `${AppRoutingConstants.FUEL_CONVERSION}`,
        component: FuelConversionComponent
      },
      {
        path: `${AppRoutingConstants.EMISSION_FACTOR}`,
        component: EmissionFactorComponent
      },
      {
        path: `${AppRoutingConstants.CHEMICAL_DENSITY}`,
        component: ChemicalDensityComponent
      },
      {
        path: `${AppRoutingConstants.ACCOUNT_MANAGEMENT}`,
        component: AccountManagementComponent
      },
      {
        path: `${AppRoutingConstants.ENTERPRISE_MANAGEMENT}`,
        component: EnterpriseManagementComponent
      },
      {
        path: `${AppRoutingConstants.TENANT_MANAGEMENT}`,
        component: TenantManagementComponent
      },
      {
        path: `${AppRoutingConstants.PAYMENT_ENTERPRISE_MANAGEMENT}`,
        component: PaymentEnterpriseManagementComponent
      },
      {
        path: `${AppRoutingConstants.TRANSACTION_ENTERPRISE_MANAGEMENT}`,
        component: TransactionEnterpriseManagementComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule {}
