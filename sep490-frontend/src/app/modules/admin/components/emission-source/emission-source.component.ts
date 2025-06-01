import {Component, EventEmitter, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {DialogService, DynamicDialogConfig, DynamicDialogRef} from 'primeng/dynamicdialog';
import {Observable, switchMap} from 'rxjs';
import {UUID} from '../../../../../types/uuid';
import {EmissionSource, ExcelImportDTO} from '@models/enterprise';
import {ApplicationService} from '@services/application.service';
import {SubscriptionAwareComponent} from '@shared/directives/subscription-aware.component';
import {TableTemplateColumn} from '@shared/components/table-template/table-template.component';
import {SearchCriteriaDto, SearchResultDto} from '@shared/models/base-models';
import {EmissionSourceDialogComponent} from '../../dialog/emission-source-dialog/emission-source-dialog.component';
import {EmissionSourceService} from '@services/emission-source.service';
import {ToastProvider} from '@shared/services/toast-provider';

export interface EmissionSourceCriteria {
  criteria: string;
}
@Component({
  selector: 'app-emission-source',
  templateUrl: './emission-source.component.html',
  styleUrl: './emission-source.component.css'
})
export class EmissionSourceComponent extends SubscriptionAwareComponent implements OnInit {
  @ViewChild('actionsTemplate', {static: true})
  actionsTemplate!: TemplateRef<any>;
  ref: DynamicDialogRef | undefined;
  isLoading: boolean = false;
  importExcelDTO: ExcelImportDTO | undefined;
  protected fetchEmissionSource!: (
    criteria: SearchCriteriaDto<EmissionSourceCriteria>
  ) => Observable<SearchResultDto<EmissionSource>>;
  protected cols: TableTemplateColumn[] = [];
  protected readonly searchEvent: EventEmitter<void> = new EventEmitter();
  protected selected: EmissionSource[] = [];
  protected searchCriteria: EmissionSourceCriteria = {criteria: ''};
  constructor(
    protected readonly applicationService: ApplicationService,
    private readonly emissionSourceService: EmissionSourceService,
    private readonly dialogService: DialogService,
    private readonly messageService: ToastProvider
  ) {
    super();
  }

  ngOnInit(): void {
    this.buildCols();
    this.fetchEmissionSource = this.emissionSourceService.getEmissionSource.bind(this.emissionSourceService);
  }

  uploadExcel(event: any): void {
    const file = event.files[0];
    if (!file) return;
    this.isLoading = true;
    this.emissionSourceService.importEmissionSources(file).subscribe({
      next: () => {
        this.isLoading = false;
        this.messageService.success({
          severity: 'success',
          summary: 'Thành công',
          detail: 'Import dữ liệu thành công'
        });
        this.search(); // Refresh lại table
      },
      error: err => {
        this.isLoading = false;
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

    this.isLoading = true;
    this.emissionSourceService.uploadExcelToMinio(file).subscribe({
      next: () => {
        this.isLoading = false;
        this.messageService.success({
          severity: 'success',
          summary: 'Thành công',
          detail: `Tệp Excel đã được tải lên MinIO`
        });
        this.search(); // Refresh lại table
      },
      error: err => {
        this.isLoading = false;
        this.messageService.businessError({
          severity: 'error',
          summary: 'Lỗi',
          detail: `Tải lên thất bại: ${err.message}`
        });
      }
    });
  }

  onDownloadFile(): void {
    this.emissionSourceService
      .getExcelImportDTO()
      .pipe(
        switchMap((data: ExcelImportDTO) => {
          this.importExcelDTO = data;

          if (!this.importExcelDTO?.fileName) {
            throw new Error('Không tìm thấy thông tin tệp để tải xuống');
          }

          return this.emissionSourceService.getFile();
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

  buildCols(): void {
    this.cols.push({
      field: 'nameVN',
      header: 'Nguồn phát thải'
    });
    this.cols.push({
      field: 'nameEN',
      header: 'Emission source'
    });
    this.cols.push({
      field: 'nameZH',
      header: '排放源'
    });
    this.cols.push({
      field: 'actions',
      header: '',
      templateRef: this.actionsTemplate
    });
  }

  onSelectionChange(selectedUsers: EmissionSource[]): void {
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
    this.ref = this.dialogService.open(EmissionSourceDialogComponent, config);
    this.ref.onClose.subscribe((result: boolean | undefined) => {
      if (result) {
        this.searchEvent.emit(); // Refresh the list if dialog closed with a result
      }
    });
  }

  // Add a new emission source
  addEmissionSource(): void {
    this.openNewActivityDialog(undefined); // Explicitly pass undefined for clarity
  }

  // Edit an existing emission source
  onEdit(rowData: EmissionSource): void {
    this.selected = [rowData];
    const emissionId = this.selected[0].id; // Fixed typo: emisstionId -> emissionId
    this.openNewActivityDialog(emissionId);
  }
  onDelete(rowData: EmissionSource): void {
    this.selected = [rowData];
  }
}
