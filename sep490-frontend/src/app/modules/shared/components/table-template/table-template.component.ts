import {Component, EventEmitter, Input, OnInit, Output, TemplateRef, ViewChild} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {ApplicationService} from '@services/application.service';
import {SortEvent} from 'primeng/api';
import {PaginatorState} from 'primeng/paginator';
import {Table} from 'primeng/table';
import {takeUntil} from 'rxjs';
import {ApplicationConstant} from '../../../../application.constant';
import {SearchResultDto, SortDto} from '../../models/base-models';
import {ToastProvider} from '../../services/toast-provider';
import {AbstractSearchComponent} from '../abstract-search';

export interface TableTemplateColumn {
  field: string;
  sortField?: string;
  header: string;
  sortable?: boolean;
  templateRef?: TemplateRef<any>;
}

@Component({
  selector: 'table-template',
  templateUrl: './table-template.component.html',
  styleUrl: './table-template.component.scss'
})
export class TableTemplateComponent<C, R, W extends SearchResultDto<R> = SearchResultDto<R>>
  extends AbstractSearchComponent<C, R, W>
  implements OnInit
{
  @Input() captionTemplateRef: TemplateRef<any> | undefined;
  @Input() expandedTemplateRef: TemplateRef<any> | undefined;
  @Input({required: true}) columns!: TableTemplateColumn[];
  @Input() sort: SortDto | undefined;
  @ViewChild('tableTemplateComponent') tableTemplateComponent!: Table;
  isSorted: boolean | null | undefined;
  @Input() checkbox: boolean = false;
  @Input() searchOnInit: boolean = true;
  @Input() triggerSearch: EventEmitter<void> | undefined;
  @Input() triggerClearSelected: EventEmitter<void> | undefined;
  @Output() readonly selectionChange: EventEmitter<R[]> = new EventEmitter();
  selected: R[] | undefined;
  paginatorTemplateString: string;

  protected readonly ApplicationConstant = ApplicationConstant;

  constructor(
    translate: TranslateService,
    messageService: ToastProvider,
    private readonly applicationService: ApplicationService
  ) {
    super(translate, messageService);
    this.paginatorTemplateString = this.translate.instant('table.paginatorString');
  }

  override ngOnInit(): void {
    super.ngOnInit();
    if (this.searchOnInit) {
      this.submit();
    }
    if (this.triggerSearch) {
      this.triggerSearch.pipe(takeUntil(this.destroy$)).subscribe((): void => this.submit());
    }
    if (this.triggerClearSelected) {
      this.triggerClearSelected.pipe(takeUntil(this.destroy$)).subscribe((): void => {
        this.selected = [];
      });
    }
    this.registerSubscription(
      this.translate.onLangChange.subscribe((): void => {
        this.paginatorTemplateString = this.translate.instant('table.paginatorString');
      })
    );
  }

  override initSearchDto(): void {
    this.searchCriteria = {
      page: {
        pageNumber: 0,
        pageSize: ApplicationConstant.DEFAULT_PAGE_SIZE
      },
      criteria: this.criteria,
      sort: this.sort
    };
  }

  get isMobile(): boolean {
    return this.applicationService.isMobile();
  }

  onSortChange(event: SortEvent): void {
    if (event.field && event.order) {
      this.sort = {
        field: event.field,
        direction: event.order === 1 ? 'ASC' : 'DESC'
      };
    }
    if (
      this.sort &&
      (this.searchCriteria.sort?.field !== this.sort.field ||
        this.searchCriteria.sort?.direction !== this.sort.direction)
    ) {
      if (this.isSorted == null) {
        this.isSorted = true;
        this.searchCriteria.sort = this.sort;
      } else if (this.isSorted) {
        this.isSorted = false;
        this.searchCriteria.sort = this.sort;
      } else if (!this.isSorted) {
        this.isSorted = null;
        this.searchCriteria.sort = undefined;
        this.tableTemplateComponent.reset();
      }
      this.search();
    }
  }

  onPageChange(value: PaginatorState): void {
    if (value.page !== undefined && value.rows !== undefined) {
      this.searchCriteria.page = {
        pageNumber: value.page,
        pageSize: value.rows
      };
      this.search();
    }
  }

  onSelectionChange(event: R[]): void {
    this.selectionChange.emit(event);
  }
}
