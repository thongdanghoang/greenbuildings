import {Injectable, Type} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {SubscriptionAwareComponent} from '@shared/directives/subscription-aware.component';
import {Confirmation} from 'primeng/api';
import {DialogService} from 'primeng/dynamicdialog';
import {Observable} from 'rxjs';
import {ConfirmDialogComponent} from '../components/dialog/confirm-dialog/confirm-dialog.component';

@Injectable()
export class ModalProvider extends SubscriptionAwareComponent {
  constructor(
    private readonly dialogService: DialogService,
    private readonly translate: TranslateService
  ) {
    super();
  }

  showConfirm(params: Confirmation): Observable<boolean> {
    return this.dialogService.open(ConfirmDialogComponent, {
      header: params.header,
      modal: true,
      data: params
    }).onClose;
  }

  openDynamicDialog(componentType: Type<any>, data?: any): Observable<boolean> {
    return this.dialogService.open(componentType, {
      closeOnEscape: true,
      showHeader: false,
      modal: true,
      data
    }).onClose;
  }

  showDefaultConfirm(message: string = 'common.defaultConfirmMessage'): Observable<boolean> {
    return this.dialogService.open(ConfirmDialogComponent, {
      modal: true,
      header: this.translate.instant('common.confirmHeader'),
      data: {
        message: this.translate.instant(message),
        icon: 'pi pi-info-circle',
        acceptButtonStyleClass: 'p-button-danger p-button-text min-w-20',
        rejectButtonStyleClass: 'p-button-contrast p-button-text min-w-20',
        acceptIcon: 'none',
        acceptLabel: this.translate.instant('common.accept'),
        rejectIcon: 'none',
        rejectLabel: this.translate.instant('common.reject')
      }
    }).onClose;
  }

  showError(): void {
    // TODO: Implement error dialog
  }

  showDirtyCheckConfirm(params: Confirmation): Observable<boolean> {
    return this.dialogService.open(ConfirmDialogComponent, {
      header: params.header,
      modal: true,
      data: {
        ...params,
        icon: 'pi pi-info-circle',
        acceptButtonStyleClass: 'p-button-danger p-button-text min-w-20',
        rejectButtonStyleClass: 'p-button-contrast p-button-text min-w-20',
        acceptIcon: 'none',
        rejectIcon: 'none'
      }
    }).onClose;
  }
}
