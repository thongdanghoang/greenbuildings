import {Directive, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {MessageService} from 'primeng/api';
import {Observable, of, takeUntil} from 'rxjs';
import {SubscriptionAwareComponent} from '../../core/subscription-aware.component';
import {SearchCriteriaDto, SearchResultDto} from '../models/base-models';
import {TranslateParamsPipe} from '../pipes/translate-params.pipe';

@Directive()
export abstract class AbstractSearchComponent<
    C,
    R,
    W extends SearchResultDto<R> = SearchResultDto<R>
  >
  extends SubscriptionAwareComponent
  implements OnInit
{
  @Input() criteria: C | undefined;
  @Input({required: true}) fetch!: (
    criteria: SearchCriteriaDto<C>
  ) => Observable<W>;
  @Input() filterValidator: ((criteria: C) => Observable<string[]>) | undefined;
  @Output() readonly searchCompleted: EventEmitter<W> = new EventEmitter<W>();

  searchResult: W | undefined;
  searchCriteria!: SearchCriteriaDto<C>; // initialize in ngOnInit

  private readonly pipe!: TranslateParamsPipe;

  protected constructor(
    protected readonly translate: TranslateService,
    protected readonly messageService: MessageService
  ) {
    super();
    this.pipe = new TranslateParamsPipe(this.translate);
  }

  ngOnInit(): void {
    this.initSearchDto();
    if (!this.filterValidator) {
      this.filterValidator = (): Observable<string[]> => of([]);
    }
  }

  submit(): void {
    if (this.criteria && this.filterValidator) {
      this.searchCriteria.criteria = this.criteria;
      this.filterValidator(this.searchCriteria.criteria)
        .pipe(takeUntil(this.destroy$))
        .subscribe(err => {
          if (err && err.length > 0) {
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: err.map(e => this.pipe.transform(e)).join('\n')
            });
          } else {
            this.search();
            this.resetPage();
          }
        });
      return;
    }
    this.search();
  }

  search(): void {
    this.beforeSearch();
    this.fetch(this.searchCriteria)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (result: W): void => {
          this.searchResult = result;
          this.afterSearch();
        },
        error: error => {
          if (error.error?.i18nKey) {
            const title = this.pipe.transform(
              `http.error.status.${error?.status}.title`
            );
            const message = this.pipe.transform(error.error.i18nKey);
            this.messageService.add({
              severity: 'error',
              summary: title,
              detail: message
            });
          } else {
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: JSON.stringify(error)
            });
          }
        },
        complete: (): void => this.searchCompleted.emit(this.searchResult)
      });
  }

  protected beforeSearch(): void {
    // Do nothing on super class
  }

  protected afterSearch(): void {
    // Do nothing on super class
  }

  protected abstract initSearchDto(): void;

  protected resetPage(): void {
    this.searchCriteria.page.pageNumber = 0;
  }
}
