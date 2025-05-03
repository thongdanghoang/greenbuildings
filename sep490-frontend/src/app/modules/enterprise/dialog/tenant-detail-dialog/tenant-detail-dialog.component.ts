import {Component, OnInit} from '@angular/core';
import {TenantDetailDTO} from '@generated/models/tenant-detail-dto';
import {TenantService} from '@services/tenant.service';
import {SubscriptionAwareComponent} from '@shared/directives/subscription-aware.component';
import {DynamicDialogConfig, DynamicDialogRef} from 'primeng/dynamicdialog';
import {takeUntil} from 'rxjs';
import {UUID} from '../../../../../types/uuid';

@Component({
  selector: 'app-tenant-detail-dialog',
  templateUrl: './tenant-detail-dialog.component.html',
  styleUrl: './tenant-detail-dialog.component.css'
})
export class TenantDetailDialogComponent extends SubscriptionAwareComponent implements OnInit {
  tenantId!: UUID;
  tenant!: TenantDetailDTO;

  constructor(
    private readonly ref: DynamicDialogRef,
    protected tenantService: TenantService,
    public config: DynamicDialogConfig<UUID>
  ) {
    super();
  }

  ngOnInit(): void {
    this.tenantId = this.config.data!;
    this.loadTenantDetail();
  }

  loadTenantDetail(): void {
    if (this.tenantId) {
      this.tenantService
        .getTenantDetail(this.tenantId)
        .pipe(takeUntil(this.destroy$))
        .subscribe(tenant => {
          this.tenant = tenant;
        });
    }
  }

  close(): void {
    this.ref.close();
  }

  openLicenseUrl(): void {
    if (this.tenant.businessLicenseImageUrl) {
      window.open(this.tenant.businessLicenseImageUrl, '_blank');
    }
  }
}
