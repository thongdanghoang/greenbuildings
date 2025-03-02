import {NgModule, inject} from '@angular/core';
import {Router, RouterModule, Routes} from '@angular/router';
import {
  AutoLoginPartialRoutesGuard,
  LoginResponse,
  OidcSecurityService
} from 'angular-auth-oidc-client';
import {Observable, of, switchMap, tap} from 'rxjs';
import {AppRoutingConstants} from './app-routing.constant';
import {DashboardComponent} from './components/dashboard/dashboard.component';
import {ForbiddenComponent} from './components/forbidden/forbidden.component';
import {HomeComponent} from './components/home/home.component';
import {LandingPageComponent} from './components/landing-page/landing-page.component';
import {NotFoundComponent} from './components/not-found/not-found.component';
import {UnauthorizedComponent} from './components/unauthorized/unauthorized.component';
import {ApplicationService} from './modules/core/services/application.service';

const authGuard: () => Observable<LoginResponse> = () => {
  const authService = inject(OidcSecurityService);
  const applicationService = inject(ApplicationService);
  const router = inject(Router);

  return authService.checkAuth().pipe(
    tap(authData => {
      if (!authData.isAuthenticated) {
        void router.navigate([AppRoutingConstants.LANDING_PAGE_PATH]);
      } else {
        applicationService.postLogin();
      }
    }),
    switchMap(loggedIn => of(loggedIn))
  );
};

const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    canActivate: [authGuard]
  },
  {
    path: AppRoutingConstants.LANDING_PAGE_PATH,
    component: LandingPageComponent
  },
  {
    path: AppRoutingConstants.DASHBOARD_PATH,
    component: DashboardComponent,
    canActivate: [AutoLoginPartialRoutesGuard, authGuard]
  },
  {
    path: AppRoutingConstants.AUTH_PATH,
    loadChildren: () =>
      import('./modules/authorization/authorization.module').then(
        m => m.AuthorizationModule
      ),
    canActivate: [AutoLoginPartialRoutesGuard, authGuard]
  },
  {
    path: AppRoutingConstants.ADMIN_PATH,
    loadChildren: () =>
      import('./modules/admin/admin.module').then(m => m.AdminModule),
    canActivate: [AutoLoginPartialRoutesGuard, authGuard]
  },
  {
    path: AppRoutingConstants.ENTERPRISE_PATH,
    loadChildren: () =>
      import('./modules/enterprise/enterprise.module').then(
        m => m.EnterpriseModule
      ),
    canActivate: [AutoLoginPartialRoutesGuard, authGuard]
  },
  {
    path: AppRoutingConstants.EMISSIONS_PATH,
    loadChildren: () =>
      import('./modules/emissions/emissions.module').then(
        m => m.EmissionsModule
      ),
    canActivate: [AutoLoginPartialRoutesGuard, authGuard]
  },
  {
    path: AppRoutingConstants.FORBIDDEN,
    component: ForbiddenComponent
  },
  {
    path: AppRoutingConstants.UNAUTHORIZED,
    component: UnauthorizedComponent
  },
  {
    path: AppRoutingConstants.DEV_PATH,
    loadChildren: () =>
      import('./modules/dev/dev.module').then(m => m.DevModule)
  },
  {
    path: '**',
    component: NotFoundComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
