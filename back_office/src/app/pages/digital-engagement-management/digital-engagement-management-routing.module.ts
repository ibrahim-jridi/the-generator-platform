import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DigitalEngagementManagementComponent } from './digital-engagement-management.component';

const routes: Routes = [
  {
    path: '',
    component: DigitalEngagementManagementComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DigitalEngagementManagementRoutingModule {}
