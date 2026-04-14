import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {EctdComponent} from "./ectd.component";

const routes: Routes = [
  {
    path: '',
    component: EctdComponent,
  },
];


@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class EctdRoutingModule {}
