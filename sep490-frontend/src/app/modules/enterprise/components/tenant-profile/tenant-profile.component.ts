import {Component} from '@angular/core';
import {AbstractControl, FormControl, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {TenantDetailDTO} from '@generated/models/tenant-detail-dto';
import {ApplicationService} from '@services/application.service';
import {TenantService} from '@services/tenant.service';
import {AbstractFormComponent} from '@shared/components/form/abstract-form-component';
import {takeUntil} from 'rxjs';
import {UUID} from '../../../../../types/uuid';
import {AppRoutingConstants} from '../../../../app-routing.constant';

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
    }),
    address: new FormControl<string>('', Validators.required),
    taxCode: new FormControl<string>('', Validators.required)
  };

  private enterpriseId: UUID | null = null;

  constructor(
    protected tenantService: TenantService,
    private readonly router: Router,
    private readonly applicationService: ApplicationService
  ) {
    super();
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
    void this.router.navigate(['/']);
  }

  private loadTenantDetail(): void {
    if (this.enterpriseId) {
      this.tenantService
        .getTenantDetail(this.enterpriseId)
        .pipe(takeUntil(this.destroy$))
        .subscribe(tenant => {
          this.formGroup.patchValue(tenant);

          this.tenantStructure.email.disable();
        });
    }
  }
}
