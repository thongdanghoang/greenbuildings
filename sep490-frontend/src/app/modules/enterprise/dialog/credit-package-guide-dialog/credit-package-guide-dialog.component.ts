import {DecimalPipe} from '@angular/common';
import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {TranslateService} from '@ngx-translate/core';
import {DynamicDialogConfig, DynamicDialogRef} from 'primeng/dynamicdialog';
import {CreditConvertRatio, CreditConvertType, CreditPackage} from '@models/enterprise';
import {SubscriptionService} from '@services/subscription.service';
import {WalletService} from '@services/wallet.service';
import {ApplicationService} from '@services/application.service';

@Component({
  selector: 'app-credit-package-guide-dialog',
  templateUrl: './credit-package-guide-dialog.component.html',
  styleUrl: './credit-package-guide-dialog.component.css'
})
export class CreditPackageGuideDialogComponent implements OnInit {
  formGroup: FormGroup;
  balance: number = 0;
  totalToPay: number = 0;
  sufficientBalance: boolean = true;
  creditConvertRatios: CreditConvertRatio[] = [];
  creditPackages: CreditPackage[] = [];
  recommendedPackage: CreditPackage | null = null;

  constructor(
    private readonly ref: DynamicDialogRef,
    private readonly translate: TranslateService,
    private readonly config: DynamicDialogConfig,
    private readonly walletService: WalletService,
    private readonly subscriptionService: SubscriptionService,
    private readonly applicationService: ApplicationService,
    private readonly formBuilder: FormBuilder,
    private readonly decimalPipe: DecimalPipe
  ) {
    this.formGroup = this.formBuilder.group({
      months: new FormControl(0, [Validators.required, Validators.min(1), Validators.max(100)]),
      numberOfDevices: new FormControl(0, [Validators.required, Validators.min(1)]),
      monthRatio: new FormControl(0, [Validators.required]),
      deviceRatio: new FormControl(0, [Validators.required])
    });
  }

  ngOnInit(): void {
    // Fetch balance
    this.walletService.getWalletBalance().subscribe(balance => {
      this.balance = balance;
      this.calculateTotalToPay();
    });

    // Fetch conversion ratios
    this.subscriptionService.getCreditConvertRatio().subscribe((ratios: CreditConvertRatio[]) => {
      this.creditConvertRatios = ratios;
      ratios.forEach(x => {
        if (x.convertType === CreditConvertType[CreditConvertType.MONTH]) {
          this.formGroup.get('monthRatio')?.setValue(x.ratio);
        } else if (x.convertType === CreditConvertType[CreditConvertType.DEVICE]) {
          this.formGroup.get('deviceRatio')?.setValue(x.ratio);
        }
      });
      this.calculateTotalToPay();
    });

    // Get creditPackages from dialog config
    this.creditPackages = this.config.data?.creditPackages || [];
  }

  calculateTotalToPay(): void {
    const months = this.formGroup.get('months')?.value || 0;
    const numberOfDevices = this.formGroup.get('numberOfDevices')?.value || 0;
    const monthRatio = this.formGroup.get('monthRatio')?.value || 0;
    const deviceRatio = this.formGroup.get('deviceRatio')?.value || 0;

    this.totalToPay = months * monthRatio * numberOfDevices * deviceRatio;
    this.totalToPay = Number(this.totalToPay.toFixed(0));

    this.sufficientBalance = this.balance >= this.totalToPay;
  }

  findRecommendedPackage(): void {
    if (!this.creditPackages || this.creditPackages.length === 0) {
      this.recommendedPackage = null;
      return;
    }

    // Filter packages with numberOfCredits greater than totalToPay
    const qualifyingPackages = this.creditPackages
      .filter(pkg => pkg.numberOfCredits >= this.totalToPay)
      .sort((a, b) => a.numberOfCredits - b.numberOfCredits); // Sort by numberOfCredits ascending

    // Select the package with the smallest numberOfCredits that is still >= totalToPay
    this.recommendedPackage = qualifyingPackages.length > 0 ? qualifyingPackages[0] : null;
  }

  proceedToStep3(activateCallback: (step: number) => void): void {
    this.findRecommendedPackage(); // Calculate the recommended package
    activateCallback(3); // Navigate to Step 3
  }

  formatedCurrency(currency: number): string | null {
    const converted = this.decimalPipe.transform(currency, '1.0-0');
    const symbol = this.translate.instant('currency.symbol');
    if (this.translate.currentLang === 'vi') {
      return `${converted} ${symbol}`;
    }
    return `${symbol} ${converted}`;
  }

  closeDialog(): void {
    this.ref.close();
  }
}
