import {Component, OnInit} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {Observable, map, takeUntil} from 'rxjs';
import {ApplicationService} from '../../modules/core/services/application.service';
import {ThemeService} from '../../modules/core/services/theme.service';
import {SubscriptionAwareComponent} from '../../modules/core/subscription-aware.component';
import {UserLanguage} from '../../modules/shared/enums/user-language.enum';
import {UserService} from '../../services/user.service';

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
export class HeaderComponent
  extends SubscriptionAwareComponent
  implements OnInit
{
  protected readonly authenticated: Observable<boolean>;
  protected languages: Language[] | undefined;
  protected selectedLanguage: Language | undefined;
  protected visible: boolean = false;

  constructor(
    protected readonly applicationService: ApplicationService,
    private readonly userService: UserService,
    private readonly themeService: ThemeService,
    private readonly translate: TranslateService
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
    this.translate.onLangChange
      .pipe(
        takeUntil(this.destroy$),
        map(event => event.lang)
      )
      .subscribe(lang => {
        this.selectedLanguage = this.languages?.find(
          language => language.key.split('-')[0] === lang
        );
      });
  }

  protected changeLanguage(language: Language): void {
    this.translate.use(language.key.split('-')[0]);
    this.userService
      .changeLanguage(language.key)
      .pipe(takeUntil(this.destroy$))
      .subscribe();
  }

  protected login(): void {
    this.applicationService.login();
  }

  protected logout(): void {
    this.applicationService.logout();
  }

  protected get isDarkMode(): Observable<boolean> {
    return this.themeService.isDarkMode();
  }

  protected toggleLightDark(): void {
    this.themeService.toggleLightDark();
  }
}
