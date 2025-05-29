import {Component, Injector} from '@angular/core';
import {AbstractControl, FormControl, Validators} from '@angular/forms';
import {ValidateOTPRequest} from '@models/enterprise-user';
import {ApplicationService} from '@services/application.service';
import {EnterpriseUserService} from '@services/enterprise-user.service';
import {AbstractFormComponent} from '@shared/components/form/abstract-form-component';
import {OidcSecurityService} from 'angular-auth-oidc-client';
import {MessageService} from 'primeng/api';
import {delay, take, tap} from 'rxjs';

@Component({
  selector: 'app-email-verify-otp',
  templateUrl: './email-verify-otp.component.html',
  styleUrl: './email-verify-otp.component.css'
})
export class EmailVerifyOTPComponent extends AbstractFormComponent<ValidateOTPRequest> {
  countdown: number | null = null;
  userEmail = '';
  firstSent = false;

  form = {
    otpCode: new FormControl('', [Validators.required, Validators.pattern(/^\d{6}$/)])
  };

  constructor(
    private readonly enterpriseUserService: EnterpriseUserService,
    private readonly injector: Injector,
    private readonly applicationService: ApplicationService
  ) {
    super();
  }

  sendOtp(): void {
    this.enterpriseUserService.sendOTP().subscribe(() => {
      this.firstSent = true;
    });
  }

  protected initializeData(): void {
    this.applicationService.UserInfoData.subscribe(info => {
      this.userEmail = info.email;
    });
  }

  protected initializeFormControls(): {[p: string]: AbstractControl} {
    return this.form;
  }

  // eslint-disable-next-line max-lines-per-function,@typescript-eslint/no-unused-vars
  protected onSubmitFormDataSuccess(result: any): void {
    const oidcSecurityService = this.injector.get(OidcSecurityService);
    const messageService = this.injector.get(MessageService);

    const countdown = 5;
    this.countdown = countdown;
    const message = this.translate.instant('account.validateOTP.success');
    const redirectMessage = this.translate.instant('account.validateOTP.redirect');

    messageService.clear();
    messageService.add({
      severity: 'custom',
      summary: message,
      detail: redirectMessage,
      sticky: true,
      closable: false,
      key: 'center'
    });

    // Cập nhật số đếm ngược mỗi giây
    const interval = setInterval(() => {
      if (this.countdown !== null && this.countdown > 0) {
        this.countdown--;
      } else {
        clearInterval(interval); // Dừng interval khi đếm về 0
      }
    }, 1000);

    // After 5 seconds, trigger logoff and login
    setTimeout(() => {
      oidcSecurityService
        .logoffAndRevokeTokens()
        .pipe(
          take(1),
          tap(() => {
            sessionStorage.clear();
            localStorage.clear();
          }),
          delay(500), // slight delay for cleanup if needed
          tap(() => {
            this.applicationService.login();
          })
        )
        .subscribe({
          error: error => {
            console.error('Error during logout process:', error);
            this.applicationService.login();
          }
        });
    }, countdown * 1000);
  }

  protected override submitFormDataUrl(): string {
    return this.enterpriseUserService.getValidateOTPURL;
  }
}
