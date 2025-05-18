import {NgModule} from '@angular/core';
import {SharedModule} from '../shared/shared.module';
import {AdminRoutingModule} from './admin-routing.module';

import {AdminComponent} from './admin.component';
import {ChemicalDensityComponent} from './components/chemical-density/chemical-density.component';
import {CreateUpdatePackageCreditComponent} from './components/create-update-package-credit/create-update-package-credit.component';
import {CreditConvertRatioComponent} from './components/credit-convert-ratio/credit-convert-ratio.component';
import {EmissionFactorComponent} from './components/emission-factor/emission-factor.component';
import {EmissionSourceComponent} from './components/emission-source/emission-source.component';
import {FuelConversionComponent} from './components/fuel-conversion/fuel-conversion.component';
import {PackageCreditComponent} from './components/package-credit/package-credit.component';
import {UpdateRatioComponent} from './components/update-ratio/update-ratio.component';
import {ChemicalDensityDialogComponent} from './dialog/chemical-density-dialog/chemical-density-dialog.component';
import {EmissionFactorDialogComponent} from './dialog/emission-factor-dialog/emission-factor-dialog.component';
import {EmissionSourceDialogComponent} from './dialog/emission-source-dialog/emission-source-dialog.component';
import {FuelDialogComponent} from './dialog/fuel-dialog/fuel-dialog.component';
import {AccountManagementComponent} from './components/account-management/account-management.component';
import {EnterpriseManagementComponent} from './components/enterprise-management/enterprise-management.component';
import {TenantManagementComponent} from './components/tenant-management/tenant-management.component';
import {PaymentEnterpriseManagementComponent} from './components/payment-enterprise-management/payment-enterprise-management.component';
import {TransactionEnterpriseManagementComponent} from './components/transaction-enterprise-management/transaction-enterprise-management.component';
import {RevenueOverviewComponent} from './components/revenue-overview/revenue-overview.component';

@NgModule({
  declarations: [
    AdminComponent,
    PackageCreditComponent,
    CreateUpdatePackageCreditComponent,
    CreditConvertRatioComponent,
    UpdateRatioComponent,
    EmissionSourceComponent,
    FuelConversionComponent,
    EmissionSourceDialogComponent,
    FuelDialogComponent,
    EmissionFactorComponent,
    EmissionFactorDialogComponent,
    ChemicalDensityComponent,
    ChemicalDensityDialogComponent,
    AccountManagementComponent,
    EnterpriseManagementComponent,
    TenantManagementComponent,
    PaymentEnterpriseManagementComponent,
    TransactionEnterpriseManagementComponent,
    RevenueOverviewComponent
  ],
  imports: [SharedModule, AdminRoutingModule],
  providers: []
})
export class AdminModule {}
