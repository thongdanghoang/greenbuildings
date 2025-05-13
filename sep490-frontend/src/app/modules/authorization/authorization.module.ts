import {NgModule} from '@angular/core';
import {SharedModule} from '@shared/shared.module';

import {AuthorizationRoutingModule} from './authorization-routing.module';
import {AuthorizationComponent} from './authorization.component';
import {PowerBiAccessTokenDetailComponent} from './components/power-bi-access-token-detail/power-bi-access-token-detail.component';
import {PowerBiAccessTokenComponent} from './components/power-bi-access-token/power-bi-access-token.component';
import {PowerBiRegenerateAccessTokenComponent} from './components/power-bi-regenerate-access-token/power-bi-regenerate-access-token.component';
import {UsersComponent} from './components/users-management/users.component';
import {UserGuidePowerBiComponent} from './components/user-guide-power-bi/user-guide-power-bi.component';

@NgModule({
  declarations: [
    AuthorizationComponent,
    UsersComponent,
    PowerBiAccessTokenComponent,
    PowerBiAccessTokenDetailComponent,
    PowerBiRegenerateAccessTokenComponent,
    UserGuidePowerBiComponent
  ],
  imports: [SharedModule, AuthorizationRoutingModule],
  providers: []
})
export class AuthorizationModule {}
