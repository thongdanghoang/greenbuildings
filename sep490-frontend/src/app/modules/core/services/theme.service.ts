import {Injectable} from '@angular/core';
import {definePreset} from '@primeng/themes';
import Aura from '@primeng/themes/aura';
import {PrimeNG, ThemeType} from 'primeng/config';
import {
  BehaviorSubject,
  Observable,
  filter,
  of,
  switchMap,
  takeUntil
} from 'rxjs';
import {UserService} from '../../../services/user.service';
import {Theme} from '../../shared/models/user-configs';
import {SubscriptionAwareComponent} from '../subscription-aware.component';
import {ApplicationService} from './application.service';

const MyPreset = definePreset(Aura, {
  primitive: {
    red: {
      50: '#fef8f8',
      100: '#fbdfdf',
      200: '#f8c5c6',
      300: '#f5acad',
      400: '#f29293',
      500: '#ef797a',
      600: '#cb6768',
      700: '#a75555',
      800: '#834343',
      900: '#603031',
      950: '#3c1e1f'
    }
  },
  semantic: {
    primary: {
      50: '#f4fcfd',
      100: '#c9f3f5',
      200: '#9ee9ee',
      300: '#73dfe6',
      400: '#48d5df',
      500: '#1dcbd7',
      600: '#19adb7',
      700: '#148e97',
      800: '#107076',
      900: '#0c5156',
      950: '#073336'
    }
  }
});

@Injectable()
export class ThemeService extends SubscriptionAwareComponent {
  readonly LOCAL_STORAGE_KEY = 'prefers-color-scheme';
  readonly TOKEN = 'my-app-dark';
  readonly SYSTEM_COLOR_SCHEME_QUERY = '(prefers-color-scheme: dark)';
  systemPreferredColorThemeChanged: BehaviorSubject<boolean>;
  systemPreferredColorTheme: ThemeType;
  userPreferredColorTheme: ThemeType;

  constructor(
    private readonly config: PrimeNG,
    private readonly userService: UserService,
    private readonly applicationService: ApplicationService
  ) {
    super();
    const themeOptions = {
      prefix: 'p',
      darkModeSelector: 'system',
      cssLayer: {
        name: 'primeng',
        order: 'tailwind-base, primeng, tailwind-utilities'
      }
    };
    this.systemPreferredColorTheme = {
      preset: MyPreset,
      options: themeOptions
    };
    this.userPreferredColorTheme = {
      preset: MyPreset,
      options: {...themeOptions, darkModeSelector: `.${this.TOKEN}`}
    };
    this.systemPreferredColorThemeChanged = new BehaviorSubject<boolean>(
      window.matchMedia(this.SYSTEM_COLOR_SCHEME_QUERY).matches
    );
  }

  initTheme(): void {
    if (this.isThemeConfigured()) {
      this.config.theme.set(this.userPreferredColorTheme);
      if (localStorage.getItem(this.LOCAL_STORAGE_KEY) === Theme[Theme.DARK]) {
        document.querySelector('html')?.classList.add(this.TOKEN);
      }
      return;
    }
    this.config.theme.set(this.systemPreferredColorTheme);
    if (window.matchMedia(this.SYSTEM_COLOR_SCHEME_QUERY).matches) {
      document.querySelector('html')?.classList.toggle(this.TOKEN);
    }
  }

  isDarkMode(): Observable<boolean> {
    if (this.isThemeConfigured()) {
      return of(
        localStorage.getItem(this.LOCAL_STORAGE_KEY) === Theme[Theme.DARK]
      );
    }
    return this.systemPreferredColorThemeChanged;
  }

  toggleLightDark(): void {
    this.config.theme.set(this.userPreferredColorTheme);
    if (document.querySelector('html')?.classList.contains(this.TOKEN)) {
      localStorage.setItem(this.LOCAL_STORAGE_KEY, Theme[Theme.LIGHT]);
      this.changeTheme(Theme[Theme.LIGHT] as keyof typeof Theme);
    } else {
      localStorage.setItem(this.LOCAL_STORAGE_KEY, Theme[Theme.DARK]);
      this.changeTheme(Theme[Theme.DARK] as keyof typeof Theme);
    }
    document.querySelector('html')?.classList.toggle(this.TOKEN);
  }

  setTheme(theme: keyof typeof Theme): void {
    if (theme === Theme[Theme.SYSTEM]) {
      localStorage.removeItem(this.LOCAL_STORAGE_KEY);
      return;
    }
    localStorage.setItem(this.LOCAL_STORAGE_KEY, theme);
  }

  private changeTheme(theme: keyof typeof Theme): void {
    this.applicationService
      .isAuthenticated()
      .pipe(
        takeUntil(this.destroy$),
        filter(isAuthenticated => isAuthenticated),
        switchMap(() => this.userService.changeTheme(theme))
      )
      .subscribe();
  }

  private isThemeConfigured(): boolean {
    return !!localStorage.getItem(this.LOCAL_STORAGE_KEY);
  }
}
