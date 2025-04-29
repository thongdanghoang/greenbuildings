import {NgModule} from '@angular/core';
import {SharedModule} from '@shared/shared.module';

import {AuthorizationRoutingModule} from './authorization-routing.module';
import {AuthorizationComponent} from './authorization.component';
import {PowerBiAccessTokenDetailComponent} from './components/power-bi-access-token-detail/power-bi-access-token-detail.component';
import {PowerBiAccessTokenComponent} from './components/power-bi-access-token/power-bi-access-token.component';
import {PowerBiRegenerateAccessTokenComponent} from './components/power-bi-regenerate-access-token/power-bi-regenerate-access-token.component';
import {UserProfileComponent} from './components/user-profile/user-profile.component';
import {UsersComponent} from './components/users-management/users.component';

@NgModule({
  declarations: [
    AuthorizationComponent,
    UsersComponent,
    UserProfileComponent,
    PowerBiAccessTokenComponent,
    PowerBiAccessTokenDetailComponent,
    PowerBiRegenerateAccessTokenComponent
  ],
  imports: [SharedModule, AuthorizationRoutingModule],
  providers: []
})
export class AuthorizationModule {}
