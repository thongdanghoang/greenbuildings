import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ToolboxComponent} from './components/toolbox/toolbox.component';

const routes: Routes = [
  {
    path: '',
    component: ToolboxComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DevRoutingModule {}
