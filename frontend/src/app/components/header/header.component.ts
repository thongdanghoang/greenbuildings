import {Component, OnInit} from '@angular/core';
import {Theme, ThemeService} from '../../modules/core/services/theme.service';
import {ApplicationService} from '../../modules/core/services/application.service';
import {SubscriptionAwareComponent} from '../../modules/core/subscription-aware.component';
import {Observable} from "rxjs";
import {Router} from '@angular/router';
import {AppRoutingConstants} from '../../app-routing.constant';
import {MenuItem} from 'primeng/api';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent extends SubscriptionAwareComponent implements OnInit {

  protected items: MenuItem[] | undefined;
  protected isDarkMode: boolean = false;
  protected readonly authenticated: Observable<boolean>;

  constructor(
    private readonly applicationService: ApplicationService,
    private readonly themeService: ThemeService,
    private readonly router: Router
  ) {
    super();
    this.authenticated = this.applicationService.isAuthenticated();
    this.applicationService.userDataSubject.subscribe((data) => {
      console.log('userDataSubject', data);
    });
  }

  ngOnInit() {
    this.items = [
      {
        label: 'Enterprise',
        icon: 'pi pi-building',
        command: () => {
          void this.router.navigate([AppRoutingConstants.ENTERPRISE_PATH]);
        }
      },
      {label: 'DevTools', icon: 'pi pi-code', route: `${AppRoutingConstants.DEV_PATH}`},
    ];
  }

  protected login(): void {
    this.applicationService.login();
  }

  protected logout(): void {
    this.applicationService.logout();
  }

  protected toggleLightDark(): void {
    if (this.themeService.isDarkMode()
    ) {
      this.themeService.switchTheme(Theme.AURA_LIGHT_CYAN);
      this.isDarkMode = false;
      return;
    }
    this.themeService.switchTheme(Theme.AURA_DARK_CYAN);
    this.isDarkMode = true;
  }

  protected toDev(): void {
    void this.router.navigate([AppRoutingConstants.DEV_PATH]);
  }

  protected toEnterprise(): void {
    void this.router.navigate([AppRoutingConstants.ENTERPRISE_PATH]);
  }
}
