import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SubsidiaryPctComponent } from './subsidiary-pct.component';

const routes: Routes = [{ path: '', component: SubsidiaryPctComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SubsidiaryPctRoutingModule { }
