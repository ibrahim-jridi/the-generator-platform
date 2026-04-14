import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { StartOfActivityComponent } from './start-of-activity.component';

const routes: Routes = [
  {
    path: '',
    component: StartOfActivityComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class StartOfActivityRoutingModule {}
