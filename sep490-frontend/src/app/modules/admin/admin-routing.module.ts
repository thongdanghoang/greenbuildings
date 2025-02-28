import {RouterModule, Routes} from '@angular/router';
import {AppRoutingConstants} from '../../app-routing.constant';

import {NgModule} from '@angular/core';
import {AdminComponent} from './admin.component';
import {PackageCreditComponent} from './components/package-credit/package-credit.component';
import {CreateUpdatePackageCreditComponent} from './components/create-update-package-credit/create-update-package-credit.component';
import {CreditConvertRatioComponent} from './components/credit-convert-ratio/credit-convert-ratio.component';
import {UpdateRatioComponent} from './components/update-ratio/update-ratio.component';

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
        component: UpdateRatioComponent
      },
      {
        path: `${AppRoutingConstants.PACKAGE_CREDIT_DETAILS_PATH}`,
        component: CreateUpdatePackageCreditComponent
      },
      {
        path: `${AppRoutingConstants.PACKAGE_CREDIT_DETAILS_PATH}/:id`,
        component: CreateUpdatePackageCreditComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule {}
