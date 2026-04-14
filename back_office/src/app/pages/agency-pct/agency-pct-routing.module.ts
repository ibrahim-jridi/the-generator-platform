import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AgencyPctComponent } from './agency-pct.component';

const routes: Routes = [{ path: '', component: AgencyPctComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AgencyPctRoutingModule { }
