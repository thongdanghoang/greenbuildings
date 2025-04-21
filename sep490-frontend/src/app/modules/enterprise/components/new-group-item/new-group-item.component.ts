import {HttpClient} from '@angular/common/http';
import {Component, OnInit} from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormControl,
  Validators
} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {filter, map, switchMap, takeUntil} from 'rxjs';
import {validate} from 'uuid';
import {UUID} from '../../../../../types/uuid';
import {AppRoutingConstants} from '../../../../app-routing.constant';
import {ApplicationService} from '../../../core/services/application.service';
import {AbstractFormComponent} from '../../../shared/components/form/abstract-form-component';
import {ModalProvider} from '../../../shared/services/modal-provider';
import {ToastProvider} from '../../../shared/services/toast-provider';
import {BuildingGroup} from '../../models/enterprise.dto';
import {ActivityTypeService} from '../../services/activity-type.service';
import {BuildingGroupService} from '../../services/building-group.service';
import {GroupItemService} from '../../services/group-item.service';

@Component({
  selector: 'app-new-group-item',
  templateUrl: './new-group-item.component.html',
  styleUrl: './new-group-item.component.css'
})
export class NewGroupItemComponent
  extends AbstractFormComponent<void>
  implements OnInit
{
  buildingGroup!: BuildingGroup;

  protected readonly formStructure = {
    id: new FormControl('', Validators.required),
    version: new FormControl(0, Validators.required),
    name: new FormControl('', Validators.required),
    description: new FormControl(''),
    buildingGroupId: new FormControl('', Validators.required)
  };

  constructor(
    protected readonly applicationService: ApplicationService,
    private readonly activatedRoute: ActivatedRoute,
    private readonly buildingGroupService: BuildingGroupService,
    private readonly router: Router,
    private readonly itemService: GroupItemService,
    private readonly userService: ActivityTypeService,
    private readonly messageService: ToastProvider,
    private readonly modalProvider: ModalProvider,
    protected override httpClient: HttpClient,
    protected override formBuilder: FormBuilder,
    protected override notificationService: ToastProvider,
    protected override translate: TranslateService
  ) {
    super(httpClient, formBuilder, notificationService, translate);
  }

  handleGroupId(): void {
    this.activatedRoute.paramMap
      .pipe(
        takeUntil(this.destroy$),
        map(params => params.get('id')),
        filter((idParam): idParam is string => !!idParam),
        filter(id => validate(id)),
        switchMap(id => this.buildingGroupService.getById(id as UUID))
      )
      .subscribe(group => {
        this.buildingGroup = group;
        this.formStructure.buildingGroupId.setValue(group.id.toString());
      });
  }

  goBack(): void {
    void this.router.navigate([
      AppRoutingConstants.ENTERPRISE_PATH,
      AppRoutingConstants.BUILDING_GROUP_PATH,
      this.buildingGroup.id
    ]);
  }

  protected override initializeFormControls(): {
    [key: string]: AbstractControl;
  } {
    return this.formStructure;
  }
  protected override initializeData(): void {
    this.handleGroupId();
  }
  protected override submitFormDataUrl(): string {
    return this.itemService.getCreateNewGroupItemUrl;
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  protected override onSubmitFormDataSuccess(result: any): void {
    void this.router.navigate([
      AppRoutingConstants.ENTERPRISE_PATH,
      AppRoutingConstants.BUILDING_GROUP_PATH,
      this.buildingGroup.id
    ]);
  }
}
