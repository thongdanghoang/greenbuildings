import {Component, TemplateRef, ViewChild} from '@angular/core';
import {MessageService} from 'primeng/api';
import {Observable} from 'rxjs';
import {ApplicationService} from '../../../core/services/application.service';
import {SubscriptionAwareComponent} from '../../../core/subscription-aware.component';
import {TableTemplateColumn} from '../../../shared/components/table-template/table-template.component';
import {SearchCriteriaDto, SearchResultDto} from '../../../shared/models/base-models';
import {ModalProvider} from '../../../shared/services/modal-provider';
import {Product} from '../../dev.service';

export interface ProductCriteria {
  criteria: string;
  // specific criteria such as name, category, etc.
}

@Component({
  selector: 'app-toolbox',
  templateUrl: './toolbox.component.html',
  styleUrl: './toolbox.component.scss'
})
export class ToolboxComponent
  extends SubscriptionAwareComponent {
  @ViewChild('actionsTemplate', {static: true})
  actionsTemplate!: TemplateRef<any>;
  cols: TableTemplateColumn[] = [];
  productCriteria!: ProductCriteria;
  protected fetchProducts!: (
    criteria: SearchCriteriaDto<ProductCriteria>
  ) => Observable<SearchResultDto<Product>>;

  constructor(
    private readonly modalProvider: ModalProvider,
    private readonly messageService: MessageService,
    protected readonly applicationService: ApplicationService
  ) {
    super();
  }

}
