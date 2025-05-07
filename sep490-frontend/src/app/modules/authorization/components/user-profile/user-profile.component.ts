import {HttpClient} from '@angular/common/http';
import {Component} from '@angular/core';
import {
  AbstractControl,
  FormArray,
  FormBuilder,
  FormControl,
  ValidationErrors,
  ValidatorFn,
  Validators
} from '@angular/forms';
import {Router} from '@angular/router';
import {BuildingPermissionRole} from '@models/building-permission-role';
import {Building} from '@models/enterprise';
import {BuildingPermission, EnterpriseUserDetails} from '@models/enterprise-user';
import {UserRole} from '@models/role-names';
import {UserScope} from '@models/user-scope';
import {TranslateService} from '@ngx-translate/core';
import {EnterpriseUserService} from '@services/enterprise-user.service';
import {AbstractFormComponent} from '@shared/components/form/abstract-form-component';
import {ToastProvider} from '@shared/services/toast-provider';
import {takeUntil} from 'rxjs';
import {UUID} from '../../../../../types/uuid';
import {AppRoutingConstants} from '../../../../app-routing.constant';
import {environment} from '@environment';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.css'
})
export class UserProfileComponent extends AbstractFormComponent<EnterpriseUserDetails> {
  passkeyIdpPage = environment.accountPageIDP;
  buildings: Building[] = [];
  protected readonly enterpriseUserStructure = {
    id: new FormControl<UUID | null>({value: null, disabled: true}),
    createdDate: new FormControl<Date | null>({value: null, disabled: true}),
    version: new FormControl(null),
    email: new FormControl<string>(
      {value: '', disabled: true},
      {
        nonNullable: true,
        validators: [Validators.required, Validators.email]
      }
    ),
    emailVerified: new FormControl<boolean>({value: false, disabled: true}),
    firstName: new FormControl<string>('', {
      validators: [Validators.required]
    }),
    lastName: new FormControl<string>('', {
      validators: [Validators.required]
    }),
    role: new FormControl(UserRole[UserRole.ENTERPRISE_OWNER]),
    scope: new FormControl<string>(''),
    buildingPermissions: new FormArray([]),
    selectedBuildingIds: new FormControl<UUID[]>([], [this.selectedBuildingIdsValidator().bind(this)]),
    enterprisePermission: new FormControl<BuildingPermissionRole | null>(null)
  };

  constructor(
    httpClient: HttpClient,
    formBuilder: FormBuilder,
    notificationService: ToastProvider,
    translate: TranslateService,
    protected userService: EnterpriseUserService,
    private readonly router: Router
  ) {
    super(httpClient, formBuilder, notificationService, translate);
  }

  get isEdit(): boolean {
    return !!this.enterpriseUserStructure.id.value;
  }

  get scopeBuildings(): boolean {
    return this.enterpriseUserStructure.scope.value === UserScope[UserScope.BUILDING];
  }

  get scopeEnterprise(): boolean {
    return this.enterpriseUserStructure.scope.value === UserScope[UserScope.ENTERPRISE];
  }

  get buildingPermissions(): FormArray {
    return this.formGroup.get('buildingPermissions') as FormArray;
  }

  navigateToPasskey(): void {
    window.location.href = this.passkeyIdpPage;
  }

  override reset(): void {
    if (this.isEdit) {
      return this.initializeData();
    }
    this.buildingPermissions.clear();
    super.reset();
  }

  protected initializeData(): void {
    this.userService
      .getUserOwner()
      .pipe(takeUntil(this.destroy$))
      .subscribe(user => {
        this.formGroup.patchValue(user);
        // js didn't parse the date correctly from LocalDateTime to js Date
        this.enterpriseUserStructure.createdDate.setValue(new Date(user.createdDate));
        this.initializeBuildingPermissions(user.buildingPermissions);
        this.initializeSelectedBuildingIds();
        this.initializeEnterprisePermission();
      });
  }

  protected initializeFormControls(): {[p: string]: AbstractControl} {
    return this.enterpriseUserStructure;
  }

  protected override prepareDataBeforeSubmit(): void {}

  protected submitFormDataUrl(): string {
    return this.userService.createOrUpdateUserURL;
  }

  protected onSubmitFormDataSuccess(): void {
    void this.router.navigate(['/', AppRoutingConstants.AUTH_PATH, AppRoutingConstants.USER_PROFILE]);
  }

  private initializeBuildingPermissions(buildingPermissions: BuildingPermission[]): void {
    // Clear the existing FormArray
    this.buildingPermissions.clear();

    // Add a FormGroup for each building permission
    buildingPermissions.forEach(permission => {
      this.buildingPermissions.push(
        this.formBuilder.group({
          buildingId: new FormControl(permission.buildingId),
          role: new FormControl(permission.role)
        })
      );
    });
  }

  private initializeSelectedBuildingIds(): void {
    if (this.scopeBuildings) {
      this.enterpriseUserStructure.selectedBuildingIds.setValue(
        this.buildingPermissions.controls.map(control => control.value.buildingId)
      );
    }
  }

  private initializeEnterprisePermission(): void {
    if (this.scopeEnterprise && this.buildingPermissions.controls.length > 0) {
      this.enterpriseUserStructure.enterprisePermission.setValue(this.buildingPermissions.controls[0].value.role);
    }
  }

  private selectedBuildingIdsValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      if (!this.enterpriseUserStructure) {
        return null;
      }
      if (this.scopeBuildings) {
        return control.value.length > 0 ? null : {required: true};
      }
      return null;
    };
  }
}
