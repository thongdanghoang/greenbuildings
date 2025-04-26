import {DecimalPipe} from '@angular/common';
import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {Confirmation} from 'primeng/api';
import {Nullable} from 'primeng/ts-helpers';
import {
  Observable,
  filter,
  forkJoin,
  map,
  of,
  switchMap,
  takeUntil
} from 'rxjs';
import {SubscriptionAwareComponent} from '../../../core/subscription-aware.component';
import {ModalProvider} from '../../../shared/services/modal-provider';
import {CreditPackage} from '../../models/enterprise.dto';
import {CreditPackageService} from '../../services/credit-package.service';
import {CurrencyConverterService} from '../../services/currency-converter.service';
import {PaymentService} from '../../services/payment.service';
import {WalletService} from '../../services/wallet.service';
import {DialogService} from 'primeng/dynamicdialog';
import {CreditPackageGuideDialogComponent} from '../../dialog/credit-package-guide-dialog/credit-package-guide-dialog.component';
import {ApplicationService} from '../../../core/services/application.service';

@Component({
  selector: 'app-subscriptions',
  templateUrl: './plan.component.html',
  styleUrl: './plan.component.css'
})
export class PlanComponent
  extends SubscriptionAwareComponent
  implements OnInit
{
  @ViewChild('aboutContent', {static: true}) aboutContent!: TemplateRef<any>;
  @ViewChild('termContent', {static: true}) termContent!: TemplateRef<any>;
  tabs = [
    {title: 'purchaseCredit.about', content: 'aboutContent', value: '0'},
    {title: 'purchaseCredit.termService', content: 'termContent', value: '1'}
  ];
  creditPackages: CreditPackage[] = [];
  selectedPackage: Nullable<CreditPackage>;
  balance: number = 0;

  constructor(
    private readonly walletService: WalletService,
    private readonly creditPackageService: CreditPackageService,
    private readonly paymentService: PaymentService,
    private readonly modalProvider: ModalProvider,
    private readonly translate: TranslateService,
    private readonly currencyConverterService: CurrencyConverterService,
    private readonly decimalPipe: DecimalPipe,
    private readonly dialogService: DialogService,
    private readonly applicationService: ApplicationService
  ) {
    super();
  }

  ngOnInit(): void {
    this.getBalance();
    this.getCreditPackages();
    this.translate.onLangChange
      .pipe(
        takeUntil(this.destroy$),
        filter(() => this.creditPackages.length > 0),
        switchMap(lang =>
          forkJoin(
            this.creditPackages.map(bundle =>
              this.currencyConverter(lang.lang, bundle).pipe(
                map(convertedPrice => {
                  bundle.convertedPriceCurrency = convertedPrice;
                  return bundle;
                })
              )
            )
          )
        )
      )
      .subscribe();
  }

  getContentTemplate(name: string): any {
    switch (name) {
      case 'aboutContent':
        return this.aboutContent;
      case 'termContent':
        return this.termContent;
      default:
        return null;
    }
  }

  showCreditPackageDialog(): void {
    this.dialogService.open(CreditPackageGuideDialogComponent, {
      closeOnEscape: true,
      dismissableMask: true,
      showHeader: false,
      data: {
        creditPackages: this.creditPackages
      }
    });
  }

  formatedCurrency(currency: number): string | null {
    const converted = this.decimalPipe.transform(
      currency,
      this.getDigitsInfo()
    );
    const symbol = this.translate.instant('currency.symbol');
    if (this.translate.currentLang === 'vi') {
      return `${converted} ${symbol}`;
    }
    return `${symbol} ${converted}`;
  }

  getDigitsInfo(): string {
    switch (this.translate.currentLang) {
      case 'en':
        return '1.2-2';
      case 'vi':
        return '1.0-0';
      case 'zh':
        return '1.2-2';
      default:
        throw new Error('Unsupported language');
    }
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
    this.creditPackageService
      .getAllCreditPackages()
      .pipe(
        takeUntil(this.destroy$),
        filter(bundles => bundles.length > 0),
        switchMap(bundles =>
          forkJoin(
            bundles.map(bundle =>
              this.currencyConverter(this.translate.currentLang, bundle).pipe(
                map(convertedPrice => ({
                  ...bundle,
                  convertedPriceCurrency: convertedPrice
                }))
              )
            )
          )
        )
      )
      .subscribe(rs => {
        this.creditPackages = rs;
      });
  }

  private currencyConverter(
    lang: string,
    credit: CreditPackage
  ): Observable<number> {
    if (lang === 'vi') {
      return of(credit.price);
    }
    const toCurrency = lang === 'en' ? 'USD' : 'CNY';
    return this.currencyConverterService.convertToVndCurrency(
      credit.price,
      toCurrency
    );
  }
}
