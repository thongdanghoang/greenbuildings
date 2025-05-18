import {Injectable} from '@angular/core';
import {SubscriptionAwareComponent} from '@shared/directives/subscription-aware.component';
import {MessageService, ToastMessageOptions} from 'primeng/api';

@Injectable()
export class ToastProvider extends SubscriptionAwareComponent {
  private readonly defaultLife: number = 2000;

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
