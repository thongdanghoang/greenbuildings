import {Component, ComponentRef, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import * as L from 'leaflet';
import {
  DialogService,
  DynamicDialogConfig,
  DynamicDialogRef
} from 'primeng/dynamicdialog';

import {forkJoin, takeUntil} from 'rxjs';
import {UUID} from '../../../../../types/uuid';
import {AppRoutingConstants} from '../../../../app-routing.constant';
import {Building, BuildingDetails, TransactionType} from '@models/enterprise';
import {BuildingService} from '@services/building.service';
import {WalletService} from '@services/wallet.service';
import {SubscriptionAwareComponent} from '@shared/directives/subscription-aware.component';
import {ModalProvider} from '@shared/services/modal-provider';
import {ToastProvider} from '@shared/services/toast-provider';
import {
  BuildingSubscriptionDialogComponent,
  SubscriptionDialogOptions
} from '../../dialog/building-subcription-dialog/building-subscription-dialog.component';
import {CreditDeductionHistoryDialogComponent} from '../../dialog/credit-deduction-history-dialog/credit-deduction-history-dialog.component';
import {PopupService} from '../../services/popup.service';
import {BuildingPopupMarkerComponent} from '../building-popup-marker/building-popup-marker.component';

const iconRetinaUrl = 'assets/marker-icon-2x.png';
const iconUrl = 'assets/marker-icon.png';
const shadowUrl = 'assets/marker-shadow.png';
const iconDefault = L.icon({
  iconRetinaUrl,
  iconUrl,
  shadowUrl,
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  tooltipAnchor: [16, -28],
  shadowSize: [41, 41]
});
L.Marker.prototype.options.icon = iconDefault;

export enum ViewMode {
  LIST = 'list',
  MAP = 'map'
}

export interface MapLocation {
  latitude: number;
  longitude: number;
}

@Component({
  selector: 'app-building',
  templateUrl: './buildings.component.html',
  styleUrl: './buildings.component.css'
})
export class BuildingsComponent
  extends SubscriptionAwareComponent
  implements OnInit
{
  balance: number = 0;
  selectedBuildingDetails: BuildingDetails | null = null;
  ref: DynamicDialogRef | undefined;
  addBuildingLocation: boolean = false;
  buildingLocation: MapLocation | null = null;
  viewMode: ViewMode = ViewMode.LIST;
  buildings: Building[] = [];

  justifyOptions: any[] = [
    {icon: 'pi pi-map', value: ViewMode.MAP},
    {icon: 'pi pi-list', value: ViewMode.LIST}
  ];

  protected readonly AppRoutingConstants = AppRoutingConstants;

  private map!: L.Map;

  constructor(
    private readonly router: Router,
    private readonly buildingService: BuildingService,
    private readonly popupService: PopupService,
    public dialogService: DialogService,
    private readonly walletService: WalletService,
    private readonly messageService: ToastProvider,
    private readonly modalProvider: ModalProvider,
    private readonly translate: TranslateService
  ) {
    super();
  }

  ngOnInit(): void {
    this.initMap();
    this.fetchBuilding();
    this.map.on('click', e => {
      const marker = L.marker([e.latlng.lat, e.latlng.lng]);
      if (this.addBuildingLocation && this.buildingLocation === null) {
        marker.addTo(this.map);
        this.buildingLocation = {
          latitude: e.latlng.lat,
          longitude: e.latlng.lng
        };
      }
    });
  }

  get mapView(): boolean {
    return this.viewMode === ViewMode.MAP;
  }

  get listView(): boolean {
    return this.viewMode === ViewMode.LIST;
  }

  hasSubscription(building: Building): boolean {
    return building?.subscriptionDTO !== null;
  }

  getManageBuildingPath(building: Building): string {
    return `/${AppRoutingConstants.ENTERPRISE_PATH}/${AppRoutingConstants.BUILDING_MANAGEMENT_PATH}/${building?.id}`;
  }

  openDialog(building: Building): void {
    forkJoin({
      details: this.buildingService.getBuildingDetails(building.id),
      balance: this.walletService.getWalletBalance()
    }).subscribe(({details, balance}) => {
      // Update your component properties with the fetched data
      this.selectedBuildingDetails = details;
      this.balance = balance;
      const transactionType = building.subscriptionDTO
        ? TransactionType.UPGRADE
        : TransactionType.NEW_PURCHASE;
      const dialogConfig: DynamicDialogConfig<SubscriptionDialogOptions> = {
        width: '50rem',
        modal: true,
        data: {
          selectedBuildingDetails: this.selectedBuildingDetails,
          balance: this.balance,
          type: transactionType,
          building
        }
      };
      this.ref = this.dialogService.open(
        BuildingSubscriptionDialogComponent,
        dialogConfig
      );

      this.ref.onClose.subscribe(result => {
        if (result === 'buy') {
          this.fetchBuilding();
        }
      });
    });
  }

  onViewModeChanged(): void {
    this.fetchBuilding();
  }

  navigateToCreateBuilding(): void {
    void this.router.navigate([
      '/',
      AppRoutingConstants.ENTERPRISE_PATH,
      AppRoutingConstants.BUILDING_PATH,
      'create'
    ]);
  }

  addBuilding(): void {
    if (this.buildingLocation) {
      void this.router.navigate(
        [
          '/',
          AppRoutingConstants.ENTERPRISE_PATH,
          AppRoutingConstants.BUILDING_PATH,
          'create'
        ],
        {
          queryParams: this.buildingLocation
        }
      );
    }
  }

  confirmDelete(buildingId: UUID): void {
    this.modalProvider
      .showConfirm({
        message: this.translate.instant('common.buildingConfirmMessage'),
        header: this.translate.instant('common.confirmHeader'),
        icon: 'pi pi-info-circle',
        acceptButtonStyleClass: 'p-button-danger p-button-text min-w-20',
        rejectButtonStyleClass: 'p-button-contrast p-button-text min-w-20',
        acceptIcon: 'none',
        acceptLabel: this.translate.instant('common.accept'),
        rejectIcon: 'none',
        rejectLabel: this.translate.instant('common.reject')
      })
      .pipe(takeUntil(this.destroy$))
      .subscribe((result: boolean): void => {
        if (result) {
          this.deleteBuilding(buildingId);
        }
      });
  }

  cancelAddBuilding(): void {
    this.addBuildingLocation = false;
    this.map.eachLayer(layer => {
      if (
        layer instanceof L.Marker &&
        layer.getLatLng().lat === this.buildingLocation?.latitude &&
        layer.getLatLng().lng === this.buildingLocation?.longitude
      ) {
        this.map.removeLayer(layer);
      }
    });
    this.buildingLocation = null;
  }

  viewBuildingDetails(id: UUID): void {
    void this.router.navigate([
      '/',
      AppRoutingConstants.ENTERPRISE_PATH,
      AppRoutingConstants.BUILDING_PATH,
      id
    ]);
  }

  fetchBuilding(): void {
    this.buildingService
      .searchBuildings({
        page: {
          pageNumber: 0,
          pageSize: 100
        }
      })
      .pipe(takeUntil(this.destroy$))
      .subscribe(buildings => {
        this.buildings = buildings.results;
        buildings.results.forEach(building => {
          const marker = L.marker([building.latitude, building.longitude]);
          marker.addTo(this.map);
          const markerPopup: HTMLDivElement = this.popupService.compilePopup(
            BuildingPopupMarkerComponent,
            (c: ComponentRef<BuildingPopupMarkerComponent>): void => {
              c.instance.building = building;
            }
          );
          marker.bindPopup(markerPopup);
        });
        if (this.buildings.length > 0) {
          const latLngs = buildings.results.map(building =>
            L.latLng(building.latitude, building.longitude)
          );
          const bounds = L.latLngBounds(latLngs);
          this.map.fitBounds(bounds);
        }
      });
  }

  zoomTo(latitude: number, longitude: number): void {
    if (this.map) {
      this.viewMode = ViewMode.MAP;
      this.map.setView([latitude, longitude], 16);
    }
  }

  private initMap(): void {
    if (document.getElementById('map')) {
      this.map = L.map('map', {
        center: [10.841394, 106.810052],
        zoom: 16
      });
    } else {
      throw new Error(
        'Map element not found, should set id="map" to the map element'
      );
    }

    const tiles = L.tileLayer(
      'https://{s}.google.com/vt/lyrs=m&x={x}&y={y}&z={z}',
      {
        maxZoom: 18,
        minZoom: 2,
        subdomains: ['mt0', 'mt1', 'mt2', 'mt3']
      }
    );

    tiles.addTo(this.map);
  }

  // eslint-disable-next-line @typescript-eslint/member-ordering
  protected viewHistoryUseCredit(buildingId: UUID): void {
    const dialogConfig: DynamicDialogConfig<UUID> = {
      closeOnEscape: true,
      dismissableMask: true,
      showHeader: false,
      data: buildingId // Truyền buildingId vào data
    };

    this.ref = this.dialogService.open(
      CreditDeductionHistoryDialogComponent,
      dialogConfig
    );
  }

  private deleteBuilding(buildingId: UUID): void {
    this.buildingService.deleteBuildingById(buildingId).subscribe({
      next: () => {
        this.messageService.success({
          summary: this.translate.instant(
            'enterprise.buildings.message.success.summary'
          ),
          detail: this.translate.instant(
            'enterprise.buildings.message.success.detail'
          )
        });
        this.fetchBuilding(); // Refresh the buildings list
      },
      error: () => {
        this.messageService.businessError({
          summary: this.translate.instant(
            'enterprise.buildings.message.error.summary'
          ),
          detail: this.translate.instant(
            'enterprise.buildings.message.error.detail'
          )
        });
      }
    });
  }
}
