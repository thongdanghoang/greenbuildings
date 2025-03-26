import {HttpClient} from '@angular/common/http';
import {Component} from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormControl,
  Validators
} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {MessageService} from 'primeng/api';
import {filter, map, switchMap, takeUntil, tap} from 'rxjs';
import {validate} from 'uuid';
import {UUID} from '../../../../../types/uuid';
import {AppRoutingConstants} from '../../../../app-routing.constant';
import {ApplicationConstant} from '../../../../application.constant';
import {AbstractFormComponent} from '../../../shared/components/form/abstract-form-component';
import {SelectableItem} from '../../../shared/models/base-models';
import {PowerBiAuthority} from '../../models/power-bi-authority.dto';
import {PowerBiAccessTokenService} from '../../services/power-bi-access-token.service';

@Component({
  selector: 'app-power-bi-access-token-detail',
  templateUrl: './power-bi-access-token-detail.component.html',
  styleUrl: './power-bi-access-token-detail.component.css'
})
export class PowerBiAccessTokenDetailComponent extends AbstractFormComponent<PowerBiAuthority> {
  readonly defaultExpirationTimeOption: SelectableItem<Date> = {
    label: 'powerBi.accessToken.expiration.30days',
    value: new Date(Date.now() + 30 * 24 * 60 * 60 * 1000)
  };
  readonly customExpirationTimeOption: SelectableItem<null> = {
    label: 'powerBi.accessToken.expiration.custom',
    value: null
  };

  protected readonly powerBiAuthorityFormStructure = {
    id: new FormControl<UUID | null>({value: null, disabled: true}),
    version: new FormControl(null),
    note: new FormControl(null, [Validators.required]),
    expirationTime: new FormControl<Date>(
      this.defaultExpirationTimeOption.value,
      {
        nonNullable: true,
        validators: [Validators.required]
      }
    )
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

  constructor(
    httpClient: HttpClient,
    formBuilder: FormBuilder,
    notificationService: MessageService,
    translate: TranslateService,
    private readonly activatedRoute: ActivatedRoute,
    private readonly router: Router,
    private readonly powerBiService: PowerBiAccessTokenService
  ) {
    super(httpClient, formBuilder, notificationService, translate);
  }

  get regenerateTokenLink(): string {
    return [
      '',
      AppRoutingConstants.AUTH_PATH,
      AppRoutingConstants.SETTINGS,
      AppRoutingConstants.POWER_BI,
      AppRoutingConstants.ACCESS_TOKEN,
      this.powerBiAuthorityFormStructure.id.value,
      AppRoutingConstants.REGENERATE
    ].join('/');
  }

  get isEdit(): boolean {
    return !!this.powerBiAuthorityFormStructure.id.value;
  }

  navigateToRegenerate(): void {
    void this.router.navigate([
      '/',
      AppRoutingConstants.AUTH_PATH,
      AppRoutingConstants.SETTINGS,
      AppRoutingConstants.POWER_BI,
      AppRoutingConstants.ACCESS_TOKEN,
      this.powerBiAuthorityFormStructure.id.value,
      AppRoutingConstants.REGENERATE
    ]);
  }

  cancel(): void {
    void this.router.navigate([
      '/',
      AppRoutingConstants.AUTH_PATH,
      AppRoutingConstants.SETTINGS,
      AppRoutingConstants.POWER_BI,
      AppRoutingConstants.ACCESS_TOKEN
    ]);
  }

  protected override initializeFormControls(): {
    [key: string]: AbstractControl;
  } {
    return this.powerBiAuthorityFormStructure;
  }

  protected override initializeData(): void {
    this.activatedRoute.paramMap
      .pipe(
        takeUntil(this.destroy$),
        map(params => params.get('id')),
        filter((idParam): idParam is string => !!idParam),
        filter(id => validate(id)),
        map(id => id as UUID),
        tap(id => this.powerBiAuthorityFormStructure.id.setValue(id)),
        switchMap(id => this.powerBiService.findById(id))
      )
      .subscribe(accessToken => {
        this.formGroup.patchValue(accessToken);
        this.powerBiAuthorityFormStructure.expirationTime.setValue(
          new Date(accessToken.expirationTime)
        );
      });
  }

  protected override submitFormDataUrl(): string {
    return this.powerBiService.POWER_BI_AUTHORITY_URL;
  }

  protected override onSubmitFormDataSuccess(result: any): void {
    if (result) {
      if (result.apiKey) {
        sessionStorage.setItem(
          ApplicationConstant.NEWEST_POWER_BI_ACCESS_TOKEN_KEY,
          JSON.stringify(result)
        );
      }
      void this.router.navigate([
        '/',
        AppRoutingConstants.AUTH_PATH,
        AppRoutingConstants.SETTINGS,
        AppRoutingConstants.POWER_BI,
        AppRoutingConstants.ACCESS_TOKEN
      ]);
    }
  }
}
