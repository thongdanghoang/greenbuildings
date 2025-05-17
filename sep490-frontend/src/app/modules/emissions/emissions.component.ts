import {Component, EventEmitter, OnInit, TemplateRef, ViewChild, inject} from '@angular/core';
import {FormBuilder, FormControl} from '@angular/forms';
import {ActivityCriteria} from '@generated/models/activity-criteria';
import {EmissionActivityRecordView} from '@generated/models/emission-activity-record-view';
import {EmissionActivityView} from '@generated/models/emission-activity-view';
import {TranslateService} from '@ngx-translate/core';
import {EmissionActivityRecordService} from '@services/emission-activity-record.service';
import {EmissionActivityService} from '@services/emission-activity.service';
import {TableTemplateColumn} from '@shared/components/table-template/table-template.component';
import {SubscriptionAwareComponent} from '@shared/directives/subscription-aware.component';
import {SearchCriteriaDto, SearchResultDto, SelectableItem} from '@shared/models/base-models';
import {ModalProvider} from '@shared/services/modal-provider';
import {ToastProvider} from '@shared/services/toast-provider';
import {Observable, of, switchMap, takeUntil} from 'rxjs';
import {UUID} from '../../../types/uuid';
import {NewActivityDialogComponent} from '../enterprise/dialog/new-activity-dialog/new-activity-dialog.component';
import {NewActivityRecordDialogComponent} from '../enterprise/dialog/new-activity-record-dialog/new-activity-record-dialog.component';

@Component({
  selector: 'app-emissions',
  templateUrl: './emissions.component.html',
  styleUrl: './emissions.component.css'
})
export class EmissionsComponent extends SubscriptionAwareComponent implements OnInit {
  protected cols: TableTemplateColumn[] = [];
  protected search: (
    criteria: SearchCriteriaDto<ActivityCriteria>
  ) => Observable<SearchResultDto<EmissionActivityView>>;
  protected criteria: ActivityCriteria = {};
  protected readonly searchEvent: EventEmitter<void> = new EventEmitter();

  @ViewChild('activityColActions', {static: true})
  protected activityColActions!: TemplateRef<any>;
  @ViewChild('buildingTemplate', {static: true})
  protected buildingTemplate!: TemplateRef<any>;
  @ViewChild('buildingGroupTemplate', {static: true})
  protected buildingGroupTemplate!: TemplateRef<any>;

  protected selectableBuildings: SelectableItem<UUID>[] = [];
  protected selectableFactors: SelectableItem<UUID>[] = [];

  protected filterFormGroup = inject(FormBuilder).group({
    name: new FormControl<null | string>(null),
    category: new FormControl<null | string>(null),
    buildings: new FormControl<UUID[]>([], {nonNullable: true}),
    factors: new FormControl<UUID[]>([], {nonNullable: true})
  });

  constructor(
    protected readonly emissionActivityService: EmissionActivityService,
    private readonly modalProvider: ModalProvider,
    private readonly msgService: ToastProvider,
    private readonly translate: TranslateService,
    private readonly emissionActivityRecordService: EmissionActivityRecordService
  ) {
    super();
    this.search = this.emissionActivityService.search.bind(this.emissionActivityService);
  }

  ngOnInit(): void {
    this.buildCols();
    this.fetchFilterOptions();
    this.translate.onLangChange.pipe(takeUntil(this.destroy$)).subscribe(() => this.fetchFilterOptions());
  }

  onResetFilters(): void {
    this.filterFormGroup.reset();
    this.onFilter();
  }

  onFilter(): void {
    this.criteria.name = this.filterFormGroup.controls.name.value || undefined;
    this.criteria.category = this.filterFormGroup.controls.category.value || undefined;
    this.criteria.factors = this.filterFormGroup.controls.factors.value.map(factor => factor.toString());
    this.criteria.buildings = this.filterFormGroup.controls.buildings.value.map(building => building.toString());
    this.searchEvent.emit();
  }

  openNewActivityDialog(): void {
    this.modalProvider
      .openDynamicDialog(NewActivityDialogComponent, {
        selectableBuildings: this.selectableBuildings
      })
      .pipe(takeUntil(this.destroy$))
      .subscribe(rs => {
        if (rs) {
          this.searchEvent.emit();
        }
      });
  }

