import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CreatePctComponent } from './create-pct.component';

const routes: Routes = [{ path: '', component: CreatePctComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CreatePctRoutingModule { }
