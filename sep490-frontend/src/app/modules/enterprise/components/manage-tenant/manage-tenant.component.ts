import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {DialogService} from 'primeng/dynamicdialog';
import {UUID} from '../../../../../types/uuid';
import {AppRoutingConstants} from '../../../../app-routing.constant';
import {BuildingService} from '../../../../services/building.service';
import {SubscriptionAwareComponent} from '../../../core/subscription-aware.component';
import {ModalProvider} from '../../../shared/services/modal-provider';
import {ToastProvider} from '../../../shared/services/toast-provider';
import {BuildingGroup} from '../../models/enterprise.dto';
import {PopupService} from '../../services/popup.service';
import {WalletService} from '../../services/wallet.service';
import {BuildingGroupService} from '../../services/building-group.service';

@Component({
  selector: 'app-manage-tenant',
  templateUrl: './manage-tenant.component.html',
  styleUrl: './manage-tenant.component.css'
})
export class ManageTenantComponent
  extends SubscriptionAwareComponent
  implements OnInit
{
  buildingGroups: BuildingGroup[] = [];

  constructor(
    private readonly router: Router,
    private readonly buildingService: BuildingService,
    private readonly popupService: PopupService,
    public dialogService: DialogService,
    private readonly walletService: WalletService,
    private readonly messageService: ToastProvider,
    private readonly modalProvider: ModalProvider,
    private readonly translate: TranslateService,
    private readonly buildingGroupService: BuildingGroupService
  ) {
    super();
  }

  ngOnInit(): void {
    this.fetchBuildingGroups();
  }

  fetchBuildingGroups(): void {
    this.buildingGroupService.getByTenant().subscribe(rs => {
      this.buildingGroups = rs;
    });
  }

  goToDetails(groupId: UUID): void {
    void this.router.navigate([
      AppRoutingConstants.ENTERPRISE_PATH,
      AppRoutingConstants.BUILDING_GROUP_PATH,
      groupId
    ]);
  }
}
