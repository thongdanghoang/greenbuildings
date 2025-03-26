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
import {GeocodingService} from '../../../../services/geocoding.service';
import {AbstractFormComponent} from '../../../shared/components/form/abstract-form-component';
import {BuildingDetails, UserByBuilding} from '../../models/enterprise.dto';
import {MapLocation} from '../buildings/buildings.component';
import {TableTemplateColumn} from '../../../shared/components/table-template/table-template.component';
import {ApplicationService} from '../../../core/services/application.service';
import {
  SearchCriteriaDto,
  SearchResultDto
} from '../../../shared/models/base-models';

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
    latitude: new FormControl<number | null>(null, [
      Validators.required,
      Validators.min(-90),
      Validators.max(90)
    ]),
    longitude: new FormControl<number | null>(null, [
      Validators.required,
      Validators.min(-180),
      Validators.max(180)
    ]),
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
    private readonly geocodingService: GeocodingService,
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
    this.handleLocationChange();
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

  private handleLocationChange(): void {
    this.activatedRoute.queryParams
      .pipe(
        takeUntil(this.destroy$),
        filter((params): params is MapLocation => !!params),
        switchMap(location => {
          if (!!location.latitude && !!location.longitude) {
            this.buildingDetailsStructure.latitude.setValue(location.latitude);
            this.buildingDetailsStructure.longitude.setValue(
              location.longitude
            );
            return this.geocodingService.reverse(
              location.latitude,
              location.longitude
            );
          } else if (!this.isEdit) {
            // only check when creating a new building
            this.notificationService.add({
              severity: 'error',
              summary: 'Error',
              detail: 'Invalid location'
            });
            return [];
          }
          return [];
        })
      )
      .subscribe(address => {
        if (address) {
          this.buildingDetailsStructure.address.setValue(address.displayName);
        }
      });
  }
}
