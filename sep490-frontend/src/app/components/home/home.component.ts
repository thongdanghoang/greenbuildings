import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {AppRoutingConstants} from '../../app-routing.constant';
import {UserRole} from '@models/role-names';
import {EnterpriseUserDetails} from '@models/enterprise-user';
import {ApplicationService, UserData} from '@services/application.service';
import {UserService} from '@services/user.service';
import {switchMap, takeUntil} from 'rxjs';
import {SubscriptionAwareComponent} from '@shared/directives/subscription-aware.component';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent extends SubscriptionAwareComponent implements OnInit {
  userData?: UserData;
  userRoles: UserRole[] = [];
  userInfo?: EnterpriseUserDetails;
  emailVerified: boolean = false;

  protected readonly UserRole = UserRole;
  protected readonly AppRoutingConstants = AppRoutingConstants;

  constructor(
    private readonly applicationService: ApplicationService,
    private readonly userService: UserService,
    private readonly activeRoute: ActivatedRoute
  ) {
    super();
  }

  ngOnInit(): void {
    this.activeRoute.url.subscribe(url => {
      if (url.some(segment => segment.path.includes('triggerLogin'))) {
        this.applicationService.login();
      } else {
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
    });
  }
}
