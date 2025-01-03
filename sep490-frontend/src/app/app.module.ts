import {HTTP_INTERCEPTORS} from '@angular/common/http';
import {APP_INITIALIZER, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {provideAnimationsAsync} from '@angular/platform-browser/animations/async';
import {
  AuthInterceptor,
  AuthModule,
  LogLevel,
  LoginResponse,
  OidcSecurityService,
  OpenIdConfigLoader
} from 'angular-auth-oidc-client';
import {providePrimeNG} from 'primeng/config';
import {environment} from '../environments/environment';
import {AppRoutingConstants} from './app-routing.constant';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {FooterComponent} from './components/footer/footer.component';
import {ForbiddenComponent} from './components/forbidden/forbidden.component';
import {HeaderComponent} from './components/header/header.component';
import {HomeComponent} from './components/home/home.component';
import {NotFoundComponent} from './components/not-found/not-found.component';
import {UnauthorizedComponent} from './components/unauthorized/unauthorized.component';
import {CoreModule} from './modules/core/core.module';
import {SharedModule} from './modules/shared/shared.module';
import {SidebarComponent} from './components/sidebar/sidebar.component';
import { PricingComponent } from './components/pricing/pricing.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';

enum OidcScopes {
  OPENID = 'openid',
  PROFILE = 'profile',
  EMAIL = 'email',
  PHONE = 'phone',
  ADDRESS = 'address'
}

function initAuth(
  oidcSecurityService: OidcSecurityService
): () => Promise<LoginResponse> {
  return () =>
    new Promise<LoginResponse>(resolve => {
      oidcSecurityService.checkAuth().subscribe(data => {
        resolve(data);
      });
    });
}

@NgModule({
  declarations: [
    AppComponent,
    ForbiddenComponent,
    UnauthorizedComponent,
    NotFoundComponent,
    HeaderComponent,
    FooterComponent,
    HomeComponent,
    SidebarComponent,
    PricingComponent,
    DashboardComponent
  ],
  imports: [
    AppRoutingModule,
    BrowserModule,
    CoreModule,
    SharedModule,
    AuthModule.forRoot({
      config: {
        authority: environment.oidcAuthority,
        redirectUrl: window.location.origin,
        clientId: environment.oidcClientId,
        responseType: 'code',
        scope: `${OidcScopes.OPENID} ${OidcScopes.EMAIL} ${OidcScopes.PHONE}`,
        postLogoutRedirectUri: window.location.origin,
        forbiddenRoute: AppRoutingConstants.FORBIDDEN,
        unauthorizedRoute: AppRoutingConstants.UNAUTHORIZED,
        autoUserInfo: true,
        renewUserInfoAfterTokenRenew: true,
        logLevel: environment.production ? LogLevel.Warn : LogLevel.Debug,
        historyCleanupOff: true,
        secureRoutes: []
      }
    })
  ],
  providers: [
    provideAnimationsAsync(),
    providePrimeNG({
      ripple: true
    }),
    OpenIdConfigLoader,
    {
      provide: APP_INITIALIZER,
      useFactory: initAuth,
      deps: [OidcSecurityService],
      multi: true
    },
    {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true}
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
