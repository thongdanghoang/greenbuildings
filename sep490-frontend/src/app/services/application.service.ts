import {Injectable, OnDestroy} from '@angular/core';
import {
  AuthenticatedResult,
  OidcSecurityService
} from 'angular-auth-oidc-client';
import {JwtPayload} from 'jwt-decode';
import {
  BehaviorSubject,
  Observable,
  filter,
  map,
  switchMap,
  takeUntil
} from 'rxjs';
import {UUID} from '../../types/uuid';
import {UserRole} from '@models/role-names';
import {SubscriptionAwareComponent} from '@shared/directives/subscription-aware.component';

interface UserInfoEmailScope {
  email: string;
  email_verified: boolean;
}

interface UserInfoPhoneScope {
  phone: string;
  phone_verified: boolean;
}

interface UserInfoData extends UserInfoEmailScope, UserInfoPhoneScope {
  sub: string;
}

export interface UserData extends JwtPayload {
  authorities: string[];
  permissions: string[];
  enterpriseId: UUID;
}

// eslint-disable-next-line @typescript-eslint/no-unused-vars
interface UserInfoAddressScope {
  address: {
    formatted: unknown;
  };
}

// eslint-disable-next-line @typescript-eslint/no-unused-vars
interface UserInfoProfileScope {
  birthdate: string;
  family_name: string;
  gender: string;
  given_name: string;
  locale: string;
  middle_name: string;
  name: string;
  nickname: string;
  picture: string;
  preferred_username: string;
  profile: string;
  updated_at: number;
  website: string;
  zoneinfo: string;
}

@Injectable({
  providedIn: 'root'
})
export class ApplicationService
  extends SubscriptionAwareComponent
  implements OnDestroy
{
  private readonly userInfoSubject;
  private readonly userDataSubject;

  constructor(private readonly oidcSecurityService: OidcSecurityService) {
    super();
    this.userDataSubject = new BehaviorSubject<UserData | null>(null);
    this.userInfoSubject = new BehaviorSubject<UserInfoData | null>(null);
    this.registerSubscription(
      this.oidcSecurityService.isAuthenticated$
        .pipe(
          filter((auth: AuthenticatedResult): boolean => auth.isAuthenticated),
          switchMap(() => this.oidcSecurityService.getPayloadFromAccessToken())
        )
        .subscribe((userData: UserData): void =>
          this.userDataSubject.next(userData)
        )
    );
  }

  override ngOnDestroy(): void {
    this.userInfoSubject.complete();
    super.ngOnDestroy();
  }

  get UserData(): Observable<UserData> {
    return this.userDataSubject
      .asObservable()
      .pipe(filter((user): user is UserData => user !== null));
  }

  get UserInfoData(): Observable<UserInfoData> {
    return this.userInfoSubject
      .asObservable()
      .pipe(filter((user): user is UserInfoData => user !== null));
  }

  isEmailVerified(): Observable<boolean> {
    return this.userInfoSubject.asObservable().pipe(
      filter((user): user is UserInfoData => user !== null),
      map((userInfo): boolean => userInfo.email_verified)
    );
  }

  isAuthenticated(): Observable<boolean> {
    return this.oidcSecurityService.isAuthenticated$.pipe(
      map((result: AuthenticatedResult): boolean => result.isAuthenticated)
    );
  }

  includeRole(authorities: string[], role: UserRole): boolean {
    const jwtRolePrefix = 'ROLE_';
    return authorities.includes(`${jwtRolePrefix}${UserRole[role]}`);
  }

  getUserRoles(): Observable<UserRole[]> {
    return this.userDataSubject.asObservable().pipe(
      filter((user): user is UserData => user !== null),
      map((userData): UserRole[] =>
        userData.authorities.map((role: string) => {
          const roleName = role.replace('ROLE_', '');
          return UserRole[roleName as keyof typeof UserRole];
        })
      )
    );
  }

  login(): void {
    this.oidcSecurityService.authorize();
  }

  postLogin(): void {
    this.oidcSecurityService
      .getPayloadFromAccessToken()
      .pipe(takeUntil(this.destroy$))
      .subscribe((data: UserData): void => {
        this.userDataSubject.next(data);
      });
    this.oidcSecurityService
      .getUserData()
      .pipe(takeUntil(this.destroy$))
      .subscribe(userInfo => {
        this.userInfoSubject.next(userInfo);
      });
  }

  isMobile(): boolean {
    return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini|Mobile|mobile|CriOS/i.test(
      navigator.userAgent
    );
  }

  logout(): void {
    this.registerSubscription(
      this.oidcSecurityService.logoff().subscribe({
        complete: (): void => {
          this.postLogout();
        }
      })
    );
  }

  private postLogout(): void {
    sessionStorage.clear();
    // localStorage.clear();
    this.userInfoSubject.next(null);
  }
}
