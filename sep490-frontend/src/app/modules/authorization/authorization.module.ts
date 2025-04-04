import {NgModule} from '@angular/core';
import {SharedModule} from '../shared/shared.module';

import {AuthorizationRoutingModule} from './authorization-routing.module';
import {AuthorizationComponent} from './authorization.component';
import {EnterpriseUserDetailsComponent} from './components/create-user/enterprise-user-details.component';
import {PowerBiAccessTokenDetailComponent} from './components/power-bi-access-token-detail/power-bi-access-token-detail.component';
import {PowerBiAccessTokenComponent} from './components/power-bi-access-token/power-bi-access-token.component';
import {PowerBiRegenerateAccessTokenComponent} from './components/power-bi-regenerate-access-token/power-bi-regenerate-access-token.component';
import {UserProfileComponent} from './components/user-profile/user-profile.component';
import {UsersComponent} from './components/users/users.component';
import {EnterpriseUserService} from './services/enterprise-user.service';
import {PowerBiAccessTokenService} from './services/power-bi-access-token.service';

@NgModule({
  declarations: [
    AuthorizationComponent,
    EnterpriseUserDetailsComponent,
    UsersComponent,
    UserProfileComponent,
    PowerBiAccessTokenComponent,
    PowerBiAccessTokenDetailComponent,
    PowerBiRegenerateAccessTokenComponent
  ],
  imports: [SharedModule, AuthorizationRoutingModule],
  providers: [EnterpriseUserService, PowerBiAccessTokenService]
})
export class AuthorizationModule {}
