import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {MessageService} from 'primeng/api';
import {filter, map, switchMap, takeUntil, tap} from 'rxjs';
import {UUID} from '../../../../../types/uuid';
import {AppRoutingConstants} from '../../../../app-routing.constant';
import {ApplicationConstant} from '../../../../application.constant';
import {SubscriptionAwareComponent} from '../../../core/subscription-aware.component';
import {ModalProvider} from '../../../shared/services/modal-provider';
import {PowerBiAuthority} from '../../models/power-bi-authority.dto';
import {PowerBiAccessTokenService} from '../../services/power-bi-access-token.service';
import {TranslateService} from '@ngx-translate/core';

@Component({
  selector: 'app-power-bi-access-token',
  templateUrl: './power-bi-access-token.component.html',
  styleUrl: './power-bi-access-token.component.css'
})
export class PowerBiAccessTokenComponent
  extends SubscriptionAwareComponent
  implements OnInit
{
  protected accessTokens: PowerBiAuthority[] = [];
  protected newestCreatedTokenId: UUID | null = null;
  protected newestCreatedTokenKey: string | null = null;
  protected mockScopes: string[] = [
    'gist, read:org, read:user, repo',
    'gist, read:org, read:user, repo, user:email, workflow',
    'codespace, copilot, delete:packages, delete_repo, gist, notifications, project, repo, user, workflow, write:discussion, write:packages',
    'admin:enterprise, admin:gpg_key, admin:org, admin:org_hook, admin:public_key, admin:repo_hook, admin:ssh_signing_key, audit_log, codespace, copilot, delete:packages, delete_repo, gist, notifications, project, repo, user, workflow, write:discussion, write:packages'
  ];

  constructor(
    private readonly powerBiService: PowerBiAccessTokenService,
    private readonly modalProvider: ModalProvider,
    private readonly messageService: MessageService,
    private readonly router: Router,
    private readonly translate: TranslateService
  ) {
    super();
  }

  ngOnInit(): void {
    const newestPowerBiAccessToken = sessionStorage.getItem(
      ApplicationConstant.NEWEST_POWER_BI_ACCESS_TOKEN_KEY
    );
    if (newestPowerBiAccessToken) {
      const token = JSON.parse(newestPowerBiAccessToken);
      this.newestCreatedTokenKey = token.apiKey;
      this.newestCreatedTokenId = token.id;
      sessionStorage.removeItem(
        ApplicationConstant.NEWEST_POWER_BI_ACCESS_TOKEN_KEY
      );
    }
    this.powerBiService
      .getMyAccessToken()
      .pipe(
        takeUntil(this.destroy$),
        filter(tokens => !!tokens),
        map(tokens =>
          tokens.map(
            (token: PowerBiAuthority): PowerBiAuthority => ({
              ...token,
              lastUsed: this.randomDate(new Date(2012, 0, 1), new Date()),
              scopes:
                this.mockScopes[
                  Math.floor(Math.random() * this.mockScopes.length)
                ]
            })
          )
        ),
        tap(tokens => {
          this.accessTokens = tokens;
        })
      )
      .subscribe();
  }

  navigateToDetail(id?: UUID): void {
    void this.router.navigate([
      '/',
      AppRoutingConstants.AUTH_PATH,
      AppRoutingConstants.SETTINGS,
      AppRoutingConstants.POWER_BI,
      AppRoutingConstants.ACCESS_TOKEN,
      id ?? 'new'
    ]);
  }

  get newestCreatedToken(): PowerBiAuthority | null {
    return (
      this.accessTokens.find(token => token.id === this.newestCreatedTokenId) ||
      null
    );
  }

  copyToClipboard(text: string): void {
    navigator.clipboard
      .writeText(text)
      .then(() => {
        this.messageService.add({
          severity: 'success',
          summary: 'Copied',
          detail: 'Token copied to clipboard'
        });
      })
      .catch(() => {
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to copy token'
        });
      });
  }

  // eslint-disable-next-line max-lines-per-function
  showDeleteConfirmDialog(id: UUID): void {
    this.modalProvider
      .showConfirm({
        message: this.translate.instant('powerBi.dialog.message'),
        header: this.translate.instant('powerBi.dialog.config'),
        icon: 'pi pi-exclamation-triangle',
        acceptIcon: 'none',
        acceptButtonStyleClass: 'p-button-danger',
        acceptLabel: this.translate.instant('powerBi.dialog.acceptLabel'),
        rejectIcon: 'none',
        rejectButtonStyleClass: 'p-button-text',
        rejectLabel: this.translate.instant('powerBi.dialog.reject')
      })
      .pipe(
        filter(confirmed => confirmed),
        switchMap(() => this.powerBiService.deleteAccessToken(id)),
        takeUntil(this.destroy$)
      )
      .subscribe({
        next: (): void => {
          this.messageService.add({
            severity: 'success',
            summary: this.translate.instant(
              'powerBi.messageService.success.summary'
            ),
            detail: this.translate.instant(
              'powerBi.messageService.success.detail'
            )
          });
          // Refresh the token list
          this.ngOnInit();
        },
        error: (): void => {
          this.messageService.add({
            severity: 'error',
            summary: this.translate.instant(
              'powerBi.messageService.error.summary'
            ),
            detail: this.translate.instant(
              'powerBi.messageService.error.detail'
            )
          });
        }
      });
  }

  private randomDate(start: Date, end: Date): Date {
    return new Date(
      start.getTime() + Math.random() * (end.getTime() - start.getTime())
    );
  }
}
