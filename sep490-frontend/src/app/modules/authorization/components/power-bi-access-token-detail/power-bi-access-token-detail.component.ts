import {HttpClient} from '@angular/common/http';
import {Component} from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormControl,
  ValidationErrors,
  ValidatorFn,
  Validators
} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {filter, map, switchMap, takeUntil, tap} from 'rxjs';
import {validate} from 'uuid';
import {UUID} from '../../../../../types/uuid';
import {AppRoutingConstants} from '../../../../app-routing.constant';
import {ApplicationConstant} from '../../../../application.constant';
import {AbstractFormComponent} from '../../../shared/components/form/abstract-form-component';
import {SelectableItem} from '../../../shared/models/base-models';
import {ToastProvider} from '../../../shared/services/toast-provider';
import {
  PowerBiAuthority,
  PowerBiScope
} from '../../models/power-bi-access-token.dto';
import {PowerBiAccessTokenService} from '../../services/power-bi-access-token.service';
import {PowerBiAccessTokenExpirationOptions} from '../power-bi-access-token-expiration-options';

@Component({
  selector: 'app-power-bi-access-token-detail',
  templateUrl: './power-bi-access-token-detail.component.html',
  styleUrl: './power-bi-access-token-detail.component.css'
})
export class PowerBiAccessTokenDetailComponent extends AbstractFormComponent<PowerBiAuthority> {
  protected readonly powerBiAuthorityFormStructure = {
    id: new FormControl<UUID | null>({value: null, disabled: true}),
    version: new FormControl(null),
    note: new FormControl(null, [Validators.required]),
    expirationTime: new FormControl<Date>(
      PowerBiAccessTokenExpirationOptions.defaultExpirationTimeOption.value,
      {
        nonNullable: true,
        validators: [Validators.required]
      }
    ),
    scope: new FormControl<string>('', {
      nonNullable: true,
      validators: [Validators.required]
    }),
    buildings: new FormControl<UUID[]>(
      [],
      [this.selectedBuildingIdsValidator().bind(this)]
    )
  };

  protected selectableExpirationTimes: SelectableItem<Date | null>[] =
    PowerBiAccessTokenExpirationOptions.selectableExpirationTimes;
  protected scopeOptions: SelectableItem<keyof typeof PowerBiScope>[] = [
    {
      disabled: false,
      value: PowerBiScope[PowerBiScope.ENTERPRISE] as keyof typeof PowerBiScope,
      label: 'enum.scope.ENTERPRISE'
    },
    {
      disabled: false,
      value: PowerBiScope[PowerBiScope.BUILDING] as keyof typeof PowerBiScope,
      label: 'enum.scope.BUILDING'
    }
  ];
  protected selectablePowerBiScopes: SelectableItem<any>[] = [];

  constructor(
    httpClient: HttpClient,
    formBuilder: FormBuilder,
    notificationService: ToastProvider,
    translate: TranslateService,
    private readonly activatedRoute: ActivatedRoute,
    private readonly router: Router,
    private readonly powerBiService: PowerBiAccessTokenService
  ) {
    super(httpClient, formBuilder, notificationService, translate);
  }

  get scopeBuilding(): boolean {
    return (
      this.powerBiAuthorityFormStructure.scope.value ===
      PowerBiScope[PowerBiScope.BUILDING]
    );
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

  fetchBuilding(): void {
    this.powerBiService
      .getSelectableBuildings()
      .pipe(takeUntil(this.destroy$))
      .subscribe(buildings => {
        this.selectablePowerBiScopes = buildings.map(building => ({
          disabled: false,
          value: building.id,
          label: building.name
        }));
      });
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
    this.fetchBuilding();
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

  private selectedBuildingIdsValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      if (!this.powerBiAuthorityFormStructure) {
        return null;
      }
      if (this.scopeBuilding) {
        return control.value.length > 0 ? null : {required: true};
      }
      return null;
    };
  }
}
