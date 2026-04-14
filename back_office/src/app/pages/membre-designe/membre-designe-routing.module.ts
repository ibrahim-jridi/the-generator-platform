import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {EctdComponent} from "../ectd/ectd.component";
import {MembreDesigneComponent} from "./membre-designe.component";

const routes: Routes = [
  {
    path: '',
    component: MembreDesigneComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MembreDesigneRoutingModule { }
