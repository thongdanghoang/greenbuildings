import {Component, EventEmitter, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {DialogService, DynamicDialogConfig, DynamicDialogRef} from 'primeng/dynamicdialog';
import {Observable, switchMap} from 'rxjs';
import {UUID} from '../../../../../types/uuid';
import {ApplicationService} from '@services/application.service';
import {SubscriptionAwareComponent} from '@shared/directives/subscription-aware.component';
import {TableTemplateColumn} from '@shared/components/table-template/table-template.component';
import {SearchCriteriaDto, SearchResultDto} from '@shared/models/base-models';
import {EmissionFactorDTO, EmissionSourceDTO, FuelDTO} from '@models/shared-models';
import {ToastProvider} from '@shared/services/toast-provider';
import {EmissionFactorDialogComponent} from '../../dialog/emission-factor-dialog/emission-factor-dialog.component';
import {EmissionFactorService} from '@services/emission_factor.service';
import {ExcelImportDTO} from '@models/enterprise';

export interface EmissionFactorCriteria {
  criteria: string;
}
@Component({
  selector: 'app-emission-factor',
  templateUrl: './emission-factor.component.html',
  styleUrl: './emission-factor.component.css'
})
export class EmissionFactorComponent extends SubscriptionAwareComponent implements OnInit {
  @ViewChild('endDateTemplate', {static: true})
  endDateTemplate!: TemplateRef<any>;
  @ViewChild('startDateTemplate', {static: true})
  startDateTemplate!: TemplateRef<any>;
  @ViewChild('nameTemplate', {static: true})
  nameTemplate!: TemplateRef<any>;
  @ViewChild('emissionSourceTemplate', {static: true})
  emissionSourceTemplate!: TemplateRef<any>;
  @ViewChild('energyConversionTemplate', {static: true})
  energyConversionTemplate!: TemplateRef<any>;
  @ViewChild('actionsTemplate', {static: true})
  actionsTemplate!: TemplateRef<any>;
  @ViewChild('isDirectEmissionTemplate', {static: true})
  isDirectEmissionTemplate!: TemplateRef<any>; // New template reference
  ref: DynamicDialogRef | undefined;
  importExcelDTO: ExcelImportDTO | undefined;
  protected fetchEmissionFactor!: (
    criteria: SearchCriteriaDto<EmissionFactorCriteria>
  ) => Observable<SearchResultDto<EmissionFactorDTO>>;
  protected cols: TableTemplateColumn[] = [];
  protected readonly searchEvent: EventEmitter<void> = new EventEmitter();
  protected selected: EmissionFactorDTO[] = [];
  protected searchCriteria: EmissionFactorCriteria = {criteria: ''};
  constructor(
    protected readonly applicationService: ApplicationService,
    private readonly emissionFactorService: EmissionFactorService,
    private readonly messageService: ToastProvider,
    public readonly translate: TranslateService,
    private readonly dialogService: DialogService
  ) {
    super();
  }

  ngOnInit(): void {
    this.buildCols();
    this.fetchEmissionFactor = this.emissionFactorService.getEmissionFactor.bind(this.emissionFactorService);
  }

  getLocalizedEmissionSourceName(source: EmissionSourceDTO | undefined): string {
    if (!source) return '';

    let lang = this.translate.currentLang.toUpperCase(); // Changed from const to let
    if (lang === 'VI') {
      lang = 'VN'; // Now this works because lang is mutable
    }

    return (source[`name${lang}` as keyof EmissionSourceDTO] as string) || source.nameEN;
  }

  uploadExcel(event: any): void {
    const file = event.files[0];
    if (!file) return;

    this.emissionFactorService.importEmissionFactorExcel(file).subscribe({
      next: () => {
        this.messageService.success({
          severity: 'success',
          summary: 'Thành công',
          detail: 'Import dữ liệu thành công'
        });
        this.search(); // Refresh lại table
      },
      error: err => {
        this.messageService.businessError({
          severity: 'error',
          summary: 'Lỗi',
          detail: `Import thất bại: ${err.message}`
        });
      }
    });
  }

  uploadExcelToMinio(event: any): void {
    const file = event.target.files[0];
    if (!file) return;

    this.emissionFactorService.uploadExcelToMinio(file).subscribe({
      next: () => {
        this.messageService.success({
          severity: 'success',
          summary: 'Thành công',
          detail: `Tệp Excel đã được tải lên MinIO`
        });
        this.search(); // Refresh lại table
      },
      error: err => {
        this.messageService.businessError({
          severity: 'error',
          summary: 'Lỗi',
          detail: `Tải lên thất bại: ${err.message}`
        });
      }
    });
  }

  onDownloadFile(): void {
    this.emissionFactorService
      .getExcelImportDTO()
      .pipe(
        switchMap((data: ExcelImportDTO) => {
          this.importExcelDTO = data;

          if (!this.importExcelDTO?.fileName) {
            throw new Error('Không tìm thấy thông tin tệp để tải xuống');
          }

          return this.emissionFactorService.getFile();
        })
      )
      .subscribe({
        next: (fileData: Blob) => {
          const link = document.createElement('a');
          link.href = URL.createObjectURL(fileData);
          link.download = this.importExcelDTO!.fileName;
          link.click();
          URL.revokeObjectURL(link.href);
        },
        error: err => {
          this.messageService.businessError({
            severity: 'error',
            summary: 'Lỗi',
            detail: err.message || 'Tải tệp thất bại'
          });
        }
      });
  }

  getLocalizedFuelName(source: FuelDTO | undefined): string {
    if (!source) return '';

    let lang = this.translate.currentLang.toUpperCase(); // Changed from const to let
    if (lang === 'VI') {
      lang = 'VN'; // Now this works because lang is mutable
    }

    return (source[`name${lang}` as keyof FuelDTO] as string) || source.nameEN;
  }

  // Lấy tên hệ số phát thải theo ngôn ngữ hiện tại
  getLocalizedEmissionFactorName(emissionFactor: EmissionFactorDTO): string {
    if (!emissionFactor) return '';

    let lang = this.translate.currentLang.toUpperCase();
    if (lang === 'VI') {
      lang = 'VN';
    }

    return (
      (emissionFactor[`name${lang}` as keyof EmissionFactorDTO] as string) || emissionFactor.nameEN // Mặc định trả về tên tiếng Anh
    );
  }
  // eslint-disable-next-line max-lines-per-function
  buildCols(): void {
    this.cols.push({
      field: 'name',
      header: 'enterprise.emission.activity.dialog.EmissionFactor',
      templateRef: this.nameTemplate // Tham chiếu đến template mới
    });
    this.cols.push({
      field: 'emissionSourceDTO',
      header: 'admin.emissionSource.name',
      templateRef: this.emissionSourceTemplate
    });
    this.cols.push({
      field: 'energyConversionDTO',
      header: 'sidebar.admin.fuel',
      templateRef: this.energyConversionTemplate
    });
    this.cols.push({
      field: 'co2',
      header: 'Co2'
    });
    this.cols.push({
      field: 'ch4',
      header: 'Ch4'
    });
    this.cols.push({
      field: 'n2o',
      header: 'N2o'
    });

    this.cols.push({
      field: 'directEmission',
      header: 'admin.emissionFactor.isDirectEmission',
      templateRef: this.isDirectEmissionTemplate
    });
    this.cols.push({
      field: 'active',
      header: 'common.active',
      templateRef: this.actionsTemplate
    });
  }

  onSelectionChange(selectedUsers: EmissionFactorDTO[]): void {
    this.selected = selectedUsers;
  }

  search(): void {
    this.searchEvent.emit();
  }

  openNewActivityDialog(emissionId?: UUID): void {
    // Made emissionId optional with ?
    const config: DynamicDialogConfig<UUID | undefined> = {
      // Allow undefined in config.data
      data: emissionId, // Will be undefined for add, UUID for edit
      closeOnEscape: true,
      dismissableMask: true,
      showHeader: false
    };

    // Clean up previous dialog if it exists
    if (this.ref) {
      this.ref.close();
    }

    // Open the dialog with EmissionSourceDialogComponent
    this.ref = this.dialogService.open(EmissionFactorDialogComponent, config);
    this.ref.onClose.subscribe((result: boolean | undefined) => {
      if (result) {
        this.searchEvent.emit(); // Refresh the list if dialog closed with a result
      }
    });
  }

  onEdit(rowData: EmissionFactorDTO): void {
    this.selected = [rowData];
    const emissionId = this.selected[0].id; // Fixed typo: emisstionId -> emissionId
    this.openNewActivityDialog(emissionId);
  }

  onDelete(rowData: EmissionFactorDTO): void {
    this.selected = [rowData];
    this.deleteFactor(rowData);
  }

  private deleteFactor(factor: EmissionFactorDTO): void {
    this.emissionFactorService.deleteEmissionFactor(factor.id.toString()).subscribe({
      next: () => {
        this.messageService.success({
          summary: this.translate.instant('admin.packageCredit.message.success.summary'),
          detail: this.translate.instant('admin.packageCredit.message.success.detail')
        });
        this.selected = []; // Clear local selection
      },
      error: () => {
        this.messageService.businessError({
          summary: this.translate.instant('admin.packageCredit.message.error.summary'),
          detail: this.translate.instant('admin.packageCredit.message.error.detail')
        });
        this.searchEvent.emit(); // Refresh table
      }
    });
  }
}
