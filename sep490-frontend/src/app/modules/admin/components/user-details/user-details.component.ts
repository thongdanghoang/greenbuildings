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
import {ActivatedRoute, Router} from '@angular/router';
import {BuildingPermissionRole} from '@models/building-permission-role';
import {Building} from '@models/enterprise';
import {BuildingPermission, EnterpriseUserDetails} from '@models/enterprise-user';
import {UserRole} from '@models/role-names';
import {UserScope} from '@models/user-scope';
import {TranslateService} from '@ngx-translate/core';
import {BuildingService} from '@services/building.service';
import {EnterpriseUserService} from '@services/enterprise-user.service';
import {AbstractFormComponent} from '@shared/components/form/abstract-form-component';
import {SelectableItem} from '@shared/models/base-models';
import {ToastProvider} from '@shared/services/toast-provider';
import {MultiSelectChangeEvent} from 'primeng/multiselect';
import {SelectChangeEvent} from 'primeng/select';
import {filter, map, switchMap, takeUntil, tap} from 'rxjs';
import {UUID} from '../../../../../types/uuid';
import {AppRoutingConstants} from '../../../../app-routing.constant';

@Component({
  selector: 'app-create-user',
  templateUrl: './user-details.component.html',
  styleUrl: './user-details.component.css'
})
export class UserDetailsComponent extends AbstractFormComponent<EnterpriseUserDetails> {
  buildings: Building[] = [];
  protected readonly enterpriseUserStructure = {
    id: new FormControl<UUID | null>({value: null, disabled: true}),
    createdDate: new FormControl<Date | null>({value: null, disabled: true}),
    version: new FormControl(null),
    email: new FormControl<string>('', {
      nonNullable: true,
      validators: [Validators.required, Validators.email]
    }),
    emailVerified: new FormControl<boolean>({value: false, disabled: true}),
    firstName: new FormControl<string>('', {
      nonNullable: true,
      validators: [Validators.required]
    }),
    lastName: new FormControl<string>('', {
      nonNullable: true,
      validators: [Validators.required]
    }),
    role: new FormControl(UserRole[UserRole.BASIC_USER], {
      nonNullable: true
    }),
    scope: new FormControl<string>('', {
      nonNullable: true,
      validators: [Validators.required]
    }),
    buildingPermissions: new FormArray([]),
    selectedBuildingIds: new FormControl<UUID[]>([], [this.selectedBuildingIdsValidator().bind(this)]),
    enterprisePermission: new FormControl<BuildingPermissionRole | null>(null, [
      this.enterprisePermissionValidator().bind(this)
    ])
  };
  protected permissionRoleOptions: SelectableItem<string>[] = [
    {
      disabled: false,
      value: BuildingPermissionRole[BuildingPermissionRole.MANAGER],
      label: 'enum.permissionRole.MANAGER'
    },
    {
      disabled: false,
      value: BuildingPermissionRole[BuildingPermissionRole.STAFF],
      label: 'enum.permissionRole.STAFF'
    },
    {
      disabled: false,
      value: BuildingPermissionRole[BuildingPermissionRole.AUDITOR],
      label: 'enum.permissionRole.AUDITOR'
    }
  ];
  protected buildingPermissionScopeEnterprise: SelectableItem<string>[] = [
    {
      disabled: false,
      value: BuildingPermissionRole[BuildingPermissionRole.MANAGER] as keyof typeof BuildingPermissionRole,
      label: 'enum.permissionRole.MANAGER'
    },
    {
      disabled: false,
      value: BuildingPermissionRole[BuildingPermissionRole.STAFF] as keyof typeof BuildingPermissionRole,
      label: 'enum.permissionRole.STAFF'
    },
    {
      disabled: false,
      value: BuildingPermissionRole[BuildingPermissionRole.AUDITOR] as keyof typeof BuildingPermissionRole,
      label: 'enum.permissionRole.AUDITOR'
    }
  ];
  protected scopeOptions: SelectableItem<keyof typeof UserScope>[] = [
    {
      disabled: false,
      value: UserScope[UserScope.ENTERPRISE] as keyof typeof UserScope,
      label: 'enum.scope.ENTERPRISE'
    },
    {
      disabled: false,
      value: UserScope[UserScope.BUILDING] as keyof typeof UserScope,
      label: 'enum.scope.BUILDING'
    }
  ];
  protected selectableBuildings: SelectableItem<UUID>[] = this.buildings.map(building => ({
    disabled: false,
    value: building.id,
    label: building.name
  }));

