import {Component, EventEmitter, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {AssetSearchCriteria} from '@generated/models/asset-search-criteria';
import {AssetView} from '@generated/models/asset-view';
import {TranslateService} from '@ngx-translate/core';
import {AssetService} from '@services/asset.service';
import {TableTemplateColumn} from '@shared/components/table-template/table-template.component';
import {SubscriptionAwareComponent} from '@shared/directives/subscription-aware.component';
import {SearchCriteriaDto, SearchResultDto} from '@shared/models/base-models';
import {ModalProvider} from '@shared/services/modal-provider';
import {ToastProvider} from '@shared/services/toast-provider';
import {Observable, of, switchMap, takeUntil} from 'rxjs';
import {UUID} from '../../../../../types/uuid';
import {AssetDetailsDialogComponent} from '../asset-details-dialog/asset-details-dialog.component';

@Component({
  selector: 'app-assets-view',
  templateUrl: './assets-view.component.html',
  styleUrl: './assets-view.component.css'
})
export class AssetsViewComponent extends SubscriptionAwareComponent implements OnInit {
  protected cols: TableTemplateColumn[] = [];
  protected search: (criteria: SearchCriteriaDto<AssetSearchCriteria>) => Observable<SearchResultDto<AssetView>>;
  protected readonly searchEvent: EventEmitter<void> = new EventEmitter();
  protected searchCriteria: AssetSearchCriteria = {criteria: ''};

  @ViewChild('buildingTemplate', {static: true})
  protected buildingTemplate!: TemplateRef<any>;
  @ViewChild('assetColActions', {static: true})
  protected assetColActions!: TemplateRef<any>;

  constructor(
    private readonly assetService: AssetService,
    private readonly modalProvider: ModalProvider,
    private readonly toastProvider: ToastProvider,
    private readonly translateService: TranslateService
  ) {
    super();
    this.search = this.assetService.searchAssets.bind(this.assetService);
  }

  ngOnInit(): void {
    this.buildCols();
  }

  openNewAssetDialog(): void {
    this.modalProvider
      .openDynamicDialog(AssetDetailsDialogComponent, {})
      .pipe(takeUntil(this.destroy$))
      .subscribe(rs => {
        if (rs) {
          this.searchEvent.emit();
        }
      });
  }

  openEditAsset(id: UUID): void {
    this.modalProvider
      .openDynamicDialog(AssetDetailsDialogComponent, {id})
      .pipe(takeUntil(this.destroy$))
      .subscribe(rs => {
        if (rs) {
          this.searchEvent.emit();
        }
      });
  }

  onDeleteAsset(id: UUID): void {
    this.modalProvider
      .showDefaultConfirm()
      .pipe(
        takeUntil(this.destroy$),
        switchMap((result: boolean) => {
          if (!result) {
            return of(null);
          }
          return this.assetService.deleteAsset(id).pipe(
            switchMap(() => {
              this.toastProvider.success({
                summary: this.translateService.instant('common.success')
              });
              this.searchEvent.emit();
              return of(null);
            })
          );
        })
      )
      .subscribe();
  }

  private buildCols(): void {
    this.cols.push({
      field: 'name',
      header: 'assets.table.header.name',
      sortable: true
    });
    this.cols.push({
      field: 'description',
      header: 'assets.table.header.description',
      sortable: true
    });
    this.cols.push({
      field: 'building',
      header: 'assets.table.header.building.name',
      templateRef: this.buildingTemplate
    });
    this.cols.push({
      field: 'id',
      header: '',
      templateRef: this.assetColActions
    });
  }
}
