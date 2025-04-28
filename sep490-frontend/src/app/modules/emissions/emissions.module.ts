import {NgModule} from '@angular/core';
import {SharedModule} from '@shared/shared.module';

import {EmissionsRoutingModule} from './emissions-routing.module';
import {EmissionsComponent} from './emissions.component';

@NgModule({
  declarations: [EmissionsComponent],
  imports: [SharedModule, EmissionsRoutingModule]
})
export class EmissionsModule {}
