import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MembreAssigneeComponent } from './membre-assignee.component';

const routes: Routes = [
  {
  path: '',
  component: MembreAssigneeComponent,
},];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MembreAssigneeRoutingModule { }
