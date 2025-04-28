import {Component, EventEmitter, Output} from '@angular/core';
import {DynamicDialogConfig, DynamicDialogRef} from 'primeng/dynamicdialog';
import {Confirmation} from 'primeng/api';
import {Nullable} from 'primeng/ts-helpers';
import {DomSanitizer} from '@angular/platform-browser';

/**
 * Type of the confirm event.
 */
export enum ConfirmEventType {
  ACCEPT,
  REJECT,
  CANCEL
}

@Component({
  selector: 'app-confirm-dialog',
  templateUrl: './confirm-dialog.component.html',
  styleUrl: './confirm-dialog.component.css'
})
export class ConfirmDialogComponent {
  confirmationOptions: Nullable<Confirmation>;

  /**
   * Callback to invoke when dialog is hidden.
   * @param {ConfirmEventType} enum - Custom confirm event.
   * @group Emits
   */
  @Output() readonly hide: EventEmitter<ConfirmEventType> = new EventEmitter<ConfirmEventType>();

  constructor(
    private readonly configs: DynamicDialogConfig,
    protected readonly sanitizer: DomSanitizer,
    private readonly ref: DynamicDialogRef
  ) {
    this.confirmationOptions = this.configs.data;
  }

  accept(): void {
    if (this.confirmationOptions?.acceptEvent) {
      this.confirmationOptions.acceptEvent.emit();
    }

    this.hideByConfirmEventType(ConfirmEventType.ACCEPT);
    this.ref.close(true);
  }

  reject(): void {
    if (this.confirmationOptions?.rejectEvent) {
      this.confirmationOptions.rejectEvent.emit(ConfirmEventType.REJECT);
    }

    this.hideByConfirmEventType(ConfirmEventType.REJECT);
    this.ref.close(false);
  }

  get acceptButtonLabel(): string {
    return this.option('acceptLabel') ?? 'TranslationKeys.ACCEPT';
  }

  get rejectButtonLabel(): string {
    return this.option('rejectLabel') ?? 'TranslationKeys.REJECT';
  }

  option(name: string): any {
    const source: {[key: string]: any} = this.confirmationOptions || this;
    if (Object.hasOwn(source, name)) {
      return source[name];
    }
    return undefined;
  }

  private hideByConfirmEventType(type?: ConfirmEventType): void {
    this.hide.emit(type);
    this.confirmationOptions = null;
  }
}
