import { Component, TemplateRef, ViewChild } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Observable } from 'rxjs';
import { ApplicationService } from '../../../core/services/application.service';
import { SubscriptionAwareComponent } from '../../../core/subscription-aware.component';
import { TableTemplateColumn } from '../../../shared/components/table-template/table-template.component';
import { SearchCriteriaDto, SearchResultDto } from '../../../shared/models/base-models';
import { ModalProvider } from '../../../shared/services/modal-provider';
import { ToastProvider } from '../../../shared/services/toast-provider';
import { Product } from '../../dev.service';

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
  @ViewChild('actionsTemplate', { static: true })
  actionsTemplate!: TemplateRef<any>;
  cols: TableTemplateColumn[] = [];
  productCriteria!: ProductCriteria;
  protected fetchProducts!: (
    criteria: SearchCriteriaDto<ProductCriteria>
  ) => Observable<SearchResultDto<Product>>;

  constructor(
    private readonly modalProvider: ModalProvider,
    private readonly messageService: ToastProvider,
    protected readonly applicationService: ApplicationService,
    protected readonly translate: TranslateService
  ) {
    super();
  }

  testToast(): void {
    const countdown = 5;
    const message = this.translate.instant('enterprise.create.success');
    const redirectMessage = this.translate.instant(
      'enterprise.create.redirect'
    );
    this.messageService.success({
      summary: message,
      detail: `${redirectMessage} ${countdown}s...`,
      closable: false,
      key: 'center',
      styleClass:
        'p-6 min-w-[400px] max-w-[600px] bg-white dark:bg-gray-800 shadow-lg rounded-lg'
    });
  }
}
