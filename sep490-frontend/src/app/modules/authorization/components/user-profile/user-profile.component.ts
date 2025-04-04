import {Component} from '@angular/core';
import {AbstractFormComponent} from '../../../shared/components/form/abstract-form-component';
import {
  BuildingPermission,
  EnterpriseUserDetails
} from '../../models/enterprise-user';
import {Building} from '../../../enterprise/models/enterprise.dto';
import {
  AbstractControl,
  FormArray,
  FormBuilder,
  FormControl,
  ValidationErrors,
  ValidatorFn,
  Validators
} from '@angular/forms';
import {UUID} from '../../../../../types/uuid';
import {UserRole} from '../../enums/role-names';
import {BuildingPermissionRole} from '../../enums/building-permission-role';
import {SelectableItem} from '../../../shared/models/base-models';
import {UserScope} from '../../enums/user-scope';
import {HttpClient} from '@angular/common/http';
import {MessageService} from 'primeng/api';
import {TranslateService} from '@ngx-translate/core';
import {BuildingService} from '../../../../services/building.service';
import {EnterpriseUserService} from '../../services/enterprise-user.service';
import {ActivatedRoute, Router} from '@angular/router';
import {takeUntil} from 'rxjs';
import {MultiSelectChangeEvent} from 'primeng/multiselect';
import {SelectChangeEvent} from 'primeng/select';
import {AppRoutingConstants} from '../../../../app-routing.constant';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.css'
})
export class UserProfileComponent extends AbstractFormComponent<EnterpriseUserDetails> {
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
    selectedBuildingIds: new FormControl<UUID[]>(
      [],
      [this.selectedBuildingIdsValidator().bind(this)]
    ),
    enterprisePermission: new FormControl<BuildingPermissionRole | null>(null)
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
      value: BuildingPermissionRole[
        BuildingPermissionRole.MANAGER
      ] as keyof typeof BuildingPermissionRole,
      label: 'enum.permissionRole.MANAGER'
    },
    {
      disabled: false,
      value: BuildingPermissionRole[
        BuildingPermissionRole.STAFF
      ] as keyof typeof BuildingPermissionRole,
      label: 'enum.permissionRole.STAFF'
    },
    {
      disabled: false,
      value: BuildingPermissionRole[
        BuildingPermissionRole.AUDITOR
      ] as keyof typeof BuildingPermissionRole,
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
  protected selectableBuildings: SelectableItem<any>[] = this.buildings.map(
    building => ({
      disabled: false,
      value: building.id,
      label: building.name
    })
  );

  constructor(
    httpClient: HttpClient,
    formBuilder: FormBuilder,
    notificationService: MessageService,
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
    const currentBuildingIds = this.buildingPermissions.controls.map(
      control => control.value.buildingId
    );
    const buildingIdsToAdd = selectedBuildingIds.filter(
      (id: UUID): boolean => !currentBuildingIds.includes(id)
    );
    const buildingIdsToRemove = currentBuildingIds.filter(
      (id: UUID): boolean => !selectedBuildingIds.includes(id)
    );
    buildingIdsToRemove.forEach(buildingId => {
      const index = this.buildingPermissions.controls.findIndex(
        control => control.value.buildingId === buildingId
      );
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
    return (
      this.enterpriseUserStructure.scope.value === UserScope[UserScope.BUILDING]
    );
  }

  get scopeEnterprise(): boolean {
    return (
      this.enterpriseUserStructure.scope.value ===
      UserScope[UserScope.ENTERPRISE]
    );
  }

  get roleOwner(): boolean {
    return (
      this.enterpriseUserStructure.role.value ===
      UserRole[UserRole.ENTERPRISE_EMPLOYEE]
    );
  }

  get buildingPermissions(): FormArray {
    return this.formGroup.get('buildingPermissions') as FormArray;
  }

  getBuildingName(control: AbstractControl): string {
    const building = this.buildings.find(
      b => b.id === control.value.buildingId
    );
    return building?.name ?? '';
  }

  addBuildingPermission(
    buildingId: UUID | null,
    role?: keyof typeof BuildingPermissionRole
  ): void {
    this.buildingPermissions.controls.push(
      this.formBuilder.group({
        buildingId: new FormControl(buildingId),
        role: new FormControl(role, [Validators.required])
      })
    );
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
    this.userService
      .getUserOwner()
      .pipe(takeUntil(this.destroy$))
      .subscribe(user => {
        this.formGroup.patchValue(user);
        // js didn't parse the date correctly from LocalDateTime to js Date
        this.enterpriseUserStructure.createdDate.setValue(
          new Date(user.createdDate)
        );
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
    void this.router.navigate([
      '/',
      AppRoutingConstants.AUTH_PATH,
      AppRoutingConstants.USER_PROFILE
    ]);
  }

  private initializeBuildingPermissions(
    buildingPermissions: BuildingPermission[]
  ): void {
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
        this.buildingPermissions.controls.map(
          control => control.value.buildingId
        )
      );
    }
  }

  private initializeEnterprisePermission(): void {
    if (
      this.scopeEnterprise &&
      this.roleOwner &&
      this.buildingPermissions.controls.length > 0
    ) {
      this.enterpriseUserStructure.enterprisePermission.setValue(
        this.buildingPermissions.controls[0].value.role
      );
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
