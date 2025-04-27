import {Injectable} from '@angular/core';
import {CanDeactivate} from '@angular/router';
import {UnsavedChangesService} from './unsaved-changes.service';
import {Observable} from 'rxjs';

@Injectable({providedIn: 'root'})
export class UnsavedChangesGuard implements CanDeactivate<any> {
  constructor(private readonly unsavedChangesService: UnsavedChangesService) {}

  // NOTE: Currently this guard has to be added on every route where it's used
  // If this Issue: https://github.com/angular/angular/issues/11836  is resolved, we can rewrite it
  canDeactivate(): Observable<boolean> {
    return this.unsavedChangesService.canDeactivate();
  }
}
