import {Component, OnInit} from '@angular/core';
import {InvitationResponse} from '@models/tenant';
import {TranslateService} from '@ngx-translate/core';
import {InvitationDTO, InvitationStatus} from '@models/enterprise';
import {InvitationService} from '@services/invitation.service';
import {ToastProvider} from '@shared/services/toast-provider';

@Component({
  selector: 'app-enterprise-invitation',
  templateUrl: './enterprise-invitation.component.html',
  styleUrl: './enterprise-invitation.component.css'
})
export class EnterpriseInvitationComponent implements OnInit {
  invitations: InvitationDTO[] = [];

  constructor(
    private readonly invitationService: InvitationService,
    public readonly translate: TranslateService,
    private readonly messageService: ToastProvider
  ) {}

  ngOnInit(): void {
    this.fetchPendingInvitation();
  }

  handleFetchInvitations(showSuccessMessage: boolean = false): void {
    this.invitationService
      .findAllPendingByEmail()
      .subscribe((invitations: InvitationDTO[]) => {
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
    });
  }

  rejectInvitation(invitation: InvitationDTO): void {
    const response: InvitationResponse = {
      id: invitation.id,
      status: InvitationStatus.REJECTED
    };
    this.invitationService.updateStatus(response).subscribe(() => {
      this.fetchPendingInvitation();
    });
  }
}
