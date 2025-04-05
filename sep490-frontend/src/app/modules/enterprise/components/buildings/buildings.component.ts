import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {MessageService} from 'primeng/api';
import {
  DialogService,
  DynamicDialogConfig,
  DynamicDialogRef
} from 'primeng/dynamicdialog';

import {forkJoin, takeUntil, tap} from 'rxjs';
import {UUID} from '../../../../../types/uuid';
import {AppRoutingConstants} from '../../../../app-routing.constant';
import {BuildingService} from '../../../../services/building.service';
import {SubscriptionAwareComponent} from '../../../core/subscription-aware.component';
import {ModalProvider} from '../../../shared/services/modal-provider';
import {
  BuildingSubscriptionDialogComponent,
  SubscriptionDialogOptions
} from '../../dialog/building-subcription-dialog/building-subscription-dialog.component';
import {
  Building,
  BuildingDetails,
  TransactionType
} from '../../models/enterprise.dto';
import {WalletService} from '../../services/wallet.service';

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
  buildings: Building[] = [];

  protected readonly AppRoutingConstants = AppRoutingConstants;

  constructor(
    private readonly router: Router,
    private readonly buildingService: BuildingService,
    public dialogService: DialogService,
    private readonly walletService: WalletService,
    private readonly messageService: MessageService,
    private readonly modalProvider: ModalProvider,
    private readonly translate: TranslateService
  ) {
    super();
  }

  ngOnInit(): void {
    this.fetchBuilding();
  }

  hasSubscription(building: Building): boolean {
    return building?.subscriptionDTO !== null;
  }

  getEmissionActivityPath(building: Building): string {
    return `/${AppRoutingConstants.ENTERPRISE_PATH}/${AppRoutingConstants.EMISSION_ACTIVITY_PATH}/${building?.id}`;
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

  addBuilding(): void {
    void this.router.navigate([
      '/',
      AppRoutingConstants.ENTERPRISE_PATH,
      AppRoutingConstants.BUILDING_PATH,
      'new'
    ]);
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
      .pipe(
        takeUntil(this.destroy$),
        tap(searchResult => (this.buildings = searchResult.results))
      )
      .subscribe();
  }

  private deleteBuilding(buildingId: UUID): void {
    this.buildingService.deleteBuildingById(buildingId).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
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
        this.messageService.add({
          severity: 'error',
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
