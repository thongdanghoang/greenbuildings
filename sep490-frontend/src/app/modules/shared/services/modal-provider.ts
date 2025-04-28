import {Injectable, OnDestroy} from '@angular/core';
import {Confirmation} from 'primeng/api';
import {DialogService, DynamicDialogRef} from 'primeng/dynamicdialog';
import {Observable} from 'rxjs';
import {SubscriptionAwareComponent} from '@shared/directives/subscription-aware.component';
import {ConfirmDialogComponent} from '../components/dialog/confirm-dialog/confirm-dialog.component';

@Injectable()
export class ModalProvider
  extends SubscriptionAwareComponent
  implements OnDestroy
{
  ref: DynamicDialogRef | undefined;

  constructor(private readonly dialogService: DialogService) {
    super();
  }

  showConfirm(params: Confirmation): Observable<boolean> {
    this.ref = this.dialogService.open(ConfirmDialogComponent, {
      header: params.header,
      modal: true,
      data: params
    });
    return this.ref.onClose;
  }

  showError(): void {
    // TODO: Implement error dialog
  }

  showDirtyCheckConfirm(params: Confirmation): Observable<boolean> {
    this.ref = this.dialogService.open(ConfirmDialogComponent, {
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
    });
    return this.ref.onClose;
  }

  override ngOnDestroy(): void {
    if (this.ref) {
      this.ref.close();
    }
    super.ngOnDestroy();
  }
}
