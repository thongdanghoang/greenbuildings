import {Component, OnInit} from '@angular/core';
import {InvitationDTO, InvitationStatus} from '../../models/enterprise.dto';
import {
  InvitationResponse,
  InvitationService
} from '../../services/invitation.service';

@Component({
  selector: 'app-enterprise-invitation',
  templateUrl: './enterprise-invitation.component.html',
  styleUrl: './enterprise-invitation.component.css'
})
export class EnterpriseInvitationComponent implements OnInit {
  invitations: InvitationDTO[] = [];

  constructor(private readonly invitationService: InvitationService) {}

  ngOnInit(): void {
    this.fetchPendingInvitation();
  }

  fetchPendingInvitation(): void {
    this.invitationService
      .findAllPendingByEmail()
      .subscribe((invitations: InvitationDTO[]) => {
        this.invitations = invitations;
      });
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
