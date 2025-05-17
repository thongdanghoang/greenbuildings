import {Component} from '@angular/core';
import {AbstractControl, FormControl, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {CreditPackage} from '@models/enterprise';
import {PackageCreditService} from '@services/package-credit.service';
import {AbstractFormComponent} from '@shared/components/form/abstract-form-component';
import {filter, map, switchMap, takeUntil, tap} from 'rxjs';

import {AppRoutingConstants} from '../../../../app-routing.constant';

@Component({
  selector: 'app-create-update-package-credit',
  templateUrl: './create-update-package-credit.component.html',
  styleUrl: './create-update-package-credit.component.css'
})
export class CreateUpdatePackageCreditComponent extends AbstractFormComponent<CreditPackage> {
  protected readonly packageCreditStructure = {
    id: new FormControl(''),
    version: new FormControl(null),
    numberOfCredits: new FormControl(null, [Validators.required, Validators.min(1)]),
    price: new FormControl(null, [Validators.required, Validators.min(1)]),
    discount: new FormControl(0, {
      nonNullable: true,
      validators: [Validators.required, Validators.min(0), Validators.max(100)]
    })
  };
  constructor(
    protected creditPackageService: PackageCreditService,
    private readonly router: Router,
    private readonly activatedRoute: ActivatedRoute
  ) {
    super();
  }

  back(): void {
    void this.router.navigate(['/', AppRoutingConstants.ADMIN_PATH, AppRoutingConstants.PACKAGE_CREDIT_PATH]);
  }

  get isEdit(): boolean {
    return !!this.packageCreditStructure.id.value;
  }
  override reset(): void {
    if (this.isEdit) {
      return this.initializeData();
    }
    super.reset();
  }
  protected initializeData(): void {
    this.activatedRoute.paramMap
      .pipe(
        takeUntil(this.destroy$),
        map(params => params.get('id')),
        filter((idParam): idParam is string => !!idParam),
        tap(id => this.packageCreditStructure.id.setValue(id)),
        switchMap(id => this.creditPackageService.getPackageById(id))
      )
      .subscribe(user => {
        this.formGroup.patchValue(user);
      });
  }

  protected initializeFormControls(): {[p: string]: AbstractControl} {
    return this.packageCreditStructure;
  }

  protected onSubmitFormDataSuccess(): void {
    void this.router.navigate(['/', AppRoutingConstants.ADMIN_PATH, AppRoutingConstants.PACKAGE_CREDIT_PATH]);
  }

  protected submitFormDataUrl(): string {
    return this.creditPackageService.createOrUpdatePackageURL;
  }
}