  openEditActivityDialog(rowData: EmissionActivityView): void {
    this.modalProvider
      .openDynamicDialog(NewActivityDialogComponent, {
        selectableBuildings: this.selectableBuildings,
        id: rowData.id,
        version: rowData.version,
        buildingId: rowData.building?.id,
        buildingGroupId: rowData.buildingGroup?.id,
        emissionFactorID: rowData.emissionFactor?.id,
        emissionSourceID: rowData.emissionFactor?.emissionSourceDTO?.id,
        name: rowData.name,
        type: rowData.type,
        category: rowData.category,
        description: rowData.description
      })
      .pipe(takeUntil(this.destroy$))
      .subscribe(rs => {
        if (rs) {
          this.searchEvent.emit();
        }
      });
  }

  onDeleteEmissionActivity(activityId: UUID): void {
    this.modalProvider
      .showDefaultConfirm()
      .pipe(
        takeUntil(this.destroy$),
        switchMap((result: boolean) => {
          if (!result) {
            return of(null); // Skip deletion if modal is cancelled
          }
          return this.emissionActivityService.deleteActivities([activityId]).pipe(
            switchMap(() => {
              this.msgService.success({
                summary: this.translate.instant('common.success')
              });
              this.searchEvent.emit();
              return of(null); // Complete the stream
            })
          );
        })
      )
      .subscribe();
  }

  onNewEmissionRecord(rowData: EmissionActivityView): void {
    this.emissionActivityService
      .getRecordedDateRanges(rowData.id as UUID)
      .pipe(
        switchMap(recordedDateRanges =>
          this.modalProvider.openDynamicDialog(NewActivityRecordDialogComponent, {
            activityId: rowData.id,
            factor: rowData.emissionFactor,
            recordedDateRanges
          })
        ),
        takeUntil(this.destroy$)
      )
      .subscribe(result => {
        if (result) {
          this.searchEvent.emit();
        }
      });
  }

  onDeleteEmissionRecord(recordId: UUID): void {
    this.modalProvider
      .showDefaultConfirm()
      .pipe(
        takeUntil(this.destroy$),
        switchMap((result: boolean) => {
          if (!result) {
            return of(null);
          }
          return this.emissionActivityRecordService.deleteRecords([recordId]).pipe(
            switchMap(() => {
              this.msgService.success({
                summary: this.translate.instant('common.success')
              });
              this.searchEvent.emit();
              return of(null);
            })
          );
        })
      )
      .subscribe();
  }

  openEditRecordDialog(activity: EmissionActivityView, record: EmissionActivityRecordView): void {
    this.emissionActivityService
      .getRecordedDateRanges(activity.id as UUID, record.id as UUID)
      .pipe(
        switchMap(recordedDateRanges =>
          this.modalProvider.openDynamicDialog(NewActivityRecordDialogComponent, {
            activityId: activity.id,
            factor: activity.emissionFactor,
            editRecord: record,
            recordedDateRanges
          })
        ),
        takeUntil(this.destroy$)
      )
      .subscribe(result => {
        if (result) {
          this.searchEvent.emit();
        }
      });
  }

  private fetchFilterOptions(): void {
    this.emissionActivityService
      .getSelectableBuildings()
      .pipe(takeUntil(this.destroy$))
      .subscribe(selectableBuildings => (this.selectableBuildings = selectableBuildings));
    this.emissionActivityService
      .getSelectableFactors(this.translate.currentLang)
      .pipe(takeUntil(this.destroy$))
      .subscribe(selectableFactors => (this.selectableFactors = selectableFactors));
  }

  private buildCols(): void {
    this.cols.push({
      field: 'name',
      header: 'emissions.activities.table.header.name',
      sortable: true
    });
    this.cols.push({
      field: 'building',
      header: 'emissions.activities.table.header.building',
      templateRef: this.buildingTemplate
    });
    this.cols.push({
      field: 'buildingGroup',
      header: 'emissions.activities.table.header.buildingGroup',
      templateRef: this.buildingGroupTemplate
    });
    this.cols.push({
      field: 'type',
      header: 'emissions.activities.table.header.type'
    });
    this.cols.push({
      field: 'category',
      header: 'emissions.activities.table.header.category',
      sortable: true
    });
    this.cols.push({
      field: 'id',
      header: '',
      templateRef: this.activityColActions
    });
  }
}
