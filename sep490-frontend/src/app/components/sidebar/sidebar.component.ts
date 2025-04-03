import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {MenuItem} from 'primeng/api';
import {takeUntil} from 'rxjs';
import {AppRoutingConstants} from '../../app-routing.constant';
import {UserRole} from '../../modules/authorization/enums/role-names';
import {
  ApplicationService,
  UserData
} from '../../modules/core/services/application.service';
import {SubscriptionAwareComponent} from '../../modules/core/subscription-aware.component';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent
  extends SubscriptionAwareComponent
  implements OnInit
{
  items: MenuItem[] | undefined;

  private readonly POWER_BI_ACCESS_TOKEN_PATH = `${AppRoutingConstants.AUTH_PATH}/${AppRoutingConstants.SETTINGS}/${AppRoutingConstants.POWER_BI_ACCESS_TOKEN}`;

  constructor(
    private readonly applicationService: ApplicationService,
    private readonly router: Router
  ) {
    super();
  }

  ngOnInit(): void {
    this.applicationService.UserData.pipe(takeUntil(this.destroy$)).subscribe(
      (userData: UserData): void => {
        if (
          this.applicationService.includeRole(
            userData.authorities,
            UserRole.ENTERPRISE_OWNER
          )
        ) {
          this.items = this.buildEnterpriseOwnerMenu();
        } else if (
          this.applicationService.includeRole(
            userData.authorities,
            UserRole.SYSTEM_ADMIN
          )
        ) {
          this.items = this.buildAdminMenu();
        } else if (
          this.applicationService.includeRole(
            userData.authorities,
            UserRole.BASIC_USER
          )
        ) {
          this.items = this.buildBasicUserMenu();
        }
      }
    );
    // this.settings = [
    //   {label: 'Settings', icon: 'pi pi-cog', route: '/settings'}
    // ];
  }

  homepage(): void {
    return void this.router.navigate(['/']);
  }

  // eslint-disable-next-line max-lines-per-function
  private buildEnterpriseOwnerMenu(): MenuItem[] {
    return [
      {
        label: 'enterprise.title',
        items: [
          {
            label: 'sidebar.owner.dashboard',
            icon: 'pi pi-chart-line',
            route: `/${AppRoutingConstants.DASHBOARD_PATH}`
          },
          {
            label: 'sidebar.owner.coefficientCenter',
            icon: 'pi pi-percentage',
            route: `/${AppRoutingConstants.EMISSIONS_PATH}`
          },
          {
            label: 'sidebar.owner.building',
            icon: 'pi pi-building',
            route: `/${AppRoutingConstants.ENTERPRISE_PATH}/${AppRoutingConstants.BUILDING_PATH}`
          }
        ]
      },
      {
        label: 'sidebar.owner.manage',
        items: [
          {
            label: 'sidebar.owner.users',
            icon: 'pi pi-users',
            route: `/${AppRoutingConstants.AUTH_PATH}/${AppRoutingConstants.USERS_PATH}`
          },
          {
            label: 'sidebar.owner.subscription',
            icon: 'pi pi-money-bill',
            route: `/${AppRoutingConstants.ENTERPRISE_PATH}/${AppRoutingConstants.PLAN_PATH}`
          },
          {
            label: 'sidebar.owner.payment',
            icon: 'pi pi-wallet',
            route: `/${AppRoutingConstants.ENTERPRISE_PATH}/${AppRoutingConstants.PAYMENT_PATH}`
          }
        ]
      },
      {
        label: 'sidebar.owner.settings.label',
        items: [
          {
            label: 'sidebar.owner.settings.items.powerBiAccessToken',
            icon: 'pi pi-cog',
            route: `/${this.POWER_BI_ACCESS_TOKEN_PATH}`
          }
        ]
      }
    ];
  }

  private buildBasicUserMenu(): MenuItem[] {
    return [
      {
        label: 'sidebar.basicUser.welcome',
        items: [
          {
            label: 'sidebar.basicUser.info',
            icon: 'pi pi-user',
            route: `/${AppRoutingConstants.ENTERPRISE_PATH}/${AppRoutingConstants.ACCOUNT_INFO_PATH}`
          },
          {
            label: 'sidebar.basicUser.enterprise',
            icon: 'pi pi-building',
            route: `/${AppRoutingConstants.ENTERPRISE_PATH}/${AppRoutingConstants.CREATE_ENTERPRISE_PATH}`
          },
          {
            label: 'sidebar.basicUser.invitation',
            icon: 'pi pi-envelope',
            route: `/${AppRoutingConstants.ENTERPRISE_PATH}/${AppRoutingConstants.INVITATION_PATH}`
          }
        ]
      }
    ];
  }

  private buildAdminMenu(): MenuItem[] {
    return [
      {
        label: 'sidebar.admin.title',
        items: [
          {
            label: 'sidebar.admin.packageCredit',
            icon: 'pi pi-chart-line',
            route: `/${AppRoutingConstants.ADMIN_PATH}/${AppRoutingConstants.PACKAGE_CREDIT_PATH}`
          },
          {
            label: 'sidebar.admin.creditRatio',
            icon: 'pi pi-percentage',
            route: `/${AppRoutingConstants.ADMIN_PATH}/${AppRoutingConstants.CREDIT_CONVERT_RATIO}`
          },
          {
            label: 'sidebar.admin.emissionSource',
            icon: 'pi pi-asterisk',
            route: `/${AppRoutingConstants.ADMIN_PATH}/${AppRoutingConstants.EMISSION_SOURCE}`
          },
          {
            label: 'sidebar.admin.fuel',
            icon: 'pi pi-sliders-v',
            route: `/${AppRoutingConstants.ADMIN_PATH}/${AppRoutingConstants.FUEL_CONVERSION}`
          },
          {
            label: 'sidebar.admin.emissionFactor',
            icon: 'pi pi-sliders-h',
            route: `/${AppRoutingConstants.ADMIN_PATH}/${AppRoutingConstants.EMISSION_FACTOR}`
          }
        ]
      }
    ];
  }
}
