import {HttpClient} from '@angular/common/http';
import {Component} from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormControl,
  Validators
} from '@angular/forms';
import {TranslateService} from '@ngx-translate/core';
import {MessageService} from 'primeng/api';
import {takeUntil} from 'rxjs';
import {UserService} from '../../../../services/user.service';
import {AbstractFormComponent} from '../../../shared/components/form/abstract-form-component';

@Component({
  selector: 'app-account-information',
  templateUrl: './account-information.component.html',
  styleUrl: './account-information.component.css'
})
export class AccountInformationComponent extends AbstractFormComponent<void> {
  protected readonly formStructure = {
    email: new FormControl('', [Validators.required, Validators.email]),
    firstName: new FormControl('', [Validators.required]),
    lastName: new FormControl('', [Validators.required]),
    phone: new FormControl('', [Validators.required])
  };

  constructor(
    httpClient: HttpClient,
    formBuilder: FormBuilder,
    notificationService: MessageService,
    translate: TranslateService,
    private readonly userService: UserService
  ) {
    super(httpClient, formBuilder, notificationService, translate);
  }

  protected initializeData(): void {
    this.userService
      .getUserInfo()
      .pipe(takeUntil(this.destroy$))
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      .subscribe(user => {
        // temp
      });
  }

  protected initializeFormControls(): {[p: string]: AbstractControl} {
    return this.formStructure;
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  protected onSubmitFormDataSuccess(result: any): void {}

  protected submitFormDataUrl(): string {
    return '';
  }
}
