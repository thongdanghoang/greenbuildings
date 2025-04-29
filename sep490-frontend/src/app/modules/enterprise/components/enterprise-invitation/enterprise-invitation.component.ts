import {Component, OnInit} from '@angular/core';
import {InvitationResponse} from '@models/tenant';
import {TranslateService} from '@ngx-translate/core';
import {InvitationDTO, InvitationStatus} from '@models/enterprise';
import {InvitationService} from '@services/invitation.service';
import {SubscriptionAwareComponent} from '@shared/directives/subscription-aware.component';
import {ModalProvider} from '@shared/services/modal-provider';
import {ToastProvider} from '@shared/services/toast-provider';
import {takeUntil} from 'rxjs';

@Component({
  selector: 'app-enterprise-invitation',
  templateUrl: './enterprise-invitation.component.html',
  styleUrl: './enterprise-invitation.component.css'
})
export class EnterpriseInvitationComponent extends SubscriptionAwareComponent implements OnInit {
  invitations: InvitationDTO[] = [];

  constructor(
    private readonly msgService: ToastProvider,
    private readonly modalProvider: ModalProvider,
    private readonly invitationService: InvitationService,
    public readonly translate: TranslateService,
    private readonly messageService: ToastProvider
  ) {
    super();
  }

  ngOnInit(): void {
    this.fetchPendingInvitation();
  }

  handleFetchInvitations(showSuccessMessage: boolean = false): void {
    this.invitationService.findAllPendingByEmail().subscribe((invitations: InvitationDTO[]) => {
      this.invitations = invitations;
      if (showSuccessMessage) {
        this.messageService.success({
          summary: this.translate.instant('common.success')
        });
      }
    });
  }

  fetchPendingInvitation(): void {
    this.handleFetchInvitations();
  }

  refetchPendingInvitation(): void {
    this.handleFetchInvitations(true);
  }

  acceptInvitation(invitation: InvitationDTO): void {
    const response: InvitationResponse = {
      id: invitation.id,
      status: InvitationStatus.ACCEPTED
    };
    this.invitationService.updateStatus(response).subscribe(() => {
      this.fetchPendingInvitation();
      this.msgService.success({
        summary: this.translate.instant('common.success'),
        detail: this.translate.instant('common.saveSuccess')
      });
    });
  }

  rejectInvitation(invitation: InvitationDTO): void {
    this.modalProvider
      .showConfirm({
        message: this.translate.instant('sentInvitation.rejectConfirm'),
        header: this.translate.instant('common.confirmHeader'),
        icon: 'pi pi-info-circle',
        acceptButtonStyleClass: 'p-button-danger p-button-text min-w-20',
        rejectButtonStyleClass: 'p-button-contrast p-button-text min-w-20',
        acceptIcon: 'none',
        acceptLabel: this.translate.instant('common.accept'),
        rejectIcon: 'none',
        rejectLabel: this.translate.instant('common.reject')
      })
      .pipe(takeUntil(this.destroy$))
      .subscribe((result: boolean): void => {
        if (result) {
          const response: InvitationResponse = {
            id: invitation.id,
            status: InvitationStatus.REJECTED
          };
          this.invitationService.updateStatus(response).subscribe(() => {
            this.fetchPendingInvitation();
            this.msgService.success({
              summary: this.translate.instant('common.success'),
              detail: this.translate.instant('sentInvitation.reject')
            });
          });
        }
      });
  }
}
