import {Component} from '@angular/core';
import {MessageService} from 'primeng/api';
import {TranslateService} from '@ngx-translate/core';
import {CreditConvertRatioService} from '../../services/credit-convert-ratio.service';
import {ActivatedRoute, Router} from '@angular/router';
import {filter, map, switchMap, takeUntil} from 'rxjs';
import {CreditConvertRatio} from '../../../enterprise/models/enterprise.dto';
import {UUID} from '../../../../../types/uuid';
import {AbstractFormComponent} from '../../../shared/components/form/abstract-form-component';
import {
  AbstractControl,
  FormBuilder,
  FormControl,
  Validators
} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {AppRoutingConstants} from '../../../../app-routing.constant';

@Component({
  selector: 'app-update-ratio',
  templateUrl: './update-ratio.component.html',
  styleUrl: './update-ratio.component.css'
})
export class UpdateRatioComponent extends AbstractFormComponent<CreditConvertRatio> {
  selectedConvertType: string | undefined;
  protected readonly ratioStructure = {
    id: new FormControl(''),
    version: new FormControl(null),
    ratio: new FormControl(null, [Validators.required, Validators.min(0)]),
    convertType: new FormControl('')
  };
  constructor(
    httpClient: HttpClient,
    formBuilder: FormBuilder,
    notificationService: MessageService,
    translate: TranslateService,
    private readonly creditConvertRatioService: CreditConvertRatioService,
    private readonly router: Router,
    private readonly activatedRoute: ActivatedRoute
  ) {
    super(httpClient, formBuilder, notificationService, translate);
  }

  getCreditConvertRatio(): void {
    this.activatedRoute.paramMap
      .pipe(
        takeUntil(this.destroy$),
        map(params => params.get('id')),
        filter((idParam): idParam is string => !!idParam),
        switchMap(id =>
          this.creditConvertRatioService.getCreditConvertRatio(id as UUID)
        )
      )
      .subscribe(ratio => {
        this.formGroup.patchValue(ratio);
        this.selectedConvertType = ratio.convertType;
      });
  }

  return(): void {
    void this.router.navigate([
      '/',
      AppRoutingConstants.ADMIN_PATH,
      AppRoutingConstants.CREDIT_CONVERT_RATIO
    ]);
  }

  protected initializeData(): void {
    this.getCreditConvertRatio();
  }

  protected initializeFormControls(): {[p: string]: AbstractControl} {
    return this.ratioStructure;
  }

  protected onSubmitFormDataSuccess(): void {
    void this.router.navigate([
      '/',
      AppRoutingConstants.ADMIN_PATH,
      AppRoutingConstants.CREDIT_CONVERT_RATIO
    ]);
  }

  protected submitFormDataUrl(): string {
    return this.creditConvertRatioService.updateRatioURL;
  }
}
