import {Component} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {filter, map, switchMap, takeUntil} from 'rxjs';
import {validate} from 'uuid';
import {UUID} from '../../../../../types/uuid';
import {AppRoutingConstants} from '../../../../app-routing.constant';
import {ApplicationConstant} from '../../../../application.constant';
import {SubscriptionAwareComponent} from '@shared/directives/subscription-aware.component';
import {SelectableItem} from '@shared/models/base-models';
import {ToastProvider} from '@shared/services/toast-provider';
import {PowerBiAccessTokenService} from '@services/power-bi-access-token.service';
import {PowerBiAccessTokenExpirationOptions} from '../power-bi-access-token-expiration-options';

@Component({
  selector: 'app-power-bi-regenerate-access-token',
  templateUrl: './power-bi-regenerate-access-token.component.html',
  styleUrl: './power-bi-regenerate-access-token.component.css'
})
export class PowerBiRegenerateAccessTokenComponent extends SubscriptionAwareComponent {
  protected expirationTime: Date | null =
    PowerBiAccessTokenExpirationOptions.defaultExpirationTimeOption.value;
  protected selectableExpirationTimes: SelectableItem<Date | null>[] =
    PowerBiAccessTokenExpirationOptions.selectableExpirationTimes;

  constructor(
    private readonly activatedRoute: ActivatedRoute,
    private readonly router: Router,
    private readonly powerBiAccessTokenService: PowerBiAccessTokenService,
    private readonly notification: ToastProvider
  ) {
    super();
  }

  // eslint-disable-next-line max-lines-per-function
  regenerateAccessToken(): void {
    this.activatedRoute.paramMap
      .pipe(
        takeUntil(this.destroy$),
        map(params => params.get('id')),
        filter((idParam): idParam is string => !!idParam),
        filter(id => validate(id)),
        map(id => id as UUID),
        switchMap(id => {
          if (this.expirationTime === null) {
            throw new Error(
              'Expiration time cannot be null. Please select a valid expiration time.'
            );
          }
          return this.powerBiAccessTokenService.regenerateAccessToken(
            id,
            this.expirationTime
          );
        })
      )
      .subscribe({
        next: result => {
          if (result.apiKey) {
            sessionStorage.setItem(
              ApplicationConstant.NEWEST_POWER_BI_ACCESS_TOKEN_KEY,
              JSON.stringify(result)
            );
            this.navigateToPowerBiAccessTokens();
            this.notification.success({
              summary: 'Regenerated',
              detail: 'Regenerate Power BI access token successfully'
            });
          }
        },
        error: error =>
          this.notification.businessError({
            summary: 'Failed',
            detail: error.message
          })
      });
  }

  navigateToPowerBiAccessTokens(): void {
    void this.router.navigate([
      '/',
      AppRoutingConstants.AUTH_PATH,
      AppRoutingConstants.SETTINGS,
      AppRoutingConstants.POWER_BI,
      AppRoutingConstants.ACCESS_TOKEN
    ]);
  }
}
