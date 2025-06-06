import {Component, OnInit, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {UserRole} from '@models/role-names';
import {TranslateService} from '@ngx-translate/core';
import {ApplicationService} from '@services/application.service';
import {UserService} from '@services/user.service';
import {SubscriptionAwareComponent} from '@shared/directives/subscription-aware.component';
import {UserLanguage} from '@shared/enums/user-language.enum';
import {MenuItem} from 'primeng/api';
import {Drawer} from 'primeng/drawer';
import {Observable, filter, map, switchMap, take, takeUntil} from 'rxjs';
import {AppRoutingConstants} from '../../app-routing.constant';
import {ThemeService} from '../../modules/core/services/theme.service';

interface Language {
  display: string;
  mobile: string;
  key: UserLanguage;
}

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent extends SubscriptionAwareComponent implements OnInit {
  menuItems: MenuItem[] = [];
  userMenuMobileItems: MenuItem[] | undefined;
  @ViewChild('drawerRef') drawerRef!: Drawer;
  protected readonly authenticated: Observable<boolean>;
  protected languages: Language[] | undefined;
  protected selectedLanguage: Language | undefined;
  protected visible: boolean = false;

  constructor(
    protected readonly applicationService: ApplicationService,
    private readonly userService: UserService,
    private readonly themeService: ThemeService,
    private readonly translate: TranslateService,
    private readonly router: Router
  ) {
    super();
    this.authenticated = this.applicationService.isAuthenticated();
  }

  ngOnInit(): void {
    this.languages = [
      {display: 'Tiếng Việt', mobile: 'VI', key: UserLanguage.VI},
      {display: 'English', mobile: 'EN', key: UserLanguage.EN},
      {display: '中文(简体)', mobile: 'ZH', key: UserLanguage.ZH}
    ];
    this.buildMenuItems();
    this.selectedLanguage = {
      display: 'Tiếng Việt',
      mobile: 'VI',
      key: UserLanguage.VI
    };
    this.translate.onLangChange
      .pipe(
        takeUntil(this.destroy$),
        map(event => event.lang)
      )
      .subscribe(lang => {
        this.selectedLanguage = this.languages?.find(language => language.key.split('-')[0] === lang);
      });
  }

  homepage(): void {
    return void this.router.navigate(['/']);
  }

  closeCallback(e: Event): void {
    this.drawerRef.close(e);
  }

  get isMobile(): boolean {
    return this.applicationService.isMobile();
  }

  protected buildMenuItems(): void {
    this.applicationService
      .getUserRoles()
      .pipe(take(1))
      .subscribe(roles => {
        this.menuItems.push({
          label: 'common.profile',
          icon: 'pi pi-user',
          command: (): void => this.userProfile(roles)
        });
        this.menuItems.push({
          label: 'common.logout',
          icon: 'pi pi-power-off',
          command: (): void => this.logout()
        });

        // Mobile
        this.buildUserMenuMobileItems(roles);
      });
  }

  protected buildUserMenuMobileItems(roles: UserRole[]): void {
    this.userMenuMobileItems = [
      {
        label: 'header.nav.Account',
        items: [
          {
            label: 'common.profile',
            icon: 'pi pi-user',
            command: (): void => this.userProfile(roles)
          }
        ]
      }
    ];
  }

  protected changeLanguage(language: Language): void {
    this.translate.use(language.key.split('-')[0]);
    this.applicationService
      .isAuthenticated()
      .pipe(
        filter(auth => auth),
        take(1),
        switchMap(() => this.userService.changeLanguage(language.key))
      )
      .subscribe();
  }
  protected login(): void {
    this.applicationService.login();
  }

  protected logout(): void {
    this.applicationService.logout();
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  protected userProfile(roles: UserRole[]): void {
    void this.router.navigate([`/${AppRoutingConstants.ENTERPRISE_PATH}/${AppRoutingConstants.ACCOUNT_INFO_PATH}`]);
  }

  protected get isDarkMode(): Observable<boolean> {
    return this.themeService.isDarkMode();
  }

  protected toggleLightDark(): void {
    this.themeService.toggleLightDark();
  }
}
