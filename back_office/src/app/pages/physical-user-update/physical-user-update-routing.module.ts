import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {PhysicalUserUpdateComponent} from "./physical-user-update.component";

const routes: Routes = [
  {
    path: ':id',
    component: PhysicalUserUpdateComponent,
  }

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PhysicalUserUpdateRoutingModule { }
