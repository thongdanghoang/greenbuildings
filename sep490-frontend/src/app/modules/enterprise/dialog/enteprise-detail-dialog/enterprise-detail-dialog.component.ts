import {Component, OnInit} from '@angular/core';
import {EnterpriseDetailDTO} from '@generated/models/enterprise-detail-dto';
import {EnterpriseService} from '@services/enterprise.service';
import {StorageService} from '@services/storage.service';
import {SubscriptionAwareComponent} from '@shared/directives/subscription-aware.component';
import {DynamicDialogConfig, DynamicDialogRef} from 'primeng/dynamicdialog';
import {takeUntil} from 'rxjs';
import {UUID} from '../../../../../types/uuid';

@Component({
  selector: 'app-enteprise-detail-dialog',
  templateUrl: './enterprise-detail-dialog.component.html',
  styleUrl: './enterprise-detail-dialog.component.css'
})
export class EnterpriseDetailDialogComponent extends SubscriptionAwareComponent implements OnInit {
  buildingId!: UUID;
  enterprise!: EnterpriseDetailDTO;

  constructor(
    private readonly ref: DynamicDialogRef,
    protected enterpriseService: EnterpriseService,
    protected fileService: StorageService,
    public config: DynamicDialogConfig<UUID>
  ) {
    super();
  }

  ngOnInit(): void {
    this.buildingId = this.config.data!;
    this.loadEnterpriseDetails();
  }

  loadEnterpriseDetails(): void {
    if (this.buildingId) {
      this.enterpriseService
        .getEnterpriseDetailsByBuildingId(this.buildingId)
        .pipe(takeUntil(this.destroy$))
        .subscribe(rs => {
          this.enterprise = rs;
        });
    }
  }

  close(): void {
    this.ref.close();
  }

  openLicenseUrl(): void {
    if (this.enterprise.businessLicenseImageUrl) {
      this.fileService.downloadBusinessLicense(this.enterprise.businessLicenseImageUrl).subscribe((fileData: Blob) => {
        const link = document.createElement('a');
        link.href = URL.createObjectURL(fileData);
        link.download = this.enterprise.businessLicenseImageUrl as string;
        link.click();
        URL.revokeObjectURL(link.href);
      });
    }
  }
}
