import {Component} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {MessageService} from 'primeng/api';
import {filter, map, switchMap, takeUntil} from 'rxjs';
import {validate} from 'uuid';
import {UUID} from '../../../../../types/uuid';
import {AppRoutingConstants} from '../../../../app-routing.constant';
import {ApplicationConstant} from '../../../../application.constant';
import {SubscriptionAwareComponent} from '../../../core/subscription-aware.component';
import {SelectableItem} from '../../../shared/models/base-models';
import {PowerBiAccessTokenService} from '../../services/power-bi-access-token.service';

@Component({
  selector: 'app-power-bi-regenerate-access-token',
  templateUrl: './power-bi-regenerate-access-token.component.html',
  styleUrl: './power-bi-regenerate-access-token.component.css'
})
export class PowerBiRegenerateAccessTokenComponent extends SubscriptionAwareComponent {
  readonly defaultExpirationTimeOption: SelectableItem<Date> = {
    label: 'powerBi.accessToken.expiration.30days',
    value: new Date(Date.now() + 30 * 24 * 60 * 60 * 1000)
  };
  readonly customExpirationTimeOption: SelectableItem<null> = {
    label: 'powerBi.accessToken.expiration.custom',
    value: null
  };
  protected selectableExpirationTimes: SelectableItem<Date | null>[] = [
    {
      label: 'powerBi.accessToken.expiration.7days',
      value: new Date(Date.now() + 7 * 24 * 60 * 60 * 1000)
    },
    this.defaultExpirationTimeOption,
    {
      label: 'powerBi.accessToken.expiration.60days',
      value: new Date(Date.now() + 60 * 24 * 60 * 60 * 1000)
    },
    {
      label: 'powerBi.accessToken.expiration.90days',
      value: new Date(Date.now() + 90 * 24 * 60 * 60 * 1000)
    },
    this.customExpirationTimeOption,
    {
      label: 'powerBi.accessToken.expiration.never',
      value: new Date(Date.now() + 100 * 365 * 24 * 60 * 60 * 1000)
    }
  ];

  protected expirationTime: Date | null =
    this.defaultExpirationTimeOption.value;

  constructor(
    private readonly activatedRoute: ActivatedRoute,
    private readonly router: Router,
    private readonly powerBiAccessTokenService: PowerBiAccessTokenService,
    private readonly notification: MessageService
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
            this.notification.add({
              severity: 'success',
              summary: 'Regenerated',
              detail: 'Regenerate Power BI access token successfully'
            });
          }
        },
        error: error =>
          this.notification.add({
            severity: 'error',
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
