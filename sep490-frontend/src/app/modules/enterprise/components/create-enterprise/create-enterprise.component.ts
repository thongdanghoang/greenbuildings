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
    private readonly enterpriseUserService: EnterpriseUserService
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

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  protected override onSubmitFormDataSuccess(result: any): void {}
}
