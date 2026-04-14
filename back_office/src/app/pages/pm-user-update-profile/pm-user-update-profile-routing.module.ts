import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {PmUserUpdateProfileComponent} from "./pm-user-update-profile.component";

const routes: Routes = [

  {
    path: ':id',
    component: PmUserUpdateProfileComponent
  },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PMUserUpdateProfileRoutingModule { }
