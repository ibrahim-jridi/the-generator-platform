import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PhysicalUserProfileComponent } from './physical-user-profile.component';

const routes: Routes = [

    {
      path: ':id',
      component: PhysicalUserProfileComponent
    },

  ];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PhysicalUserProfileRoutingModule { }
