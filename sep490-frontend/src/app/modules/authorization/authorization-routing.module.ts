import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {UnsavedChangesGuard} from '@shared/directives/unsaved-changes/unsaved-changes-guard.service';
import {AppRoutingConstants} from '../../app-routing.constant';
import {AuthorizationComponent} from './authorization.component';
import {PowerBiAccessTokenDetailComponent} from './components/power-bi-access-token-detail/power-bi-access-token-detail.component';
import {PowerBiAccessTokenComponent} from './components/power-bi-access-token/power-bi-access-token.component';
import {PowerBiRegenerateAccessTokenComponent} from './components/power-bi-regenerate-access-token/power-bi-regenerate-access-token.component';
import {UsersComponent} from './components/users-management/users.component';

const routes: Routes = [
  {
    path: '',
    component: AuthorizationComponent,
    children: [
      {path: AppRoutingConstants.USERS_PATH, component: UsersComponent},
      {
        path: AppRoutingConstants.SETTINGS,
        children: [
          {
            path: AppRoutingConstants.POWER_BI_ACCESS_TOKEN,
            component: PowerBiAccessTokenComponent
          },
          {
            path: `${AppRoutingConstants.POWER_BI_ACCESS_TOKEN}/:id`,
            component: PowerBiAccessTokenDetailComponent,
            canDeactivate: [UnsavedChangesGuard]
          },
          {
            path: `${AppRoutingConstants.POWER_BI_ACCESS_TOKEN}/:id/${AppRoutingConstants.REGENERATE}`,
            component: PowerBiRegenerateAccessTokenComponent
          }
        ]
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuthorizationRoutingModule {}
