import {
  Component,
  EventEmitter,
  OnInit,
  TemplateRef,
  ViewChild,
  inject
} from '@angular/core';
import {Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {Observable} from 'rxjs';
import {UUID} from '../../../../../types/uuid';
import {AppRoutingConstants} from '../../../../app-routing.constant';
import {CreditPackageAdmin, CreditPackageVersion} from '@models/enterprise';
import {ApplicationService} from '@services/application.service';
import {SubscriptionAwareComponent} from '@shared/directives/subscription-aware.component';
import {TableTemplateColumn} from '@shared/components/table-template/table-template.component';
import {
  SearchCriteriaDto,
  SearchResultDto,
  SortDto
} from '@shared/models/base-models';
import {ToastProvider} from '@shared/services/toast-provider';
import {PackageCreditService} from '@services/package-credit.service';

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
  @ViewChild('activeTemplate', {static: true})
  activeTemplate!: TemplateRef<any>;
  @ViewChild('viewVersionsTemplate', {static: true})
  viewVersionsTemplate!: TemplateRef<any>;
  cols: TableTemplateColumn[] = [];
  triggerSearch: EventEmitter<void> = new EventEmitter();
  sort!: SortDto;
  selectedPackageVersions: CreditPackageVersion[] = [];
  isDialogVisible = false;
  protected fetchData!: (
    criteria: SearchCriteriaDto<void>
  ) => Observable<SearchResultDto<CreditPackageAdmin>>;
  protected readonly AppRoutingConstants = AppRoutingConstants;
  protected selected: CreditPackageAdmin[] = [];
  private readonly router = inject(Router);
  constructor(
    protected readonly applicationService: ApplicationService,
    private readonly packageCreditService: PackageCreditService,
    private readonly messageService: ToastProvider,
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
      header: 'admin.packageCredit.table.numberCredit'
    });
    this.cols.push({
      field: 'price',
      header: 'admin.packageCredit.table.price',
      templateRef: this.priceTemplate
    });
    this.cols.push({
      field: 'viewVersions',
      header: '',
      templateRef: this.viewVersionsTemplate
    });
    this.cols.push({
      field: 'actions',
      header: '',
      templateRef: this.actionsTemplate
    });
    this.cols.push({
      field: 'active',
      header: '',
      templateRef: this.activeTemplate
    });
  }
  onSelectionChange(selectedPackages: CreditPackageAdmin[]): void {
    this.selected = selectedPackages;
  }

  onDelete(rowData: CreditPackageAdmin): void {
    this.selected = [rowData];
    this.deletePackages();
  }

  onEdit(rowData: CreditPackageAdmin): void {
    this.selected = [rowData];
    const pkgId = this.selected[0].id; // Retrieve the selected user's ID.
    void this.router.navigate([
      `/${AppRoutingConstants.ADMIN_PATH}/${AppRoutingConstants.PACKAGE_CREDIT_DETAILS_PATH}`,
      pkgId
    ]);
  }
  onViewVersions(pkg: CreditPackageAdmin): void {
    if (pkg.packageVersionDTOList && pkg.packageVersionDTOList.length > 0) {
      this.selectedPackageVersions = Array.from(pkg.packageVersionDTOList);
    } else {
      this.selectedPackageVersions = [];
    }
    this.isDialogVisible = true;
  }

  private deletePackages(): void {
    const pkgIds: UUID[] = this.selected.map(pkg => pkg.id);

    this.packageCreditService.deletePackages(pkgIds).subscribe({
      next: (): void => {
        this.messageService.success({
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
      error: (): void => {
        this.messageService.businessError({
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
