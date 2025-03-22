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

@NgModule({
  declarations: [
    AdminComponent,
    PackageCreditComponent,
    CreateUpdatePackageCreditComponent,
    CreditConvertRatioComponent,
    UpdateRatioComponent,
    EmissionSourceComponent,
    FuelConversionComponent
  ],
  imports: [SharedModule, AdminRoutingModule],
  providers: [
    PackageCreditService,
    CreditConvertRatioService,
    EmissionSourceService,
    FuelConversionService
  ]
})
export class AdminModule {}
