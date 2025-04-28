import {Component} from '@angular/core';
import {MessageService} from 'primeng/api';

@Component({
  selector: 'app-enterprise-profile',
  templateUrl: './enterprise-profile.component.html',
  styleUrl: './enterprise-profile.component.css',
  providers: [MessageService]
})
export class EnterpriseProfileComponent {}
