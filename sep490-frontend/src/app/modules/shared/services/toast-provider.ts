import {Injectable} from '@angular/core';
import {MessageService, ToastMessageOptions} from 'primeng/api';
import {SubscriptionAwareComponent} from '../../core/subscription-aware.component';

@Injectable()
export class ToastProvider extends SubscriptionAwareComponent {
  private readonly defaultLife = 1000;

  constructor(private readonly messageService: MessageService) {
    super();
  }

  businessError(message: ToastMessageOptions): void {
    return this.messageService.add({
      ...message,
      severity: 'error',
      life: this.defaultLife
    });
  }

  technicalError(message: ToastMessageOptions): void {
    return this.messageService.add({
      ...message,
      severity: 'error',
      sticky: true
    });
  }

  success(message: ToastMessageOptions): void {
    return this.messageService.add({
      ...message,
      severity: 'success',
      life: this.defaultLife
    });
  }

  warn(message: ToastMessageOptions): void {
    return this.messageService.add({
      ...message,
      severity: 'warn',
      life: this.defaultLife
    });
  }

  info(message: ToastMessageOptions): void {
    return this.messageService.add({
      ...message,
      severity: 'info',
      life: this.defaultLife
    });
  }
}
