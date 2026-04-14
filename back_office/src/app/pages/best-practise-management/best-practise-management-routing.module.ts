import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BestPractiseManagementComponent } from './best-practise-management.component';

const routes: Routes = [
  {
    path: '',
    component: BestPractiseManagementComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BestPractiseManagementRoutingModule {}
