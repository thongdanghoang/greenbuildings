import {Directive, Input, OnChanges, OnDestroy, OnInit} from '@angular/core';
import {ControlContainer} from '@angular/forms';
import {UnsavedChangesService} from './unsaved-changes.service';

@Directive({
  selector: '[unsavedChanges]'
  // host: {class: 'unsaved-changes'}
})
export class UnsavedChangesDirective implements OnChanges, OnInit, OnDestroy {
  @Input() id!: string;
  @Input() isActive = true;

  constructor(
    private readonly unsavedChangesService: UnsavedChangesService,
    private readonly form: ControlContainer
  ) {}

  ngOnChanges(): void {
    if (this.id) {
      if (this.isActive) {
        this.unsavedChangesService.watch(this.id, this.form);
      } else {
        this.unsavedChangesService.unWatch(this.id);
      }
    }
  }

  ngOnInit(): void {
    if (!this.id) {
      throw new Error('unsavedChanges directive needs an "id" attribute.');
    }
  }

  ngOnDestroy(): void {
    this.unsavedChangesService.unWatch(this.id);
  }
}
