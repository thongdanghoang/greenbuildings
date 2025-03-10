import {Injectable} from '@angular/core';
import {definePreset} from '@primeng/themes';
import Aura from '@primeng/themes/aura';
import {PrimeNG, ThemeType} from 'primeng/config';
import {BehaviorSubject, Observable, of} from 'rxjs';
import {SubscriptionAwareComponent} from '../subscription-aware.component';

enum Theme {
  DARK,
  LIGHT,
  SYSTEM
}

const MyPreset = definePreset(Aura, {
  primitive: {
    green: {
      50: '#fafcfa',
      100: '#e5f2e6',
      200: '#d0e7d2',
      300: '#bbddbe',
      400: '#a6d2aa',
      500: '#91c896',
      600: '#7baa80',
      700: '#668c69',
      800: '#506e53',
      900: '#3a503c',
      950: '#243226'
    },
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
    },
    yellow: {
      50: '#fffdf3',
      100: '#fff5c3',
      200: '#ffed94',
      300: '#ffe464',
      400: '#ffdc35',
      500: '#ffd405',
      600: '#d9b404',
      700: '#b39404',
      800: '#8c7503',
      900: '#665502',
      950: '#403501'
    },
    blue: {
      50: '#f9fdfd',
      100: '#e4f4f7',
      200: '#cfebf0',
      300: '#bae2e9',
      400: '#a5dae3',
      500: '#90d1dc',
      600: '#7ab2bb',
      700: '#65929a',
      800: '#4f7379',
      900: '#3a5458',
      950: '#243437'
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

  constructor(private readonly config: PrimeNG) {
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
    } else {
      localStorage.setItem(this.LOCAL_STORAGE_KEY, Theme[Theme.DARK]);
    }
    document.querySelector('html')?.classList.toggle(this.TOKEN);
  }

  private isThemeConfigured(): boolean {
    return !!localStorage.getItem(this.LOCAL_STORAGE_KEY);
  }
}
