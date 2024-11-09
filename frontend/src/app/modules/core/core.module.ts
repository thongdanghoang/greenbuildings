import {CommonModule} from '@angular/common';
import {NgModule, Optional, SkipSelf} from '@angular/core';
import {ThemeService} from './services/theme.service';
import {throwIfAlreadyLoaded} from './module-import-guard';
import {ApplicationService} from './services/application.service';


@NgModule({
  declarations: [],
  imports: [
    CommonModule
  ],
  providers: [ApplicationService, ThemeService]
})
export class CoreModule {
  constructor(@Optional() @SkipSelf() parentModule: CoreModule) {
    throwIfAlreadyLoaded(parentModule, 'CoreModule');
  }
}
