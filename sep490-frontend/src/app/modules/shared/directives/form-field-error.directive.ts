import {
  Directive,
  ElementRef,
  OnDestroy,
  OnInit,
  Optional
} from '@angular/core';
import {ValidationErrors} from '@angular/forms';
import {TranslateService} from '@ngx-translate/core';
import {takeUntil, tap} from 'rxjs';
import {SubscriptionAwareComponent} from '@shared/directives/subscription-aware.component';
import {TranslateParamsPipe} from '../pipes/translate-params.pipe';
import {ErrorMessagesDirective} from './error-messages.directive';

@Directive({
  selector: '[formFieldError]'
})
export class FormFieldErrorDirective
  extends SubscriptionAwareComponent
  implements OnInit, OnDestroy
{
  private readonly pipe: TranslateParamsPipe;
  private errors: ValidationErrors | null = null;

  constructor(
    @Optional() private readonly control: ErrorMessagesDirective,
    private readonly el: ElementRef,
    translate: TranslateService
  ) {
    super();
    this.pipe = new TranslateParamsPipe(translate);
    if (this.control) {
      translate.onLangChange
        .pipe(takeUntil(this.destroy$))
        .subscribe((): void => this.showErrors(this.errors || {}));
    }
  }

  ngOnInit(): void {
    if (this.control) {
      this.control.errors$
        .pipe(
          tap(errors => (this.errors = errors)),
          takeUntil(this.destroy$)
        )
        .subscribe(errors => {
          if (
            errors &&
            !(
              this.control.ngControl?.untouched &&
              this.control.ngControl?.pristine
            )
          ) {
            this.showErrors(errors);
          } else {
            this.showErrors({});
          }
        });
    }
  }

  private showErrors(errors: ValidationErrors): void {
    this.el.nativeElement.innerText = Object.keys(errors)
      .map(key => this.pipe.transform(`validation.${key}`, errors[key]))
      .join('\n');
  }
}
