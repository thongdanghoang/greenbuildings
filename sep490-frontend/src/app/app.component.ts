import {Component, OnDestroy, OnInit} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {Observable, Subject, filter, switchMap, take} from 'rxjs';
import {ApplicationService} from './modules/core/services/application.service';
import {ThemeService} from './modules/core/services/theme.service';
import {UserService} from './services/user.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit, OnDestroy {
  protected readonly destroy$: Subject<void> = new Subject<void>();
  private systemThemeMediaQuery?: MediaQueryList;

  constructor(
    private readonly themeService: ThemeService,
    private readonly translate: TranslateService,
    private readonly userService: UserService,
    private readonly applicationService: ApplicationService
  ) {}

  ngOnInit(): void {
    this.translate.setDefaultLang('vi');
    this.themeService.initTheme();
    // Listen for system theme changes
    this.systemThemeMediaQuery = window.matchMedia(
      this.themeService.SYSTEM_COLOR_SCHEME_QUERY
    );
    this.systemThemeMediaQuery.addEventListener(
      'change',
      this.handleThemeChange
    );
    this.authenticated
      .pipe(
        filter(auth => auth),
        take(1),
        switchMap(() => this.userService.userConfigs)
      )
      .subscribe(configs => {
        this.translate.use(configs.language.split('-')[0]);
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    this.systemThemeMediaQuery?.removeEventListener(
      'change',
      this.handleThemeChange
    );
  }

  get authenticated(): Observable<boolean> {
    return this.applicationService.isAuthenticated();
  }

  private readonly handleThemeChange = (e: MediaQueryListEvent): void => {
    this.themeService.systemPreferredColorThemeChanged.next(e.matches);
  };
}
