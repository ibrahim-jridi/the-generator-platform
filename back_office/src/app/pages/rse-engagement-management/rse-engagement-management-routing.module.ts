import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RseEngagementManagementComponent } from './rse-engagement-management.component';

const routes: Routes = [
  {
    path: '',
    component: RseEngagementManagementComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class RseEngagementManagementRoutingModule {}
