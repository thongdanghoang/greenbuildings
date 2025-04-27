import {Injectable, inject} from '@angular/core';
import {ControlContainer} from '@angular/forms';
import {TranslateService} from '@ngx-translate/core';
import {Observable, of} from 'rxjs';
import {SubscriptionAwareComponent} from '../../../core/subscription-aware.component';
import {ModalProvider} from '../../services/modal-provider';
import {GlobalEventsService, WINDOW} from './global-events.service';

@Injectable({
  providedIn: 'root'
})
export class UnsavedChangesService extends SubscriptionAwareComponent {
  public isActive = true;
  private readonly controlContainer: Record<string, ControlContainer> = {};
  private readonly window = inject(WINDOW);

  constructor(
    private readonly globalEventsService: GlobalEventsService,
    private readonly translate: TranslateService,
    private readonly modalProvider: ModalProvider
  ) {
    super();
    globalEventsService.beforeUnload$.subscribe(event => this.onUnload(event));
  }

  watch(formId: string, form: ControlContainer): void {
    this.controlContainer[formId] = form;
  }

  unWatch(formId: string): void {
    delete this.controlContainer[formId];
  }

  canDeactivate(): Observable<boolean> {
    return this.ignoreChanges();
  }

  ignoreChanges(formIds?: string[]): Observable<boolean> {
    return this.hasPendingChanges(formIds) ? this.confirm() : of(true);
  }

  private onUnload(event: BeforeUnloadEvent): string | null {
    if (this.hasPendingChanges()) {
      const confirmationMessage = this.message();
      event.returnValue = confirmationMessage;
      return confirmationMessage;
    }
    return null;
  }

  private hasPendingChanges(
    ids: string[] = Object.keys(this.controlContainer)
  ): boolean {
    const includesPendingChanges =
      Object.keys(this.controlContainer).filter(
        formId => ids.includes(formId) && this.controlContainer[formId].dirty
      ).length > 0;
    return this.isActive && includesPendingChanges;
  }

  private confirm(): Observable<boolean> {
    return this.modalProvider.showDirtyCheckConfirm({
      message: this.message(),
      header: this.translate.instant('validation.unsavedChanges.title'),
      acceptLabel: this.translate.instant('validation.unsavedChanges.discard'),
      rejectLabel: this.translate.instant('validation.unsavedChanges.cancel')
    });
  }

  private message(): string {
    return this.translate.instant('validation.unsavedChanges.message');
  }
}
