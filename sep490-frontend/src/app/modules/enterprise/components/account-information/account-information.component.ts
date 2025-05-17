import {Location} from '@angular/common';
import {Component} from '@angular/core';
import {AbstractControl, FormControl, Validators} from '@angular/forms';
import {environment} from '@environment';
import {EnterpriseUserDetails} from '@models/enterprise-user';
import {EnterpriseUserService} from '@services/enterprise-user.service';
import {UserService} from '@services/user.service';
import {AbstractFormComponent} from '@shared/components/form/abstract-form-component';
import {takeUntil} from 'rxjs';
import {AppRoutingConstants} from '../../../../app-routing.constant';
import {ApplicationConstant} from '../../../../application.constant';

@Component({
  selector: 'app-account-information',
  templateUrl: './account-information.component.html',
  styleUrl: './account-information.component.css'
})
export class AccountInformationComponent extends AbstractFormComponent<EnterpriseUserDetails> {
  passkeyIdpPage = environment.accountPageIDP;
  initUser: EnterpriseUserDetails = {} as EnterpriseUserDetails;
  protected readonly formStructure = {
    id: new FormControl('', [Validators.required]),
    version: new FormControl(0, [Validators.required]),
    email: new FormControl('', [Validators.required, Validators.email]),
    firstName: new FormControl('', [Validators.required]),
    lastName: new FormControl('', [Validators.required]),
    phone: new FormControl('', [Validators.required, Validators.pattern(ApplicationConstant.PHONE_PATTERN)]),
    role: new FormControl('')
  };

  protected readonly AppRoutingConstants = AppRoutingConstants;

  constructor(
    private readonly location: Location,
    private readonly userService: UserService,
    private readonly enterpriseUserService: EnterpriseUserService
  ) {
    super();
  }

  revert(): void {
    this.formGroup.patchValue(this.initUser);
  }

  navigateToPasskey(): void {
    window.location.href = this.passkeyIdpPage;
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

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  protected onSubmitFormDataSuccess(result: any): void {
    this.location.back();
  }

  protected submitFormDataUrl(): string {
    return this.enterpriseUserService.updateBasicUser;
  }
}
