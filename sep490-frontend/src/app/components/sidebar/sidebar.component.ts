import {Component, OnInit} from '@angular/core';
import {UserRole} from '@models/role-names';
import {ApplicationService, UserData} from '@services/application.service';
import {SubscriptionAwareComponent} from '@shared/directives/subscription-aware.component';
import {MenuItem} from 'primeng/api';
import {filter, switchMap, takeUntil} from 'rxjs';
import {AppRoutingConstants} from '../../app-routing.constant';

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

  constructor(private readonly applicationService: ApplicationService) {
    super();
  }

  // eslint-disable-next-line max-lines-per-function
  ngOnInit(): void {
    this.applicationService
      .isEmailVerified()
      .pipe(
        takeUntil(this.destroy$),
        filter(verified => verified),
        switchMap(() => this.applicationService.UserData),
        takeUntil(this.destroy$)
      )
      .subscribe((userData: UserData): void => {
        switch (true) {
          case this.applicationService.includeRole(
            userData.authorities,
            UserRole.ENTERPRISE_OWNER
          ):
            this.items = this.buildEnterpriseOwnerMenu();
            break;
          case this.applicationService.includeRole(
            userData.authorities,
            UserRole.TENANT
          ):
            this.items = this.buildTenantMenu();
            break;
          case this.applicationService.includeRole(
            userData.authorities,
            UserRole.SYSTEM_ADMIN
          ):
            this.items = this.buildAdminMenu();
            break;
          case this.applicationService.includeRole(
            userData.authorities,
            UserRole.BASIC_USER
          ):
            this.items = this.buildBasicUserMenu();
            break;
        }
      });
  }

  // eslint-disable-next-line max-lines-per-function
  buildTenantMenu(): MenuItem[] {
    return [
      {
        label: 'sidebar.basicUser.welcome',
        items: [
          {
            label: 'sidebar.tenant.profile',
            icon: 'pi pi-id-card',
            route: `/${AppRoutingConstants.ENTERPRISE_PATH}/${AppRoutingConstants.TENANT_PROFILE}`
          }
        ]
      },
      {
        label: 'sidebar.tenant.manageMenu',
        items: [
          {
            label: 'sidebar.tenant.manageTenant',
            icon: 'pi pi-user',
            route: `/${AppRoutingConstants.ENTERPRISE_PATH}/${AppRoutingConstants.TENANT_MANAGEMENT_PATH}`
          },
          {
            label: 'sidebar.owner.activityType',
            icon: 'pi pi-sitemap',
            route: `/${AppRoutingConstants.ENTERPRISE_PATH}/${AppRoutingConstants.ACTIVITY_TYPE}`
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

  // eslint-disable-next-line max-lines-per-function
  private buildEnterpriseOwnerMenu(): MenuItem[] {
    return [
      {
        label: 'enterprise.title',
        items: [
          {
            label: 'sidebar.owner.profile',
            icon: 'pi pi-user-edit',
            route: `/${AppRoutingConstants.ENTERPRISE_PATH}/${AppRoutingConstants.ENTERPRISE_PROFILE}`
          },
          {
            label: 'sidebar.owner.dashboard',
            icon: 'pi pi-chart-line',
            route: `/${AppRoutingConstants.DASHBOARD_PATH}`
          },
          {
            label: 'sidebar.owner.emission',
            icon: 'pi pi-percentage',
            route: `/${AppRoutingConstants.EMISSIONS_PATH}`
          },
          {
            label: 'sidebar.owner.building',
            icon: 'pi pi-building',
            route: `/${AppRoutingConstants.ENTERPRISE_PATH}/${AppRoutingConstants.BUILDING_PATH}`
          }
          // {
          //   label: 'sidebar.owner.activityType',
          //   icon: 'pi pi-sitemap',
          //   route: `/${AppRoutingConstants.ENTERPRISE_PATH}/${AppRoutingConstants.ACTIVITY_TYPE}`
          // }
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
          },
          {
            label: 'sidebar.owner.invitation',
            icon: 'pi pi-envelope',
            route: `/${AppRoutingConstants.ENTERPRISE_PATH}/${AppRoutingConstants.SENT_INVITATION_PATH}`
          }
        ]
      },
      {
        label: 'sidebar.owner.settings.label',
        items: [
          {
            label: 'sidebar.owner.settings.items.powerBiAccessToken',
            icon: 'pi pi-key',
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
          }
        ]
      }
    ];
  }

  // eslint-disable-next-line max-lines-per-function
  private buildAdminMenu(): MenuItem[] {
    return [
      {
        label: 'sidebar.admin.credit',
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
          }
        ]
      },
      {
        label: 'sidebar.admin.emission',
        items: [
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
          },
          {
            label: 'sidebar.admin.chemicalDensity',
            icon: 'pi pi-sparkles',
            route: `/${AppRoutingConstants.ADMIN_PATH}/${AppRoutingConstants.CHEMICAL_DENSITY}`
          }
        ]
      }
    ];
  }
}
