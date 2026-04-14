import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {PmUserProfileComponent} from "./pm-user-profile.component";

const routes: Routes = [

  {
    path: ':id',
    component: PmUserProfileComponent
  },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PMUserProfileRoutingModule { }
