import {CommonModule} from '@angular/common';
import {NgModule, Optional, SkipSelf} from '@angular/core';
import {MessageService} from 'primeng/api';
import {throwIfAlreadyLoaded} from './module-import-guard';
import {ApplicationService} from './services/application.service';
import {ThemeService} from './services/theme.service';

@NgModule({
  declarations: [],
  imports: [CommonModule],
  providers: [ApplicationService, ThemeService, MessageService]
})
export class CoreModule {
  constructor(@Optional() @SkipSelf() parentModule: CoreModule) {
    throwIfAlreadyLoaded(parentModule, 'CoreModule');
  }
}
