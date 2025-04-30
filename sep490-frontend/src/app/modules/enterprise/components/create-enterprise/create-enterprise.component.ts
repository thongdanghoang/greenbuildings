import {HttpClient} from '@angular/common/http';
import {Component, Injector} from '@angular/core';
import {AbstractControl, FormBuilder, FormControl, Validators} from '@angular/forms';
import {RegisterEnterpriseDTO} from '@generated/models/register-enterprise-dto';
import {UserRole} from '@models/role-names';
import {TranslateService} from '@ngx-translate/core';
import {ApplicationService} from '@services/application.service';
import {EnterpriseUserService} from '@services/enterprise-user.service';
import {StorageService} from '@services/storage.service';
import {AbstractFormComponent} from '@shared/components/form/abstract-form-component';
import {ToastProvider} from '@shared/services/toast-provider';
import {OidcSecurityService} from 'angular-auth-oidc-client';
import {MessageService} from 'primeng/api';
import {delay, take, takeUntil, tap} from 'rxjs';

@Component({
  selector: 'app-create-enterprise',
  templateUrl: './create-enterprise.component.html',
  styleUrl: './create-enterprise.component.css'
})
export class CreateEnterpriseComponent extends AbstractFormComponent<RegisterEnterpriseDTO> {
  protected readonly UserRole = UserRole;
  protected readonly formStructure = {
    name: new FormControl<string | null>(null, [Validators.required]),
    taxCode: new FormControl<string | null>(null, [Validators.required]),
    hotline: new FormControl<string | null>(null, [Validators.required]),
    role: new FormControl('', [Validators.required]),
    address: new FormControl<string | null>(null, [Validators.required]),
    businessLicenseImageUrl: new FormControl<string | null>(null, {
      nonNullable: true,
      validators: [Validators.required]
    }),
    representativeName: new FormControl<string | null>(null),
    representativePosition: new FormControl<string | null>(null),
    representativeContact: new FormControl<string | null>(null)
  };

  constructor(
    httpClient: HttpClient,
    formBuilder: FormBuilder,
    notificationService: ToastProvider,
    translate: TranslateService,
    private readonly enterpriseUserService: EnterpriseUserService,
    private readonly injector: Injector,
    private readonly applicationService: ApplicationService,
    private readonly storageService: StorageService
  ) {
    super(httpClient, formBuilder, notificationService, translate);
  }

  protected uploadBusinessLicense(event: Event): void {
    const input = event.target as HTMLInputElement;
    const maxSize = 10 * 1024 * 1024;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      if (file.size > maxSize) {
        this.notificationService.businessError({
          summary: this.translate.instant('enterprise.create.error.fileSizeExceeded'),
          detail: this.translate.instant('enterprise.create.error.fileSizeExceededDetail', {
            maxSize: maxSize / 1024 / 1024
          })
        });
      }
      this.storageService
        .uploadBusinessLicense(file)
        .pipe(takeUntil(this.destroy$))
        .subscribe(view => {
          if (view.path) {
            this.formStructure.businessLicenseImageUrl.setValue(view.path);
          }
        });
    }
  }

  protected initializeFormControls(): {[p: string]: AbstractControl} {
    return this.formStructure;
  }

  protected override initializeData(): void {}

  protected override submitFormDataUrl(): string {
    return this.enterpriseUserService.createNewEnterpriseURL;
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars,max-lines-per-function
  protected override onSubmitFormDataSuccess(_result: any): void {
    const oidcSecurityService = this.injector.get(OidcSecurityService);
    const messageService = this.injector.get(MessageService);

    const countdown = 5;
    const message = this.translate.instant('enterprise.create.success');
    const redirectMessage = this.translate.instant('enterprise.create.redirect');

    messageService.clear();
    messageService.add({
      severity: 'success',
      summary: message,
      detail: `${redirectMessage} ${countdown}s...`,
      sticky: true,
      closable: false,
      key: 'center',
      styleClass: 'text-xl p-6 min-w-[400px] max-w-[600px] bg-white dark:bg-gray-800 shadow-lg rounded-lg'
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
