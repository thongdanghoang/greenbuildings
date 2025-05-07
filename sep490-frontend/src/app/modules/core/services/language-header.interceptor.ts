import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {Observable} from 'rxjs';

@Injectable()
export class LanguageHeaderInterceptor implements HttpInterceptor {
  constructor(private readonly translate: TranslateService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const currentLang = this.translate.currentLang || this.translate.getDefaultLang() || 'en';

    const modifiedReq = req.clone({
      setHeaders: {
        'Accept-Language': currentLang
      }
    });

    return next.handle(modifiedReq);
  }
}
