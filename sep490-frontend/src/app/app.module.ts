import {HTTP_INTERCEPTORS, HttpClient} from '@angular/common/http';
import {APP_INITIALIZER, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {provideAnimationsAsync} from '@angular/platform-browser/animations/async';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {
  AuthInterceptor,
  AuthModule,
  LogLevel,
  LoginResponse,
  OidcSecurityService,
  OpenIdConfigLoader,
  StsConfigHttpLoader,
  StsConfigLoader
} from 'angular-auth-oidc-client';
import {providePrimeNG} from 'primeng/config';
import {map} from 'rxjs';
import {environment} from '../environments/environment';
import {AppRoutingConstants} from './app-routing.constant';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {DashboardComponent} from './components/dashboard/dashboard.component';
import {FooterComponent} from './components/footer/footer.component';
import {ForbiddenComponent} from './components/forbidden/forbidden.component';
import {HeaderComponent} from './components/header/header.component';
import {HomeComponent} from './components/home/home.component';
import {NotFoundComponent} from './components/not-found/not-found.component';
import {PricingComponent} from './components/pricing/pricing.component';
import {SidebarComponent} from './components/sidebar/sidebar.component';
import {UnauthorizedComponent} from './components/unauthorized/unauthorized.component';
import {CoreModule} from './modules/core/core.module';
import {HttpErrorHandlerInterceptor} from './modules/core/services/http-error-handler.interceptor';
import {SharedModule} from './modules/shared/shared.module';

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

// AoT requires an exported function for factories
export function HttpLoaderFactory(http: HttpClient): TranslateHttpLoader {
  return new TranslateHttpLoader(http);
}

export const httpLoaderFactory = (
  httpClient: HttpClient
): StsConfigHttpLoader => {
  const config$ = httpClient.get<any>(`assets/config/config.json`).pipe(
    map((customConfig: any) => {
      return {
        authority: environment.oidcAuthority,
        redirectUrl: window.location.origin,
        clientId: environment.oidcClientId,
        responseType: customConfig.responseType,
        scope: `${OidcScopes.OPENID} ${OidcScopes.EMAIL} ${OidcScopes.PHONE}`,
        postLogoutRedirectUri: `${window.location.origin}/${AppRoutingConstants.LANDING_PATH}`,
        forbiddenRoute: AppRoutingConstants.FORBIDDEN,
        unauthorizedRoute: AppRoutingConstants.UNAUTHORIZED,
        autoUserInfo: customConfig.autoUserInfo,
        renewUserInfoAfterTokenRenew: customConfig.renewUserInfoAfterTokenRenew,
        logLevel: environment.production ? LogLevel.Warn : LogLevel.Debug,
        secureRoutes: [environment.idpApiUrl, environment.enterpriseUrl]
      };
    })
  );

  return new StsConfigHttpLoader(config$);
};

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
      loader: {
        provide: StsConfigLoader,
        useFactory: httpLoaderFactory,
        deps: [HttpClient]
      }
    }),
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
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
    {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true},
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpErrorHandlerInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
