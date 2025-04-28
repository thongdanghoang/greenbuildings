import {AfterViewInit, ContentChild, Directive, ElementRef, OnDestroy, Optional, Renderer2} from '@angular/core';
import {FormGroupDirective, NgControl, NgForm, ValidationErrors} from '@angular/forms';
import {Observable, Subject, merge, takeUntil} from 'rxjs';
import {SubscriptionAwareComponent} from '@shared/directives/subscription-aware.component';

@Directive({
  selector: '[errorMessages]'
})
export class ErrorMessagesDirective extends SubscriptionAwareComponent implements OnDestroy, AfterViewInit {
  @ContentChild(NgControl) ngControl?: NgControl;
  readonly errors$: Observable<ValidationErrors | null>;
  private readonly errors = new Subject<ValidationErrors | null>();
  private readonly form: NgForm | FormGroupDirective;

  private readonly INVALID_STYLE = 'ng-invalid';
  private readonly DIRTY_STYLE = 'ng-dirty';

  constructor(
    @Optional() ngForm: NgForm,
    @Optional() formGroupDirective: FormGroupDirective,
    private readonly el: ElementRef,
    private readonly renderer: Renderer2
  ) {
    super();
    this.errors$ = this.errors.asObservable();
    this.form = ngForm || formGroupDirective;

    if (!this.form) {
      throw new Error('The ErrorMessagesDirective needs to be either within a NgForm or a FormGroupDirective!');
    }
  }

  ngAfterViewInit(): void {
    if (this.ngControl) {
      this.errors.next(this.ngControl.errors); // because 1st statusChange occurs before ngAfterViewInit
      merge(this.formSubmitEvent(), this.controlStatusChanges())
        .pipe(takeUntil(this.destroy$))
        .subscribe((): void => {
          this.errors.next(this.ngControl?.errors ?? null);
          this.updateDirtyStyle(this.ngControl?.errors);
        });
    } else {
      console.error('No NgControl found for ErrorMessagesDirective');
    }
  }

  formSubmitEvent(): any {
    return this.form.ngSubmit;
  }

  controlStatusChanges(): any {
    return this.ngControl?.statusChanges;
  }

  override ngOnDestroy(): void {
    this.errors.complete();
    super.ngOnDestroy();
  }

  private updateDirtyStyle(errors: ValidationErrors | null | undefined): void {
    const element = this.findFormControlElement();
    if (element) {
      if (errors && !(this.ngControl?.untouched && this.ngControl?.pristine)) {
        this.renderer.addClass(element, this.INVALID_STYLE);
        this.renderer.addClass(element, this.DIRTY_STYLE);
      } else {
        this.renderer.removeClass(element, this.INVALID_STYLE);
        this.renderer.removeClass(element, this.DIRTY_STYLE);
      }
    }
  }

  private findFormControlElement(): HTMLElement | null {
    const nativeElement = this.el.nativeElement;
    return nativeElement.querySelector('[formControlName]');
  }
}
