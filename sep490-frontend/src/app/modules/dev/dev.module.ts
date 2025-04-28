import {NgModule} from '@angular/core';
import {ToolboxComponent} from './components/toolbox/toolbox.component';
import {DevRoutingModule} from './dev-routing.module';
import {SharedModule} from '../shared/shared.module';

@NgModule({
  declarations: [ToolboxComponent],
  imports: [SharedModule, DevRoutingModule]
})
export class DevModule {}
