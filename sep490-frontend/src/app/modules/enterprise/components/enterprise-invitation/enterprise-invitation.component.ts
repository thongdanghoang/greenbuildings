import {Component, OnInit} from '@angular/core';
import {InvitationService} from '../../services/invitation.service';
import {InvitationDTO} from '../../models/enterprise.dto';

@Component({
  selector: 'app-enterprise-invitation',
  templateUrl: './enterprise-invitation.component.html',
  styleUrl: './enterprise-invitation.component.css'
})
export class EnterpriseInvitationComponent implements OnInit {
  invitations: InvitationDTO[] = [];

  constructor(private readonly invitationService: InvitationService) {}

  ngOnInit(): void {
    this.invitationService
      .findAllPendingByEmail()
      .subscribe((invitations: InvitationDTO[]) => {
        this.invitations = invitations;
      });
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  acceptInvitation(invitation: InvitationDTO): void {}

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  rejectInvitation(invitation: InvitationDTO): void {}
}
