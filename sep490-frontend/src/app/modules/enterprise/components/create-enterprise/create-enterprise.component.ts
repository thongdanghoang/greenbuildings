import {HttpClient} from '@angular/common/http';
import {Component} from '@angular/core';
import {AbstractControl, FormBuilder} from '@angular/forms';
import {TranslateService} from '@ngx-translate/core';
import {MessageService} from 'primeng/api';
import {UserService} from '../../../../services/user.service';
import {AbstractFormComponent} from '../../../shared/components/form/abstract-form-component';

@Component({
  selector: 'app-create-enterprise',
  templateUrl: './create-enterprise.component.html',
  styleUrl: './create-enterprise.component.css'
})
export class CreateEnterpriseComponent extends AbstractFormComponent<void> {
  protected readonly formStructure = {};

  constructor(
    httpClient: HttpClient,
    formBuilder: FormBuilder,
    notificationService: MessageService,
    translate: TranslateService,
    private readonly userService: UserService
  ) {
    super(httpClient, formBuilder, notificationService, translate);
  }

  protected override initializeFormControls(): {
    [key: string]: AbstractControl;
  } {
    return this.formStructure;
  }
  protected override initializeData(): void {
    throw new Error('Method not implemented.');
  }
  protected override submitFormDataUrl(): string {
    throw new Error('Method not implemented.');
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  protected override onSubmitFormDataSuccess(result: any): void {
    throw new Error('Method not implemented.');
  }
}
