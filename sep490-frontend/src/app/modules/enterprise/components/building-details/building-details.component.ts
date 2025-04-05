import {Location} from '@angular/common';
import {HttpClient} from '@angular/common/http';
import {Component, EventEmitter, TemplateRef, ViewChild} from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormControl,
  Validators
} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {MessageService} from 'primeng/api';
import {Observable, filter, map, switchMap, takeUntil, tap} from 'rxjs';
import {validate} from 'uuid';
import {UUID} from '../../../../../types/uuid';
import {AppRoutingConstants} from '../../../../app-routing.constant';
import {BuildingService} from '../../../../services/building.service';
import {ApplicationService} from '../../../core/services/application.service';
import {AbstractFormComponent} from '../../../shared/components/form/abstract-form-component';
import {TableTemplateColumn} from '../../../shared/components/table-template/table-template.component';
import {
  SearchCriteriaDto,
  SearchResultDto
} from '../../../shared/models/base-models';
import {BuildingDetails, UserByBuilding} from '../../models/enterprise.dto';

@Component({
  selector: 'app-building-detail',
  templateUrl: './building-details.component.html',
  styleUrl: './building-details.component.css'
})
export class BuildingDetailsComponent extends AbstractFormComponent<BuildingDetails> {
  @ViewChild('permissionTemplate', {static: true})
  permissionTemplate!: TemplateRef<any>;
  cols: TableTemplateColumn[] = [];
  triggerSearch: EventEmitter<void> = new EventEmitter();
  protected buildingId!: UUID;
  protected fetchData!: (
    criteria: SearchCriteriaDto<void>
  ) => Observable<SearchResultDto<UserByBuilding>>;
  protected readonly buildingDetailsStructure = {
    id: new FormControl(''),
    version: new FormControl(null),
    name: new FormControl(null, Validators.required),
    numberOfDevices: new FormControl(0, {
      nonNullable: true,
      validators: [Validators.min(1), Validators.required]
    }),
    address: new FormControl<string | null>(null, Validators.required),
    subscriptionDTO: this.formBuilder.group({
      startDate: [{value: null, disabled: true}],
      endDate: [{value: null, disabled: true}],
      maxNumberOfDevices: [{value: null, disabled: true}]
    })
  };

  constructor(
    httpClient: HttpClient,
    formBuilder: FormBuilder,
    notificationService: MessageService,
    translate: TranslateService,
    private readonly activatedRoute: ActivatedRoute,
    private readonly buildingService: BuildingService,
    private readonly location: Location,
    private readonly router: Router,
    protected readonly applicationService: ApplicationService
  ) {
    super(httpClient, formBuilder, notificationService, translate);
  }

  buildCols(): void {
    this.cols.push({
      field: 'name',
      header: 'enterprise.Users.table.name'
    });
    this.cols.push({
      field: 'role',
      header: 'enterprise.permissionInBuilding',
      templateRef: this.permissionTemplate
    });
  }

  back(): void {
    this.location.back();
  }

  get isEdit(): boolean {
    return !!this.buildingDetailsStructure.id.value;
  }

  get startDateControl(): FormControl {
    return this.formGroup.get('subscriptionDTO.startDate') as FormControl;
  }

  get endDateControl(): FormControl {
    return this.formGroup.get('subscriptionDTO.endDate') as FormControl;
  }

  get maxNumberOfDevicesControl(): FormControl {
    return this.formGroup.get(
      'subscriptionDTO.maxNumberOfDevices'
    ) as FormControl;
  }

  get hasSubscription(): boolean {
    const startDate = this.startDateControl.value;
    const endDate = this.endDateControl.value;
    const maxNumberOfDevices = this.maxNumberOfDevicesControl.value;

    return (
      startDate !== null || endDate !== null || maxNumberOfDevices !== null
    );
  }

  override reset(): void {
    if (this.isEdit) {
      return this.initializeData();
    }
    super.reset();
  }

  protected initializeData(): void {
    this.fetchBuildingDetails();
    this.fetchData = (
      criteria: SearchCriteriaDto<void>
    ): Observable<SearchResultDto<UserByBuilding>> => {
      return this.buildingService.searchUserByBuilding(
        criteria,
        this.buildingId
      );
    };
    this.buildCols();
  }

  protected initializeFormControls(): {[p: string]: AbstractControl} {
    return this.buildingDetailsStructure;
  }

  protected onSubmitFormDataSuccess(): void {
    void this.router.navigate([
      '/',
      AppRoutingConstants.ENTERPRISE_PATH,
      AppRoutingConstants.BUILDING_PATH
    ]);
  }

  protected submitFormDataUrl(): string {
    return this.buildingService.createBuildingUrl;
  }

  private fetchBuildingDetails(): void {
    this.activatedRoute.paramMap
      .pipe(
        takeUntil(this.destroy$),
        map(params => params.get('id')),
        filter((idParam): idParam is string => !!idParam),
        filter(id => validate(id)),
        tap(id => {
          this.buildingDetailsStructure.id.setValue(id);
          this.buildingId = id as UUID;
        }),
        switchMap(id => this.buildingService.getBuildingDetails(id as UUID))
      )
      .subscribe(building => {
        this.formGroup.patchValue(building);
      });
  }
}
