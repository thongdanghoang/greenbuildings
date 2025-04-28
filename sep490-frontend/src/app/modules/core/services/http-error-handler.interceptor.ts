import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest
} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {Observable, catchError, throwError} from 'rxjs';
import {BusinessErrorParam} from '@shared/models/base-models';
import {ToastProvider} from '@shared/services/toast-provider';

@Injectable()
export class HttpErrorHandlerInterceptor implements HttpInterceptor {
  constructor(
    private readonly messageService: ToastProvider,
    private readonly translateService: TranslateService
  ) {}

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError((error: any) => {
        if (error.error && error.error.errorType === 'TECHNICAL') {
          this.handleTechnicalErrorException(error);
        }
        if (error.error && error.error.errorType === 'BUSINESS') {
          this.handleBusinessErrorException(error);
        }
        return throwError(() => error);
      })
    );
  }

  private handleTechnicalErrorException(error: any): void {
    const message = this.translateService.instant(
      'common.error.technicalServerError',
      {correlationId: error.error.correlationId}
    );
    this.messageService.technicalError({
      summary: this.translateService.instant('common.error.unexpectedError'),
      detail: message
    });
    error.error.errorNotified = true;
  }

  private handleBusinessErrorException(error: any): void {
    if (!error.error.field) {
      this.messageService.businessError({
        summary: this.translateService.instant('common.error.businessError'),
        detail: this.translateService.instant(
          `validation.${error.error.i18nKey}`,
          this.convertErrorParams(error.error.args)
        )
      });
    }
    error.error.errorNotified = true;
  }

  private convertErrorParams(params: BusinessErrorParam[]): {
    [key: string]: string;
  } {
    return params?.reduce(
      (result, arg: BusinessErrorParam) => {
        result[arg.key] = arg.value;
        return result;
      },
      {} as {[key: string]: string}
    );
  }
}
