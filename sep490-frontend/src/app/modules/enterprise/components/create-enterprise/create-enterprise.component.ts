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
import {ApplicationService} from '../../../core/services/application.service';
import {AbstractFormComponent} from '../../../shared/components/form/abstract-form-component';
import {
  EnterpriseUserService,
  NewEnterpriseDTO
} from '../../../authorization/services/enterprise-user.service';

@Component({
  selector: 'app-create-enterprise',
  templateUrl: './create-enterprise.component.html',
  styleUrl: './create-enterprise.component.css'
})
export class CreateEnterpriseComponent extends AbstractFormComponent<NewEnterpriseDTO> {
  protected readonly formStructure = {
    name: new FormControl('', [Validators.required]),
    enterpriseEmail: new FormControl('', [
      Validators.required,
      Validators.email
    ]),
    taxCode: new FormControl('', [Validators.required]),
    hotline: new FormControl('', [Validators.required])
  };

  constructor(
    httpClient: HttpClient,
    formBuilder: FormBuilder,
    notificationService: MessageService,
    translate: TranslateService,
    private readonly enterpriseUserService: EnterpriseUserService,
    private readonly injector: Injector,
    private readonly applicationService: ApplicationService
  ) {
    super(httpClient, formBuilder, notificationService, translate);
  }

  protected initializeFormControls(): {[p: string]: AbstractControl} {
    return this.formStructure;
  }

  protected override initializeData(): void {}

  protected override submitFormDataUrl(): string {
    return this.enterpriseUserService.createNewEnterpriseURL;
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars,max-lines-per-function
  protected override onSubmitFormDataSuccess(result: any): void {
    const oidcSecurityService = this.injector.get(OidcSecurityService);
    const messageService = this.injector.get(MessageService);

    const countdown = 5;
    const message = this.translate.instant('enterprise.create.success');
    const redirectMessage = this.translate.instant(
      'enterprise.create.redirect'
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
}
