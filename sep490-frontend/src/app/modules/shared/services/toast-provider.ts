import {Injectable} from '@angular/core';
import {MessageService, ToastMessageOptions} from 'primeng/api';
import {SubscriptionAwareComponent} from '@shared/directives/subscription-aware.component';

@Injectable()
export class ToastProvider extends SubscriptionAwareComponent {
  private readonly defaultLife = 200;

  constructor(private readonly messageService: MessageService) {
    super();
  }

  businessError(message: ToastMessageOptions): void {
    return this.messageService.add({
      ...message,
      severity: 'error',
      life: 3000
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
