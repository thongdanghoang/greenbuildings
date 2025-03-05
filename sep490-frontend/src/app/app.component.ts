import {Component, OnDestroy, OnInit} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {Observable, Subject, of, switchMap, takeUntil, tap} from 'rxjs';
import {ApplicationService} from './modules/core/services/application.service';
import {ThemeService} from './modules/core/services/theme.service';
import {UserLocale} from './modules/shared/enums/user-language.enum';
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
    this.applicationService
      .isAuthenticated()
      .pipe(
        takeUntil(this.destroy$),
        switchMap(isAuthenticated => {
          if (isAuthenticated) {
            return this.userService.userConfigs.pipe(
              takeUntil(this.destroy$),
              tap(userConfigs => {
                if (userConfigs) {
                  this.translate.use(userConfigs.language.split('-')[0]); // extract from locale
                }
              })
            );
          }
          this.translate.use(UserLocale.VI.split('-')[0]);
          return of(null);
        })
      )
      .subscribe();
    this.themeService.initTheme();
    // Listen for system theme changes
    this.systemThemeMediaQuery = window.matchMedia(
      this.themeService.SYSTEM_COLOR_SCHEME_QUERY
    );
    this.systemThemeMediaQuery.addEventListener(
      'change',
      this.handleThemeChange
    );
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
