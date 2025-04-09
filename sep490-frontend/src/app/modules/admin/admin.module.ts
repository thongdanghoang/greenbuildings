import {NgModule} from '@angular/core';
import {SharedModule} from '../shared/shared.module';

import {AdminComponent} from './admin.component';
import {AdminRoutingModule} from './admin-routing.module';
import {PackageCreditComponent} from './components/package-credit/package-credit.component';
import {PackageCreditService} from './services/package-credit.service';
import {CreateUpdatePackageCreditComponent} from './components/create-update-package-credit/create-update-package-credit.component';
import {CreditConvertRatioComponent} from './components/credit-convert-ratio/credit-convert-ratio.component';
import {CreditConvertRatioService} from './services/credit-convert-ratio.service';
import {UpdateRatioComponent} from './components/update-ratio/update-ratio.component';
import {EmissionSourceComponent} from './components/emission-source/emission-source.component';
import {EmissionSourceService} from './services/emission-source.service';
import {FuelConversionComponent} from './components/fuel-conversion/fuel-conversion.component';
import {FuelConversionService} from './services/fuel-conversion.service';
import {EmissionSourceDialogComponent} from './dialog/emission-source-dialog/emission-source-dialog.component';
import {FuelDialogComponent} from './dialog/fuel-dialog/fuel-dialog.component';
import {EmissionFactorComponent} from './components/emission-factor/emission-factor.component';
import {EmissionFactorService} from './services/emission_factor.service';
import {EmissionFactorDialogComponent} from './dialog/emission-factor-dialog/emission-factor-dialog.component';
import {ChemicalDensityComponent} from './components/chemical-density/chemical-density.component';
import {ChemicalDensityService} from './services/chemical-density.service';
import {ChemicalDensityDialogComponent} from './dialog/chemical-density-dialog/chemical-density-dialog.component';

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
    ChemicalDensityDialogComponent
  ],
  imports: [SharedModule, AdminRoutingModule],
  providers: [
    PackageCreditService,
    CreditConvertRatioService,
    EmissionSourceService,
    FuelConversionService,
    EmissionFactorService,
    ChemicalDensityService
  ]
})
export class AdminModule {}
