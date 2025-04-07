import {Component, OnInit} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {Confirmation} from 'primeng/api';
import {Nullable} from 'primeng/ts-helpers';
import {takeUntil} from 'rxjs';
import {SubscriptionAwareComponent} from '../../../core/subscription-aware.component';
import {ModalProvider} from '../../../shared/services/modal-provider';
import {CreditPackage} from '../../models/enterprise.dto';
import {CreditPackageService} from '../../services/credit-package.service';
import {PaymentService} from '../../services/payment.service';
import {WalletService} from '../../services/wallet.service';

@Component({
  selector: 'app-subscriptions',
  templateUrl: './plan.component.html',
  styleUrl: './plan.component.css'
})
export class PlanComponent
  extends SubscriptionAwareComponent
  implements OnInit
{
  tabs = [
    {title: 'purchaseCredit.about', content: 'Content 1', value: '0'},
    {title: 'purchaseCredit.termService', content: 'Content 2', value: '1'}
  ];
  creditPackages: CreditPackage[] = [];
  selectedPackage: Nullable<CreditPackage>;
  balance: number = 0;

  constructor(
    private readonly walletService: WalletService,
    private readonly creditPackageService: CreditPackageService,
    private readonly paymentService: PaymentService,
    private readonly modalProvider: ModalProvider,
    private readonly translate: TranslateService
  ) {
    super();
  }

  ngOnInit(): void {
    this.getBalance();
    this.getCreditPackages();
  }

  selectPackage(pkg: CreditPackage): void {
    if (this.selectedPackage?.id === pkg.id) {
      this.selectedPackage = null;
    } else {
      this.selectedPackage = pkg;
    }
  }

  onBuyCredit(): void {
    if (this.selectedPackage) {
      this.modalProvider
        .showConfirm(this.prepareDialogParams())
        .pipe(takeUntil(this.destroy$))
        .subscribe(rs => {
          if (rs) {
            this.confirmPurchase();
          }
        });
    }
  }

  prepareDialogParams(): Confirmation {
    const formattedPrice = new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND'
    }).format(this.selectedPackage!.price);

    return {
      message: this.translate.instant('payment.confirmMessage', {
        noCredits: this.selectedPackage!.numberOfCredits,
        price: formattedPrice
      }),
      header: this.translate.instant('common.confirmHeader'),
      icon: 'pi pi-info-circle',
      acceptButtonStyleClass: 'p-button-primary p-button-text min-w-20',
      rejectButtonStyleClass: 'p-button-contrast p-button-text min-w-20',
      acceptIcon: 'none',
      acceptLabel: this.translate.instant('common.accept'),
      rejectIcon: 'none',
      rejectLabel: this.translate.instant('common.reject')
    };
  }

  confirmPurchase(): void {
    this.paymentService
      .createPayment(this.selectedPackage!.id)
      .pipe(takeUntil(this.destroy$))
      .subscribe(rs => {
        window.location.href = rs.checkoutUrl;
      });
  }

  getBalance(): void {
    this.registerSubscription(
      this.walletService.getWalletBalance().subscribe(result => {
        this.balance = result;
      })
    );
  }

  getCreditPackages(): void {
    this.registerSubscription(
      this.creditPackageService.getAllCreditPackages().subscribe(rs => {
        this.creditPackages = rs;
      })
    );
  }
}
