import {HttpClient} from '@angular/common/http';
import {Component, Injector} from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormControl,
  Validators
} from '@angular/forms';
import {TranslateService} from '@ngx-translate/core';
import {OidcSecurityService} from 'angular-auth-oidc-client';
import {MessageService} from 'primeng/api';
import {delay, take, tap} from 'rxjs';
import {
  EnterpriseUserService,
  ValidateOTPRequest
} from '../../modules/authorization/services/enterprise-user.service';
import {ApplicationService} from '../../modules/core/services/application.service';
import {AbstractFormComponent} from '../../modules/shared/components/form/abstract-form-component';
import {ToastProvider} from '../../modules/shared/services/toast-provider';

@Component({
  selector: 'app-email-verify-otp',
  templateUrl: './email-verify-otp.component.html',
  styleUrl: './email-verify-otp.component.css'
})
export class EmailVerifyOTPComponent extends AbstractFormComponent<ValidateOTPRequest> {
  userEmail = '';
  firstSent = false;

  form = {
    otpCode: new FormControl('', [
      Validators.required,
      Validators.pattern(/^\d{6}$/)
    ])
  };

  constructor(
    httpClient: HttpClient,
    formBuilder: FormBuilder,
    notificationService: ToastProvider,
    translate: TranslateService,
    private readonly enterpriseUserService: EnterpriseUserService,
    private readonly injector: Injector,
    private readonly applicationService: ApplicationService
  ) {
    super(httpClient, formBuilder, notificationService, translate);
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
    const message = this.translate.instant('account.validateOTP.success');
    const redirectMessage = this.translate.instant(
      'account.validateOTP.redirect'
    );

    messageService.clear();
    messageService.add({
      severity: 'success',
      summary: message,
      detail: `${redirectMessage} ${countdown}s...`,
      sticky: true,
      closable: false,
      key: 'center',
      styleClass:
        'text-xl p-6 min-w-[400px] max-w-[600px] bg-white dark:bg-gray-800 shadow-lg rounded-lg'
    });

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
    }, 5000);
  }

  protected override submitFormDataUrl(): string {
    return this.enterpriseUserService.getValidateOTPURL;
  }
}
