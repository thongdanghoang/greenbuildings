import {Component, OnInit} from '@angular/core';
import {AppRoutingConstants} from '../../app-routing.constant';
import {UserRole} from '../../modules/authorization/enums/role-names';
import {EnterpriseUserDetails} from '../../modules/authorization/models/enterprise-user';
import {
  ApplicationService,
  UserData
} from '../../modules/core/services/application.service';
import {UserService} from '../../services/user.service';
import {switchMap, takeUntil} from 'rxjs';
import {SubscriptionAwareComponent} from '../../modules/core/subscription-aware.component';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent
  extends SubscriptionAwareComponent
  implements OnInit
{
  userData?: UserData;
  userRoles: UserRole[] = [];
  userInfo?: EnterpriseUserDetails;
  emailVerified: boolean = false;

  protected readonly UserRole = UserRole;
  protected readonly AppRoutingConstants = AppRoutingConstants;

  constructor(
    private readonly applicationService: ApplicationService,
    private readonly userService: UserService
  ) {
    super();
  }

  ngOnInit(): void {
    this.applicationService.UserData.pipe(
      takeUntil(this.destroy$),
      switchMap(userData => {
        this.userData = userData;
        return this.applicationService.getUserRoles();
      }),
      switchMap(roles => {
        this.userRoles = roles;
        return this.userService.getUserInfo();
      }),
      switchMap(userInfo => {
        this.userInfo = userInfo;
        return this.applicationService.isEmailVerified();
      })
    ).subscribe(verified => {
      this.emailVerified = verified;
    });
  }
}