  constructor(
    httpClient: HttpClient,
    formBuilder: FormBuilder,
    notificationService: ToastProvider,
    translate: TranslateService,
    private readonly buildingService: BuildingService,
    protected userService: EnterpriseUserService,
    private readonly router: Router,
    private readonly activatedRoute: ActivatedRoute
  ) {
    super(httpClient, formBuilder, notificationService, translate);
  }

  fetchBuilding(): void {
    this.buildingService
      .searchBuildings({
        page: {
          pageNumber: 0,
          pageSize: 100
        }
      })
      .pipe(takeUntil(this.destroy$))
      .subscribe(buildings => {
        this.buildings = buildings.results;
        this.updateSelectableBuildings();
      });
  }

  updateSelectableBuildings(): void {
    this.selectableBuildings = this.buildings.map(building => ({
      disabled: false,
      value: building.id,
      label: building.name
    }));
  }

  onBuildingSelect(event: MultiSelectChangeEvent): void {
    const selectedBuildingIds = event.value;
    const currentBuildingIds = this.buildingPermissions.controls.map(control => control.value.buildingId);
    const buildingIdsToAdd = selectedBuildingIds.filter((id: UUID): boolean => !currentBuildingIds.includes(id));
    const buildingIdsToRemove = currentBuildingIds.filter((id: UUID): boolean => !selectedBuildingIds.includes(id));
    buildingIdsToRemove.forEach(buildingId => {
      const index = this.buildingPermissions.controls.findIndex(control => control.value.buildingId === buildingId);
      if (index !== -1) {
        this.removeBuildingPermission(index);
      }
    });
    buildingIdsToAdd.forEach((buildingId: UUID): void => {
      this.addBuildingPermission(buildingId);
    });
  }

  onEnterprisePermissionChange(event: SelectChangeEvent): void {
    this.enterpriseUserStructure.buildingPermissions.clear();
    this.addBuildingPermission(null, event.value);
  }

  onScopeChange(): void {
    this.buildingPermissions.clear();
    this.enterpriseUserStructure.selectedBuildingIds.setValue([]);
    this.enterpriseUserStructure.enterprisePermission.setValue(null);
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

  getBuildingName(control: AbstractControl): string {
    const building = this.buildings.find(b => b.id === control.value.buildingId);
    return building?.name ?? '';
  }

  addBuildingPermission(buildingId: UUID | null, role?: keyof typeof BuildingPermissionRole): void {
    this.buildingPermissions.controls.push(
      this.formBuilder.group({
        buildingId: new FormControl(buildingId),
        role: new FormControl(role, [Validators.required])
      })
    );
  }

  onBack(): void {
    void this.router.navigate(['/', AppRoutingConstants.AUTH_PATH, AppRoutingConstants.USERS_PATH]);
  }

  removeBuildingPermission(index: number): void {
    this.buildingPermissions.removeAt(index);
  }

  override reset(): void {
    if (this.isEdit) {
      return this.initializeData();
    }
    this.buildingPermissions.clear();
    super.reset();
  }

  protected initializeData(): void {
    this.fetchBuilding();
    this.activatedRoute.paramMap
      .pipe(
        takeUntil(this.destroy$),
        map(params => params.get('id')),
        filter((idParam): idParam is string => !!idParam),
        tap(id => this.enterpriseUserStructure.id.setValue(id as UUID)),
        switchMap(id => this.userService.getUserById(id))
      )
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
    void this.router.navigate(['/', AppRoutingConstants.AUTH_PATH, AppRoutingConstants.USERS_PATH]);
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

  private enterprisePermissionValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      if (!this.enterpriseUserStructure) {
        return null;
      }
      if (this.scopeEnterprise) {
        return control.value ? null : {required: true};
      }
      return null;
    };
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
