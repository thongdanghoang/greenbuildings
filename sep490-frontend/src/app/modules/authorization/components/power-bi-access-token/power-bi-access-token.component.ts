import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {environment} from '@environment';
import {PowerBiAuthority} from '@models/power-bi-access-token';
import {TranslateService} from '@ngx-translate/core';
import {ApplicationService} from '@services/application.service';
import {PowerBiAccessTokenService} from '@services/power-bi-access-token.service';
import {SubscriptionAwareComponent} from '@shared/directives/subscription-aware.component';
import {ModalProvider} from '@shared/services/modal-provider';
import {ToastProvider} from '@shared/services/toast-provider';
import {filter, switchMap, takeUntil, tap} from 'rxjs';
import {UUID} from '../../../../../types/uuid';
import {AppRoutingConstants} from '../../../../app-routing.constant';
import {ApplicationConstant} from '../../../../application.constant';

@Component({
  selector: 'app-power-bi-access-token',
  templateUrl: './power-bi-access-token.component.html',
  styleUrl: './power-bi-access-token.component.css'
})
export class PowerBiAccessTokenComponent extends SubscriptionAwareComponent implements OnInit {
  protected accessTokens: PowerBiAuthority[] = [];
  protected newestCreatedTokenId: UUID | null = null;
  protected newestCreatedTokenKey: string | null = null;
  protected userToken: string = '';

  constructor(
    private readonly powerBiService: PowerBiAccessTokenService,
    private readonly modalProvider: ModalProvider,
    private readonly messageService: ToastProvider,
    private readonly router: Router,
    private readonly translate: TranslateService,
    private readonly applicationService: ApplicationService
  ) {
    super();
  }

  ngOnInit(): void {
    this.initializeData();
  }

  get isMobile(): boolean {
    return this.applicationService.isMobile();
  }

  get newestPowerBiAccessToken(): string | null {
    return sessionStorage.getItem(ApplicationConstant.NEWEST_POWER_BI_ACCESS_TOKEN_KEY);
  }

  get newestCreatedToken(): PowerBiAuthority | null {
    return this.accessTokens.find(token => token.id === this.newestCreatedTokenId) || null;
  }

  initializeData(): void {
    const newestPowerBiAccessToken = this.newestPowerBiAccessToken;
    if (newestPowerBiAccessToken) {
      const token = JSON.parse(newestPowerBiAccessToken);
      this.newestCreatedTokenKey = token.apiKey;
      this.newestCreatedTokenId = token.id;
      sessionStorage.removeItem(ApplicationConstant.NEWEST_POWER_BI_ACCESS_TOKEN_KEY);
    }
    this.powerBiService
      .getMyAccessToken()
      .pipe(
        takeUntil(this.destroy$),
        filter(tokens => !!tokens),
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

  copyToClipboard(text: string | null): void {
    if (text) {
      navigator.clipboard
        .writeText(text)
        .then(() => {
          this.messageService.success({
            summary: 'Copied',
            detail: 'Data copied to clipboard'
          });
        })
        .catch(() => {
          this.messageService.businessError({
            summary: 'Error',
            detail: 'Failed to copy data'
          });
        });
    }
  }

  goToLink(link: string): void {
    window.open(link, '_blank');
  }

  getUrl(userToken: string): string {
    const api = environment.enterpriseUrl;
    return `${api}/power-bi/${userToken}/report`;
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
          this.messageService.success({
            summary: this.translate.instant('powerBi.messageService.success.summary'),
            detail: this.translate.instant('powerBi.messageService.success.detail')
          });
          this.initializeData();
          this.clearNewestPowerBiAccessToken();
        },
        error: (): void => {
          this.messageService.success({
            summary: this.translate.instant('powerBi.messageService.error.summary'),
            detail: this.translate.instant('powerBi.messageService.error.detail')
          });
        }
      });
  }

  private clearNewestPowerBiAccessToken(): void {
    this.newestCreatedTokenId = null;
    this.newestCreatedTokenKey = null;
  }
}
