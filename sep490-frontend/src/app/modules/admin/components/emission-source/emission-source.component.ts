import {Component, EventEmitter, OnInit} from '@angular/core';
import {SubscriptionAwareComponent} from '../../../core/subscription-aware.component';
import {ApplicationService} from '../../../core/services/application.service';
import {MessageService} from 'primeng/api';
import {ModalProvider} from '../../../shared/services/modal-provider';
import {TranslateService} from '@ngx-translate/core';
import {TableTemplateColumn} from '../../../shared/components/table-template/table-template.component';
import {EmissionSource} from '../../../enterprise/models/enterprise.dto';
import {
  SearchCriteriaDto,
  SearchResultDto
} from '../../../shared/models/base-models';
import {Observable} from 'rxjs';
import {EmissionSourceService} from '../../services/emission-source.service';
export interface EmissionSourceCriteria {
  criteria: string;
}
@Component({
  selector: 'app-emission-source',
  templateUrl: './emission-source.component.html',
  styleUrl: './emission-source.component.css'
})
export class EmissionSourceComponent
  extends SubscriptionAwareComponent
  implements OnInit
{
  protected fetchEmissionSource!: (
    criteria: SearchCriteriaDto<EmissionSourceCriteria>
  ) => Observable<SearchResultDto<EmissionSource>>;
  protected cols: TableTemplateColumn[] = [];
  protected readonly searchEvent: EventEmitter<void> = new EventEmitter();
  protected selected: EmissionSource[] = [];
  protected searchCriteria: EmissionSourceCriteria = {criteria: ''};
  constructor(
    protected readonly applicationService: ApplicationService,
    private readonly emissionSourceService: EmissionSourceService,
    private readonly messageService: MessageService,
    private readonly modalProvider: ModalProvider,
    private readonly translate: TranslateService
  ) {
    super();
  }

  ngOnInit(): void {
    this.buildCols();
    this.fetchEmissionSource =
      this.emissionSourceService.getEmissionSource.bind(
        this.emissionSourceService
      );
  }
  buildCols(): void {
    this.cols.push({
      field: 'nameVN',
      header: 'Nguồn phát thải'
    });
    this.cols.push({
      field: 'nameEN',
      header: 'Emission source'
    });
    this.cols.push({
      field: 'nameZH',
      header: '排放源'
    });
  }

  onSelectionChange(selectedUsers: EmissionSource[]): void {
    this.selected = selectedUsers;
  }

  search(): void {
    this.searchEvent.emit();
  }
}
