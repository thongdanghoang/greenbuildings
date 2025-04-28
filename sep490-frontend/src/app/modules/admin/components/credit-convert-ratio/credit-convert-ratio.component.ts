import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {AppRoutingConstants} from '../../../../app-routing.constant';
import {CreditConvertRatio} from '@models/enterprise';
import {SubscriptionAwareComponent} from '../../../core/subscription-aware.component';
import {CreditConvertRatioService} from '@services/credit-convert-ratio.service';

@Component({
  selector: 'app-credit-covert-ratio',
  templateUrl: './credit-convert-ratio.component.html',
  styleUrl: './credit-convert-ratio.component.css'
})
export class CreditConvertRatioComponent
  extends SubscriptionAwareComponent
  implements OnInit
{
  creditConvertRatios: CreditConvertRatio[] = [];
  constructor(
    private readonly creditConvertRatioService: CreditConvertRatioService,
    private readonly router: Router
  ) {
    super();
  }
  ngOnInit(): void {
    this.getCreditConvertRatios();
  }

  getCreditConvertRatios(): void {
    this.registerSubscription(
      this.creditConvertRatioService
        .getAllCreditConvertRatio()
        .subscribe(rs => {
          this.creditConvertRatios = rs;
        })
    );
  }

  control(ratio: CreditConvertRatio): void {
    void this.router.navigate([
      `/${AppRoutingConstants.ADMIN_PATH}/${AppRoutingConstants.CREDIT_CONVERT_RATIO_DETAILS}`,
      ratio.id
    ]);
  }
}
