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
import L from 'leaflet';
import {
  Observable,
  debounceTime,
  distinctUntilChanged,
  filter,
  map,
  switchMap,
  takeUntil,
  tap
} from 'rxjs';
import {validate} from 'uuid';
import {UUID} from '../../../../../types/uuid';
import {AppRoutingConstants} from '../../../../app-routing.constant';
import {BuildingDetails, UserByBuilding} from '@models/enterprise';
import {BuildingService} from '@services/building.service';
import {GeocodingService} from '@services/geocoding.service';
import {ApplicationService} from '../../../core/services/application.service';
import {AbstractFormComponent} from '@shared/components/form/abstract-form-component';
import {TableTemplateColumn} from '@shared/components/table-template/table-template.component';
import {SearchCriteriaDto, SearchResultDto} from '@shared/models/base-models';
import {ToastProvider} from '@shared/services/toast-provider';

@Component({
  selector: 'app-building-detail',
  templateUrl: './building-details.component.html',
  styleUrl: './building-details.component.css'
})
export class BuildingDetailsComponent extends AbstractFormComponent<BuildingDetails> {
  addressSuggestions: any[] = [];
  showMap: boolean = false;
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
    latitude: new FormControl<number | null>(null),
    longitude: new FormControl<number | null>(null),
    address: new FormControl<string | null>(null, Validators.required),
    subscriptionDTO: this.formBuilder.group({
      startDate: [{value: null, disabled: true}],
      endDate: [{value: null, disabled: true}],
      maxNumberOfDevices: [{value: null, disabled: true}]
    })
  };
  private map!: L.Map;
  private marker!: L.Marker | null;
  private markers: L.Marker[] = [];

  constructor(
    httpClient: HttpClient,
    formBuilder: FormBuilder,
    notificationService: ToastProvider,
    translate: TranslateService,
    private readonly activatedRoute: ActivatedRoute,
    private readonly buildingService: BuildingService,
    private readonly geocodingService: GeocodingService,
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
    if (this.showMap) {
      this.showMap = false;
      if (this.map) {
        this.map.remove();
        this.map = null as any;
        this.marker = null;
        this.markers = [];
      }
    }
    setTimeout(() => {
      void this.router.navigate([
        '/',
        AppRoutingConstants.ENTERPRISE_PATH,
        AppRoutingConstants.BUILDING_PATH
      ]);
    }, 0); // Delay navigation để đảm bảo logic chạy xong
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
    this.formGroup.reset({
      numberOfDevices: 0 // Giữ giá trị mặc định
    });
    super.reset();
  }

  showMapView(): void {
    if (this.showMap) {
      // If map is currently shown, hide it
      this.showMap = false;
      if (this.map) {
        this.map.remove(); // Remove the map instance to clean up
        this.map = null as any; // Reset the map reference
        this.marker = null; // Reset the marker reference
        this.markers = []; // Clear the markers array
      }
    } else {
      // If map is not shown, display it
      this.showMap = true;
      setTimeout(() => {
        this.initMap();
        this.fetchBuildings(); // Fetch and display all buildings
      }, 0); // Delay to ensure DOM is updated
    }
  }

  onSubmit(): void {
    if (this.showMap) {
      // If map is currently shown, hide it
      this.showMap = false;
      if (this.map) {
        this.map.remove(); // Remove the map instance to clean up
        this.map = null as any; // Reset the map reference
        this.marker = null; // Reset the marker reference
        this.markers = []; // Clear the markers array
      }
    }
    this.submit();
  }

  // Xử lý autocomplete với p-autoComplete
  searchAddress(event: any): void {
    const query = event.query;
    if (query && query.length >= 2) {
      this.geocodingService
        .getPlaceAutocomplete(query, '21.013715429594125,105.79829597455202')
        .pipe(debounceTime(300), distinctUntilChanged())
        .subscribe(response => {
          this.addressSuggestions = response.predictions || [];
        });
    } else {
      this.addressSuggestions = [];
    }
  }

  onSelectAddress(event: any): void {
    const suggestion = event.value;
    this.formGroup.patchValue({address: suggestion.description});
    // Gọi API chi tiết để lấy tọa độ
    this.geocodingService
      .getPlaceDetail(suggestion.place_id)
      .subscribe(detail => {
        this.formGroup.patchValue({
          latitude: detail.result.geometry.location.lat,
          longitude: detail.result.geometry.location.lng
        });
      });
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

  private initMap(): void {
    if (document.getElementById('building-details-map')) {
      this.map = L.map('building-details-map', {
        center: [10.841394, 106.810052], // Vị trí mặc định
        zoom: 16
      });

      const tiles = L.tileLayer(
        'https://{s}.google.com/vt/lyrs=m&x={x}&y={y}&z={z}',
        {
          maxZoom: 18,
          minZoom: 2,
          subdomains: ['mt0', 'mt1', 'mt2', 'mt3']
        }
      );
      tiles.addTo(this.map);

      // Thêm sự kiện click để chọn vị trí
      this.map.on('click', e => {
        if (this.marker) {
          this.map.removeLayer(this.marker);
        }
        this.marker = L.marker([e.latlng.lat, e.latlng.lng]).addTo(this.map);
        this.formGroup.patchValue({
          latitude: e.latlng.lat,
          longitude: e.latlng.lng
        });
        this.updateAddressFromCoordinates(e.latlng.lat, e.latlng.lng);
      });
    } else {
      throw new Error(
        'Map element not found, should set id="map" to the map element'
      );
    }
  }

  // eslint-disable-next-line max-lines-per-function
  private fetchBuildings(): void {
    this.buildingService
      .searchBuildings({
        page: {
          pageNumber: 0,
          pageSize: 100
        }
      })
      .pipe(takeUntil(this.destroy$))
      // eslint-disable-next-line max-lines-per-function
      .subscribe(buildings => {
        // Xóa tất cả marker cũ trước khi thêm mới
        this.markers.forEach(marker => this.map.removeLayer(marker));
        this.markers = [];

        // Thêm marker cho từng building (trừ building hiện tại nếu đang edit)
        buildings.results.forEach(building => {
          if (this.isEdit && building.id === this.buildingId) {
            return; // Bỏ qua building hiện tại khi edit
          }
          const marker = L.marker([building.latitude, building.longitude]);
          marker.addTo(this.map);
          this.markers.push(marker); // Lưu marker vào danh sách
          marker.bindPopup(`<b>${building.name}</b><br>${building.address}`);
        });

        // Điều chỉnh bản đồ để hiển thị tất cả marker
        if (buildings.results.length > 0) {
          const latLngs = buildings.results
            .filter(building => !this.isEdit || building.id !== this.buildingId)
            .map(building => L.latLng(building.latitude, building.longitude));
          if (latLngs.length > 0) {
            const bounds = L.latLngBounds(latLngs);
            this.map.fitBounds(bounds, {padding: [50, 50]});
          }
        }

        // Nếu đang edit, hiển thị marker của building hiện tại
        if (
          this.isEdit &&
          this.formGroup.get('latitude')?.value &&
          this.formGroup.get('longitude')?.value
        ) {
          this.marker = L.marker([
            this.formGroup.get('latitude')?.value,
            this.formGroup.get('longitude')?.value
          ]).addTo(this.map);
          this.map.setView(
            [
              this.formGroup.get('latitude')?.value,
              this.formGroup.get('longitude')?.value
            ],
            16
          );
        }
      });
  }

  private updateAddressFromCoordinates(lat: number, lng: number): void {
    this.geocodingService.reverse(lat, lng).subscribe(address => {
      if (address) {
        this.showMap = false;
        this.buildingDetailsStructure.address.setValue(address.displayName);
      }
    });
  }
}
