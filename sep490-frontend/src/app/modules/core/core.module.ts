import {NgModule, Optional, SkipSelf} from '@angular/core';
import {throwIfAlreadyLoaded} from './module-import-guard';
import {CommonModule} from '@angular/common';
import {ThemeService} from './services/theme.service';
import {ApplicationService} from './services/application.service';
import {MessageService} from 'primeng/api';

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
