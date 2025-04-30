import {HttpClient} from '@angular/common/http';
import {Component} from '@angular/core';
import {AbstractControl, FormBuilder, FormControl, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {takeUntil} from 'rxjs';
import {UUID} from '../../../../../types/uuid';
import {AppRoutingConstants} from '../../../../app-routing.constant';
import {TenantDetailDTO} from '@models/enterprise';
import {AbstractFormComponent} from '@shared/components/form/abstract-form-component';
import {ToastProvider} from '@shared/services/toast-provider';
import {TenantService} from '@services/tenant.service';
import {Location} from '@angular/common';
import {ApplicationService} from '@services/application.service';

@Component({
  selector: 'app-tenant-profile',
  templateUrl: './tenant-profile.component.html',
  styleUrl: './tenant-profile.component.css'
})
export class TenantProfileComponent extends AbstractFormComponent<TenantDetailDTO> {
  protected readonly tenantStructure = {
    id: new FormControl<UUID | null>({value: null, disabled: true}),
    version: new FormControl(null),
    name: new FormControl<string>('', {
      validators: [Validators.required]
    }),
    email: new FormControl<string>('', {
      validators: [Validators.required, Validators.email]
    }),
    hotline: new FormControl<string>('', {
      validators: [Validators.required, Validators.pattern('[0-9]{10}')]
    })
  };

  private enterpriseId: UUID | null = null;

  constructor(
    httpClient: HttpClient,
    formBuilder: FormBuilder,
    notificationService: ToastProvider,
    translate: TranslateService,
    protected tenantService: TenantService,
    private readonly router: Router,
    private readonly location: Location,
    private readonly applicationService: ApplicationService
  ) {
    super(httpClient, formBuilder, notificationService, translate);
  }

  back(): void {
    this.location.back();
  }

  get isEdit(): boolean {
    return !!this.tenantStructure.id.value;
  }

  override reset(): void {
    if (this.isEdit) {
      return this.initializeData();
    }
    super.reset();
  }

  protected initializeData(): void {
    this.applicationService.TenantId.pipe(takeUntil(this.destroy$)).subscribe(tenantId => {
      if (tenantId) {
        this.enterpriseId = tenantId;
        this.loadTenantDetail();
      }
    });
  }

  protected initializeFormControls(): {[p: string]: AbstractControl} {
    return this.tenantStructure;
  }

  protected override prepareDataBeforeSubmit(): void {}

  protected submitFormDataUrl(): string {
    if (this.enterpriseId) {
      return `${AppRoutingConstants.ENTERPRISE_API_URL}/tenants/${this.enterpriseId}/detail`;
    }
    return '';
  }

  protected onSubmitFormDataSuccess(): void {
    void this.router.navigate(['/', AppRoutingConstants.ENTERPRISE_PATH, 'tenants']);
  }

  private loadTenantDetail(): void {
    if (this.enterpriseId) {
      this.tenantService
        .getTenantDetail(this.enterpriseId)
        .pipe(takeUntil(this.destroy$))
        .subscribe(tenant => {
          this.formGroup.patchValue(tenant);
        });
    }
  }
}
