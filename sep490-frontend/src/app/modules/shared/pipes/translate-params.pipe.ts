import {Pipe, PipeTransform} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';

@Pipe({
  name: 'appTranslateParam',
  // eslint-disable-next-line @angular-eslint/no-pipe-impure
  pure: false
})
export class TranslateParamsPipe implements PipeTransform {
  constructor(private readonly translate: TranslateService) {}
  transform(value: string, params?: any): string {
    return this.translate.instant(value, typeof params === 'object' ? this.translateParams(params) : undefined);
  }

  private translateParams(
    params: Record<string, string | number | boolean>
  ): Record<string, string | number | boolean> {
    return Object.keys(params).reduce(
      (parameters, key) => ({
        ...parameters,
        [key]: typeof params[key] === 'string' ? this.translate.instant(params[key]) : params[key]
      }),
      {}
    );
  }
}
