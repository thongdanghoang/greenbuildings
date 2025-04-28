import {HttpClient} from '@angular/common/http';
import {Component} from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormControl,
  Validators
} from '@angular/forms';
import {TranslateService} from '@ngx-translate/core';
import {takeUntil} from 'rxjs';
import {AppRoutingConstants} from '../../../../app-routing.constant';
import {ApplicationConstant} from '../../../../application.constant';
import {UserService} from '../../../../services/user.service';
import {EnterpriseUserDetails} from '../../../../models/enterprise-user';
import {EnterpriseUserService} from '../../../../services/enterprise-user.service';
import {AbstractFormComponent} from '../../../shared/components/form/abstract-form-component';
import {ToastProvider} from '../../../shared/services/toast-provider';

@Component({
  selector: 'app-account-information',
  templateUrl: './account-information.component.html',
  styleUrl: './account-information.component.css'
})
export class AccountInformationComponent extends AbstractFormComponent<EnterpriseUserDetails> {
  initUser: EnterpriseUserDetails = {} as EnterpriseUserDetails;
  protected readonly formStructure = {
    id: new FormControl('', [Validators.required]),
    version: new FormControl(0, [Validators.required]),
    email: new FormControl('', [Validators.required, Validators.email]),
    firstName: new FormControl('', [Validators.required]),
    lastName: new FormControl('', [Validators.required]),
    phone: new FormControl('', [
      Validators.required,
      Validators.pattern(ApplicationConstant.PHONE_PATTERN)
    ]),
    role: new FormControl('', [Validators.required]),
    scope: new FormControl('', [Validators.required])
  };

  protected readonly AppRoutingConstants = AppRoutingConstants;

  constructor(
    httpClient: HttpClient,
    formBuilder: FormBuilder,
    notificationService: ToastProvider,
    translate: TranslateService,
    private readonly userService: UserService,
    private readonly enterpriseUserService: EnterpriseUserService
  ) {
    super(httpClient, formBuilder, notificationService, translate);
  }

  revert(): void {
    this.formGroup.patchValue(this.initUser);
  }

  protected initializeData(): void {
    this.userService
      .getUserInfo()
      .pipe(takeUntil(this.destroy$))
      .subscribe((user: EnterpriseUserDetails) => {
        this.initUser = user;
        this.formGroup.patchValue(user);
      });
  }

  protected override submitFormMethod(): string {
    return 'PUT';
  }

  protected initializeFormControls(): {[p: string]: AbstractControl} {
    this.formStructure.email.disable();
    return this.formStructure;
  }

  protected onSubmitFormDataSuccess(result: any): void {
    this.formGroup.patchValue(result);
  }

  protected submitFormDataUrl(): string {
    return this.enterpriseUserService.updateBasicUser;
  }
}
