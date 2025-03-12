import {
  Component,
  EventEmitter,
  OnInit,
  TemplateRef,
  ViewChild,
  inject
} from '@angular/core';
import {ApplicationService} from '../../../core/services/application.service';
import {PackageCreditService} from '../../services/package-credit.service';
import {TableTemplateColumn} from '../../../shared/components/table-template/table-template.component';
import {
  SearchCriteriaDto,
  SearchResultDto,
  SortDto
} from '../../../shared/models/base-models';
import {Observable, takeUntil} from 'rxjs';

import {CreditPackage} from '../../../enterprise/models/enterprise.dto';
import {AppRoutingConstants} from '../../../../app-routing.constant';
import {Router} from '@angular/router';
import {SubscriptionAwareComponent} from '../../../core/subscription-aware.component';
import {UUID} from '../../../../../types/uuid';
import {MessageService} from 'primeng/api';
import {ModalProvider} from '../../../shared/services/modal-provider';
import {TranslateService} from '@ngx-translate/core';

@Component({
  selector: 'app-package-credit',
  templateUrl: './package-credit.component.html',
  styleUrl: './package-credit.component.css'
})
export class PackageCreditComponent
  extends SubscriptionAwareComponent
  implements OnInit
{
  @ViewChild('priceTemplate', {static: true})
  priceTemplate!: TemplateRef<any>;
  @ViewChild('actionsTemplate', {static: true})
  actionsTemplate!: TemplateRef<any>;
  cols: TableTemplateColumn[] = [];
  triggerSearch: EventEmitter<void> = new EventEmitter();
  sort!: SortDto;
  protected fetchData!: (
    criteria: SearchCriteriaDto<void>
  ) => Observable<SearchResultDto<CreditPackage>>;
  protected readonly AppRoutingConstants = AppRoutingConstants;
  protected selected: CreditPackage[] = [];
  private readonly router = inject(Router);
  constructor(
    protected readonly applicationService: ApplicationService,
    private readonly packageCreditService: PackageCreditService,
    private readonly messageService: MessageService,
    private readonly modalProvider: ModalProvider,
    private readonly translate: TranslateService
  ) {
    super();
  }

  ngOnInit(): void {
    this.sort = {
      field: 'numberOfCredits',
      direction: 'DESC'
    };
    this.buildCols();
    this.fetchData = this.packageCreditService.getCreditPackages.bind(
      this.packageCreditService
    );
  }

  buildCols(): void {
    this.cols.push({
      field: 'numberOfCredits',
      header: 'admin.packageCredit.table.numberCredit',
      sortable: true
    });
    this.cols.push({
      field: 'price',
      header: 'admin.packageCredit.table.price',
      sortable: true,
      templateRef: this.priceTemplate
    });
    this.cols.push({
      field: 'actions',
      header: '',
      templateRef: this.actionsTemplate
    });
  }
  onSelectionChange(selectedPackages: CreditPackage[]): void {
    this.selected = selectedPackages;
  }

  onDelete(rowData: CreditPackage): void {
    this.selected = [rowData];
    this.confirmDelete();
  }

  onEdit(rowData: CreditPackage): void {
    this.selected = [rowData];
    const pkgId = this.selected[0].id; // Retrieve the selected user's ID.
    void this.router.navigate([
      `/${AppRoutingConstants.ADMIN_PATH}/${AppRoutingConstants.PACKAGE_CREDIT_DETAILS_PATH}`,
      pkgId
    ]);
  }

  confirmDelete(): void {
    this.modalProvider
      .showConfirm({
        message: this.translate.instant('common.defaultConfirmMessage'),
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
          this.deletePackages();
        }
      });
  }

  private deletePackages(): void {
    const pkgIds: UUID[] = this.selected.map(pkg => pkg.id);

    this.packageCreditService.deletePackages(pkgIds).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: this.translate.instant(
            'admin.packageCredit.message.success.summary'
          ),
          detail: this.translate.instant(
            'admin.packageCredit.message.success.detail'
          )
        });
        this.selected = []; // Clear local selection
        this.triggerSearch.emit(); // Refresh table
      },
      error: () => {
        this.messageService.add({
          severity: 'error',
          summary: this.translate.instant(
            'admin.packageCredit.message.error.summary'
          ),
          detail: this.translate.instant(
            'admin.packageCredit.message.error.detail'
          )
        });
      }
    });
  }
}
